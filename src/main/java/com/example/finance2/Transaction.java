package com.example.finance2;

import jakarta.persistence.*;

@Entity     //ska bli tabell i databas säger till spring boot, utan går det ej
public class Transaction {  //i sql ska  tabell med namnet transactions se ut såhär

    @Id     // hur databasen ska hitta rätt, som ett personnummer, unikt
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // varje rad ska få id automatiskt
    private Long id;        // variabeln long som sparar nummrne

    private String description;     //data vi vill spara
    private double amount;      //getters and setters

    public Long getId() {   // hämta data
        return id;
    }

    public String getDescription() {
        return description;     // metioden ger tillbaka description
    }

    public void setDescription(String description) {        //ändra description
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}