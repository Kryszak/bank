
package com.bsr.types.bank;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.bsr.types.bank package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.bsr.types.bank
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AuthHeader }
     * 
     */
    public AuthHeader createAuthHeader() {
        return new AuthHeader();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link AccountsElement }
     * 
     */
    public AccountsElement createAccountsElement() {
        return new AccountsElement();
    }

    /**
     * Create an instance of {@link WithdrawalRequest }
     * 
     */
    public WithdrawalRequest createWithdrawalRequest() {
        return new WithdrawalRequest();
    }

    /**
     * Create an instance of {@link InternalTransferRequest }
     * 
     */
    public InternalTransferRequest createInternalTransferRequest() {
        return new InternalTransferRequest();
    }

    /**
     * Create an instance of {@link PaymentRequest }
     * 
     */
    public PaymentRequest createPaymentRequest() {
        return new PaymentRequest();
    }

    /**
     * Create an instance of {@link AccountHistoryRequest }
     * 
     */
    public AccountHistoryRequest createAccountHistoryRequest() {
        return new AccountHistoryRequest();
    }

    /**
     * Create an instance of {@link OperationSuccessResponse }
     * 
     */
    public OperationSuccessResponse createOperationSuccessResponse() {
        return new OperationSuccessResponse();
    }

    /**
     * Create an instance of {@link LoginRequest }
     * 
     */
    public LoginRequest createLoginRequest() {
        return new LoginRequest();
    }

    /**
     * Create an instance of {@link ExternalTransferRequest }
     * 
     */
    public ExternalTransferRequest createExternalTransferRequest() {
        return new ExternalTransferRequest();
    }

    /**
     * Create an instance of {@link AccountHistoryResponse }
     * 
     */
    public AccountHistoryResponse createAccountHistoryResponse() {
        return new AccountHistoryResponse();
    }

    /**
     * Create an instance of {@link AccountHistoryElement }
     * 
     */
    public AccountHistoryElement createAccountHistoryElement() {
        return new AccountHistoryElement();
    }

}
