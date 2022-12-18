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
     * The white color.
     */
    public static final Color WHITE = new Color(1, 1, 1, 1);
    /**
     * The packed white color bits.
     */
    public static final float WHITE_PACKED = WHITE.toFloatBits();
    /**
     * The gray colors.
     */
    public static final Color LIGHT_GRAY = new Color(0xbfbfbfff),
        GRAY = new Color(0x7f7f7fff),
        DARK_GRAY = new Color(0x3f3f3fff),
        BLACK = new Color(0, 0, 0, 1);
    /**
     * The color with 0 alpha.
     */
    public static final Color CLEAR = new Color(0, 0, 0, 0);
    /**
     * The blue colors.
     */
    public static final Color BLUE = new Color(0, 0, 1, 1),
        NAVY = new Color(0, 0, 0.5f, 1),
        ROYAL = new Color(0x4169e1ff),
        SLATE = new Color(0x708090ff),
        SKY = new Color(0x87ceebff),
        CYAN = new Color(0, 1, 1, 1),
        TEAL = new Color(0, 0.5f, 0.5f, 1);
    /**
     * The green colors.
     */
    public static final Color GREEN = new Color(0x00ff00ff),
        CHARTREUSE = new Color(0x7fff00ff),
        LIME = new Color(0x32cd32ff),
        FOREST = new Color(0x228b22ff),
        OLIVE = new Color(0x6b8e23ff);
    /**
     * The yellow colors.
     */
    public static final Color YELLOW = new Color(0xffff00ff),
        GOLD = new Color(0xffd700ff),
        GOLDENROD = new Color(0xdaa520ff),
        ORANGE = new Color(0xffa500ff);
    /**
     * The brown colors.
     */
    public static final Color BROWN = new Color(0x8b4513ff),
        TAN = new Color(0xd2b48cff),
        FIREBRICK = new Color(0xb22222ff);
    /**
     * The red colors.
     */
    public static final Color RED = new Color(0xff0000ff),
        SCARLET = new Color(0xff341cff),
        CORAL = new Color(0xff7f50ff),
        SALMON = new Color(0xfa8072ff),
        PINK = new Color(0xff69b4ff),
        MAGENTA = new Color(1, 0, 1, 1);
    /**
     * The purple colors.
     */
    public static final Color PURPLE = new Color(0xa020f0ff),
        VIOLET = new Color(0xee82eeff),
        MAROON = new Color(0xb03060ff);

    private float r, g, b, a;

    /**
     * Creates a color with the given component values.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @param a the alpha value. defaults to 1.
     */
    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Creates a color with the given component values.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     */
    public Color(float r, float g, float b) {
        this(r, g, b, 1.0f);
    }

    /**
     * Creates a color with the given hex value.
     *
     * @param rgba the hex color value ordered in RGBA.
     */
    public Color(int rgba) {
        this(intToFloat(rgba >>> 24), intToFloat(rgba >>> 16), intToFloat(rgba >>> 8), intToFloat(rgba));
    }

    /**
     * Creates a color. All components are initial to 0.
     */
    public Color() {
    }

    /**
     * Converts the byte color to float.
     *
     * @param c the color value.
     * @return the converted value.
     */
    public static float byteToFloat(byte c) {
        return Math.min(255, c) / 255.0f;
    }

    /**
     * Converts the int color in range [0,255] to float.
     *
     * @param c the color value.
     * @return the converted value.
     */
    public static float intToFloat(int c) {
        return byteToFloat((byte) c);
    }

    /**
     * Converts the float color to int in range [0,255].
     *
     * @param c the color value.
     * @return the converted value.
     */
    public static int floatToInt(float c) {
        return (int) (Math.min(c * 256.0f, 255.0f));
    }

    /**
     * Converts the float color to byte.
     *
     * @param c the color value.
     * @return the converted value.
     */
    public static byte floatToByte(float c) {
        return (byte) floatToInt(c);
    }

    /**
     * Packs a color into a float that ordered in ABGR.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @param a the alpha value. defaults to 1.
     * @return the float value.
     */
    public static float pack(float r, float g, float b, float a) {
        // This mask avoids using bits in the NaN range. See Float.intBitsToFloat javadocs.
        // This unfortunately means we don't get the full range of alpha.
        return Float.intBitsToFloat(((floatToInt(a) << 24) | (floatToInt(b) << 16) | (floatToInt(g) << 8) | (floatToInt(r))) & 0xfeffffff);
    }

    /**
     * Packs a color into a float that ordered in ABGR.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @return the float value.
     */
    public static float pack(float r, float g, float b) {
        return pack(r, g, b, 1.0f);
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

    /**
     * Gets the alpha component of the given packed color bits.
     *
     * @param bits the color bits.
     * @return the blue component.
     */
    public static byte getAlphaComponent(int bits) {
        return (byte) (bits >>> 24);
    }

    /**
     * Converts the packed color that ordered in ABGR to color.
     *
     * @param dest        the destination.
     * @param packedColor the packed color.
     */
    public static void abgr8888ToColor(Color dest, float packedColor) {
        int bits = Float.floatToRawIntBits(packedColor);
        dest.set(
            byteToFloat(getRedComponent(bits)),
            byteToFloat(getGreenComponent(bits)),
            byteToFloat(getBlueComponent(bits)),
            byteToFloat(getAlphaComponent(bits))
        );
    }

    /**
     * Sets this with the given component values.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @param a the alpha value.
     */
    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Sets this with the given component values.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     */
    public void set(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Sets this with the given color.
     *
     * @param color the other color.
     */
    public void set(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
    }

    /**
     * Gets the red component.
     *
     * @return the red value.
     */
    public float red() {
        return r;
    }

    /**
     * Gets the green component.
     *
     * @return the green value.
     */
    public float green() {
        return g;
    }

    /**
     * Gets the blue component.
     *
     * @return the blue value.
     */
    public float blue() {
        return b;
    }

    /**
     * Gets the alpha component.
     *
     * @return the alpha value.
     */
    public float alpha() {
        return a;
    }

    /**
     * Packs this color to float bits.
     *
     * @return the bits.
     */
    public float toFloatBits() {
        return pack(r, g, b, a);
    }
}
