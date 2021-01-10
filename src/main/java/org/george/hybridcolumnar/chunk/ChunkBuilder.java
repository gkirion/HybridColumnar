package org.george.hybridcolumnar.chunk;

import org.george.hybridcolumnar.column.Column;

import java.util.HashMap;

public class ChunkBuilder {

    private String name;
    private HashMap<String, Column<Comparable>> columns;

    private ChunkBuilder() {}

    private ChunkBuilder(String name) {
        this.name = name;
        columns = new HashMap<>();
    }

    public static ChunkBuilder chunk(String name) {
        return new ChunkBuilder(name);
    }

    public ChunkBuilder column(String name, Column column) {
        columns.put(name, column);
        return this;
    }

    public Chunk build() {
        Chunk chunk = new Chunk();
        chunk.setName(name);
        for (String name : columns.keySet()) {
            chunk.addColumn(name, columns.get(name));
        }
        return chunk;
    }

}
