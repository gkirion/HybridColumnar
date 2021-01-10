package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public class ColumnRoaringSizeEstimator implements ColumnSizeEstimator {

    @Override
    public long estimate(ColumnAnalysisResult columnAnalysisResult) {
        return Math.min(columnAnalysisResult.getCardinality() * columnAnalysisResult.getSize() / 8, 16 * columnAnalysisResult.getSize() / 8);
    }

}
