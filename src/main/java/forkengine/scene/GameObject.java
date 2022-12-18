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

package forkengine.scene;

import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

/**
 * The game object.
 *
 * @author squid233
 * @since 0.1.0
 */
public class GameObject {
    /**
     * The previous position.
     */
    public static final String COMP_PREV_POSITION = "fe_GameObject.PrevPosition";
    /**
     * The position.
     */
    public static final String COMP_POSITION = "fe_GameObject.Position";
    private final Map<String, Object> components = new HashMap<>();
    private final Vector3f prevPosition = new Vector3f();
    private final Vector3f position = new Vector3f();

    /**
     * Creates the game object.
     */
    public GameObject() {
        addComponent(COMP_PREV_POSITION, prevPosition);
        addComponent(COMP_POSITION, position);
    }

    /**
     * Gets a component by the given name.
     *
     * @param name the name of the component.
     * @param <T>  the type of the component.
     * @return the component.
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(String name) {
        Object comp = components.get(name);
        return comp != null ? (T) comp : null;
    }

    /**
     * Adds a component with the given name.
     *
     * @param name the name of the component.
     * @param comp the component.
     * @return this.
     */
    public GameObject addComponent(String name, Object comp) {
        components.put(name, comp);
        return this;
    }

    /**
     * Gets the previous position of this game object.
     *
     * @return the previous position.
     */
    public Vector3f prevPosition() {
        return prevPosition;
    }

    /**
     * Gets the position of this game object.
     *
     * @return the position.
     */
    public Vector3f position() {
        return position;
    }
}
