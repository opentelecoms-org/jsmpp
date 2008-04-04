package org.jsmpp.session.state;

/**
 * Enum constant represent session state.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public enum Mode {

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
     * Bound transceiver, means bound transceive has been initiated.
     */
    BOUND_TRX(true, true, true),

    /**
     * Unbound, means unbound has been initiated but the connection hasn't been
     * closed.
     */
    UNBOUND(false),

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

    private Mode(boolean bound, boolean transmitable, boolean receiveable) {
        this.bound = bound;
        this.transmitable = transmitable;
        this.receiveable = receiveable;
    }

    private Mode(boolean bound) {
        this(bound, false, false);
    }

    public boolean isBound() {
        return bound;
    }

    public boolean isTransmittable() {
        return transmitable;
    }

    public boolean isReceivable() {
        return receiveable;
    }
}
