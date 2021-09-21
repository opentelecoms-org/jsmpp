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
package org.jsmpp.unicode;

import static org.testng.Assert.*;

import java.nio.charset.StandardCharsets;

import org.testng.annotations.Test;

/**
 * This is test case for arabic unicode. Some resources taken from Internet.
 * 
 * @author uudashr
 * @see <a href="http://java.sun.com/docs/books/tutorial/i18n/text/char.html">Java Tutorials - Character boundaries</a>
 */
public class ArabicTest {
    
    @Test
    public void writeChars() throws Exception {
        // house in arabic
        String house = "\u0628" + "\u064e" + "\u064a" + 
                        "\u0652" + "\u067a" + "\u064f";

        assertEquals(house.length(), 6);
        
        // we are using UTF-16BE charset for UCS2
        byte[] bytes = house.getBytes(StandardCharsets.UTF_16BE);
        assertEquals(bytes.length, 12);
        
        /*
        JFrame frame = new JFrame("Arabic Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(house));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        Thread.sleep(5000);
        */
    }
}
