package org.jsmpp.extra;

/**
 * Enum constant represent session state.
 *
 * @author uudashr
 * @version 1.0
 * @since 1.0
 */
public enum SessionState {

  /**
   * Open, means connection has established but not bounded.
   */
  OPEN,

  /**
   * Bound transmitter, means bound transmit has been initiated.
   */
  BOUND_TX,

  /**
   * Bound receiver, means bound receive has been initiated.
   */
  BOUND_RX,

  /**
   * Bound transceiver, means bound transceive has been initiated.
   */
  BOUND_TRX,

  /**
   * Unbound, means unbound has been initiated but the connection hasn't been closed.
   */
  UNBOUND,

  /**
   * There is no connection at all.
   */
  CLOSED,

  /**
   * Outbound, means the session is in outbound state, ready to initiate bound.
   */
  OUTBOUND;

  /**
   * Check whether the session state is bound.
   *
   * @return {@code true} if session state is bound state
   */
  public boolean isBound() {
    return equals(BOUND_RX) || equals(BOUND_TX) || equals(BOUND_TRX);
  }

  /**
   * Check whether the session state is transmittable.
   *
   * @return {@code true} if session is transmittable.
   */
  public boolean isTransmittable() {
    return equals(BOUND_TX) || equals(BOUND_TRX);
  }

  /**
   * Check whether the session state is receivable.
   *
   * @return {@code true} if session is receivable.
   */
  public boolean isReceivable() {
    return equals(BOUND_RX) || equals(BOUND_TRX);
  }

  /**
   * Check whether the session state is not closed.
   *
   * @return {@code true} if session is not closed.
   */
  public boolean isNotClosed() {
    return !equals(CLOSED);
  }
}
