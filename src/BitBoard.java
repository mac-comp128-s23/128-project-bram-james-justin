import java.util.BitSet;

public class BitBoard {
    public BitSet board;
    public int bit;

    public BitBoard(int previousBit) {
        board = new BitSet();
        bit = previousBit;


    }

    public int unMask(int mask){
        return bit^mask;
    }

    public boolean checkHor(int unmaskedPosition){
        int m = unmaskedPosition & (unmaskedPosition >> 7);
        int f = m & (m >> 14);   
        if(Integer.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkUpLeftDiag(int unmaskedPosition){
        int m = unmaskedPosition & (unmaskedPosition >> 6);
        int f = m & (m >> 12);   
        if(Integer.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkUpRight(int unmaskedPosition){
        int m = unmaskedPosition & (unmaskedPosition >> 8);
        int f = m & (m >> 16);   
        if(Integer.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkVert(int unmaskedPosition){
        int m = unmaskedPosition & (unmaskedPosition >> 1);
        int f = m & (m >> 2);   
        if(Integer.valueOf(f) > 0){
            return true;
        } 
        return false;
    } 

    public boolean checkWin(int unmaskedPosition){
        if(checkHor(unmaskedPosition) || checkUpLeftDiag(unmaskedPosition)||checkUpRight(unmaskedPosition)||checkVert(unmaskedPosition)){
            return true;
        }
        return false;
    }

    public BitBoard addPiece(int col){
        BitSet number = BitSet.valueOf(new long[] {bit}); // check here for issues 
        int count = 0;
        //find height of piece to change
        while(((number.get((col* 6) + count)) && count < 6)){
            count++;
            System.out.println("count = " + count);
        }
        System.out.println((col* 6) + count);
        // change one digit of bitstring 
        bit += Math.pow(2, (col* (6)) + count); 
        return new BitBoard(bit);
    }

    public static void main(String[] args) {
        BitBoard board = new BitBoard(0b000000000000000000000000000000000000000000);
        board = board.addPiece(2);
        System.out.println(board.bit); 
        System.out.println(Integer.toBinaryString(board.bit));
        board = board.addPiece(2);  
        System.out.println(board.bit); 
        System.out.println(Integer.toBinaryString(board.bit));
      

        
        
        
        // System.out.println(bitBoard);
    }
    
}
