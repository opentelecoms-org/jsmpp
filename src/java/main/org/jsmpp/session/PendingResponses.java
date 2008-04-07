package org.jsmpp.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.util.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PendingResponses {
    private static final Logger logger = LoggerFactory.getLogger(PendingResponses.class);

    private final Sequence sequence = new Sequence(1);
    private final Map<Integer, PendingResponse<? extends Command>> responses = new ConcurrentHashMap<Integer, PendingResponse<? extends Command>>();
    private long transactionTimer = 2000;

    public <T extends Command> PendingResponse<T> add(Class<T> type) {
        int seqNum = sequence.nextValue();
        PendingResponse<T> response = new PendingResponse<T>(seqNum, transactionTimer);
        if (responses.containsKey(seqNum)) {
            throw new IllegalStateException("Pending responses already contains seq_num " + seqNum);
        }
        responses.put(seqNum, response);
        return response;
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> PendingResponse<T> remove(PendingResponse<T> response) {
        return (PendingResponse<T>) remove(response.getSequenceNumber());
    }

    public PendingResponse<? extends Command> remove(int sequenceNumber) {
        return responses.remove(sequenceNumber);
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> PendingResponse<T> remove(T resp) {
        return (PendingResponse<T>) remove(resp.getSequenceNumber());
    }

    private void doWait(PendingResponse<? extends Command> pendingResp) throws ResponseTimeoutException, InvalidResponseException {
        try {
            pendingResp.waitDone();
        } catch (ResponseTimeoutException e) {
            remove(pendingResp);
            throw e;
        } catch (InvalidResponseException e) {
            remove(pendingResp);
            throw e;
        }
    }

    public void tryWait(PendingResponse<? extends Command> pendingResp) throws ResponseTimeoutException, InvalidResponseException {
        doWait(pendingResp);
        if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
            logger.warn("Receive NON-OK response of " + pendingResp.getClass().getSimpleName() + ": " + pendingResp.getResponse().getCommandIdAsHex());
        }
    }

    public void wait(PendingResponse<? extends Command> pendingResp) throws ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        doWait(pendingResp);
        if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
            throw new NegativeResponseException(pendingResp.getResponse().getCommandStatus());
        }
    }

    public void setTransactionTimer(long transactionTimer) {
        this.transactionTimer = transactionTimer;
    }

    public long getTransactionTimer() {
        return transactionTimer;
    }

    public int currentSequenceValue() {
        return sequence.currentValue();
    }

    public Sequence getSequence() {
        return sequence;
    }

}