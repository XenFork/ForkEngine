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
import org.jetbrains.annotations.NotNull;

/**
 * The OpenGL functions and constants.
 *
 * @author squid233
 * @since 0.1.0
 */
public interface IGL {
    /**
     * The data type enum values.
     */
    int BYTE = 0x1400, UNSIGNED_BYTE = 0x1401,
        SHORT = 0x1402, UNSIGNED_SHORT = 0x1403,
        INT = 0x1404, UNSIGNED_INT = 0x1405,
        FLOAT = 0x1406,
        DOUBLE = 0x140A;
    /**
     * The clear flag enum values.
     */
    int DEPTH_BUFFER_BIT = 0x100, STENCIL_BUFFER_BIT = 0x400, COLOR_BUFFER_BIT = 0x4000;
    /**
     * The draw mode enum values.
     */
    int
        POINTS = 0x0,
        LINES = 0x1, LINE_LOOP = 0x2, LINE_STRIP = 0x3,
        TRIANGLES = 0x4, TRIANGLE_STRIP = 0x5, TRIANGLE_FAN = 0x6;
    /**
     * The buffer target enum values.
     */
    int
        ARRAY_BUFFER = 0x8892,
        ELEMENT_ARRAY_BUFFER = 0x8893,
        UNIFORM_BUFFER = 0x8A11;
    /**
     * The buffer data usage enum values.
     */
    int
        STREAM_DRAW = 0x88E0, STREAM_READ = 0x88E1, STREAM_COPY = 0x88E2,
        STATIC_DRAW = 0x88E4, STATIC_READ = 0x88E5, STATIC_COPY = 0x88E6,
        DYNAMIC_DRAW = 0x88E8, DYNAMIC_READ = 0x88E9, DYNAMIC_COPY = 0x88EA;
    /**
     * The texture targets.
     */
    int
        TEXTURE_2D = 0x0DE1,
        TEXTURE_3D = 0x806F,
        TEXTURE_CUBE_MAP = 0x8513;
    /**
     * The texture units.
     */
    int
        TEXTURE0 = 0x84C0, TEXTURE1 = 0x84C1, TEXTURE2 = 0x84C2, TEXTURE3 = 0x84C3, TEXTURE4 = 0x84C4, TEXTURE5 = 0x84C5, TEXTURE6 = 0x84C6, TEXTURE7 = 0x84C7,
        TEXTURE8 = 0x84C8, TEXTURE9 = 0x84C9, TEXTURE10 = 0x84CA, TEXTURE11 = 0x84CB, TEXTURE12 = 0x84CC, TEXTURE13 = 0x84CD, TEXTURE14 = 0x84CE, TEXTURE15 = 0x84CF,
        TEXTURE16 = 0x84D0, TEXTURE17 = 0x84D1, TEXTURE18 = 0x84D2, TEXTURE19 = 0x84D3, TEXTURE20 = 0x84D4, TEXTURE21 = 0x84D5, TEXTURE22 = 0x84D6, TEXTURE23 = 0x84D7,
        TEXTURE24 = 0x84D8, TEXTURE25 = 0x84D9, TEXTURE26 = 0x84DA, TEXTURE27 = 0x84DB, TEXTURE28 = 0x84DC, TEXTURE29 = 0x84DD, TEXTURE30 = 0x84DE, TEXTURE31 = 0x84DF;
    /**
     * The compare functions.
     */
    int NEVER = 0x200, LESS = 0x201, EQUAL = 0x202, LEQUAL = 0x203, GREATER = 0x204, NOTEQUAL = 0x205, GEQUAL = 0x206, ALWAYS = 0x207;
    /**
     * The texture filters.
     */
    int
        NEAREST = 0x2600, LINEAR = 0x2601,
        NEAREST_MIPMAP_NEAREST = 0x2700, LINEAR_MIPMAP_NEAREST = 0x2701,
        NEAREST_MIPMAP_LINEAR = 0x2702, LINEAR_MIPMAP_LINEAR = 0x2703;
    /**
     * The pixel formats.
     */
    int
        STENCIL_INDEX = 0x1901,
        DEPTH_COMPONENT = 0x1902,
        RED = 0x1903, GREEN = 0x1904, BLUE = 0x1905, ALPHA = 0x1906,
        RGB = 0x1907, RGBA = 0x1908;
    /**
     * The sized internal formats.
     */
    int
        R3_G3_B2 = 0x2A10,
        RGB4 = 0x804F,
        RGB5 = 0x8050,
        RGB8 = 0x8051,
        RGB10 = 0x8052,
        RGB12 = 0x8053,
        RGB16 = 0x8054,
        RGBA2 = 0x8055,
        RGBA4 = 0x8056,
        RGB5_A1 = 0x8057,
        RGBA8 = 0x8058,
        RGB10_A2 = 0x8059,
        RGBA12 = 0x805A,
        RGBA16 = 0x805B,
        R8 = 0x8229,
        R16 = 0x822A,
        RG8 = 0x822B,
        RG16 = 0x822C,
        R16F = 0x822D,
        R32F = 0x822E,
        RG16F = 0x822F,
        RG32F = 0x8230,
        R8I = 0x8231,
        R8UI = 0x8232,
        R16I = 0x8233,
        R16UI = 0x8234,
        R32I = 0x8235,
        R32UI = 0x8236,
        RG8I = 0x8237,
        RG8UI = 0x8238,
        RG16I = 0x8239,
        RG16UI = 0x823A,
        RG32I = 0x823B,
        RG32UI = 0x823C,
        RG = 0x8227,
        COMPRESSED_RED = 0x8225,
        COMPRESSED_RG = 0x8226,
        RGBA32F = 0x8814,
        RGB32F = 0x8815,
        RGBA16F = 0x881A,
        RGB16F = 0x881B,
        R11F_G11F_B10F = 0x8C3A,
        RGB9_E5 = 0x8C3D,
        SRGB = 0x8C40,
        SRGB8 = 0x8C41,
        SRGB_ALPHA = 0x8C42,
        SRGB8_ALPHA8 = 0x8C43,
        COMPRESSED_SRGB = 0x8C48,
        COMPRESSED_SRGB_ALPHA = 0x8C49,
        RGBA32UI = 0x8D70,
        RGB32UI = 0x8D71,
        RGBA16UI = 0x8D76,
        RGB16UI = 0x8D77,
        RGBA8UI = 0x8D7C,
        RGB8UI = 0x8D7D,
        RGBA32I = 0x8D82,
        RGB32I = 0x8D83,
        RGBA16I = 0x8D88,
        RGB16I = 0x8D89,
        RGBA8I = 0x8D8E,
        RGB8I = 0x8D8F,
        R8_SNORM = 0x8F94,
        RG8_SNORM = 0x8F95,
        RGB8_SNORM = 0x8F96,
        RGBA8_SNORM = 0x8F97,
        R16_SNORM = 0x8F98,
        RG16_SNORM = 0x8F99,
        RGB16_SNORM = 0x8F9A,
        RGBA16_SNORM = 0x8F9B,
        RGB10_A2UI = 0x906F;
    /**
     * The texture wrap parameters.
     */
    int REPEAT = 0x2901, CLAMP_TO_BORDER = 0x812D, CLAMP_TO_EDGE = 0x812F, MIRRORED_REPEAT = 0x8370, MIRROR_CLAMP_TO_EDGE = 0x8743;

