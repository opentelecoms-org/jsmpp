/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsmpp.session;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsmpp.bean.Outbind;

/**
 * @author pmoerenhout
 */
class OutbindRequestReceiver {
  private final Lock lock = new ReentrantLock();
  private final Condition requestCondition = this.lock.newCondition();
  private OutbindRequest request;
  private boolean alreadyWaitForRequest;

  OutbindRequestReceiver() {
  }

  /**
   * Wait until the outbind request received for specified timeout.
   *
   * @param timeout is the timeout in milliseconds.
   * @return the {@link OutbindRequest}.
   * @throws IllegalStateException if this method already called before.
   * @throws TimeoutException      if the timeout has been reach.
   */
  OutbindRequest waitForRequest(long timeout) throws IllegalStateException, TimeoutException {
    this.lock.lock();
    try {
      if (this.alreadyWaitForRequest) {
        throw new IllegalStateException("waitForRequest(long) method already invoked");
      } else if (this.request == null) {
        try {
          this.requestCondition.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      if (this.request != null) {
        return this.request;
      } else {
        throw new TimeoutException("Waiting for outbind request take time too long");
      }
    } finally {
      this.alreadyWaitForRequest = true;
      this.lock.unlock();
    }
  }

  /**
   * Notify that the outbind was accepted.
   *
   * @param outbind is the {@link Outbind} command.
   * @throws IllegalStateException if this method already called before.
   */
  void notifyAcceptOutbind(Outbind outbind) throws IllegalStateException {
    this.lock.lock();
    try {
      if (this.request == null) {
        this.request = new OutbindRequest(outbind);
        this.requestCondition.signal();
      } else {
        throw new IllegalStateException("Already waiting for acceptance outbind");
      }
    } finally {
      this.lock.unlock();
    }
  }
}
