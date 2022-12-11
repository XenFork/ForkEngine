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
import forkengine.core.ForkEngine;
import org.joml.*;

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
        buffer = ForkEngine.application.allocateNative(type.bytesSize());
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
        INT(4, 1),
        /**
         * The single unsigned int.
         */
        UNSIGNED_INT(4, 1),
        /**
         * The single float.
         */
        FLOAT(4, 1),
        /**
         * The single double.
         */
        DOUBLE(8, 1),
        /**
         * The int vector2.
         */
        IVEC2(8, 2),
        /**
         * The unsigned int vector2.
         */
        UVEC2(8, 2),
        /**
         * The float vector2.
         */
        VEC2(8, 2),
        /**
         * The double vector2.
         */
        DVEC2(16, 2),
        /**
         * The int vector3.
         */
        IVEC3(12, 3),
        /**
         * The unsigned int vector3.
         */
        UVEC3(12, 3),
        /**
         * The float vector3.
         */
        VEC3(12, 3),
        /**
         * The double vector3.
         */
        DVEC3(24, 3),
        /**
         * The int vector4.
         */
        IVEC4(16, 4),
        /**
         * The unsigned int vector4.
         */
        UVEC4(16, 4),
        /**
         * The float vector4.
         */
        VEC4(16, 4),
        /**
         * The double vector4.
         */
        DVEC4(32, 4),
        /**
         * The float 2x2 matrix.
         */
        MAT2(16, 4),
        /**
         * The float 3x3 matrix.
         */
        MAT3(36, 9),
        /**
         * The float 4x4 matrix.
         */
        MAT4(64, 16),
        /**
         * The float 2x3 matrix.
         */
        MAT2X3(24, 6),
        /**
         * The float 2x4 matrix.
         */
        MAT2X4(32, 8),
        /**
         * The float 3x2 matrix.
         */
        MAT3X2(24, 6),
        /**
         * The float 3x4 matrix.
         */
        MAT3X4(48, 12),
        /**
         * The float 4x2 matrix.
         */
        MAT4X2(32, 8),
        /**
         * The float 4x3 matrix.
         */
        MAT4X3(48, 12),
        /**
         * The double 2x2 matrix.
         */
        DMAT2(32, 4),
        /**
         * The double 3x3 matrix.
         */
        DMAT3(72, 9),
        /**
         * The double 4x4 matrix.
         */
        DMAT4(128, 16),
        /**
         * The double 2x3 matrix.
         */
        DMAT2X3(48, 6),
        /**
         * The double 2x4 matrix.
         */
        DMAT2X4(64, 8),
        /**
         * The double 3x2 matrix.
         */
        DMAT3X2(48, 6),
        /**
         * The double 3x4 matrix.
         */
        DMAT3X4(96, 12),
        /**
         * The double 4x2 matrix.
         */
        DMAT4X2(64, 8),
        /**
         * The double 4x3 matrix.
         */
        DMAT4X3(96, 12);

        private final int bytesSize;
        private final int count;

        Type(int bytesSize, int count) {
            this.bytesSize = bytesSize;
            this.count = count;
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
        buffer.setInt(0, x);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(int x, int y) {
        buffer.setInt(0, x).setInt(4, y);
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
        buffer.setInt(0, x).setInt(4, y).setInt(8, z);
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
        buffer.setInt(0, x).setInt(4, y).setInt(8, z).setInt(12, w);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(boolean x) {
        buffer.setInt(0, x ? 1 : 0);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(boolean x, boolean y) {
        buffer.setInt(0, x ? 1 : 0).setInt(4, y ? 1 : 0);
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
        buffer.setInt(0, x ? 1 : 0).setInt(4, y ? 1 : 0).setInt(8, z ? 1 : 0);
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
        buffer.setInt(0, x ? 1 : 0).setInt(4, y ? 1 : 0).setInt(8, z ? 1 : 0).setInt(12, w ? 1 : 0);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(float x) {
        buffer.setFloat(0, x);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(float x, float y) {
        buffer.setFloat(0, x).setFloat(4, y);
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
        buffer.setFloat(0, x).setFloat(4, y).setFloat(8, z);
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
        buffer.setFloat(0, x).setFloat(4, y).setFloat(8, z).setFloat(12, w);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     */
    public void set(double x) {
        buffer.setDouble(0, x);
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param x x.
     * @param y y.
     */
    public void set(double x, double y) {
        buffer.setDouble(0, x).setDouble(4, y);
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
        buffer.setDouble(0, x).setDouble(4, y).setDouble(8, z);
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
        buffer.setDouble(0, x).setDouble(4, y).setDouble(8, z).setDouble(12, w);
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
        buffer.setFloat(0, value.m00())
            .setFloat(4, value.m01())
            .setFloat(8, value.m10())
            .setFloat(12, value.m11());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix2dc value) {
        buffer.setDouble(0, value.m00())
            .setDouble(8, value.m01())
            .setDouble(16, value.m10())
            .setDouble(24, value.m11());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3fc value) {
        buffer.setFloat(0, value.m00())
            .setFloat(4, value.m01())
            .setFloat(8, value.m02())
            .setFloat(12, value.m10())
            .setFloat(16, value.m11())
            .setFloat(20, value.m12())
            .setFloat(24, value.m20())
            .setFloat(28, value.m21())
            .setFloat(32, value.m22());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3dc value) {
        buffer.setDouble(0, value.m00())
            .setDouble(8, value.m01())
            .setDouble(16, value.m02())
            .setDouble(24, value.m10())
            .setDouble(32, value.m11())
            .setDouble(40, value.m12())
            .setDouble(48, value.m20())
            .setDouble(56, value.m21())
            .setDouble(64, value.m22());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4fc value) {
        buffer.setFloat(0, value.m00())
            .setFloat(4, value.m01())
            .setFloat(8, value.m02())
            .setFloat(12, value.m03())
            .setFloat(16, value.m10())
            .setFloat(20, value.m11())
            .setFloat(24, value.m12())
            .setFloat(28, value.m13())
            .setFloat(32, value.m20())
            .setFloat(36, value.m21())
            .setFloat(40, value.m22())
            .setFloat(44, value.m23())
            .setFloat(48, value.m30())
            .setFloat(52, value.m31())
            .setFloat(56, value.m32())
            .setFloat(60, value.m33());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4dc value) {
        buffer.setDouble(0, value.m00())
            .setDouble(8, value.m01())
            .setDouble(16, value.m02())
            .setDouble(24, value.m03())
            .setDouble(32, value.m10())
            .setDouble(40, value.m11())
            .setDouble(48, value.m12())
            .setDouble(56, value.m13())
            .setDouble(64, value.m20())
            .setDouble(72, value.m21())
            .setDouble(80, value.m22())
            .setDouble(88, value.m23())
            .setDouble(96, value.m30())
            .setDouble(104, value.m31())
            .setDouble(112, value.m32())
            .setDouble(120, value.m33());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3x2fc value) {
        buffer.setFloat(0, value.m00())
            .setFloat(4, value.m01())
            .setFloat(8, value.m10())
            .setFloat(12, value.m11())
            .setFloat(16, value.m20())
            .setFloat(20, value.m21());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix3x2dc value) {
        buffer.setDouble(0, value.m00())
            .setDouble(8, value.m01())
            .setDouble(16, value.m10())
            .setDouble(24, value.m11())
            .setDouble(32, value.m20())
            .setDouble(40, value.m21());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4x3fc value) {
        buffer.setFloat(0, value.m00())
            .setFloat(4, value.m01())
            .setFloat(8, value.m02())
            .setFloat(12, value.m10())
            .setFloat(16, value.m11())
            .setFloat(20, value.m12())
            .setFloat(24, value.m20())
            .setFloat(28, value.m21())
            .setFloat(32, value.m22())
            .setFloat(36, value.m30())
            .setFloat(40, value.m31())
            .setFloat(44, value.m32());
        markDirty();
    }

    /**
     * Sets the value.
     *
     * @param value the value.
     */
    public void set(Matrix4x3dc value) {
        buffer.setDouble(0, value.m00())
            .setDouble(8, value.m01())
            .setDouble(16, value.m02())
            .setDouble(24, value.m10())
            .setDouble(32, value.m11())
            .setDouble(40, value.m12())
            .setDouble(48, value.m20())
            .setDouble(56, value.m21())
            .setDouble(64, value.m22())
            .setDouble(72, value.m30())
            .setDouble(80, value.m31())
            .setDouble(88, value.m32());
        markDirty();
    }

    /**
     * Uploads this uniform.
     *
     * @param program the shader program id
     */
    protected abstract void internalUpload(int program);

    /**
     * Uploads this uniform.
     *
     * @param program the shader program id
     */
    public void upload(int program) {
        if (!dirty) {
            return;
        }
        internalUpload(program);
    }

    /**
     * Gets the location of this uniform.
     *
     * @return the location
     */
    public int location() {
        return location;
    }

    /**
     * Gets the type of this uniform.
     *
     * @return the type
     */
    public Type type() {
        return type;
    }

    @Override
    public void close() {
        ForkEngine.application.freeNative(buffer);
    }
}
