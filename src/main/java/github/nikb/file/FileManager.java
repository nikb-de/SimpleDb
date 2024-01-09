package github.nikb.file;

/* TODO: Check if NIO is better than IO */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileManager {
    /**
     * The database directory.
     */
    private File dbDirectory;

    /**
     * The block size of pages in the database.
     */
    private int blockSize;

    /**
     * Check if the database is new and needs initializing.
     */
    private boolean isNew;

    /**
     * A map of open files.
     */
    public Map<String, RandomAccessFile> openFiles = new HashMap<>();


    /**
     * Initializes the file manager for the specified database and buffer size.
     * @param dbDirectory the name of the directory that holds the database
     * @param blockSize the size of buffers in the buffer pool
     * @param isMkDir if true, create the directory if it does not exist
     */

    public FileManager(File dbDirectory, int blockSize, boolean isMkDir) {
        /* TODO: refactor to Builder pattern */
        this.dbDirectory = Objects.requireNonNull(dbDirectory);
        if (blockSize < 0)
            throw new IllegalArgumentException("negative block size");
        this.blockSize = blockSize;

        this.isNew = !dbDirectory.exists();
        if (isNew) {
            if (isMkDir) {
                dbDirectory.mkdir();
            }
            else {
                throw new IllegalArgumentException("database directory does not exist");
            }
        }



        for (String filename : Objects.requireNonNull(dbDirectory.list()))
            if (filename.startsWith("temp"))
                new File(dbDirectory, filename).delete();
    }

    /**
     * Initializes the file manager for the specified database and buffer size. Creates the directory if it does not exist.
     * @param dbDirectory the name of the directory that holds the database
     * @param blockSize the size of buffers in the buffer pool
     */
    public FileManager(File dbDirectory, int blockSize) {
        this(dbDirectory, blockSize, true);
    }


    /**
     * Reads the contents of a disk block into a page.
     * @param blk a reference to a disk block
     * @param page the page to be read
     */
    public synchronized void read(BlockId blk, Page page) {
        try {
            RandomAccessFile f = getFile(blk.fileName());
            f.seek((long) blk.number() * blockSize);
            f.getChannel().read(Objects.requireNonNull(page.contents()));
        } catch (NullPointerException e) {
            throw new RuntimeException("Page contents is null");
        }
        catch (Exception e) {
            throw new RuntimeException("cannot read block " + blk);
        }
    }



    public synchronized void write(BlockId blk, Page page) {
        try {
            RandomAccessFile f = getFile(Objects.requireNonNull(blk, "File name is null").fileName());
            f.seek((long) blk.number() * blockSize);
            f.getChannel().write(Objects.requireNonNull(page, "Page contents is null").contents());
        }
        catch (NullPointerException e) {
            throw new RuntimeException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("cannot write block" + blk);
        }
    }


    /**
     * Get the file length in blocks.
     * @param filename the name of the file
     * @return the number of blocks in the file
     */
    public int length(String filename) {
        try {
            RandomAccessFile f = getFile(filename);
            return (int) (f.length() / blockSize);
        } catch (IOException e) {
            throw new RuntimeException("cannot access " + filename);
        }
    }


    public boolean isNew() {
        return isNew;
    }

    public int getBlockSize() {
        return blockSize;
    }


    public File getDbDirectory() {
        return dbDirectory;
    }

    private RandomAccessFile getFile(String filename) throws IOException {
        RandomAccessFile f = openFiles.get(filename);
        if (f == null) {
            File dbTable = new File(dbDirectory, filename);
            f = new RandomAccessFile(dbTable, "rws");
            openFiles.put(filename, f);
        }
        return f;

    }


}
