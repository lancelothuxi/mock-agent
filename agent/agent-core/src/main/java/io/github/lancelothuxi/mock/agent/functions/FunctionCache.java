package io.github.lancelothuxi.mock.agent.functions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public class FunctionCache {

    public static Map<String, CompoundVariable> cache = new HashMap<>();

    public static int size() {
        return cache.size();
    }

    public static boolean isEmpty() {
        return cache.isEmpty();
    }

    public static boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    public static boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    public static CompoundVariable get(Object key) {
        return cache.get(key);
    }

    public static CompoundVariable put(String key, CompoundVariable value) {
        return cache.put(key, value);
    }

    public static CompoundVariable remove(Object key) {
        return cache.remove(key);
    }

    public static void putAll(Map<? extends String, ? extends CompoundVariable> m) {
        cache.putAll(m);
    }

    public static void clear() {
        cache.clear();
    }

    public static Set<String> keySet() {
        return cache.keySet();
    }

    public static Collection<CompoundVariable> values() {
        return cache.values();
    }

    public static Set<Map.Entry<String, CompoundVariable>> entrySet() {
        return cache.entrySet();
    }

    public static CompoundVariable getOrDefault(Object key, CompoundVariable defaultValue) {
        return cache.getOrDefault(key, defaultValue);
    }

    public static void forEach(BiConsumer<? super String, ? super CompoundVariable> action) {
        cache.forEach(action);
    }

    public static void replaceAll(BiFunction<? super String, ? super CompoundVariable, ? extends CompoundVariable> function) {
        cache.replaceAll(function);
    }

    public static CompoundVariable putIfAbsent(String key, CompoundVariable value) {
        return cache.putIfAbsent(key, value);
    }

    public static boolean remove(Object key, Object value) {
        return cache.remove(key, value);
    }

    public static boolean replace(String key, CompoundVariable oldValue, CompoundVariable newValue) {
        return cache.replace(key, oldValue, newValue);
    }

    public static CompoundVariable replace(String key, CompoundVariable value) {
        return cache.replace(key, value);
    }

    public static CompoundVariable computeIfAbsent(String key, Function<? super String, ? extends CompoundVariable> mappingFunction) {
        return cache.computeIfAbsent(key, mappingFunction);
    }

    public static CompoundVariable computeIfPresent(String key, BiFunction<? super String, ? super CompoundVariable, ? extends CompoundVariable> remappingFunction) {
        return cache.computeIfPresent(key, remappingFunction);
    }

    public static CompoundVariable compute(String key, BiFunction<? super String, ? super CompoundVariable, ? extends CompoundVariable> remappingFunction) {
        return cache.compute(key, remappingFunction);
    }

    public static CompoundVariable merge(String key, CompoundVariable value, BiFunction<? super CompoundVariable, ? super CompoundVariable, ? extends CompoundVariable> remappingFunction) {
        return cache.merge(key, value, remappingFunction);
    }
}
