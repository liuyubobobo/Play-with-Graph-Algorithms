/// Leetcode 743. Network Delay Time
/// https://leetcode.com/problems/network-delay-time/
///
/// 课程中在这里暂时没有介绍这个问题
/// 该代码主要用于使用 Leetcode 上的问题测试我们的 Dijkstra 算法类

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.PriorityQueue;

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

    public class Dijkstra {

        private WeightedGraph G;
        private int s;
        private int[] dis;
        private boolean[] visited;

        private class Node implements Comparable<Node>{

            public int v, dis;

            public Node(int v, int dis){
                this.v = v;
                this.dis = dis;
            }

            @Override
            public int compareTo(Node another){
                return dis - another.dis;
            }
        }

        public Dijkstra(WeightedGraph G, int s){

            this.G = G;

            G.validateVertex(s);
            this.s = s;

            dis = new int[G.V()];
            Arrays.fill(dis, Integer.MAX_VALUE);
            dis[s] = 0;

            visited = new boolean[G.V()];

            PriorityQueue<Node> pq = new PriorityQueue<>();
            pq.add(new Node(s, 0));
            while(!pq.isEmpty()){

                int cur = pq.remove().v;

                if(visited[cur]) continue;

                visited[cur] = true;
                for(int w: G.adj(cur))
                    if(!visited[w]){
                        if(dis[cur] + G.getWeight(cur, w) < dis[w]){
                            dis[w] = dis[cur] + G.getWeight(cur, w);
                            pq.add(new Node(w, dis[w]));
                        }
                    }
            }
        }

        public boolean isConnectedTo(int v){

            G.validateVertex(v);
            return visited[v];
        }

        public int distTo(int v){

            G.validateVertex(v);
            return dis[v];
        }
    }

    public int networkDelayTime(int[][] times, int N, int K) {

        WeightedGraph g = new WeightedGraph(N);
        for(int[] time: times)
            g.addEdge(time[0] - 1, time[1] - 1, time[2]);

        Dijkstra dij = new Dijkstra(g, K - 1);
        int res = Integer.MIN_VALUE;
        for(int v = 0; v < N; v ++)
            res = Math.max(res, dij.distTo(v));
        return res == Integer.MAX_VALUE ? -1 : res;
    }
}
