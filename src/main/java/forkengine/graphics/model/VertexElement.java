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

import forkengine.asset.shader.Shader;
import forkengine.core.DataBuffer;
import forkengine.core.ForkEngine;
import forkengine.graphics.Color;
import forkengine.util.DataType;
import forkengine.util.MathHelper;
import org.joml.Vector3fc;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * The vertex element.
 * <h2>Builtin Elements</h2>
 * Builtin elements are used for easily creating vertex layouts. The available builtin elements are:
 * <ul>
 *     <li>{@link #position3(int) fe_Position}, or {@link #position2(int) 2-Dimension version}</li>
 *     <li>{@link #color(int) fe_Color}, or {@link #colorNormalized(int) normalized version}, or {@link #colorPacked(int) packed version}</li>
 *     <li>{@link #texCoord2(int, int) fe_TexCoord}, or {@link #texCoord3(int, int) 3-Dimension version},
 *     requires a number at the end, i.e. {@code fe_TexCoord0}, {@code fe_TexCoord1}, etc.</li>
 *     <li>{@link #normal(int) fe_Normal}, or {@link #normalNormalized(int) normalized version}</li>
 * </ul>
 * <h2>Value-Based</h2>
 * This class is immutable and value-based.
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
     * Creates a 2D position element.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement position2(int index) {
        return new VertexElement(Putter.VEC2, index, Shader.POSITION_ATTRIBUTE, DataType.FLOAT, 2, false);
    }

    /**
     * Creates a 3D position element.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement position3(int index) {
        return new VertexElement(Putter.VEC3, index, Shader.POSITION_ATTRIBUTE, DataType.FLOAT, 3, false);
    }

    /**
     * Creates a color element.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement color(int index) {
        return new VertexElement(Putter.COLOR3, index, Shader.COLOR_ATTRIBUTE, DataType.UNSIGNED_BYTE, 3, true);
    }

    /**
     * Creates a color element, which all components are normalized.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement colorNormalized(int index) {
        return new VertexElement(Putter.VEC3, index, Shader.COLOR_ATTRIBUTE, DataType.FLOAT, 3, false);
    }

    /**
     * Creates a 4D color element, which source contains one {@link Color#pack packed} float.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement colorPacked(int index) {
        return new VertexElement(Putter.COLOR_PACKED4, index, Shader.COLOR_ATTRIBUTE, DataType.UNSIGNED_BYTE, 4, true);
    }

    /**
     * Creates a 2D texture coordinate element.
     *
     * @param index  the index of the element.
     * @param number the number of the texture coordinate.
     * @return the element.
     */
    public static VertexElement texCoord2(int index, int number) {
        return new VertexElement(Putter.VEC2, index, Shader.TEX_COORD_ATTRIBUTE + number, DataType.FLOAT, 2, false);
    }

    /**
     * Creates a 3D texture coordinate element.
     *
     * @param index  the index of the element.
     * @param number the number of the texture coordinate.
     * @return the element.
     */
    public static VertexElement texCoord3(int index, int number) {
        return new VertexElement(Putter.VEC3, index, Shader.TEX_COORD_ATTRIBUTE + number, DataType.FLOAT, 3, false);
    }

    /**
     * Creates a vertex normal element.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement normal(int index) {
        return new VertexElement(Putter.NORMAL, index, Shader.NORMAL_ATTRIBUTE, DataType.BYTE, 3, true);
    }

    /**
     * Creates a vertex normal element, which all components are normalized.
     *
     * @param index the index of the element.
     * @return the element.
     */
    public static VertexElement normalNormalized(int index) {
        return new VertexElement(Putter.VEC3, index, Shader.NORMAL_ATTRIBUTE, DataType.FLOAT, 3, false);
    }

    /**
     * The vertex element putter.
     *
     * @author squid233
     * @since 0.1.0
     */
    public interface Putter {
        /**
         * The float vector putter.
         */
        Putter VEC2 = new Putter() {
            @Override
            public void accept(DataBuffer buffer, Vector3fc vertex) {
                buffer.putFloat(vertex.x()).putFloat(vertex.y());
            }

            @Override
            public void accept(DataBuffer buffer, float value) {
                buffer.putFloat(value);
            }

            @Override
            public int floatPutCount() {
                return 2;
            }
        },
            VEC3 = new Putter() {
                @Override
                public void accept(DataBuffer buffer, Vector3fc vertex) {
                    buffer.putFloat(vertex.x()).putFloat(vertex.y()).putFloat(vertex.z());
                }

                @Override
                public void accept(DataBuffer buffer, float value) {
                    buffer.putFloat(value);
                }

                @Override
                public int floatPutCount() {
                    return 3;
                }
            };
        /**
         * The color putter with 3 components.
         */
        Putter COLOR3 = new Putter() {
            @Override
            public void accept(DataBuffer buffer, Vector3fc vertex) {
                buffer.putByte(Color.floatToByte(vertex.x()))
                    .putByte(Color.floatToByte(vertex.y()))
                    .putByte(Color.floatToByte(vertex.z()));
            }

            @Override
            public void accept(DataBuffer buffer, float value) {
                buffer.putByte(Color.floatToByte(value));
            }

            @Override
            public int floatPutCount() {
                return 3;
            }
        };
        /**
         * The color putter with a packed float, which is ordered in ABGR.
         */
        Putter COLOR_PACKED4 = new Putter() {
            @Override
            public void accept(DataBuffer buffer, Vector3fc vertex) {
                accept(buffer, vertex.x());
            }

            @Override
            public void accept(DataBuffer buffer, float value) {
                int bits = Float.floatToRawIntBits(value);
                buffer.putByte(Color.getRedComponent(bits))
                    .putByte(Color.getGreenComponent(bits))
                    .putByte(Color.getBlueComponent(bits))
                    .putByte(Color.getAlphaComponent(bits));
            }

            @Override
            public int floatPutCount() {
                return 1;
            }
        };
        /**
         * The vertex normal putter.
         */
        Putter NORMAL = new Putter() {
            @Override
            public void accept(DataBuffer buffer, Vector3fc vertex) {
                buffer.putByte(MathHelper.normalFloatToByte(vertex.x()))
                    .putByte(MathHelper.normalFloatToByte(vertex.y()))
                    .putByte(MathHelper.normalFloatToByte(vertex.z()));
            }

            @Override
            public void accept(DataBuffer buffer, float value) {
                buffer.putByte(MathHelper.normalFloatToByte(value));
            }

            @Override
            public int floatPutCount() {
                return 3;
            }
        };

        /**
         * Processes a vertex.
         *
         * @param buffer the data buffer.
         * @param vertex the vertex.
         */
        void accept(DataBuffer buffer, Vector3fc vertex);

        /**
         * Processes a float value.
         *
         * @param buffer the data buffer.
         * @param value  the value.
         */
        void accept(DataBuffer buffer, float value);

        /**
         * Gets the required putting float count.
         *
         * @return the putted float count.
         */
        int floatPutCount();
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
