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

import forkengine.asset.AssetFile;
import forkengine.asset.shader.Shader;
import forkengine.asset.shader.ShaderUniform;
import forkengine.backend.lwjgl3.LWJGL3App;
import forkengine.core.AppConfig;
import forkengine.core.Game;
import forkengine.gui.screen.ScreenUtil;
import forkengine.level.LinearTransformation;
import forkengine.level.model.Model;
import forkengine.level.model.StaticModel;
import forkengine.level.model.VertexElement;
import forkengine.level.model.VertexLayout;
import forkengine.level.scene.Actor;
import forkengine.util.DataType;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Tests basic features
 *
 * @author squid233
 * @since 0.1.0
 */
public final class DukeGame extends Game {
    private Shader shader;
    private StaticModel model;
    private final Actor duke = new Actor();
    private final LinearTransformation transformation = new LinearTransformation();
    private final Matrix4f modelMat = new Matrix4f();

    @Override
    public void init() {
        super.init();
        shader = Shader.create();
        Shader.Builder vsh = shader.attach(Shader.VERTEX_SHADER)
            .source(AssetFile.internal("shader/position_color.vert"))
            .compileThrowLog();
        Shader.Builder fsh = shader.attach(Shader.FRAGMENT_SHADER)
            .source(AssetFile.internal("shader/position_color.frag"))
            .compileThrowLog();

        VertexElement positionElement = VertexElement.builder(DataType.FLOAT, 2)
            .name("Position")
            .index(0)
            .build(VertexElement.Putter.VEC2);
        VertexElement colorElement = VertexElement.builder(DataType.FLOAT, 3)
            .name("Color")
            .index(1)
            .build(VertexElement.Putter.VEC3);
        VertexLayout layout = VertexLayout.interleaved(positionElement, colorElement);

        shader.bindLayout(layout).linkThrow(shader::getInfoLog);
        shader.detach(vsh).close();
        shader.detach(fsh).close();

        shader.createUniform("ModelMat", ShaderUniform.Type.MAT4);

        Vector3f v0 = new Vector3f(-0.5f, 0.5f, 0.0f);
        Vector3f v1 = new Vector3f(-0.5f, -0.5f, 0.0f);
        Vector3f v2 = new Vector3f(0.5f, -0.5f, 0.0f);
        Vector3f v3 = new Vector3f(0.5f, 0.5f, 0.0f);
        Vector3f c0 = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f c1 = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f c2 = new Vector3f(0.0f, 0.0f, 1.0f);
        Vector3f c3 = new Vector3f(1.0f, 1.0f, 1.0f);
        model = Model.single()
            .addVertex(positionElement, v0).addVertex(colorElement, c0)
            .addVertex(positionElement, v1).addVertex(colorElement, c1)
            .addVertex(positionElement, v2).addVertex(colorElement, c2)
            .addVertex(positionElement, v3).addVertex(colorElement, c3)
            .buildStatic(layout, positionElement, Model.Type.POLYGON);
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
        shader.getUniform("ModelMat").orElseThrow().set(modelMat);
        shader.uploadUniforms();
        model.render();
        Shader.useProgram(0);
        super.render();
    }

    @Override
    public void exit() {
        shader.close();
        model.close();
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
