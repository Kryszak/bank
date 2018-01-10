package client;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SoapHeaderHandler implements SOAPHandler<SOAPMessageContext>, HandlerResolver {


    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {

            SOAPMessage message = context.getMessage();

            try {
                SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();

                //new
                if (envelope.getHeader() != null) {
                    envelope.getHeader().detachNode();
                }

                SOAPHeader header = envelope.addHeader();
                SOAPElement authHeader = header.addChildElement("AuthHeader", "bank", "http://bsr.com/types/bank");
                SOAPElement username = authHeader.addChildElement("username", "bank", "http://bsr.com/types/bank");
                username.addTextNode(CredentialsHolder.username);
                SOAPElement password = authHeader.addChildElement("password", "bank", "http://bsr.com/types/bank");
                password.addTextNode(CredentialsHolder.password);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                SOAPMessage message = context.getMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return outboundProperty;
    }


    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return false;
    }

    @Override
    public void close(MessageContext context) {

    }

    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<>();

        SoapHeaderHandler hh = new SoapHeaderHandler();

        handlerChain.add(hh);

        return handlerChain;
    }
}
