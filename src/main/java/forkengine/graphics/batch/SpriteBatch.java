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
import forkengine.asset.shader.ShaderUniform;
import forkengine.asset.texture.Texture2D;
import forkengine.core.ForkEngine;
import forkengine.core.ISized;
import forkengine.gl.GLStateManager;
import forkengine.gl.IGL;
import forkengine.graphics.Color;
import forkengine.graphics.Sprite;
import forkengine.graphics.model.DynamicModel;
import forkengine.graphics.model.Model;
import forkengine.graphics.model.VertexElement;
import forkengine.graphics.model.VertexLayout;
import org.joml.Matrix4f;

import static org.joml.Math.*;

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
    private final DynamicModel model;

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
        this(1000);
    }

    /**
     * Constructs a {@code SpriteBatch} with one buffer and the default shader.
     *
     * @see #SpriteBatch(int, Shader)
     */
    public SpriteBatch(int size) {
        this(size, null);
    }

    /**
     * Constructs a new {@code SpriteBatch}. Sets the projection matrix to an orthographic projection with y-axis point upwards,
     * x-axis point to the right and the origin being in the bottom left corner of the screen. The projection will be pixel perfect
     * with respect to the current screen resolution.
     * <p>
     * The {@code defaultShader} specifies the shader to use. Note that the names for uniforms for this default shader
     * are different from the ones expect for shaders set with {@link #setShader(Shader)}. See {@link #createDefaultShader()}.
     *
     * @param size          The max number of sprites in a single batch. Max of 536870911.
     * @param defaultShader The default shader to use. This is not owned by the {@code SpriteBatch} and must be disposed separately.
     */
    public SpriteBatch(int size, Shader defaultShader) {
        // 0x7FFFFFFF is max vertex index, so 0x7FFFFFFF / 4 vertices per sprite = 536870911 sprites max.
        if (size > (Integer.MAX_VALUE / 4)) {
            throw new IllegalArgumentException("Can't have more than 536870911 sprites per batch: " + size);
        }

        model = new DynamicModel(LAYOUT, Model.Type.TRIANGLES, size * 4, size * 6);

        resizeTo(ForkEngine.appAdapter);

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
     *
     * @return the shader.
     */
    public static Shader createDefaultShader() {
        Shader shader = Shader.loadCustom(
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
        shader.createUniform("u_ProjModelMat", ShaderUniform.Type.MAT4);
        shader.createUniform("u_Sampler0", ShaderUniform.Type.INT);
        return shader;
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
            GLStateManager.disableBlend();
        }
    }

    @Override
    public void setColor(Color tint) {
        color.set(tint);
        colorPacked = tint.toFloatBits();
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        color.set(r, g, b, a);
        colorPacked = color.toFloatBits();
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setPackedColor(float packedColor) {
        Color.abgr8888ToColor(color, packedColor);
        this.colorPacked = packedColor;
    }

    @Override
    public float getPackedColor() {
        return colorPacked;
    }

    private void checkDrawing() {
        if (!drawing) {
            throw new IllegalStateException("SpriteBatch.begin must be called before draw.");
        }
    }

    @Override
    public void draw(Texture2D texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
                     float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        checkDrawing();

        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (index == vertices.length)
            flush();

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counterclockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if (rotation != 0) {
            final float sin = sin(toRadians(rotation));
            final float cos = cosFromSin(sin, toRadians(rotation));

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        float u = srcX * invTexWidth;
        float v = (srcY + srcHeight) * invTexHeight;
        float u2 = (srcX + srcWidth) * invTexWidth;
        float v2 = srcY * invTexHeight;

        if (flipX) {
            float tmp = u;
            u = u2;
            u2 = tmp;
        }

        if (flipY) {
            float tmp = v;
            v = v2;
            v2 = tmp;
        }

        float color = this.colorPacked;
        int idx = this.index;
        vertices[idx] = x1;
        vertices[idx + 1] = y1;
        vertices[idx + 2] = color;
        vertices[idx + 3] = u;
        vertices[idx + 4] = v;

        vertices[idx + 5] = x2;
        vertices[idx + 6] = y2;
        vertices[idx + 7] = color;
        vertices[idx + 8] = u;
        vertices[idx + 9] = v2;

        vertices[idx + 10] = x3;
        vertices[idx + 11] = y3;
        vertices[idx + 12] = color;
        vertices[idx + 13] = u2;
        vertices[idx + 14] = v2;

        vertices[idx + 15] = x4;
        vertices[idx + 16] = y4;
        vertices[idx + 17] = color;
        vertices[idx + 18] = u2;
        vertices[idx + 19] = v;
        this.index = idx + 20;
    }

    @Override
    public void draw(Texture2D texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth,
                     int srcHeight, boolean flipX, boolean flipY) {
        checkDrawing();

        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (index == vertices.length)
            flush();

        float u = srcX * invTexWidth;
        float v = (srcY + srcHeight) * invTexHeight;
        float u2 = (srcX + srcWidth) * invTexWidth;
        float v2 = srcY * invTexHeight;
        final float fx2 = x + width;
        final float fy2 = y + height;

        if (flipX) {
            float tmp = u;
            u = u2;
            u2 = tmp;
        }

        if (flipY) {
            float tmp = v;
            v = v2;
            v2 = tmp;
        }

        float color = this.colorPacked;
        int idx = this.index;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = color;
        vertices[idx + 3] = u;
        vertices[idx + 4] = v;

        vertices[idx + 5] = x;
        vertices[idx + 6] = fy2;
        vertices[idx + 7] = color;
        vertices[idx + 8] = u;
        vertices[idx + 9] = v2;

        vertices[idx + 10] = fx2;
        vertices[idx + 11] = fy2;
        vertices[idx + 12] = color;
        vertices[idx + 13] = u2;
        vertices[idx + 14] = v2;

        vertices[idx + 15] = fx2;
        vertices[idx + 16] = y;
        vertices[idx + 17] = color;
        vertices[idx + 18] = u2;
        vertices[idx + 19] = v;
        this.index = idx + 20;
    }

    @Override
    public void draw(Texture2D texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        checkDrawing();

        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (index == vertices.length)
            flush();

        final float u = srcX * invTexWidth;
        final float v = (srcY + srcHeight) * invTexHeight;
        final float u2 = (srcX + srcWidth) * invTexWidth;
        final float v2 = srcY * invTexHeight;
        final float fx2 = x + srcWidth;
        final float fy2 = y + srcHeight;

        float color = this.colorPacked;
        int idx = this.index;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = color;
        vertices[idx + 3] = u;
        vertices[idx + 4] = v;

        vertices[idx + 5] = x;
        vertices[idx + 6] = fy2;
        vertices[idx + 7] = color;
        vertices[idx + 8] = u;
        vertices[idx + 9] = v2;

        vertices[idx + 10] = fx2;
        vertices[idx + 11] = fy2;
        vertices[idx + 12] = color;
        vertices[idx + 13] = u2;
        vertices[idx + 14] = v2;

        vertices[idx + 15] = fx2;
        vertices[idx + 16] = y;
        vertices[idx + 17] = color;
        vertices[idx + 18] = u2;
        vertices[idx + 19] = v;
        this.index = idx + 20;
    }

    @Override
    public void draw(Texture2D texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        checkDrawing();

        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (index == vertices.length)
            flush();

        final float fx2 = x + width;
        final float fy2 = y + height;

        float color = this.colorPacked;
        int idx = this.index;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = color;
        vertices[idx + 3] = u;
        vertices[idx + 4] = v;

        vertices[idx + 5] = x;
        vertices[idx + 6] = fy2;
        vertices[idx + 7] = color;
        vertices[idx + 8] = u;
        vertices[idx + 9] = v2;

        vertices[idx + 10] = fx2;
        vertices[idx + 11] = fy2;
        vertices[idx + 12] = color;
        vertices[idx + 13] = u2;
        vertices[idx + 14] = v2;

        vertices[idx + 15] = fx2;
        vertices[idx + 16] = y;
        vertices[idx + 17] = color;
        vertices[idx + 18] = u2;
        vertices[idx + 19] = v;
        this.index = idx + 20;
    }

    @Override
    public void draw(Texture2D texture, float x, float y) {
        draw(texture, x, y, texture.width(), texture.height());
    }

    @Override
    public void draw(Texture2D texture, float x, float y, float width, float height) {
        checkDrawing();

        float[] vertices = this.vertices;

        if (texture != lastTexture)
            switchTexture(texture);
        else if (index == vertices.length)
            flush();

        final float fx2 = x + width;
        final float fy2 = y + height;
        final float u = 0;
        final float v = 1;
        final float u2 = 1;
        final float v2 = 0;

        float color = this.colorPacked;
        int idx = this.index;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = color;
        vertices[idx + 3] = u;
        vertices[idx + 4] = v;

        vertices[idx + 5] = x;
        vertices[idx + 6] = fy2;
        vertices[idx + 7] = color;
        vertices[idx + 8] = u;
        vertices[idx + 9] = v2;

        vertices[idx + 10] = fx2;
        vertices[idx + 11] = fy2;
        vertices[idx + 12] = color;
        vertices[idx + 13] = u2;
        vertices[idx + 14] = v2;

        vertices[idx + 15] = fx2;
        vertices[idx + 16] = y;
        vertices[idx + 17] = color;
        vertices[idx + 18] = u2;
        vertices[idx + 19] = v;
        this.index = idx + 20;
    }

    @Override
    public void draw(Texture2D texture, float[] spriteVertices, int offset, int count) {
        checkDrawing();

        int verticesLength = vertices.length;
        int remainingVertices = verticesLength;
        if (texture != lastTexture)
            switchTexture(texture);
        else {
            remainingVertices -= index;
            if (remainingVertices == 0) {
                flush();
                remainingVertices = verticesLength;
            }
        }
        int copyCount = Math.min(remainingVertices, count);

        System.arraycopy(spriteVertices, offset, vertices, index, copyCount);
        index += copyCount;
        count -= copyCount;
        while (count > 0) {
            offset += copyCount;
            flush();
            copyCount = Math.min(verticesLength, count);
            System.arraycopy(spriteVertices, offset, vertices, 0, copyCount);
            index += copyCount;
            count -= copyCount;
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
            GLStateManager.disableBlend();
        } else {
            GLStateManager.enableBlend();
            if (blendSrcFunc != -1) {
                GLStateManager.blendFuncSeparate(blendSrcFunc, blendDstFunc, blendSrcFuncAlpha, blendDstFuncAlpha);
            }
        }

        if (customShader != null) customShader.use();
        else shader.use();
        model.render(IGL.TRIANGLES, count);
        Shader.useProgram(0);

        index = 0;
    }

    @Override
    public void disableBlend() {
        if (disabledBlend) return;
        flush();
        disabledBlend = true;
    }

    @Override
    public void enableBlend() {
        if (!disabledBlend) return;
        flush();
        disabledBlend = false;
    }

    @Override
    public void setBlendFunction(int srcFunc, int dstFunc) {
        setBlendFunctionSeparate(srcFunc, dstFunc, srcFunc, dstFunc);
    }

    @Override
    public void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha) {
        if (blendSrcFunc == srcFuncColor &&
            blendDstFunc == dstFuncColor &&
            blendSrcFuncAlpha == srcFuncAlpha &&
            blendDstFuncAlpha == dstFuncAlpha) return;
        flush();
        blendSrcFunc = srcFuncColor;
        blendDstFunc = dstFuncColor;
        blendSrcFuncAlpha = srcFuncAlpha;
        blendDstFuncAlpha = dstFuncAlpha;
    }

    @Override
    public int getBlendSrcFunc() {
        return blendSrcFunc;
    }

    @Override
    public int getBlendDstFunc() {
        return blendDstFunc;
    }

    @Override
    public int getBlendSrcFuncAlpha() {
        return blendSrcFuncAlpha;
    }

    @Override
    public int getBlendDstFuncAlpha() {
        return blendDstFuncAlpha;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    @Override
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    /**
     * Resizes the projection matrix to the given size.
     *
     * @param width  the new width.
     * @param height the new height.
     */
    public void resizeTo(int width, int height) {
        projectionMatrix.setOrtho2D(0, width, 0, height);
    }

    /**
     * Resizes the projection matrix to the given size.
     *
     * @param size the new size.
     */
    public void resizeTo(ISized size) {
        resizeTo(size.width(), size.height());
    }

    @Override
    public void setProjectionMatrix(Matrix4f projection) {
        if (drawing) flush();
        projectionMatrix.set(projection);
        if (drawing) setupMatrices();
    }

    @Override
    public void setModelMatrix(Matrix4f model) {
        if (drawing) flush();
        modelMatrix.set(model);
        if (drawing) setupMatrices();
    }

    private void setupMatrices() {
        combinedMatrix.set(projectionMatrix).mul(modelMatrix);
        if (customShader != null) {
            customShader.setUniform("u_ProjModelMat", combinedMatrix);
            customShader.uploadUniforms();
        } else {
            shader.setUniform("u_ProjModelMat", combinedMatrix);
            shader.uploadUniforms();
        }
    }

    private void switchTexture(Texture2D texture) {
        flush();
        lastTexture = texture;
        invTexWidth = 1.0f / texture.width();
        invTexHeight = 1.0f / texture.height();
    }

    @Override
    public void setShader(Shader shader) {
        // avoid unnecessary flushing in case we are drawing
        if (shader == customShader) return;
        if (drawing) flush();
        customShader = shader;
        if (drawing) {
            if (customShader != null) customShader.use();
            else this.shader.use();
            setupMatrices();
        }
    }

    @Override
    public Shader getShader() {
        return customShader != null ? customShader : shader;
    }

    @Override
    public boolean isBlendEnabled() {
        return !disabledBlend;
    }

    @Override
    public boolean isDrawing() {
        return drawing;
    }

    @Override
    public void close() {
        model.close();
        if (ownsShader && shader != null) {
            shader.close();
        }
    }
}
