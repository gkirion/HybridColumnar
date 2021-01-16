import org.george.hybridcolumnar.domain.RowArray;
import org.george.hybridcolumnar.util.RowArrayAnalyzer;
import org.george.hybridcolumnar.util.RowArrayComparator;
import org.george.hybridcolumnar.util.Table;
import org.george.hybridcolumnar.util.TableRowAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableTest {

    @Test
    public void tableIsSortedTest() {
        int[] numbers = {6, 622, 8, 238429, 100, 64354,4232,78, 9237498, 1000000, 5, 34, 834023, 1,2,3,4,5,6,7,8,9};

        long start = System.currentTimeMillis();
        Table<Integer> table = new Table<>();
        Random r = new Random();
        for (int i = 0; i < 1000000; i ++) {
            table.add(r.nextInt(numbers.length), r.nextInt(numbers.length), r.nextInt(numbers.length));
        }
        table.sort();
        long end = System.currentTimeMillis();
        System.out.println("total time: " + (end - start) + " ms");

        List<Integer> cardinalities = TableRowAnalyzer.getColumnsOrderedByCardinality(table.getRows());
        Integer[] previousRow = null;
        for (Integer[] row : table) {
            boolean lessThan = true;
            if (previousRow != null) {
                for (Integer index : cardinalities) {
                    if (previousRow[index].compareTo(row[index]) > 0) {
                        lessThan = false;
                        break;
                    } else if (previousRow[index].compareTo(row[index]) < 0) {
                        break;
                    }
                }
                Assertions.assertEquals(true, lessThan, "previous row must be less or equal to current row");
            }
            previousRow = row;
        }

        start = System.currentTimeMillis();
        List<RowArray> rows = new ArrayList<>();
        for (int i = 0; i < 1000000; i ++) {
            RowArray row = new RowArray();
            row.add(r.nextInt(numbers.length));
            row.add(r.nextInt(numbers.length));
            row.add(r.nextInt(numbers.length));
            rows.add(row);
        }
        List<Integer> indexes = RowArrayAnalyzer.getColumnsOrderedByCardinality(rows);
        RowArrayComparator rowComparator = new RowArrayComparator(indexes);
        rows.sort(rowComparator);
        end = System.currentTimeMillis();
        System.out.println("total time: " + (end - start) + " ms");

    }

}
