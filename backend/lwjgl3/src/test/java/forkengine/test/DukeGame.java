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
import forkengine.backend.lwjgl3.LWJGL3App;
import forkengine.camera.OrthographicCamera;
import forkengine.core.AppConfig;
import forkengine.core.Game;
import forkengine.core.Input;
import forkengine.gl.GLStateManager;
import forkengine.gl.IGL;
import forkengine.graphics.Color;
import forkengine.graphics.batch.SpriteBatch;
import forkengine.graphics.font.BitmapFont;
import forkengine.graphics.model.Model;
import forkengine.graphics.model.StaticModel;
import forkengine.graphics.model.VertexElement;
import forkengine.graphics.model.VertexLayout;
import forkengine.gui.screen.ScreenUtil;
import forkengine.scene.GameObject;
import org.joml.Intersectionf;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests basic features
 *
 * @author squid233
 * @since 0.1.0
 */
public final class DukeGame extends Game {
    private Shader shader;
    private Model model;
    private Texture2D dukeTexture;
    private Texture2D redBulletTex;
    private Texture2D greenBulletTex;
    private Texture2D blueBulletTex;
    private Texture2D coffeeTex;
    private SpriteBatch batch;
    private BitmapFont font;
    private BulletType spawnBulletType = BulletType.GREEN;
    private final GameObject duke = new GameObject();
    private final Coffee coffee = new Coffee();
    private final List<Bullet> bullets = new ArrayList<>();
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Matrix4f projViewMat = new Matrix4f();
    private final Matrix4f modelMat = new Matrix4f();

    public final class Bullet extends GameObject {
        public final BulletType type;
        private final float size, halfSize;

        public Bullet(BulletType type) {
            this.type = type;
            size = switch (type) {
                case RED -> 48;
                case BLUE -> 32;
                case GREEN -> 16;
            };
            halfSize = size * 0.5f;
        }

        public void render(SpriteBatch batch, float partialTick) {
            batch.draw(switch (type) {
                    case RED -> redBulletTex;
                    case BLUE -> blueBulletTex;
                    case GREEN -> greenBulletTex;
                }, Math.lerp(prevPosition().x(), position().x(), partialTick) - halfSize,
                Math.lerp(prevPosition().y(), position().y(), partialTick) - halfSize,
                size, size);
        }
    }

    public final class Coffee extends GameObject {
        public static final float WIDTH = 432;
        public static final float HEIGHT = 405;
        public double health = 512;
        public boolean removed = false;

        public void onDamaged(double amount) {
            health -= amount;
            if (health <= 20.0) {
                removed = true;
            }
        }

        public void render(SpriteBatch batch) {
            float renderSize = (float) health;
            float halfRenderSize = renderSize * 0.5f;
            batch.draw(coffeeTex, position().x() - halfRenderSize, position().y() - halfRenderSize, renderSize, renderSize);
        }
    }

    public enum BulletType {
        RED(20),
        BLUE(15),
        GREEN(10);

        public final double damageAmount;

        BulletType(double damageAmount) {
            this.damageAmount = damageAmount;
        }
    }

    private static Texture2D loadTexture(String path, long bufferSize, int levels) {
        return Texture2D.internal(path, bufferSize, levels, it -> it
            .setParameter(Texture.MAG_FILTER, IGL.LINEAR)
            .setParameter(Texture.MIN_FILTER, IGL.LINEAR_MIPMAP_LINEAR));
    }

    @Override
    public void init() {
        super.init();

        ScreenUtil.clearColor(0.4f, 0.6f, 0.9f, 1.0f);

        VertexElement positionElement = VertexElement.position2(0);
        VertexElement colorElement = VertexElement.colorPacked(1);
        VertexElement texCoordElement = VertexElement.texCoord2(2, 0);
        VertexLayout layout = VertexLayout.interleaved(positionElement, colorElement, texCoordElement);

        shader = Shader.loadJson(FileProvider.internal("shader/position_color_tex.json"), layout);

        model = new StaticModel(layout, Model.Type.POLYGON, 4,
            new float[]{
                -36.0f, 48.0f, Color.pack(1.0f, 1.0f, 1.0f), 0.0f, 0.0f,
                -36.0f, -48.0f, Color.pack(1.0f, 1.0f, 1.0f), 0.0f, 1.0f,
                36.0f, -48.0f, Color.pack(1.0f, 1.0f, 1.0f), 1.0f, 1.0f,
                36.0f, 48.0f, Color.pack(1.0f, 1.0f, 1.0f), 1.0f, 0.0f
            },
            new int[]{0, 1, 2, 0, 2, 3});

        dukeTexture = loadTexture("texture/duke.png", 4096, 4);
        redBulletTex = loadTexture("texture/red_bullet.png", 6144, 8);
        greenBulletTex = loadTexture("texture/green_bullet.png", 3072, 8);
        blueBulletTex = loadTexture("texture/blue_bullet.png", 4096, 8);
        coffeeTex = loadTexture("texture/coffee.png", 29696, 10);
        Texture2D.bind(0, 0);

        coffee.position().set(400, 600, 0);

        batch = new SpriteBatch();

        font = BitmapFont.create();
        Path path = Path.of("test.ttf");
        if (!Files.isReadable(path)) {
            throw new IllegalStateException("To run this test, you need to put a font named 'test.ttf' in the run directory!");
        }
        font.bakeTrueTypeFont(FileProvider.LOCAL, "test.ttf", FileProvider.size(path),
            20.0f, 4096, 4096, 0, 0xFFFF);

        window.setInputMode(Input.CURSOR, Input.CURSOR_HIDDEN);
    }

