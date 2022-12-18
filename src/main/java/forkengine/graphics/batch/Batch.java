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

package forkengine.graphics.batch;

import forkengine.asset.shader.Shader;
import forkengine.asset.texture.Texture2D;
import forkengine.gl.IGL;
import forkengine.graphics.Color;
import org.joml.Matrix4f;

/**
 * The base batch.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface Batch extends AutoCloseable {
    void begin();

    /**
     * Finishes off rendering. Enables depth writes, disables blending and texturing. Must always be called after a call to
     * {@link #begin()}.
     */
    void end();

    /**
     * Sets the color used to tint images when they are added to the Batch.
     *
     * @param tint the color to be set. defaults to {@link Color#WHITE}.
     */
    void setColor(Color tint);

    /**
     * Sets the color used to tint images when they are added to the Batch.
     *
     * @param r the red value.
     * @param g the green value.
     * @param b the blue value.
     * @param a the alpha value.
     * @see #setColor(Color)
     */
    void setColor(float r, float g, float b, float a);

    /**
     * Returns the rendering color of this {@code Batch}.
     *
     * @return the rendering color of this {@code Batch}. If the returned instance is manipulated,
     * {@link #setColor(Color)} must be called afterward.
     */
    Color getColor();

    /**
     * Sets the rendering color of this {@code Batch}, expanding the alpha from 0-254 to 0-255.
     *
     * @param packedColor the packed color.
     * @see #setColor(Color)
     * @see Color#toFloatBits()
     */
    void setPackedColor(float packedColor);

    /**
     * Returns the rendering color of this {@code Batch} in vertex format (alpha compressed to 0-254).
     *
     * @return the rendering color of this {@code Batch} in vertex format (alpha compressed to 0-254)
     * @see Color#toFloatBits()
     */
    float getPackedColor();

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The rectangle is offset
     * by originX, originY relative to the origin. Scale specifies the scaling factor by which the rectangle should be scaled
     * around originX, originY. Rotation specifies the angle of counterclockwise rotation of the rectangle around originX,
     * originY. The portion of the {@link Texture2D} given by srcX, srcY and srcWidth, srcHeight is used. These coordinates and sizes
     * are given in texels. FlipX and flipY specify whether the texture portion should be flipped horizontally or vertically.
     *
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param originX   the x-coordinate of the scaling and rotation origin relative to the screen space coordinates
     * @param originY   the y-coordinate of the scaling and rotation origin relative to the screen space coordinates
     * @param width     the width in pixels
     * @param height    the height in pixels
     * @param scaleX    the scale of the rectangle around originX/originY in x
     * @param scaleY    the scale of the rectangle around originX/originY in y
     * @param rotation  the angle of counterclockwise rotation of the rectangle around originX/originY
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     * @param flipX     whether to flip the sprite horizontally
     * @param flipY     whether to flip the sprite vertically
     */
    void draw(Texture2D texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
              float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY);

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture2D} given by srcX, srcY and srcWidth, srcHeight is used. These coordinates and sizes are given in texels. FlipX
     * and flipY specify whether the texture portion should be flipped horizontally or vertically.
     *
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param width     the width in pixels
     * @param height    the height in pixels
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     * @param flipX     whether to flip the sprite horizontally
     * @param flipY     whether to flip the sprite vertically
     */
    void draw(Texture2D texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth,
              int srcHeight, boolean flipX, boolean flipY);

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture2D} given by srcX, srcY and srcWidth, srcHeight are used. These coordinates and sizes are given in texels.
     *
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     */
    void draw(Texture2D texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight);

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture2D} given by u, v and u2, v2 are used. These coordinates and sizes are given in texture size percentage. The
     * rectangle will have the given tint {@link Color}.
     *
     * @param x      the x-coordinate in screen space
     * @param y      the y-coordinate in screen space
     * @param width  the width in pixels
     * @param height the height in pixels
     */
    void draw(Texture2D texture, float x, float y, float width, float height, float u, float v, float u2, float v2);

    /**
     * Draws a rectangle with the bottom left corner at x,y having the width and height of the texture.
     *
     * @param x the x-coordinate in screen space
     * @param y the y-coordinate in screen space
     */
    void draw(Texture2D texture, float x, float y);

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height.
     */
    void draw(Texture2D texture, float x, float y, float width, float height);

    /**
     * Draws a rectangle using the given vertices. There must be 4 vertices, each made up of 5 elements in this order: x, y,
     * color, u, v. The {@link #getColor()} from the Batch is not applied.
     */
    void draw(Texture2D texture, float[] spriteVertices, int offset, int count);

    /**
     * Causes any pending sprites to be rendered, without ending the Batch.
     */
    void flush();

    /**
     * Enables blending for drawing sprites. Calling this within {@link #begin()}/{@link #end()} will flush the batch.
     */
    void enableBlend();

    /**
     * Disables blending for drawing sprites. Calling this within {@link #begin()}/{@link #end()} will flush the batch.
     */
    void disableBlend();

    /**
     * Sets the blending function to be used when rendering sprites.
     *
     * @param srcFunc the source function, e.g. {@link IGL#SRC_ALPHA}. If set to -1, {@code Batch} won't change the blending function.
     * @param dstFunc the destination function, e.g. {@link IGL#ONE_MINUS_SRC_ALPHA}
     */
    void setBlendFunction(int srcFunc, int dstFunc);

    /**
     * Sets separate (color/alpha) blending function to be used when rendering sprites.
     *
     * @param srcFuncColor the source color function, e.g. {@link IGL#SRC_ALPHA}. If set to -1, {@code Batch} won't change
     *                     the blending function.
     * @param dstFuncColor the destination color function, e.g. {@link IGL#ONE_MINUS_SRC_ALPHA}.
     * @param srcFuncAlpha the source alpha function, e.g. {@link IGL#SRC_ALPHA}.
     * @param dstFuncAlpha the destination alpha function, e.g. {@link IGL#ONE_MINUS_SRC_ALPHA}.
     */
    void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha);

    /**
     * Gets the blend source color function.
     *
     * @return the blend source color function.
     */
    int getBlendSrcFunc();

    /**
     * Gets the blend destination color function.
     *
     * @return the blend destination color function.
     */
    int getBlendDstFunc();

    /**
     * Gets the blend source alpha function.
     *
     * @return the blend source alpha function.
     */
    int getBlendSrcFuncAlpha();

    /**
     * Gets the blend destination alpha function.
     *
     * @return the blend destination alpha function.
     */
    int getBlendDstFuncAlpha();

    /**
     * Returns the current projection matrix. Changing this within {@link #begin()}/{@link #end()} results in undefined
     * behaviour.
     */
    Matrix4f getProjectionMatrix();

    /**
     * Returns the current model matrix. Changing this within {@link #begin()}/{@link #end()} results in undefined
     * behaviour.
     */
    Matrix4f getModelMatrix();

    /**
     * Sets the projection matrix to be used by this Batch. If this is called inside a {@link #begin()}/{@link #end()} block, the
     * current batch is flushed to the gpu.
     *
     * @param projection the new projection matrix.
     */
    void setProjectionMatrix(Matrix4f projection);

    /**
     * Sets the model matrix to be used by this Batch.
     *
     * @param model the new model matrix.
     */
    void setModelMatrix(Matrix4f model);

    /**
     * Sets the shader to be used in a GL 3.2 environment.
     * <p>
     * Vertex position attribute is called {@code fe_Position}, the texture
     * coordinates attribute is called {@code fe_TexCoord0}, the color attribute is called {@code fe_Color}. See
     * {@link Shader#POSITION_ATTRIBUTE}, {@link Shader#COLOR_ATTRIBUTE} and {@link Shader#TEX_COORD_ATTRIBUTE}
     * which gets "0" appended to indicate the use of the first texture unit.
     * <p>
     * The combined model and projection matrix is uploaded via a mat4 uniform called {@code u_ProjModelMat}.
     * The texture sampler is passed via a uniform called {@code u_Sampler0}.
     * <p>
     * Call this method with a null argument to use the default shader.
     * <p>
     * This method will flush the batch before setting the new shader, you can call it in between {@link #begin()} and
     * {@link #end()}.
     *
     * @param shader the {@link Shader} or null to use the default shader.
     */
    void setShader(Shader shader);

    /**
     * @return the current {@link Shader} set by {@link #setShader(Shader)} or the defaultShader.
     */
    Shader getShader();

    /**
     * Returns true if blending for sprites is enabled.
     *
     * @return true if blending for sprites is enabled.
     */
    boolean isBlendEnabled();

    /**
     * Returns true if currently between begin and end.
     *
     * @return true if currently between begin and end.
     */
    boolean isDrawing();

    @Override
    void close();
}
