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

import forkengine.gui.screen.ScreenUtil;

import java.util.Objects;

/**
 * The game.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Game implements ISizeListener, IMouseListener, IKeyListener {
    /**
     * The game implementation.
     */
    protected Application application;
    /**
     * The window instance.
     */
    protected Window window;
    /**
     * The default timer.
     */
    protected Timer defaultTimer;
    /**
     * The frames per second.
     */
    protected int framesPerSecond;
    /**
     * The framebuffer width of this game.
     */
    protected int width;
    /**
     * The framebuffer height of this game.
     */
    protected int height;

    /**
     * Pre-initialization. This method is invoked before initializing context.
     */
    public void preInit() {
    }

    /**
     * Initialization. This method is invoked after initializing context and before game loop.
     */
    public void init() {
    }

    /**
     * Updating the things in the world.
     * <p>
     * This method is invoked per frame. You should use {@code super} call at the beginning of the overridden.
     *
     * @param delta {@link Timer#deltaFrameTime()}
     */
    public void update(double delta) {
        application.pollEvents();
    }

    /**
     * Updating the things in the physical world.
     * <p>
     * This method is invoked per tick.
     *
     * @param partialTick {@link Timer#partialTick()}
     */
    public void fixedUpdate(double partialTick) {
    }

    /**
     * Update anything.
     * <p>
     * This method is invoked after {@link #fixedUpdate(double)} and {@link #update(double)}.
     *
     * @param delta {@link Timer#deltaFrameTime()}
     */
    public void lateUpdate(double delta) {
    }

    /**
     * Rendering. You should use {@code super} call at the end of the overridden.
     */
    public void render() {
        window.swapBuffers();
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        ScreenUtil.viewport(0, 0, newWidth, newHeight);
    }

    @Override
    public void onCursorPos(double oldX, double oldY, double newX, double newY) {
    }

    @Override
    public void onCursorEnter(boolean entered) {
    }

    @Override
    public void onKeyPress(int key, int scancode, int mods) {
    }

    @Override
    public void onKeyReleased(int key, int scancode, int mods) {
    }

    @Override
    public void onKeyRepeated(int key, int scancode, int mods) {
    }

    @Override
    public void onMouseButtonPress(int button, int mods) {
    }

    @Override
    public void onMouseButtonRelease(int button, int mods) {
    }

    @Override
    public void onMouseScroll(double offsetX, double offsetY) {
    }

    /**
     * This method is invoked on setting {@link #framesPerSecond}.
     *
     * @param oldFps the previous fps.
     * @param newFps the current fps.
     */
    public void onFpsChanged(int oldFps, int newFps) {
    }

    /**
     * Exiting.
     */
    public void exit() {
    }

    /**
     * Launch this game with the implemented features.
     *
     * @param createInfo the creation info of this game.
     * @param app        the implementation of application.
     */
    public void run(GameCreateInfo createInfo, Application app) {
        ForkEngine.game = this;
        ForkEngine.application = app;
        application = app;
        app.setErrorCallback(Objects.requireNonNullElseGet(createInfo.errorCallback, app::defaultErrorCallback));
        if (!app.initGLFW()) {
            application = null;
            throw new IllegalStateException("Failed to initialize GLFW!");
        }
        try {
            window = app.createWindow(createInfo.width, createInfo.height, createInfo.title, createInfo.monitor, 0);
            if (window == null) {
                throw new IllegalStateException("Failed to create the GLFW window!");
            }
            window.setFramebufferSizeCb(this);
            window.setCursorListener(this);
            window.setKeyListener(this);
            width = window.framebufferWidth();
            height = window.framebufferHeight();
            try {
                preInit();
                defaultTimer = app.createTimer();
                window.makeContextCurrent();
                app.swapInterval(createInfo.swapInterval);
                ForkEngine.gl = app.loadOpenGL(true);
                init();
                int frames = 0;
                double lastTime = defaultTimer.getTimeSecond();
                while (!window.shouldClose()) {
                    defaultTimer.advanceTime();
                    for (int i = 0; i < defaultTimer.performTicks(); i++) {
                        fixedUpdate(defaultTimer.partialTick());
                    }
                    update(defaultTimer.deltaFrameTime());
                    lateUpdate(defaultTimer.deltaFrameTime());
                    render();
                    ++frames;
                    while (defaultTimer.getTimeSecond() >= lastTime + 1.0) {
                        onFpsChanged(framesPerSecond, frames);
                        framesPerSecond = frames;
                        lastTime += 1.0;
                        frames = 0;
                    }
                }
            } finally {
                exit();
                window.destroy();
            }
        } finally {
            app.terminateGLFW();
            app.setErrorCallback(null).close();
        }
        ForkEngine.game = null;
    }
}
