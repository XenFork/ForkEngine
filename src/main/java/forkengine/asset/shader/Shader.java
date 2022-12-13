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
import forkengine.level.model.VertexLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static forkengine.core.ForkEngine.gl;

/**
 * The base shader program.
 *
 * <h2>Loading Shaders</h2>
 * To load shaders, use {@link #attach(int)}, {@link Builder#source(String) source(String)}, {@link Builder#compile() compile()},
 * {@link #link()}, {@link #detach(Builder)} and {@link Builder#close()}.
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
    private final Map<String, Integer> attribIndexCache = new HashMap<>();
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
        public Builder compileThrow(Supplier<String> msg) throws IllegalStateException {
            if (!compile()) throw exception(msg.get());
            return this;
        }

        /**
         * Compiles this shader or throw with info log if failed.
         *
         * @throws IllegalStateException if failed to compile the shader.
         */
        public Builder compileThrowLog() throws IllegalStateException {
            if (!compile()) throw exception(getInfoLog());
            return this;
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
     * Associates a generic vertex attribute index with a named attribute variable.
     * <p>
     * Attribute bindings do not go into effect until {@link #link()} is called. After a program object has been linked
     * successfully, the index values for generic attributes remain fixed (and their values can be queried) until the next
     * link command occurs.
     *
     * @param index the index of the generic vertex attribute to be bound.
     * @param name  a null terminated string containing the name of the vertex shader attribute variable to which index is to be bound.
     * @return this.
     */
    public Shader bindAttribLocation(int index, String name) {
        attribIndexCache.put(name, index);
        gl.bindAttribLocation(id(), index, name);
        return this;
    }

    /**
     * Associates the vertex attribute indexes with a vertex layout.
     *
     * @param layout the vertex layout to be bound.
     * @return this.
     * @see #bindAttribLocation(int, String)
     */
    public Shader bindLayout(VertexLayout layout) {
        layout.forEach((element, index) -> this.bindAttribLocation(index, element.name()));
        return this;
    }

    /**
     * Returns the location of an attribute variable.
     *
     * @param name a null terminated string containing the name of the attribute variable whose location is to be queried
     * @return the location of the attribute variable.
     */
    public int getAttribLocation(String name) {
        return attribIndexCache.computeIfAbsent(name, s -> gl.getAttribLocation(id(), s));
    }

    /**
     * Creates or gets a shader uniform with the given type.
     *
     * @param name the name of the uniform.
     * @param type the type.
     * @return the uniform, or empty if not found.
     * @see #getUniform(String)
     */
    public Optional<ShaderUniform> createUniform(String name, ShaderUniform.Type type) {
        Optional<ShaderUniform> uniform = getUniform(name);
        if (uniform.isPresent()) {
            return uniform;
        }
        final int location = gl.getUniformLocation(id(), name);
        if (location == -1) {
            return Optional.empty();
        }
        final ShaderUniform result = gl.createUniform(location, type);
        uniformCache.put(name, result);
        return Optional.of(result);
    }

    /**
     * Gets a shader uniform with the given name.
     *
     * @param name the name of the uniform.
     * @return the uniform, or empty if not found.
     * @see #createUniform(String, ShaderUniform.Type)
     */
    public Optional<ShaderUniform> getUniform(String name) {
        return Optional.ofNullable(uniformCache.get(name));
    }

    /**
     * Uploads the uniforms.
     */
    public void uploadUniforms() {
        uniformCache.values().forEach(uniform -> uniform.upload(id()));
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
    public void close() {
        uniformCache.values().forEach(ShaderUniform::close);
    }
}
