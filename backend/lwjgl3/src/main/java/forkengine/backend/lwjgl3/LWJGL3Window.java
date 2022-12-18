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

import forkengine.core.*;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The window implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public class LWJGL3Window implements Window {
    private final long address;
    private int width, height;
    private String title;
    private long monitor;
    private @Nullable ISizeListener sizeListener;
    private int framebufferWidth, framebufferHeight;
    private @Nullable ISizeListener framebufferSizeCb;
    private double cursorX, cursorY;
    private @Nullable ICursorListener cursorListener;
    private @Nullable IKeyListener keyListener;

    /**
     * Creates the window with the given address.
     *
     * @param address the raw address
     * @param width   the initial window width
     * @param height  the initial window height
     * @param title   the initial window title
     * @param monitor the initial window monitor, or {@code NULL} to use windowed mode
     */
    public LWJGL3Window(long address, int width, int height, String title, long monitor) {
        this.address = address;
        this.width = width;
        this.height = height;
        this.title = title;
        this.monitor = monitor;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pw = stack.callocInt(1);
            IntBuffer ph = stack.callocInt(1);
            glfwGetFramebufferSize(address(), pw, ph);
            framebufferWidth = pw.get(0);
            framebufferHeight = ph.get(0);
        }
    }

    @Override
    public Window registerCallbacks() {
        glfwSetWindowSizeCallback(address(), (window, newWidth, newHeight) ->
            onResize(width, height, newWidth, newHeight)
        );
        glfwSetFramebufferSizeCallback(address(), (window, newWidth, newHeight) -> {
            if (framebufferSizeCb != null) {
                framebufferSizeCb.onResize(framebufferWidth, framebufferHeight, newWidth, newHeight);
            }
            framebufferWidth = newWidth;
            framebufferHeight = newHeight;
        });
        glfwSetCursorPosCallback(address(), (window, xpos, ypos) -> {
            if (cursorListener != null) {
                cursorListener.onCursorPos(cursorX, cursorY, xpos, ypos);
            }
            cursorX = xpos;
            cursorY = ypos;
        });
        glfwSetCursorEnterCallback(address(), (window, entered) -> {
            if (cursorListener != null) {
                cursorListener.onCursorEnter(entered);
            }
        });
        glfwSetMouseButtonCallback(address(), (window, button, action, mods) -> {
            if (cursorListener != null && cursorListener instanceof IMouseListener mouseListener) {
                switch (action) {
                    case GLFW_PRESS -> mouseListener.onMouseButtonPress(button, mods);
                    case GLFW_RELEASE -> mouseListener.onMouseButtonRelease(button, mods);
                }
            }
        });
        glfwSetScrollCallback(address(), (window, offsetX, offsetY) -> {
            if (cursorListener != null && cursorListener instanceof IMouseListener mouseListener) {
                mouseListener.onMouseScroll(offsetX, offsetY);
            }
        });
        glfwSetKeyCallback(address(), (window, key, scancode, action, mods) -> {
            if (keyListener != null) {
                switch (action) {
                    case GLFW_PRESS -> keyListener.onKeyPress(key, scancode, mods);
                    case GLFW_RELEASE -> keyListener.onKeyReleased(key, scancode, mods);
                    case GLFW_REPEAT -> keyListener.onKeyRepeated(key, scancode, mods);
                }
            }
        });
        return this;
    }

    @Override
    public void destroy() {
        Callbacks.glfwFreeCallbacks(address());
        glfwDestroyWindow(address());
    }

    @Override
    public void makeContextCurrent() {
        glfwMakeContextCurrent(address());
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(address());
    }

    @Override
    public void swapBuffers() {
        glfwSwapBuffers(address());
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        glfwSetWindowSize(address(), width, height);
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        glfwSetWindowSize(address(), width, height);
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        glfwSetWindowSize(address(), width, height);
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int framebufferWidth() {
        return framebufferWidth;
    }

    @Override
    public int framebufferHeight() {
        return framebufferHeight;
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        if (sizeListener != null) {
            sizeListener.onResize(oldWidth, oldHeight, newWidth, newHeight);
        }
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(address(), title);
    }

    @Override
    public void setMonitor(long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        this.monitor = monitor;
        glfwSetWindowMonitor(address(), monitor, xpos, ypos, width, height, refreshRate);
    }

    @Override
    public long monitor() {
        return monitor;
    }

    @Override
    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(address(), button) == GLFW_PRESS;
    }

    @Override
    public boolean isMouseButtonUp(int button) {
        return glfwGetMouseButton(address(), button) == GLFW_RELEASE;
    }

    @Override
    public boolean isKeyDown(int key) {
        return glfwGetKey(address(), key) == GLFW_PRESS;
    }

    @Override
    public boolean isKeyUp(int key) {
        return glfwGetKey(address(), key) == GLFW_RELEASE;
    }

    @Override
    public double cursorX() {
        return cursorX;
    }

    @Override
    public double cursorY() {
        return cursorY;
    }

    @Override
    public void setSizeListener(@Nullable ISizeListener sizeListener) {
        this.sizeListener = sizeListener;
    }

    @Override
    public void setFramebufferSizeCb(@Nullable ISizeListener framebufferSizeCb) {
        this.framebufferSizeCb = framebufferSizeCb;
    }

    @Override
    public void setCursorListener(@Nullable ICursorListener cursorListener) {
        this.cursorListener = cursorListener;
    }

    @Override
    public void setKeyListener(@Nullable IKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    @Override
    public void setInputMode(int mode, int value) {
        glfwSetInputMode(address(), mode, value);
    }

    @Override
    public long address() {
        return address;
    }
}
