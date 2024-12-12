package edu.miracosta.cs112.finalproject.finalproject;

//imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;

//Class
public class BlackjackController {

    //FXML's
    @FXML
    private Button DealCards;
    @FXML
    private Button Balance;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button LeaveTable;
    @FXML
    private Button DoubleDown;
    @FXML
    private Button Bet;
    @FXML
    private Button Hit;
    @FXML
    private Button Stand;
    @FXML
    private Button NewGame;
    //@FXML
    //private Button startGame;
    @FXML
    private ImageView PlayerCard1, PlayerCard2, PlayerCard3, PlayerCard4;
    @FXML
    private ImageView DealerCard1, DealerCard2, DealerCard3, DealerCard4;

    //Deck, Player, Array
    private Deck deck;
    private Player player;
    private List<Card> dealerHand;

    //Display the player's balance
    @FXML
    public void showBalance() {
        String balanceMessage = "Your current balance is: $" + player.getBalance();
        System.out.println(balanceMessage);

        //Update a label in the UI
        if (balanceLabel != null) {
            balanceLabel.setText(balanceMessage);
        } else {
            //Popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Player Balance");
            alert.setHeaderText("Current Balance");
            alert.setContentText(balanceMessage);
            alert.showAndWait();
        }
    }

    //Create and shuffle deck
    //Create player and give a starting balance
    @FXML
    public void initialize() {
        deck = new Deck();
        deck.shuffle();
        player = new Player("Player 1", 1000);
        dealerHand = new ArrayList<>();

        //Disable all buttons except for the "Bet" button
        setButtonState(false);
        Bet.setDisable(false);
        LeaveTable.setDisable(false);
    }

    //CUSTOM EXCEPTION
    @FXML
    public void dealCards() {
        Hit.setDisable(false);
        Stand.setDisable(false);
        DoubleDown.setDisable(false);
        try {
            if (player.getBalance() < 0) {
                throw new BlackjackException("Insufficient balance to place a bet.");
            }

            // Deal cards logic
            if (deck.isEmpty()) {
                System.out.println("Deck is empty! Reshuffling.");
                deck = new Deck();
                deck.shuffle();
            }

            // Deal two cards to player and dealer
            Card playerCard1 = deck.dealCard();
            Card playerCard2 = deck.dealCard();
            Card dealerCard1 = deck.dealCard();
            Card dealerCard2 = deck.dealCard();

            // Add cards to hands
            player.addCardToHand(playerCard1);
            player.addCardToHand(playerCard2);
            dealerHand.add(dealerCard1);
            dealerHand.add(dealerCard2);

            // Set images for player's cards
            PlayerCard1.setImage(getCardImage(playerCard1));
            PlayerCard2.setImage(getCardImage(playerCard2));

            // Set images for dealer's cards (one face down)
            DealerCard1.setImage(getCardImage(dealerCard1));
            DealerCard2.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Pictures/SingleFaceDownCard.jpg"))));

            // Enable other buttons, disable "Bet"
            setButtonState(true);
            Bet.setDisable(true);
        } catch (BlackjackException e) {
            System.out.println(e.getMessage());
            showAlert("Error", e.getMessage());
        }
    }

