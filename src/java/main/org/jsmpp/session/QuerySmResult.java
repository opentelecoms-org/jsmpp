package org.jsmpp.session;

import org.jsmpp.bean.MessageState;

/**
 * Result of query short message.
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public class QuerySmResult {
	// TODO uud: change using Date instead of String.
	private String finalDate;
	private MessageState messageState;
	private byte errorCode;
	
	public QuerySmResult(String finalDate, MessageState messageState, byte errorCode) {
		this.finalDate = finalDate;
		this.messageState = messageState;
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public byte getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the finalDate
	 */
	public String getFinalDate() {
		return finalDate;
	}

	/**
	 * @return the messageState
	 */
	public MessageState getMessageState() {
		return messageState;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((finalDate == null) ? 0 : finalDate.hashCode());
		result = PRIME * result + ((messageState == null) ? 0 : messageState.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuerySmResult other = (QuerySmResult) obj;
		if (errorCode != other.errorCode)
			return false;
		if (finalDate == null) {
			if (other.finalDate != null)
				return false;
		} else if (!finalDate.equals(other.finalDate))
			return false;
		if (messageState == null) {
			if (other.messageState != null)
				return false;
		} else if (!messageState.equals(other.messageState))
			return false;
		return true;
	}
	
	
}
