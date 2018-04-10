package org.george.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class ColumnDictionaryPlain<E extends Comparable<E>> implements Column<E>, Serializable {

    private ArrayList<Integer> arrayList;
    private Dictionary<E> dictionary;
    private String name;
    private Integer id;

    public ColumnDictionaryPlain() {
        arrayList = new ArrayList<>();
        dictionary = new Dictionary<>();
        name = "";
        id = 0;
    }

    public ColumnDictionaryPlain(String name) {
        arrayList = new ArrayList<>();
        dictionary = new Dictionary<>();
        this.name = name;
        id = 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(E item) {
        Integer key = dictionary.insert(item);
        arrayList.add(key);
        id++;
    }

    public Tuple2<E, Integer> get(int i) {
        return new Tuple2<E, Integer>(dictionary.get(arrayList.get(i)), 1);
    }

    public int getLength() {
        return arrayList.size();
    }

    /*public ArrayList<E> entrySet() {
        return arrayList;
    }*/

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (Integer value : arrayList) {

        }
        sb.append("]");
        return arrayList.toString();
    }

    @Override
    public BitSet selectEquals(E item) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < id; i++) {
            if (dictionary.get(arrayList.get(i)).equals(item)) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public BitSet selectNotEquals(E item) {
        BitSet bitSet = selectEquals(item);
        BitSet bSet = new BitSet();
        bSet.set(0, id); // set all to 1
        bSet.andNot(bitSet);
        return bSet;
    }

    @Override
    public BitSet selectLessThan(E item) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < id; i++) {
            if (dictionary.get(arrayList.get(i)).compareTo(item) < 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public BitSet selectLessThanOrEquals(E item) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < id; i++) {
            if (dictionary.get(arrayList.get(i)).compareTo(item) <= 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public BitSet selectMoreThan(E item) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < id; i++) {
            if (dictionary.get(arrayList.get(i)).compareTo(item) > 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public BitSet selectMoreThanOrEquals(E item) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < id; i++) {
            if (dictionary.get(arrayList.get(i)).compareTo(item) >= 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public BitSet selectBetween(E from, E to) {
        BitSet bitSet = new BitSet();
        for (int i = 0; i < id; i++) {
            if (dictionary.get(arrayList.get(i)).compareTo(from) >= 0 && dictionary.get(arrayList.get(i)).compareTo(to) <= 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }

    @Override
    public Long sum(int start, int end) {
        Long sum = new Long(0);
        for (int i = start; i < end; i++) {
            sum += (Integer)dictionary.get(arrayList.get(i));
        }
        return sum;
    }

    @Override
    public Long sum(BitSet bitSet) {
        Long sum = new Long(0);
        for (int i = 0; i < id; i++) {
            if (bitSet.get(i)) {
                sum += (Integer)dictionary.get(arrayList.get(i));
            }
        }
        return sum;
    }

    @Override
    public Integer count(int start, int end) {
        return (end < id ? end : id) - start;
    }

    @Override
    public Double avg(int start, int end) {
        return sum(start, end) / (double)count(start, end);
    }

    @Override
    public int getCardinality() {
        HashMap<E, Boolean> distinctMap = new HashMap<>();
        for (Integer value : arrayList) {
            distinctMap.put(dictionary.get(value), true);
        }
        return distinctMap.size();
    }

}
