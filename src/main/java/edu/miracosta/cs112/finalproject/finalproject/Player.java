package edu.miracosta.cs112.finalproject.finalproject;

import java.util.ArrayList;
import java.util.List;

//Inheritance!
public class Player extends Participant {
    private String name;
    private int balance;
    private List<Card> hand;


    public Player(String name, int balance) {
        super(name);
        this.name = name;
        this.balance = balance;
        this.hand = new ArrayList<>(); //Initializes player hand
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public void adjustBalance(int amount) {
        balance += amount;
    }

    public void clearHand() {
        hand.clear(); // Clears the list of cards in the player's hand
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    @Override
    public int calculateHandValue() {
        int handValue = 0;
        int aceCount = 0;

        for (Card card : hand) {
            int cardValue = switch (card.getValue()) {
                case "Ace" -> 11;
                case "Two" -> 2;
                case "Three" -> 3;
                case "Four" -> 4;
                case "Five" -> 5;
                case "Six" -> 6;
                case "Seven" -> 7;
                case "Eight" -> 8;
                case "Nine" -> 9;
                case "Ten", "Jack", "Queen", "King" -> 10;
                default -> throw new IllegalStateException("Invalid card value: " + card.getValue());
            };

            handValue += cardValue;
            if (cardValue == 11) aceCount++;
        }

        // Adjust for Aces if the hand value exceeds 21
        while (handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }

        return handValue;
    }

}