import java.util.BitSet;

public class BitBoard {
    public BitSet board;
    private int bit;

    public BitBoard(int previousBit) {
        board = new BitSet(42);
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

    public void addPiece(int column){

    }

    public static void main(String[] args) {
        

        
        
        
        // System.out.println(bitBoard);
    }
    
}
