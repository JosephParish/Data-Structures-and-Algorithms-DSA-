public class MINMAX<T> {

    private final int maxDepth;
    private final ArrayList<T> adjStates; // Adjacency of states making up a tree (has to be in tree form, NO LOOPS)
    // scores is the tree in form {1, a, 2, 3, b, c, 4, 5}
    
    private final Function<T, T> mFunc; // mutation function
    private final Comparator<T> fitComp; // fitness comparator

    public MINMAX(ArrayList<T> states, int maxDepth, Function<T, Double> fitFunc) {
        this.maxDepth = maxDepth;
        this.states = states
        
        this.fitFunc = fitFunc;
        this.fitComp = Comparator.comparing(fitFunc).reversed();
    }

    // as is this is a recursive function that returns the last found value from maxing and then nimimising but does not backtrack, does not use arraylists and only uses pre made scores
    public T minMaxSearch(int depth, int nodeIndex, boolean isMax, int[] scores) {
        // needs to change scores to a generic type array/list
        if (depth == maxDepth) {
            return scores[nodeIndex];
        }
        // needs to change math.max to a generic type evaluation function passed to it in parameters
        if (isMax) {
            return Math.max(minimax(depth+1, nodeIndex*2, false, scores), minimax(depth+1, nodeIndex*2 + 1, false, scores));
        } else {
            return Math.min(minimax(depth+1, nodeIndex*2, true, scores), minimax(depth+1, nodeIndex*2 + 1, true, scores));
        }
    }

    ////////////////////////////////////////////////////////////// BELOW IS FOR AID IN CREATION

    // for use in traversing tree and comparing neighbours
    private static <T> boolean dfsUtil(ArrayList<ArrayList<T>> adj, ArrayList<Boolean> visited, T currentNode, ArrayList<T> traversal, T target) {
        
        int currentIndex = adj.indexOf(currentNode);
        if (currentIndex == -1) return false;
        
        visited.set(currentIndex, true);
        traversal.add(currentNode);
        
        if (currentNode.equals(target)) {
            return true;
        }

        for (T neighbor : adj.get(currentIndex)) {
            int neighborIndex = adj.indexOf(neighbor);
            if (!visited.get(neighborIndex)) {
                dfsUtil(adj, visited, neighbor, traversal, target);
            }
        }

        return false;
    }

    // for use in using comparitive functions
    public static int hillSearch(Function<T, T> fitness, Function<T> get_neighbors, T start) {
	  T x = start; 
	  while (true) {
		List<T> neighbors = get_neighbors(x);
		T best_neighbor = Collections.max(neighbors,Comparator.comparingInt(fitness::apply)); 
		if (fitness.apply(best_neighbor) <= fitness.apply(x)) {
			return x;
      		}
		x = best_neighbor;
	  }
  }
}
