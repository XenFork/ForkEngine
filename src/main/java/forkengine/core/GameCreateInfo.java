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
 * The creation info of the game.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class GameCreateInfo {
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
     * Creates the creation info.
     *
     * @param consumer the consumer to set the arguments.
     * @return the {@link GameCreateInfo}
     */
    public static GameCreateInfo of(Consumer<GameCreateInfo> consumer) {
        var info = new GameCreateInfo();
        consumer.accept(info);
        return info;
    }
}
