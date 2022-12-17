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

package forkengine.graphics;

import org.joml.Math;

/**
 * The color.
 *
 * @author squid233
 * @since 0.1.0
 */
public class Color {
    /**
     * Converts the float color to byte.
     *
     * @param c the color value.
     * @return the converted value.
     */
    public static byte floatToByte(float c) {
        return (byte) (Math.min(c * 256.0f, 255.0f));
    }

    /**
     * Packs a color into a float that ordered in BGR.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @return the float value.
     */
    public static float pack(float r, float g, float b) {
        return Float.intBitsToFloat((floatToByte(b) << 16) | (floatToByte(g) << 8) | (floatToByte(r)));
    }

    /**
     * Gets the red component of the given packed color bits.
     *
     * @param bits the color bits.
     * @return the red component.
     */
    public static byte getRedComponent(int bits) {
        return (byte) bits;
    }

    /**
     * Gets the green component of the given packed color bits.
     *
     * @param bits the color bits.
     * @return the green component.
     */
    public static byte getGreenComponent(int bits) {
        return (byte) (bits >>> 8);
    }

    /**
     * Gets the blue component of the given packed color bits.
     *
     * @param bits the color bits.
     * @return the blue component.
     */
    public static byte getBlueComponent(int bits) {
        return (byte) (bits >>> 16);
    }
}
