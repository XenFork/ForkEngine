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
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static forkengine.core.ForkEngine.gl;
import static forkengine.gl.GLStateManager.bindVertexArray;

/**
 * The static model, which is built to the vertex buffer.
 *
 * @author squid233
 * @since 0.1.0
 */
public class StaticModel extends BufferModel {
    private final int vertexCount;
    private final int indexCount;

    /**
     * Creates the static model with the given vertices.
     *
     * @param layout      the vertex layout. all elements are float type.
     * @param type        the render type.
     * @param vertexCount the vertex count.
     * @param vertices    the vertices.
     * @param indices     the indices.
     */
    public StaticModel(VertexLayout layout, Type type,
                       int vertexCount,
                       float[] vertices, int[] indices) {
        super(layout, type, gl.genVertexArray(), gl.genBuffer(), gl.genBuffer());
        this.vertexCount = vertexCount;
        this.indexCount = indices.length;

        final var elements = layout.getElements();

        // Build GL buffer
        bindVertexArray(vao);

        gl.bindBuffer(IGL.ARRAY_BUFFER, vbo);
        DataBuffer buffer = DataBuffer.allocate(vertexCount * layout.floatPutCount() * 4L);
        for (int i = 0; i < vertices.length; ) {
            for (VertexElement element : elements) {
                VertexElement.Putter putter = element.putter();
                for (int j = 0; j < putter.floatPutCount(); j++) {
                    putter.accept(buffer, vertices[i]);
                    i++;
                }
            }
        }
        buffer.position(0);
        gl.bufferData(IGL.ARRAY_BUFFER, vbo, buffer.capacity(), buffer.address(), IGL.STATIC_DRAW);
        buffer.free();
        // Enable vertex attributes
        switch (layout) {
            case VertexLayout.Flat flat -> {
                long pointer = 0;
                for (VertexElement element : elements) {
                    flat.enable(element);
                    flat.pointer(element, pointer);
                    pointer += (long) vertexCount * element.bytesSize();
                }
            }
            case VertexLayout.Interleaved interleaved -> {
                long pointer = 0;
                for (VertexElement element : elements) {
                    interleaved.enable(element);
                    interleaved.pointer(element, interleaved.elementBytesSize(), pointer);
                    pointer += element.bytesSize();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + layout);
        }
        gl.bindBuffer(IGL.ARRAY_BUFFER, 0);

        gl.bindBuffer(IGL.ELEMENT_ARRAY_BUFFER, ebo);
        DataBuffer indexBuffer = DataBuffer.allocate(indices.length * 4L);
        for (int i = 0; i < indices.length; i++) {
            indexBuffer.putInt(i * 4L, indices[i]);
        }
        gl.bufferData(IGL.ELEMENT_ARRAY_BUFFER, ebo, indexBuffer.capacity(), indexBuffer.address(), IGL.STATIC_DRAW);
        indexBuffer.free();

        bindVertexArray(0);
    }

    /**
     * Creates the static model with the given vertex layout and render type.
     *
     * @param layout          the vertex layout.
     * @param positionElement the position element in the layout.
     * @param type            the render type.
     * @param meshes          the meshes to be built.
     */
    public StaticModel(VertexLayout layout, VertexElement positionElement,
                       Type type, Mesh... meshes) {
        super(layout, type, gl.genVertexArray(), gl.genBuffer(), gl.genBuffer());

        final var elements = layout.getElements();
        for (Mesh mesh : meshes) {
            mesh.checkLayout(elements);
        }

        // Build vertices
        Map<VertexElement, List<Vector3fc>> vertices = new HashMap<>();
        List<Integer> indices = new ArrayList<>();
        int vertCount = 0;
        int stride = 0;
        switch (type) {
            case POINTS, LINES, TRIANGLES -> {
                for (VertexElement element : elements) {
                    boolean isPosElem = element.equals(positionElement);
                    stride += element.bytesSize();
                    var list = vertices.computeIfAbsent(element, e -> new ArrayList<>());
                    for (Mesh mesh : meshes) {
                        var meshVertices = mesh.getVertices(element);
                        for (Vector3fc vertex : meshVertices) {
                            // Finds index
                            int index = getVertexIndex(list, vertex);
                            if (index == -1) {
                                // index not found
                                list.add(vertex);
                                if (isPosElem) {
                                    indices.add(list.size() - 1);
                                }
                            } else if (isPosElem) {
                                indices.add(index);
                            }
                        }
                    }
                    vertCount = list.size();
                }
            }
            case POLYGON -> {
                for (VertexElement element : elements) {
                    boolean isPosElem = element.equals(positionElement);
                    stride += element.bytesSize();
                    var list = vertices.computeIfAbsent(element, e -> new ArrayList<>());
                    int baseIndex = 0;
                    for (Mesh mesh : meshes) {
                        var meshVertices = mesh.getVertices(element);
                        int vertSz = meshVertices.size();
                        list.addAll(meshVertices);
                        if (isPosElem) {
                            vertCount += vertSz;
                            for (int i = 0; i < vertSz - 2; i++) {
                                indices.add(baseIndex);
                                indices.add(baseIndex + i + 1);
                                indices.add(baseIndex + i + 2);
                            }
                            baseIndex += vertSz;
                        }
                    }
                }
            }
        }

        vertexCount = vertCount;
        indexCount = indices.size();

        // Build GL buffer
        bindVertexArray(vao);

        gl.bindBuffer(IGL.ARRAY_BUFFER, vbo);
        DataBuffer buffer = DataBuffer.allocate((long) vertexCount * stride);
        switch (layout) {
            case VertexLayout.Flat flat -> {
                for (VertexElement element : elements) {
                    for (Vector3fc vertex : vertices.get(element)) {
                        element.putter().accept(buffer, vertex);
                    }
                }
            }
            case VertexLayout.Interleaved interleaved -> {
                for (int i = 0; i < vertexCount; i++) {
                    for (VertexElement element : elements) {
                        element.putter().accept(buffer, vertices.get(element).get(i));
                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + layout);
        }
        // Resets position
        buffer.position(0);
        gl.bufferData(IGL.ARRAY_BUFFER, vbo, buffer.capacity(), buffer.address(), IGL.STATIC_DRAW);
        buffer.free();
        switch (layout) {
            case VertexLayout.Flat flat -> {
                long pointer = 0;
                for (VertexElement element : elements) {
                    flat.enable(element);
                    flat.pointer(element, pointer);
                    pointer += (long) vertexCount * element.bytesSize();
                }
            }
            case VertexLayout.Interleaved interleaved -> {
                long pointer = 0;
                for (VertexElement element : elements) {
                    interleaved.enable(element);
                    interleaved.pointer(element, stride, pointer);
                    pointer += element.bytesSize();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + layout);
        }
        gl.bindBuffer(IGL.ARRAY_BUFFER, 0);

        gl.bindBuffer(IGL.ELEMENT_ARRAY_BUFFER, ebo);
        DataBuffer indexBuffer = DataBuffer.allocate(indexCount * 4L);
        for (int i = 0; i < indexCount; i++) {
            indexBuffer.putInt(i * 4L, indices.get(i));
        }
        gl.bufferData(IGL.ELEMENT_ARRAY_BUFFER, ebo, indexBuffer.capacity(), indexBuffer.address(), IGL.STATIC_DRAW);
        indexBuffer.free();

        bindVertexArray(0);
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
