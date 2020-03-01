/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.session;

import java.util.Objects;

import org.jsmpp.bean.MessageState;

/**
 * Result of query short message.
 *
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public class QuerySmResult {
	private final String finalDate;
	private final MessageState messageState;
	private final byte errorCode;
	
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final QuerySmResult that = (QuerySmResult) o;
		return errorCode == that.errorCode &&
				Objects.equals(finalDate, that.finalDate) &&
				messageState == that.messageState;
	}

	@Override
	public int hashCode() {
		return Objects.hash(finalDate, messageState, errorCode);
	}
}
