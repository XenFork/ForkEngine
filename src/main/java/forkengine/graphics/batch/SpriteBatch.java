/*
 * MIT License
 *
 * Copyright (c) 2022 XenFork Union
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package forkengine.graphics.batch;

import forkengine.asset.shader.Shader;
import forkengine.asset.texture.Texture2D;
import forkengine.core.ForkEngine;
import forkengine.gl.GLStateManager;
import forkengine.gl.IGL;
import forkengine.graphics.Color;
import forkengine.graphics.Sprite;
import forkengine.graphics.model.DynamicModel;
import forkengine.graphics.model.Model;
import forkengine.graphics.model.VertexElement;
import forkengine.graphics.model.VertexLayout;
import org.joml.Matrix4f;

import static forkengine.gl.GLStateManager.*;

/**
 * The sprite batch.
 *
 * @author squid233
 * @since 0.1.0
 */
public class SpriteBatch implements Batch {
    private static final VertexLayout LAYOUT = VertexLayout.interleaved(
        VertexElement.position2(0),
        VertexElement.colorPacked(1),
        VertexElement.texCoord2(2, 0)
    );
    private DynamicModel model;

    private final float[] vertices;
    private int index = 0;
    private Texture2D lastTexture = null;
    private float invTexWidth = 0.0f, invTexHeight = 0.0f;

    private boolean drawing = false;

    private final Matrix4f modelMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f combinedMatrix = new Matrix4f();

    private boolean disabledBlend = false;
    private int blendSrcFunc = IGL.SRC_ALPHA;
    private int blendDstFunc = IGL.ONE_MINUS_SRC_ALPHA;
    private int blendSrcFuncAlpha = IGL.SRC_ALPHA;
    private int blendDstFuncAlpha = IGL.ONE_MINUS_SRC_ALPHA;

    private final Shader shader;
    private Shader customShader;
    private boolean ownsShader;

    private final Color color = Color.WHITE;
    private float colorPacked = Color.WHITE_PACKED;

    /**
     * Number of render calls since the last {@link #begin()}.
     **/
    public int renderCalls = 0;
    /**
     * Number of rendering calls, ever. Will not be reset unless set manually.
     **/
    public int totalRenderCalls = 0;
    /**
     * The maximum number of sprites rendered in one batch so far.
     **/
    public int maxSpritesInBatch = 0;

    /**
     * Constructs a new {@code SpriteBatch} with a size of 1000, one buffer, and the default shader.
     *
     * @see #SpriteBatch(int, Shader)
     */
    public SpriteBatch() {
        this(1000, null);
    }

    /**
     * Constructs a {@code SpriteBatch} with one buffer and the default shader.
     *
     * @see #SpriteBatch(int, Shader)
     */
    public SpriteBatch(int size) {
        this(size, null);
    }

    public SpriteBatch(int size, Shader defaultShader) {
        // 0x7FFFFFFF is max vertex index, so 0x7FFFFFFF / 4 vertices per sprite = 536870911 sprites max.
        if (size > (Integer.MAX_VALUE / 4)) {
            throw new IllegalArgumentException("Can't have more than 536870911 sprites per batch: " + size);
        }

        model = new DynamicModel(LAYOUT, Model.Type.TRIANGLES, size * 4, size * 6);

        projectionMatrix.setOrtho2D(0, ForkEngine.appAdapter.width(), 0, ForkEngine.appAdapter.height());

        vertices = new float[size * Sprite.SPRITE_SIZE];

        int[] indices = new int[size * 6];
        int j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i] = j;
            indices[i + 1] = j + 1;
            indices[i + 2] = j + 2;
            indices[i + 3] = j + 2;
            indices[i + 4] = j + 3;
            indices[i + 5] = j;
        }
        model.setIndices(indices);

        if (defaultShader == null) {
            shader = createDefaultShader();
            ownsShader = true;
        } else {
            shader = defaultShader;
        }
    }

    /**
     * Returns a new instance of the default shader used by {@code SpriteBatch} for GL when no shader is specified.
     */
    public static Shader createDefaultShader() {
        return Shader.loadCustom(
            String.format("""
                    #version 150 core
                    in vec2 %1$s;
                    in vec4 %2$s;
                    in vec2 %3$s0;
                    out vec4 vertexColor;
                    out vec2 UV0;
                    uniform mat4 u_ProjModelMat;
                    void main() {
                        gl_Position = u_ProjModelMat * vec4(%1$s, 0.0, 1.0);
                        vertexColor = %2$s;
                        vertexColor.a = vertexColor.a * (255.0 / 254.0);
                        UV0 = %3$s0;
                    }
                    """,
                Shader.POSITION_ATTRIBUTE,
                Shader.COLOR_ATTRIBUTE,
                Shader.TEX_COORD_ATTRIBUTE),
            """
                #version 150 core
                in vec4 vertexColor;
                in vec2 UV0;
                out vec4 FragColor;
                uniform sampler2D u_Sampler0;
                void main() {
                    FragColor = vertexColor * texture(u_Sampler0, UV0);
                }""",
            LAYOUT);
    }

    @Override
    public void begin() {
        if (drawing) {
            throw new IllegalStateException("SpriteBatch.end must be called before begin.");
        }
        renderCalls = 0;

        ForkEngine.gl.depthMask(false);
        if (customShader != null) {
            customShader.use();
        } else {
            shader.use();
        }
        setupMatrices();

        drawing = true;
    }

    @Override
    public void end() {
        if (!drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before end.");
        }
        if (index > 0) {
            flush();
        }
        lastTexture = null;
        drawing = false;

        ForkEngine.gl.depthMask(true);
        if (isBlendEnabled()) {
            disableBlend();
        }
    }

    @Override
    public void flush() {
        if (index == 0) {
            return;
        }

        renderCalls++;
        totalRenderCalls++;
        int spritesInBatch = index / 20;
        if (spritesInBatch > maxSpritesInBatch) {
            maxSpritesInBatch = spritesInBatch;
        }
        int count = spritesInBatch * 6;

        lastTexture.bind(0);
        model.setVertices(model.vertexCount(), vertices);
        model.indexCountLimit(count);

        if (disabledBlend) {
            disableBlend();
        } else {
            enableBlend();
            if (blendSrcFunc != -1) {
                blendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
            }
        }

        if (customShader != null) {
            customShader.use();
        } else {
            shader.use();
        }
        model.render();
        Shader.useProgram(0);

        index = 0;
    }

    private void setupMatrices() {
        combinedMatrix.set(projectionMatrix).mul(modelMatrix);
        if (customShader != null) {
        } else {
            // STOPSHIP: 2022/12/17
        }
    }

    @Override
    public void close() {
        model.close();
        if (ownsShader && shader != null) {
            shader.close();
        }
    }
}
