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
package org.jsmpp.examples;

import java.util.Date;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.RelativeTimeFormatter;
import org.jsmpp.util.TimeFormatter;

/**
 * This is example to send message in unicode format, such as arabic, greek,
 * thai, etc. GSM able to send in several coding such as 7bit, 8bit and UCS6
 * (16bit). For the examples arabic using 16bit, so you have to write characters
 * on unicode. Unicode charts can be download from
 * http://www.unicode.org/charts/.
 * 
 * @author uudashr
 * 
 */
public class SendUnicode {
    public static void main(String[] args) throws Exception {
        SMPPSession session = null; // 1. initialize
        // 2. Initiate bind
        // 3. Set message receiver listener if needed
        
        // this is how to write "house" in arabic
        String house = "\u0628" + "\u064e" + "\u064a" + 
                        "\u0652" + "\u067a" + "\u064f";
        
        TimeFormatter timeFormatter = new RelativeTimeFormatter();
        
        
        // 4. Specify the data coding using UCS2
        DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false);
        
        // 5. UTF-16BE is equals to UCS2
        byte[] data = house.getBytes("UTF-16BE");
        
        // 6. Submit the short message
        String messageId = session.submitShortMessage("CMT", 
                TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, 
                "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, 
                "628176504657", new ESMClass(), (byte)0, (byte)1,  
                timeFormatter.format(new Date()), null, 
                new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, 
                dataCoding, 
                (byte)0, data);
    }
}
