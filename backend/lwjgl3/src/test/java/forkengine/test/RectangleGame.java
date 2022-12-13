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

import forkengine.asset.shader.Shader;
import forkengine.asset.shader.ShaderUniform;
import forkengine.backend.lwjgl3.LWJGL3App;
import forkengine.core.Game;
import forkengine.core.GameCreateInfo;
import forkengine.gui.screen.ScreenUtil;
import forkengine.level.LinearTransformation;
import forkengine.level.model.Model;
import forkengine.level.model.StaticModel;
import forkengine.level.model.VertexElement;
import forkengine.level.model.VertexLayout;
import forkengine.util.DataType;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30C.*;

/**
 * Tests rectangle
 *
 * @author squid233
 * @since 0.1.0
 */
public final class RectangleGame extends Game {
    private Shader shader;
    private StaticModel model;
    private final LinearTransformation transformation = new LinearTransformation();
    private final Matrix4f modelMat = new Matrix4f();
    private int vao, vbo;

    @Override
    public void init() {
        super.init();
        shader = Shader.create();
        Shader.Builder vsh = shader.attach(Shader.VERTEX_SHADER)
            .source("""
                #version 150 core
                in vec2 Position;
                in vec3 Color;
                out vec4 vertexColor;
                uniform mat4 ModelMat;
                void main() {
                    gl_Position = ModelMat * vec4(Position, 0.0, 1.0);
                    vertexColor = vec4(Color, 1.0);
                }
                """)
            .compileThrowLog();
        Shader.Builder fsh = shader.attach(Shader.FRAGMENT_SHADER)
            .source("""
                #version 150 core
                in vec4 vertexColor;
                out vec4 FragColor;
                void main() {
                    FragColor = vertexColor;
                }
                """)
            .compileThrowLog();

        VertexElement positionElement = VertexElement.builder(DataType.FLOAT, 2)
            .name("Position")
            .index(0)
            .build(VertexElement.Putter.VEC2);
        VertexElement colorElement = VertexElement.builder(DataType.FLOAT, 3)
            .name("Color")
            .index(1)
            .build(VertexElement.Putter.VEC3);
        VertexLayout.Interleaved layout = VertexLayout.interleaved(positionElement, colorElement);

        shader.bindLayout(layout).linkThrow(shader::getInfoLog);
        shader.detach(vsh).close();
        shader.detach(fsh).close();

        shader.createUniform("ModelMat", ShaderUniform.Type.MAT4);

        model = Model.rectangle()
            .buildStatic(layout);

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, new float[]{
            0.0f, 0.5f, 1.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f,
        }, GL_STATIC_DRAW);
        glBindVertexArray(vao);
        layout.pointerAll();
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        layout.enableAll();
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
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        super.render();
    }

    @Override
    public void exit() {
        shader.close();
        model.close();
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        super.exit();
    }

    public static void main(String[] args) {
        new RectangleGame().run(new GameCreateInfo()
                .width(800)
                .height(640)
                .title("Rectangle Game"),
            LWJGL3App.getInstance());
    }
}
