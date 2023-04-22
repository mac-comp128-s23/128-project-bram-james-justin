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

    public boolean checkWin(int unmaskedPosition){
        if(checkHor(unmaskedPosition) || checkUpLeftDiag(unmaskedPosition)||checkUpRight(unmaskedPosition)||checkVert(unmaskedPosition)){
            return true;
        }
        return false;
    }

    public BitBoard addPiece(int col){
        BitSet number = new BitSet();
        int count = 0;
        while(!((number.get((col* 6) + count)))){
            count++;
            System.out.println(count);
        }
        number.set((col* 6) + count, true);
        
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println(addPiece());

        
        
        
        // System.out.println(bitBoard);
    }
    
}
