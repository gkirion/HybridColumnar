package org.george.chunk;

import java.util.BitSet;

public class ChunkDriver {

	public static void main(String[] args) {
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
		RoaringBitmap ror = new RoaringBitmap();
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
		System.out.println(ror.getContainers()[0].getId());
		System.out.println(ror.getContainers()[1].getId());
		System.out.println(ror.getContainers()[2].getId());

		b.set(80000, 4322040);
		System.out.println(ror.cardinality());
		long st = System.currentTimeMillis();
		ror.or(b);
		/*
		 * RoaringBitmap ror3 = new RoaringBitmap(b); ror.or(ror3);
		 */
		long et = System.currentTimeMillis();
		System.out.println("time: " + (et - st));
		System.out.println(ror.cardinality());

	}

}
