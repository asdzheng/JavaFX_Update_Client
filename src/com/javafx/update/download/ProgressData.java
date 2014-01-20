package com.javafx.update.download;

public class ProgressData {

    // the three variable from ProgressListener update method
    private long totalSize;
    private long curSize;
    private int items;
    //the below variable is caculated 
    private double percent;
    private String speed;

    public void calculatePercent() {
        percent = ((double) (curSize * 1000 / totalSize)) / 10;
    }
    
    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurSize() {
        return curSize;
    }

    public void setCurSize(long curSize) {
        this.curSize = curSize;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (this.totalSize ^ (this.totalSize >>> 32));
        hash = 83 * hash + (int) (this.curSize ^ (this.curSize >>> 32));
        hash = 83 * hash + this.items;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.percent) ^ (Double.doubleToLongBits(this.percent) >>> 32));
        hash = 83 * hash + (this.speed != null ? this.speed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProgressData other = (ProgressData) obj;
        if (this.totalSize != other.totalSize) {
            return false;
        }
        if (this.curSize != other.curSize) {
            return false;
        }
        if (this.items != other.items) {
            return false;
        }
        if (Double.doubleToLongBits(this.percent) != Double.doubleToLongBits(other.percent)) {
            return false;
        }
        if ((this.speed == null) ? (other.speed != null) : !this.speed.equals(other.speed)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProgressData{" + "totalSize=" + totalSize + ", curSize=" + curSize + ", items=" + items + ", percent=" + percent + ", speed=" + speed + '}';
    }
}
