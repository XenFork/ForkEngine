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

import forkengine.asset.Asset;
import forkengine.core.ForkEngine;

/**
 * The base texture.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class Texture extends Asset {
    /**
     * The texture parameters.
     */
    public static final int
        MAG_FILTER = 0x2800,
        MIN_FILTER = 0x2801,
        WRAP_S = 0x2802,
        WRAP_T = 0x2803,
        WRAP_R = 0x8072,
        MIN_LOD = 0x813A,
        MAX_LOD = 0x813B,
        BASE_LEVEL = 0x813C,
        MAX_LEVEL = 0x813D,
        LOD_BIAS = 0x8501,
        COMPARE_MODE = 0x884C,
        COMPARE_FUNC = 0x884D,
        SWIZZLE_R = 0x8E42,
        SWIZZLE_G = 0x8E43,
        SWIZZLE_B = 0x8E44,
        SWIZZLE_A = 0x8E45,
        DEPTH_STENCIL_TEXTURE_MODE = 0x90EA;
    /**
     * The cube-map texture faces.
     */
    public static final int
        CUBE_MAP_POSITIVE_X = 0x8515,
        CUBE_MAP_NEGATIVE_X = 0x8516,
        CUBE_MAP_POSITIVE_Y = 0x8517,
        CUBE_MAP_NEGATIVE_Y = 0x8518,
        CUBE_MAP_POSITIVE_Z = 0x8519,
        CUBE_MAP_NEGATIVE_Z = 0x851A;
    private final int id;
    private final int target;

    /**
     * Creates the texture with the given id and target.
     *
     * @param id     the texture id.
     * @param target the texture target.
     */
    public Texture(int id, int target) {
        this.id = id;
        this.target = target;
    }

    /**
     * Binds this texture to the given unit starting at 0.
     *
     * @param unit the texture unit to be bound.
     */
    public abstract void bind(int unit);

    /**
     * Sets the integer value of a texture parameter, which controls how the texel array is treated when specified or changed,
     * and when applied to a fragment.
     *
     * @param pname the parameter to set.
     * @param param the parameter value.
     */
    public abstract Texture setParameter(int pname, int param);

    /**
     * Float version of {@link #setParameter(int, int) setParameter}.
     *
     * @param pname the parameter to set.
     * @param param the parameter value.
     */
    public abstract Texture setParameter(int pname, float param);

    /**
     * Generate mipmaps for a specified texture target.
     *
     * @return this.
     */
    public Texture generateMipmap() {
        ForkEngine.gl.generateMipmap(target(), id());
        return this;
    }

    /**
     * Gets the id of this texture.
     *
     * @return the texture id.
     */
    public int id() {
        return id;
    }

    /**
     * Gets the target of this texture.
     *
     * @return the texture target.
     */
    public int target() {
        return target;
    }

    @Override
    public void close() {
        ForkEngine.gl.deleteTexture(id());
    }
}
