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

package forkengine.graphics.font;

import forkengine.asset.FileProvider;
import forkengine.core.DataBuffer;
import forkengine.core.ForkEngine;
import forkengine.core.ISized;
import forkengine.graphics.batch.SpriteBatch;
import org.jetbrains.annotations.Nullable;

/**
 * The base bitmap font that is baked from a true-type font.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface BitmapFont extends ISized, AutoCloseable {
    /**
     * Creates a bitmap font.
     *
     * @return the bitmap font.
     */
    static BitmapFont create() {
        return ForkEngine.application.newBitmapFont();
    }

    /**
     * Bakes a true-type font to a bitmap.
     *
     * @param data        the font data.
     * @param pixelHeight the glyph height in pixels.
     * @param bitmapW     the bitmap width.
     * @param bitmapH     the bitmap height.
     * @param firstChar   the first character to be baked.
     * @param charCount   the character count to be baked.
     */
    void bakeTrueTypeFont(DataBuffer data, float pixelHeight, int bitmapW, int bitmapH, int firstChar, int charCount);

    /**
     * Bakes a true-type font to a bitmap.
     *
     * @param provider    the file provider.
     * @param filename    the font filename.
     * @param bufferSize  the initial buffer size.
     * @param pixelHeight the glyph height in pixels.
     * @param bitmapW     the bitmap width.
     * @param bitmapH     the bitmap height.
     * @param firstChar   the first character to be baked.
     * @param charCount   the character count to be baked.
     */
    default void bakeTrueTypeFont(FileProvider provider, String filename, long bufferSize,
                                  float pixelHeight, int bitmapW, int bitmapH, int firstChar, int charCount) {
        DataBuffer buffer = provider.toDataBuffer(filename, bufferSize);
        bakeTrueTypeFont(buffer, pixelHeight, bitmapW, bitmapH, firstChar, charCount);
        buffer.free();
    }

    /**
     * Sets a backend font, used if some glyphs are empty in this font.
     *
     * @param backendFont the backend font.
     */
    void setBackendFont(@Nullable BitmapFont backendFont);

    /**
     * Gets the backend font.
     *
     * @return the backend font.
     */
    @Nullable BitmapFont backendFont();

    /**
     * Gets the width of the given text.
     *
     * @param text the text.
     * @return the text width.
     */
    float textWidth(String text);

    /**
     * Draws a text.
     *
     * @param batch the sprite batch.
     * @param text  the text.
     * @param x     the x position.
     * @param y     the x position.
     */
    void draw(SpriteBatch batch, String text, float x, float y);

    /**
     * Draws a text with the given alignment.
     *
     * @param batch     the sprite batch.
     * @param text      the text.
     * @param x         the x position.
     * @param y         the x position.
     * @param width     the text width.
     * @param alignment the alignment.
     */
    void draw(SpriteBatch batch, String text, float x, float y, int width, Alignment alignment);
}
