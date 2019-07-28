/// Leetcode 785
public class Solution {

    private boolean[] visited;
    private int[] colors;
    private int[][] graph;

    public boolean isBipartite(int[][] graph) {

        this.graph = graph;

        int V = graph.length;
        visited = new boolean[V];
        colors = new int[V];

        for(int v = 0; v < V; v ++)
            if(!visited[v])
                if(!dfs(v, 0)) return false;
        return true;
    }

    private boolean dfs(int v, int color){

        visited[v] = true;
        colors[v] = color;
        for(int w: graph[v])
            if(!visited[w]){
                if(!dfs(w, 1 - color)) return false;
            }
            else if(colors[v] == colors[w])
                return false;
        return true;
    }
}
