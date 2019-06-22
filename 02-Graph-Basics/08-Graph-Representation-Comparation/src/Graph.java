import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;


/// 暂时只支持无向无权图
public class Graph {

    private int V;
    private int E;
    private HashSet<Integer>[] adj;

    public Graph(String pathStr){

        File file = new File(pathStr);

        try(Scanner scanner = new Scanner(file)){

            V = scanner.nextInt();
            if(V < 0) throw new IllegalArgumentException("V must be non-negative");
            adj = (HashSet<Integer>[]) new HashSet[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new HashSet<Integer>();

            E = scanner.nextInt();
            if(E < 0) throw new IllegalArgumentException("E must be non-negative");

            for(int i = 0; i < E; i ++){
                int a = scanner.nextInt();
                validateVertex(a);
                int b = scanner.nextInt();
                validateVertex(b);
                if(a == b) throw new IllegalArgumentException("Self Loop Detected!");
                adj[a].add(b);
                adj[b].add(a);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void validateVertex(int v){
        if(v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + "is invalid");
    }

    public int V(){
        return V;
    }

    public int E(){
        return E;
    }

    public int degree(int v){
        validateVertex(v);
        return adj[v].size();
    }

    public Iterable<Integer> adj(int v){
        // public HashSet<Integer> adj(int v){
        validateVertex(v);
        return adj[v];
    }

    public boolean isAdj(int v, int w){
        validateVertex(v);
        validateVertex(w);
        return adj[v].contains(w);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("V = %d, E = %d\n", V, E));
        for(int v = 0; v < V; v ++){
            sb.append(v + " : " );
            for(int w : adj[v])
                sb.append(w + " ");
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String args[]){

        Graph g = new Graph("g.txt");
        System.out.print(g);
    }
}
