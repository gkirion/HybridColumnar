package org.george.hybridcolumnar.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.george.hybridcolumnar.chunk.Chunk;
import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnFactory;
import org.george.hybridcolumnar.column.ColumnType;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.RowArray;
import org.george.hybridcolumnar.domain.Tuple2;

public class ChunkDriver {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ParseException {
		/*
		 * Tuple2<Integer, String> tuple2 = new Tuple2<Integer, String>(29, "george");
		 * System.out.println(tuple2.getFirst());
		 * System.out.println(tuple2.getSecond()); System.out.println(tuple2);
		 * System.out.println(tuple2.hashCode()); System.out.println(tuple2.equals(new
		 * Tuple2<String, String>("29", "george"))); HashMap<Tuple2<Integer, String>,
		 * Integer> map = new HashMap<>(); map.put(new Tuple2<Integer, String>(29,
		 * "george"), 101); System.out.println(map.get(new Tuple2<Integer, String>(29,
		 * "george"))); ColumnRle<String> col = new ColumnRle<String>("number");
		 * 
		 * for (int i = 0; i < 1000000; i++) { col.add(i + ""); }
		 * System.out.println(col.get(1000000)); System.out.println(col.get(1000000 -
		 * 1)); for (Entry<Integer, Tuple2<String, Integer>> entry : col.entrySet()) {
		 * System.out.println(entry.getKey() + "\t" + entry.getValue()); }
		 * 
		 * Column<Integer> age = new ColumnRle<Integer>("age"); Column<String> name =
		 * new ColumnRle<String>("name"); Chunk2<Integer, String> c = new Chunk2<>(age,
		 * name); age.add(29); age.add(29); name.add("george"); age.add(28);
		 * name.add("nick"); age.add(29); name.add("bob"); System.out.println("sum: " +
		 * age.sum(0, 4)); System.out.println("count: " + age.count(0, 4));
		 * System.out.println("avg: " + age.avg(0, 4)); System.out.println(age.get(3));
		 * System.out.println(age.get(0)); System.out.println(age.get(1));
		 * System.out.println(age.get(2));
		 * 
		 * System.out.println(age); System.out.println(name);
		 * 
		 * System.out.println(name.get(0));
		 * 
		 * Chunk2<String, Integer> chunk = new Chunk2<String, Integer>("name", "age");
		 * 
		 * chunk.add("george", 29); chunk.add("george", 29); chunk.add("george", 30);
		 * System.out.println(chunk); System.out.println(chunk.getAll());
		 * System.out.println(map); col.add(10 + ""); col.add(10 + ""); col.add(11 +
		 * ""); col.add(10 + ""); col.add(10 + ""); col.add(10 + ""); Tuple2 t = new
		 * Tuple2("geo", 18); System.out.println(t.equals(new Tuple2("geo", "18")));
		 * System.out.println(t); Object s = new String("geo"); s = new Integer(4);
		 * System.out.println(s);
		 * 
		 * for (Entry<Integer, Tuple2<String, Integer>> entry : col.entrySet()) {
		 * System.out.println(entry.getKey() + "\t" + entry.getValue()); }
		 * 
		 * ArrayList<Tuple2<String, Integer>> list = new ArrayList<>(); list.add(new
		 * Tuple2<String, Integer>("tzopaty", 1)); list.add(new Tuple2<String,
		 * Integer>("tzopaty", 2)); list.add(new Tuple2<String, Integer>("george", 1));
		 * list.add(new Tuple2<String, Integer>("aa", 1)); list.sort(null);
		 * System.out.println(list); ArrayList<Tuple3<Integer, Integer, Integer>>
		 * intList = new ArrayList<>(); intList.add(new Tuple3<Integer, Integer,
		 * Integer>(2, 3, 2)); intList.add(new Tuple3<Integer, Integer, Integer>(1, 3,
		 * 2)); intList.add(new Tuple3<Integer, Integer, Integer>(2, 2, 3));
		 * intList.add(new Tuple3<Integer, Integer, Integer>(2, 2, 2));
		 * intList.sort(null); System.out.println(intList);
		 * System.out.println(Integer.class); Object o = 1; Object o2 = 2;
		 * System.out.println(map.getClass()); System.out.println(o.getClass());
		 * Column<Integer> colp = new ColumnPlain<>(); Column<Integer> colr = new
		 * ColumnRle<>(); Random rand = new Random(); int num; for (int i = 0; i <
		 * 1000000; i++) { num = rand.nextInt(); colp.add(num); colr.add(num); }
		 * 
		 * System.out.println("generic column test"); Chunk2<Integer, Integer> chunk2t =
		 * new Chunk2<>(colp, colr); Chunk chunkGeneric = new Chunk();
		 * chunkGeneric.addColumn(colp); chunkGeneric.addColumn(colr); int count = 0;
		 * 
		 * long startTime = System.currentTimeMillis();
		 * 
		 * for (ArrayList<Comparable> row : chunkGeneric) { Integer n = (Integer)
		 * row.get(0); count += n; }
		 * 
		 * long stopTime = System.currentTimeMillis();
		 * 
		 * System.out.println(stopTime - startTime + " ms"); System.out.println(count);
		 * 
		 * count = 0; startTime = System.currentTimeMillis();
		 * 
		 * for (Tuple4<Integer, Integer, Integer, Integer> row : chunk2t) { count +=
		 * row.getFirst(); }
		 * 
		 * stopTime = System.currentTimeMillis();
		 * 
		 * System.out.println(stopTime - startTime + " ms"); System.out.println(count);
		 * 
		 * 
		 * colp.add(4); colp.add(4); colp.add(5); colp.add(5); colp.add(5); colp.add(1);
		 * 
		 * System.out.println( "sum test: " + (colp.sum(19,
		 * colp.length()).equals(colr.sum(19, colr.length())) ? "ok" : "not ok"));
		 * System.out.println("length test: " + (colp.count(88, colp.length() -
		 * 1000).equals(colr.count(88, colr.length() - 1000)) ? "ok" : "not ok"));
		 * System.out.println("avg test: " + (colp.avg(88, colp.length() -
		 * 1000).equals(colr.avg(88, colr.length() - 1000)) ? "ok" : "not ok"));
		 * System.out.println(colp.sum(88, colp.length() - 1000));
		 * System.out.println(colr.sum(88, colr.length() - 1000));
		 * System.out.println(colp.sum(88, colp.length() - 1000).equals(colr.sum(88,
		 * colr.length() - 1000))); System.out.println(new Long(1000000000000L) + 1);
		 * HashMap<Tuple2<Integer, Integer>, Integer> hmap = new HashMap<>();
		 * Tuple2<Integer, Integer> t2; t2 = new Tuple2<Integer, Integer>(3, 2);
		 * hmap.put(t2, 5); System.out.println(hmap); t2.setFirst(7);
		 * System.out.println(hmap); ArrayList<Tuple3<Integer, Integer, Integer>>
		 * rowlist = new ArrayList<>(); for (int i = 0; i < 1000000; i++) {
		 * rowlist.add(new Tuple3<Integer, Integer, Integer>(rand.nextInt(201),
		 * rand.nextInt(201), rand.nextInt(201))); }
		 * 
		 * rowlist.sort(null); System.out.println(rowlist.subList(0, 100));
		 * Chunk3<Integer, Integer, Integer> c3 = new Chunk3<>("", "", ""); for (int i =
		 * 0; i < 1000000; i++) { c3.add(rowlist.get(i).getFirst(),
		 * rowlist.get(i).getSecond(), rowlist.get(i).getThird()); } //
		 * System.out.println(c3.get(1000000 - 1)); // c3.add(101, c3.get(1000000 -
		 * 1).getSecond(), c3.get(1000000 - 1).getThird()); //
		 * System.out.println(c3.get(1000000)); Instant start; Instant end; Duration
		 * duration;
		 * 
		 * // start = Instant.now(); Runtime runtime = Runtime.getRuntime();
		 * runtime.gc(); long memory = runtime.totalMemory() - runtime.freeMemory();
		 * System.out.println("memory used: " + memory / 1024); startTime =
		 * System.currentTimeMillis();
		 * 
		 * ArrayList<Tuple5<Integer, Integer, Integer, Integer, Integer>> a1 =
		 * c3.getAll(); for (Tuple5<Integer, Integer, Integer, Integer, Integer> tt :
		 * a1) {
		 * 
		 * }
		 * 
		 * stopTime = System.currentTimeMillis();
		 * 
		 * runtime = Runtime.getRuntime(); runtime.gc(); memory = runtime.totalMemory()
		 * - runtime.freeMemory(); System.out.println("memory used: " + memory / 1024);
		 * 
		 * // end = Instant.now();
		 * 
		 * // duration = Duration.between(start, end); //
		 * System.out.println(duration.toMillis() + " ms"); System.out.println(stopTime
		 * - startTime + " ms");
		 * 
		 * // start = Instant.now(); startTime = System.currentTimeMillis();
		 * 
		 * ArrayList<Tuple5<Integer, Integer, Integer, Integer, Integer>> a2 =
		 * c3.getAllImproved(); for (Tuple5<Integer, Integer, Integer, Integer, Integer>
		 * tt : a2) {
		 * 
		 * } // end = Instant.now(); stopTime = System.currentTimeMillis();
		 * 
		 * runtime = Runtime.getRuntime(); runtime.gc(); memory = runtime.totalMemory()
		 * - runtime.freeMemory(); System.out.println("memory used: " + memory / 1024);
		 * // duration = Duration.between(start, end); //
		 * System.out.println(duration.toMillis() + " ms"); System.out.println(stopTime
		 * - startTime + " ms");
		 * 
		 * 
		 * Chunk2<Integer, Integer> c2 = new Chunk2<>(c3.getFirstColumn(),
		 * c3.getThirdColumn()); ArrayList<Tuple4<Integer, Integer, Integer, Integer>>
		 * a12 = c2.getAll(); ArrayList<Tuple4<Integer, Integer, Integer, Integer>> a22
		 * = c2.getAllImproved(); System.out.println("rows test c2: " + (a12.size() ==
		 * a22.size() ? "ok" : "not ok")); n = a12.size(); for (int i = 0; i < n; i++) {
		 * if (!a12.get(i).equals(a22.get(i))) {
		 * System.out.println("rows test c2: not ok"); System.out.println(a12.get(i) +
		 * " " + a22.get(i)); break; } } System.out.println("rows test c2: ok");
		 * 
		 * int n = a1.size(); System.out.println("rows test: " + (a1.size() == a2.size()
		 * ? "ok" : "not ok"));
		 * 
		 * for (int i = 0; i < n; i++) { if (!a1.get(i).equals(a2.get(i))) {
		 * System.out.println("rows test: not ok"); System.out.println(a1.get(i) + " " +
		 * a2.get(i)); break; } } System.out.println("rows test: ok");
		 * 
		 * int j = 0; for (Tuple3<Integer, Integer, Integer> tt : rowlist) { //
		 * System.out.println(tt); if (j > 100) break; j++; }
		 * 
		 * // start = Instant.now();
		 * 
		 * Chunk2<Integer, Integer> c2 = new Chunk2<>(c3.getFirstColumn(),
		 * c3.getSecondColumn()); ArrayList<Tuple4<Integer, Integer, Integer, Integer>>
		 * arr21 = c2.getAll(); ArrayList<Tuple4<Integer, Integer, Integer, Integer>>
		 * arr22 = c2.getAllImproved();
		 * 
		 * System.out.println("rows test: " + (arr21.size() == arr22.size() ? "ok" :
		 * "not ok"));
		 * 
		 * n = arr21.size(); for (int i = 0; i < n; i++) { if
		 * (!arr21.get(i).equals(arr22.get(i))) {
		 * System.out.println("rows test: not ok"); System.out.println(arr21.get(i) +
		 * " " + arr22.get(i)); break; } }
		 * 
		 * System.out.println("rows test: ok");
		 * 
		 * int i = 0; for (Tuple4<Integer, Integer, Integer, Integer> tt : c2) { if
		 * (!arr21.get(i).equals(tt)) { System.out.println("rows test: not ok");
		 * System.out.println(arr21.get(i) + " " + tt); break; } i++; }
		 * System.out.println("rows test: " + (arr21.size() == i ? "ok" : "not ok"));
		 * System.out.println("rows test: ok");
		 * 
		 * startTime = System.currentTimeMillis();
		 * 
		 * i = 0;
		 * 
		 * for (Tuple5<Integer, Integer, Integer, Integer, Integer> tt : c3) { if
		 * (!a1.get(i).equals(tt)) { System.out.println("rows test: not ok");
		 * System.out.println(a1.get(i) + " " + tt); break; } i++; }
		 * System.out.println("rows test: " + (a1.size() == i ? "ok" : "not ok"));
		 * System.out.println("rows test: ok");
		 * 
		 * // end = Instant.now(); stopTime = System.currentTimeMillis(); runtime =
		 * Runtime.getRuntime(); runtime.gc(); memory = runtime.totalMemory() -
		 * runtime.freeMemory(); System.out.println("memory used: " + memory / 1024);
		 * System.out.println(stopTime - startTime + " ms");
		 * 
		 * // duration = Duration.between(start, end); //
		 * System.out.println(duration.toMillis() + " ms");
		 * 
		 * BitSet bitSet = new BitSet(); bitSet.set(10); bitSet.set(2); for (int k = 0;
		 * k < bitSet.length(); k++) { System.out.println(bitSet.get(k)); }
		 * System.out.println(bitSet.cardinality());
		 * 
		 * ColumnBitmap<Integer> colbit = new ColumnBitmap<>(); ColumnRle<Integer>
		 * colrle = new ColumnRle<>(); ColumnPlain<Integer> colplain = new
		 * ColumnPlain<>(); ColumnBitmapRoaring<Integer> colror = new
		 * ColumnBitmapRoaring<>(); System.out.println( "KB: " + (double)
		 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) /
		 * 1024); ArrayList<Integer> arrint = new ArrayList<>(); for (int k = 0; k <
		 * 1000000; k++) { num = rand.nextInt(100); arrint.add(num); } arrint.sort((a,
		 * b) -> a.compareTo(b) * (-1)); for (int k = 0; k < 1000000; k++) { // num =
		 * rand.nextInt(100); num = arrint.get(k); colbit.add(num); colrle.add(num);
		 * colplain.add(num); colror.add(num); } System.out.println(arrint.get(0));
		 * System.out.println(arrint.get(1)); System.out.println(arrint.get(2));
		 * 
		 * // System.out.println(colbit.getCardinality()); System.out.println( "KB: " +
		 * (double) (Runtime.getRuntime().totalMemory() -
		 * Runtime.getRuntime().freeMemory()) / 1024); for (int k = 0; k < 1000000; k++)
		 * { if (!colbit.get(k).getFirst().equals(colrle.get(k).getFirst()) ||
		 * !colrle.get(k).getFirst().equals(colplain.get(k).getFirst()) ||
		 * !colror.get(k).getFirst().equals(colplain.get(k).getFirst())) {
		 * System.out.println("NOT OK"); } } for (int k = 1000; k < 1000000 - 92323;
		 * k++) { if (!colbit.get(k).getFirst().equals(colrle.get(k).getFirst()) ||
		 * !colrle.get(k).getFirst().equals(colplain.get(k).getFirst()) ||
		 * !colror.get(k).getFirst().equals(colplain.get(k).getFirst())) {
		 * System.out.println("NOT OK"); } } for (int k = 1000; k < 1000000 - 92323;
		 * k++) { if (!colbit.get(k).getFirst().equals(colrle.get(k).getFirst()) ||
		 * !colrle.get(k).getFirst().equals(colplain.get(k).getFirst()) ||
		 * !colror.get(k).getFirst().equals(colplain.get(k).getFirst())) {
		 * System.out.println("NOT OK"); } } if (!colplain.sum(10, colplain.getLength()
		 * - 90000).equals(colbit.sum(10, colbit.getLength() - 90000))) {
		 * System.out.println("SUM NOT OK"); } if (!colplain.sum(10,
		 * colplain.getLength() - 90000).equals(colror.sum(10, colror.getLength() -
		 * 90000))) { System.out.println("1SUM NOT OK"); } if (!colplain.sum(10,
		 * colplain.getLength()).equals(colror.sum(10, colror.getLength()))) {
		 * System.out.println("2SUM NOT OK"); } if (!colplain.sum(10,
		 * colplain.getLength()).equals(colrle.sum(10, colrle.getLength()))) {
		 * System.out.println("2SUM NOT OK"); } if (!colplain.sum(10,
		 * colplain.getLength() - 90000).equals(colrle.sum(10, colrle.getLength() -
		 * 90000))) { System.out.println("2SUM NOT OK"); } if (!colplain.sum(0,
		 * colplain.getLength()).equals(colrle.sum(0, colrle.getLength()))) {
		 * System.out.println("2SUM NOT OK"); } if (!colplain.sum(0,
		 * colplain.getLength() - 90000).equals(colrle.sum(0, colrle.getLength() -
		 * 90000))) { System.out.println("2SUM NOT OK"); }
		 * 
		 * 
		 * start = Instant.now(); for (int k = 0; k < 1000000; k++) { colplain.get(k); }
		 * end = Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("PLAIN ITERATE: " + duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); for (int k = 0; k < 1000000; k++) { colrle.get(k); }
		 * end = Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("RLE ITERATE: " + duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); for (int k = 0; k < 1000000; k++) { colbit.get(k); }
		 * end = Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("BITSET ITERATE: " + duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); for (int k = 0; k < 1000000; k++) { colror.get(k); }
		 * end = Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("ROARING ITERATE: " + duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); colplain.sum(0, colplain.length()); end =
		 * Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("PLAIN: " + duration.toMillis() + " ms");
		 * System.out.println(colplain.sum(0, colplain.length()));
		 * 
		 * start = Instant.now(); colrle.sum(0, colrle.length()); end = Instant.now();
		 * duration = Duration.between(start, end); System.out.println("RLE: " +
		 * duration.toMillis() + " ms"); System.out.println(colrle.sum(0,
		 * colrle.length()));
		 * 
		 * start = Instant.now(); colbit.sum(0, colbit.length()); end = Instant.now();
		 * duration = Duration.between(start, end); System.out.println("BITSET: " +
		 * duration.toMillis() + " ms"); System.out.println(colbit.sum(0,
		 * colbit.length()));
		 * 
		 * start = Instant.now(); colror.sum(0, colror.length()); end = Instant.now();
		 * duration = Duration.between(start, end); System.out.println("ROARING: " +
		 * duration.toMillis() + " ms"); System.out.println(colror.sum(0,
		 * colror.length()));
		 * 
		 * start = Instant.now(); colplain.select(e -> e > 4 && e <= 11); end =
		 * Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("PLAIN SELECT: " + duration.toMillis() + " ms");
		 * System.out.println(colplain.select(e -> e > 4 && e <= 11).cardinality());
		 * start = Instant.now(); colrle.select(e -> e > 4 && e <= 11); end =
		 * Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("RLE SELECT: " + duration.toMillis() + " ms");
		 * System.out.println(colrle.select(e -> e > 4 && e <= 11).cardinality()); start
		 * = Instant.now(); colbit.select(e -> e > 4 && e <= 11); end = Instant.now();
		 * duration = Duration.between(start, end); System.out.println("BITSET SELECT: "
		 * + duration.toMillis() + " ms"); System.out.println(colbit.select(e -> e > 4
		 * && e <= 11).cardinality()); start = Instant.now(); colror.select(e -> e > 4
		 * && e <= 11); end = Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("ROARING SELECT: " + duration.toMillis() + " ms");
		 * System.out.println(colror.select(e -> e > 4 && e <= 11).cardinality());
		 * 
		 * BitSet bSet = colplain.selectLessThan(40); // RoaringBitmap bSet =
		 * colplain.selectLessThanOrEquals(9); start = Instant.now();
		 * colplain.sum(bSet); end = Instant.now(); duration = Duration.between(start,
		 * end); System.out.println("SUM PLAIN: " + duration.toMillis() + " ms");
		 * System.out.println(colplain.sum(bSet));
		 * 
		 * start = Instant.now(); colrle.sum(bSet); end = Instant.now(); duration =
		 * Duration.between(start, end); System.out.println("SUM RLE: " +
		 * duration.toMillis() + " ms"); System.out.println(colrle.sum(bSet));
		 * 
		 * start = Instant.now(); colbit.sum(bSet); end = Instant.now(); duration =
		 * Duration.between(start, end); System.out.println("SUM BITSET: " +
		 * duration.toMillis() + " ms"); System.out.println(colbit.sum(bSet));
		 * 
		 * start = Instant.now(); colror.sum(bSet); end = Instant.now(); duration =
		 * Duration.between(start, end); System.out.println("SUM ROARING: " +
		 * duration.toMillis() + " ms"); System.out.println(colror.sum(bSet));
		 * 
		 * start = Instant.now(); colror.selectLessThan(40); end = Instant.now();
		 * duration = Duration.between(start, end);
		 * System.out.println("SELECT LESS THAN ROARING: " + duration.toMillis() +
		 * " ms"); System.out.println(colror.sum(bSet));
		 * 
		 * int testint = 40000; short test = (short) (testint);
		 * System.out.println(test); System.out.println((int) test);
		 * System.out.println(test & 0xFFFF);
		 * 
		 * BitSet bitSet2 = new BitSet(); bitSet2.set(0); bitSet2.set(1);
		 * bitSet2.set(2); BitSet bitSet3 = bitSet2.get(1, 3);
		 * System.out.println(bitSet3.get(0)); System.out.println(bitSet3.get(1));
		 * System.out.println(bitSet3.get(2)); System.out.println(bitSet2.length());
		 * System.out.println(bitSet3.length()); bitSet3.and(bitSet2);
		 * System.out.println(bitSet3);
		 * 
		 * RoaringBitmap roaringBitmap = new RoaringBitmap(); BitSet bitSet4 = new
		 * BitSet(); start = Instant.now(); for (int k = 0; k < 1000000; k++) { num =
		 * arrint.get(k); roaringBitmap.set(k * 100); } end = Instant.now(); duration =
		 * Duration.between(start, end); System.out.println("ITERATE ROARING: " +
		 * duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); for (int k = 0; k < 1000000; k++) { num =
		 * arrint.get(k); bitSet4.set(k * 100); } end = Instant.now(); duration =
		 * Duration.between(start, end); System.out.println("ITERATE BITSET: " +
		 * duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); for (int k = 0; k < 10000; k++) {
		 * roaringBitmap.get(500000, 500000 + rand.nextInt(800000)); } end =
		 * Instant.now(); duration = Duration.between(start, end);
		 * System.out.println("GET ROARING: " + duration.toMillis() + " ms");
		 * 
		 * start = Instant.now(); for (int k = 0; k < 10000; k++) { bitSet4.get(500000,
		 * 500000 + rand.nextInt(800000)); } end = Instant.now(); duration =
		 * Duration.between(start, end); System.out.println("GET BITSET: " +
		 * duration.toMillis() + " ms"); BitSet bitSet5 = bitSet4.get(400000, 600000);
		 * bitSet4.or(bitSet5); System.out.println(bitSet4.cardinality());
		 * System.out.println(roaringBitmap.cardinality());
		 * 
		 * roaringBitmap.or(bitSet5); System.out.println(roaringBitmap.cardinality());
		 * 
		 * bitSet4.and(bitSet5); System.out.println(bitSet4.cardinality());
		 * roaringBitmap.and(bitSet5); System.out.println(roaringBitmap.cardinality());
		 * 
		 * for (int k = 0; k < 1000000; k++) { colror.get(k).getFirst(); }
		 * 
		 * Dictionary<String> dict = new Dictionary<>();
		 * System.out.println(dict.insert("george"));
		 * System.out.println(dict.insert("gkirion"));
		 * System.out.println(dict.insert("mpempis"));
		 * System.out.println(dict.insert("george")); System.out.println(dict.get(0));
		 * System.out.println(dict.get(2));
		 * 
		 * Chunk1<Integer> chunk1 = new Chunk1<Integer>(""), chunk2 = new
		 * Chunk1<Integer>(""); chunk1.add(44); chunk1.add(21); chunk1.add(80); chunk1 =
		 * new Chunk1<Integer>(new ColumnDelta()); ColumnAnalyzer<Integer> anal = new
		 * ColumnAnalyzer<>(); anal.add(5); anal.add(8); anal.add(4);
		 * System.out.println("max delta: " + anal.maxDelta());
		 * System.out.println("min delta: " + anal.minDelta()); ColumnDelta delta = new
		 * ColumnDelta(anal.maxDelta() - anal.minDelta() + 1 + 200, (anal.minDelta() -
		 * 99) * (-1)); ColumnPlain<Integer> plain = new ColumnPlain<>(); delta.add(5);
		 * delta.add(8); delta.add(4); delta.add(4);
		 * 
		 * plain.add(5); plain.add(8); plain.add(4); plain.add(4);
		 * 
		 * for (int i1 = 0; i1 < 1000; i1++) { num = rand.nextInt(100); delta.add(num);
		 * plain.add(num); }
		 * 
		 * for (int i1 = 0; i1 < delta.length(); i1++) {
		 * 
		 * if (!delta.get(i1).equals(plain.get(i1))) { System.out.println("NOT OK: " +
		 * delta.get(i1) + " " + plain.get(i1)); } }
		 * 
		 * ColumnType ct = ColumnType.ROARING;
		 * 
		 * System.out.println(ct); ColumnFactory<Integer> cf = new ColumnFactory<>();
		 * ColumnAnalyzer<Integer> ca = new ColumnAnalyzer<>(); ca.add(5);
		 * Column<Integer> col2 = cf.createColumn(ca, ColumnType.ROARING);
		 * System.out.println(col2); ColumnPlain<Integer> coll = new ColumnPlain<>();
		 * Column cc = coll; coll.add(3); cc.add(4); System.out.println(cc.avg());
		 * System.out.println(cc.toString()); System.out.println(coll.toString());
		 * ArrayList<String> strList = new ArrayList<>(); strList.add("hello");
		 * strList.add("baby"); System.out.println(strList);
		 * System.out.println("bytes: " + (Runtime.getRuntime().totalMemory() -
		 * Runtime.getRuntime().freeMemory())); Column<Integer> dlta = new
		 * ColumnBitmapRoaring<>(); Random random = new Random(); for (i = 0; i <
		 * 1000000; i++) { dlta.add(random.nextInt(100)); } System.out.println("bytes: "
		 * + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
		 * 
		 * long st = System.currentTimeMillis(); double counter = 0; int cnt = 1; for (i
		 * = 0; i < 1000000; i += cnt) { counter += ((Number)
		 * dlta.get(i).getFirst()).intValue(); cnt = dlta.get(i).getSecond(); } long et
		 * = System.currentTimeMillis(); System.out.println("time: " + (et - st));
		 * System.out.println(counter);
		 * 
		 * st = System.currentTimeMillis(); counter = 0; for (Tuple2<Integer, Integer>
		 * val : dlta) { counter += val.getFirst(); } et = System.currentTimeMillis();
		 * System.out.println("time: " + (et - st)); System.out.println(counter);
		 */
/*		RoaringBitmap ror = new RoaringBitmap();
		BitSet b = new BitSet();
		b.set(0, 2);
		int count = 0;
		for (int i = 0; i < 65536; i++) {
			if (b.get(i)) {
				count++;
			}
		}
		System.out.println(count);
		for (int i = 0; i < 1131072; i++) {
			ror.set(i);
		}
		RoaringBitmap ror2 = new RoaringBitmap();
		for (int i = 1131072; i < 1131072 + 65536; i++) {
			ror2.set(i);
		}
		ror.or(ror2);
		System.out.println(ror.getContainers()[0].getKey());
		System.out.println(ror.getContainers()[1].getKey());
		System.out.println(ror.getContainers()[2].getKey());

		b.set(80000, 4322040);
		System.out.println(ror.cardinality());
		long st = System.currentTimeMillis();
		ror.or(b);
		
		 * RoaringBitmap ror3 = new RoaringBitmap(b); ror.or(ror3);
		 
		long et = System.currentTimeMillis();
		System.out.println("time: " + (et - st));
		System.out.println(ror.cardinality());

		ColumnFactory columnFactory = new ColumnFactory();
		String[] nameList = { "bob", "mary", "nick", "jill", "george", "fotini", "jack" };
		Column<String> names = columnFactory.createColumn(new ColumnAnalyzer<String>(), ColumnType.RLE);
		Column<Integer> ages = columnFactory.createColumn(new ColumnAnalyzer<Integer>(), ColumnType.RLE);
		ArrayList<String> tempNames = new ArrayList<>();
		ArrayList<Integer> tempAges = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 1000000; i++) {
			tempNames.add(nameList[random.nextInt(nameList.length)]);
			tempAges.add(random.nextInt(100));
		}
		tempNames.sort(null);
		tempAges.sort(null);
		for (int i = 0; i < 1000000; i++) {
			names.add(tempNames.get(i));
			ages.add(tempAges.get(i));
		}
		names.add("bob");
		ages.add(10);
		Chunk chunk = new Chunk();
		chunk.addColumn("names", names);
		chunk.addColumn("ages", ages);
		System.out.println(((ColumnRle<Integer>) ages).storageType());
		System.out.println(((Column<String>) chunk.getColumn("names")).selectLessThan("george").cardinality());
		System.out.println(((Column<Integer>) chunk.getColumn("ages")).selectLessThan(50).cardinality());
		System.out.println(chunk.getColumn("names").selectLessThan("george").cardinality());
		System.out.println(chunk.getColumn("ages").selectLessThan(50).cardinality());
		BitSet bitset = chunk.getColumn("names").selectLessThan("george");
		System.out.println(((Column<Integer>) chunk.getColumn("ages")).sum(bitset));
		System.out.println(chunk.getColumn("ages").sum(bitset));
		int counter = 0;
		HashMap<ArrayList<Comparable<?>>, Integer> map = new HashMap<>();
		for (Row row : chunk) {
			ArrayList<Comparable<?>> tuple = new ArrayList<>();
			tuple.add(row.get("names"));
			tuple.add(row.get("ages"));
			if (!map.containsKey(tuple)) {
				map.put(tuple, 1);
			} else {
				map.put(tuple, map.get(tuple) + 1);
			}
			// if (counter < 10) {
			System.out.println(row);
			// }
			counter++;
		}
		System.out.println(counter);
		System.out.println(map);
		Row row = new Row();
		Row row2 = new Row();
		System.out.println(row.equals(row2));
		row.add("age", 3);
		row.add("name", "george");
		row.setRunLength(2);
		row2.add("age", 3);
		row2.add("name", "george");
		row.setRunLength(3);
		System.out.println(row.equals(row2));
		Column<Integer> column = new ColumnBitmapRoaring<>();
		Column<Integer> column2 = new ColumnPlain<>();
		Random r = new Random();
		for (int i = 0; i < 10000; i++) {
			int n = r.nextInt();
			column.add(n);
			column2.add(n);
		}
		System.out.println("Column type: " + column.type());
		System.out.println("Column type: " + column2.type());
		System.out.println("SUM TEST: " + (column.sum().equals(column2.sum())));
		System.out.println(column.sum());
		System.out.println(column2.sum());
		boolean ok = true;
		for (int i = 0; i < 10000; i++) {
			if (!column.get(i).equals(column2.get(i))) {
				ok = false;
			}
		}
		System.out.println("ELEMENT TEST: " + ok);
		RoaringBitmap roaring = new RoaringBitmap();
		BitSet bitSet = new BitSet();
		for (int i = 0; i < 10000000; i++) {
			// int n = r.nextInt(10000000);
			roaring.set(i);
			bitSet.set(i);
		}
		System.out.println("Bitmap TEST: " + (roaring.convertToBitSet().equals(bitSet)));
		roaring.convertToBitSet().and(bitSet);
		System.out.println("Bitmap TEST: " + bitSet.cardinality());
		Row list1 = new Row();
		Row list2 = new Row();
		list1.add("id", 1);
		list1.add("serial", 5);
		list1.add("state", 2);
		list2.add("id", 2);
		list2.add("serial", 5);
		list2.add("state", 4);
		System.out.println(list1);
		ArrayList<Row> list3 = new ArrayList<>();
		list3.add(list1);
		list3.add(list2);
		System.out.println(list3);
		List<String> orderList = new ArrayList<>();
		orderList.add("state");
		orderList.add("id");
		orderList.add("serial");
		list3.sort(new RowComparator(orderList));
		Chunk c = new Chunk();
		c.addColumn("id", new ColumnRle<>());
		c.addColumn("serial", new ColumnPlain<>());
		c.addColumn("state", new ColumnBitmapRoaring<>());
		for (Row row3 : list3) {
			c.add(row3);
		}
		for (Row row3 : c) {
			System.out.println("chunk: " + row3);
		}
		System.out.println(c.getColumn("serial").sum());
		System.out.println(c.getColumn("state").selectMoreThan(2).cardinality());
		System.out.println(list3);
		RowAnalyzer rowAnalyzer = new RowAnalyzer(list3);
		System.out.println(rowAnalyzer.analyze());
		list3.sort(new RowComparator(rowAnalyzer.getOrderList(rowAnalyzer.analyze())));
		System.out.println(list3);
		*/
		// load data
/*		Comparable date = new Date();
		System.out.println(date instanceof Integer);
		FileReader file = new FileReader(
				"D:\\george backup\\Downloads\\nyc-parking-tickets\\Parking_Violations_Issued_-_Fiscal_Year_2017.csv");
		BufferedReader bufferedReader = new BufferedReader(file);

		// get headers
		// spark rdd.take(1)
		String headers = bufferedReader.readLine();
		String[] hnames = headers.split(",");
		System.out.println(headers);
		System.out.println(hnames[2]);
		System.out.println(hnames[3]);
		System.out.println(hnames[1]);
		System.out.println(hnames[4]);
		System.out.println(hnames[6]);
		System.out.println(hnames[7]);
		System.out.println(hnames[19]);
		SimpleDateFormat df = new SimpleDateFormat("mm/dd/yyyy");
		System.out.println("loading data into rows");
		long start = System.currentTimeMillis();
		int index = 0;
		int lines = 0;
		List<Row> ticketList = new ArrayList<>();
		List<Chunk> chunkList = new ArrayList<>();
		String line = bufferedReader.readLine();
		while (line != null) {
			String[] tokens = line.split(",");
			Row ticket = new Row();
			try {
				ticket.add(hnames[2], tokens[2]);
				ticket.add(hnames[3], tokens[3]);
				ticket.add(hnames[1], tokens[1]);
				ticket.add(hnames[4], df.parse(tokens[4]));
				ticket.add(hnames[6], tokens[6]);
				ticket.add(hnames[7], tokens[7]);
				ticket.add(hnames[19], tokens[19]);
				ticketList.add(ticket);
			} catch (Exception e) {
				System.out.println(index);
			}
			if (lines > 1000000)
				break;
			lines++;
			line = bufferedReader.readLine();
		}
		System.out.println("inserted " + lines + " rows");
		bufferedReader.close();

		// analyze list by cardinality
		RowAnalyzer analyzer = new RowAnalyzer(ticketList);
		// sort list by cardinality
		ticketList.sort(new RowComparator(analyzer.getOrderList(analyzer.analyze())));
		Chunk ticketChunk = new Chunk();
		HashMap<String, ColumnType> colsBestEncodings = Model.findBestEncoding(ticketList);
		for (String name : colsBestEncodings.keySet()) {
			ticketChunk.addColumn(name, ColumnFactory.createColumn(colsBestEncodings.get(name)));
		}*/
		
/*		ticketChunk.addColumn("Registration State", ColumnFactory.createColumn(colsBestEncodings.get(0)));
		ticketChunk.addColumn("Plate Type", ColumnFactory.createColumn(colsBestEncodings.get(1)));
		ticketChunk.addColumn("Plate ID", ColumnFactory.createColumn(colsBestEncodings.get(2)));
		ticketChunk.addColumn("Issue Date", ColumnFactory.createColumn(colsBestEncodings.get(3)));
		ticketChunk.addColumn("Vehicle Body Type", ColumnFactory.createColumn(colsBestEncodings.get(4)));
		ticketChunk.addColumn("Vehicle Make", ColumnFactory.createColumn(colsBestEncodings.get(5)));
		ticketChunk.addColumn("Violation Time", ColumnFactory.createColumn(colsBestEncodings.get(6)));*/
		// load data into chunk
/*		Random r = new Random();
		ticketChunk.addColumn("test", ColumnFactory.createColumn(ColumnType.ROARING));
		for (Row row : ticketList) {
			ticketChunk.add(row);
			ticketChunk.getColumn("test").add(r.nextInt(10000));
		}*/
/*		for (RowArray t : ticketList) {
			ticketChunk.getColumn("Registration State").add(t.get(0));
			ticketChunk.getColumn("Plate Type").add(t.get(1));
			ticketChunk.getColumn("Plate ID").add(t.get(2));
			ticketChunk.getColumn("Issue Date").add(t.get(3));
			ticketChunk.getColumn("Vehicle Body Type").add(t.get(4));
			ticketChunk.getColumn("Vehicle Make").add(t.get(5));
			ticketChunk.getColumn("Violation Time").add(t.get(6));
		}*/
/*		long end = System.currentTimeMillis();
		System.out.println("processing time: " + (end - start) / 1000.0 + " s");
		//ticketList.clear();
		System.out.println("type: " + ticketChunk.getColumn("Registration State").type() + " size: " + ticketChunk.getColumn("Registration State").sizeEstimation());
		System.out.println("type: " + ticketChunk.getColumn("Plate Type").type() + " size: " + ticketChunk.getColumn("Plate Type").sizeEstimation());
		System.out.println("type: " + ticketChunk.getColumn("Plate ID").type() + " size: " + ticketChunk.getColumn("Plate ID").sizeEstimation());
		System.out.println("type: " + ticketChunk.getColumn("Issue Date").type() + " size: " + ticketChunk.getColumn("Issue Date").sizeEstimation());
		System.out.println("type: " + ticketChunk.getColumn("Vehicle Body Type").type() + " size: " + ticketChunk.getColumn("Vehicle Body Type").sizeEstimation());
		System.out.println("type: " + ticketChunk.getColumn("Vehicle Make").type() + " size: " + ticketChunk.getColumn("Vehicle Make").sizeEstimation());
		System.out.println("type: " + ticketChunk.getColumn("Violation Time").type() + " size: " + ticketChunk.getColumn("Violation Time").sizeEstimation());
		start = System.currentTimeMillis();
		ticketChunk.getColumn("test").sum();

		System.out.println("tickets issued on 27/03/2017: " + ticketChunk.getColumn("Issue Date").selectEquals(df.parse("27/03/2017")).cardinality());
		System.out.println("tickets issued with car make Toyota: " + ticketChunk.getColumn("Vehicle Make").selectEquals("TOYOT").cardinality());
		System.out.println("tickets issued with car make VW: " + ticketChunk.getColumn("Vehicle Make").selectEquals("VW").cardinality());
		System.out.println("tickets issued with car type SUBN: " + ticketChunk.getColumn("Vehicle Body Type").selectEquals("SUBN").cardinality());
		System.out.println("tickets issued with car make Toyota on 27/03/2017: " + ticketChunk.getColumn("Vehicle Make").selectEquals("TOYOT").and(ticketChunk.getColumn("Issue Date").selectEquals(df.parse("27/03/2017"))).cardinality());
		System.out.println("tickets issued with car make VW on 27/03/2017: " + ticketChunk.getColumn("Vehicle Make").selectEquals("VW").and(ticketChunk.getColumn("Issue Date").selectEquals(df.parse("27/03/2017"))).cardinality());
		end = System.currentTimeMillis();
		System.out.println("processing time: " + (end - start) / 1000.0 + " s");
		int i = 0;
		start = System.currentTimeMillis();
		Chunk smallChunk = new Chunk();
		smallChunk.addColumn("Registration State", ticketChunk.getColumn("Registration State"));
		smallChunk.addColumn("Plate Type", ticketChunk.getColumn("Plate Type"));
		smallChunk.addColumn("Plate ID", ticketChunk.getColumn("Plate ID"));
		smallChunk.addColumn("Vehicle Make", ticketChunk.getColumn("Vehicle Make"));
		for (Row row : smallChunk) {
			i++;
		}
		end = System.currentTimeMillis();
		System.out.println(i);
		System.out.println("processing time: " + (end - start) / 1000.0 + " s");
		System.out.println(ticketChunk.getColumn("Vehicle Make").cardinality());
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING && columnType != ColumnType.PLAIN) {
				continue;
			}
			
			Column<Comparable> column = ColumnFactory.createColumn(columnType);
			for (Row row  : ticketList) {
				column.add(row.get("test"));
			}
			System.out.println(ticketList.size());
			System.out.println(column.length());
			System.out.println("column type: " + columnType);
			start = System.currentTimeMillis();
			column.sum();
			end = System.currentTimeMillis();
			System.out.println("aggregate test: " + (end - start) + " ms");
			start = System.currentTimeMillis();
			column.selectEquals("BMW");
			end = System.currentTimeMillis();
			System.out.println("select test: " + (end - start) + " ms");
			start = System.currentTimeMillis();
			for (Tuple2<Comparable, Integer> t : column) {
				
			}
			end = System.currentTimeMillis();
			System.out.println("iterate test: " + (end - start) + " ms");
		}*/
/*
		long end = System.currentTimeMillis();
		System.out.println("time: " + (end - start) + " ms");
		// System.out.println(ticketList);

		System.out.println("analyzing data [getting cardinality]");
		// System.out.println(analyzer.analyze());

		System.out.println("sorting data by cardinality");

		System.out.println("loading data into chunk");

		System.out.println("getting tickets issued on 07/10/2016 [uncompressed]");
		start = System.currentTimeMillis();
		long count2 = 0;
		for (Row t : ticketList) {
			if (t.get("Issue Date").equals((df.parse("07/10/2016")))) {
				count2++;
			}
		}
		System.out.println("tickets issued on 07/10/2016 [uncompressed]: " + count2);
		end = System.currentTimeMillis();
		System.out.println("tickets issued on 07/10/2016 [uncompressed] time: " + (end - start) + " ms");

		System.out.println("getting tickets issued on 07/10/2016 [compressed]");
		start = System.currentTimeMillis();
		// count2 = ticketChunk.getColumn("Issue
		// Date").selectEquals(df.parse("07/10/2016")).cardinality();
		count2 = chunkList.stream().mapToLong(e -> {
			try {
				return e.getColumn("Issue Date").selectEquals(df.parse("07/10/2016")).cardinality();
			} catch (ParseException e2) {
				return 0;
			}
		}).sum();
		System.out.println("tickets issued on 07/10/2016 [compressed]: " + count2);
		end = System.currentTimeMillis();
		System.out.println("tickets issued on 07/10/2016 [compressed] time: " + (end - start) + " ms");

		System.out.println("count tickets issued between 01/01/2016 and 01/01/2017 group by state [uncompressed]");
		start = System.currentTimeMillis();
		map = new HashMap<>();
		for (Row t : ticketList) {
			if (t.get("Issue Date").compareTo(df.parse("01/01/2016")) >= 0
					&& t.get("Issue Date").compareTo(df.parse("01/01/2017")) <= 0) {
				ArrayList<Comparable<?>> tuple = new ArrayList<>();
				tuple.add(t.get("Registration State"));
				if (!map.containsKey(tuple)) {
					map.put(tuple, 1);
				} else {
					map.put(tuple, map.get(tuple) + 1);
				}
			}
		}
		System.out.println("tickets issued between 01/01/2016 and 01/01/2017 group by state [uncompressed]: " + map);
		end = System.currentTimeMillis();
		System.out.println("tickets issued between 01/01/2016 and 01/01/2017 group by state [uncompressed] time: "
				+ (end - start) + " ms");

		System.out.println("count tickets issued between 01/01/2016 and 01/01/2017 group by state [compressed]");
		start = System.currentTimeMillis();

		final HashMap<ArrayList<Comparable<?>>, Integer> statesMap = new HashMap<>();
		chunkList.stream().forEach(e -> {
			Chunk tempChunk = new Chunk();
			tempChunk.addColumn("Registration State", e.getColumn("Registration State"));
			try {
				final BitSet res = e.getColumn("Issue Date").selectBetween(df.parse("01/01/2016"),
						df.parse("01/01/2017"));
				tempChunk.forEach(t -> {
					ArrayList<Comparable<?>> tuple = new ArrayList<>();
					tuple.add(t.get("Registration State"));
					if (!statesMap.containsKey(tuple)) {
						statesMap.put(tuple, res.get(t.getIndex(), t.getIndex() + t.getRunLength()).cardinality());
					} else {
						statesMap.put(tuple, statesMap.get(tuple)
								+ res.get(t.getIndex(), t.getIndex() + t.getRunLength()).cardinality());
					}
				});
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		System.out
				.println("tickets issued between 01/01/2016 and 01/01/2017 group by state [compressed]: " + statesMap);
		end = System.currentTimeMillis();
		System.out.println("tickets issued between 01/01/2016 and 01/01/2017 group by state [compressed] time: "
				+ (end - start) + " ms");

		System.out.println("07/10/2016".compareTo("06/14/2017"));
		System.out.println(df.parse("07/10/2016").compareTo(df.parse("06/14/2017")));

		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(1);
		list.add(2);
		list.add(2);
		list.add(2);
		list.add(2);
		list.add(3);
		list.add(5);
		Integer maxDelta = null;
		Integer minDelta = null;
		Integer previous = null;
		for (Integer item : list) {
			if (previous == null) {
				previous = item;
			}
			if (minDelta == null || item - previous < minDelta) {
				minDelta = item - previous;
			}
			if (maxDelta == null || item - previous > maxDelta) {
				maxDelta = item - previous;
			}
			previous = item;
		}
		System.out.println("min delta: " + minDelta);
		System.out.println("max delta: " + maxDelta);
		System.out.println("range: " + (maxDelta - minDelta + 1));
		System.out.println("offset: " + minDelta * (-1));*/

