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

package forkengine.gui;

/**
 * The gui element.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface XElement {
    /**
     * Returns {@code true} if the mouse button is pressed.
     *
     * @param cursorX the x position of the cursor.
     * @param cursorY the y position of the cursor.
     * @param button  the mouse button that is pressed.
     * @return {@code true} if the mouse button is pressed.
     */
    default boolean mousePressed(double cursorX, double cursorY, int button) {
        return false;
    }

    /**
     * Returns {@code true} if the key is pressed.
     *
     * @param key      the key.
     * @param scancode the scancode.
     * @param mods     the modifiers.
     * @return {@code true} if the key is pressed.
     */
    default boolean keyPressed(int key, int scancode, int mods) {
        return false;
    }

    /**
     * Returns {@code true} if the cursor is hovering on this element.
     *
     * @param cursorX the x position of the cursor.
     * @param cursorY the y position of the cursor.
     * @return {@code true} if the cursor is hovering on this element.
     */
    default boolean isCursorHover(double cursorX, double cursorY) {
        return false;
    }
}
