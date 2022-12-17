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

package forkengine.asset.shader;

import forkengine.core.DataBuffer;
import forkengine.util.DataType;
import org.joml.*;

import java.util.Locale;

/**
 * The base shader uniform.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class ShaderUniform implements AutoCloseable {
    private final int location;
    /**
     * The data buffer.
     */
    protected final DataBuffer buffer;
    private final Type type;
    private boolean dirty = false;

    /**
     * Creates the shader uniform.
     *
     * @param location the location of this uniform.
     * @param type     the type.
     */
    public ShaderUniform(int location, Type type) {
        this.location = location;
        this.type = type;
        buffer = DataBuffer.allocate(type.bytesSize());
    }

    /**
     * The shader uniform types.
     *
     * @author squid233
     * @since 0.1.0
     */
    public enum Type {
        /**
         * The single int.
         */
        INT(4, 1, DataType.INT),
        /**
         * The single unsigned int.
         */
        UNSIGNED_INT(4, 1, DataType.UNSIGNED_INT),
        /**
         * The single float.
         */
        FLOAT(4, 1, DataType.FLOAT),
        /**
         * The single double.
         */
        DOUBLE(8, 1, DataType.DOUBLE),
        /**
         * The int vector2.
         */
        IVEC2(8, 2, DataType.INT),
        /**
         * The unsigned int vector2.
         */
        UVEC2(8, 2, DataType.UNSIGNED_INT),
        /**
         * The float vector2.
         */
        VEC2(8, 2, DataType.FLOAT),
        /**
         * The double vector2.
         */
        DVEC2(16, 2, DataType.DOUBLE),
        /**
         * The int vector3.
         */
        IVEC3(12, 3, DataType.INT),
        /**
         * The unsigned int vector3.
         */
        UVEC3(12, 3, DataType.UNSIGNED_INT),
        /**
         * The float vector3.
         */
        VEC3(12, 3, DataType.FLOAT),
        /**
         * The double vector3.
         */
        DVEC3(24, 3, DataType.DOUBLE),
        /**
         * The int vector4.
         */
        IVEC4(16, 4, DataType.INT),
        /**
         * The unsigned int vector4.
         */
        UVEC4(16, 4, DataType.UNSIGNED_INT),
        /**
         * The float vector4.
         */
        VEC4(16, 4, DataType.FLOAT),
        /**
         * The double vector4.
         */
        DVEC4(32, 4, DataType.DOUBLE),
        /**
         * The float 2x2 matrix.
         */
        MAT2(16, 4, DataType.FLOAT),
        /**
         * The float 3x3 matrix.
         */
        MAT3(36, 9, DataType.FLOAT),
        /**
         * The float 4x4 matrix.
         */
        MAT4(64, 16, DataType.FLOAT),
        /**
         * The float 2x3 matrix.
         */
        MAT2X3(24, 6, DataType.FLOAT),
        /**
         * The float 2x4 matrix.
         */
        MAT2X4(32, 8, DataType.FLOAT),
        /**
         * The float 3x2 matrix.
         */
        MAT3X2(24, 6, DataType.FLOAT),
        /**
         * The float 3x4 matrix.
         */
        MAT3X4(48, 12, DataType.FLOAT),
        /**
         * The float 4x2 matrix.
         */
        MAT4X2(32, 8, DataType.FLOAT),
        /**
         * The float 4x3 matrix.
         */
        MAT4X3(48, 12, DataType.FLOAT),
        /**
         * The double 2x2 matrix.
         */
        DMAT2(32, 4, DataType.DOUBLE),
        /**
         * The double 3x3 matrix.
         */
        DMAT3(72, 9, DataType.DOUBLE),
        /**
         * The double 4x4 matrix.
         */
        DMAT4(128, 16, DataType.DOUBLE),
        /**
         * The double 2x3 matrix.
         */
        DMAT2X3(48, 6, DataType.DOUBLE),
        /**
         * The double 2x4 matrix.
         */
        DMAT2X4(64, 8, DataType.DOUBLE),
        /**
         * The double 3x2 matrix.
         */
        DMAT3X2(48, 6, DataType.DOUBLE),
        /**
         * The double 3x4 matrix.
         */
        DMAT3X4(96, 12, DataType.DOUBLE),
        /**
         * The double 4x2 matrix.
         */
        DMAT4X2(64, 8, DataType.DOUBLE),
        /**
         * The double 4x3 matrix.
         */
        DMAT4X3(96, 12, DataType.DOUBLE);

        private final int bytesSize;
        private final int count;
        private final DataType dataType;

        Type(int bytesSize, int count, DataType dataType) {
            this.bytesSize = bytesSize;
            this.count = count;
            this.dataType = dataType;
        }

        /**
         * Gets the uniform type by the given GLSL data type name.
         *
         * @param name the data type name.
         * @return the uniform type.
         */
        public static Type fromName(String name) {
            name = name.toUpperCase(Locale.ROOT);
            return switch (name) {
                case "BOOL" -> INT;
                case "UINT" -> UNSIGNED_INT;
                case "BVEC2" -> IVEC2;
                case "BVEC3" -> IVEC3;
                case "BVEC4" -> IVEC4;
                default -> valueOf(name);
            };
        }

        /**
         * Gets the bytes size of this uniform type.
         *
         * @return the bytes size.
         */
        public int bytesSize() {
            return bytesSize;
        }

        /**
         * Gets the element count of this uniform type.
         *
         * @return the element count.
         */
        public int count() {
            return count;
        }

        /**
         * Gets the data type of this uniform type.
         *
         * @return the data type.
         */
        public DataType dataType() {
            return dataType;
        }
    }

    private void markDirty() {
        dirty = true;
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(int x) {
        buffer.putInt(0, x);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(int x, int y) {
        buffer.putInt(0, x).putInt(4, y);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     */
    public void set(int x, int y, int z) {
        buffer.putInt(0, x).putInt(4, y).putInt(8, z);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     * @param w w.
     */
    public void set(int x, int y, int z, int w) {
        buffer.putInt(0, x).putInt(4, y).putInt(8, z).putInt(12, w);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(boolean x) {
        buffer.putInt(0, x ? 1 : 0);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(boolean x, boolean y) {
        buffer.putInt(0, x ? 1 : 0).putInt(4, y ? 1 : 0);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     */
    public void set(boolean x, boolean y, boolean z) {
        buffer.putInt(0, x ? 1 : 0).putInt(4, y ? 1 : 0).putInt(8, z ? 1 : 0);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     * @param w w.
     */
    public void set(boolean x, boolean y, boolean z, boolean w) {
        buffer.putInt(0, x ? 1 : 0).putInt(4, y ? 1 : 0).putInt(8, z ? 1 : 0).putInt(12, w ? 1 : 0);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(float x) {
        buffer.putFloat(0, x);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(float x, float y) {
        buffer.putFloat(0, x).putFloat(4, y);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     */
    public void set(float x, float y, float z) {
        buffer.putFloat(0, x).putFloat(4, y).putFloat(8, z);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     * @param w w.
     */
    public void set(float x, float y, float z, float w) {
        buffer.putFloat(0, x).putFloat(4, y).putFloat(8, z).putFloat(12, w);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(double x) {
        buffer.putDouble(0, x);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(double x, double y) {
        buffer.putDouble(0, x).putDouble(4, y);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     */
    public void set(double x, double y, double z) {
        buffer.putDouble(0, x).putDouble(4, y).putDouble(8, z);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     * @param z z.
     * @param w w.
     */
    public void set(double x, double y, double z, double w) {
        buffer.putDouble(0, x).putDouble(4, y).putDouble(8, z).putDouble(12, w);
        markDirty();
    }

    /**
     * Sets the values.
     *
     * @param values the values.
     */
    public void set(int[] values) {
        for (int i = 0; i < values.length; i++) {
            buffer.putInt(i * 4L, values[i]);
        }
        markDirty();
    }

    /**
     * Sets the values.
     *
     * @param values the values.
     */
    public void set(boolean[] values) {
        for (int i = 0; i < values.length; i++) {
            buffer.putInt(i * 4L, values[i] ? 1 : 0);
        }
        markDirty();
    }

    /**
     * Sets the values.
     *
     * @param values the values.
     */
    public void set(float[] values) {
        for (int i = 0; i < values.length; i++) {
            buffer.putFloat(i * 4L, values[i]);
        }
        markDirty();
    }

    /**
     * Sets the values.
     *
     * @param values the values.
     */
    public void set(double[] values) {
        for (int i = 0; i < values.length; i++) {
            buffer.putDouble(i * 8L, values[i]);
        }
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector2ic value) {
        set(value.x(), value.y());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector3ic value) {
        set(value.x(), value.y(), value.z());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector4ic value) {
        set(value.x(), value.y(), value.z(), value.w());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector2fc value) {
        set(value.x(), value.y());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector3fc value) {
        set(value.x(), value.y(), value.z());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector4fc value) {
        set(value.x(), value.y(), value.z(), value.w());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector2dc value) {
        set(value.x(), value.y());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector3dc value) {
        set(value.x(), value.y(), value.z());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Vector4dc value) {
        set(value.x(), value.y(), value.z(), value.w());
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix2fc value) {
        buffer.putFloat(0, value.m00())
            .putFloat(4, value.m01())
            .putFloat(8, value.m10())
            .putFloat(12, value.m11());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix2dc value) {
        buffer.putDouble(0, value.m00())
            .putDouble(8, value.m01())
            .putDouble(16, value.m10())
            .putDouble(24, value.m11());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3fc value) {
        buffer.putFloat(0, value.m00())
            .putFloat(4, value.m01())
            .putFloat(8, value.m02())
            .putFloat(12, value.m10())
            .putFloat(16, value.m11())
            .putFloat(20, value.m12())
            .putFloat(24, value.m20())
            .putFloat(28, value.m21())
            .putFloat(32, value.m22());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3dc value) {
        buffer.putDouble(0, value.m00())
            .putDouble(8, value.m01())
            .putDouble(16, value.m02())
            .putDouble(24, value.m10())
            .putDouble(32, value.m11())
            .putDouble(40, value.m12())
            .putDouble(48, value.m20())
            .putDouble(56, value.m21())
            .putDouble(64, value.m22());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4fc value) {
        buffer.putFloat(0, value.m00())
            .putFloat(4, value.m01())
            .putFloat(8, value.m02())
            .putFloat(12, value.m03())
            .putFloat(16, value.m10())
            .putFloat(20, value.m11())
            .putFloat(24, value.m12())
            .putFloat(28, value.m13())
            .putFloat(32, value.m20())
            .putFloat(36, value.m21())
            .putFloat(40, value.m22())
            .putFloat(44, value.m23())
            .putFloat(48, value.m30())
            .putFloat(52, value.m31())
            .putFloat(56, value.m32())
            .putFloat(60, value.m33());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4dc value) {
        buffer.putDouble(0, value.m00())
            .putDouble(8, value.m01())
            .putDouble(16, value.m02())
            .putDouble(24, value.m03())
            .putDouble(32, value.m10())
            .putDouble(40, value.m11())
            .putDouble(48, value.m12())
            .putDouble(56, value.m13())
            .putDouble(64, value.m20())
            .putDouble(72, value.m21())
            .putDouble(80, value.m22())
            .putDouble(88, value.m23())
            .putDouble(96, value.m30())
            .putDouble(104, value.m31())
            .putDouble(112, value.m32())
            .putDouble(120, value.m33());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3x2fc value) {
        buffer.putFloat(0, value.m00())
            .putFloat(4, value.m01())
            .putFloat(8, value.m10())
            .putFloat(12, value.m11())
            .putFloat(16, value.m20())
            .putFloat(20, value.m21());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3x2dc value) {
        buffer.putDouble(0, value.m00())
            .putDouble(8, value.m01())
            .putDouble(16, value.m10())
            .putDouble(24, value.m11())
            .putDouble(32, value.m20())
            .putDouble(40, value.m21());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4x3fc value) {
        buffer.putFloat(0, value.m00())
            .putFloat(4, value.m01())
            .putFloat(8, value.m02())
            .putFloat(12, value.m10())
            .putFloat(16, value.m11())
            .putFloat(20, value.m12())
            .putFloat(24, value.m20())
            .putFloat(28, value.m21())
            .putFloat(32, value.m22())
            .putFloat(36, value.m30())
            .putFloat(40, value.m31())
            .putFloat(44, value.m32());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4x3dc value) {
        buffer.putDouble(0, value.m00())
            .putDouble(8, value.m01())
            .putDouble(16, value.m02())
            .putDouble(24, value.m10())
            .putDouble(32, value.m11())
            .putDouble(40, value.m12())
            .putDouble(48, value.m20())
            .putDouble(56, value.m21())
            .putDouble(64, value.m22())
            .putDouble(72, value.m30())
            .putDouble(80, value.m31())
            .putDouble(88, value.m32());
        markDirty();
    }

    /**
     * Uploads this uniform.
     *
     * @param program the shader program id.
     */
    protected abstract void internalUpload(int program);

    /**
     * Uploads this uniform.
     *
     * @param program the shader program id.
     */
    public void upload(int program) {
        if (!dirty) {
            return;
        }
        dirty = false;
        internalUpload(program);
    }

    /**
     * Gets the location of this uniform.
     *
     * @return the location.
     */
    public int location() {
        return location;
    }

    /**
     * Gets the type of this uniform.
     *
     * @return the type.
     */
    public Type type() {
        return type;
    }

    @Override
    public void close() {
        buffer.free();
    }
}
