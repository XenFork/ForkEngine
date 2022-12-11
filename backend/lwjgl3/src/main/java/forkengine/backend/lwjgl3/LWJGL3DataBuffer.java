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

package forkengine.backend.lwjgl3;

import forkengine.core.DataBuffer;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * The data buffer implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public class LWJGL3DataBuffer extends DataBuffer {
    private final ByteBuffer buffer;

    /**
     * Creates the data buffer.
     *
     * @param buffer the container buffer.
     */
    public LWJGL3DataBuffer(ByteBuffer buffer) {
        super(MemoryUtil.memAddress(buffer));
        this.buffer = buffer;
    }

    @Override
    public DataBuffer setByte(long offset, byte value) {
        buffer.put((int) offset, value);
        return this;
    }

    @Override
    public DataBuffer setShort(long offset, short value) {
        buffer.putShort((int) offset, value);
        return this;
    }

    @Override
    public DataBuffer setInt(long offset, int value) {
        buffer.putInt((int) offset, value);
        return this;
    }

    @Override
    public DataBuffer setLong(long offset, long value) {
        buffer.putLong((int) offset, value);
        return this;
    }

    @Override
    public DataBuffer setFloat(long offset, float value) {
        buffer.putFloat((int) offset, value);
        return this;
    }

    @Override
    public DataBuffer setDouble(long offset, double value) {
        buffer.putDouble((int) offset, value);
        return this;
    }

    @Override
    public byte getByte(long offset) {
        return buffer.get((int) offset);
    }

    @Override
    public short getShort(long offset) {
        return buffer.getShort((int) offset);
    }

    @Override
    public int getInt(long offset) {
        return buffer.getInt((int) offset);
    }

    @Override
    public long getLong(long offset) {
        return buffer.getLong((int) offset);
    }

    @Override
    public float getFloat(long offset) {
        return buffer.getFloat((int) offset);
    }

    @Override
    public double getDouble(long offset) {
        return buffer.getDouble((int) offset);
    }
}
