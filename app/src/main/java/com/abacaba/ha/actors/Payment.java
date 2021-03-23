package com.abacaba.ha.actors;

public class Payment {
    int personId;
    double sum;
    int transactionId;
    int type;
    public Payment(int personId, double sum, int transactionId, int type){
        this.personId = personId;
        this.sum = sum;
        this.transactionId = transactionId;
        this.type = type;
    }

    public double getSum() {
        return sum;
    }

    public int getPersonId() {
        return personId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getType() {
        return type;
    }
}
