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

import forkengine.asset.shader.ShaderUniform;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.opengl.GL41C.*;

/**
 * The shader uniform implemented with LWJGL 3.
 *
 * @author squid233
 * @since 0.1.0
 */
public class LWJGL3ShaderUniform extends ShaderUniform {
    /**
     * Creates the shader uniform.
     *
     * @param location the location of this uniform.
     * @param type  the type.
     */
    public LWJGL3ShaderUniform(int location, Type type) {
        super(location, type);
    }

    @Override
    protected void internalUpload(int program) {
        final GLCapabilities caps = GL.getCapabilities();
        final boolean arb = caps.GL_ARB_separate_shader_objects || caps.OpenGL41;
        final long buffer = this.buffer.address();
        switch (type()) {
            case INT -> {
                if (arb) nglProgramUniform1iv(program, location(), 1, buffer);
                else nglUniform1iv(location(), 1, buffer);
            }
            case UNSIGNED_INT -> {
                if (arb) nglProgramUniform1uiv(program, location(), 1, buffer);
                else nglUniform1uiv(location(), 1, buffer);
            }
            case FLOAT -> {
                if (arb) nglProgramUniform1fv(program, location(), 1, buffer);
                else nglUniform1fv(location(), 1, buffer);
            }
            case DOUBLE -> {
                if (arb) nglProgramUniform1dv(program, location(), 1, buffer);
                else nglUniform1dv(location(), 1, buffer);
            }
            case IVEC2 -> {
                if (arb) nglProgramUniform2iv(program, location(), 2, buffer);
                else nglUniform2iv(location(), 2, buffer);
            }
            case UVEC2 -> {
                if (arb) nglProgramUniform2uiv(program, location(), 2, buffer);
                else nglUniform2uiv(location(), 2, buffer);
            }
            case VEC2 -> {
                if (arb) nglProgramUniform2fv(program, location(), 2, buffer);
                else nglUniform2fv(location(), 2, buffer);
            }
            case DVEC2 -> {
                if (arb) nglProgramUniform2dv(program, location(), 2, buffer);
                else nglUniform2dv(location(), 2, buffer);
            }
            case IVEC3 -> {
                if (arb) nglProgramUniform3iv(program, location(), 3, buffer);
                else nglUniform3iv(location(), 3, buffer);
            }
            case UVEC3 -> {
                if (arb) nglProgramUniform3uiv(program, location(), 3, buffer);
                else nglUniform3uiv(location(), 3, buffer);
            }
            case VEC3 -> {
                if (arb) nglProgramUniform3fv(program, location(), 3, buffer);
                else nglUniform3fv(location(), 3, buffer);
            }
            case DVEC3 -> {
                if (arb) nglProgramUniform3dv(program, location(), 3, buffer);
                else nglUniform3dv(location(), 3, buffer);
            }
            case IVEC4 -> {
                if (arb) nglProgramUniform4iv(program, location(), 4, buffer);
                else nglUniform4iv(location(), 4, buffer);
            }
            case UVEC4 -> {
                if (arb) nglProgramUniform4uiv(program, location(), 4, buffer);
                else nglUniform4uiv(location(), 4, buffer);
            }
            case VEC4 -> {
                if (arb) nglProgramUniform4fv(program, location(), 4, buffer);
                else nglUniform4fv(location(), 4, buffer);
            }
            case DVEC4 -> {
                if (arb) nglProgramUniform4dv(program, location(), 4, buffer);
                else nglUniform4dv(location(), 4, buffer);
            }
            case MAT2 -> {
                if (arb) nglProgramUniformMatrix2fv(program, location(), 1, false, buffer);
                else nglUniformMatrix2fv(location(), 1, false, buffer);
            }
            case MAT3 -> {
                if (arb) nglProgramUniformMatrix3fv(program, location(), 1, false, buffer);
                else nglUniformMatrix3fv(location(), 1, false, buffer);
            }
            case MAT4 -> {
                if (arb) nglProgramUniformMatrix4fv(program, location(), 1, false, buffer);
                else nglUniformMatrix4fv(location(), 1, false, buffer);
            }
            case MAT2X3 -> {
                if (arb) nglProgramUniformMatrix2x3fv(program, location(), 1, false, buffer);
                else nglUniformMatrix2x3fv(location(), 1, false, buffer);
            }
            case MAT2X4 -> {
                if (arb) nglProgramUniformMatrix2x4fv(program, location(), 1, false, buffer);
                else nglUniformMatrix2x4fv(location(), 1, false, buffer);
            }
            case MAT3X2 -> {
                if (arb) nglProgramUniformMatrix3x2fv(program, location(), 1, false, buffer);
                else nglUniformMatrix3x2fv(location(), 1, false, buffer);
            }
            case MAT3X4 -> {
                if (arb) nglProgramUniformMatrix3x4fv(program, location(), 1, false, buffer);
                else nglUniformMatrix3x4fv(location(), 1, false, buffer);
            }
            case MAT4X2 -> {
                if (arb) nglProgramUniformMatrix4x2fv(program, location(), 1, false, buffer);
                else nglUniformMatrix4x2fv(location(), 1, false, buffer);
            }
            case MAT4X3 -> {
                if (arb) nglProgramUniformMatrix4x3fv(program, location(), 1, false, buffer);
                else nglUniformMatrix4x3fv(location(), 1, false, buffer);
            }
            case DMAT2 -> {
                if (arb) nglProgramUniformMatrix2dv(program, location(), 1, false, buffer);
                else nglUniformMatrix2dv(location(), 1, false, buffer);
            }
            case DMAT3 -> {
                if (arb) nglProgramUniformMatrix3dv(program, location(), 1, false, buffer);
                else nglUniformMatrix3dv(location(), 1, false, buffer);
            }
            case DMAT4 -> {
                if (arb) nglProgramUniformMatrix4dv(program, location(), 1, false, buffer);
                else nglUniformMatrix4dv(location(), 1, false, buffer);
            }
            case DMAT2X3 -> {
                if (arb) nglProgramUniformMatrix2x3dv(program, location(), 1, false, buffer);
                else nglUniformMatrix2x3dv(location(), 1, false, buffer);
            }
            case DMAT2X4 -> {
                if (arb) nglProgramUniformMatrix2x4dv(program, location(), 1, false, buffer);
                else nglUniformMatrix2x4dv(location(), 1, false, buffer);
            }
            case DMAT3X2 -> {
                if (arb) nglProgramUniformMatrix3x2dv(program, location(), 1, false, buffer);
                else nglUniformMatrix3x2dv(location(), 1, false, buffer);
            }
            case DMAT3X4 -> {
                if (arb) nglProgramUniformMatrix3x4dv(program, location(), 1, false, buffer);
                else nglUniformMatrix3x4dv(location(), 1, false, buffer);
            }
            case DMAT4X2 -> {
                if (arb) nglProgramUniformMatrix4x2dv(program, location(), 1, false, buffer);
                else nglUniformMatrix4x2dv(location(), 1, false, buffer);
            }
            case DMAT4X3 -> {
                if (arb) nglProgramUniformMatrix4x3dv(program, location(), 1, false, buffer);
                else nglUniformMatrix4x3dv(location(), 1, false, buffer);
            }
        }
    }
}
