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
import forkengine.camera.OrthographicCamera;
import forkengine.core.AppConfig;
import forkengine.core.Game;
import forkengine.core.Window;
import forkengine.gl.GLStateManager;
import forkengine.gl.IGL;
import forkengine.graphics.Color;
import forkengine.graphics.model.Model;
import forkengine.graphics.model.StaticModel;
import forkengine.graphics.model.VertexElement;
import forkengine.graphics.model.VertexLayout;
import forkengine.gui.screen.ScreenUtil;
import forkengine.scene.GameObject;
import org.joml.Matrix4f;

/**
 * Tests basic features
 *
 * @author squid233
 * @since 0.1.0
 */
public final class DukeGame extends Game {
    private Shader shader;
    private Model model;
    private Texture2D texture;
    private Texture2D redBulletTex;
    private Texture2D greenBulletTex;
    private Texture2D blueBulletTex;
    private final GameObject duke = new GameObject();
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Matrix4f projViewMat = new Matrix4f();
    private final Matrix4f modelMat = new Matrix4f();

    private static Texture2D loadTexture(String path, int bufferSize, int levels) {
        Texture2D texture = Texture2D.create();
        texture.bind(0);
        texture.setParameter(Texture.MAG_FILTER, IGL.LINEAR)
            .setParameter(Texture.MIN_FILTER, IGL.LINEAR_MIPMAP_LINEAR);
        try (TextureData data = TextureData.internal(path, bufferSize)) {
            texture.specifyImage(levels, data);
        }
        texture.generateMipmap();
        return texture;
    }

    @Override
    public void init() {
        super.init();

        GLStateManager.enableBlend();
        GLStateManager.blendFunc(IGL.SRC_ALPHA, IGL.ONE_MINUS_SRC_ALPHA);

        ScreenUtil.clearColor(0.4f, 0.6f, 0.9f, 1.0f);

        VertexElement positionElement = VertexElement.position(0);
        VertexElement colorElement = VertexElement.colorPacked(1);
        VertexElement texCoordElement = VertexElement.texCoord2(2, 0);
        VertexLayout layout = VertexLayout.interleaved(positionElement, colorElement, texCoordElement);

        shader = Shader.loadJson(FileProvider.internal("shader/position_color_tex.json"), layout);

        model = new StaticModel(layout, Model.Type.POLYGON, 4,
            new float[]{
                0.0f, 96.0f, 0.0f, Color.pack(1.0f, 1.0f, 1.0f), 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, Color.pack(1.0f, 1.0f, 1.0f), 0.0f, 1.0f,
                72.0f, 0.0f, 0.0f, Color.pack(1.0f, 1.0f, 1.0f), 1.0f, 1.0f,
                72.0f, 96.0f, 0.0f, Color.pack(1.0f, 1.0f, 1.0f), 1.0f, 0.0f
            },
            new int[]{0, 1, 2, 0, 2, 3});

        texture = loadTexture("texture/duke.png", 4096, 4);
        redBulletTex = loadTexture("texture/red_bullet.png", 6144, 8);
        greenBulletTex = loadTexture("texture/green_bullet.png", 3072, 8);
        blueBulletTex = loadTexture("texture/blue_bullet.png", 4096, 8);
        Texture2D.bind(0, 0);

        window.setInputMode(Window.CURSOR, Window.CURSOR_HIDDEN);
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        super.onResize(oldWidth, oldHeight, newWidth, newHeight);
        camera.setSymmetric(newWidth, newHeight, -1, 1);
    }

    @Override
    public void onCursorPos(double oldX, double oldY, double newX, double newY) {
        super.onCursorPos(oldX, oldY, newX, newY);
        duke.position().set(newX - texture.width() * 0.5f - width * 0.5f,
            (height - newY) - texture.height() * 0.5f - height * 0.5f,
            0.0f);
    }

    @Override
    public void lateUpdate(double delta) {
        super.lateUpdate(delta);
        camera.applyMatrix(projViewMat.identity());
    }

    @Override
    public void render() {
        ScreenUtil.clear(ScreenUtil.COLOR_BUFFER_BIT | ScreenUtil.DEPTH_BUFFER_BIT);
        shader.use();
        shader.setUniform("u_ProjectionViewMatrix", projViewMat);
        shader.setUniform("u_ModelMatrix", modelMat.translation(duke.position()));
        shader.uploadUniforms();
        texture.bind(0);
        model.render();
        Texture2D.bind(0, 0);
        Shader.useProgram(0);
        super.render();
    }

    @Override
    public void exit() throws Exception {
        dispose(shader, model, texture, redBulletTex, greenBulletTex, blueBulletTex);
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
