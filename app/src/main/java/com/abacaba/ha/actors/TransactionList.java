package com.abacaba.ha.actors;

public class TransactionList {
    Integer id;
    String name;
    Integer compId;
    public TransactionList(String name, Integer id, Integer compId){
        this.id = id;
        this.name = name;
        this.compId = compId;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
