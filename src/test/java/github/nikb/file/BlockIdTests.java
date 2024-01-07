package github.nikb.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockIdTests {

    @Test
    @DisplayName("getFileName should return test")
    void getFileName() {
        BlockId blk = new BlockId("test", 1);
        assertEquals("test", blk.fileName());
    }

    @Test
    @DisplayName("getNumber should return 1")
    void getNumber() {
        BlockId blk = new BlockId("test", 1);
        assertEquals(1, blk.number());
    }

    @Test
    @DisplayName("getEqualsCompare should return true")
    void getEqualsCompare() {
        BlockId blk = new BlockId("test", 1);
        BlockId blk2 = new BlockId("test", 1);
        assertEquals(blk, blk2);
    }

    @Test
    @DisplayName("getEqualsCompare should return false")
    void getEqualsCompareFalse() {
        BlockId blk = new BlockId("test", 1);
        BlockId blk2 = new BlockId("test", 2);
        assertNotEquals(blk, blk2);
    }


    @Test
    @DisplayName("getToString should return [file test, block 1]")
    void getToString() {
        BlockId blk = new BlockId("test", 1);
        assertEquals("[file test, block 1]", blk.toString());
    }

    @Test
    @DisplayName("getHashCode should be equal")
    void getHashCode() {
        BlockId blk = new BlockId("test", 1);
        BlockId blk2 = new BlockId("test", 1);
        assertEquals(blk.hashCode(), blk2.hashCode());
    }

    @Test
    @DisplayName("getHashCode should not be equal")
    void getHashCodeFalse() {
        BlockId blk = new BlockId("test", 1);
        BlockId blk2 = new BlockId("test", 2);
        assertNotEquals(blk.hashCode(), blk2.hashCode());
    }


}