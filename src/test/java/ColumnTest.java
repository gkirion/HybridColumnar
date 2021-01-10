import org.george.hybridcolumnar.column.*;
import org.george.hybridcolumnar.column.analyzer.ColumnAnalyzer;
import org.george.hybridcolumnar.util.ColumnEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColumnTest {

    @Test
    public void dictSameAsNoDict() {
        Column<String> noDict = new ColumnRle<>();
        Column<String> dict = new ColumnDictionaryRle<>();
        noDict.add("hello");
        noDict.add("hello");
        noDict.add("george");
        noDict.add("fotini");
        noDict.add("kallithea");
        noDict.add("kali");

        dict.add("hello");
        dict.add("hello");
        dict.add("george");
        dict.add("fotini");
        dict.add("kallithea");
        dict.add("kali");

        System.out.println("test run");

        Assertions.assertEquals(noDict.length(), dict.length(), "length must be equal!");
        for (int i = 0; i < noDict.length(); i++) {
            Assertions.assertEquals(noDict.get(i), dict.get(i), "columns data must be equal!");
        }
        Class c  = "hello".getClass();
        Class h  = Integer.valueOf(4).getClass();
        System.out.println(c);
        System.out.println(h);
        System.out.println(ColumnAnalyzer.avgItemSize(Arrays.asList(7)));
//        String[] names = {"george", "fotini", "kallithea", "jack"};
        int[] numbers = {6, 8, 2, 10};

        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 1000000; i ++) {
            list.add(numbers[r.nextInt(4)]);
        }
        Assertions.assertEquals(ColumnType.DELTA_DICTIONARY, ColumnEncoder.encode(list).type());

    }

}
