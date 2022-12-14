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

import forkengine.core.DataBuffer;
import forkengine.gl.IGL;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static forkengine.core.ForkEngine.application;
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
    private final int vertexCount;
    private final int indexCount;

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

        // Build vertices
        Map<VertexElement, List<Vector3fc>> vertices = new HashMap<>();
        List<Integer> indices = new ArrayList<>();
        final var elements = mesh.getElements();
        boolean indicesFrozen = false;
        int vertCount = 0;
        int stride = 0;
        // TODO: 2022/12/14 Points and Lines type
        switch (type) {
            case POINTS -> {
            }
            case LINES -> {
            }
            case TRIANGLES -> {
                for (VertexElement element : elements) {
                    stride += element.bytesSize();
                    var list = vertices.computeIfAbsent(element, e -> new ArrayList<>());
                    var meshVertices = mesh.getVertices(element);
                    for (Vector3fc vertex : meshVertices) {
                        // Finds index
                        int index = getVertexIndex(list, vertex);
                        if (index == -1) {
                            // index not found
                            list.add(vertex);
                            if (!indicesFrozen) {
                                indices.add(vertices.size() - 1);
                            }
                        } else if (!indicesFrozen) {
                            indices.add(index);
                        }
                    }
                    vertCount = list.size();
                    indicesFrozen = true;
                }
            }
            case POLYGON -> {
                for (VertexElement element : elements) {
                    stride += element.bytesSize();
                    var list = vertices.computeIfAbsent(element, e -> new ArrayList<>());
                    var meshVertices = mesh.getVertices(element);
                    vertCount = meshVertices.size();
                    list.addAll(meshVertices);
                    if (!indicesFrozen) {
                        for (int i = 0; i < vertCount - 2; i++) {
                            indices.add(0);
                            indices.add(i + 1);
                            indices.add(i + 2);
                        }
                    }
                    indicesFrozen = true;
                }
            }
        }

        vertexCount = vertCount;
        indexCount = indices.size();

        // Build GL buffer
        vao = gl.genVertexArray();
        vbo = gl.genBuffer();
        ebo = gl.genBuffer();
        bindVertexArray(vao);
        gl.bindBuffer(IGL.ARRAY_BUFFER, vbo);
        DataBuffer buffer = application.allocateNative((long) vertexCount * stride);
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
        application.freeNative(buffer);
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
        DataBuffer indexBuffer = application.allocateNative(indexCount * 4L);
        for (int i = 0; i < indexCount; i++) {
            indexBuffer.putInt(i * 4L, indices.get(i));
        }
        gl.bufferData(IGL.ELEMENT_ARRAY_BUFFER, ebo, indexBuffer.capacity(), indexBuffer.address(), IGL.STATIC_DRAW);
        application.freeNative(indexBuffer);

        bindVertexArray(0);
    }

    /**
     * Renders the model.
     */
    public void render() {
        bindVertexArray(vao);
        gl.drawElements(type.value(), indexCount(), IGL.UNSIGNED_INT, 0);
        bindVertexArray(0);
    }

    public int vertexCount() {
        return vertexCount;
    }

    public int indexCount() {
        return indexCount;
    }

    @Override
    public void close() {
        super.close();
        gl.deleteVertexArray(vao);
        gl.deleteBuffer(vbo);
        gl.deleteBuffer(ebo);
    }
}
