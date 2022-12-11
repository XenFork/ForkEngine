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

import org.joml.Math;

/**
 * The timer.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class Timer {
    private int ticksPerSecond;
    private double lastTime;
    private double passedTime;
    private double deltaFrameTime;
    private double partialTick;
    private double timescale = 1.0;
    private int performTicks;
    private int maxPerformTicks = 250;

    /**
     * Gets the current time in seconds.
     *
     * @return the current time.
     */
    public abstract double getTimeSecond();

    /**
     * Creates the timer with the given tps.
     *
     * @param ticksPerSecond the ticks per second. defaults to 50.
     */
    public Timer(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }

    /**
     * Creates the timer.
     */
    public Timer() {
        this(50);
    }

    /**
     * Advances the current time.
     */
    public void advanceTime() {
        double currentTime = getTimeSecond();
        double frameTime = currentTime - lastTime;
        deltaFrameTime = frameTime;
        lastTime = currentTime;
        frameTime = Math.clamp(0.0, 1.0, frameTime);
        passedTime += frameTime * timescale * ticksPerSecond;
        performTicks = Math.clamp(0, maxPerformTicks, (int) passedTime);
        passedTime -= performTicks;
        partialTick = passedTime;
    }

    /**
     * Gets ticks per second.
     *
     * @return ticks per second.
     */
    public int ticksPerSecond() {
        return ticksPerSecond;
    }

    /**
     * Sets ticks per second.
     *
     * @param tps ticks per second.
     */
    public void setTicksPerSecond(int tps) {
        ticksPerSecond = tps <= 0 ? 50 : tps;
    }

    /**
     * Gets the passed time from the previous frame to the current frame.
     *
     * @return the passed frame time.
     */
    public double deltaFrameTime() {
        return deltaFrameTime;
    }

    /**
     * Gets the percentage of the ticks performed.
     *
     * @return the partial tick, that <code>partialTick * {@link #ticksPerSecond() tps} = currentTick</code>.
     */
    public double partialTick() {
        return partialTick;
    }

    /**
     * Gets the timescale.
     *
     * @return the timescale.
     */
    public double timescale() {
        return timescale;
    }

    /**
     * Sets the timescale. For example, 0.0 for pause, 1.0 for normal and 2.0 for 2x speed.
     *
     * @param timescale the new timescale.
     */
    public void setTimescale(double timescale) {
        this.timescale = Math.max(timescale, 0.0);
    }

    /**
     * Gets the tick count that should be performed at this frame.
     *
     * @return the tick count
     */
    public int performTicks() {
        return performTicks;
    }

    /**
     * Gets the max performed tick count.
     *
     * @return the max performed tick count
     */
    public int maxPerformTicks() {
        return maxPerformTicks;
    }

    /**
     * Sets the max performed tick count.
     *
     * @param maxPerformTicks the max performed tick count
     */
    public void setMaxPerformTicks(int maxPerformTicks) {
        this.maxPerformTicks = maxPerformTicks <= 0 ? 250 : maxPerformTicks;
    }
}
