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

import static org.lwjgl.opengl.GL20C.*;

/**
 * The shader program implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public class LWJGL3Shader extends Shader {
    /**
     * Creates the shader with the given id.
     *
     * @param id the program id.
     */
    public LWJGL3Shader(int id) {
        super(id);
    }

    /**
     * The program shader implemented with LWJGL 3.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static class Builder extends Shader.Builder {
        /**
         * Creates the program shader with the given id.
         *
         * @param id the shader id.
         */
        public Builder(int id) {
            super(id);
        }

        @Override
        public Shader.Builder source(String src) {
            glShaderSource(id(), src);
            return this;
        }

        @Override
        public boolean compile() {
            glCompileShader(id());
            return glGetShaderi(id(), GL_COMPILE_STATUS) != GL_FALSE;
        }

        @Override
        public String getInfoLog() {
            return glGetShaderInfoLog(id());
        }

        @Override
        public void close() {
            glDeleteShader(id());
        }
    }

    @Override
    public boolean link() {
        glLinkProgram(id());
        return glGetProgrami(id(), GL_LINK_STATUS) != GL_FALSE;
    }

    @Override
    public String getInfoLog() {
        return glGetProgramInfoLog(id());
    }

    @Override
    public void close() {
        super.close();
        glDeleteProgram(id());
    }
}
