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

    public class WeightedGraph{

        private int V;
        private int E;
        private TreeMap<Integer, Integer>[] adj;
        private boolean directed;

        public WeightedGraph(int V, boolean directed){
            this.V = V;
            this.directed = directed;
            this.E = 0;

            adj = new TreeMap[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeMap<Integer, Integer>();
        }

        public void addEdge(int a, int b, int v){

            validateVertex(a);
            validateVertex(b);

            if(a == b) throw new IllegalArgumentException("Self Loop is Detected!");
            if(adj[a].containsKey(b)) throw new IllegalArgumentException("Parallel Edges are Detected!");

            adj[a].put(b, v);
            if(!directed)
                adj[b].put(a, v);
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
            return adj[v].containsKey(w);
        }

        public int getWeight(int v, int w){

            if(hasEdge(v, w)) return adj[v].get(w);
            throw new IllegalArgumentException(String.format("No edge %d-%d", v, w));
        }

        public void setWeight(int v, int w, int newWeight){
            if(!hasEdge(v, w))
                throw new IllegalArgumentException(String.format("No edge %d-%d", v, w));

            adj[v].put(w, newWeight);
            if(!directed)
                adj[w].put(v, newWeight);
        }

        public Iterable<Integer> adj(int v){
            validateVertex(v);
            return adj[v].keySet();
        }

        public void removeEdge(int v, int w){
            validateVertex(v);
            validateVertex(w);

            if(adj[v].containsKey(w)) E --;

            adj[v].remove(w);
            if(!directed)
                adj[w].remove(v);
        }

        @Override
        public Object clone(){

            try{
                WeightedGraph cloned = (WeightedGraph) super.clone();
                cloned.adj = new TreeMap[V];
                for(int v = 0; v < V; v ++){
                    cloned.adj[v] = new TreeMap<Integer, Integer>();
                    for(Map.Entry<Integer, Integer> entry: adj[v].entrySet())
                        cloned.adj[v].put(entry.getKey(), entry.getValue());
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
                for(Map.Entry<Integer, Integer> entry: adj[v].entrySet())
                    sb.append(String.format("(%d: %d) ", entry.getKey(), entry.getValue()));
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

    public class MaxFlow {

        private WeightedGraph network;
        private int s, t;

        private WeightedGraph rG;
        private int maxFlow = 0;

        public MaxFlow(WeightedGraph network, int s, int t){

            if(!network.isDirected())
                throw new IllegalArgumentException("MaxFlow only works in directed graph.");

            if(network.V() < 2)
                throw new IllegalArgumentException("The network should hs at least 2 vertices.");

            network.validateVertex(s);
            network.validateVertex(t);
            if(s == t)
                throw new IllegalArgumentException("s and t should be differrent.");

            this.network = network;
            this.s = s;
            this.t = t;

            this.rG = new WeightedGraph(network.V(), true);
            for(int v = 0; v < network.V(); v ++)
                for(int w: network.adj(v)){
                    int c = network.getWeight(v, w);
                    rG.addEdge(v, w, c);
                    rG.addEdge(w, v, 0);
                }

            while(true){
                ArrayList<Integer> augPath = getAugmentingPath();
                if(augPath.size() == 0) break;

                int f = Integer.MAX_VALUE;
                for(int i = 1; i < augPath.size(); i ++) {
                    int v = augPath.get(i - 1);
                    int w = augPath.get(i);
                    f = Math.min(f, rG.getWeight(v, w));
                }
                maxFlow += f;

                for(int i = 1; i < augPath.size(); i ++){
                    int v = augPath.get(i - 1);
                    int w = augPath.get(i);

                    rG.setWeight(v, w, rG.getWeight(v, w) - f);
                    rG.setWeight(w, v, rG.getWeight(w, v) + f);
                }
            }
        }

        private ArrayList<Integer> getAugmentingPath(){

            Queue<Integer> q = new LinkedList<>();
            int[] pre = new int[network.V()];
            Arrays.fill(pre, -1);

            q.add(s);
            pre[s] = s;
            while(!q.isEmpty()){
                int cur = q.remove();
                if(cur == t) break;
                for(int next: rG.adj(cur))
                    if(pre[next] == -1 && rG.getWeight(cur, next) > 0){
                        pre[next] = cur;
                        q.add(next);
                    }
            }

            ArrayList<Integer> res = new ArrayList<>();
            if(pre[t] == -1) return res;

            int cur = t;
            while(cur != s){
                res.add(cur);
                cur = pre[cur];
            }
            res.add(s);

            Collections.reverse(res);
            return res;
        }

        public int result(){
            return maxFlow;
        }

        public int flow(int v, int w){

            if(!network.hasEdge(v, w))
                throw new IllegalArgumentException(String.format("No edge %d-%d", v, w));

            return rG.getWeight(w, v);
        }
    }

    public class BipartiteMatching {

        private Graph G;
        private int maxMatching;

        public BipartiteMatching(Graph G){

            BipartitionDetection bd = new BipartitionDetection(G);
            if(!bd.isBipartite())
                throw new IllegalArgumentException("BipartiteMatching only works for bipartite graph.");

            int[] colors = bd.colors();
//        for(int e: colors) System.out.print(e + " "); System.out.println();

            // 源点为 V, 汇点为 V + 1
            WeightedGraph network = new WeightedGraph(G.V() + 2, true);
            for(int v = 0; v < G.V(); v ++) {
                for (int w : G.adj(v))
                    if(v < w){
                        if (colors[v] == 0) network.addEdge(v, w, 1);
                        else network.addEdge(w, v, 1);
                    }

                if(colors[v] == 0)network.addEdge(G.V(), v, 1);
                else network.addEdge(v, G.V() + 1, 1);
            }

            MaxFlow maxFlow = new MaxFlow(network, G.V(), G.V() + 1);
            maxMatching = maxFlow.result();

//        for(int v = 0; v < network.V(); v ++)
//            for(int w: network.adj(v))
//                System.out.println(String.format("%d-%d : %d / %d", v, w, maxFlow.flow(v, w), network.getWeight(v, w)));
        }

        public int maxMatching(){
            return maxMatching;
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

        BipartiteMatching bm = new BipartiteMatching(g);
        return bm.maxMatching();
    }

    public static void main(String[] args){

        int[][] broken = new int[0][2];
        System.out.println((new Solution()).domino(3, 3, broken));
    }
}
