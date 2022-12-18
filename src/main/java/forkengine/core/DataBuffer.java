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

import forkengine.asset.FileProvider;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The data buffer wrapped byte operation.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class DataBuffer {
    /**
     * The address of this buffer. It might be changed on reallocating.
     */
    protected long address;
    private long position = 0;
    /**
     * The capacity of this buffer.
     */
    protected long capacity = 0;

    /**
     * Creates the data buffer.
     *
     * @param address the buffer address.
     */
    public DataBuffer(long address) {
        this.address = address;
    }

    /**
     * Allocates native memory.
     *
     * @param bytesSize the bytes size.
     * @return the memory address.
     */
    public static DataBuffer allocate(long bytesSize) {
        return ForkEngine.application.allocateNative(bytesSize);
    }

    /**
     * Releases native memory.
     *
     * @param address the memory address.
     */
    public static void free(DataBuffer address) {
        if (address != null) ForkEngine.application.freeNative(address);
    }

    /**
     * Puts the byte at the given offset.
     *
     * @param offset the offset in bytes.
     * @param value  the value.
     * @return this.
     */
    public abstract DataBuffer putByte(long offset, byte value);

    /**
     * Puts the short at the given offset.
     *
     * @param offset the offset in bytes.
     * @param value  the value.
     * @return this.
     */
    public abstract DataBuffer putShort(long offset, short value);

    /**
     * Puts the int at the given offset.
     *
     * @param offset the offset in bytes.
     * @param value  the value.
     * @return this.
     */
    public abstract DataBuffer putInt(long offset, int value);

    /**
     * Puts the long at the given offset.
     *
     * @param offset the offset in bytes.
     * @param value  the value.
     * @return this.
     */
    public abstract DataBuffer putLong(long offset, long value);

    /**
     * Puts the float at the given offset.
     *
     * @param offset the offset in bytes.
     * @param value  the value.
     * @return this.
     */
    public abstract DataBuffer putFloat(long offset, float value);

    /**
     * Puts the double at the given offset.
     *
     * @param offset the offset in bytes.
     * @param value  the value.
     * @return this.
     */
    public abstract DataBuffer putDouble(long offset, double value);

    /**
     * Puts the buffer at the given offset.
     *
     * @param offset the offset in bytes.
     * @param buffer the buffer.
     * @return this.
     */
    public abstract DataBuffer putBuffer(long offset, ByteBuffer buffer);

    /**
     * Puts the byte at the current position.
     *
     * @param value the value.
     * @return this.
     */
    public DataBuffer putByte(byte value) {
        putByte(position(), value);
        position++;
        return this;
    }

    /**
     * Puts the short at the current position.
     *
     * @param value the value.
     * @return this.
     */
    public DataBuffer putShort(short value) {
        putShort(position(), value);
        position += 2;
        return this;
    }

    /**
     * Puts the int at the current position.
     *
     * @param value the value.
     * @return this.
     */
    public DataBuffer putInt(int value) {
        putInt(position(), value);
        position += 4;
        return this;
    }

    /**
     * Puts the long at the current position.
     *
     * @param value the value.
     * @return this.
     */
    public DataBuffer putLong(long value) {
        putLong(position(), value);
        position += 8;
        return this;
    }

    /**
     * Puts the float at the current position.
     *
     * @param value the value.
     * @return this.
     */
    public DataBuffer putFloat(float value) {
        putFloat(position(), value);
        position += 4;
        return this;
    }

    /**
     * Puts the double at the current position.
     *
     * @param value the value.
     * @return this.
     */
    public DataBuffer putDouble(double value) {
        putDouble(position(), value);
        position += 8;
        return this;
    }

    /**
     * Puts the buffer at the current position.
     *
     * @param buffer the buffer.
     * @return this.
     */
    public DataBuffer putBuffer(ByteBuffer buffer) {
        putBuffer(position(), buffer);
        position += buffer.remaining();
        return this;
    }

    /**
     * Gets the byte at the given offset.
     *
     * @param offset the offset in bytes.
     * @return the value.
     */
    public abstract byte getByte(long offset);

    /**
     * Gets the short at the given offset.
     *
     * @param offset the offset in bytes.
     * @return the value.
     */
    public abstract short getShort(long offset);

    /**
     * Gets the int at the given offset.
     *
     * @param offset the offset in bytes.
     * @return the value.
     */
    public abstract int getInt(long offset);

    /**
     * Gets the long at the given offset.
     *
     * @param offset the offset in bytes.
     * @return the value.
     */
    public abstract long getLong(long offset);

    /**
     * Gets the float at the given offset.
     *
     * @param offset the offset in bytes.
     * @return the value.
     */
    public abstract float getFloat(long offset);

    /**
     * Gets the double at the given offset.
     *
     * @param offset the offset in bytes.
     * @return the value.
     */
    public abstract double getDouble(long offset);

    /**
     * Reallocate this buffer.
     *
     * @param size the new bytes size.
     * @return this.
     */
    public abstract DataBuffer realloc(long size);

    /**
     * Loads a file from the given file provider.
     *
     * @param provider the file provider.
     * @param resource the resource name.
     * @return this.
     */
    public abstract DataBuffer loadFile(FileProvider provider, String resource);

    /**
     * Loads a file from local.
     *
     * @param resource the resource name.
     * @return this.
     * @throws IOException if error reading.
     */
    public abstract DataBuffer loadLocalFile(String resource) throws IOException;

    /**
     * Sets the current position of this buffer.
     *
     * @param position the new position.
     * @return this.
     */
    public DataBuffer position(long position) {
        this.position = position;
        return this;
    }

    /**
     * Gets the current position of this buffer.
     *
     * @return the position.
     */
    public long position() {
        return position;
    }

    /**
     * Gets the capacity of this buffer.
     *
     * @return the capacity.
     */
    public long capacity() {
        return capacity;
    }

    /**
     * Gets the address of this buffer.
     *
     * @return the address.
     */
    public long address() {
        return address;
    }

    /**
     * Releases the native memory.
     */
    public void free() {
        free(this);
    }
}
