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
import forkengine.gl.IGL;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.opengl.GL45C.*;

/**
 * The OpenGL functions implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public final class LWJGL3GL implements IGL {
    private static final LWJGL3GL INSTANCE = new LWJGL3GL();

    private LWJGL3GL() {
    }

    private static boolean hasDSA(GLCapabilities caps) {
        return caps.GL_ARB_direct_state_access;
    }

    private static boolean hasDSA() {
        return hasDSA(GL.getCapabilities());
    }

    @Override
    public void clear(int mask) {
        glClear(mask);
    }

    @Override
    public void clearColor(float red, float green, float blue, float alpha) {
        glClearColor(red, green, blue, alpha);
    }

    @Override
    public void viewport(int x, int y, int w, int h) {
        glViewport(x, y, w, h);
    }

    @Override
    public @NotNull ShaderUniform createUniform(int location, ShaderUniform.Type type) {
        return new LWJGL3ShaderUniform(location, type);
    }

    @Override
    public int getUniformLocation(int program, String name) {
        return glGetUniformLocation(program, name);
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

    @Override
    public int genVertexArray() {
        return glGenVertexArrays();
    }

    @Override
    public void deleteVertexArray(int array) {
        glDeleteVertexArrays(array);
    }

    @Override
    public int genBuffer() {
        return glGenBuffers();
    }

    @Override
    public void bindBuffer(int target, int buffer) {
        glBindBuffer(target, buffer);
    }

    @Override
    public void deleteBuffer(int buffer) {
        glDeleteBuffers(buffer);
    }

    @Override
    public void bufferData(int target, int buffer, long size, long data, int usage) {
        if (hasDSA()) {
            nglNamedBufferData(buffer, size, data, usage);
        } else {
            nglBufferData(target, size, data, usage);
        }
    }

    @Override
    public void bufferSubData(int target, int buffer, long offset, long size, long data) {
        if (hasDSA()) {
            nglNamedBufferSubData(buffer, offset, size, data);
        } else {
            nglBufferSubData(target, offset, size, data);
        }
    }

    @Override
    public int getAttribLocation(int program, String name) {
        return glGetAttribLocation(program, name);
    }

    @Override
    public void bindAttribLocation(int program, int index, String name) {
        glBindAttribLocation(program, index, name);
    }

    @Override
    public void enableVertexAttribArray(int index) {
        glEnableVertexAttribArray(index);
    }

    @Override
    public void disableVertexAttribArray(int index) {
        glDisableVertexAttribArray(index);
    }

    @Override
    public void vertexAttribArrayPointer(int index, int size, int type, boolean normalized, int stride, long pointer) {
        glVertexAttribPointer(index, size, type, normalized, stride, pointer);
    }

    @Override
    public void drawElements(int mode, int count, int type, long indices) {
        glDrawElements(mode, count, type, indices);
    }

    @Override
    public int createTexture(int target) {
        if (hasDSA()) {
            return glCreateTextures(target);
        }
        return glGenTextures();
    }

    @Override
    public void bindTexture(int target, int unit, int texture) {
        if (hasDSA()) {
            glBindTextureUnit(unit, texture);
        } else {
            glActiveTexture(unit);
            glBindTexture(target, texture);
        }
    }

    @Override
    public void deleteTexture(int texture) {
        glDeleteTextures(texture);
    }

    @Override
    public void textureParameter(int target, int texture, int pname, int param) {
        if (hasDSA()) {
            glTextureParameteri(texture, pname, param);
        } else {
            glTexParameteri(target, pname, param);
        }
    }

    @Override
    public void textureParameter(int target, int texture, int pname, float param) {
        if (hasDSA()) {
            glTextureParameterf(texture, pname, param);
        } else {
            glTexParameterf(target, pname, param);
        }
    }

    @Override
    public void textureStorage2D(int target, int texture,
                                 int levels,
                                 int internalFormat,
                                 int width, int height,
                                 int format, int type) {
        if (hasDSA()) {
            glTextureStorage2D(texture, levels, internalFormat, width, height);
        } else {
            switch (target) {
                case GL_TEXTURE_1D, GL_TEXTURE_1D_ARRAY -> {
                    for (int i = 0; i < levels; i++) {
                        nglTexImage2D(target, i, internalFormat, width, height, 0, format, type, 0);
                        width = Math.max(1, width / 2);
                    }
                }
                case TEXTURE_CUBE_MAP -> {
                    for (int i = 0; i < levels; i++) {
                        for (int face = 0; face < 6; face++) {
                            nglTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, i, internalFormat, width, height, 0, format, type, 0);
                        }
                        width = Math.max(1, (width / 2));
                        height = Math.max(1, (height / 2));
                    }
                }
                default -> {
                    for (int i = 0; i < levels; i++) {
                        nglTexImage2D(target, i, internalFormat, width, height, 0, format, type, 0);
                        width = Math.max(1, (width / 2));
                        height = Math.max(1, (height / 2));
                    }
                }
            }
        }
    }

    @Override
    public void textureSubImage2D(int target, int texture,
                                  int level,
                                  int xOffset, int yOffset,
                                  int width, int height,
                                  int format, int type,
                                  long pixels) {
        if (hasDSA()) {
            nglTextureSubImage2D(texture, level, xOffset, yOffset, width, height, format, type, pixels);
        } else {
            nglTexSubImage2D(target, level, xOffset, yOffset, width, height, format, type, pixels);
        }
    }

    @Override
    public void generateMipmap(int target, int texture) {
        if (hasDSA()) {
            glGenerateTextureMipmap(texture);
        } else {
            glGenerateMipmap(target);
        }
    }

    @Override
    public void blendEquation(int mode) {
        glBlendEquation(mode);
    }

    @Override
    public void blendEquationSeparate(int modeRGB, int modeAlpha) {
        glBlendEquationSeparate(modeRGB, modeAlpha);
    }

    @Override
    public void blendFunc(int sfactor, int dfactor) {
        glBlendFunc(sfactor, dfactor);
    }

    @Override
    public void blendFuncSeparate(int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
        glBlendFuncSeparate(srcRGB, dstRGB, srcAlpha, dstAlpha);
    }

    @Override
    public void enable(int target) {
        glEnable(target);
    }

    @Override
    public void disable(int target) {
        glDisable(target);
    }

    /**
     * Gets the instance of this.
     *
     * @return this
     */
    public static LWJGL3GL getInstance() {
        return INSTANCE;
    }
}
