package org.george.hybridcolumnar.column.analyzer;

public class ColumnAnalysisResult {

    int size;
    int numberOfRuns;
    long avgItemSize;
    int cardinality;
    long maxNumber = 0;
    long maxDelta = 0;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        this.numberOfRuns = numberOfRuns;
    }

    public long getAvgItemSize() {
        return avgItemSize;
    }

    public void setAvgItemSize(long avgItemSize) {
        this.avgItemSize = avgItemSize;
    }

    public int getCardinality() {
        return cardinality;
    }

    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    public long getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(long maxNumber) {
        this.maxNumber = maxNumber;
    }

    public long getMaxDelta() {
        return maxDelta;
    }

    public void setMaxDelta(long maxDelta) {
        this.maxDelta = maxDelta;
    }

}
