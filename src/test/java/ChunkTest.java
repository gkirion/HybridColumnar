import org.george.hybridcolumnar.chunk.Chunk;
import org.george.hybridcolumnar.chunk.ChunkBuilder;
import org.george.hybridcolumnar.column.Column;
import org.george.hybridcolumnar.column.ColumnType;
import org.george.hybridcolumnar.domain.Row;
import org.george.hybridcolumnar.domain.RowArray;
import org.george.hybridcolumnar.util.ColumnEncoder;
import org.george.hybridcolumnar.util.RowArrayAnalyzer;
import org.george.hybridcolumnar.util.RowArrayComparator;
import org.george.hybridcolumnar.util.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ChunkTest {

    @Test
    public void chunkSuccessfullyLoadedTest() {
        int[] numbers = {6, 622, 8, 238429, 100, 64354,4232,78, 9237498, 1000000, 5, 34, 834023, 1,2,3,4,5,6,7,8,9};
        Table<Integer> table = new Table<>();
        Random r = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i ++) {
            table.add(numbers[r.nextInt(numbers.length)], numbers[r.nextInt(numbers.length)], numbers[r.nextInt(numbers.length)]);
        }
        table.sort();
        List<Integer> l1 = new ArrayList<>(), l2 = new ArrayList<>(), l3 = new ArrayList<>();
        for (Integer[] row : table) {
            l1.add(row[0]);
            l2.add(row[1]);
            l3.add(row[2]);
        }
        Chunk chunk = ChunkBuilder.chunk("3 columns").column("0", ColumnEncoder.encode(l1)).column("1", ColumnEncoder.encode(l2)).column("2", ColumnEncoder.encode(l3)).build();
        long end = System.currentTimeMillis();
        System.out.println("total time: " + (end - start));

        start = System.currentTimeMillis();
        List<RowArray> rows = new ArrayList<>();
        for (int i = 0; i < 1000000; i ++) {
            RowArray row = new RowArray();
            row.add(l1.get(i));
            row.setRunLength(1);
            row.add(l2.get(i));
            row.add(l3.get(i));
            rows.add(row);
        }
        List<Integer> indexes = RowArrayAnalyzer.getColumnsOrderedByCardinality(rows);
        RowArrayComparator rowComparator = new RowArrayComparator(indexes);
        rows.sort(rowComparator);
        HashMap<Integer, ColumnType> encodings = ColumnEncoder.findBestEncodingArray(rows);
        HashMap<Integer, Column<Comparable>> columns = ColumnEncoder.splitIntoColumns(rows);
        Chunk c = new Chunk();
        for (Integer index : encodings.keySet()) {
            Column column = ColumnEncoder.compressColumn(columns.get(index), encodings.get(index));
            c.addColumn(index + "", column);
        }
        end = System.currentTimeMillis();
        System.out.println("total time: " + (end - start));

        int i = 0;
        Assertions.assertNotEquals(c, null);
        Iterator<Row> it = c.iterator();
        for (Row row : chunk) {
            Row row2 = it.next();
            Assertions.assertEquals(row, row2, "rows must be the same");
            i += row.getRunLength();
        }
    }

}
