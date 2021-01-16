package org.george.hybridcolumnar.util;

import org.george.hybridcolumnar.domain.Tuple2;

import java.util.*;

public class TableRowAnalyzer {

    public static<T> List<Integer> getColumnsOrderedByCardinality(List<T[]> rows) {
        HashMap<Integer, Set<T>> uniqueElements = new HashMap<>();
        for (T[] row : rows) {
            int i = 0;
            for (T item : row) {
                if (uniqueElements.get(i) == null) {
                    uniqueElements.put(i, new HashSet<>());
                }
                uniqueElements.get(i).add(item);
                i++;
            }
        }
        ArrayList<Tuple2<Integer, Integer>> cardinalities = new ArrayList<>();
        for (int i = 0; i < uniqueElements.size(); i++) {
            cardinalities.add(new Tuple2<Integer, Integer>(i, uniqueElements.get(i).size()));
        }
        cardinalities.sort(new Comparator<Tuple2<Integer, Integer>>() {

            @Override
            public int compare(Tuple2<Integer, Integer> o1, Tuple2<Integer, Integer> o2) {
                return o1.getSecond().compareTo(o2.getSecond());
            }

        });
        List<Integer> orderList = new ArrayList<>();
        for (Tuple2<Integer, Integer> tuple : cardinalities) {
            orderList.add(tuple.getFirst());
        }
        return orderList;

    }

}
