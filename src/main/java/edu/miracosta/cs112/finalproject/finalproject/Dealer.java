package edu.miracosta.cs112.finalproject.finalproject;

public class Dealer extends Participant {

    public Dealer(String name) {
        super(name);
    }

    //Calculate the value of the dealers hand
    @Override
    public int calculateHandValue() {
        //Dealer hand value logic
        int handValue = 0;
        int aceCount = 0;

        for (Card card : hand) {
            int cardValue = switch (card.getValue()) {
                case "Ace" -> 11;
                case "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten" -> Integer.parseInt(card.getValue().substring(0, 1));
                case "Jack", "Queen", "King" -> 10;
                default -> throw new IllegalStateException("Invalid card value");
            };

            handValue += cardValue;
            if (cardValue == 11) {
                aceCount++;
            }
        }

        while (handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }
        return handValue;
    }
}
