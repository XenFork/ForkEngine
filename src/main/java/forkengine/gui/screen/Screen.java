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

package forkengine.gui.screen;

import forkengine.core.ForkEngine;
import forkengine.core.ISized;
import forkengine.core.Input;
import forkengine.core.Timer;
import forkengine.gui.XDrawable;
import forkengine.gui.XElement;
import forkengine.gui.XParentElement;
import forkengine.gui.button.XClickable;

import java.util.ArrayList;
import java.util.List;

/**
 * The screen.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Screen implements ISized, XParentElement, XTickableElement, XDrawable {
    /**
     * The width of this screen.
     */
    protected int width;
    /**
     * The height of this screen.
     */
    protected int height;
    protected final List<XElement> children = new ArrayList<>();
    protected final List<XClickable> clickableList = new ArrayList<>();

    /**
     * Opening the screen.
     */
    public void init() {
        width = ForkEngine.appAdapter.width();
        height = ForkEngine.appAdapter.height();
    }

    /**
     * Resizing the screen.
     *
     * @param width  the new width.
     * @param height the new height.
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Updating the screen.
     *
     * @param delta {@link Timer#deltaFrameTime()}
     */
    public void update(double delta) {
    }

    @Override
    public void fixedUpdate(double partialTick) {
    }

    /**
     * Rendering the screen.
     *
     * @param partialTick {@link Timer#partialTick()}
     * @param cursorX     the x position of the cursor.
     * @param cursorY     the y position of the cursor.
     */
    @Override
    public void render(double partialTick, double cursorX, double cursorY) {
        for (XElement element : children) {
            if (element instanceof XDrawable drawable) {
                drawable.render(partialTick, cursorX, cursorY);
            }
        }
    }

    /**
     * This method is invoked on removed.
     *
     * @param newScreen the new screen.
     */
    public void onRemoved(Screen newScreen) {
    }

    /**
     * This method is invoked on closing the screen with esc key.
     */
    public void onClose() {
    }

    /**
     * {@code true} if this screen should be closed on pressing esc key.
     *
     * @return {@code true} if this screen should be closed on pressing esc key.
     */
    public boolean shouldCloseOnEsc() {
        return true;
    }

    /**
     * Adds a clickable element.
     *
     * @param clickable the element.
     * @param <T>       the type of the element.
     * @return the element.
     */
    protected <T extends XClickable> T addClickable(T clickable) {
        clickableList.add(clickable);
        return addChild(clickable);
    }

    /**
     * Adds an element.
     *
     * @param child the element.
     * @param <T>   the type of the element.
     * @return the element.
     */
    protected <T extends XElement> T addChild(T child) {
        children.add(child);
        return child;
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (key == Input.KEY_ESCAPE) {
            onClose();
            return true;
        }
        return XParentElement.super.keyPressed(key, scancode, mods);
    }

    @Override
    public boolean mousePressed(double cursorX, double cursorY, int button) {
        boolean pressed = false;
        for (XClickable clickable : clickableList) {
            if (clickable.isCursorHover(cursorX, cursorY) && button == Input.MOUSE_BUTTON_LEFT) {
                clickable.onClick(cursorX, cursorY);
                pressed = true;
            }
        }
        return pressed;
    }

    @Override
    public List<XElement> children() {
        return children;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }
}
