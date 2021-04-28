package com.paxovision.rest.utils;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class FlatMapUtil {

    private FlatMapUtil() {
        throw new AssertionError("No instances for you!");
    }

    /*public static Map<String, Object> flatten(Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .flatMap(FlatMapUtil::flatten)
                .collect(LinkedHashMap::new, (m, e) -> m.put("/" + e.getKey(), e.getValue()), LinkedHashMap::putAll);
    }*/

    public static Map<String, Object> flatten(Object json) {
        if(json instanceof LinkedHashMap) {
            Map<String, Object> map = (Map<String, Object>) json;
            return map.entrySet()
                    .stream()
                    .flatMap(FlatMapUtil::flatten)
                    .sorted(Map.Entry.comparingByKey())
                    .collect(LinkedHashMap::new, (m, e) -> m.put("/" + e.getKey(), e.getValue()), LinkedHashMap::putAll);
        }
        else if(json instanceof HashMap) {
            Map<String, Object> map = (Map<String, Object>) json;
            return map.entrySet()
                    .stream()
                    .flatMap(FlatMapUtil::flatten)
                    .sorted(Map.Entry.comparingByKey())
                    .collect(LinkedHashMap::new, (m, e) -> m.put("/" + e.getKey(), e.getValue()), LinkedHashMap::putAll);
        }
        else if(json instanceof ArrayList){
            ArrayList<?> list = (ArrayList<?>) json;
            return IntStream.range(0, list.size())
                    .mapToObj(i -> new SimpleEntry<String, Object>( "/" + i, list.get(i)))
                    .flatMap(FlatMapUtil::flatten)
                    .sorted(Map.Entry.comparingByKey())
                    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), LinkedHashMap::putAll);
        }
        else  {
            return new LinkedHashMap<>();
        }
    }

    private static Stream<Entry<String, Object>> flatten(Entry<String, Object> entry) {

        if (entry == null) {
            return Stream.empty();
        }

        if (entry.getValue() instanceof Map<?, ?>) {
            Map<?, ?> properties = (Map<?, ?>) entry.getValue();
            return properties.entrySet()
                    .stream()
                    .flatMap(e -> flatten(new SimpleEntry<>(entry.getKey() + "/" + e.getKey(), e.getValue())));
        }

        if (entry.getValue() instanceof List<?>) {
            List<?> list = (List<?>) entry.getValue();
            return IntStream.range(0, list.size())
                    .mapToObj(i -> new SimpleEntry<String, Object>(entry.getKey() + "/" + i, list.get(i)))
                    .flatMap(FlatMapUtil::flatten);
        }

        return Stream.of(entry);
    }


    public static void main(String[] args) {
        String jsonString ="";
    }
}