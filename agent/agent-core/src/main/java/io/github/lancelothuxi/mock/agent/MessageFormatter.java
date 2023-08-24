package io.github.lancelothuxi.mock.agent;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ExceptionMessageFormatter
 *
 * <p>
 * log4j message format 核心类 Supports parameter formatting as used in ParameterizedMessage and
 * ReusableParameterizedMessage.
 */
final class MessageFormatter {
    /** Prefix for recursion. */
    static final String RECURSION_PREFIX = "[...";
    /** Suffix for recursion. */
    static final String RECURSION_SUFFIX = "...]";

    /** Prefix for errors. */
    static final String ERROR_PREFIX = "[!!!";
    /** Separator for errors. */
    static final String ERROR_SEPARATOR = "=>";
    /** Separator for error messages. */
    static final String ERROR_MSG_SEPARATOR = ":";
    /** Suffix for errors. */
    static final String ERROR_SUFFIX = "!!!]";

    private static final char DELIM_START = '{';
    private static final char DELIM_STOP = '}';
    private static final char ESCAPE_CHAR = '\\';

    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_REF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        }
    };

    private MessageFormatter() {}

    /**
     * Replace placeholders in the given messagePattern with arguments.
     *
     * @param messagePattern the message pattern containing placeholders.
     * @param arguments the arguments to be used to replace placeholders.
     * @return the formatted message.
     */
    static String format(final String messagePattern, final Object[] arguments) {
        final StringBuilder result = new StringBuilder();
        final int argCount = arguments == null ? 0 : arguments.length;
        formatMessage(result, messagePattern, arguments, argCount);
        return result.toString();
    }

    /**
     * Replace placeholders in the given messagePattern with arguments.
     *
     * @param buffer the buffer to write the formatted message into
     * @param messagePattern the message pattern containing placeholders.
     * @param arguments the arguments to be used to replace placeholders.
     */
    static void formatMessage(final StringBuilder buffer, final String messagePattern, final Object[] arguments,
        final int argCount) {
        if (messagePattern == null || arguments == null || argCount == 0) {
            buffer.append(messagePattern);
            return;
        }
        int escapeCounter = 0;
        int currentArgument = 0;
        int i = 0;
        final int len = messagePattern.length();
        // last char is excluded from the loop
        for (; i < len - 1; i++) {
            final char curChar = messagePattern.charAt(i);
            if (curChar == ESCAPE_CHAR) {
                escapeCounter++;
            } else {
                // looks ahead one char
                if (isDelimPair(curChar, messagePattern, i)) {
                    i++;

                    // write escaped escape chars
                    writeEscapedEscapeChars(escapeCounter, buffer);

                    if (isOdd(escapeCounter)) {
                        // i.e. escaped: write escaped escape chars
                        writeDelimPair(buffer);
                    } else {
                        // unescaped
                        writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
                        currentArgument++;
                    }
                } else {
                    handleLiteralChar(buffer, escapeCounter, curChar);
                }
                escapeCounter = 0;
            }
        }
        handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
    }

    /**
     * Returns {@code true} if the specified char and the char at {@code curCharIndex + 1} in the specified message
     * pattern together form a "{}" delimiter pair, returns {@code false} otherwise.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 22 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static boolean isDelimPair(final char curChar, final String messagePattern, final int curCharIndex) {
        return curChar == DELIM_START && messagePattern.charAt(curCharIndex + 1) == DELIM_STOP;
    }

    /**
     * Detects whether the message pattern has been fully processed or if an unprocessed character remains and processes
     * it if necessary, returning the resulting position in the result char array.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 28 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void handleRemainingCharIfAny(final String messagePattern, final int len, final StringBuilder buffer,
        final int escapeCounter, final int i) {
        if (i == len - 1) {
            final char curChar = messagePattern.charAt(i);
            handleLastChar(buffer, escapeCounter, curChar);
        }
    }

    /**
     * Processes the last unprocessed character and returns the resulting position in the result char array.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 28 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void handleLastChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
        if (curChar == ESCAPE_CHAR) {
            writeUnescapedEscapeChars(escapeCounter + 1, buffer);
        } else {
            handleLiteralChar(buffer, escapeCounter, curChar);
        }
    }

    /**
     * Processes a literal char (neither an '\' escape char nor a "{}" delimiter pair) and returns the resulting
     * position.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 16 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void handleLiteralChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
        // any other char beside ESCAPE or DELIM_START/STOP-combo
        // write unescaped escape chars
        writeUnescapedEscapeChars(escapeCounter, buffer);
        buffer.append(curChar);
    }

    /**
     * Writes "{}" to the specified result array at the specified position and returns the resulting position.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 18 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void writeDelimPair(final StringBuilder buffer) {
        buffer.append(DELIM_START);
        buffer.append(DELIM_STOP);
    }

    /**
     * Returns {@code true} if the specified parameter is odd.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 11 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static boolean isOdd(final int number) {
        return (number & 1) == 1;
    }

    /**
     * Writes a '\' char to the specified result array (starting at the specified position) for each <em>pair</em> of
     * '\' escape chars encountered in the message format and returns the resulting position.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 11 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void writeEscapedEscapeChars(final int escapeCounter, final StringBuilder buffer) {
        // divide by two
        final int escapedEscapes = escapeCounter >> 1;
        writeUnescapedEscapeChars(escapedEscapes, buffer);
    }

    /**
     * Writes the specified number of '\' chars to the specified result array (starting at the specified position) and
     * returns the resulting position.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 20 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void writeUnescapedEscapeChars(int escapeCounter, final StringBuilder buffer) {
        while (escapeCounter > 0) {
            buffer.append(ESCAPE_CHAR);
            escapeCounter--;
        }
    }

    /**
     * Appends the argument at the specified argument index (or, if no such argument exists, the "{}" delimiter pair) to
     * the specified result char array at the specified position and returns the resulting position.
     *
     * <p>
     * Profiling showed this method is important to log4j performance. Modify with care! 25 bytes (allows immediate JVM
     * inlining: < 35 bytes) LOG4J2-1096
     */
    private static void writeArgOrDelimPair(final Object[] arguments, final int argCount, final int currentArgument,
        final StringBuilder buffer) {
        if (currentArgument < argCount) {
            recursiveDeepToString(arguments[currentArgument], buffer);
        } else {
            writeDelimPair(buffer);
        }
    }

    /**
     * This method performs a deep {@code toString()} of the given {@code Object}.
     *
     * <p>
     * Primitive arrays are converted using their respective {@code Arrays.toString()} methods, while special handling
     * is implemented for <i>container types</i>, i.e. {@code Object[]}, {@code Map} and {@code Collection}, because
     * those could contain themselves.
     *
     * <p>
     * It should be noted that neither {@code AbstractMap.toString()} nor {@code
     * AbstractCollection.toString()} implement such a behavior. They only check if the container is directly contained
     * in itself, but not if a contained container contains the original one. Because of that,
     * {@code Arrays.toString(Object[])} isn't safe either. Confusing? Just read the last paragraph again and check the
     * respective {@code toString()} implementation.
     *
     * <p>
     * This means, in effect, that logging would produce a usable output even if an ordinary
     * {@code System.out.println(o)} would produce a relatively hard-to-debug {@code
     * StackOverflowError}.
     *
     * @param o the {@code Object} to convert into a {@code String}
     * @param str the {@code StringBuilder} that {@code o} will be appended to
     */
    static void recursiveDeepToString(final Object o, final StringBuilder str) {
        recursiveDeepToString(o, str, null);
    }

    /**
     * This method performs a deep {@code toString()} of the given {@code Object}.
     *
     * <p>
     * Primitive arrays are converted using their respective {@code Arrays.toString()} methods, while special handling
     * is implemented for <i>container types</i>, i.e. {@code Object[]}, {@code Map} and {@code Collection}, because
     * those could contain themselves.
     *
     * <p>
     * {@code dejaVu} is used in case of those container types to prevent an endless recursion.
     *
     * <p>
     * It should be noted that neither {@code AbstractMap.toString()} nor {@code
     * AbstractCollection.toString()} implement such a behavior. They only check if the container is directly contained
     * in itself, but not if a contained container contains the original one. Because of that,
     * {@code Arrays.toString(Object[])} isn't safe either. Confusing? Just read the last paragraph again and check the
     * respective {@code toString()} implementation.
     *
     * <p>
     * This means, in effect, that logging would produce a usable output even if an ordinary
     * {@code System.out.println(o)} would produce a relatively hard-to-debug {@code
     * StackOverflowError}.
     *
     * @param o the {@code Object} to convert into a {@code String}
     * @param str the {@code StringBuilder} that {@code o} will be appended to
     * @param dejaVu a set of container objects directly or transitively containing {@code o}
     */
    private static void recursiveDeepToString(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        if (appendSpecialTypes(o, str)) {
            return;
        }
        if (isMaybeRecursive(o)) {
            appendPotentiallyRecursiveValue(o, str, dejaVu);
        } else {
            tryObjectToString(o, str);
        }
    }

    private static boolean appendSpecialTypes(final Object o, final StringBuilder str) {
        return appendSpecificTypes(str, o) || appendDate(o, str);
    }

    private static boolean appendDate(final Object o, final StringBuilder str) {
        if (!(o instanceof Date)) {
            return false;
        }
        final Date date = (Date)o;
        final SimpleDateFormat format = SIMPLE_DATE_FORMAT_REF.get();
        str.append(format.format(date));
        return true;
    }

    public static boolean appendSpecificTypes(final StringBuilder stringBuilder, final Object obj) {
        if (obj == null || obj instanceof String) {
            stringBuilder.append((String)obj);
        } else if (obj instanceof CharSequence) {
            stringBuilder.append((CharSequence)obj);
            // LOG4J2-1437 unbox auto-boxed primitives to avoid calling toString()
        } else if (obj instanceof Integer) {
            stringBuilder.append(((Integer)obj).intValue());
        } else if (obj instanceof Long) {
            stringBuilder.append(((Long)obj).longValue());
        } else if (obj instanceof Double) {
            stringBuilder.append(((Double)obj).doubleValue());
        } else if (obj instanceof Boolean) {
            stringBuilder.append(((Boolean)obj).booleanValue());
        } else if (obj instanceof Character) {
            stringBuilder.append(((Character)obj).charValue());
        } else if (obj instanceof Short) {
            stringBuilder.append(((Short)obj).shortValue());
        } else if (obj instanceof Float) {
            stringBuilder.append(((Float)obj).floatValue());
        } else if (obj instanceof Byte) {
            stringBuilder.append(((Byte)obj).byteValue());
        } else {
            return false;
        }
        return true;
    }

    /** Returns {@code true} if the specified object is an array, a Map or a Collection. */
    private static boolean isMaybeRecursive(final Object o) {
        return o.getClass().isArray() || o instanceof Map || o instanceof Collection;
    }

    private static void appendPotentiallyRecursiveValue(final Object o, final StringBuilder str,
        final Set<Object> dejaVu) {
        final Class<?> oClass = o.getClass();
        if (oClass.isArray()) {
            appendArray(o, str, dejaVu, oClass);
        } else if (o instanceof Map) {
            appendMap(o, str, dejaVu);
        } else if (o instanceof Collection) {
            appendCollection(o, str, dejaVu);
        } else {
            throw new IllegalArgumentException("was expecting a container, found " + oClass);
        }
    }

    private static void appendArray(final Object o, final StringBuilder str, final Set<Object> dejaVu,
        final Class<?> oClass) {
        if (oClass == byte[].class) {
            str.append(Arrays.toString((byte[])o));
        } else if (oClass == short[].class) {
            str.append(Arrays.toString((short[])o));
        } else if (oClass == int[].class) {
            str.append(Arrays.toString((int[])o));
        } else if (oClass == long[].class) {
            str.append(Arrays.toString((long[])o));
        } else if (oClass == float[].class) {
            str.append(Arrays.toString((float[])o));
        } else if (oClass == double[].class) {
            str.append(Arrays.toString((double[])o));
        } else if (oClass == boolean[].class) {
            str.append(Arrays.toString((boolean[])o));
        } else if (oClass == char[].class) {
            str.append(Arrays.toString((char[])o));
        } else {
            // special handling of container Object[]
            final Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
            final boolean seen = !effectiveDejaVu.add(o);
            if (seen) {
                final String id = identityToString(o);
                str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
            } else {
                final Object[] oArray = (Object[])o;
                str.append('[');
                boolean first = true;
                for (final Object current : oArray) {
                    if (first) {
                        first = false;
                    } else {
                        str.append(", ");
                    }
                    recursiveDeepToString(current, str, cloneDejaVu(effectiveDejaVu));
                }
                str.append(']');
            }
        }
    }

    /** Specialized handler for {@link Map}s. */
    private static void appendMap(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        final Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
        final boolean seen = !effectiveDejaVu.add(o);
        if (seen) {
            final String id = identityToString(o);
            str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
        } else {
            final Map<?, ?> oMap = (Map<?, ?>)o;
            str.append('{');
            boolean isFirst = true;
            for (final Object o1 : oMap.entrySet()) {
                final Map.Entry<?, ?> current = (Map.Entry<?, ?>)o1;
                if (isFirst) {
                    isFirst = false;
                } else {
                    str.append(", ");
                }
                final Object key = current.getKey();
                final Object value = current.getValue();
                recursiveDeepToString(key, str, cloneDejaVu(effectiveDejaVu));
                str.append('=');
                recursiveDeepToString(value, str, cloneDejaVu(effectiveDejaVu));
            }
            str.append('}');
        }
    }

    /** Specialized handler for {@link Collection}s. */
    private static void appendCollection(final Object o, final StringBuilder str, final Set<Object> dejaVu) {
        final Set<Object> effectiveDejaVu = getOrCreateDejaVu(dejaVu);
        final boolean seen = !effectiveDejaVu.add(o);
        if (seen) {
            final String id = identityToString(o);
            str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
        } else {
            final Collection<?> oCol = (Collection<?>)o;
            str.append('[');
            boolean isFirst = true;
            for (final Object anCol : oCol) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    str.append(", ");
                }
                recursiveDeepToString(anCol, str, cloneDejaVu(effectiveDejaVu));
            }
            str.append(']');
        }
    }

    private static Set<Object> getOrCreateDejaVu(Set<Object> dejaVu) {
        return dejaVu == null ? createDejaVu() : dejaVu;
    }

    private static Set<Object> createDejaVu() {
        return Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>() {
            @Override
            public Boolean put(Object key, Boolean value) {
                return true;
            }
        });
    }

    private static Set<Object> cloneDejaVu(Set<Object> dejaVu) {
        Set<Object> clonedDejaVu = createDejaVu();
        clonedDejaVu.addAll(dejaVu);
        return clonedDejaVu;
    }

    private static void tryObjectToString(final Object o, final StringBuilder str) {
        // it's just some other Object, we can only use toString().
        try {
            str.append(o.toString());
        } catch (final Throwable t) {
            handleErrorInObjectToString(o, str, t);
        }
    }

    private static void handleErrorInObjectToString(final Object o, final StringBuilder str, final Throwable t) {
        str.append(ERROR_PREFIX);
        str.append(identityToString(o));
        str.append(ERROR_SEPARATOR);
        final String msg = t.getMessage();
        final String className = t.getClass().getName();
        str.append(className);
        if (!className.equals(msg)) {
            str.append(ERROR_MSG_SEPARATOR);
            str.append(msg);
        }
        str.append(ERROR_SUFFIX);
    }

    /**
     * This method returns the same as if Object.toString() would not have been overridden in obj.
     *
     * <p>
     * Note that this isn't 100% secure as collisions can always happen with hash codes.
     *
     * <p>
     * Copied from Object.hashCode():
     *
     * <blockquote>
     *
     * As much as is reasonably practical, the hashCode method defined by class {@code Object} does return distinct
     * integers for distinct objects. (This is typically implemented by converting the internal address of the object
     * into an integer, but this implementation technique is not required by the Java&#8482; programming language.)
     *
     * </blockquote>
     *
     * @param obj the Object that is to be converted into an identity string.
     * @return the identity string as also defined in Object.toString()
     */
    static String identityToString(final Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
    }
}
