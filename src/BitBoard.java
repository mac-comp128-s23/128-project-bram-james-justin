import java.util.BitSet;

public class BitBoard {
    public BitSet board;
    public long bit;

    public BitBoard(long previousBit) {
        board = new BitSet();
        bit = previousBit;
    }

    public long updateBitboard(BitBoard newBoard){
        return newBoard.bit^bit + bit;
    }

    public BitBoard unMask(BitBoard mask){
        return new BitBoard(bit^mask.bit);
    }

    public boolean checkHor( long unmaskedPosition){
        long m = unmaskedPosition & (unmaskedPosition >> 6);
        long f = m & (m >> 12);   
        if(Long.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    // public boolean checkUpLeftDiag(long unmaskedPosition){
    //     long m = unmaskedPosition & (unmaskedPosition >> 6);
    //     long f = m & (m >> 12);   
    //     if(Long.valueOf(f) > 0){
    //         return true;
    //     } 
    //     return false;
    // } 

    // public boolean checkUpRight(long unmaskedPosition){
    //     long m = unmaskedPosition & (unmaskedPosition >> 5);
    //     long f = m & (m >> 10);   
    //     if(Long.valueOf(f) > 0){
    //         return true;
    //     } 
    //     return false;
    // } 

    // public boolean checkVert(long unmaskedPosition){
    //     long m = unmaskedPosition & (unmaskedPosition >> 1);
    //     long f = m & (m >> 2);   
    //     if(Long.valueOf(f) > 0){
    //         return true;
    //     } 
    //     return false;
    // } 

    public boolean checkWin(){
        return checkHor(bit);   //|| checkUpLeftDiag(unmaskedPosition)||checkUpRight(unmaskedPosition)||checkVert(unmaskedPosition)){;
    }

    public BitBoard addBitPiece(int col){
        BitSet number = BitSet.valueOf(new long[] {bit}); // check here for issues 
        int count = 0;
        //find height of piece to change
        while(((number.get((col* 6) + count)))){
            count++;
        }
        System.out.println("count: " + count);
        // change one digit of bitstring 
        if(count < 6){   //For some reason the limit in the while loop wasn't working
        bit += Math.pow(2, (col* (6)) + count); 
        }
        return new BitBoard(bit);
    }

    public static void main(String[] args) {
        BitBoard board = new BitBoard(0b000000000000000000000000000000000000000000);
        board = board.addBitPiece(6);  
        System.out.println(board.bit); 
        System.out.println(Long.toBinaryString(board.bit));
        
    }
    
}
