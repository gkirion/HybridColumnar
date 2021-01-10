package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public class ColumnBitPackingSizeEstimator implements ColumnSizeEstimator {

    @Override
    public long estimate(ColumnAnalysisResult columnAnalysisResult) {
        return (columnAnalysisResult.getSize() * 4) / (Integer.SIZE / (Integer.SIZE - Integer.numberOfLeadingZeros((int)columnAnalysisResult.getMaxNumber())));
    }

}
