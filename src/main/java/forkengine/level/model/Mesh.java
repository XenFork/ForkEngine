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

import org.joml.Vector3fc;

import java.util.*;

/**
 * The mesh.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Mesh {
    private final Map<VertexElement, List<Vector3fc>> vertices = new HashMap<>();

    /**
     * Creates an empty mesh.
     */
    public Mesh() {
    }

    /**
     * Adds a vertex to the given element.
     *
     * @param element the vertex element.
     * @param vertex  the vertex.
     * @return this.
     */
    public Mesh addVertex(VertexElement element, Vector3fc vertex) {
        vertices.computeIfAbsent(element, e -> new ArrayList<>()).add(vertex);
        return this;
    }

    /**
     * Gets the vertices with the given element.
     *
     * @param element the vertex elements.
     * @return the vertices.
     */
    public List<Vector3fc> getVertices(VertexElement element) {
        return vertices.get(element);
    }

    /**
     * Gets the vertex elements in this mesh.
     *
     * @return the vertex elements.
     */
    public Set<VertexElement> getElements() {
        return vertices.keySet();
    }

    /**
     * Checks the vertex elements of this mesh are all in the given layout.
     *
     * @param layout the layout.
     */
    public void checkLayout(Set<VertexElement> layout) {
        getElements().forEach(element -> {
            if (!layout.contains(element)) {
                throw new IllegalStateException("Vertex element not found in the given layout! got: " + element + ", layout: " + layout);
            }
        });
    }
}
