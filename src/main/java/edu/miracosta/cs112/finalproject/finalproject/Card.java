package edu.miracosta.cs112.finalproject.finalproject;
//Create the deck of cards
//Create the value, set, and get
//Concrete Class!!!

//Cards suit and value
public class Card {

    String suit; //Diamonds, Hearts, Spades, Clubs
    String value; //2-10, Jack, Queen, King, Ace (1 or 11)

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + " of " + suit;
    }

}