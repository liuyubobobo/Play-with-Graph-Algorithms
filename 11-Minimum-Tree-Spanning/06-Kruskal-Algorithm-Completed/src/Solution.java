/// Leetcode 1168. Optimize Water Distribution in a Village
/// https://leetcode.com/problems/optimize-water-distribution-in-a-village/
///
/// 课程中在这里暂时没有介绍这个问题
/// 该代码主要用于使用 Leetcode 上的问题测试我们的 Kruskal 算法类
import java.util.*;


/// Kruskal Solution
/// Time Complexity: O(ElogE)
/// Space Complexity: O(V + E)
class Solution {

    public class WeightedGraph implements Cloneable{

        private int V;
        private int E;
        private TreeMap<Integer, Integer>[] adj;

        public WeightedGraph(int V){

            this.V = V;
            adj = new TreeMap[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeMap<Integer, Integer>();

            E = 0;
        }

        public void addEdge(int v, int w, int weight){

            if(v == w)
                throw new IllegalArgumentException("Self Loop is Detected!");

            validateVertex(v);
            validateVertex(w);

            if(adj[v].containsKey(w)){
                int newWeight = Math.min(weight, adj[v].get(w));
                adj[v].put(w, newWeight);
                adj[w].put(v, newWeight);
                return;
            }

            adj[v].put(w, weight);
            adj[w].put(v, weight);
            E ++;
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

        public Iterable<Integer> adj(int v){
            validateVertex(v);
            return adj[v].keySet();
        }

        public int degree(int v){
            validateVertex(v);
            return adj[v].size();
        }

        public void removeEdge(int v, int w){
            validateVertex(v);
            validateVertex(w);
            if(adj[v].containsKey(w)) E --;
            adj[v].remove(w);
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

            sb.append(String.format("V = %d, E = %d\n", V, E));
            for(int v = 0; v < V; v ++){
                sb.append(String.format("%d : ", v));
                for(Map.Entry<Integer, Integer> entry: adj[v].entrySet())
                    sb.append(String.format("(%d: %d) ", entry.getKey(), entry.getValue()));
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    public class WeightedEdge implements Comparable<WeightedEdge>{

        private int v, w, weight;

        public WeightedEdge(int v, int w, int weight){
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        public int getV(){return v;}

        public int getW(){return w;}

        public int getWeight(){return weight;}

        @Override
        public int compareTo(WeightedEdge another){
            return weight - another.weight;
        }

        @Override
        public String toString(){
            return String.format("(%d-%d: %d)", v, w, weight);
        }
    }

    public class CC {

        private WeightedGraph G;
        private int[] visited;
        private int cccount = 0;

        public CC(WeightedGraph G){

            this.G = G;
            visited = new int[G.V()];
            for(int i = 0; i < visited.length; i ++)
                visited[i] = -1;

            for(int v = 0; v < G.V(); v ++)
                if(visited[v] == -1){
                    dfs(v, cccount);
                    cccount ++;
                }
        }

        private void dfs(int v, int ccid){

            visited[v] = ccid;
            for(int w: G.adj(v))
                if(visited[w] == -1)
                    dfs(w, ccid);
        }

        public int count(){
            return cccount;
        }

        public boolean isConnected(int v, int w){
            G.validateVertex(v);
            G.validateVertex(w);
            return visited[v] == visited[w];
        }

        public ArrayList<Integer>[] components(){

            ArrayList<Integer>[] res = new ArrayList[cccount];
            for(int i = 0; i < cccount; i ++)
                res[i] = new ArrayList<Integer>();

            for(int v = 0; v < G.V(); v ++)
                res[visited[v]].add(v);
            return res;
        }
    }

    public class UF{

        private int[] parent;

        public UF(int n){

            parent = new int[n];
            for(int i = 0 ; i < n ; i ++)
                parent[i] = i;
        }

        public int find(int p){
            if( p != parent[p] )
                parent[p] = find( parent[p] );
            return parent[p];
        }

        public boolean isConnected(int p , int q){
            return find(p) == find(q);
        }

        public void unionElements(int p, int q){

            int pRoot = find(p);
            int qRoot = find(q);

            if( pRoot == qRoot )
                return;

            parent[pRoot] = qRoot;
        }
    }

    public class Kruskal {

        private WeightedGraph G;
        private ArrayList<WeightedEdge> mst;

        public Kruskal(WeightedGraph G){

            this.G = G;
            mst = new ArrayList<>();

            CC cc = new CC(G);
            if(cc.count() > 1) return;

            ArrayList<WeightedEdge> edges = new ArrayList<>();
            for(int v = 0; v < G.V(); v ++)
                for(int w: G.adj(v))
                    if(v < w)
                        edges.add(new WeightedEdge(v, w, G.getWeight(v, w)));

            Collections.sort(edges);

            UF uf = new UF(G.V());
            for(WeightedEdge edge: edges){
                int v = edge.getV();
                int w = edge.getW();
                if(!uf.isConnected(v, w)){
                    mst.add(edge);
                    uf.unionElements(v, w);
                }
            }
        }

        public ArrayList<WeightedEdge> result(){
            return mst;
        }
    }

    public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {

        WeightedGraph g = new WeightedGraph(n + 1);
        for(int i = 0; i < wells.length; i ++)
            g.addEdge(0, i + 1, wells[i]);
        for(int i = 0; i < pipes.length; i ++)
            g.addEdge(pipes[i][0], pipes[i][1], pipes[i][2]);

        Kruskal kruskal = new Kruskal(g);
        ArrayList<WeightedEdge> mst = kruskal.result();

        int res = 0;
        for(WeightedEdge edge: mst)
            res += edge.getWeight();
        return res;
    }

    public static void main(String[] args){

        int[] wells = {1, 2, 2};
        int[][] pipes = {{1, 2, 1}, {2, 3, 1}};
        System.out.println((new Solution()).minCostToSupplyWater(3, wells, pipes));
    }
}
