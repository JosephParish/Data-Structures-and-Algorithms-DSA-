//ToDo
// 1. implement custom comparator
// 2. get the function to compare all subsequent choices until chosen depth
// 3. BackTracking

public class MINMAX<T> {

    private final int maxDepth;
    private final ArrayList<ArrayList<T>> adjStates; // Adjacency of states making up a tree (has to be in tree form, NO LOOPS)
    // scores is the tree in form {1, a, 2, 3, b, c, 4, 5}
    
    private final Function<T, Double> fitFunc; // fitness function
    private final Comparator<T> fitComp; // fitness comparator

    public MINMAX(ArrayList<ArrayList<T>> adjstates, int maxDepth, Function<T, Double> fitFunc) {
        this.maxDepth = maxDepth;
        this.adjstates = adjstates;
        
        this.fitFunc = fitFunc;
        this.fitComp = Comparator.comparing(fitFunc).reversed();
    }
	
    public T minMaxSearch(int depth, int currentIndex, boolean isMax) {
		ArrayList<T> optimalChoice = new ArrayList<>(maxDepth);
		for (T neighbor : adjStates.get(currentIndex)) {
            if (isMax) {
            	return Math.max(minMaxUtil(depth+1, currentIndex*2, false), minMaxUtil(depth+1, currentIndex*2 + 1, false));
        	} else {
            	return Math.min(minMaxUtil(depth+1, currentIndex*2, true), minMaxUtil(depth+1, currentIndex*2 + 1, true));
        	}
        }
        // needs to change math.max to a generic type evaluation function passed to it in parameters
    }

	public T minMaxUtil (int depth, int currentIndex, boolean isMax) {
        if (depth == maxDepth) {
            return adjStates.get(currentIndex);
        }
		
        // needs to change math.max to a generic type evaluation function passed to it in parameters
        if (isMax) {
            return Math.max(minMaxUtil(depth+1, currentIndex*2, false), minMaxUtil(depth+1, currentIndex*2 + 1, false));
        } else {
            return Math.min(minMaxUtil(depth+1, currentIndex*2, true), minMaxUtil(depth+1, currentIndex*2 + 1, true));
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

	for (T neighbor : adj.get(currentIndex)) {
            int neighborIndex = adj.indexOf(neighbor);
            if (!visited.get(neighborIndex)) {
                dfsUtil(adj, visited, neighbor, traversal, target);
            }
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

