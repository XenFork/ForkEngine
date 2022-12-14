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
import java.util.function.ObjIntConsumer;
import java.util.function.ToLongFunction;

import static forkengine.core.ForkEngine.gl;

/**
 * The vertex layout.
 *
 * @author squid233
 * @since 0.1.0
 */
public sealed abstract class VertexLayout permits VertexLayout.Flat, VertexLayout.Interleaved {
    protected final Map<String, VertexElement> elements = new LinkedHashMap<>();
    protected final Map<VertexElement, Integer> elementIndex = new LinkedHashMap<>();
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
     * Creates the flat vertex layout with the given vertex elements.
     *
     * @param elements the vertex elements.
     * @return the vertex layout.
     */
    public static Flat flat(VertexElement... elements) {
        var flat = flat();
        for (VertexElement element : elements) {
            flat.addElement(element);
        }
        return flat;
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
     * Creates the interleaved vertex layout with the given vertex elements.
     *
     * @param elements the vertex elements.
     * @return the vertex layout.
     */
    public static Interleaved interleaved(VertexElement... elements) {
        var interleaved = interleaved();
        for (VertexElement element : elements) {
            interleaved.addElement(element);
        }
        return interleaved;
    }

    /**
     * The flat vertex layout.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static final class Flat extends VertexLayout {
        @Override
        public Flat addElement(VertexElement element) {
            super.addElement(element);
            return this;
        }

        @Override
        public void pointer(VertexElement element, long pointer) {
            pointer(element, element.bytesSize(), pointer);
        }

        /**
         * Specifies the location and organization of a vertex attribute array.
         *
         * @param pointer the offset getter for each vertex element.
         */
        public void pointerAll(ToLongFunction<VertexElement> pointer) {
            forEach((element, index) -> element.pointer(index, element.bytesSize(), pointer.applyAsLong(element)));
        }

        /**
         * Specifies the location and organization of a vertex attribute array.
         *
         * @param arraySize the length of the array for each element.
         */
        public void pointerAll(int arraySize) {
            long pointer = 0;
            for (var e : elementIndex.entrySet()) {
                VertexElement element = e.getKey();
                int index = e.getValue();
                element.pointer(index, element.bytesSize(), pointer);
                pointer += (long) element.bytesSize() * arraySize;
            }
        }
    }

    /**
     * The interleaved vertex layout.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static final class Interleaved extends VertexLayout {
        private int stride;

        @Override
        public Interleaved addElement(VertexElement element) {
            super.addElement(element);
            stride += element.bytesSize();
            return this;
        }

        @Override
        public void pointer(VertexElement element, long pointer) {
            pointer(element, stride(), pointer);
        }

        /**
         * Specifies the location and organization of a vertex attribute array.
         */
        public void pointerAll() {
            long pointer = 0;
            for (var e : elementIndex.entrySet()) {
                VertexElement element = e.getKey();
                int index = e.getValue();
                element.pointer(index, stride(), pointer);
                pointer += element.bytesSize();
            }
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
     * Specifies the location and organization of a vertex element.
     *
     * @param element the vertex element.
     * @param index   the index of the generic vertex attribute to be modified.
     * @param stride  the byte offset between consecutive generic vertex attributes. If stride is 0, the generic vertex attributes
     *                are understood to be tightly packed in the array. The initial value is 0.
     * @param pointer the vertex attribute data or the offset of the first component of the first generic vertex attribute
     *                in the array in the data store of the buffer currently bound to the {@link forkengine.gl.IGL#ARRAY_BUFFER ARRAY_BUFFER} target. The initial value is 0.
     * @see #pointer(VertexElement, int, long)
     */
    protected void pointer(VertexElement element, int index, int stride, long pointer) {
        element.pointer(index, stride, pointer);
    }

    /**
     * Specifies the location and organization of a vertex element.
     *
     * @param element the vertex element.
     * @param stride  the byte offset between consecutive generic vertex attributes. If stride is 0, the generic vertex attributes
     *                are understood to be tightly packed in the array. The initial value is 0.
     * @param pointer the vertex attribute data or the offset of the first component of the first generic vertex attribute
     *                in the array in the data store of the buffer currently bound to the {@link forkengine.gl.IGL#ARRAY_BUFFER ARRAY_BUFFER} target. The initial value is 0.
     * @see #pointer(VertexElement, int, int, long)
     */
    public void pointer(VertexElement element, int stride, long pointer) {
        pointer(element, getIndex(element), stride, pointer);
    }

    /**
     * Specifies the location and organization of a vertex element.
     *
     * @param element the vertex element.
     * @param pointer the vertex attribute data or the offset of the first component of the first generic vertex attribute
     *                in the array in the data store of the buffer currently bound to the {@link forkengine.gl.IGL#ARRAY_BUFFER ARRAY_BUFFER} target. The initial value is 0.
     * @see #pointer(VertexElement, int, int, long)
     */
    public abstract void pointer(VertexElement element, long pointer);

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

    private void checkElement(VertexElement element) {
        if (!elementIndex.containsKey(element))
            throw new IllegalArgumentException("the element is not exists in this layout! got: " + element);
    }

    /**
     * Enables a generic vertex attribute array.
     *
     * @param element the vertex elements to be enabled.
     */
    public void enable(VertexElement element) {
        checkElement(element);
        gl.enableVertexAttribArray(getIndex(element));
    }

    /**
     * Enables all vertex elements.
     */
    public void enableAll() {
        elementIndex.keySet().forEach(this::enable);
    }

    /**
     * Disables a generic vertex attribute array.
     *
     * @param element the vertex elements to be disabled.
     */
    public void disable(VertexElement element) {
        checkElement(element);
        gl.disableVertexAttribArray(getIndex(element));
    }

    /**
     * Disables all vertex elements.
     */
    public void disableAll() {
        elementIndex.keySet().forEach(this::disable);
    }

    /**
     * Performs the given action for each vertex element in this vertex layout until all elements have been processed
     * or the action throws an exception.
     *
     * @param action The action to be performed for each entry.
     */
    public void forEach(ObjIntConsumer<VertexElement> action) {
        elementIndex.forEach(action::accept);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VertexLayout.class.getSimpleName() + "[", "]")
            .add("elements=" + elements)
            .add("elementIndex=" + elementIndex)
            .add("nextIndex=" + nextIndex)
            .toString();
    }
}
