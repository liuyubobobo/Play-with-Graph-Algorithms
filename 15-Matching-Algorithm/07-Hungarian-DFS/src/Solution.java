import java.util.*;

public class Solution {

    public class Graph implements Cloneable{

        private int V;
        private int E;
        private TreeSet<Integer>[] adj;
        private boolean directed;
        private int[] indegrees, outdegrees;

        public Graph(int V, boolean directed){
            this.V = V;
            this.directed = directed;
            this.E = 0;

            adj = new TreeSet[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeSet<Integer>();
        }

        public void addEdge(int a, int b){

            validateVertex(a);
            validateVertex(b);

            if(a == b) throw new IllegalArgumentException("Self Loop is Detected!");
            if(adj[a].contains(b)) throw new IllegalArgumentException("Parallel Edges are Detected!");

            adj[a].add(b);
            if(!directed)
                adj[b].add(a);
            this.E ++;
        }

        public boolean isDirected(){
            return directed;
        }

        public void validateVertex(int v){
            if(v < 0 || v >= V)
                throw new IllegalArgumentException("vertex " + v + "is invalid");
        }

        public int V(){
            return V;
        }

        public int E(){
            return E;
        }

        public boolean hasEdge(int v, int w){
            validateVertex(v);
            validateVertex(w);
            return adj[v].contains(w);
        }

        public Iterable<Integer> adj(int v){
            validateVertex(v);
            return adj[v];
        }

        public int degree(int v){
            if(!directed) throw new RuntimeException("degree only works in undirected graph.");
            validateVertex(v);
            return adj[v].size();
        }

        public int indegree(int v){
            if(!directed) throw new RuntimeException("indegree only works in directed graph.");
            validateVertex(v);
            return indegrees[v];
        }

        public int outdegree(int v){
            if(!directed) throw new RuntimeException("outdegree only works in directed graph.");
            validateVertex(v);
            return outdegrees[v];
        }

        public void removeEdge(int v, int w){
            validateVertex(v);
            validateVertex(w);

            if(adj[v].contains(w)){
                E --;

                if(directed){
                    indegrees[w] --;
                    outdegrees[v] --;
                }
            }

            adj[v].remove(w);
            if(!directed)
                adj[w].remove(v);
        }

        @Override
        public Object clone(){

            try{
                Graph cloned = (Graph) super.clone();
                cloned.adj = new TreeSet[V];
                for(int v = 0; v < V; v ++){
                    cloned.adj[v] = new TreeSet<Integer>();
                    for(int w: adj[v])
                        cloned.adj[v].add(w);
                }
                return cloned;
            }
            catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();

            sb.append(String.format("V = %d, E = %d, directed = %b\n", V, E, directed));
            for(int v = 0; v < V; v ++){
                sb.append(String.format("%d : ", v));
                for(int w : adj[v])
                    sb.append(String.format("%d ", w));
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    public class BipartitionDetection {

        private Graph G;

        private boolean[] visited;
        private int[] colors;
        private boolean isBipartite = true;

        public BipartitionDetection(Graph G){

            this.G = G;
            visited = new boolean[G.V()];
            colors = new int[G.V()];
            for(int i = 0; i < G.V(); i ++)
                colors[i] = -1;

            for(int v = 0; v < G.V(); v ++)
                if(!visited[v])
                    if(!dfs(v, 0)){
                        isBipartite = false;
                        break;
                    }
        }

        private boolean dfs(int v, int color){

            visited[v] = true;
            colors[v] = color;
            for(int w: G.adj(v))
                if(!visited[w]){
                    if(!dfs(w, 1 - color)) return false;
                }
                else if(colors[w] == colors[v])
                    return false;
            return true;
        }

        public boolean isBipartite(){
            return isBipartite;
        }

        public int[] colors(){ return colors;}
    }

    public class Hungarian {

        private Graph G;
        private int maxMatching;
        private int matching[];
        private boolean visited[];

        public Hungarian(Graph G){

            BipartitionDetection bd = new BipartitionDetection(G);
            if(!bd.isBipartite())
                throw new IllegalArgumentException("BipartiteMatching only works for bipartite graph.");

            this.G = G;

            int[] colors = bd.colors();

            matching = new int[G.V()];
            Arrays.fill(matching, -1);

            visited = new boolean[G.V()];
            for(int v = 0; v < G.V(); v ++)
                if(colors[v] == 0 && matching[v] == -1){
                    Arrays.fill(visited, false);
                    if(dfs(v)) maxMatching ++;
                }
        }

        private boolean dfs(int v){

            visited[v] = true;
            for(int u: G.adj(v))
                if(!visited[u]){
                    visited[u] = true;
                    if(matching[u] == -1 || dfs(matching[u])){
                        matching[u] = v;
                        matching[v] = u;
                        return true;
                    }
                }
            return false;
        }

        public int maxMatching(){
            return maxMatching;
        }

        public boolean isPerfectMatching(){
            return maxMatching * 2 == G.V();
        }
    }

    public int domino(int n, int m, int[][] broken) {

        int[][] board = new int[n][m];
        for(int[] p: broken)
            board[p[0]][p[1]] = 1;

        Graph g = new Graph(n * m, false);

        for(int i = 0; i < n; i ++)
            for(int j = 0; j < m; j ++){
                if(j + 1 < m && board[i][j] == 0 && board[i][j + 1] == 0)
                    g.addEdge(i * m + j, i * m + (j + 1));
                if(i + 1 < n && board[i][j] == 0 && board[i + 1][j] == 0)
                    g.addEdge(i * m + j, (i + 1) * m + j);
            }

        Hungarian hungarian = new Hungarian(g);
        return hungarian.maxMatching();
    }
}
