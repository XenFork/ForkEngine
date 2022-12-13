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

package forkengine.level.model;

import forkengine.gl.IGL;

import static forkengine.core.ForkEngine.gl;
import static forkengine.gl.GLStateManager.bindVertexArray;

/**
 * The static model, which is built to the vertex buffer.
 *
 * @author squid233
 * @since 0.1.0
 */
public class StaticModel extends Model {
    private final VertexLayout layout;
    private final Type type;
    private final int vao;
    private final int vbo;
    private final int ebo;

    /**
     * Creates the static model with the given vertex layout and render type.
     *
     * @param layout the vertex layout.
     * @param type   the render type.
     * @param mesh   the mesh to be built.
     */
    public StaticModel(VertexLayout layout, Type type, Mesh mesh) {
        this.layout = layout;
        this.type = type;
        vao = gl.genVertexArray();
        vbo = gl.genBuffer();
        ebo = gl.genBuffer();
        bindVertexArray(vao);
    }

    /**
     * Renders the model.
     */
    public void render() {
        bindVertexArray(vao);
        gl.drawElements(type.value(), , IGL.UNSIGNED_INT, 0);
    }

    @Override
    public void close() {
        super.close();
        gl.deleteVertexArray(vao);
        gl.deleteBuffer(vbo);
        gl.deleteBuffer(ebo);
    }
}
