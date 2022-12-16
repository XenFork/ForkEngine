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
     * Binds this texture to the given unit started at 0.
     *
     * @param unit the texture unit to be bound.
     */
    public abstract void bind(int unit);

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
