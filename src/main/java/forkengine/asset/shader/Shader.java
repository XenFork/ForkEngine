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

package forkengine.asset.shader;

import forkengine.asset.Asset;
import forkengine.gl.GLStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static forkengine.core.ForkEngine.gl;

/**
 * The base shader program.
 *
 * @author squid233
 * @since 0.1.0
 */
public abstract class Shader extends Asset {
    /**
     * The fragment shader enum value.
     */
    public static final int FRAGMENT_SHADER = 0x8B30;
    /**
     * The vertex shader enum value.
     */
    public static final int VERTEX_SHADER = 0x8B31;

    private final int id;
    private final Map<String, ShaderUniform> uniformCache = new HashMap<>();

    /**
     * Creates the shader with the given id.
     *
     * @param id the program id.
     */
    public Shader(int id) {
        this.id = id;
    }

    /**
     * The base program shader.
     *
     * @author squid233
     * @since 0.1.0
     */
    public static abstract class Builder implements AutoCloseable {
        private final int id;

        /**
         * Creates the program shader with the given id.
         *
         * @param id the shader id.
         */
        public Builder(int id) {
            this.id = id;
        }

        /**
         * Generates an exception with the given message.
         *
         * @param msg the message.
         * @return the exception.
         */
        public static IllegalStateException exception(String msg) {
            return new IllegalStateException("Failed to compile the shader: " + msg);
        }

        /**
         * Sets the shader source.
         *
         * @param src the source string.
         */
        public abstract Builder source(String src);

        /**
         * Compiles this shader.
         *
         * @return the compile status
         */
        public abstract boolean compile();

        /**
         * Compiles this shader or throw if failed.
         *
         * @param msg the message.
         * @throws IllegalStateException if failed to compile the shader.
         */
        public void compileThrow(Supplier<String> msg) throws IllegalStateException {
            if (!compile()) throw exception(msg.get());
        }

        /**
         * Gets the info log.
         *
         * @return the info log.
         */
        public abstract String getInfoLog();

        /**
         * Gets the program shader id.
         *
         * @return the program shader id.
         */
        public int id() {
            return id;
        }

        @Override
        public abstract void close();
    }

    /**
     * Creates the shader.
     *
     * @return the instance of the shader.
     */
    public static Shader create() {
        return gl.createShader();
    }

    /**
     * Installs a program object as part of current rendering state.
     *
     * @param id the program object whose executables are to be used as part of current rendering state.
     */
    public static void useProgram(int id) {
        GLStateManager.useProgram(id);
    }

    /**
     * Generates an exception with the given message.
     *
     * @param msg the message.
     * @return the exception.
     */
    public static IllegalStateException exception(String msg) {
        return new IllegalStateException("Failed to link the program: " + msg);
    }

    /**
     * Attaches the given program shader.
     *
     * @param builder the program shader.
     * @return <i>{@code builder}</i>
     */
    public Builder attach(Builder builder) {
        gl.attachShader(id(), builder.id());
        return builder;
    }

    /**
     * Creates and attaches the program shader.
     *
     * @param type the type of the program shader.
     * @return the program shader.
     */
    public Builder attach(int type) {
        return attach(gl.createShaderBuilder(type));
    }

    /**
     * Detaches the given program shader.
     *
     * @param builder the program shader.
     * @return <i>{@code builder}</i>
     */
    public Builder detach(Builder builder) {
        gl.detachShader(id(), builder.id());
        return builder;
    }

    /**
     * Link this shader program.
     */
    public abstract boolean link();

    /**
     * Link this shader program or throw if failed.
     *
     * @param msg the message.
     * @throws IllegalStateException if failed to link the shader program.
     */
    public void linkThrow(Supplier<String> msg) {
        if (!link()) throw exception(msg.get());
    }

    /**
     * Gets the info log.
     *
     * @return the info log.
     */
    public abstract String getInfoLog();

    /**
     * Call {@link #useProgram(int)}.
     */
    public void use() {
        useProgram(id());
    }

    /**
     * Gets the program id.
     *
     * @return the program id.
     */
    public int id() {
        return id;
    }

    @Override
    public abstract void close();
}
