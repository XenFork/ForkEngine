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

package forkengine.test;

import forkengine.asset.FileProvider;
import forkengine.asset.shader.Shader;
import forkengine.asset.texture.Texture;
import forkengine.asset.texture.Texture2D;
import forkengine.asset.texture.TextureData;
import forkengine.backend.lwjgl3.LWJGL3App;
import forkengine.core.AppConfig;
import forkengine.core.Game;
import forkengine.gl.IGL;
import forkengine.gui.screen.ScreenUtil;
import forkengine.level.LinearTransformation;
import forkengine.level.model.Model;
import forkengine.level.model.StaticModel;
import forkengine.level.model.VertexElement;
import forkengine.level.model.VertexLayout;
import forkengine.level.scene.GameObject;
import org.joml.Matrix4f;

/**
 * Tests basic features
 *
 * @author squid233
 * @since 0.1.0
 */
public final class DukeGame extends Game {
    private Shader shader;
    private StaticModel model;
    private Texture2D texture;
    private final GameObject duke = new GameObject();
    private final LinearTransformation transformation = new LinearTransformation();
    private final Matrix4f modelMat = new Matrix4f();

    @Override
    public void init() {
        super.init();

        ScreenUtil.clearColor(0.4f, 0.6f, 0.9f, 1.0f);

        VertexElement positionElement = VertexElement.position(0);
        VertexElement colorElement = VertexElement.colorNormalized(1);
        VertexElement texCoordElement = VertexElement.texCoord2(2, 0);
        VertexLayout layout = VertexLayout.interleaved(positionElement, colorElement, texCoordElement);

        shader = Shader.loadJson(FileProvider.internal("shader/position_color_texture.json"), layout);

        model = new StaticModel(layout, Model.Type.POLYGON, 4,
            new float[]{
                -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f
            },
            new int[]{0, 1, 2, 0, 2, 3});

        texture = Texture2D.create();
        texture.bind(0);
        texture.setParameter(Texture.MAG_FILTER, IGL.LINEAR)
            .setParameter(Texture.MIN_FILTER, IGL.LINEAR_MIPMAP_LINEAR);
        try (TextureData data = TextureData.create().load(FileProvider.CLASSPATH, "texture/duke.png", 4096)) {
            texture.specifyImage(4, data);
        }
        texture.generateMipmap();
        Texture2D.bind(0, 0);
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        transformation.rotate().rotateZ((float) delta);
        transformation.applyMatrix(modelMat.identity());
    }

    @Override
    public void render() {
        ScreenUtil.clear(ScreenUtil.COLOR_BUFFER_BIT | ScreenUtil.DEPTH_BUFFER_BIT);
        shader.use();
        shader.getUniform("u_ModelMatrix").orElseThrow().set(modelMat);
        shader.uploadUniforms();
        texture.bind(0);
        model.render();
        Texture2D.bind(0, 0);
        Shader.useProgram(0);
        super.render();
    }

    @Override
    public void exit() throws Exception {
        dispose(shader);
        dispose(model);
        dispose(texture);
        super.exit();
    }

    public static void main(String[] args) {
        new DukeGame().run(new AppConfig()
                .width(800)
                .height(640)
                .title("Duke Game"),
            LWJGL3App.getInstance());
    }
}
