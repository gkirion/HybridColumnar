package org.george.hybridcolumnar.column.sizeestimator;

import org.george.hybridcolumnar.column.analyzer.ColumnAnalysisResult;

public interface ColumnSizeEstimator {

    long estimate(ColumnAnalysisResult columnAnalysisResult);

}
