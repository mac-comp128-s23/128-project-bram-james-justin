import java.util.Comparator;

public class ChildSorter implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        if(o1.score > o2.score) {
            return 1;
        } else if (o1.score < o2.score) {
            return -1;
        }
        long previousMaskBit = o1.mask.bit & o2.mask.bit;
        int positionPreferenceO1 = Long.numberOfTrailingZeros(o1.mask.bit ^ previousMaskBit) - 28;
        int positionPreferenceO2 = Long.numberOfTrailingZeros(o2.mask.bit ^ previousMaskBit) - 28;
        return positionPreferenceO1 - positionPreferenceO2;
    }
}
