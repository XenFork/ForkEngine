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

package forkengine.asset;

import forkengine.core.DataBuffer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The file io utilities.
 *
 * @author squid233
 * @since 0.1.0
 */
@FunctionalInterface
public interface FileProvider {
    /**
     * The local file loader.
     */
    FileProvider LOCAL = new FileProvider() {
        @Override
        public URI apply(String path) {
            return new File(path).toURI();
        }

        @Override
        public DataBuffer toDataBuffer(String resource, long bufferSize) {
            Path path = Path.of(apply(resource));
            if (Files.isReadable(path)) {
                try {
                    return DataBuffer.allocate(bufferSize).loadLocalFile(resource);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return FileProvider.super.toDataBuffer(resource, bufferSize);
        }
    };
    /**
     * The classpath file loader.
     */
    FileProvider CLASSPATH = path -> ClassLoader.getSystemResource(path).toURI();

    /**
     * Applies the path to an uri.
     *
     * @param path the path.
     * @return the uri.
     * @throws URISyntaxException if this URL is not formatted strictly according to RFC2396 and cannot be converted to a URI.
     */
    URI apply(String path) throws URISyntaxException;

    /**
     * Loads the string from the given local file.
     *
     * @param path the path of the file.
     * @return the string.
     */
    static String local(String path) {
        return LOCAL.loadString(path);
    }

    /**
     * Loads the string from the given classpath file.
     *
     * @param path the path of the file.
     * @return the string.
     */
    static String internal(String path) {
        return CLASSPATH.loadString(path);
    }

    /**
     * Returns the size of a file (in bytes).
     *
     * @param path the path to the file.
     * @return the file size, in bytes.
     * @throws IllegalStateException if an I/O error occurs.
     * @see Files#size(Path)
     */
    static long size(Path path) throws IllegalStateException {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't get the size of the font file!", e);
        }
    }

    /**
     * Applies the path to an uri.
     *
     * @param path the path.
     * @return the uri.
     */
    default URI toURI(String path) {
        try {
            return apply(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies the path to an url.
     *
     * @param path the path.
     * @return the url.
     */
    default URL toURL(String path) {
        try {
            return apply(path).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException("Failed to load resource '" + path + '\'', e);
        }
    }

    /**
     * Opens an input stream for the given path. The stream must be explicitly closed.
     *
     * @param path the path.
     * @return the stream.
     */
    default InputStream toStream(String path) {
        try {
            return toURL(path).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the string from the given file.
     *
     * @param path the path of the file.
     * @return the string.
     */
    default String loadString(String path) {
        try (var br = new BufferedReader(new InputStreamReader(toStream(path), StandardCharsets.UTF_8))) {
            String ln;
            StringBuilder sb = new StringBuilder();
            while ((ln = br.readLine()) != null) {
                sb.append(ln).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the file with the given name.
     *
     * @param resource   the resource name.
     * @param bufferSize the initial buffer size.
     * @return the data buffer.
     */
    default DataBuffer toDataBuffer(String resource, long bufferSize) {
        return DataBuffer.allocate(bufferSize).loadFile(this, resource);
    }
}
