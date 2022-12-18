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

package forkengine.backend.lwjgl3;

import forkengine.asset.texture.TextureData;
import forkengine.core.*;
import forkengine.gl.IGL;
import forkengine.graphics.font.BitmapFont;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.MemoryUtil;

import java.util.Map;
import java.util.function.ObjIntConsumer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The application implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class LWJGL3App implements Application {
    private static LWJGL3App instance;

    private LWJGL3App() {
    }

    @Override
    public boolean initGLFW() {
        return glfwInit();
    }

    @Override
    public void terminateGLFW() {
        glfwTerminate();
    }

    @Override
    public AutoCloseable setErrorCallback(ObjIntConsumer<String> logger) {
        return glfwSetErrorCallback((error, description) ->
            logger.accept(GLFWErrorCallback.getDescription(description), error));
    }

    @Override
    public ObjIntConsumer<String> defaultErrorCallback() {
        return new ObjIntConsumer<>() {
            private final Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens(
                (field, value) -> 0x10000 < value && value < 0x20000,
                null,
                org.lwjgl.glfw.GLFW.class);

            @Override
            public void accept(String description, int errorCode) {
                var sb = new StringBuilder(512);

                sb.append("[LWJGL] ").append(ERROR_CODES.get(errorCode)).append("error\n")
                    .append("\tDescription : ").append(description)
                    .append("\n\tStacktrace  :\n");
                StackTraceElement[] stack = Thread.currentThread().getStackTrace();
                for (int i = 4; i < stack.length; i++) {
                    sb.append("\t\t").append(stack[i]).append('\n');
                }
                APIUtil.apiLog(sb);
            }
        };
    }

    @Override
    public @Nullable Window createWindow(int width, int height, String title, long monitor, long share) {
        final long address = glfwCreateWindow(width, height, title, monitor, share);
        if (address == 0) return null;
        return new LWJGL3Window(address, width, height, title, monitor)
            .registerCallbacks();
    }

    @Override
    public IGL loadOpenGL(boolean forwardCompatible) {
        GL.createCapabilities(forwardCompatible);
        return LWJGL3GL.getInstance();
    }

    @Override
    public void swapInterval(int interval) {
        glfwSwapInterval(interval);
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
    }

    @Override
    public long[] monitors() {
        PointerBuffer monitors = glfwGetMonitors();
        if (monitors == null) return null;
        long[] arr = new long[monitors.remaining()];
        monitors.get(arr);
        return arr;
    }

    @Override
    public long primaryMonitor() {
        return glfwGetPrimaryMonitor();
    }

    @Override
    public Timer createTimer(int ticksPerSecond) {
        return new LWJGL3Timer(ticksPerSecond);
    }

    @Override
    public DataBuffer allocateNative(long bytesSize) {
        return new LWJGL3DataBuffer(MemoryUtil.memCalloc((int) bytesSize));
    }

    @Override
    public void freeNative(DataBuffer address) {
        MemoryUtil.nmemFree(address.address());
    }

    @Override
    public TextureData newTextureData() {
        return new LWJGL3TextureData();
    }

    @Override
    public BitmapFont newBitmapFont() {
        return new LWJGL3BitmapFont();
    }

    /**
     * Gets the instance of this.
     *
     * @return this
     */
    public static LWJGL3App getInstance() {
        if (instance == null) {
            instance = new LWJGL3App();
        }
        return instance;
    }
}
