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
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The base model.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Model implements AutoCloseable {
    /**
     * The model render type.
     *
     * @author squid233
     * @since 0.1.0
     */
    public enum Type {
        /**
         * The points render.
         */
        POINTS(IGL.POINTS),
        /**
         * The lines render.
         */
        LINES(IGL.LINES),
        /**
         * The triangles render.
         */
        TRIANGLES(IGL.TRIANGLES),
        /**
         * The polygon render.
         */
        POLYGON(IGL.TRIANGLES);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        /**
         * Gets the enum value of this type.
         *
         * @return the enum value.
         */
        public int value() {
            return value;
        }
    }

    /**
     * The model builder with single mesh.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static class SingleBuilder {
        private final Mesh mesh = new Mesh();

        /**
         * Adds a vertex to the given element.
         *
         * @param element the vertex element.
         * @param vec     the vertex.
         * @return this.
         */
        public SingleBuilder addVertex(VertexElement element, Vector3fc vec) {
            mesh.addVertex(element, vec);
            return this;
        }

        /**
         * Builds a static model.
         *
         * @param layout          the layout of the model.
         * @param positionElement the position element in the layout.
         * @param type            the render type.
         * @return the static model.
         */
        public StaticModel buildStatic(VertexLayout layout, VertexElement positionElement, Type type) {
            return new StaticModel(layout, positionElement, type, mesh);
        }
    }

    /**
     * The model builder with multi mesh.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static class MultiBuilder {
        private final List<Mesh> meshes = new ArrayList<>();

        /**
         * Adds a mesh to this builder.
         *
         * @param mesh the mesh.
         * @return this.
         */
        public MultiBuilder addMesh(Mesh mesh) {
            meshes.add(mesh);
            return this;
        }

        /**
         * Creates an empty mesh, performs the given action and adds the mesh to this builder.
         *
         * @param consumer the action to be performed.
         * @return this.
         */
        public MultiBuilder createMesh(Consumer<Mesh> consumer) {
            Mesh mesh = new Mesh();
            consumer.accept(mesh);
            return addMesh(mesh);
        }

        /**
         * Builds a static model.
         *
         * @param layout          the layout of the model.
         * @param positionElement the position element in the layout.
         * @param type            the render type.
         * @return the static model.
         */
        public StaticModel buildStatic(VertexLayout layout, VertexElement positionElement, Type type) {
            return new StaticModel(layout, positionElement, type, meshes.toArray(new Mesh[0]));
        }
    }

    /**
     * Finds the index of the given vertex in the list, or -1 if not found.
     *
     * @param list   the vertex list.
     * @param vertex the vertex.
     * @return the index, or -1 if not found.
     */
    public static int getVertexIndex(List<Vector3fc> list, Vector3fc vertex) {
        final float x = vertex.x();
        final float y = vertex.y();
        final float z = vertex.z();
        for (int i = 0, len = list.size(); i < len; i++) {
            Vector3fc v = list.get(i);
            if (v.equals(x, y, z)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Creates the model builder with single mesh.
     *
     * @return the model builder.
     */
    public static SingleBuilder single() {
        return new SingleBuilder();
    }

    /**
     * Creates the model builder with multi mesh.
     *
     * @return the model builder.
     */
    public static MultiBuilder multi() {
        return new MultiBuilder();
    }

    @Override
    public void close() {
    }
}
