package com.abacaba.ha.actors;

public class Person {
    private String name;
    private Double debt;
    private Integer companyId;
    private Integer id;

    public Person(String name, Double debt, Integer companyId, Integer id){
        this.name = name;
        this.debt = debt;
        this.companyId = companyId;
        this.id = id;
    }

    public Double getDebt() {
        return debt;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getName() {
        return name;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public Integer getId() {
        return id;
    }
}
