package com.example.mirakram.banmiok;

/**
 * Created by Mirakram on 6/24/2017.
 */

public class Person {
    private String byting;
    private String name;
    private String score;
    private String perc;
    private String n;

    public Person(String name, String score,  String perc, String byting, String n) {
        this.score = score;
        this.name = name;
        this.perc = perc;
        this.byting=byting;
        this.n=n;
    }

    public String getByting() {return byting;}

    public void setByting(String byting) {
        this.byting = byting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPerc() {
        return perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }

    public String getN() {return n;}

    public void setN(String n) {this.n = n;}
}
