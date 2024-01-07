package github.nikb.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import static github.nikb.file.Page.maxLength;
import static org.junit.jupiter.api.Assertions.*;

public class PageTests {


    @Test
    @DisplayName("getInt 0")
    void getInt() {
        Page page = new Page(4096);
        assertEquals(0, page.getInt(0));
    }

    @Test
    @DisplayName("getInt 12")
    void getInt12() {
        Page page = new Page(4096);
        assertEquals(0, page.getInt(12));
    }



    @ParameterizedTest(name = "getInt {0} when setInt with offset {1}")
    @CsvSource({ "1, 0", "2, 1", "3, 2", "4, 3" })
    void getIntWhenSetIntWithOffset(int expected, int offset) {
        Page page = new Page(4096);
        page.setInt(offset, expected);
        assertEquals(expected, page.getInt(offset));
    }

    @Test
    @DisplayName("getBytes 0")
    void getBytes() {
        Page page = new Page(4096);
        assertEquals(0, page.getBytes(0).length);
    }

    @Test
    @DisplayName("getBytes 12 when setBytes with offset 10")
    void getBytes12WhenSetBytesWithOffset10() {
        Page page = new Page(4096);
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        page.setBytes(10, bytes);
        byte[] res = page.getBytes(10);
        assertEquals(2, res.length);
        assertEquals(1, res[0]);
        assertEquals(2, res[1]);
    }

    @Test
    @DisplayName("getBytes raises exception when offset is incorrect")
    void getBytesRaisesExceptionWhenOffsetIsIncorrect() {
        Page page = new Page(4096);
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        assertThrows(BufferOverflowException.class, () -> {
            page.setBytes(4096, bytes);
        });
    }

    @Test
    @DisplayName("setBytes three times and getBytes")
    void setBytesThreeTimesAndGetBytes() {
        Page page = new Page(4096);
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        page.setBytes(10, bytes);
        page.setBytes(20, bytes);
        page.setBytes(30, bytes);
        byte[] res = page.getBytes(10);
        assertEquals(2, res.length);
        assertEquals(1, res[0]);
        assertEquals(2, res[1]);
        res = page.getBytes(20);
        assertEquals(2, res.length);
        assertEquals(1, res[0]);
        assertEquals(2, res[1]);
        res = page.getBytes(30);
        assertEquals(2, res.length);
        assertEquals(1, res[0]);
        assertEquals(2, res[1]);
    }

    @Test
    @DisplayName("setBytes overwrites correctly")
    void setBytesOverwritesCorrectly() {
        Page page = new Page(4096);
        byte[] bytes = new byte[2];
        bytes[0] = 1;
        bytes[1] = 2;
        page.setBytes(10, bytes);
        bytes[0] = 3;
        bytes[1] = 4;
        page.setBytes(10, bytes);
        byte[] res = page.getBytes(10);
        assertEquals(2, res.length);
        assertEquals(3, res[0]);
        assertEquals(4, res[1]);
    }


    @Test
    @DisplayName("getString 0")
    void getString() {
        Page page = new Page(4096);
        assertEquals("", page.getString(0));
    }

    @Test
    @DisplayName("getString 12 when setString with offset 10")
    void getString12WhenSetStringWithOffset10() {
        int strLen = 2;
        assertEquals(13, maxLength(3));
    }

    @Test
    @DisplayName("contents returned correctly")
    void contentsReturnedCorrectly() {
        Page page = new Page(4096);
        byte[] bytes = new byte[4];
        bytes[0] = 3;
        bytes[1] = 4;
        bytes[2] = 5;
        bytes[3] = 6;
        page.setBytes(12, bytes);


        ByteBuffer contents = page.contents();
        assertEquals(4096, contents.capacity());
        assertEquals(4096, contents.limit());
        for (int i = 0; i < 30; i++) {
            System.out.println(i + ": " + contents.get(i));
        }

        assertEquals(4, contents.getInt(12 ));
        assertEquals(3, contents.get(16));
        assertEquals(4, contents.get(17));
        assertEquals(5, contents.get(18));
        assertEquals(6, contents.get(19));




    }


}