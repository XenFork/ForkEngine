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

import forkengine.asset.FileProvider;
import forkengine.asset.texture.TextureData;
import forkengine.core.DataBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * The texture data implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public class LWJGL3TextureData implements TextureData {
    private long address = 0;
    private int width = 0;
    private int height = 0;

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public TextureData load(DataBuffer dataBuffer) {
        if (address() != 0) nstbi_image_free(address());
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer px = stack.callocInt(1);
            IntBuffer py = stack.callocInt(1);
            IntBuffer pc = stack.callocInt(1);
            address = nstbi_load_from_memory(dataBuffer.address(),
                (int) dataBuffer.capacity(),
                MemoryUtil.memAddress(px),
                MemoryUtil.memAddress(py),
                MemoryUtil.memAddress(pc),
                STBI_rgb_alpha);
            width = px.get(0);
            height = py.get(0);
        }
        return this;
    }

    @Override
    public TextureData load(FileProvider provider, String path, int bufferSize) {
        DataBuffer buffer = provider.toDataBuffer(path, bufferSize);
        try {
            return load(buffer);
        } finally {
            buffer.free();
        }
    }

    @Override
    public long address() {
        return address;
    }

    @Override
    public void close() {
        nstbi_image_free(address());
    }
}
