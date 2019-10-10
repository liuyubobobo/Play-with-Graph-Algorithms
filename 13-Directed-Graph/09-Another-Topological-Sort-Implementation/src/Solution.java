/// Leetcode 210. Course Schedule II
/// https://leetcode.com/problems/course-schedule-ii/
///
/// 课程中在这里暂时没有介绍这个问题
/// 该代码主要用于使用 Leetcode 上的问题测试我们的 TopSort2 算法类
import java.io.File;
import java.io.IOException;
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

    public class DirectedCycleDetection {

        private Graph G;
        private boolean[] visited;
        private boolean[] onPath;
        private boolean hasCycle = false;

        public DirectedCycleDetection(Graph G){

            if(!G.isDirected())
                throw new IllegalArgumentException("DirectedCycleDetection only works in directed graph.");

            this.G = G;
            visited = new boolean[G.V()];
            onPath = new boolean[G.V()];
            for(int v = 0; v < G.V(); v ++)
                if(!visited[v])
                    if(dfs(v)){
                        hasCycle = true;
                        break;
                    }
        }

        // 从顶点 v 开始，判断图中是否有环
        private boolean dfs(int v){

            visited[v] = true;
            onPath[v] = true;
            for(int w: G.adj(v))
                if(!visited[w]){
                    if(dfs(w)) return true;
                }
                else if(onPath[w])
                    return true;
            onPath[v] = false;
            return false;
        }

        public boolean hasCycle(){
            return hasCycle;
        }
    }

    public class GraphDFS {

        private Graph G;
        private boolean[] visited;

        private ArrayList<Integer> pre = new ArrayList<>();
        private ArrayList<Integer> post = new ArrayList<>();

        public GraphDFS(Graph G){

            this.G = G;
            visited = new boolean[G.V()];
            for(int v = 0; v < G.V(); v ++)
                if(!visited[v])
                    dfs(v);
        }

        private void dfs(int v){

            visited[v] = true;
            pre.add(v);
            for(int w: G.adj(v))
                if(!visited[w])
                    dfs(w);
            post.add(v);
        }

        public Iterable<Integer> pre(){
            return pre;
        }

        public Iterable<Integer> post(){
            return post;
        }
    }

    public class TopoSort2 {

        private Graph G;
        private ArrayList<Integer> res;
        private boolean hasCycle = false;

        public TopoSort2(Graph G){

            if(!G.isDirected())
                throw new IllegalArgumentException("DirectedCycleDetection only works in directed graph.");

            this.G = G;
            res = new ArrayList<>();

            hasCycle = (new DirectedCycleDetection(G)).hasCycle();
            if(hasCycle) return;

            GraphDFS dfs = new GraphDFS(G);
            for(int v: dfs.post())
                res.add(v);

            Collections.reverse(res);
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

        TopoSort2 topoSort = new TopoSort2(g);
        if(topoSort.hasCycle()) return new int[0];

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