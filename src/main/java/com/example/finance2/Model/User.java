package com.example.finance2.Model;
import jakarta.persistence.*; //Importerar verktygen för databasen
// ETIKETTER (Annotationer)
@Entity// Säger till Java: "Detta är en databasmall"
@Table(name = " users")
public class User {
    //VARIABLER

    @Id // Säger: "Detta är den unika nyckel"
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Säger: "MySQL sköter numreringen 1, 2, 3 själv"

    private int id; // Ett heltal för ID
    private String name; // En textsträng för namnet
    private double balance; // Ett decimaltal för pengarna

    //KONSTRUKTORER (Verktygen för att skapa en användare i koden)

    // En tom konstruktor är ett krav för Spring Boot (som en tom blankett)
    public User(){}

    // En konstruktor för att snabbt kunna skapa användare
    public User(String name, double balance){
        this.name = name; // Sätt namnet på den vi skapar
        this.balance = balance; //Sätt saldot på den vi skapar
    }

    // GETTERS & SETTERS (Dörrarna till variablerna)


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
