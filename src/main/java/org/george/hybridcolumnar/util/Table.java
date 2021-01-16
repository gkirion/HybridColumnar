package org.george.hybridcolumnar.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table<T> implements Iterable<T[]> {

    private List<T[]> rows;

    public Table() {
        rows = new ArrayList<>();
    }

    public void add(T... item) {
        rows.add(item);
    }

    public void sort() {
        List<Integer> columnIndexes = TableRowAnalyzer.getColumnsOrderedByCardinality(rows);
        TableRowComparator tableRowComparator = new TableRowComparator(columnIndexes);
        rows.sort(tableRowComparator);
    }

    public List<T[]> getRows() {
        return rows;
    }


    @Override
    public Iterator<T[]> iterator() {
        return new TableIterator();
    }

    private class TableIterator implements Iterator<T[]> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < rows.size();
        }

        @Override
        public T[] next() {
            T[] ts = rows.get(index);
            index++;
            return ts;
        }

    }

}
