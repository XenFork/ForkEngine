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

import forkengine.core.ForkEngine;
import org.intellij.lang.annotations.MagicConstant;

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
    public static final int DEPTH_BUFFER_BIT = 0x100,
        STENCIL_BUFFER_BIT = 0x400,
        COLOR_BUFFER_BIT = 0x4000;

    /**
     * Call {@code glClear}.
     *
     * @param flag the clear flag.
     */
    public static void clear(@MagicConstant(flags = {DEPTH_BUFFER_BIT, STENCIL_BUFFER_BIT, COLOR_BUFFER_BIT})
                             int flag) {
        ForkEngine.gl.clear(flag);
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
        ForkEngine.gl.viewport(x, y, w, h);
    }
}
