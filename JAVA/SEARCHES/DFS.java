import java.util.ArrayList;

public class DFS<T> {

    public static <T> ArrayList<Integer> dfs(ArrayList<ArrayList<Integer>> adj, int source, T target) {
        boolean[] visited = new boolean[adj.size()];
        ArrayList<Integer> traversal = new ArrayList<>();
        dfsRec(adj, visited, source, traversal, target);
        return traversal;
    }
  
    private static <T> void dfsUtil(ArrayList<ArrayList<Integer>> adj, boolean[] visited, int currentNode, ArrayList<Integer> traversal, T target) {
        visited[currentNode] = true;
        traversal.add(currentNode);
      
        for (int i : adj.get(currentNode)) {
            if (!visited[i] && currentNode != target) {
                dfsRec(adj, visited, i, traversal, target);
            }
        }
    }
}
