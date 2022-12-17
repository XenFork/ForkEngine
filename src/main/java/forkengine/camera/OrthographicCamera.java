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

package forkengine.camera;

/**
 * The camera with orthographic projection.
 *
 * @author squid233
 * @since 0.1.0
 */
public class OrthographicCamera extends PVCamera {
    /**
     * Sets this camera to be an orthographic projection transformation.
     *
     * @param left   the distance from the center to the left frustum edge.
     * @param right  the distance from the center to the right frustum edge.
     * @param bottom the distance from the center to the bottom frustum edge.
     * @param top    the distance from the center to the top frustum edge.
     * @param zNear  near clipping plane distance.
     * @param zFar   far clipping plane distance.
     * @return this.
     */
    public OrthographicCamera set(float left, float right, float bottom, float top, float zNear, float zFar) {
        projection().setOrtho(left, right, bottom, top, zNear, zFar);
        this.zNear = zNear;
        this.zFar = zFar;
        return this;
    }

    /**
     * Sets this camera to be an orthographic projection transformation.
     * <p>
     * This method is equivalent to calling {@link #set} with {@code zNear=-1} and {@code zFar=+1}.
     *
     * @param left   the distance from the center to the left frustum edge.
     * @param right  the distance from the center to the right frustum edge.
     * @param bottom the distance from the center to the bottom frustum edge.
     * @param top    the distance from the center to the top frustum edge.
     * @return this.
     */
    public OrthographicCamera set2D(float left, float right, float bottom, float top) {
        projection().setOrtho2D(left, right, bottom, top);
        this.zNear = -1.0f;
        this.zFar = 1.0f;
        return this;
    }

    /**
     * Sets this camera to be a symmetric orthographic projection transformation.
     * <p>
     * This method is equivalent to calling {@link #set} with {@code left=-width/2}, {@code right=+width/2},
     * {@code bottom=-height/2} and {@code top=+height/2}.
     *
     * @param width  the distance between the right and left frustum edges.
     * @param height the distance between the top and bottom frustum edges.
     * @param zNear  near clipping plane distance.
     * @param zFar   far clipping plane distance.
     * @return this.
     */
    public OrthographicCamera setSymmetric(float width, float height, float zNear, float zFar) {
        projection().setOrthoSymmetric(width, height, zNear, zFar);
        this.zNear = zNear;
        this.zFar = zFar;
        return this;
    }
}
