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
package org.jsmpp.bean;

/**
 * @author pmoerenhout
 * @since 3.0.0
 */
public class CancelBroadcastSmResp extends Command {

  private static final long serialVersionUID = -2162306464377501301L;

  public CancelBroadcastSmResp() {
    super();
  }

    /**
     * Null-safe comparison of two objects for equality
     *
     * @param object1 the first object
     * @param object2 the second object
     * @return {@code true} if object1 is the same as object2 argument; {@code false} otherwise.
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }
}
