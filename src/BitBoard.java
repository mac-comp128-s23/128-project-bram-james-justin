import java.util.BitSet;

public class BitBoard {
    private BitSet board;
    private long bit;

    public BitBoard(long previousBit) {
        board = new BitSet();
        bit = previousBit;
    }

    /**
     * Updates the current bitboard by performing the XOR operation on the bitstrings (exclusive-or on 0s and 1s in
     * the bitstring).
     * @param newBoard
     * @return
     */
    public long updateBitboard(BitBoard newBoard){
        return newBoard.bit^bit + bit;
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
     * Gets the column based on the location of the first one in the bitstring.
     * @param oldmask
     * @return column number
     */
    public int getColumnUsingNewMask(long oldMaskBit){
        long bitString = oldMaskBit ^ bit;
        areMultipleDigitsDifferent(oldMaskBit);
        int trailingZeroes = Long.numberOfTrailingZeros(bitString);
        return trailingZeroes/7; 
    }

    public void areMultipleDigitsDifferent(long oldBit){
        if((oldBit ^ bit) >> Long.numberOfTrailingZeros(oldBit ^ bit) > 1) {
            throw new RuntimeException(); 
        }
    }
    
    /**
     * Gets a bit from a bitboard.
     * @return
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
     * Adds a bit to a BitBoard and returns the new Bitboard.
     * @param oldMaskbit
     * @param newMaskBit
     * @return
     */
    public long addBitToThisPosition(Long oldMaskbit, long newMaskBit){
        long newBit = bit;
        newBit += newMaskBit ^ oldMaskbit;
        return newBit;
    }

    /**
     * Checks for a horizontal connect 4.
     * @return
     */
    public boolean checkHor(){
        long m = bit & (bit >> 7);
        long f = m & (m >> 14);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    /**
     * Checks for a upwards right diagonal connect 4.
     * @return
     */
    public boolean checkUpRightDiag(){
        long m = bit & (bit >> 6);
        long f = m & (m >> 12);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 
    /**
     * Checks for a upwards left diagonal connect 4.
     * @return
     */
    public boolean checkUpLeft(long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 8);
        long f = m & (m >> 16);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 
    /**
     * Checks for a vertical connect 4.
     * @return
     */
    public boolean checkVert(long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 1);
        long f = m & (m >> 2);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    /**
     * Checks for connect 4.
     * @return
     */
    public boolean checkWin(){
        return checkHor()  || checkVert(bit) || checkUpLeft(bit) ||  checkUpRightDiag();
    } //

    /**
     * Changes a bit of a bitboard (i.e., one 42-digit bitstring) from 0 to 1 at a specific column index. Successive
     * additions to the same column are represented at an adjacent index.
     * @param col Column of a bitboard.
     * @return A new bitboard
     */
    public BitBoard addBitPieceToMask(int col) { // to add to position: x += updatedMask.bit ^ oldMask.bit;
        BitSet number = BitSet.valueOf(new long[] {bit}); // check here for issues 
        int count = 0;
        //find height of piece to change
        while(number.get((col* 7) + count) && count < 6){
            count++;
        }
        // change one digit of bitstring 
        long tempBit = bit;
        // if (count < 6){   //For some reason the limit in the while loop wasn't working
            tempBit += (long) Math.pow(2, (col* (7)) + count); 
            // System.out.println("coutn: " + count);
        // }    
        return new BitBoard(tempBit);
        
    }
    
   
 
    /**
     * Gets the number of horizontal conect two's.
     * @return
     */
    public int numberOfTwoHorizontals(){
        long m = bit & (bit >> 7);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 
    /**
     * Gets the number of right diagonal conect two's.
     * @return
     */
    public int numberOfTwoRightDiagonals(){
        long m = bit & (bit >> 6);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 
    /**
     * Gets the number of left diagonal conect two's.
     * @return
     */
    public int numberOfTwoLeftDiagonals(){
        long m = bit & (bit >> 8);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 
    /**
     * Gets the number of vertical conect two's.
     * @return
     */
    public int numberOfTwoVerticals(){
        long m = bit & (bit >> 1);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 
    /**
     * Gets the number of connect twos in the BitBoard. This is used for scoring purposes.
     * @return
     */
    public int checkNumberOfTwos(){
        return numberOfTwoHorizontals()  +  numberOfTwoVerticals() + numberOfTwoLeftDiagonals() +  numberOfTwoRightDiagonals();
    }

    public static void main(String[] args){
        BitBoard board = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        board = board.addBitPieceToMask(0);
        
    }
    
}
