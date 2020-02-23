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

import org.jsmpp.bean.MessageState;
import org.jsmpp.util.ObjectUtil;

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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((finalDate == null) ? 0 : finalDate.hashCode());
		result = prime * result + ((messageState == null) ? 0 : messageState.hashCode());
		return result;
	}
	
	private boolean hasEqualFinalDate(QuerySmResult other) {
	    return ObjectUtil.equals(finalDate, other.finalDate);
	}
	
	private boolean hasEqualMessageState(QuerySmResult other) {
	    return ObjectUtil.equals(messageState, other.messageState);
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
		if (!hasEqualFinalDate(other)) {
		    return false;
		}
		if (!hasEqualMessageState(other)) {
		    return false;
		}
		return true;
	}
	
	
}
