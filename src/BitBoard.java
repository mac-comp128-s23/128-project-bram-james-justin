import java.util.BitSet;

public class BitBoard {
    private long bit;

    /**
     * Creates a Binary representation of the entire board. The bottom left positon of the board
     * is the rightmost digit in the bitString. The top right position of the visual board is the 48 spot. 
     * The 49th is in the sentinel row. Every 7 digits there is a sentinel row that cannot be placed in.
     * @param bitString binary representation of the entire board.
     */
    public BitBoard(long bitString) {
        bit = bitString;
    }

    /**
     * Creates a new BitBoard by unmasking the bits that are set in the given mask.
     * @param mask the mask used for unmasking
     * @return the unmasked BitBoard
     */
    public BitBoard unMask(BitBoard mask){
        return new BitBoard(bit^mask.bit);
    }

    /**
     * Compares this mask to old mask to find the newest placed bitpiece's column number as an int
     * @param previousPosition
     * @return column number
     */
    public int getColUsingBitBoard(long previousPosition){
        long bitString = previousPosition ^ bit;
        int trailingZeroes = Long.numberOfTrailingZeros(bitString);
        return trailingZeroes/7; 
    }
    
    /**
     * Gets a bit from a BitBoard.
     * @return bit
     */
    public long getBit(){
        return this.bit;
    }

    /**
     * Sets a bit to newly passed in bit. Used for updating the yellow position in the updateGameState method.
     * @param newLong
     */
    public void setBit(long newLong){
        this.bit = newLong;
    }

    /**
     * Adds a bit to a BitBoard and returns the new BitBoard.
     * @param oldPositionBit The old bit position from previous move
     * @param newPositionBit The future bit position created by a move
     * @return 
     */
    public long addBitToPos(Long oldPositionBit, long newPositionBit){
        Long newBit = bit | (newPositionBit ^ oldPositionBit);
        return newBit;
    }

    /**
     * Shifts bits to check for a horizontal connect 4.
     * @return
     */
    private boolean checkHor(){
        long lineUpBit1 = bit & (bit >> 7);
        long lineUpBit2 = lineUpBit1 & (lineUpBit1 >> 14);   
        if(Long.valueOf(lineUpBit2) > 0){
            return true;
        } 
        return false;
    } 

    /**
     * Shifts bits to check for a upwards right diagonal connect 4.
     * @return
     */
    private boolean checkUpRightDiag(){
        long lineUpBit1 = bit & (bit >> 6);
        long lineUpBit2 = lineUpBit1 & (lineUpBit1 >> 12);   
        if(Long.valueOf(lineUpBit2) > 0){
            return true;
        } 
        return false;
    } 
    /**
     * Shifts bits to check for a upwards left diagonal connect 4.
     * @return
     */
    private boolean checkUpLeft(long unmaskedPosition){
        long lineUpBit1 = unmaskedPosition & (unmaskedPosition >> 8);
        long lineUpBit2 = lineUpBit1 & (lineUpBit1 >> 16);   
        if(Long.valueOf(lineUpBit2) > 0){
            return true;
        } 
        return false;
    } 

    /**
     * Shifts bits to check for a vertical connect 4.
     * @return
     */
    private boolean checkVert(long unmaskedPosition){
        long lineUpBit1 = unmaskedPosition & (unmaskedPosition >> 1);
        long lineUpBit2 = lineUpBit1 & (lineUpBit1 >> 2);   
        if(Long.valueOf(lineUpBit2) > 0){
            return true;
        } 
        return false;
    } 

    /**
     * Checks for connect 4.
     * @return true if connect 4 exists.
     */
    public boolean checkWin(){
        return checkHor() || checkVert(bit) || checkUpLeft(bit) || checkUpRightDiag();
    } 

    /**
     * Changes a bit of a BitBoard (i.e., one 42-digit bitstring) from 0 to 1 at a specific column index. Successive
     * additions to the same column are represented at an adjacent index.
     * @param col Column of a BitBoard.
     * @return A new BitBoard
     */
    public BitBoard addBitToMask(int col) {
        BitSet number = BitSet.valueOf(new long[] {bit});
        int count = 0;
        while(number.get((col* 7) + count) && count < 6){
            count++;
        }
        long tempBit = bit;
        tempBit += (long) Math.pow(2, (col* (7)) + count);   
        
        return new BitBoard(tempBit);
    }
 
    /**
     * Shifts bits to get the number of horizontal conect two's.
     * @return 
     */
    private int numOfTwoHoriz(){
        long lineUpBit = bit & (bit >> 7);
        BitSet bs = BitSet.valueOf(new long[] {lineUpBit});
        return bs.cardinality();
    } 

    /**
     * Shifts bits to get the number of right diagonal conect two's.
     * @return
     */
    private int numOfTwoRightDiag(){
        long lineUpBit = bit & (bit >> 6);
        BitSet bs = BitSet.valueOf(new long[] {lineUpBit});
        return bs.cardinality();
    } 

    /**
     * Shifts bits to get the number of left diagonal conect two's.
     * @return
     */
    private int numOfTwoLeftDiag(){
        long lineUpBit = bit & (bit >> 8);
        BitSet bs = BitSet.valueOf(new long[] {lineUpBit});
        return bs.cardinality();
    } 

    /**
     * Shifts bits to get the number of vertical conect two's.
     * @return
     */
    private int numOfTwoVert(){
        long lineUpBit = bit & (bit >> 1);
        BitSet bs = BitSet.valueOf(new long[] {lineUpBit});
        return bs.cardinality();
    } 

    /**
     * Gets the number of connect twos in the BitBoard. This is used for scoring purposes.
     * @return
     */
    public int checkTwos(){
        return numOfTwoHoriz() + numOfTwoVert() + numOfTwoLeftDiag() + numOfTwoRightDiag();
    }

    /**
     * Creates a grid representation of the board to help with debugging.
     * Credit to Bret Jackson for helping us create this method.
     */
    public String toString(){
        String asBinary = Long.toBinaryString(bit);
        while (asBinary.length() != 64)
            asBinary = "0" + asBinary;
        String s = asBinary +"\n"+
         "    a   b   c   d   e   f   g\n";
        s += "  +---+---+---+---+---+---+---+\n 7 | ";
        for (int row = 6; row >= 0; row--) {
            for (int col = 0; col < 7; col++) {
                if (asBinary.charAt(63 - (col * 7 + row)) == '1')
                    s += "X | ";
                else
                    s += "  | ";
            }
            s += (row + 1) + "\n   +---+---+---+---+---+---+---+";
            if (row != 0)
                s += "\n " + row + " | ";
        }
        s += "\n     a   b   c   d   e   f   g\n";
        return s;
    }
    
}
