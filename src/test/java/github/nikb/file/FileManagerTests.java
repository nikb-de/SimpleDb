package github.nikb.file;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTests {

    String testFolder = "test_folder";

    @BeforeAll
    static void setup() {
        // Create a test folder if it does not exist
        File testFolder = new File("test_folder");
        if (!testFolder.exists()) {
            testFolder.mkdir();
        }
        else {
            // Delete all files in the test folder
            for (File file : Objects.requireNonNull(testFolder.listFiles())) {
                file.delete();
            }
        }
        File existingFile = new File(testFolder + "/existing_file");
        if (!existingFile.exists()) {
            existingFile.mkdir();
        }


    }


    // Initializes the file manager with a valid directory and buffer size
    @Test
    public void test_initializeFileManagerWithValidDirectoryAndBufferSize() {
        File dbDirectory = new File(testFolder + "/existing_file");
        int blockSize = 1024;

        FileManager fileManager = new FileManager(dbDirectory, blockSize);

        assertEquals(dbDirectory, fileManager.getDbDirectory());
        assertEquals(blockSize, fileManager.getBlockSize());
        assertFalse(fileManager.isNew());
    }

    // Initializes the file manager with a new directory and buffer size
    @Test
    public void test_initializeFileManagerWithNewDirectoryAndBufferSize() {
        File dbDirectory = new File(testFolder+ "/new_file");
        int blockSize = 512;

        FileManager fileManager = new FileManager(dbDirectory, blockSize);

        assertEquals(dbDirectory, fileManager.getDbDirectory());
        assertEquals(blockSize, fileManager.getBlockSize());
        assertTrue(fileManager.isNew());
        assertTrue(dbDirectory.exists());
    }

    // Reads a block from an existing file
    @Test
    public void test_readBlockFromExistingFile() {
        File dbDirectory = new File(testFolder+ "/existing_file");
        int blockSize = 2048;
        FileManager fileManager = new FileManager(dbDirectory, blockSize);


        BlockId blk = new BlockId("existing_file", 0);
        Page page = new Page(blockSize);
        fileManager.read(blk, page);
        assertNotNull(page);
        // Add more assertions to validate the content of the page if necessary
        // put 123 in the first 4 bytes of block
        page.setInt(0, 123);
        // write the page back to disk
    }

    @Test
    public void test_throwsExceptionIfPageContentsIsNull() {
        File dbDirectory = new File("testDirectory");
        int blockSize = 2048;
        FileManager fileManager = new FileManager(dbDirectory, blockSize);
        BlockId blk = new BlockId("testFile", 0);
        Page page = null;
        // Assert that a RuntimeException is thrown with the expected message
        assertThrows(RuntimeException.class, () -> fileManager.read(blk, page), "Page contents is null");
    }

    // Initializes the file manager with a null directory
    @Test
    public void test_initializeFileManagerWithNullDirectory() {
        File dbDirectory = null;
        int blockSize = 1024;

        assertThrows(NullPointerException.class, () -> new FileManager(dbDirectory, blockSize));
    }

    // Initializes the file manager with a negative buffer size
    @Test
    public void test_initializeFileManagerWithNegativeBufferSize() {
        File dbDirectory = new File(testFolder + "/buffer_file");
        int blockSize = -512;
        assertThrows(IllegalArgumentException.class, () -> new FileManager(dbDirectory, blockSize));
    }

    // Initializes the file manager with a directory that does not exist and cannot be created
    @Test
    public void test_initializeFileManagerWithNonexistentDirectory() {
        File dbDirectory = new File(testFolder + "/nonexistent_file");
        int blockSize = 1024;
        assertThrows(RuntimeException.class, () -> new FileManager(dbDirectory, blockSize, false));
    }


    @Test
    public void test_throws_exception_when_page_contents_is_null() {
        FileManager fileManager = new FileManager(new File("dbDirectory"), 4096);
        BlockId blk = new BlockId("filename", 1);
        Page page = null;

        var e = assertThrows(RuntimeException.class, () -> fileManager.write(blk, page));
        assertEquals("Page contents is null", e.getMessage());
    }


    @Test
    public void test_writeMultiplePagesToSameFile() {
        File dbDirectory = new File("same_file");
        int blockSize = 2048;
        FileManager fileManager = new FileManager(dbDirectory, blockSize);

        BlockId blk1 = new BlockId("same_file", 0);
        Page page1 = new Page(blockSize);
        fileManager.write(blk1, page1);

        BlockId blk2 = new BlockId("same_file", 1);
        Page page2 = new Page(blockSize);
        fileManager.write(blk2, page2);

        // Add assertions to validate that both pages are written to the same file
        // and that the contents of the pages are correct
        Page page3 = new Page(blockSize);
        fileManager.read(blk1, page3);
        assertEquals(page1, page3);

        Page page4 = new Page(blockSize);
        fileManager.read(blk2, page4);
        assertEquals(page2, page4);
    }

}