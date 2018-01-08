package bsr.project.bank;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MainViewController {

    @FXML
    private GridPane loginForm;

    @FXML
    private GridPane bankForm;

    @FXML
    private TextField userText;

    @FXML
    private TextField passwordText;

    @FXML
    private Button loginButton;

    @FXML
    private Button paymentButton;

    @FXML
    private TextField paymentAccount;

    @FXML
    private TextField paymentAmount;

    @FXML
    private void initialize() {
        loginButton.setOnMouseClicked(getLogInEventEventHandler());
        paymentButton.setOnMouseClicked(getPaymentEventEventHandler());
    }

    private EventHandler<MouseEvent> getPaymentEventEventHandler() {
        return event -> {
            String account = paymentAccount.getText();
            String amount = paymentAmount.getText();
            System.out.println("wpłata " + account + " " + amount);
            // TODO request wpłata
        };
    }

    private EventHandler<MouseEvent> getLogInEventEventHandler() {
        return event -> {
            String username = userText.getText();
            String password = passwordText.getText();
            if (validString(username) && validString(password)) {
                // TODO request logowania
                loginForm.visibleProperty().setValue(false);
                bankForm.visibleProperty().setValue(true);
            }
        };
    }

    private boolean validString(String s) {
        return s != null && !s.isEmpty();
    }
}
