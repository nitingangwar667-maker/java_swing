package com.college.model;

public class Fee {
    private int feeId;
    private String rollNo;
    private double totalFees;
    private double paidFees;
    private double dueFees;
    private String status;

    public Fee() {}

    public Fee(String rollNo, double totalFees, double paidFees, double dueFees, String status) {
        this.rollNo = rollNo;
        this.totalFees = totalFees;
        this.paidFees = paidFees;
        this.dueFees = dueFees;
        this.status = status;
    }

    public int getFeeId() { return feeId; }
    public void setFeeId(int feeId) { this.feeId = feeId; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public double getTotalFees() { return totalFees; }
    public void setTotalFees(double totalFees) { this.totalFees = totalFees; }

    public double getPaidFees() { return paidFees; }
    public void setPaidFees(double paidFees) { this.paidFees = paidFees; }

    public double getDueFees() { return dueFees; }
    public void setDueFees(double dueFees) { this.dueFees = dueFees; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
