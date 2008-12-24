package org.jsmpp.unicode;

import static org.testng.Assert.*;

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
        byte[] bytes = house.getBytes("UTF-16BE");
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