		// System.out.println(ticketList);
		

		/*
		for (int i = 0; i < 10000010; i++) {
			intList.add(r.nextInt(10));
		}
		intList.sort(null);
		ColumnPlain<Integer> plain = new ColumnPlain<>();
		for (Integer element : intList) {
			plain.add(element);
		}
		ColumnBitmapRoaring<Integer> delta = new ColumnBitmapRoaring<>();
		for (Integer element : intList) {
			delta.add(element);
		}
		System.out.println("agg test 1: " + (plain.sum().equals(delta.sum())));
		System.out.println("agg test 2: " + (plain.sum(538, 15674).equals(delta.sum(538, 15674))));
		System.out.println("select test 1: " + (plain.selectLessThan(500).equals(delta.selectLessThan(500))));
		System.out.println("select test 2: " + (plain.selectBetween(500, 700).equals(delta.selectBetween(500, 700))));
		System.out.println("agg test 3: " + (plain.sum(plain.selectMoreThan(500)).equals(delta.sum(delta.selectMoreThan(500)))));
		Iterator<Tuple2<Integer, Integer>> plainIterator = plain.iterator();
		Iterator<Tuple2<Integer, Integer>> deltaIterator = delta.iterator();
		while (plainIterator.hasNext() && deltaIterator.hasNext()) {
			if (!plainIterator.next().equals(deltaIterator.next())) {
				System.out.println("mismatch");
				System.out.println(plainIterator.next() + " " + deltaIterator.next());

			}
		}
		System.out.println(plain.sizeEstimation());
		System.out.println(delta.sizeEstimation());
		*/
/*		String[] names = {"jo", "jack", "george", "giagkos", "nick", "fotini", "jill", "jane", "maria", "niki" ,"platon"};
		ArrayList<Row> rlist = new ArrayList<>();
		for (i = 0; i < 1000000; i++) {
			Row row = new Row();
			row.add("name", names[r.nextInt(names.length)]);
			row.add("age", r.nextInt(100));
			row.add("height", r.nextInt(220));
			rlist.add(row);
		}
		RowAnalyzer rowAnalyzer = new RowAnalyzer(rlist);
		rlist.sort(new RowComparator(rowAnalyzer.getOrderList(rowAnalyzer.analyze())));
		Chunk nc = new Chunk();
		HashMap<String, ColumnType> types = Model.findBestEncoding(rlist);
		for (String name : types.keySet()) {
			nc.addColumn(name, ColumnFactory.createColumn(types.get(name)));
		}
		for (Row row : rlist) {
			nc.add(row);
		}
		Double ages = 0.0;
		Double heights = 0.0;
		for (Row row : nc) {
			ages += (Integer)row.get("age");
			heights += (Integer)row.get("height");
		}
		System.out.println("age sum test: " + ages.equals(nc.getColumn("age").sum()));
		System.out.println("height sum test: " + heights.equals(nc.getColumn("height").sum()));*/

		
/*		Integer[] cardinalities = {50, 100, 500, 1800};
		
		for (Integer cardinality : cardinalities) {
			Integer[] repetitions = {1, 50, 100, 1000};
			for (Integer repetition : repetitions) {
				List<Row> rowsFull = new ArrayList<>();
				for (int rep = 1; rep <= repetition; rep++) {
					List<Row> rows = new ArrayList<>();
					for (int i = 0; i < 1000000 / repetition; i++) {
						Row row = new Row();
						//row.add("names", r.nextInt(names.length));
						//row.add("age", r.nextInt(509));
						row.add("id", r.nextInt(cardinality));
						rows.add(row);
					}
					RowAnalyzer rowAnalyzer = new RowAnalyzer(rows);
					rows.sort(new RowComparator(rowAnalyzer.getOrderList(rowAnalyzer.analyze())));
					for (Row row : rows) {
						rowsFull.add(row);
					}
				}
				PrintWriter printWriter = new PrintWriter("C:\\Users\\george\\bestEncodings\\bestEncoding_" + cardinality + "_" + repetition + ".txt");
				BufferedWriter bufferedWriter = new BufferedWriter(printWriter);
				List<List<Row>> packs = Model.splitIntoPacks(rowsFull, 1000);
				for (List<Row> pack : packs) {
					HashMap<String, Column<Comparable>> columns = Model.splitIntoColumns(pack);
					HashMap<String, ColumnType> bestEncoding = Model.findBestEncoding(pack);
					System.out.println(bestEncoding);
					for (String column : columns.keySet()) {
						bufferedWriter.write(columns.get(column) + " " + bestEncoding.get(column) + "\n");
					}
				}
				bufferedWriter.close();
			}
		}
		List<Integer> test = new ArrayList<>();
		List<Row> rowsFull = new ArrayList<>();
		for (int rep = 1; rep <= 1; rep++) {
			List<Row> rows = new ArrayList<>();
			for (int i = 0; i < 1000000; i++) {
				Row row = new Row();
				row.add("id", r.nextInt(1806));
				row.add("names", r.nextInt(names.length));
				row.add("age", r.nextInt(100));
				row.add("t", r.nextInt(806));

				rows.add(row);
				test.add(r.nextInt(10000));
			}
			RowAnalyzer rowAnalyzer = new RowAnalyzer(rows);
			List<String> orderList = rowAnalyzer.getOrderList(rowAnalyzer.analyze());
			System.out.println(rowAnalyzer.getOrderList(rowAnalyzer.analyze()));
			long start = System.currentTimeMillis();
			rows.sort(new RowComparator(orderList));
			long end = System.currentTimeMillis();
			System.out.println("sort time: " + (end - start));
			start = System.currentTimeMillis();
			test.sort(null);
			end = System.currentTimeMillis();
			System.out.println("test time: " + (end - start));
			for (Row row : rows) {
				rowsFull.add(row);
			}
		}

		/*
		Model model = new Model();
		//model.loadModel("column-model-full7.hdf5");
		int i = 0, correct = 0;
		List<List<Row>> packs = Model.splitIntoPacks(rowsFull, 1000);
		long start = System.currentTimeMillis();

		//List<ColumnType> predictions = model.predict(packs);
		
		long end = System.currentTimeMillis();
		System.out.println("total time: " + (end - start));
		
		HashMap<String, Integer> rleMap = new HashMap<>();
		HashMap<String, Integer> deltaMap = new HashMap<>();
		HashMap<String, Integer> roaringMap = new HashMap<>();
		
		rleMap.put("names", 0);
		rleMap.put("age", 0);
		rleMap.put("id", 0);
		rleMap.put("t", 0);

		deltaMap.put("names", 0);
		deltaMap.put("age", 0);
		deltaMap.put("id", 0);
		deltaMap.put("t", 0);

		roaringMap.put("names", 0);
		roaringMap.put("age", 0);
		roaringMap.put("id", 0);
		roaringMap.put("t", 0);
		long totalSize = 0;
		for (List<Row> pack : packs) {

			for (Column<Comparable> col : Model.splitIntoColumns(pack).values()) {
				//long start = System.currentTimeMillis();
				//ColumnType prediction = model.predict(col);
				//long end = System.currentTimeMillis();
				//System.out.println("predicition time: " + (end - start));
				
				//start = System.currentTimeMillis();
				ColumnType columnType = Model.findBestEncoding(col);
				Column<Comparable> bestColumn = ColumnFactory.createColumn(columnType);
				for (Tuple2<Comparable, Integer> t : col) {
					bestColumn.add(t.getFirst());
				}
				totalSize += bestColumn.sizeEstimation();
				if (columnType == ColumnType.RLE) {
					System.out.println(col.getName());
					System.out.println(rleMap.get(col.getName()));
					rleMap.put(col.getName(), rleMap.get(col.getName()) + 1);
				}
				else if (columnType == ColumnType.DELTA) {
					deltaMap.put(col.getName(), deltaMap.get(col.getName()) + 1);
				}
				else if (columnType == ColumnType.ROARING) {
					roaringMap.put(col.getName(), roaringMap.get(col.getName()) + 1);
				}
				
				//end = System.currentTimeMillis();
				//System.out.println("best encoding time: " + (end - start));

				System.out.println("prediction: " + predictions.get(i) + ", actual: " + columnType);
				if (predictions.get(i) == columnType) {
					correct++;
				}
				i++;
			}
		}
		System.out.println(rleMap);
		System.out.println(deltaMap);
		System.out.println(roaringMap);
		System.out.println("total size: " + totalSize);

		System.out.println("accuracy:" + (correct / (double)i));
		HashMap<String, String> map = new HashMap<>();
		map.put("age", "18");
		map.put("name", "george");
		System.out.println(map.keySet());
		for (String key : map.keySet()) {
			System.out.println(key);
		} */
		
		
		Random r = new Random();
		ArrayList<Row> rows = new ArrayList<>();
		for (int i = 0; i < 1000000; i++) {
			Row row = new Row();
			row.add("1", r.nextInt(100));
			row.add("2", r.nextInt(1000));
			row.add("3", r.nextInt(3000));
			rows.add(row);
		}
		
