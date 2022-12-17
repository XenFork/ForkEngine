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

package forkengine.level.camera;

import org.joml.Matrix4f;

/**
 * The camera with projection and view matrix.
 *
 * @author squid233
 * @since 0.1.0
 */
public class PVCamera extends Camera {
    private final Matrix4f projection = new Matrix4f();
    private final Matrix4f view = new Matrix4f();

    /**
     * Gets the projection matrix.
     *
     * @return the projection matrix.
     */
    public Matrix4f projection() {
        return projection;
    }

    /**
     * Gets the view matrix.
     *
     * @return the view matrix.
     */
    public Matrix4f view() {
        return view;
    }

    @Override
    public Matrix4f applyMatrix(Matrix4f dest) {
        return dest.mul(projection).mul(view);
    }
}
