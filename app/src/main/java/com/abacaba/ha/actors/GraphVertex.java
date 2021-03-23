package com.abacaba.ha.actors;

public class GraphVertex {
    private int number;
    private double weight;
    public GraphVertex(int number, double weight){
        this.number = number;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
