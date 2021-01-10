package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public class ColumnDeltaDictSizeEstimator implements ColumnSizeEstimator {

    @Override
    public long estimate(ColumnAnalysisResult columnAnalysisResult) {
        return (columnAnalysisResult.getSize() * 4) / (Integer.SIZE / (Integer.SIZE - Integer.numberOfLeadingZeros((int)columnAnalysisResult.getCardinality()))) + columnAnalysisResult.getCardinality() * columnAnalysisResult.getAvgItemSize() * 4;
    }

}
