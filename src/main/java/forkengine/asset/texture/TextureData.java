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

package forkengine.asset.texture;

import forkengine.asset.FileProvider;
import forkengine.core.DataBuffer;
import forkengine.core.ForkEngine;
import forkengine.core.ISized;

/**
 * The base texture data.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface TextureData extends ISized, AutoCloseable {
    /**
     * Creates a new texture data.
     *
     * @return the texture data.
     */
    static TextureData create() {
        return ForkEngine.application.newTextureData();
    }

    /**
     * Loads the texture data from a local file.
     *
     * @param path       the resource name.
     * @param bufferSize the initial buffer size.
     * @return the texture data.
     */
    static TextureData local(String path, long bufferSize) {
        return create().load(FileProvider.LOCAL, path, bufferSize);
    }

    /**
     * Loads the texture data from a classpath file.
     *
     * @param path       the resource name.
     * @param bufferSize the initial buffer size.
     * @return the texture data.
     */
    static TextureData internal(String path, long bufferSize) {
        return create().load(FileProvider.CLASSPATH, path, bufferSize);
    }

    /**
     * Sets the data to the given address.
     *
     * @param address the data address.
     * @param width   the width.
     * @param height  the height.
     * @return this.
     */
    TextureData setAs(long address, int width, int height);

    /**
     * Loads the texture data from the given data buffer.
     *
     * @param dataBuffer the data buffer.
     * @return this.
     */
    TextureData load(DataBuffer dataBuffer);

    /**
     * Loads the texture data from the given file.
     *
     * @param provider   the file provider.
     * @param path       the resource name.
     * @param bufferSize the initial buffer size.
     * @return this.
     */
    default TextureData load(FileProvider provider, String path, long bufferSize) {
        DataBuffer buffer = provider.toDataBuffer(path, bufferSize);
        try {
            return load(buffer);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load image '" + path + '\'', e);
        } finally {
            buffer.free();
        }
    }

    /**
     * Gets the address of the pixel data.
     *
     * @return the pixel data address.
     */
    long address();

    @Override
    void close();
}
