package edu.miracosta.cs112.finalproject.finalproject;

//import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClock() {welcomeText.setText("Welcome to JavaFX Application!");}

    public void onHelloButtonClick(ActionEvent actionEvent) {

    }
}