    /**
     * Sets portions of every pixel in a particular buffer to the same value.
     * The value to which each buffer is cleared depends on the setting of the clear value for that buffer.
     *
     * @param mask Zero or the bitwise OR of one or more values indicating which buffers are to be cleared.
     */
    void clear(int mask);

    /**
     * Sets the clear value for fixed-point and floating-point color buffers in RGBA mode.
     * The specified components are stored as floating-point values.
     *
     * @param red   the value to which to clear the R channel of the color buffer.
     * @param green the value to which to clear the G channel of the color buffer.
     * @param blue  the value to which to clear the B channel of the color buffer.
     * @param alpha the value to which to clear the A channel of the color buffer.
     */
    void clearColor(float red, float green, float blue, float alpha);

    /**
     * Call {@code glViewport}.
     *
     * @param x the left viewport coordinate.
     * @param y the bottom viewport coordinate.
     * @param w the viewport width.
     * @param h the viewport height.
     */
    void viewport(int x, int y, int w, int h);

    ///////////////////////////////////////////////////////////////////////////
    // Shader program
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Creates a shader uniform.
     *
     * @param location the location of this uniform.
     * @param type     the type.
     * @return the shader uniform.
     */
    @NotNull ShaderUniform createUniform(int location, ShaderUniform.Type type);

