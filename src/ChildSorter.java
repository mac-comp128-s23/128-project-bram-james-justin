import java.util.Comparator;

public class ChildSorter implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        if(o1 != null && o2 != null){
            if(o1.getScore() < o2.getScore()) {
                return 1;
            } 
            if (o1.getScore() > o2.getScore()) {
                return -1;
            }
            long previousMaskBit = o1.getMask().getBit() & o2.getMask().getBit();
            int colO1 = Math.abs((Long.numberOfTrailingZeros(o1.getMask().getBit() ^ previousMaskBit) / 7) - 3);
            int colO2 = Math.abs((Long.numberOfTrailingZeros(o2.getMask().getBit() ^ previousMaskBit) / 7 - 3));
            if(colO1 < colO2){
                return 1; 
            } else {
                return colO2;
            }
        } else {
            if(o1 != null) return -1;
            return 1;
        }
    }
}
