package bsr.project.bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class MainViewController {

    // formsy
    @FXML
    private GridPane loginForm;

    @FXML
    private GridPane bankForm;

    // login
    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button loginButton;

    @FXML

    private Button logoutButton;

    // payment
    @FXML
    private Button paymentButton;

    @FXML
    private TextField paymentAccount;

    @FXML
    private TextField paymentAmount;

    // withdraw
    @FXML
    private Button withdrawButton;

    @FXML
    private TextField withdrawAccount;

    @FXML
    private TextField withdrawAmount;

    // history
    @FXML
    private TextField historyAccount;

    @FXML
    private Button historyButton;

    // internal transfer
    @FXML
    private Button internalTransferButton;

    @FXML
    private TextField internalSourceAccount;

    @FXML
    private TextField internalDestinationAccount;

    @FXML
    private TextField internalTitle;

    @FXML
    private TextField internalAmount;

    // external transfer
    @FXML
    private Button externalTransferButton;

    @FXML
    private TextField externalSourceAccount;

    @FXML
    private TextField externalDestinationAccount;

    @FXML
    private TextField externalTitle;

    @FXML
    private TextField externalName;

    @FXML
    private TextField externalAmount;

    // account list
    @FXML
    private ListView<String> accountList;

    private ObservableList<String> userAccounts = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loginButton.setOnMouseClicked(getLogInEventEventHandler());
        logoutButton.setOnMouseClicked(getLogoutEventEventHandler());
        paymentButton.setOnMouseClicked(getPaymentEventEventHandler());
        withdrawButton.setOnMouseClicked(getWithdrawEventEventHandler());
        historyButton.setOnMouseClicked(getHistoryEventEventHandler());
        internalTransferButton.setOnMouseClicked(getInternalTransferEventEventHandler());
        externalTransferButton.setOnMouseClicked(getExternalTransferEventEventHandler());
    }

    private EventHandler<MouseEvent> getLogoutEventEventHandler() {
        return event -> {
            CredentialsHolder.username = "";
            CredentialsHolder.password = "";
            loginForm.visibleProperty().setValue(true);
            bankForm.visibleProperty().setValue(false);
        };
    }

    private EventHandler<MouseEvent> getExternalTransferEventEventHandler() {
        return event -> {
            String sourceAccount = externalSourceAccount.getText();
            String destinationAccount = externalDestinationAccount.getText();
            String title = externalTitle.getText();
            String name = externalName.getText();
            String amount = externalAmount.getText();
            System.out.println("przelew zewnętrzny " + sourceAccount + " do " + destinationAccount + " " + title + " " + amount + " " + name);
            // TODO request przelew zewnetrzny

            // TODO popup z wiadomoscia zwrotna
            showPopup("TODO", INFORMATION);
            showPopup("TODO", ERROR);
        };
    }

    private EventHandler<MouseEvent> getInternalTransferEventEventHandler() {
        return event -> {
            String sourceAccount = internalSourceAccount.getText();
            String destinationAccount = internalDestinationAccount.getText();
            String title = internalTitle.getText();
            String amount = internalAmount.getText();
            System.out.println("przelew wewnętrzny " + sourceAccount + " do " + destinationAccount + " " + title + " " + amount);
            // TODO request wewnetrzny przelew

            // TODO popup z wiadomoscia zwrotna
            showPopup("TODO", INFORMATION);
            showPopup("TODO", ERROR);
        };
    }

    private EventHandler<MouseEvent> getHistoryEventEventHandler() {
        return event -> {
            String account = historyAccount.getText();
            System.out.println("historia " + account);
            // TODO request historia

            // TODO popup z wiadomoscia zwrotna
            showPopup("TODO", INFORMATION);
            showPopup("TODO", ERROR);
        };
    }

    private EventHandler<MouseEvent> getWithdrawEventEventHandler() {
        return event -> {
            String account = withdrawAccount.getText();
            String amount = withdrawAmount.getText();
            System.out.println("wypłata " + account + " " + amount);
            // TODO request wypłata

            // TODO popup z wiadomoscia zwrotna
            showPopup("TODO", INFORMATION);
            showPopup("TODO", ERROR);
        };
    }

    private EventHandler<MouseEvent> getPaymentEventEventHandler() {
        return event -> {
            String account = paymentAccount.getText();
            String amount = paymentAmount.getText();
            System.out.println("wpłata " + account + " " + amount);
            // TODO request wpłata

            // TODO popup z wiadomoscia zwrotna
            showPopup("TODO", INFORMATION);
            showPopup("TODO", ERROR);
        };
    }

    private EventHandler<MouseEvent> getLogInEventEventHandler() {
        return event -> {
            String username = userText.getText();
            String password = passwordText.getText();
            if (validString(username) && validString(password)) {
                userText.setText("");
                passwordText.setText("");
                // TODO request logowania
                loginForm.visibleProperty().setValue(false);
                bankForm.visibleProperty().setValue(true);
                CredentialsHolder.username = username;
                CredentialsHolder.password = password;

                // TODO wypełnić konta usera
                userAccounts.add("test");
                accountList.setItems(userAccounts);
            }
        };
    }

    private void showPopup(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    private boolean validString(String s) {
        return s != null && !s.isEmpty();
    }
}
