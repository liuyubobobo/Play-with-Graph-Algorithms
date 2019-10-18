import java.util.*;

class Solution {

    private int n, m;
    private int[][] dirs = {{0, 1}, {1, 0}};

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
                adj[i] = new TreeMap<>();
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

    public int domino(int n, int m, int[][] broken) {

        this.n = n;
        this.m = m;

        int[][] board = new int[n][m];
        for(int[] pos: broken)
            board[pos[0]][pos[1]] = 1;

        WeightedGraph g = new WeightedGraph(n * m + 2, true);
        int s = n * m, t = n * m + 1;
        for(int i = 0; i < n; i ++)
            for(int j = 0; j < m; j ++)
                if(board[i][j] == 0){
                    for(int d = 0; d < 2; d ++){
                        int nexti = i + dirs[d][0], nextj = j + dirs[d][1];
                        if(inArea(nexti, nextj) && board[nexti][nextj] == 0){
                            int a = i * m + j, b = nexti * m + nextj;
                            if((i + j) % 2 == 0){
                                g.addEdge(a, b, 1);
                                if(!g.hasEdge(s, a)) g.addEdge(s, a, 1);
                                if(!g.hasEdge(b, t)) g.addEdge(b, t, 1);
                            }
                            else{
                                g.addEdge(b, a, 1);
                                if(!g.hasEdge(s, b)) g.addEdge(s, b, 1);
                                if(!g.hasEdge(a, t)) g.addEdge(a, t, 1);
                            }
                        }
                    }
                }
        MaxFlow maxFlow = new MaxFlow(g, s, t);
        return maxFlow.result();
    }

    private boolean inArea(int x, int y){
        return x >= 0 && x < n && y >= 0 && y < m;
    }

    public static void main(String[] args){

        int[][] broken1 = {{1, 0}, {1, 1}};
        System.out.println((new Solution()).domino(2, 3, broken1));
        // 2

        int[][] broken2 = {};
        System.out.println((new Solution()).domino(4, 4, broken2));
        // 8
    }
}
