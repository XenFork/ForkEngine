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

import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

/**
 * The config of an application adapter.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class AppConfig {
    /**
     * The GLFW error callback.
     */
    public @Nullable ObjIntConsumer<String> errorCallback = null;
    /**
     * The initial window width.
     */
    public int width = 640;
    /**
     * The initial window height.
     */
    public int height = 480;
    /**
     * The initial window title.
     */
    public String title = "Game";
    /**
     * The initial window monitor.
     */
    public long monitor = 0L;
    /**
     * The minimum number of screen updates to wait for until the buffers are swapped by {@link Window#swapBuffers() SwapBuffers}.
     * <p>
     * For example, 0 to disable v-sync, and 1 to enable v-sync.
     */
    public int swapInterval = 0;
    /**
     * The initial ticks per second for the default timer of {@link Game}.
     */
    public int ticksPerSecond = 50;

    /**
     * Sets {@link #errorCallback}.
     *
     * @param errorCallback the new value.
     * @return this.
     */
    public AppConfig errorCallback(ObjIntConsumer<String> errorCallback) {
        this.errorCallback = errorCallback;
        return this;
    }

    /**
     * Sets {@link #width}.
     *
     * @param width the new value.
     * @return this.
     */
    public AppConfig width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets {@link #height}.
     *
     * @param height the new value.
     * @return this.
     */
    public AppConfig height(int height) {
        this.height = height;
        return this;
    }

    /**
     * Sets {@link #title}.
     *
     * @param title the new value.
     * @return this.
     */
    public AppConfig title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets {@link #monitor}.
     *
     * @param monitor the new value.
     * @return this.
     */
    public AppConfig monitor(long monitor) {
        this.monitor = monitor;
        return this;
    }

    /**
     * Sets {@link #swapInterval}.
     *
     * @param swapInterval the new value.
     * @return this.
     */
    public AppConfig swapInterval(int swapInterval) {
        this.swapInterval = swapInterval;
        return this;
    }

    /**
     * Sets {@link #ticksPerSecond}.
     *
     * @param ticksPerSecond the new value.
     * @return this.
     */
    public AppConfig ticksPerSecond(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        return this;
    }

    /**
     * Creates the creation info.
     *
     * @param consumer the consumer to set the arguments.
     * @return the {@link AppConfig}
     */
    public static AppConfig of(Consumer<AppConfig> consumer) {
        var info = new AppConfig();
        consumer.accept(info);
        return info;
    }
}
