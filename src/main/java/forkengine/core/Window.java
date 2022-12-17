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

import org.jetbrains.annotations.Nullable;

/**
 * The base window.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface Window extends ISized, ISizeable, ISizeListener {
    /**
     * Register the callbacks.
     *
     * @return the callbacks.
     */
    Window registerCallbacks();

    /**
     * Destroys the callback and this window.
     */
    void destroy();

    /**
     * Makes the OpenGL or OpenGL ES context of this window current on the calling thread.
     */
    void makeContextCurrent();

    /**
     * Returns the value of the close flag of this window.
     *
     * @return the value of the close flag
     */
    boolean shouldClose();

    /**
     * Swap the buffers.
     */
    void swapBuffers();

    /**
     * Gets the width of the framebuffer.
     *
     * @return the width of the framebuffer.
     */
    int framebufferWidth();

    /**
     * Gets the height of the framebuffer.
     *
     * @return the height of the framebuffer.
     */
    int framebufferHeight();

    /**
     * Sets the title.
     *
     * @param title the new title of this window.
     */
    void setTitle(String title);

    /**
     * Gets the title.
     *
     * @return the title of this window.
     */
    String title();

    /**
     * Sets the monitor.
     *
     * @param monitor     the desired monitor, or {@code NULL} to set windowed mode.
     * @param xpos        the desired x-coordinate of the upper-left corner of the content area.
     * @param ypos        the desired y-coordinate of the upper-left corner of the content area.
     * @param width       the desired with, in screen coordinates, of the content area or video mode.
     * @param height      the desired height, in screen coordinates, of the content area or video mode.
     * @param refreshRate the desired refresh rate, in Hz, of the video mode, or {@code -1} for don't care.
     */
    void setMonitor(long monitor, int xpos, int ypos, int width, int height, int refreshRate);

    /**
     * Gets the monitor.
     *
     * @return the monitor of this window.
     */
    long monitor();

    /**
     * Gets the status of the given mouse button.
     *
     * @param button the mouse button.
     * @return is the button pressing
     */
    boolean isMouseButtonDown(int button);

    /**
     * Gets the status of the given mouse button.
     *
     * @param button the mouse button.
     * @return is the button not pressing
     */
    boolean isMouseButtonUp(int button);

    /**
     * Gets the status of the given key.
     *
     * @param key the key.
     * @return is the key pressing
     */
    boolean isKeyDown(int key);

    /**
     * Gets the status of the given key.
     *
     * @param key the key.
     * @return is the key not pressing
     */
    boolean isKeyUp(int key);

    /**
     * Sets the size listener.
     *
     * @param sizeListener the size listener.
     */
    void setSizeListener(@Nullable ISizeListener sizeListener);

    /**
     * Sets the framebuffer size callback.
     *
     * @param framebufferSizeCb the framebuffer size callback.
     */
    void setFramebufferSizeCb(@Nullable ISizeListener framebufferSizeCb);

    /**
     * Sets the cursor listener.
     *
     * @param cursorListener the cursor listener.
     */
    void setCursorListener(@Nullable ICursorListener cursorListener);

    /**
     * Sets the key listener.
     *
     * @param keyListener the key listener.
     */
    void setKeyListener(@Nullable IKeyListener keyListener);

    /**
     * Gets the raw address.
     *
     * @return the raw address of this window.
     */
    long address();
}
