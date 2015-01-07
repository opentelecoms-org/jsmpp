package org.jsmpp.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * There were some ongoing bugs related to use of DataInputStream in conjunction
 * with BufferedInputStream. Listed below:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4112757
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4030995
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4401235
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4100022
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4479751
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6192696
 * 
 * This class can be used a drop-in replacement for BufferedInputStream and
 * provides a workaround to the faulty behavior in BufferedInputStream.
 * 
 * Adapted from the comment by steffen.hauptmann@epost.de at the URL below:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4030995
 * 
 * @author Shantanu Kumar (kumar.shantanu@gmail.com)
 * 
 */
public class StrictBufferedInputStream extends BufferedInputStream {

    public StrictBufferedInputStream(final InputStream in) {
        super(in);
    }

    public StrictBufferedInputStream(final InputStream in, final int size) {
        super(in, size);
    }

    /** Workaround for an unexpected behavior of 'BufferedInputStream'! */
    @Override
    public int read(final byte[] buffer, final int bufPos, final int length)
            throws IOException {
        int i = super.read(buffer, bufPos, length);
        if ((i == length) || (i == -1))
            return i;
        int j = super.read(buffer, bufPos + i, length - i);
        if (j == -1)
            return i;
        return j + i;
    }

}