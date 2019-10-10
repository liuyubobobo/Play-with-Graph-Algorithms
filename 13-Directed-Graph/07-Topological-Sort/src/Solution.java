/// Leetcode 210. Course Schedule II
/// https://leetcode.com/problems/course-schedule-ii/
///
/// 课程中在这里暂时没有介绍这个问题
/// 该代码主要用于使用 Leetcode 上的问题测试我们的 TopSort 算法类

import java.util.*;

class Solution {

    public class Graph implements Cloneable{

        private int V;
        private int E;
        private TreeSet<Integer>[] adj;
        private boolean directed;
        private int[] indegrees, outdegrees;

        public Graph(int V, boolean directed){

            this.directed = directed;
            this.V = V;
            this.E = 0;

            adj = new TreeSet[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeSet<Integer>();

            indegrees = new int[V];
            outdegrees = new int[V];
        }

        public void addEdge(int a, int b){

            validateVertex(a);
            validateVertex(b);

            adj[a].add(b);
            outdegrees[a] ++;
            indegrees[b] ++;
            if(!directed)
                adj[b].add(a);
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

    public class TopoSort {

        private Graph G;

        private ArrayList<Integer> res;
        private boolean hasCycle = false;

        public TopoSort(Graph G){

            if(!G.isDirected())
                throw new IllegalArgumentException("TopoSort only works in directed graph.");

            this.G = G;

            res = new ArrayList<>();

            int[] indegrees = new int[G.V()];
            Queue<Integer> q = new LinkedList<>();
            for(int v = 0; v < G.V(); v ++){
                indegrees[v] = G.indegree(v);
                if(indegrees[v] == 0) q.add(v);
            }

            while(!q.isEmpty()){
                int cur = q.remove();
                res.add(cur);

                for(int next: G.adj(cur)){
                    indegrees[next] --;
                    if(indegrees[next] == 0) q.add(next);
                }
            }

            if(res.size() != G.V()){
                hasCycle = true;
                res.clear();
            }
        }

        public boolean hasCycle(){
            return hasCycle;
        }

        public ArrayList<Integer> result(){
            return res;
        }
    }

    public int[] findOrder(int numCourses, int[][] prerequisites) {

        Graph g = new Graph(numCourses, true);
        for(int[] e: prerequisites)
            g.addEdge(e[1], e[0]);

        TopoSort topoSort = new TopoSort(g);
        if(topoSort.hasCycle) return new int[0];

        int[] res = new int[numCourses];
        for(int i = 0; i < numCourses; i ++)
            res[i] = topoSort.result().get(i);
        return res;
    }

    public static void main(String[] args){

        int[][] pre = {{0, 1}, {1, 0}};
        int[] res = (new Solution()).findOrder(2, pre);
        System.out.println("res size : " + res.length);
        for(int e: res) System.out.print(e + " ");
        System.out.println();
    }
}