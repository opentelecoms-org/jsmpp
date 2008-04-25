package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.ResponseHandler;

/**
 * @author uudashr
 *
 */
interface GenericSMPPSessionState {
    
    /**
     * Get the associated session state value.
     * 
     * @return the {@link SessionState} associated by the interface implementation.
     */
    SessionState getSessionState();
    
    /**
     * Process the generick_nack command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the enquire_link command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the enquire_link_resp command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the unbind command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the unbind_resp command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the unknown command id.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the data short message request command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the data short message response command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException;
}