    //Program finding image
    private Image getCardImage(Card card) {
        String cardName = card.getValue().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".jpg";
        String imagePath = "/Cards/" + cardName;
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream == null) {
            throw new RuntimeException("Image not found: " + imagePath);
        }
        return new Image(imageStream);
    }

    //Clear card slots
    @FXML
    private void clearRemainingCards() {
        PlayerCard3.setImage(null);
        PlayerCard4.setImage(null);
        DealerCard3.setImage(null);
        DealerCard4.setImage(null);
    }

    @FXML
    public void hit() {
        // Deal a new card
        Card newCard = deck.dealCard();
        player.addCardToHand(newCard);

        // Update UI
        if (PlayerCard3.getImage() == null) {
            PlayerCard3.setImage(getCardImage(newCard));
        } else if (PlayerCard4.getImage() == null) {
            PlayerCard4.setImage(getCardImage(newCard));
        } else {
            System.out.println("Maximum cards reached for the player.");
        }

        // Disable Double Down button after Hit
        DoubleDown.setDisable(true);

        // Check if player busts
        int playerHandValue = player.calculateHandValue();
        if (playerHandValue > 21) {
            System.out.println("Player busts! Dealer wins.");
            showBustMessage();
            endGame();
        }
    }

    //Initial bet
    @FXML
    public void bet(ActionEvent event) {
        // Deduct $200 from the player's balance and enable other buttons.
        player.adjustBalance(-200);
        updateBalanceLabel(); // Update the balance display
        setButtonState(true); // Enable other buttons
        Bet.setDisable(true); // Disable the Bet button
        Balance.setDisable(false);
        DealCards.setDisable(false);
        NewGame.setDisable(false);
        Hit.setDisable(true);
        Stand.setDisable(true);
        DoubleDown.setDisable(true);
        System.out.println("Bet placed. $200 deducted from balance.");
    }

    @FXML
    public void doubleDown() {
        try {
            if (player.getBalance() < 200) {
                throw new BlackjackException("Insufficient balance to Double Down.");
            }

            // Deduct $200 from balance
            player.adjustBalance(-200);
            updateBalanceLabel(); // Update the balance display

            // Deal one card to the player
            Card newCard = deck.dealCard();
            player.addCardToHand(newCard);

            // Update UI
            if (PlayerCard3.getImage() == null) {
                PlayerCard3.setImage(getCardImage(newCard));
            } else if (PlayerCard4.getImage() == null) {
                PlayerCard4.setImage(getCardImage(newCard));
            }

            // Disable Double Down button
            DoubleDown.setDisable(true);

            // Proceed to dealer's turn
            stand();
        } catch (BlackjackException e) {
            System.out.println(e.getMessage());
            showAlert("Error", e.getMessage());
        }
    }

    // Message for when player busts
    private void showBustMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Player Busted!");
        alert.setContentText("Your hand value exceeded 21. You lose.");
        alert.showAndWait();
    }

    //Ending the game, reset
    private void endGame() {
        // Clear player and dealer hands
        player.clearHand();
        dealerHand.clear();

        // Clear card images from the board
        PlayerCard1.setImage(null);
        PlayerCard2.setImage(null);
        PlayerCard3.setImage(null);
        PlayerCard4.setImage(null);
        DealerCard1.setImage(null);
        DealerCard2.setImage(null);
        DealerCard3.setImage(null);
        DealerCard4.setImage(null);

        // Create a new deck, shuffle it
        // Make sure buttons are activated
        deck = new Deck();
        deck.shuffle();
        //setButtonState(true);
        Hit.setDisable(true);
        Stand.setDisable(true);
        DoubleDown.setDisable(true);
        DealCards.setDisable(true);
        Balance.setDisable(true);

        Bet.setDisable(false);
        //LeaveTable.setDisable(false);

        System.out.println("Game has been reset. Start a new round!");
    }

    //Stand logic & Dealer's turn logic
    @FXML
    public void stand() {
        disableActionButtons(true);
        // Reveal the dealer's second card
        DealerCard2.setImage(getCardImage(dealerHand.get(1)));

        // Dealer's turn logic
        while (getDealerHandValue() < 17 ||
                (getDealerHandValue() <= player.calculateHandValue() && getDealerHandValue() <= 21)) {
            Card newCard = deck.dealCard();
            dealerHand.add(newCard);
            if (DealerCard3.getImage() == null) {
                DealerCard3.setImage(getCardImage(newCard));
            } else if (DealerCard4.getImage() == null) {
                DealerCard4.setImage(getCardImage(newCard));
            } else {
                System.out.println("Maximum cards reached for the dealer.");
            }
        }
        determineWinner();
    }

    //Calculate the dealers hand
    private int getDealerHandValue() {
        int handValue = 0;
        int aceCount = 0;

        for (Card card : dealerHand) {
            int cardValue = getCardValue(card);
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

    //Disable buttons once player stands
    private void disableActionButtons(boolean disable) {
        Hit.setDisable(disable);
        Stand.setDisable(disable);
        DoubleDown.setDisable(disable);
    }

    //Get the value of a card
    private int getCardValue(Card card) {
        return switch (card.getValue()) {
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
            default -> throw new IllegalArgumentException("Invalid card value: " + card.getValue());
        };
    }

    //Game over message
    private void showGameOverMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Result");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Track whether the player doubled down
    private boolean doubledDown = false;

    private void determineWinner() {
        int playerValue = player.calculateHandValue();
        int dealerValue = getDealerHandValue();

        if (playerValue > 21) {
            System.out.println("Player busts! Dealer wins.");
            showGameOverMessage("You lose! Player busted.");
        } else if (dealerValue > 21) {
            System.out.println("Dealer busts! Player wins.");
            if (doubledDown) {
                player.adjustBalance(800); // Add $800 if doubled down
                updateBalanceLabel();
                showGameOverMessage("You win! Dealer busted. $800 added to your balance.");
            } else {
                player.adjustBalance(400); // Add $400 for a normal win
                updateBalanceLabel();
                showGameOverMessage("You win! Dealer busted. $400 added to your balance.");
            }
        } else if (playerValue > dealerValue) {
            System.out.println("Player wins!");
            if (doubledDown) {
                player.adjustBalance(800); // Add $800 if doubled down
                updateBalanceLabel();
                showGameOverMessage("You win! Your hand is better. $800 added to your balance.");
            } else {
                player.adjustBalance(400); // Add $400 for a normal win
                updateBalanceLabel();
                showGameOverMessage("You win! Your hand is better. $400 added to your balance.");
            }
        } else if (dealerValue > playerValue) {
            System.out.println("Dealer wins!");
            showGameOverMessage("You lose! Dealer's hand is better.");
        } else {
            System.out.println("It's a tie!");
            showGameOverMessage("It's a tie!");
        }

        // Reset the doubledDown flag for the next round
        doubledDown = false;

        // Reset game
        endGame();
    }

    private void updateBalanceLabel() {
        if (balanceLabel != null) {
            balanceLabel.setText("Balance: $" + player.getBalance());
        } else {
            System.out.println("Balance Label is not initialized.");
        }
    }

    //Make sure buttons are enabled
    private void setButtonState(boolean enable) {

        Hit.setDisable(!enable);
        Stand.setDisable(!enable);
        DoubleDown.setDisable(!enable);
    }

    @FXML
    public void startNextRound() {
        clearRemainingCards();

        PlayerCard1.setImage(null);
        PlayerCard2.setImage(null);
        DealerCard1.setImage(null);
        DealerCard2.setImage(null);

        deck = new Deck();
        deck.shuffle();
        player.clearHand();
        dealerHand.clear();

        // Reset buttons
        setButtonState(false); // Disable all buttons
        Bet.setDisable(false); // Enable "Bet"

        System.out.println("New round started!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void leaveTable() {
        System.out.println("Exiting the game. Thank you for playing!");
        Platform.exit();
        System.exit(0); // Ensure the program exits completely
    }
}