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

import java.util.List;
import java.util.Optional;

/**
 * The element that contains child elements.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface XParentElement extends XElement {
    /**
     * Gets the children elements.
     *
     * @return the children elements.
     */
    List<? extends XElement> children();

    /**
     * Gets the hovered element with the given cursor position.
     *
     * @param cursorX the x position of the cursor.
     * @param cursorY the y position of the cursor.
     * @return the hovered element, or empty if no element was hovered.
     */
    default Optional<XElement> hoveredElement(double cursorX, double cursorY) {
        for (XElement element : children()) {
            if (element.isCursorHover(cursorX, cursorY)) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns {@code true} if the mouse button is pressed and any element was performed.
     *
     * @param cursorX the x position of the cursor.
     * @param cursorY the y position of the cursor.
     * @param button  the mouse button that is pressed.
     * @return {@code true} if the mouse button is pressed.
     */
    @Override
    default boolean mousePressed(double cursorX, double cursorY, int button) {
        for (XElement element : children()) {
            if (element.mousePressed(cursorX, cursorY, button)) {
                return true;
            }
        }
        return false;
    }
}
