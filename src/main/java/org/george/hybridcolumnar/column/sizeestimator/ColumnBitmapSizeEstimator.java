package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public class ColumnBitmapSizeEstimator implements ColumnSizeEstimator {

    @Override
    public long estimate(ColumnAnalysisResult columnAnalysisResult) {
        return columnAnalysisResult.getCardinality() * columnAnalysisResult.getSize();
    }

}
