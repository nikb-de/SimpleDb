package github.nikb.file;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Page {
    private ByteBuffer bb;


    /**
     * charset for interpreting strings
     */
    public static Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Creates a data buffer from a byte buffer
     * @param blocksize the size of the page
     */
    public Page(int blocksize) {
        bb = ByteBuffer.allocateDirect(blocksize);
    }

    /**
     * Creates a log page by wrapping a byte buffer
     * @param b the byte buffer
     */
    public Page(byte[] b) {
        bb = ByteBuffer.wrap(b);
    }

    /** Returns the integer value at a specified offset of the page.
     * @param offset the byte offset within the page
     * @return the integer value at that offset
     */
    public int getInt(int offset) {
        return bb.getInt(offset);
    }


    /**
     * Writes an integer to the specified offset on the page.
     * @param offset the byte offset within the page
     * @param n the integer to be written to the page
     */
    public void setInt(int offset, int n) {
        bb.putInt(offset, n);
    }

    /**
     * Returns the array of bytes starting at the specified offset of the page.
     * @param offset the byte offset within the page
     * @return the array of bytes starting at that offset
     *
     */
    public byte[] getBytes(int offset) {
        bb.position(offset);
        int length = bb.getInt();
        byte[] b = new byte[length];
        bb.get(b);
        return b;
    }

    /**
     * Writes an array of bytes to the specified offset on the page.
     * @param offset the byte offset within the page
     * @param b the bytes to be written to the page
     */
    public void setBytes(int offset, byte[] b) {
        bb.position(offset);
        bb.putInt(b.length);
        bb.put(b);
    }

    /**
     * Returns the string starting at the specified offset of the page.
     * @param offset the byte offset within the page
     * @return the string starting at that offset
     */
    public String getString(int offset) {
        byte[] b = getBytes(offset);
        return new String(b, CHARSET);
    }


    /**
     * Writes a string to the specified offset on the page.
     * @param offset the byte offset within the page
     * @param s the string to be written to the page
     */
    public void setString(int offset, String s) {
        byte[] b = s.getBytes(CHARSET);
        setBytes(offset, b);
    }

/**
     * Returns the maximum number of bytes required to store a string of a given length.
     * @param strlen the size of the string
     * @return the maximum number of bytes required to store it
     */
    public static int maxLength(int strlen) {
        float bytesPerChar = CHARSET.newEncoder().maxBytesPerChar();
        return Integer.BYTES + (strlen * (int)bytesPerChar);
    }

    /**
     * Returns the contents of the page. Required by FileMgr.
     * @return the contents of the page
     */
    ByteBuffer contents() {
        bb.position(0);
        return bb;
    }


}
