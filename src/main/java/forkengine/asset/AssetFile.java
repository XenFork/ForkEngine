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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The asset file io utilities.
 *
 * @author squid233
 * @since 0.1.0
 */
@FunctionalInterface
public interface AssetFile {
    /**
     * The local file loader.
     */
    AssetFile LOCAL = path -> new File(path).toURI().toURL();
    /**
     * The classpath file loader.
     */
    AssetFile CLASSPATH = ClassLoader::getSystemResource;

    /**
     * Applies the path to an url.
     *
     * @param path the path.
     * @return the url.
     * @throws MalformedURLException If a protocol handler for the URL could not be found,
     *                               or if some other error occurred while constructing the URL.
     */
    URL apply(String path) throws MalformedURLException;

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
     * Applies the path to an url.
     *
     * @param path the path.
     * @return the url.
     */
    default URL toURL(String path) {
        try {
            return apply(path);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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
}
