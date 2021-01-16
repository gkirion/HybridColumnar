package org.george.hybridcolumnar.util;

import java.util.Comparator;
import java.util.List;

public class TableRowComparator<T extends Comparable<T>> implements Comparator<T[]> {

    private List<Integer> orderList;

    public TableRowComparator(List<Integer> orderList) {
        this.orderList = orderList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(T[] o1, T[] o2) {
        for (Integer index : orderList) {
            if (o1[index].compareTo(o2[index]) < 0) {
                return -1;
            } else if (o1[index].compareTo(o2[index]) > 0) {
                return 1;
            }
        }
        return 0;
    }

}
