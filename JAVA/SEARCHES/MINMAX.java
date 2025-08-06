public class MINMAX<T> {

    public MINMAX() {
    }

    public T minMaxSearch(int depth, int nodeIndex, boolean isMax, int[] scores, int maxDepth) {
        // needs to change scores to a generic type array/list
        if (depth == maxDepth) {
            return scores[nodeIndex];
        }
        // needs to change math.max to a generic type evaluation function passed to it in parameters
        if (isMax) {
            return Math.max(minimax(depth+1, nodeIndex*2, false, scores, maxDepth), minimax(depth+1, nodeIndex*2 + 1, false, scores, maxDepth));
        } else {
            return Math.min(minimax(depth+1, nodeIndex*2, true, scores, maxDepth), minimax(depth+1, nodeIndex*2 + 1, true, scores, maxDepth));
        }
    }
}
