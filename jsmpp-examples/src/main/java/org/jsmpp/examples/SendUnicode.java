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

import java.nio.charset.StandardCharsets;
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
import org.jsmpp.session.SubmitSmResult;
import org.jsmpp.util.RelativeTimeFormatter;
import org.jsmpp.util.TimeFormatter;

/**
 * This is example to send messages in Unicode format, such as arabic, greek,
 * thai, etc. GSM is able to send in several codings such as 7bit, 8bit and UCS2
 * (16bit). For the examples arabic is using UCS2, so you have to write characters
 * in unicode. Unicode charts can be downloaded from
 * https://www.unicode.org/charts/.
 * 
 * @author uudashr
 * 
 */
public class SendUnicode {
    public static void main(String[] args) throws Exception {
        SMPPSession session = null;
        // 1. initialize
        // 2. Initiate bind
        // 3. Set message receiver listener if needed
        
        // this is how to write "house" in arabic
        String house = "\u0628" + "\u064e" + "\u064a" + 
                        "\u0652" + "\u067a" + "\u064f";
        
        TimeFormatter timeFormatter = new RelativeTimeFormatter();
        
        // 4. Specify the data coding using UCS2
        DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false);
        
        // 5. UCS-2 is subset of the UTF-16BE charset, only the Basic Multilingual Plane codepoints are encodeable as UCS-2
        byte[] data = house.getBytes(StandardCharsets.UTF_16BE);
        
        // 6. Submit the short message
        SubmitSmResult submitSmResult = session.submitShortMessage("CMT",
                TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, 
                "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, 
                "628176504657", new ESMClass(), (byte)0, (byte)1,  
                timeFormatter.format(new Date(System.currentTimeMillis() + 60000)), null,
                new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, 
                dataCoding, 
                (byte)0, data);
    }
}