    private void spawnBullet() {
        Bullet bullet = new Bullet(spawnBulletType);
        Vector3f position = duke.position();
        bullet.position().set(position.x(), position.y() + 48, 0);
        bullet.prevPosition().set(bullet.position());
        bullets.add(bullet);
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        super.onResize(oldWidth, oldHeight, newWidth, newHeight);
        camera.set2D(0, newWidth, 0, newHeight);
        if (batch != null) {
            batch.resizeTo(this);
        }
    }

    @Override
    public void onCursorPos(double oldX, double oldY, double newX, double newY) {
        super.onCursorPos(oldX, oldY, newX, newY);
        duke.position().set(newX, height - newY, 0.0f);
    }

    @Override
    public void onKeyPress(int key, int scancode, int mods) {
        super.onKeyPress(key, scancode, mods);
        if (key == Input.KEY_SPACE) {
            spawnBullet();
        }
    }

    @Override
    public void onKeyReleased(int key, int scancode, int mods) {
        super.onKeyReleased(key, scancode, mods);
        switch (key) {
            case Input.KEY_1 -> spawnBulletType = BulletType.GREEN;
            case Input.KEY_2 -> spawnBulletType = BulletType.BLUE;
            case Input.KEY_3 -> spawnBulletType = BulletType.RED;
        }
    }

    @Override
    public void fixedUpdate(double partialTick) {
        for (var iterator = bullets.iterator(); iterator.hasNext(); ) {
            Bullet bullet = iterator.next();
            Vector3f pos = bullet.position();
            bullet.prevPosition().set(pos);
            pos.y += 3.0f;
            if (pos.y() >= 1200) {
                iterator.remove();
            } else {
                Vector3f coffeePos = coffee.position();
                float halfCoffeeW = (float) (Coffee.WIDTH - (512 - coffee.health)) * 0.5f;
                float halfCoffeeH = (float) (Coffee.HEIGHT - (512 - coffee.health)) * 0.5f;
                if (Intersectionf.testAarCircle(
                    coffeePos.x() - halfCoffeeW,
                    coffeePos.y() - halfCoffeeH,
                    coffeePos.x() + halfCoffeeW,
                    coffeePos.y() + halfCoffeeH,
                    pos.x(),
                    pos.y(),
                    bullet.halfSize * bullet.halfSize
                )) {
                    coffee.onDamaged(bullet.type.damageAmount);
                    iterator.remove();
                }
            }
        }
        super.fixedUpdate(partialTick);
    }

    @Override
    public void lateUpdate(double delta) {
        super.lateUpdate(delta);
        camera.applyMatrix(projViewMat.identity());
    }

    @Override
    public void render(double partialTick) {
        ScreenUtil.clear(ScreenUtil.COLOR_BUFFER_BIT | ScreenUtil.DEPTH_BUFFER_BIT);

        GLStateManager.enableBlend();
        GLStateManager.blendFunc(IGL.SRC_ALPHA, IGL.ONE_MINUS_SRC_ALPHA);
        shader.use();
        shader.setUniform("u_ProjectionViewMatrix", projViewMat);
        shader.setUniform("u_ModelMatrix", modelMat.translation(duke.position()));
        shader.uploadUniforms();
        dukeTexture.bind(0);
        model.render();
        Texture2D.bind(0, 0);
        Shader.useProgram(0);

        batch.begin();
        for (Bullet bullet : bullets) {
            bullet.render(batch, (float) partialTick);
        }
        if (!coffee.removed) {
            coffee.render(batch);
        }
        font.draw(batch, "The quick brown fox jumps over the lazy dog", 100, 100);
        batch.end();

        super.render(partialTick);
    }

    @Override
    public void exit() throws Exception {
        dispose(shader, model, dukeTexture, redBulletTex, greenBulletTex, blueBulletTex, batch, font);
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
