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

package forkengine.core;

/**
 * The data buffer wrapped byte operation.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class DataBuffer {
    private final long address;

    /**
     * Creates the data buffer.
     *
     * @param address the buffer address.
     */
    public DataBuffer(long address) {
        this.address = address;
    }

    /**
     * Sets the byte at the given offset.
     *
     * @param offset the offset in bytes
     * @param value  the value
     * @return this
     */
    public abstract DataBuffer setByte(long offset, byte value);

    /**
     * Sets the short at the given offset.
     *
     * @param offset the offset in bytes
     * @param value  the value
     * @return this
     */
    public abstract DataBuffer setShort(long offset, short value);

    /**
     * Sets the int at the given offset.
     *
     * @param offset the offset in bytes
     * @param value  the value
     * @return this
     */
    public abstract DataBuffer setInt(long offset, int value);

    /**
     * Sets the long at the given offset.
     *
     * @param offset the offset in bytes
     * @param value  the value
     * @return this
     */
    public abstract DataBuffer setLong(long offset, long value);

    /**
     * Sets the float at the given offset.
     *
     * @param offset the offset in bytes
     * @param value  the value
     * @return this
     */
    public abstract DataBuffer setFloat(long offset, float value);

    /**
     * Sets the double at the given offset.
     *
     * @param offset the offset in bytes
     * @param value  the value
     * @return this
     */
    public abstract DataBuffer setDouble(long offset, double value);

    /**
     * Gets the byte at the given offset.
     *
     * @param offset the offset in bytes
     * @return the value
     */
    public abstract byte getByte(long offset);

    /**
     * Gets the short at the given offset.
     *
     * @param offset the offset in bytes
     * @return the value
     */
    public abstract short getShort(long offset);

    /**
     * Gets the int at the given offset.
     *
     * @param offset the offset in bytes
     * @return the value
     */
    public abstract int getInt(long offset);

    /**
     * Gets the long at the given offset.
     *
     * @param offset the offset in bytes
     * @return the value
     */
    public abstract long getLong(long offset);

    /**
     * Gets the float at the given offset.
     *
     * @param offset the offset in bytes
     * @return the value
     */
    public abstract float getFloat(long offset);

    /**
     * Gets the double at the given offset.
     *
     * @param offset the offset in bytes
     * @return the value
     */
    public abstract double getDouble(long offset);

    /**
     * Gets the address of this buffer.
     *
     * @return the address.
     */
    public long address() {
        return address;
    }
}
