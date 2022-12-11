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

package forkengine.util;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * The data layout.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class DataLayout {
    /**
     * The single byte layout.
     */
    public static final DataLayout BYTE = new DataLayout(DataType.BYTE, 1);
    /**
     * The single unsigned byte layout.
     */
    public static final DataLayout UNSIGNED_BYTE = new DataLayout(DataType.UNSIGNED_BYTE, 1);
    /**
     * The single short layout.
     */
    public static final DataLayout SHORT = new DataLayout(DataType.SHORT, 1);
    /**
     * The single unsigned short layout.
     */
    public static final DataLayout UNSIGNED_SHORT = new DataLayout(DataType.UNSIGNED_SHORT, 1);
    /**
     * The single int.
     */
    public static final DataLayout INT = new DataLayout(DataType.INT, 1);
    /**
     * The single unsigned int.
     */
    public static final DataLayout UNSIGNED_INT = new DataLayout(DataType.UNSIGNED_INT, 1);
    /**
     * The single float.
     */
    public static final DataLayout FLOAT = new DataLayout(DataType.FLOAT, 1);
    /**
     * The single double.
     */
    public static final DataLayout DOUBLE = new DataLayout(DataType.DOUBLE, 1);
    /**
     * The int vector2.
     */
    public static final DataLayout IVEC2 = new DataLayout(DataType.INT, 2);
    /**
     * The unsigned int vector2.
     */
    public static final DataLayout UVEC2 = new DataLayout(DataType.UNSIGNED_INT, 2);
    /**
     * The float vector2.
     */
    public static final DataLayout VEC2 = new DataLayout(DataType.FLOAT, 2);
    /**
     * The double vector2.
     */
    public static final DataLayout DVEC2 = new DataLayout(DataType.DOUBLE, 2);
    /**
     * The int vector3.
     */
    public static final DataLayout IVEC3 = new DataLayout(DataType.INT, 3);
    /**
     * The unsigned int vector3.
     */
    public static final DataLayout UVEC3 = new DataLayout(DataType.UNSIGNED_INT, 3);
    /**
     * The float vector3.
     */
    public static final DataLayout VEC3 = new DataLayout(DataType.FLOAT, 3);
    /**
     * The double vector3.
     */
    public static final DataLayout DVEC3 = new DataLayout(DataType.DOUBLE, 3);
    /**
     * The int vector4.
     */
    public static final DataLayout IVEC4 = new DataLayout(DataType.INT, 4);
    /**
     * The unsigned int vector4.
     */
    public static final DataLayout UVEC4 = new DataLayout(DataType.UNSIGNED_INT, 4);
    /**
     * The float vector4.
     */
    public static final DataLayout VEC4 = new DataLayout(DataType.FLOAT, 4);
    /**
     * The double vector4.
     */
    public static final DataLayout DVEC4 = new DataLayout(DataType.DOUBLE, 4);
    /**
     * The matrix with 4 floats.
     */
    public static final DataLayout MAT4 = new DataLayout(DataType.FLOAT, 4);
    /**
     * The matrix with 6 floats.
     */
    public static final DataLayout MAT6 = new DataLayout(DataType.FLOAT, 6);
    /**
     * The matrix with 8 floats.
     */
    public static final DataLayout MAT8 = new DataLayout(DataType.FLOAT, 8);
    /**
     * The matrix with 9 floats.
     */
    public static final DataLayout MAT9 = new DataLayout(DataType.FLOAT, 9);
    /**
     * The matrix with 12 floats.
     */
    public static final DataLayout MAT12 = new DataLayout(DataType.FLOAT, 12);
    /**
     * The matrix with 16 floats.
     */
    public static final DataLayout MAT16 = new DataLayout(DataType.FLOAT, 16);
    /**
     * The matrix with 4 doubles.
     */
    public static final DataLayout DMAT4 = new DataLayout(DataType.DOUBLE, 4);
    /**
     * The matrix with 6 doubles.
     */
    public static final DataLayout DMAT6 = new DataLayout(DataType.DOUBLE, 6);
    /**
     * The matrix with 8 doubles.
     */
    public static final DataLayout DMAT8 = new DataLayout(DataType.DOUBLE, 8);
    /**
     * The matrix with 9 doubles.
     */
    public static final DataLayout DMAT9 = new DataLayout(DataType.DOUBLE, 9);
    /**
     * The matrix with 12 doubles.
     */
    public static final DataLayout DMAT12 = new DataLayout(DataType.DOUBLE, 12);
    /**
     * The matrix with 16 doubles.
     */
    public static final DataLayout DMAT16 = new DataLayout(DataType.DOUBLE, 16);

    private final DataType dataType;
    private final int count;
    private final int bytesSize;

    /**
     * Creates the data layout.
     *
     * @param dataType the data type.
     * @param count    the data element count.
     */
    public DataLayout(DataType dataType, int count) {
        this.dataType = dataType;
        this.count = count;
        bytesSize = dataType.bytesSize() * count;
    }

    /**
     * Gets the bytes per data element.
     *
     * @return the bytes per data element.
     */
    public int sizeof() {
        return dataType.bytesSize();
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
     * Gets the data element count.
     *
     * @return the data element count.
     */
    public int count() {
        return count;
    }

    /**
     * Gets the total bytes size.
     *
     * @return the total bytes size.
     */
    public int bytesSize() {
        return bytesSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataLayout that = (DataLayout) o;
        return count == that.count && bytesSize == that.bytesSize && dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, count, bytesSize);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataLayout.class.getSimpleName() + "[", "]")
            .add("dataType=" + dataType)
            .add("count=" + count)
            .add("bytesSize=" + bytesSize)
            .toString();
    }
}