		rows.sort(new RowComparator(RowAnalyzer.analyze(rows)));
		
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType != ColumnType.RLE && columnType != ColumnType.RLE_DICTIONARY && columnType != ColumnType.DELTA && columnType != ColumnType.ROARING && columnType != ColumnType.PLAIN && columnType != ColumnType.BIT_PACKING && columnType != ColumnType.BIT_PACKING_DICTIONARY) {
				continue;
			}
			Column<Comparable> col1 = ColumnFactory.createColumn(columnType);
			Column<Comparable> col2 = ColumnFactory.createColumn(columnType);
			Column<Comparable> col3 = ColumnFactory.createColumn(columnType);
			Chunk chunk = new Chunk();
			chunk.addColumn("1", col1);
			chunk.addColumn("2", col2);
			chunk.addColumn("3", col3);
			for (Row row : rows) {
				chunk.add(row);
			}
			
			Double sum1 = 0.0, sum2 = 0.0, sum3 = 0.0;
			for (Row row : rows) {
				sum1 += (Integer)row.get("1");
				sum2 += (Integer)row.get("2");
				sum3 += (Integer)row.get("3");
			}
			long start, end;
			System.out.println("column type: " + columnType);
			start = System.currentTimeMillis();
			System.out.println("col 1 sum test(pass): " + col1.sum().equals(sum1));
			end = System.currentTimeMillis();
			System.out.println("col 1 sum time: " + (end - start) / 1000.0 + " s");
			start = System.currentTimeMillis();
			System.out.println("col 2 sum test(pass): " + col2.sum().equals(sum2));
			end = System.currentTimeMillis();
			System.out.println("col 2 sum time: " + (end - start) / 1000.0 + " s");
			start = System.currentTimeMillis();
			System.out.println("col 3 sum test(pass): " + col3.sum().equals(sum3));
			end = System.currentTimeMillis();
			System.out.println("col 3 sum time: " + (end - start) / 1000.0 + " s");
			
