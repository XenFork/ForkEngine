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

import forkengine.core.DataBuffer;
import forkengine.gl.IGL;

import static forkengine.core.ForkEngine.gl;
import static forkengine.gl.GLStateManager.bindVertexArray;

/**
 * The dynamic model, which can be modified at any frame.
 *
 * @author squid233
 * @since 0.1.0
 */
public class DynamicModel extends BufferModel {
    private int vertexCount;
    private int indexCount;

    /**
     * Creates a dynamic model with the given initial buffer sizes.
     *
     * @param layout             the vertex layout. all elements are float type.
     * @param type               the render type.
     * @param initialVertexCount the initial vertex count.
     * @param initialIndexCount  the initial index count.
     */
    public DynamicModel(VertexLayout layout, Type type,
                        int initialVertexCount,
                        int initialIndexCount) {
        super(layout, type, gl.genVertexArray(), gl.genBuffer(), gl.genBuffer());
        this.vertexCount = initialVertexCount;
        this.indexCount = initialIndexCount;

        // Build GL buffer
        bindVertexArray(vao);

        gl.bindBuffer(IGL.ARRAY_BUFFER, vbo);
        gl.bufferData(IGL.ARRAY_BUFFER, vbo, initialVertexCount * layout.floatPutCount() * 4L, 0, IGL.DYNAMIC_DRAW);
        // Enable vertex attributes
        switch (layout) {
            case VertexLayout.Flat flat -> flatLayoutVA(vertexCount);
            case VertexLayout.Interleaved interleaved -> {
                long pointer = 0;
                for (VertexElement element : interleaved.getElements()) {
                    interleaved.enable(element);
                    interleaved.pointer(element, interleaved.elementBytesSize(), pointer);
                    pointer += element.bytesSize();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + layout);
        }
        gl.bindBuffer(IGL.ARRAY_BUFFER, 0);

        gl.bindBuffer(IGL.ELEMENT_ARRAY_BUFFER, ebo);
        gl.bufferData(IGL.ELEMENT_ARRAY_BUFFER, ebo, initialIndexCount * 4L, 0, IGL.DYNAMIC_DRAW);

        bindVertexArray(0);
    }

    private void flatLayoutVA(int vertexCount) {
        long pointer = 0;
        for (VertexElement element : layout.getElements()) {
            layout.enable(element);
            layout.pointer(element, pointer);
            pointer += (long) vertexCount * element.bytesSize();
        }
    }

    /**
     * Sets the vertices with the given vertex count.
     *
     * @param newVertexCount the new vertex count. defaults calculated with the length of <i>{@code vertices}</i>.
     * @param vertices       the new vertices.
     */
    public void setVertices(int newVertexCount, float[] vertices) {
        newVertexCount = Math.max(newVertexCount, vertices.length * 4 / layout().elementBytesSize());
        DataBuffer buffer = DataBuffer.allocate( newVertexCount * layout().floatPutCount() * 4L);
        for (int i = 0; i < vertices.length; ) {
            for (VertexElement element : layout().getElements()) {
                VertexElement.Putter putter = element.putter();
                for (int j = 0; j < putter.floatPutCount(); j++) {
                    putter.accept(buffer, vertices[i]);
                    i++;
                }
            }
        }
        buffer.position(0);
        gl.bindBuffer(IGL.ARRAY_BUFFER, vbo);
        if (newVertexCount > vertexCount) {
            gl.bufferData(IGL.ARRAY_BUFFER, vbo, buffer.capacity(), buffer.address(), IGL.DYNAMIC_DRAW);
        } else {
            gl.bufferSubData(IGL.ARRAY_BUFFER, vbo, 0, buffer.capacity(), buffer.address());
        }
        if (layout() instanceof VertexLayout.Flat) {
            gl.bindVertexArray(vao);
            flatLayoutVA(newVertexCount);
            gl.bindVertexArray(0);
        }
        gl.bindBuffer(IGL.ARRAY_BUFFER, 0);
        buffer.free();
        vertexCount = newVertexCount;
    }

    /**
     * Sets the vertices.
     *
     * @param vertices the new vertices.
     */
    public void setVertices(float[] vertices) {
        setVertices(-1, vertices);
    }

    /**
     * Sets the indices.
     *
     * @param indices the new indices.
     */
    public void setIndices(int[] indices) {
        DataBuffer buffer = DataBuffer.allocate(indices.length * 4L);
        for (int i = 0; i < indices.length; i++) {
            buffer.putInt(i * 4L, indices[i]);
        }
        gl.bindBuffer(IGL.ELEMENT_ARRAY_BUFFER, ebo);
        if (indices.length > indexCount) {
            gl.bufferData(IGL.ELEMENT_ARRAY_BUFFER, ebo, buffer.capacity(), buffer.address(), IGL.DYNAMIC_DRAW);
        } else {
            gl.bufferSubData(IGL.ELEMENT_ARRAY_BUFFER, ebo, 0, buffer.capacity(), buffer.address());
        }
        gl.bindBuffer(IGL.ELEMENT_ARRAY_BUFFER, 0);
        buffer.free();
        indexCount = indices.length;
    }

    /**
     * Sets a limit for the index count.
     *
     * @param indexCount new index count.
     */
    public void indexCountLimit(int indexCount) {
        this.indexCount = indexCount;
    }

    @Override
    public int vertexCount() {
        return vertexCount;
    }

    @Override
    public int indexCount() {
        return indexCount;
    }
}
