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

package forkengine.gl;

import static forkengine.core.ForkEngine.gl;

/**
 * The OpenGL state manager.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class GLStateManager {
    private static int programId = 0;
    private static int vertexArray = 0;
    private static int activeTextureUnit2D = 0;
    private static int boundTexture2D = 0;
    private static int activeTextureUnit3D = 0;
    private static int boundTexture3D = 0;
    private static int activeTextureUnitCubeMap = 0;
    private static int boundTextureCubeMap = 0;
    private static boolean enabledBlend = false;
    private static int blendEquationRGB = IGL.FUNC_ADD;
    private static int blendEquationAlpha = IGL.FUNC_ADD;
    private static int blendFuncSrcRGB = IGL.ONE;
    private static int blendFuncDstRGB = IGL.ZERO;
    private static int blendFuncSrcAlpha = IGL.ONE;
    private static int blendFuncDstAlpha = IGL.ZERO;

    /**
     * Installs a program object as part of current rendering state.
     *
     * @param program the program object whose executables are to be used as part of current rendering state.
     */
    public static void useProgram(int program) {
        if (programId != program) {
            programId = program;
            gl.useProgram(program);
        }
    }

    /**
     * Binds a vertex array object.
     *
     * @param array the name of the vertex array to bind.
     */
    public static void bindVertexArray(int array) {
        if (vertexArray != array) {
            vertexArray = array;
            gl.bindVertexArray(array);
        }
    }

    /**
     * Binds a 2D texture.
     *
     * @param unit    the texture unit starting at 0.
     * @param texture the texture id.
     */
    public static void bindTexture2D(int unit, int texture) {
        if (activeTextureUnit2D != unit || boundTexture2D != texture) {
            activeTextureUnit2D = unit;
            boundTexture2D = texture;
            gl.bindTexture(IGL.TEXTURE_2D, unit, texture);
        }
    }

    /**
     * Binds a 3D texture.
     *
     * @param unit    the texture unit starting at 0.
     * @param texture the texture id.
     */
    public static void bindTexture3D(int unit, int texture) {
        if (activeTextureUnit3D != unit || boundTexture3D != texture) {
            activeTextureUnit3D = unit;
            boundTexture3D = texture;
            gl.bindTexture(IGL.TEXTURE_3D, unit, texture);
        }
    }

    /**
     * Binds a cube-map texture.
     *
     * @param unit    the texture unit starting at 0.
     * @param texture the texture id.
     */
    public static void bindTextureCubeMap(int unit, int texture) {
        if (activeTextureUnitCubeMap != unit || boundTextureCubeMap != texture) {
            activeTextureUnitCubeMap = unit;
            boundTextureCubeMap = texture;
            gl.bindTexture(IGL.TEXTURE_CUBE_MAP, unit, texture);
        }
    }

    /**
     * Gets the active 2D texture unit.
     *
     * @return the active 2D texture unit.
     */
    public static int activeTextureUnit2D() {
        return activeTextureUnit2D;
    }

    /**
     * Gets the active 3D texture unit.
     *
     * @return the active 3D texture unit.
     */
    public static int activeTextureUnit3D() {
        return activeTextureUnit3D;
    }

    /**
     * Gets the active cube-map texture unit.
     *
     * @return the active cube-map texture unit.
     */
    public static int activeTextureUnitCubeMap() {
        return activeTextureUnitCubeMap;
    }

    /**
     * Gets the bound 2D texture id.
     *
     * @return the bound 2D texture id.
     */
    public static int boundTexture2D() {
        return boundTexture2D;
    }

    /**
     * Gets the bound 3D texture id.
     *
     * @return the bound 3D texture id.
     */
    public static int boundTexture3D() {
        return boundTexture3D;
    }

    /**
     * Gets the bound cube-map texture id.
     *
     * @return the bound cube-map texture id.
     */
    public static int boundTextureCubeMap() {
        return boundTextureCubeMap;
    }

    /**
     * Enables blend.
     */
    public static void enableBlend() {
        if (!enabledBlend) {
            enabledBlend = true;
            gl.enable(IGL.BLEND);
        }
    }

    /**
     * Disables blend.
     */
    public static void disableBlend() {
        if (enabledBlend) {
            enabledBlend = false;
            gl.disable(IGL.BLEND);
        }
    }

    /**
     * Controls the blend equations used for per-fragment blending.
     *
     * @param mode the blend equation.
     */
    public static void blendEquation(int mode) {
        blendEquationSeparate(mode, mode);
    }

    /**
     * Sets the RGB blend equation and the alpha blend equation separately.
     *
     * @param modeRGB   the RGB blend equation, how the red, green, and blue components of the source and destination colors are combined.
     * @param modeAlpha the alpha blend equation, how the alpha component of the source and destination colors are combined.
     */
    public static void blendEquationSeparate(int modeRGB, int modeAlpha) {
        if (blendEquationRGB != modeRGB || blendEquationAlpha != modeAlpha) {
            blendEquationRGB = modeRGB;
            blendEquationAlpha = modeAlpha;
            gl.blendEquationSeparate(modeRGB, modeAlpha);
        }
    }

    /**
     * Specifies the weighting factors used by the blend equation, for both RGB and alpha functions and for all draw buffers.
     *
     * @param sfactor the source weighting factor.
     * @param dfactor the destination weighting factor.
     */
    public static void blendFunc(int sfactor, int dfactor) {
        blendFuncSeparate(sfactor, dfactor, sfactor, dfactor);
    }

    /**
     * Specifies pixel arithmetic for RGB and alpha components separately.
     *
     * @param srcRGB   how the red, green, and blue blending factors are computed. The initial value is GL_ONE.
     * @param dstRGB   how the red, green, and blue destination blending factors are computed. The initial value is GL_ZERO.
     * @param srcAlpha how the alpha source blending factor is computed. The initial value is GL_ONE.
     * @param dstAlpha how the alpha destination blending factor is computed. The initial value is GL_ZERO.
     */
    public static void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
        if (blendFuncSrcRGB != srcRGB || blendFuncDstRGB != dstRGB || blendFuncSrcAlpha != srcAlpha || blendFuncDstAlpha != dstAlpha) {
            blendFuncSrcRGB = srcRGB;
            blendFuncDstRGB = dstRGB;
            blendFuncSrcAlpha = srcAlpha;
            blendFuncDstAlpha = dstAlpha;
            gl.blendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
        }
    }
}
