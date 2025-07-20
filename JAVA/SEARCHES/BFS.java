public class BFS {

  public static int[] bfs(int[][] adjacencyList) {
    
        int verticies = adjacencyList.length();
        int s = 0;
        ArrayList<Integer> traversal = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[verticies];
        
        visited[s] = true;
        queue.add(s);
        
        while (!queue.isEmpty()) {
        
            int curr = queue.poll();
            traversal.add(curr);
            
            for (int x : adjacencyList[curr]) {
                if (!visited[x]) {
                    visited[x] = true;
                    queue.add(x);
                }
            }
        }
    
        return traversal;
  }
}
