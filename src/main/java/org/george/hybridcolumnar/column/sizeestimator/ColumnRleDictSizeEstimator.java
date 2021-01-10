package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public class ColumnRleDictSizeEstimator implements ColumnSizeEstimator {

    @Override
    public long estimate(ColumnAnalysisResult columnAnalysisResult) {
        return columnAnalysisResult.getNumberOfRuns() * 16 + columnAnalysisResult.getCardinality() * columnAnalysisResult.getAvgItemSize() * 4;
    }

}
