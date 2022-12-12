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

package forkengine.level.model;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * The vertex layout.
 *
 * @author squid233
 * @since 0.1.0
 */
public class VertexLayout {
    protected final Map<String, VertexElement> elements = new LinkedHashMap<>();
    protected final Map<VertexElement, Integer> elementIndex = new LinkedHashMap<>();
    protected int stride = 0;
    private int nextIndex = 0;

    /**
     * Creates the flat vertex layout.
     *
     * @return the vertex layout.
     */
    public static Flat flat() {
        return new Flat();
    }

    /**
     * Creates the interleaved vertex layout.
     *
     * @return the vertex layout.
     */
    public static Interleaved interleaved() {
        return new Interleaved();
    }

    /**
     * The flat vertex layout.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static class Flat extends VertexLayout {
        @Override
        public Flat addElement(VertexElement element) {
            super.addElement(element);
            return this;
        }
    }

    /**
     * The interleaved vertex layout.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static class Interleaved extends VertexLayout {
        @Override
        public Interleaved addElement(VertexElement element) {
            super.addElement(element);
            stride += element.dataType().bytesSize() * element.count();
            return this;
        }

        /**
         * Gets the stride.
         *
         * @return the stride.
         */
        public int stride() {
            return stride;
        }
    }

    /**
     * Adds a vertex element to this layout.
     *
     * @param element the vertex element.
     * @return this.
     */
    public VertexLayout addElement(VertexElement element) {
        elements.put(element.name(), element);
        int index = element.index();
        if (index < 0) {
            index = nextIndex;
        } else if (index > nextIndex) {
            nextIndex = index;
        }
        elementIndex.put(element, index);
        nextIndex++;
        return this;
    }

    /**
     * Gets the vertex element by the given name.
     *
     * @param name the name of vertex element.
     * @return the vertex element.
     */
    public @Nullable VertexElement getElement(String name) {
        return elements.get(name);
    }

    /**
     * Gets the index by the given vertex element.
     *
     * @param element the vertex element.
     * @return the index.
     */
    public int getIndex(VertexElement element) {
        return elementIndex.get(element);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VertexLayout.class.getSimpleName() + "[", "]")
            .add("elementIndex=" + elementIndex)
            .add("stride=" + stride)
            .add("nextIndex=" + nextIndex)
            .toString();
    }
}
