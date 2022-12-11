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

import forkengine.asset.shader.Shader;
import forkengine.asset.shader.ShaderUniform;

/**
 * The OpenGL functions.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface IGLContext {
    /**
     * Call {@code glClear}.
     *
     * @param flag the clear flag.
     */
    void clear(int flag);

    /**
     * Call {@code glViewport}.
     *
     * @param x the left viewport coordinate.
     * @param y the bottom viewport coordinate.
     * @param w the viewport width.
     * @param h the viewport height.
     */
    void viewport(int x, int y, int w, int h);

    /**
     * Creates a shader uniform.
     *
     * @param location the location of this uniform.
     * @param type     the type.
     * @return the shader uniform.
     */
    ShaderUniform createUniform(int location, ShaderUniform.Type type);

    /**
     * Creates a shader.
     *
     * @return the shader program.
     */
    Shader createShader();

    /**
     * Creates a shader builder.
     *
     * @param type the program shader type.
     * @return the program shader.
     */
    Shader.Builder createShaderBuilder(int type);

    /**
     * Attaches the shader.
     *
     * @param program the program id.
     * @param shader  the shader id.
     */
    void attachShader(int program, int shader);

    /**
     * Detaches the shader.
     *
     * @param program the program id.
     * @param shader  the shader id.
     */
    void detachShader(int program, int shader);

    /**
     * Installs a program object as part of current rendering state.
     *
     * @param program the program object whose executables are to be used as part of current rendering state.
     */
    void useProgram(int program);

    /**
     * Binds a vertex array object.
     *
     * @param array the name of the vertex array to bind.
     */
    void bindVertexArray(int array);
}
