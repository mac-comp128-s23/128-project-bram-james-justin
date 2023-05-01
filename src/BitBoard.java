import java.util.BitSet;

public class BitBoard {
    public BitSet board;
    public long bit;

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

    public BitBoard unMask(BitBoard mask){
        return new BitBoard(bit^mask.bit);
    }

    public boolean checkHor(){
        long m = bit & (bit >> 7);
        long f = m & (m >> 14);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkUpRightDiag(){
        long m = bit & (bit >> 6);
        long f = m & (m >> 12);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkUpLeft(long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 8);
        long f = m & (m >> 16);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkVert(long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 1);
        long f = m & (m >> 2);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkWin(){
        return checkHor()  || checkVert(bit) || checkUpLeft(bit) ||  checkUpRightDiag();
    } //
    /**
     * Changes a bit of a bitboard (i.e., one 42-digit bitstring) from 0 to 1 at a specific column index. Successive
     * additions to the same column are represented at an adjacent index.
     * @param col Column of a bitboard.
     * @return A new bitboard
     * @throws Exception
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

    public long addBitToThisPosition(Long oldMaskbit, long newMaskBit){
        long newBit = bit;
        newBit += newMaskBit ^ oldMaskbit;
        return newBit;
    }

    // check for twos: evaluation methods

    public int checkHorForTwo(){
        long m = bit & (bit >> 7);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 

    public int checkUpRightDiagForTwo(){
        long m = bit & (bit >> 6);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 

    public int checkUpLeftForTwo(){
        long m = bit & (bit >> 8);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 

    public int checkVertForTwo(){
        long m = bit & (bit >> 1);
        BitSet bs = BitSet.valueOf(new long[] {m});
        return bs.cardinality();
    } 

    public int checkTwo(){
        return checkHorForTwo()  +  checkVertForTwo() + checkUpLeftForTwo() +  checkUpRightDiagForTwo();
    }

    public static void main(String[] args) throws Exception {
        BitBoard board = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        board = board.addBitPieceToMask(0);
        
    }
    
}
