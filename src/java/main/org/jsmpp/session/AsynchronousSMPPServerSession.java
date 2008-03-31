package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.connection.Connection;

public class AsynchronousSMPPServerSession extends BaseServerSession {

    public AsynchronousSMPPServerSession(Connection conn, SessionStateListener sessionStateListener, ServerMessageReceiverListener messageReceiverListener) {
        super(conn, sessionStateListener, messageReceiverListener);
    }

    public void deliverShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, IOException {
        PendingResponse<DeliverSmResp> pendingResp = pendingResponses.add(DeliverSmResp.class);
        try {
            pduSender.sendDeliverSm(pendingResp.getSequenceNumber(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId, priorityFlag, registeredDelivery, dataCoding, shortMessage, params);
        } catch (IOException e) {
            pendingResponses.remove(pendingResp);
            logger.error("Failed deliver short message", e);
            close();
            throw e;
        }
    }
}
