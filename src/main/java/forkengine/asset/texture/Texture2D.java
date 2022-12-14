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
import forkengine.core.ISized;
import forkengine.gl.GLStateManager;
import forkengine.gl.IGL;

import java.util.function.Consumer;

import static forkengine.core.ForkEngine.gl;

/**
 * The 2D texture.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Texture2D extends Texture implements ISized {
    private int width = 0;
    private int height = 0;

    /**
     * Creates the texture with the given id.
     *
     * @param id the texture id.
     */
    public Texture2D(int id) {
        super(id, IGL.TEXTURE_2D);
    }

    /**
     * Binds a 2D texture with {@link GLStateManager#bindTexture2D}.
     *
     * @param unit    the texture unit starting at 0.
     * @param texture the texture id.
     */
    public static void bind(int unit, int texture) {
        GLStateManager.bindTexture2D(unit, texture);
    }

    /**
     * Creates a 2D texture with {@link IGL#createTexture}.
     *
     * @return the texture.
     */
    public static Texture2D create() {
        return new Texture2D(gl.createTexture(IGL.TEXTURE_2D));
    }

    /**
     * Loads a texture with the given parameters.
     *
     * @param provider   the file provider.
     * @param path       the resource path.
     * @param bufferSize the initial buffer size.
     * @param levels     the number of texture levels.
     * @param consumer   the action to be performed before loading image.
     * @return the texture.
     */
    public static Texture2D load(FileProvider provider, String path, long bufferSize,
                                 int levels, Consumer<Texture2D> consumer) {
        Texture2D texture = create();
        texture.bind(0);
        consumer.accept(texture);
        try (TextureData data = TextureData.create().load(provider, path, bufferSize)) {
            texture.specifyImage(levels, data);
        }
        texture.generateMipmap();
        return texture;
    }

    /**
     * Loads a texture from local with the given parameters.
     *
     * @param path       the resource path.
     * @param bufferSize the initial buffer size.
     * @param levels     the number of texture levels.
     * @param consumer   the action to be performed before loading image.
     * @return the texture.
     */
    public static Texture2D local(String path, long bufferSize,
                                  int levels, Consumer<Texture2D> consumer) {
        return load(FileProvider.LOCAL, path, bufferSize, levels, consumer);
    }

    /**
     * Loads a texture from classpath with the given parameters.
     *
     * @param path       the resource path.
     * @param bufferSize the initial buffer size.
     * @param levels     the number of texture levels.
     * @param consumer   the action to be performed before loading image.
     * @return the texture.
     */
    public static Texture2D internal(String path, long bufferSize,
                                     int levels, Consumer<Texture2D> consumer) {
        return load(FileProvider.CLASSPATH, path, bufferSize, levels, consumer);
    }


    @Override
    public void bind(int unit) {
        bind(unit, id());
    }

    @Override
    public Texture2D setParameter(int pname, int param) {
        gl.textureParameter(IGL.TEXTURE_2D, id(), pname, param);
        return this;
    }

    @Override
    public Texture2D setParameter(int pname, float param) {
        gl.textureParameter(IGL.TEXTURE_2D, id(), pname, param);
        return this;
    }

    /**
     * Specifies a two-dimensional texture image.
     *
     * @param levels the number of texture levels.
     * @param data   the pixel data.
     * @return this.
     */
    public Texture2D specifyImage(int levels, TextureData data) {
        width = data.width();
        height = data.height();
        gl.textureStorage2D(IGL.TEXTURE_2D, id(), levels, IGL.RGBA8, width, height, IGL.RGBA, IGL.UNSIGNED_BYTE);
        gl.textureSubImage2D(IGL.TEXTURE_2D, id(), 0, 0, 0, width, height, IGL.RGBA, IGL.UNSIGNED_BYTE, data.address());
        return this;
    }

    @Override
    public Texture2D generateMipmap() {
        super.generateMipmap();
        return this;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }
}
