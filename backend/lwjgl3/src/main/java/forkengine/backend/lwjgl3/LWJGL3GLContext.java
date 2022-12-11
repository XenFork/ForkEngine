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

import forkengine.asset.shader.Shader;
import forkengine.asset.shader.ShaderUniform;
import forkengine.gl.IGLContext;

import static org.lwjgl.opengl.GL30C.*;

/**
 * The OpenGL functions implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class LWJGL3GLContext implements IGLContext {
    private static final LWJGL3GLContext INSTANCE = new LWJGL3GLContext();

    private LWJGL3GLContext() {
    }

    @Override
    public void clear(int flag) {
        glClear(flag);
    }

    @Override
    public void viewport(int x, int y, int w, int h) {
        glViewport(x, y, w, h);
    }

    @Override
    public ShaderUniform createUniform(int location, ShaderUniform.Type type) {
        return new LWJGL3ShaderUniform(location, type);
    }

    @Override
    public Shader createShader() {
        return new LWJGL3Shader(glCreateProgram());
    }

    @Override
    public Shader.Builder createShaderBuilder(int type) {
        return new LWJGL3Shader.Builder(glCreateShader(type));
    }

    @Override
    public void attachShader(int program, int shader) {
        glAttachShader(program, shader);
    }

    @Override
    public void detachShader(int program, int shader) {
        glDetachShader(program, shader);
    }

    @Override
    public void useProgram(int program) {
        glUseProgram(program);
    }

    @Override
    public void bindVertexArray(int array) {
        glBindVertexArray(array);
    }

    /**
     * Gets the instance of this.
     *
     * @return this
     */
    public static LWJGL3GLContext getInstance() {
        return INSTANCE;
    }
}