			BitSet bit1 = new BitSet(), bit2 = new BitSet(), bit3 = new BitSet();
			int i = 0;
			for (Row row : rows) {
				if (row.get("1").compareTo(50) <= 0) {
					bit1.set(i);
				}
				if (row.get("2").compareTo(50) <= 0) {
					bit2.set(i);
				}				
				if (row.get("3").compareTo(50) <= 0) {
					bit3.set(i);
				}
				i++;
			}
			start = System.currentTimeMillis();
			System.out.println("col 1 select test(pass): " + col1.selectLessThanOrEquals(50).equals(bit1));
			end = System.currentTimeMillis();
			System.out.println("col 1 select time: " + (end - start) / 1000.0 + " s");
			start = System.currentTimeMillis();
			System.out.println("col 2 select test(pass): " + col2.selectLessThanOrEquals(50).equals(bit2));
			end = System.currentTimeMillis();
			System.out.println("col 2 select time: " + (end - start) / 1000.0 + " s");
			start = System.currentTimeMillis();
			System.out.println("col 3 select test(pass): " + col3.selectLessThanOrEquals(50).equals(bit3));
			end = System.currentTimeMillis();
			System.out.println("col 3 select time: " + (end - start) / 1000.0 + " s");
			
			sum1 = 0.0; sum2 = 0.0; sum3 = 0.0;
			i = 0;
			for (Row row : rows) {
				if (bit1.get(i)) {
					sum1 += (Integer)row.get("1");
				}
				if (bit2.get(i)) {
					sum2 += (Integer)row.get("2");
				}
				if (bit3.get(i)) {
					sum3 += (Integer)row.get("3");
				}
				i++;
			}
			start = System.currentTimeMillis();
			System.out.println("col 1 select and sum test(pass): " + col1.sum(col1.selectLessThanOrEquals(50)).equals(sum1));
			end = System.currentTimeMillis();
			System.out.println("col 1 select and sum time: " + (end - start) / 1000.0 + " s");
			start = System.currentTimeMillis();
			System.out.println("col 2 select and sum test(pass): " + col2.sum(col2.selectLessThanOrEquals(50)).equals(sum2));
			end = System.currentTimeMillis();
			System.out.println("col 2 select and sum time: " + (end - start) / 1000.0 + " s");
			start = System.currentTimeMillis();
			System.out.println("col 3 select and sum test(pass): " + col3.sum(col3.selectLessThanOrEquals(50)).equals(sum3));
			end = System.currentTimeMillis();
			System.out.println("col 3 select and sum time: " + (end - start) / 1000.0 + " s");
			
