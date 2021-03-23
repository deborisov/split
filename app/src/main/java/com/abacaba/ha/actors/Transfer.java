package com.abacaba.ha.actors;

public class Transfer {
    private String fromName;
    private String toName;
    private Double sum;
    public Transfer(String fromName, String toName, Double sum){
        this.fromName = fromName;
        this.toName = toName;
        this.sum = sum;
    }

    public Double getSum() {
        return sum;
    }

    public String getFromName() {
        return fromName;
    }

    public String getToName() {
        return toName;
    }
}
