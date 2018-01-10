package client;

import com.bsr.services.bank.BankPortType;
import com.bsr.services.bank.BankService;
import com.bsr.types.bank.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.List;
import java.util.stream.Collectors;

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
    private TextArea accountsTextArea;

    private BankPortType port;

    @FXML
    private void initialize() {
        loginButton.setOnMouseClicked(getLogInEventEventHandler());
        logoutButton.setOnMouseClicked(getLogoutEventEventHandler());
        paymentButton.setOnMouseClicked(getPaymentEventEventHandler());
        withdrawButton.setOnMouseClicked(getWithdrawEventEventHandler());
        historyButton.setOnMouseClicked(getHistoryEventEventHandler());
        internalTransferButton.setOnMouseClicked(getInternalTransferEventEventHandler());
        externalTransferButton.setOnMouseClicked(getExternalTransferEventEventHandler());

        BankService bankService = new BankService();
        bankService.setHandlerResolver(new SoapHeaderHandler());
        port = bankService.getBankPort();
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
            try {
                ExternalTransferRequest request = new ExternalTransferRequest();
                request.setTitle(title);
                request.setSourceAccount(sourceAccount);
                request.setDestinationAccount(destinationAccount);
                request.setAmount(Integer.parseInt(amount));
                request.setName(name);
                OperationSuccessResponse response = port.externalTransfer(request);
                showPopup(response.getMessage(), INFORMATION);
            } catch (SOAPFaultException ex) {
                showPopup(ex.getFault().getFaultString(), Alert.AlertType.ERROR);
            }
        };
    }

    private EventHandler<MouseEvent> getInternalTransferEventEventHandler() {
        return event -> {
            String sourceAccount = internalSourceAccount.getText();
            String destinationAccount = internalDestinationAccount.getText();
            String title = internalTitle.getText();
            String amount = internalAmount.getText();
            try {
                InternalTransferRequest request = new InternalTransferRequest();
                request.setTitle(title);
                request.setSourceAccount(sourceAccount);
                request.setDestinationAccount(destinationAccount);
                request.setAmount(Integer.parseInt(amount));
                OperationSuccessResponse response = port.internalTransfer(request);
                showPopup(response.getMessage(), INFORMATION);
            } catch (SOAPFaultException ex) {
                showPopup(ex.getFault().getFaultString(), Alert.AlertType.ERROR);
            }
        };
    }

    private EventHandler<MouseEvent> getHistoryEventEventHandler() {
        return event -> {
            String account = historyAccount.getText();
            try {
                AccountHistoryRequest request = new AccountHistoryRequest();
                request.setAccount(account);
                AccountHistoryResponse response = port.accountHistory(request);
                showPopup(response.getAccountHistory().toString(), INFORMATION);
            } catch (SOAPFaultException ex) {
                showPopup(ex.getFault().getFaultString(), Alert.AlertType.ERROR);
            }
        };
    }

    private EventHandler<MouseEvent> getWithdrawEventEventHandler() {
        return event -> {
            String account = withdrawAccount.getText();
            String amount = withdrawAmount.getText();
            try {
                WithdrawalRequest request = new WithdrawalRequest();
                request.setDestinationAccount(account);
                request.setAmount(Integer.parseInt(amount));
                OperationSuccessResponse response = port.withdrawal(request);
                showPopup(response.getMessage(), INFORMATION);
            } catch (SOAPFaultException ex) {
                showPopup(ex.getFault().getFaultString(), Alert.AlertType.ERROR);
            }
        };
    }

    private EventHandler<MouseEvent> getPaymentEventEventHandler() {
        return event -> {
            String account = paymentAccount.getText();
            String amount = paymentAmount.getText();
            try {
                PaymentRequest request = new PaymentRequest();
                request.setDestinationAccount(account);
                request.setAmount(Integer.parseInt(amount));
                OperationSuccessResponse response = port.payment(request);
                showPopup(response.getMessage(), INFORMATION);
            } catch (SOAPFaultException ex) {
                showPopup(ex.getFault().getFaultString(), Alert.AlertType.ERROR);
            }
        };
    }

    private EventHandler<MouseEvent> getLogInEventEventHandler() {
        return event -> {
            String username = userText.getText();
            String password = passwordText.getText();
            if (validString(username) && validString(password)) {
                userText.setText("");
                passwordText.setText("");
                CredentialsHolder.username = username;
                CredentialsHolder.password = password;

                LoginResponse response;

                try {
                    response = port.login(new LoginRequest());
                } catch (SOAPFaultException ex) {
                    showPopup(ex.getFault().getFaultString(), Alert.AlertType.ERROR);
                    return;
                }

                loginForm.visibleProperty().setValue(false);
                bankForm.visibleProperty().setValue(true);

                List<String> accounts = response.getAccounts().stream().map(AccountsElement::getAccountNumber).collect(Collectors.toList());
                accounts.forEach(s -> accountsTextArea.appendText(s + System.lineSeparator()));
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
