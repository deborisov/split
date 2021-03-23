package com.abacaba.ha.actors;

public class TransactionObj {
    private int from;
    private int to;
    private double sum;

    public TransactionObj(int from, int to, double sum){
        this.from = from;
        this.to = to;
        this.sum = sum;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public double getSum() {
        return sum;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