    /**
     * Returns the location of a uniform variable.
     *
     * @param program the program object to be queried.
     * @param name    a null terminated string containing the name of the uniform variable whose location is to be queried.
     * @return the location of a uniform variable.
     */
    int getUniformLocation(int program, String name);

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
     * Attaches a shader object to a program object.
     *
     * @param program the program object to which a shader object will be attached.
     * @param shader  the shader object that is to be attached.
     */
    void attachShader(int program, int shader);

    /**
     * Detaches a shader object from a program object to which it is attached.
     *
     * @param program the program object from which to detach the shader object.
     * @param shader  the shader object to be detached.
     */
    void detachShader(int program, int shader);

    /**
     * Installs a program object as part of current rendering state.
     *
     * @param program the program object whose executables are to be used as part of current rendering state.
     */
    void useProgram(int program);

    ///////////////////////////////////////////////////////////////////////////
    // Vertex array
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Generates a vertex array object name.
     *
     * @return the name.
     */
    int genVertexArray();

    /**
     * Binds a vertex array object.
     *
     * @param array the name of the vertex array to bind.
     */
    void bindVertexArray(int array);

    /**
     * Deletes a vertex array object.
     *
     * @param array the object.
     */
    void deleteVertexArray(int array);

    /**
     * Generates a buffer object name.
     *
     * @return the buffer object name.
     */
    int genBuffer();

    /**
     * Binds a named buffer object.
     *
     * @param target the target to which the buffer object is bound.
     * @param buffer the name of a buffer object.
     */
    void bindBuffer(int target, int buffer);

    /**
     * Deletes a named buffer object.
     *
     * @param buffer the named buffer object.
     */
    void deleteBuffer(int buffer);

    /**
     * Creates and initializes a buffer object's data store.
     *
     * @param target the target buffer object.
     * @param buffer the name of the target buffer object.
     * @param size   the size in bytes of the buffer object's new data store.
     * @param data   the data.
     * @param usage  the expected usage pattern of the data store.
     */
    void bufferData(int target, int buffer, long size, long data, int usage);

    /**
     * Returns the location of an attribute variable.
     *
     * @param program the program object to be queried.
     * @param name    a null terminated string containing the name of the attribute variable whose location is to be queried
     * @return the location of the attribute variable.
     */
    int getAttribLocation(int program, String name);

    /**
     * Associates a generic vertex attribute index with a named attribute variable.
     *
     * @param program the program object in which the association is to be made.
     * @param index   the index of the generic vertex attribute to be bound.
     * @param name    a null terminated string containing the name of the vertex shader attribute variable to which index is to be bound.
     */
    void bindAttribLocation(int program, int index, String name);

    /**
     * Enables a generic vertex attribute array.
     *
     * @param index the index of the generic vertex attribute to be enabled.
     */
    void enableVertexAttribArray(int index);

    /**
     * Disables a generic vertex attribute array.
     *
     * @param index the index of the generic vertex attribute to be disabled.
     */
    void disableVertexAttribArray(int index);

    /**
     * Specifies the location and organization of a vertex attribute array.
     *
     * @param index      the index of the generic vertex attribute to be modified.
     * @param size       the number of values per vertex that are stored in the array. The initial value is 4.
     * @param type       the data type of each component in the array. The initial value is GL_FLOAT.
     * @param normalized whether fixed-point data values should be normalized or converted directly as fixed-point values
     *                   when they are accessed.
     * @param stride     the byte offset between consecutive generic vertex attributes. If stride is 0, the generic vertex attributes
     *                   are understood to be tightly packed in the array. The initial value is 0.
     * @param pointer    the vertex attribute data or the offset of the first component of the first generic vertex attribute
     *                   in the array in the data store of the buffer currently bound to the ARRAY_BUFFER target. The initial value is 0.
     */
    void vertexAttribArrayPointer(int index, int size, int type, boolean normalized, int stride, long pointer);

