package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public class ColumnPlainDictSizeEstimator implements ColumnSizeEstimator {

    @Override
    public long estimate(ColumnAnalysisResult columnAnalysisResult) {
        return columnAnalysisResult.getSize() * 16 + columnAnalysisResult.getCardinality() * columnAnalysisResult.getAvgItemSize() * 4;
    }

}
