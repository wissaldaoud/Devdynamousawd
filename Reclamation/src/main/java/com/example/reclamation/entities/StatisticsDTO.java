package com.example.reclamation.entities;

public class StatisticsDTO {
    private long totalComplaints;
    private long pendingCount;
    private long solvedCount;
    private long declinedCount;
    private double resolutionRate;

    public long getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(long totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public long getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(long solvedCount) {
        this.solvedCount = solvedCount;
    }

    public long getDeclinedCount() {
        return declinedCount;
    }

    public void setDeclinedCount(long declinedCount) {
        this.declinedCount = declinedCount;
    }

    public double getResolutionRate() {
        return resolutionRate;
    }

    public void setResolutionRate(double resolutionRate) {
        this.resolutionRate = resolutionRate;
    }
}
