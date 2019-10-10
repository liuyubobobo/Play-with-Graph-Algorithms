import java.io.File;
import java.io.IOException;
import java.util.TreeSet;
import java.util.Scanner;


/// 无权图(支持有向，无向)
public class Graph implements Cloneable{

    private int V;
    private int E;
    private TreeSet<Integer>[] adj;
    private boolean directed;
    private int[] indegrees, outdegrees;

    public Graph(String filename, boolean directed){

        this.directed = directed;

        File file = new File(filename);

        try(Scanner scanner = new Scanner(file)){

            V = scanner.nextInt();
            if(V < 0) throw new IllegalArgumentException("V must be non-negative");
            adj = new TreeSet[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeSet<Integer>();

            indegrees = new int[V];
            outdegrees = new int[V];

            E = scanner.nextInt();
            if(E < 0) throw new IllegalArgumentException("E must be non-negative");

            for(int i = 0; i < E; i ++){
                int a = scanner.nextInt();
                validateVertex(a);
                int b = scanner.nextInt();
                validateVertex(b);

                if(a == b) throw new IllegalArgumentException("Self Loop is Detected!");
                if(adj[a].contains(b)) throw new IllegalArgumentException("Parallel Edges are Detected!");

                adj[a].add(b);
                outdegrees[a] ++;
                indegrees[b] ++;
                if(!directed)
                    adj[b].add(a);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public Graph(String filename){
        this(filename, false);
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

    public static void main(String[] args){

        Graph ug = new Graph("ug.txt", true);
        System.out.print(ug);

        for(int v = 0; v < ug.V(); v ++)
            System.out.println(String.format("%d %d", ug.indegrees[v], ug.outdegrees[v]));
    }
}