    ///////////////////////////////////////////////////////////////////////////
    // Draw call
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a sequence of geometric primitives by successively transferring elements for count vertices to the GL.
     *
     * @param mode    the kind of primitives being constructed.
     * @param count   the number of vertices to transfer to the GL.
     * @param type    indicates the type of index values in indices.
     * @param indices the index values.
     */
    void drawElements(int mode, int count, int type, long indices);

    ///////////////////////////////////////////////////////////////////////////
    // Texture
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a previously unused texture name in textures, representing a new texture object.
     *
     * @param target the texture target.
     * @return the texture name.
     */
    int createTexture(int target);

    /**
     * Binds an existing texture object to the texture unit numbered {@code unit} in DSA,
     * or binds a texture to a texture target.
     *
     * @param target  the texture target.
     * @param unit    the texture unit number in DSA, or which texture unit to make active.
     * @param texture the texture name in DSA, or the texture object to bind.
     */
    void bindTexture(int target, int unit, int texture);

    /**
     * Deletes texture objects. After a texture object is deleted, it has no contents or dimensionality, and its name is again unused.
     * If a texture that is currently bound to any of the target bindings of {@link #bindTexture} is deleted, it is as
     * though {@link #bindTexture} had been executed with the same target and texture zero. Additionally, special care
     * must be taken when deleting a texture if any of the images of the texture are attached to a framebuffer object.
     * <p>
     * Unused names in textures that have been marked as used for the purposes of {@link #createTexture} are marked as unused again.
     * Unused names in textures are silently ignored, as is the name zero.
     *
     * @param texture the texture to be deleted.
     */
    void deleteTexture(int texture);

    /**
     * Sets the integer value of a texture parameter, which controls how the texel array is treated when specified or changed,
     * and when applied to a fragment.
     *
     * @param target  the texture target.
     * @param texture the texture name.
     * @param pname   the parameter to set.
     * @param param   the parameter value.
     */
    void textureParameter(int target, int texture, int pname, int param);

    /**
     * Float version of {@link #textureParameter(int, int, int, int) textureParameter}.
     *
     * @param target  the texture target.
     * @param texture the texture name.
     * @param pname   the parameter to set.
     * @param param   the parameter value.
     */
    void textureParameter(int target, int texture, int pname, float param);

    /**
     * Simultaneously specifies storage for all levels of a two-dimensional or one-dimensional array texture.
     *
     * @param target         the target of the operation.
     * @param texture        the texture name.
     * @param levels         the number of texture levels.
     * @param internalFormat the sized internal format to be used to store texture image data.
     * @param width          the width of the texture, in texels.
     * @param height         the height of the texture, in texels.
     * @param format         the texel data format.
     * @param type           the texel data type.
     */
    void textureStorage2D(int target, int texture,
                          int levels,
                          int internalFormat,
                          int width, int height,
                          int format, int type);

    /**
     * Re-specifies a rectangular subregion of an existing texel array. No change is made to the internalFormat,
     * width, height, depth, or border parameters of the specified texel array, nor is any change made to texel values
     * outside the specified subregion.
     *
     * @param target  the texture target.
     * @param texture the texture name.
     * @param level   the level-of-detail-number.
     * @param xOffset the left coordinate of the texel subregion.
     * @param yOffset the bottom coordinate of the texel subregion.
     * @param width   the subregion width.
     * @param height  the subregion height.
     * @param format  the pixel data format.
     * @param type    the pixel data type.
     * @param pixels  the pixel data.
     */
    void textureSubImage2D(int target, int texture,
                           int level,
                           int xOffset, int yOffset,
                           int width, int height,
                           int format, int type,
                           long pixels);

    /**
     * Generate mipmaps for a specified texture target.
     *
     * @param target  the target to which the texture whose mimaps to generate is bound.
     * @param texture the texture name.
     */
    void generateMipmap(int target, int texture);
}
