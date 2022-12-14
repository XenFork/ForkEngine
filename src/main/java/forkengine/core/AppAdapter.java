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

import java.util.Objects;

/**
 * The application adapter.
 *
 * @author squid233
 * @since 0.1.0
 */
public class AppAdapter implements ISized, ISizeListener, IMouseListener, IKeyListener {
    /**
     * The application implementation.
     */
    protected Application application;
    /**
     * The application adapter configurations.
     */
    protected AppConfig config;
    /**
     * The window instance.
     */
    protected Window window;
    /**
     * The framebuffer width of this application adapter.
     */
    protected int width;
    /**
     * The framebuffer height of this application adapter.
     */
    protected int height;

    /**
     * Early-initialization. This method is invoked before creating window.
     */
    public void earlyInit() {
    }

    /**
     * Pre-initialization. This method is invoked before initializing context.
     */
    public void preInit() {
    }

    /**
     * Initialization. This method is invoked after initializing context and before application adapter loop.
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
     * Rendering. You should use {@code super} call at the end of the overridden.
     *
     * @param partialTick {@link Timer#partialTick()}
     */
    public void render(double partialTick) {
        window.swapBuffers();
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
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
     * Closes the resource if it is not null.
     *
     * @param autoCloseable the resource.
     * @throws Exception if the resource cannot be closed.
     */
    public static void dispose(@Nullable AutoCloseable autoCloseable) throws Exception {
        if (autoCloseable != null) {
            autoCloseable.close();
        }
    }

    /**
     * Closes the resources.
     *
     * @param autoCloseables the resources.
     * @throws Exception if the resource cannot be closed.
     */
    public static void dispose(@Nullable AutoCloseable... autoCloseables) throws Exception {
        for (AutoCloseable autoCloseable : autoCloseables) {
            dispose(autoCloseable);
        }
    }

    /**
     * Exiting.
     */
    public void exit() throws Exception {
    }

    /**
     * Pre-loop.
     */
    protected void preLoop() {
    }

    /**
     * The loop body.
     */
    protected void loop() {
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    /**
     * Launch this application adapter with the implemented features.
     *
     * @param config the config of this application adapter.
     * @param app    the implementation of application.
     */
    public void run(AppConfig config, Application app) {
        ForkEngine.appAdapter = this;
        ForkEngine.application = app;
        application = app;
        this.config = config;
        app.setErrorCallback(Objects.requireNonNullElseGet(config.errorCallback, app::defaultErrorCallback));
        if (!app.initGLFW()) {
            application = null;
            throw new IllegalStateException("Failed to initialize GLFW!");
        }
        try {
            earlyInit();
            window = app.createWindow(config.width, config.height, config.title, config.monitor, 0);
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
                window.makeContextCurrent();
                app.swapInterval(config.swapInterval);
                ForkEngine.gl = app.loadOpenGL(true);
                init();
                preLoop();
                while (!window.shouldClose()) {
                    loop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                exit();
                window.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            app.terminateGLFW();
            try {
                dispose(app.setErrorCallback(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ForkEngine.appAdapter = null;
    }
}