			i = 0;
			boolean equal = true;
			start = System.currentTimeMillis();
			for (Row row : chunk) {
				if (!row.get("1").equals(rows.get(i).get("1")) || !row.get("2").equals(rows.get(i).get("2")) || !row.get("3").equals(rows.get(i).get("3"))) {
					equal = false;
				}
				i += row.getRunLength();
			}
			System.out.println("iterate test(pass): " + equal); 
			end = System.currentTimeMillis();
			System.out.println("iterate time: " + (end - start) / 1000.0 + " s");//love
		}
		
	}

	public static class RowComparator implements Comparator<Row> {

		private List<String> orderList;

		public RowComparator() {

		}

		public RowComparator(List<String> orderList) {
			this.orderList = orderList;
		}

		@Override
		@SuppressWarnings("unchecked")
		public int compare(Row o1, Row o2) {
			for (String key : orderList) {
				if (o1.get(key).compareTo(o2.get(key)) < 0) {
					return -1;
				} else if (o1.get(key).compareTo(o2.get(key)) > 0) {
					return 1;
				}
			}
			return 0;
		}

	}
	
	public static class RowArrayComparator implements Comparator<RowArray> {

		private List<Integer> orderList;

		public RowArrayComparator() {

		}

		public RowArrayComparator(List<Integer> orderList) {
			this.orderList = orderList;
		}

		@Override
		@SuppressWarnings("unchecked")
		public int compare(RowArray o1, RowArray o2) {
			for (Integer index : orderList) {
				if (o1.get(index).compareTo(o2.get(index)) < 0) {
					return -1;
				} else if (o1.get(index).compareTo(o2.get(index)) > 0) {
					return 1;
				}
			}
			return 0;
		}

	}

	public static class RowAnalyzer {


		public static List<String> analyze(List<Row> rows) {
			HashMap<String, HashMap<Comparable<?>, Boolean>> uniqueElements = new HashMap<>();
			for (Row row : rows) {
				for (String key : row) {
					if (uniqueElements.get(key) == null) {
						uniqueElements.put(key, new HashMap<>());
					}
					uniqueElements.get(key).put(row.get(key), true);
				}
			}
			ArrayList<Tuple2<String, Integer>> cardinalities = new ArrayList<>();
			for (String key : uniqueElements.keySet()) {
				cardinalities.add(new Tuple2<String, Integer>(key, uniqueElements.get(key).size()));
			}
			cardinalities.sort(new Comparator<Tuple2<String, Integer>>() {

				@Override
				public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {
					return o1.getSecond().compareTo(o2.getSecond());
				}

			});
			List<String> orderList = new ArrayList<>();
			for (Tuple2<String, Integer> tuple : cardinalities) {
				orderList.add(tuple.getFirst());
			}
			return orderList;

		}
		
		public List<String> getOrderList(ArrayList<Tuple2<String, Integer>> cardinalities) {
			List<String> orderList = new ArrayList<>();
			for (Tuple2<String, Integer> tuple : cardinalities) {
				orderList.add(tuple.getFirst());
			}
			return orderList;
		}
	}
		
		public static class RowArrayAnalyzer {

			private List<RowArray> rows;

			public RowArrayAnalyzer(List<RowArray> rows) {
				this.rows = rows;
			}

			public ArrayList<Tuple2<Integer, Integer>> analyze() {
				HashMap<Integer, HashMap<Comparable<?>, Boolean>> uniqueElements = new HashMap<>();
				for (RowArray row : rows) {
					int i = 0;
					for (Comparable item : row) {
						if (uniqueElements.get(i) == null) {
							uniqueElements.put(i, new HashMap<>());
						}
						uniqueElements.get(i).put(item, true);
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
				return cardinalities;

			}
			
			public List<Integer> getOrderArrayList(ArrayList<Tuple2<Integer, Integer>> cardinalities) {
				List<Integer> orderList = new ArrayList<>();
				for (Tuple2<Integer, Integer> tuple : cardinalities) {
					orderList.add(tuple.getFirst());
				}
				return orderList;
			}
		}

}
