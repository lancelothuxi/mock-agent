package io.github.lancelothuxi.mock.agent; /*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * Provides utility methods for working with character streams.
 *
 * <p>
 * All method parameters must be non-null unless documented otherwise.
 *
 * <p>
 * Some of the methods in this class take arguments with a generic type of {@code Readable &
 * Closeable}. A {@link java.io.Reader} implements both of those interfaces. Similarly for {@code
 * Appendable & Closeable} and {@link java.io.Writer}.
 *
 * @author Chris Nokleberg
 * @author Bin Zhu
 * @author Colin Decker
 * @since 1.0
 */
public final class CharStreams {
    private static final int BUF_SIZE = 0x800; // 2K chars (4K bytes)

    private CharStreams() {
    }

    /**
     * Copies all characters between the {@link Readable} and {@link Appendable} objects. Does not close or flush either
     * object.
     *
     * @param from the object to read from
     * @param to   the object to write to
     * @return the number of characters copied
     * @throws IOException if an I/O error occurs
     */
    public static long copy(Readable from, Appendable to) throws IOException {
        CharBuffer buf = CharBuffer.allocate(BUF_SIZE);
        long total = 0;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += buf.remaining();
            buf.clear();
        }
        return total;
    }

    /**
     * Reads all characters from a {@link Readable} object into a {@link String}. Does not close the {@code Readable}.
     *
     * @param r the object to read from
     * @return a string containing all the characters
     * @throws IOException if an I/O error occurs
     */
    public static String toString(Readable r) throws IOException {
        return toStringBuilder(r).toString();
    }

    /**
     * Reads all characters from a {@link Readable} object into a new {@link StringBuilder} instance. Does not close the
     * {@code Readable}.
     *
     * @param r the object to read from
     * @return a {@link StringBuilder} containing all the characters
     * @throws IOException if an I/O error occurs
     */
    private static StringBuilder toStringBuilder(Readable r) throws IOException {
        StringBuilder sb = new StringBuilder();
        copy(r, sb);
        return sb;
    }

    static Reader asReader(final Readable readable) {
        if (readable instanceof Reader) {
            return (Reader) readable;
        }
        return new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return read(CharBuffer.wrap(cbuf, off, len));
            }

            @Override
            public int read(CharBuffer target) throws IOException {
                return readable.read(target);
            }

            @Override
            public void close() throws IOException {
                if (readable instanceof Closeable) {
                    ((Closeable) readable).close();
                }
            }
        };
    }
}
