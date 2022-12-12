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
import forkengine.backend.lwjgl3.LWJGL3App;
import forkengine.core.Game;
import forkengine.core.GameCreateInfo;
import forkengine.gui.screen.ScreenUtil;
import forkengine.level.model.Model;
import forkengine.level.model.VertexElement;
import forkengine.level.model.VertexLayout;
import forkengine.util.DataType;

import static org.lwjgl.opengl.GL30C.*;

/**
 * Tests rectangle
 *
 * @author squid233
 * @since 0.1.0
 */
public final class RectangleGame extends Game {
    private Shader shader;
    private Model model;
    private int vao, vbo;

    @Override
    public void init() {
        super.init();
        shader = Shader.create();
        Shader.Builder vsh = shader.attach(Shader.VERTEX_SHADER)
            .source("""
                #version 330 core
                layout (location = 0) in vec2 Position;
                layout (location = 1) in vec3 Color;
                out vec4 vertexColor;
                void main() {
                    gl_Position = vec4(Position, 0.0, 1.0);
                    vertexColor = vec4(Color, 1.0);
                }
                """)
            .compileThrowLog();
        Shader.Builder fsh = shader.attach(Shader.FRAGMENT_SHADER)
            .source("""
                #version 330 core
                in vec4 vertexColor;
                out vec4 FragColor;
                void main() {
                    FragColor = vertexColor;
                }
                """)
            .compileThrowLog();
        shader.linkThrow(shader::getInfoLog);
        shader.detach(vsh).close();
        shader.detach(fsh).close();

        VertexElement positionElement = VertexElement.builder(DataType.FLOAT, 2)
            .name("Position")
            .index(0)
            .build();
        VertexElement colorElement = VertexElement.builder(DataType.FLOAT, 3)
            .name("Color")
            .index(1)
            .build();
        VertexLayout layout = VertexLayout.interleaved()
            .addElement(positionElement)
            .addElement(colorElement);

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
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 20, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 20, 8);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void render() {
        ScreenUtil.clear(ScreenUtil.COLOR_BUFFER_BIT | ScreenUtil.DEPTH_BUFFER_BIT);
        shader.use();
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
