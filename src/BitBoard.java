import java.util.BitSet;

public class BitBoard {
    public BitSet board;
    public long bit;

    public BitBoard(long previousBit) {
        board = new BitSet();
        bit = previousBit;


    }

    public long unMask(int mask){
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
        while(((number.get((col* 6) + count)))){
            System.out.println("while"+(number.get((col* 6) + count)));
            System.out.println("count =" + count);
            count++;
        }
        // change one digit of bitstring 
        if(count < 6){   //For some reason the limit in the while loop wasn't working
        bit += Math.pow(2, (col* (6)) + count); 
        }
        return new BitBoard(bit);
    }

    public static void main(String[] args) {
        BitBoard board = new BitBoard(0b000000000000000000000000000000000000000000);
        board = board.addPiece(6);  
        System.out.println(board.bit); 
        System.out.println(Long.toBinaryString(board.bit));
        
    }
    
}
