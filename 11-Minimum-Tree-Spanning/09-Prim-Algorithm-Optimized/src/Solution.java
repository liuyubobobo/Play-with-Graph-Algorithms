/// Leetcode 1168. Optimize Water Distribution in a Village
/// https://leetcode.com/problems/optimize-water-distribution-in-a-village/
///
/// 课程中在这里暂时没有介绍这个问题
/// 该代码主要用于使用 Leetcode 上的问题测试我们的 Prim 算法类
import java.util.*;


/// Prim Optimized
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

    public class Prim {

        private WeightedGraph G;
        private ArrayList<WeightedEdge> mst;

        public Prim(WeightedGraph G){

            this.G = G;
            mst = new ArrayList<>();

            CC cc = new CC(G);
            if(cc.count() > 1) return;

            boolean visited[] = new boolean[G.V()];
            visited[0] = true;
            Queue pq = new PriorityQueue<WeightedEdge>();
            for(int w: G.adj(0))
                pq.add(new WeightedEdge(0, w, G.getWeight(0, w)));

            while(!pq.isEmpty()){

                WeightedEdge minEdge = (WeightedEdge) pq.remove();
                if(visited[minEdge.getV()] && visited[minEdge.getW()])
                    continue;

                mst.add(minEdge);

                int newp = visited[minEdge.getV()] ? minEdge.getW() : minEdge.getV();
                visited[newp] = true;
                for(int w: G.adj(newp))
                    if(!visited[w])
                        pq.add(new WeightedEdge(newp, w, G.getWeight(newp, w)));
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

        Prim prim = new Prim(g);
        ArrayList<WeightedEdge> mst = prim.result();

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
