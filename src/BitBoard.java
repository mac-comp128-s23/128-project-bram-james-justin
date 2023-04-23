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

    public boolean checkHor( long unmaskedPosition){
        System.out.println(Long.toBinaryString(unmaskedPosition));
        long m = unmaskedPosition & (unmaskedPosition >> 6);
        long f = m & (m >> 12);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkUpLeftDiag(long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 7);
        long f = m & (m >> 14);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkUpRight(long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 5);
        long f = m & (m >> 10);   
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
        return checkHor(bit) || checkUpRight(bit) || checkUpLeftDiag(bit) ||  checkVert(bit);
    } //
    /**
     * Changes a bit of a bitboard (i.e., one 42-digit bitstring) from 0 to 1 at a specific column index. Successive
     * additions to the same column are represented at an adjacent index.
     * @param col Column of a bitboard.
     * @return A new bitboard
     * @throws Exception
     */
    public BitBoard addBitPieceToMask(int col) throws Exception{ // to add to position: yellow.bit += updatedMask.bit ^ oldMask.bit;
        BitSet number = BitSet.valueOf(new long[] {bit}); // check here for issues 
        int count = 0;
        //find height of piece to change
        while(((number.get((col* 6) + count)))){
            count++;
        }
        System.out.println("count: " + count);
        // change one digit of bitstring 
        if(count < 6){   //For some reason the limit in the while loop wasn't working
        long tempBit = bit;
        tempBit += (long) Math.pow(2, (col* (6)) + count); 
        return new BitBoard(tempBit);
        } else {
            throw new Exception("addBitPiece count bound exception");
        }
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
        BitBoard board = new BitBoard(0b000000000000000000000000000000000000000000);

    }
    
}
