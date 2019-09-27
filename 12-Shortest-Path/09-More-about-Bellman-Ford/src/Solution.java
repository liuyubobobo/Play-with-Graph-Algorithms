/// Leetcode 743. Network Delay Time
/// https://leetcode.com/problems/network-delay-time/
///
/// 课程中在这里暂时没有介绍这个问题
/// 该代码主要用于使用 Leetcode 上的问题测试我们的 Bellman-Ford 算法类

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Solution {

    public class WeightedGraph implements Cloneable{

        private int V;
        private int E;
        private TreeMap<Integer, Integer>[] adj;

        public WeightedGraph(int V){

            this.V = V;
            this.E = 0;
            adj = new TreeMap[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeMap<Integer, Integer>();
        }

        void addEdge(int u, int v, int weight){

            validateVertex(u);
            validateVertex(v);
            if(adj[u].containsKey(v)){
                adj[u].put(v, Math.min(adj[u].get(v), weight));
                return;
            }

            adj[u].put(v, weight);
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

    public class BellmanFord {

        private WeightedGraph G;
        private int s;
        private int[] dis;
        private boolean hasNegCycle = false;

        public BellmanFord(WeightedGraph G, int s){

            this.G = G;

            G.validateVertex(s);
            this.s = s;

            dis = new int[G.V()];
            Arrays.fill(dis, Integer.MAX_VALUE);
            dis[s] = 0;

            for(int pass = 1; pass < G.V(); pass ++){
                for(int v = 0; v < G.V(); v ++)
                    for(int w: G.adj(v))
                        if(dis[v] != Integer.MAX_VALUE &&
                                dis[v] + G.getWeight(v, w) < dis[w])
                            dis[w] = dis[v] + G.getWeight(v, w);
            }

            for(int v = 0; v < G.V(); v ++)
                for(int w : G.adj(v))
                    if(dis[v] != Integer.MAX_VALUE &&
                            dis[v] + G.getWeight(v, w) < dis[w])
                        hasNegCycle = true;
        }

        boolean hasNegativeCycle(){
            return hasNegCycle;
        }

        boolean isConnectedTo(int v){
            G.validateVertex(v);
            return dis[v] != Integer.MAX_VALUE;
        }

        int distTo(int v){
            G.validateVertex(v);
            if(hasNegCycle) throw new RuntimeException("exist negative cycle.");
            return dis[v];
        }
    }

    public int networkDelayTime(int[][] times, int N, int K) {

        WeightedGraph g = new WeightedGraph(N);
        for(int[] time: times)
            g.addEdge(time[0] - 1, time[1] - 1, time[2]);

        BellmanFord bellmanford = new BellmanFord(g, K - 1);
        int res = Integer.MIN_VALUE;
        for(int v = 0; v < N; v ++)
            res = Math.max(res, bellmanford.distTo(v));
        return res == Integer.MAX_VALUE ? -1 : res;
    }
}
