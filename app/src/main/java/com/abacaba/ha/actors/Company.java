package com.abacaba.ha.actors;

public class Company {
    private String name;
    private Integer id;

    public Company(){}
    public Company(String name, Integer id){
        this.name = name;
        setId(id);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
