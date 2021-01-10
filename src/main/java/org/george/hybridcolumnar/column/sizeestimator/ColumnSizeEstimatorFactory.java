package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.ColumnType;

public class ColumnSizeEstimatorFactory {

    public static ColumnSizeEstimator createEstimator(ColumnType columnType) {
        switch (columnType) {
            case RLE:
                return new ColumnRleSizeEstimator();
            case RLE_DICTIONARY:
                return new ColumnRleDictSizeEstimator();
            case DELTA:
                return new ColumnDeltaSizeEstimator();
            case DELTA_DICTIONARY:
                return new ColumnDeltaDictSizeEstimator();
            case BIT_PACKING:
                return new ColumnBitPackingSizeEstimator();
            case BIT_PACKING_DICTIONARY:
                return new ColumnBitPackingDictSizeEstimator();
            case ROARING:
                return new ColumnRoaringSizeEstimator();
            case BITMAP:
                return new ColumnBitmapSizeEstimator();
            case PLAIN:
                return new ColumnPlainSizeEstimator();
            default:
                return new ColumnPlainDictSizeEstimator();
        }
    }

}
