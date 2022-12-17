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

package forkengine.gui.screen;

import forkengine.gl.IGL;

import static forkengine.core.ForkEngine.gl;

/**
 * The screen util.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class ScreenUtil {
    /**
     * The clear flag.
     */
    public static final int DEPTH_BUFFER_BIT = IGL.DEPTH_BUFFER_BIT,
        STENCIL_BUFFER_BIT = IGL.STENCIL_BUFFER_BIT,
        COLOR_BUFFER_BIT = IGL.COLOR_BUFFER_BIT;

    /**
     * Sets portions of every pixel in a particular buffer to the same value.
     * The value to which each buffer is cleared depends on the setting of the clear value for that buffer.
     *
     * @param mask Zero or the bitwise OR of one or more values indicating which buffers are to be cleared.
     */
    public static void clear(int mask) {
        gl.clear(mask);
    }

    /**
     * Sets the clear value for fixed-point and floating-point color buffers in RGBA mode.
     * The specified components are stored as floating-point values.
     *
     * @param red   the value to which to clear the R channel of the color buffer.
     * @param green the value to which to clear the G channel of the color buffer.
     * @param blue  the value to which to clear the B channel of the color buffer.
     * @param alpha the value to which to clear the A channel of the color buffer.
     */
    public static void clearColor(float red, float green, float blue, float alpha) {
        gl.clearColor(red, green, blue, alpha);
    }

    /**
     * Call {@code glViewport}.
     *
     * @param x viewport x.
     * @param y viewport y.
     * @param w viewport width.
     * @param h viewport height.
     */
    public static void viewport(int x, int y, int w, int h) {
        gl.viewport(x, y, w, h);
    }
}
