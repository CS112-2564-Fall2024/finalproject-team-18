package edu.miracosta.cs112.finalproject.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class BlackjackController {
    @FXML
    private Button startGame;
    @FXML
    private ImageView PlayerCard1, PlayerCard2, PlayerCard3, PlayerCard4;
    @FXML
    private ImageView DealerCard1, DealerCard2, DealerCard3, DealerCard4;

    private Deck deck;
    private List<Card> dealerHand;

    @FXML
    public void initialize() {
        deck = new Deck();
        deck.shuffle();
        dealerHand = new ArrayList<>();
    }

    //Dealing initial cards
    @FXML
    public void dealCards() {
        //Deal two cards to player and dealer
        Card playerCard1 = deck.dealCard();
        Card playerCard2 = deck.dealCard();
        Card dealerCard1 = deck.dealCard();
        Card dealerCard2 = deck.dealCard();

        //Set image for player's first two cards
        PlayerCard1.setImage(getCardImage(playerCard1));
        PlayerCard2.setImage(getCardImage(playerCard2));

        //Set image for dealer's first two cards
        DealerCard1.setImage(getCardImage(dealerCard1));
        DealerCard2.setImage(new Image(getClass().getResourceAsStream("/edu/miracosta/cs112/finalproject/Pictures/SingleFaceDownCard.jpg")));

        //Remaining card slots unused
        clearRemainingCards();
    }

    private Image getCardImage(Card card) {
        String cardName = card.getValue() + "_of_" + card.getSuit();
        return new Image(BlackjackController.class.getResourceAsStream("edu/miracosta/cs112/finalproject/Pictures" + cardName + ".png"));

    }

    //Clear remaining card slots

    @FXML
    private void clearRemainingCards() {
        PlayerCard3.setImage(null);
        PlayerCard4.setImage(null);
        DealerCard3.setImage(null);
        DealerCard4.setImage(null);
    }

    //Hit method
    @FXML
    public void hit() {
        Card newCard = deck.dealCard();

        if(PlayerCard3.getImage() == null) {
            PlayerCard3.setImage(getCardImage(newCard));
        } else if (PlayerCard4.getImage() == null) {
            PlayerCard4.setImage(getCardImage(newCard));
        } else {
            System.out.println("Maximum cards reached for the players");
        }
    }

    //Stand method
    @FXML
    public void stand() {
        //Show the dealers second card once the player stands
        DealerCard2.setImage(getCardImage(deck.dealCard()));

        while (getDealerHandValue() < 17) {
            if (DealerCard3.getImage() == null) {
                DealerCard3.setImage(getCardImage(deck.dealCard()));
            } else if (DealerCard4.getImage() == null) {
                DealerCard4.setImage(getCardImage(deck.dealCard()));
            }
        }

        //Implement logic to determine winner
    }

    // Calculate the dealer's hand value
    private int getDealerHandValue() {
        int handValue = 0;
        int aceCount = 0;

        // Loop through the dealer's hand and calculate the total hand value
        for (Card card : dealerHand) {
            int cardValue = getCardValue(card);
            handValue += cardValue;
            if (cardValue == 11) {
                aceCount++;
            }
        }

        // Adjust for Aces if necessary
        while (handValue > 21 && aceCount > 0) {
            handValue -= 10; // Convert Ace from 11 to 1
            aceCount--;
        }

        return handValue;
    }

    // Get the value of a single card
    private int getCardValue(Card card) {
        switch (card.getValue()) {
            case "Ace": return 11;
            case "Two": case "Three": case "Four": case "Five": case "Six":
            case "Seven": case "Eight": case "Nine": case "Ten": return Integer.parseInt(card.getValue().substring(0, 1));
            case "Jack": case "Queen": case "King": return 10;
            default: throw new IllegalArgumentException("Invalid card value: " + card.getValue());
        }
    }

    //Start the next round
    @FXML
    public void startNextRound() {
        //clear all cards
        PlayerCard1.setImage(null);
        PlayerCard2.setImage(null);
        PlayerCard3.setImage(null);
        PlayerCard4.setImage(null);

        DealerCard1.setImage(null);
        DealerCard2.setImage(null);
        DealerCard3.setImage(null);
        DealerCard4.setImage(null);

        //Reinitialize deck
        deck = new Deck();
        deck.shuffle();
        dealerHand.clear();
    }
}