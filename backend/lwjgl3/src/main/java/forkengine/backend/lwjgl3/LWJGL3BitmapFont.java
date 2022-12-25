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

package forkengine.backend.lwjgl3;

import forkengine.asset.texture.Texture2D;
import forkengine.asset.texture.TextureData;
import forkengine.core.DataBuffer;
import forkengine.gl.GLStateManager;
import forkengine.graphics.batch.SpriteBatch;
import forkengine.graphics.font.Alignment;
import forkengine.graphics.font.BitmapFont;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.stb.STBTruetype.nstbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The bitmap font implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public class LWJGL3BitmapFont implements BitmapFont {
    private final STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
    private BitmapFont backendFont;
    private STBTTBakedChar.Buffer charData;
    private Texture2D texture;
    private int firstChar;
    private int width, height;

    @Override
    public void bakeTrueTypeFont(DataBuffer data, float pixelHeight, int bitmapW, int bitmapH, int firstChar, int charCount) {
        this.width = bitmapW;
        this.height = bitmapH;
        this.firstChar = firstChar;

        if (charData == null) {
            charData = STBTTBakedChar.malloc(charCount);
        } else if (charData.capacity() < charCount) {
            charData.close();
            charData = STBTTBakedChar.malloc(charCount);
        }

        int pixelsSize = bitmapW * bitmapH;
        ByteBuffer pixels = memAlloc(pixelsSize);
        ByteBuffer pixelDataBuf = memAlloc(pixelsSize * 4);
        nstbtt_BakeFontBitmap(data.address(), 0, pixelHeight, memAddress(pixels), bitmapW, bitmapH, firstChar, charCount, charData.address());
        for (int i = 0; i < pixelsSize; i++) {
            pixelDataBuf.put(i * 4, (byte) 0xFF)
                .put(i * 4 + 1, (byte) 0xFF)
                .put(i * 4 + 2, (byte) 0xFF)
                .put(i * 4 + 3, pixels.get(i));
        }

        int prevUnit = GLStateManager.activeTextureUnit2D();
        int prevTex = GLStateManager.boundTexture2D();
        if (texture == null) {
            texture = Texture2D.create();
        }
        texture.bind(0);
        //setParam
        try (TextureData textureData = TextureData.create().setAs(memAddress(pixelDataBuf), bitmapW, bitmapH)) {
            texture.specifyImage(1, textureData);
        }
        memFree(pixels);
        memFree(pixelDataBuf);
        texture.generateMipmap();
        Texture2D.bind(prevUnit, prevTex);
    }

    @Override
    public void setBackendFont(@Nullable BitmapFont backendFont) {
        this.backendFont = backendFont;
    }

    @Nullable
    @Override
    public BitmapFont backendFont() {
        return backendFont;
    }

    @Override
    public float textWidth(String text) {
        checkBaked();
        int codePointCount = text.codePointCount(0, text.length());
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer currX = stack.mallocFloat(1);
            FloatBuffer currY = stack.mallocFloat(1);
            for (int i = 0; i < codePointCount; i++) {
                int codePoint = text.codePointAt(i);
                stbtt_GetBakedQuad(charData, width, height, codePoint - firstChar, currX, currY, quad, true);
            }
            return quad.x1();
        }
    }

    private void checkBaked() {
        if (charData == null) {
            throw new IllegalStateException("BitmapFont must be baked before draw!");
        }
    }

    @Override
    public void draw(SpriteBatch batch, String text, float x, float y) {
        checkBaked();
        int codePointCount = text.codePointCount(0, text.length());
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer currX = stack.floats(x);
            FloatBuffer currY = stack.floats(y);
            for (int i = 0; i < codePointCount; i++) {
                int codePoint = text.codePointAt(i);
                stbtt_GetBakedQuad(charData, width, height, codePoint - firstChar, currX, currY, quad, true);
                final float x0 = quad.x0();
                final float y1 = quad.y1();
                batch.draw(texture,
                    x0, y - (y1 - y),
                    quad.x1() - x0, y1 - quad.y0(),
                    quad.s0(), quad.t1(),
                    quad.s1(), quad.t0());
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch, String text, float x, float y, int width, Alignment alignment) {
        switch (alignment) {
            case LEFT -> draw(batch, text, x, y);
            case CENTER -> draw(batch, text, (x + width - textWidth(text)) * 0.5f, y);
            case RIGHT -> draw(batch, text, x + width - textWidth(text), y);
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public void close() {
        quad.close();
        if (charData != null) {
            charData.close();
        }
        if (texture != null) {
            texture.close();
        }
    }
}
