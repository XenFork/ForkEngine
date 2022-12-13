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

import forkengine.gl.IGL;

/**
 * The data types.
 *
 * @author squid233
 * @since 0.1.0
 */
public enum DataType {
    /**
     * The byte type.
     */
    BYTE(IGL.BYTE, 1, "Byte"),
    /**
     * The unsigned byte type.
     */
    UNSIGNED_BYTE(IGL.UNSIGNED_BYTE, 1, "Unsigned Byte"),
    /**
     * The short type.
     */
    SHORT(IGL.SHORT, 2, "Short"),
    /**
     * The unsigned short type.
     */
    UNSIGNED_SHORT(IGL.UNSIGNED_SHORT, 2, "Unsigned Short"),
    /**
     * The int type.
     */
    INT(IGL.INT, 4, "Int"),
    /**
     * The unsigned int type.
     */
    UNSIGNED_INT(IGL.UNSIGNED_INT, 4, "Unsigned Int"),
    /**
     * The float type.
     */
    FLOAT(IGL.FLOAT, 4, "Float"),
    /**
     * The double type.
     */
    DOUBLE(IGL.DOUBLE, 8, "Double");

    private final int value;
    private final int bytesSize;
    private final String toStringValue;

    DataType(int value, int bytesSize, String toStringValue) {
        this.value = value;
        this.bytesSize = bytesSize;
        this.toStringValue = toStringValue;
    }

    /**
     * Gets the enum value of this type.
     *
     * @return the enum value.
     */
    public int value() {
        return value;
    }

    /**
     * Gets the bytes size of this type.
     *
     * @return the bytes size.
     */
    public int bytesSize() {
        return bytesSize;
    }

    @Override
    public String toString() {
        return toStringValue;
    }
}
