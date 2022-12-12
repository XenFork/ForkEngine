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

import forkengine.util.DataType;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * The vertex element.
 *
 * @author squid233
 * @since 0.1.0
 */
public class VertexElement {
    private final int index;
    private final String name;
    private final DataType dataType;
    private final int count;

    private VertexElement(int index, String name, DataType dataType, int count) {
        this.index = index;
        this.name = name;
        this.dataType = dataType;
        this.count = count;
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
         * Builds this vertex element.
         *
         * @return the vertex element.
         */
        public VertexElement build() {
            Objects.requireNonNull(dataType, "required a data type");
            return new VertexElement(index, name, dataType, count);
        }
    }

    /**
     * Creates a new vertex element with the given index.
     *
     * @param index the new index.
     * @return the new vertex element.
     */
    public VertexElement withIndex(int index) {
        return new VertexElement(index, name, dataType, count);
    }

    /**
     * Creates a new vertex element with the given name.
     *
     * @param name the new name.
     * @return the new vertex element.
     */
    public VertexElement withName(String name) {
        return new VertexElement(index, name, dataType, count);
    }

    /**
     * Creates a new vertex element with the given data type.
     *
     * @param dataType the new data type.
     * @return the new vertex element.
     */
    public VertexElement withDataType(DataType dataType) {
        return new VertexElement(index, name, dataType, count);
    }

    /**
     * Creates a new vertex element with the given count.
     *
     * @param count the new count.
     * @return the new vertex element.
     */
    public VertexElement withCount(int count) {
        return new VertexElement(index, name, dataType, count);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexElement that = (VertexElement) o;
        return count == that.count && Objects.equals(name, that.name) && dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataType, count);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VertexElement.class.getSimpleName() + "[", "]")
            .add("index=" + index)
            .add("name='" + name + "'")
            .add("dataType=" + dataType)
            .add("count=" + count)
            .toString();
    }
}
