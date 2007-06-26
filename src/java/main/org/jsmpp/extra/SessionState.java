package org.jsmpp.extra;

/**
 * Enum const represent session state.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public enum SessionState {

    /**
     * Open, means connection has established but not bounded.
     */
    OPEN(false),

    /**
     * Bound transmitter, means bound transmit has been initiated.
     */
    BOUND_TX(true, true, false),

    /**
     * Bound receiver, means bound receive has been initiated.
     */
    BOUND_RX(true, false, true),

    /**
     * Bound transceiver, means bound tranceive has been initiated.
     */
    BOUND_TRX(true, true, true),

    /**
     * Unbound, means unbound has been initiated but the connection hasn't been
     * closed.
     */
    UNBOUND(true),

    /**
     * There is no connection at all.
     */
    CLOSED(false),

    /**
     * Outbound, means the session is in outbound state, ready to initiate
     * bound.
     */
    OUTBOUND(false);

    private boolean bound;
    private boolean transmitable;
    private boolean receiveable;

    private SessionState(boolean bound, boolean transmitable,
            boolean receiveable) {
        this.bound = bound;
        this.transmitable = transmitable;
        this.receiveable = receiveable;
    }

    private SessionState(boolean bound) {
        this(bound, false, false);
    }

    /**
     * Check whether the session state is bound or not.
     * 
     * @return <tt>true</tt> if session state is bound state
     */
    public boolean isBound() {
        return bound;
    }

    /**
     * Check whether the session state is transmitable.
     * 
     * @return <tt>true</tt> if session is transmitable.
     */
    public boolean isTransmitable() {
        return transmitable;
    }

    /**
     * Check whether the session state is receiveable.
     * 
     * @return <tt>true</tt> if session is receiveable.
     */
    public boolean isReceiveable() {
        return receiveable;
    }
}
