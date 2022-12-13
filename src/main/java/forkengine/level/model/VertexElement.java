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
import forkengine.core.ForkEngine;
import forkengine.util.DataType;
import org.joml.Vector3fc;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

/**
 * The vertex element.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class VertexElement {
    private final Putter putter;
    private final int index;
    private final String name;
    private final DataType dataType;
    private final int count;
    private final boolean normalized;
    private final int bytesSize;

    private VertexElement(Putter putter, int index, String name, DataType dataType, int count, boolean normalized) {
        this.putter = putter;
        this.index = index;
        this.name = name;
        this.dataType = dataType;
        this.count = count;
        this.normalized = normalized;
        bytesSize = dataType.bytesSize() * count;
    }

    /**
     * Creates a vertex element builder.
     *
     * @return the builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a vertex element builder with the given data type and count.
     *
     * @param dataType the initial data type.
     * @param count    the initial count.
     * @return the builder.
     */
    public static Builder builder(DataType dataType, int count) {
        return new Builder().dataType(dataType).count(count);
    }

    /**
     * The vertex element putter.
     *
     * @author squid233
     * @since 0.1.0
     */
    @FunctionalInterface
    public interface Putter extends BiConsumer<DataBuffer, Vector3fc> {
        /**
         * The float vector putter.
         */
        Putter VEC2 =
            (buffer, vector3fc) -> buffer.putFloat(buffer.position(), vector3fc.x()).putFloat(buffer.position() + 4, vector3fc.y()),
            VEC3 = VEC2.andThen((buffer, vector3fc) -> buffer.putFloat(buffer.position() + 8, vector3fc.z()));

        /**
         * Returns a composed {@code Putter} that performs, in sequence, this operation followed by the after operation.
         *
         * @param after the operation to perform after this operation.
         * @return a composed {@code Putter} that performs in sequence this operation followed by the {@code after} operation.
         */
        default Putter andThen(Putter after) {
            return (buffer, vector3fc) -> {
                accept(buffer, vector3fc);
                after.accept(buffer, vector3fc);
            };
        }
    }

    /**
     * The vertex element builder.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static class Builder {
        private int index = -1;
        private String name = "";
        private DataType dataType;
        private int count = 1;
        private boolean normalized = false;

        /**
         * Sets the index.
         *
         * @param index the new index.
         * @return this.
         */
        public Builder index(int index) {
            this.index = index;
            return this;
        }

        /**
         * Sets the name.
         *
         * @param name the new name.
         * @return this.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the dataType.
         *
         * @param dataType the new dataType.
         * @return this.
         */
        public Builder dataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        /**
         * Sets the count.
         *
         * @param count the new count.
         * @return this.
         */
        public Builder count(int count) {
            this.count = count;
            return this;
        }

        /**
         * Sets the value of normalized.
         *
         * @param normalized the new value of normalized.
         * @return this.
         */
        public Builder normalized(boolean normalized) {
            this.normalized = normalized;
            return this;
        }

        /**
         * Builds this vertex element.
         *
         * @param putter the putter to put the vector to the data buffer.
         * @return the vertex element.
         */
        public VertexElement build(Putter putter) {
            Objects.requireNonNull(dataType, "required a data type");
            return new VertexElement(putter, index, name, dataType, count, normalized);
        }
    }

    /**
     * Specifies the location and organization of a vertex attribute array.
     *
     * @param index   the index of the generic vertex attribute to be modified.
     * @param stride  the byte offset between consecutive generic vertex attributes. If stride is 0, the generic vertex attributes
     *                are understood to be tightly packed in the array. The initial value is 0.
     * @param pointer the vertex attribute data or the offset of the first component of the first generic vertex attribute
     *                in the array in the data store of the buffer currently bound to the ARRAY_BUFFER target. The initial value is 0.
     */
    public void pointer(int index, int stride, long pointer) {
        ForkEngine.gl.vertexAttribArrayPointer(index, count(), dataType().value(), normalized(), stride, pointer);
    }

    /**
     * Creates a new vertex element with the given putter.
     *
     * @param putter the new putter.
     * @return the new vertex element.
     */
    public VertexElement withPutter(Putter putter) {
        return new VertexElement(putter, index, name, dataType, count, normalized);
    }

    /**
     * Creates a new vertex element with the given index.
     *
     * @param index the new index.
     * @return the new vertex element.
     */
    public VertexElement withIndex(int index) {
        return new VertexElement(putter, index, name, dataType, count, normalized);
    }

    /**
     * Creates a new vertex element with the given name.
     *
     * @param name the new name.
     * @return the new vertex element.
     */
    public VertexElement withName(String name) {
        return new VertexElement(putter, index, name, dataType, count, normalized);
    }

    /**
     * Creates a new vertex element with the given data type.
     *
     * @param dataType the new data type.
     * @return the new vertex element.
     */
    public VertexElement withDataType(DataType dataType) {
        return new VertexElement(putter, index, name, dataType, count, normalized);
    }

    /**
     * Creates a new vertex element with the given count.
     *
     * @param count the new count.
     * @return the new vertex element.
     */
    public VertexElement withCount(int count) {
        return new VertexElement(putter, index, name, dataType, count, normalized);
    }

    /**
     * Creates a new vertex element with the given value of normalized.
     *
     * @param normalized the new value of normalized.
     * @return the new vertex element.
     */
    public VertexElement withNormalized(boolean normalized) {
        return new VertexElement(putter, index, name, dataType, count, normalized);
    }

    /**
     * Gets the putter.
     *
     * @return the putter.
     */
    public Putter putter() {
        return putter;
    }

    /**
     * Gets the index.
     *
     * @return the index.
     */
    public int index() {
        return index;
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public String name() {
        return name;
    }

    /**
     * Gets the data type.
     *
     * @return the data type.
     */
    public DataType dataType() {
        return dataType;
    }

    /**
     * Gets the count.
     *
     * @return the count.
     */
    public int count() {
        return count;
    }

    /**
     * Gets the value of normalized.
     *
     * @return the value of normalized.
     */
    public boolean normalized() {
        return normalized;
    }

    /**
     * Gets the bytes size.
     *
     * @return the bytes size.
     */
    public int bytesSize() {
        return bytesSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexElement that = (VertexElement) o;
        return index == that.index && count == that.count && normalized == that.normalized && Objects.equals(name, that.name) && dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name, dataType, count, normalized);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VertexElement.class.getSimpleName() + "[", "]")
            .add("index=" + index)
            .add("name='" + name + "'")
            .add("dataType=" + dataType)
            .add("count=" + count)
            .add("normalized=" + normalized)
            .add("bytesSize=" + bytesSize)
            .toString();
    }
}
