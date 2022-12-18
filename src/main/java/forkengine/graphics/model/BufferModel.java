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

package forkengine.graphics.model;

import forkengine.gl.IGL;

import static forkengine.core.ForkEngine.gl;
import static forkengine.gl.GLStateManager.bindVertexArray;

/**
 * The model with GL vertex array and buffers.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class BufferModel extends Model {
    /**
     * The vertex layout.
     */
    protected final VertexLayout layout;
    /**
     * The render type.
     */
    protected final Type type;
    /**
     * The vertex array object.
     */
    protected final int vao;
    /**
     * The vertex buffer.
     */
    protected final int vbo;
    /**
     * The index buffer.
     */
    protected final int ebo;

    /**
     * Creates the buffer model with the given layout, type and GL objects.
     *
     * @param layout the vertex layout.
     * @param type   the render type.
     * @param vao    the vertex array object.
     * @param vbo    the vertex buffer.
     * @param ebo    the index buffer.
     */
    protected BufferModel(VertexLayout layout, Type type,
                          int vao, int vbo, int ebo) {
        this.layout = layout;
        this.type = type;
        this.vao = vao;
        this.vbo = vbo;
        this.ebo = ebo;
    }

    @Override
    public void render(int mode, int indexCount) {
        bindVertexArray(vao);
        gl.drawElements(mode, indexCount, IGL.UNSIGNED_INT, 0);
        bindVertexArray(0);
    }

    @Override
    public void render() {
        render(type.value(), indexCount());
    }

    @Override
    public VertexLayout layout() {
        return layout;
    }

    @Override
    public void close() {
        gl.deleteVertexArray(vao);
        gl.deleteBuffer(vbo);
        gl.deleteBuffer(ebo);
    }
}
