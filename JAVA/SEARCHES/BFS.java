public class BFS {

  public static int[] bfs(int[][] adj) {
    
        int s = 0;
        ArrayList<Integer> traversal = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[adj.length()];
        
        visited[s] = true;
        queue.add(s);
        
        while (!queue.isEmpty()) {
        
            int curr = queue.poll();
            traversal.add(curr);
            
            for (int x : adj[curr]) {
                if (!visited[x]) {
                    visited[x] = true;
                    queue.add(x);
                }
            }
        }
    
        return traversal;
  }
}
