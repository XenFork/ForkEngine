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
     * @param unit    the texture unit.
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
     * @param unit    the texture unit.
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
     * @param unit    the texture unit.
     * @param texture the texture id.
     */
    public static void bindTextureCubeMap(int unit, int texture) {
        if (activeTextureUnitCubeMap != unit || boundTextureCubeMap != texture) {
            activeTextureUnitCubeMap = unit;
            boundTextureCubeMap = texture;
            gl.bindTexture(IGL.TEXTURE_CUBE_MAP, unit, texture);
        }
    }
}
