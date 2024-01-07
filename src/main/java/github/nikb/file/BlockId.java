package github.nikb.file;


/**
 * An identifier for a disk block.
 * A BlockId object consists of a filename and a block number.
 * It does not hold the contents of the block;
 * instead, that is the job of a {@link Page} object.
 * <p>
 * The {@link #equals} and {@link #hashCode} methods
 * are provided, and so blocks can be used as keys in a map.
 * </p><p>
 * The {@link #toString} method is also provided
 * for debugging purposes.
 * </p>
 */


public class BlockId {

    private String filename;

    private int blknum;

    /**
     * Constructs a block ID for the specified filename and block number.
     * @param filename the name of the file
     * @param blknum the block number
     */
    public BlockId(String filename, int blknum) {
        this.filename = filename;
        this.blknum   = blknum;
    }

    /**
     * Returns the name of the file where the block lives.
     * @return the file name
     */
    public String fileName() {
        return filename;
    }

    /**
     * Returns the location of the block within the file.
     * @return the block number
     */
    public int number() {
        return blknum;
    }

    public boolean equals(Object obj) {
        BlockId blk = (BlockId) obj;
        return filename.equals(blk.filename) && blknum == blk.blknum;
    }

    public String toString() {
        return "[file " + filename + ", block " + blknum + "]";
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
