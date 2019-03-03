package org.george.hybridcolumnar.util;

import java.util.HashMap;

public class Dictionary<E> {

    private HashMap<E, Integer> mappings;
    private HashMap<Integer, E> reverseMappings;
    private int id;

    public Dictionary() {
        mappings = new HashMap<>();
        reverseMappings = new HashMap<>();
        id = 0;
    }

    public Integer insert(E item) {
        if (!mappings.containsKey(item)) {
            mappings.put(item, id);
            reverseMappings.put(id, item);
            id++;
        }
        return mappings.get(item);
    }

    public E get(Integer item) {
        return reverseMappings.get(item);
    }

}
