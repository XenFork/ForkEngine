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

/**
 * The game.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Game extends AppAdapter {
    /**
     * The default timer.
     */
    protected Timer defaultTimer;
    /**
     * The frames per second.
     */
    protected int framesPerSecond;
    private int frames;
    private double lastTime;

    @Override
    public void preInit() {
        super.preInit();
        defaultTimer = application.createTimer(config.ticksPerSecond);
    }

    @Override
    public void init() {
        super.init();
        onResize(0, 0, width, height);
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

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        ScreenUtil.viewport(0, 0, newWidth, newHeight);
        super.onResize(oldWidth, oldHeight, newWidth, newHeight);
    }

    /**
     * This method is invoked on setting {@link #framesPerSecond}.
     *
     * @param oldFps the previous fps.
     * @param newFps the current fps.
     */
    public void onFpsChanged(int oldFps, int newFps) {
    }

    @Override
    protected void preLoop() {
        super.preLoop();
        frames = 0;
        lastTime = defaultTimer.getTimeSecond();
    }

    @Override
    protected void loop() {
        super.loop();
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
}
