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

import forkengine.gl.IGL;
import org.jetbrains.annotations.Nullable;

import java.util.function.ObjIntConsumer;

/**
 * The base application with an abstract layer.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class Application {
    /**
     * Initialize GLFW.
     */
    public abstract boolean initGLFW();

    /**
     * Terminate GLFW.
     */
    public abstract void terminateGLFW();

    /**
     * Sets the GLFW error callback.
     *
     * @param logger the logger with type {@code (description, error) -> void }.
     * @return the previous callback.
     */
    public abstract IResource setErrorCallback(ObjIntConsumer<String> logger);

    /**
     * Gets the default GLFW error callback.
     *
     * @return the default GLFW error callback.
     */
    public abstract ObjIntConsumer<String> defaultErrorCallback();

    /**
     * Creates the window with the given parameters.
     *
     * @param width   the initial window width.
     * @param height  the initial window height.
     * @param title   the initial window title.
     * @param monitor the initial window monitor, or {@code NULL} to use windowed mode.
     * @param share   the other window context to share with.
     * @return the instance of the window.
     */
    public abstract @Nullable Window createWindow(int width, int height, String title, long monitor, long share);

    /**
     * Loads the OpenGL context.
     *
     * @param forwardCompatible {@code true} to creates a forward compatible context, which cannot access deprecated functions.
     * @return the OpenGL functions.
     */
    public abstract IGL loadOpenGL(boolean forwardCompatible);

    /**
     * Sets the swap interval for the current OpenGL or OpenGL ES context, i.e. the number of screen updates to wait from the time {@link Window#swapBuffers() SwapBuffers} was called
     * before swapping the buffers and returning. This is sometimes called <i>vertical synchronization</i>, <i>vertical retrace synchronization</i> or just
     * <i>vsync</i>.
     *
     * @param interval the minimum number of screen updates to wait for until the buffers are swapped by {@link Window#swapBuffers() SwapBuffers}.
     */
    public abstract void swapInterval(int interval);

    /**
     * Poll GLFW events.
     */
    public abstract void pollEvents();

    /**
     * Gets the monitors.
     *
     * @return the monitors.
     */
    public abstract long @Nullable [] monitors();

    /**
     * Gets the primary monitor.
     *
     * @return the primary monitor.
     */
    public abstract long primaryMonitor();

    /**
     * Creates a timer.
     *
     * @param ticksPerSecond the ticks per second. defaults to 50.
     * @return the timer.
     */
    public abstract Timer createTimer(int ticksPerSecond);

    /**
     * Allocates native memory.
     *
     * @param bytesSize the bytes size.
     * @return the memory address.
     */
    public abstract DataBuffer allocateNative(long bytesSize);

    /**
     * Releases native memory.
     *
     * @param address the memory address.
     */
    public abstract void freeNative(DataBuffer address);
}
