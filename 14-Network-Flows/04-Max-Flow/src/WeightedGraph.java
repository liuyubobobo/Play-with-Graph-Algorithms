import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;


/// 带权图(支持有向，无向)
public class WeightedGraph{

    private int V;
    private int E;
    private TreeMap<Integer, Integer>[] adj;
    private boolean directed;

    public WeightedGraph(String filename, boolean directed){

        this.directed = directed;

        File file = new File(filename);

        try(Scanner scanner = new Scanner(file)){

            V = scanner.nextInt();
            if(V < 0) throw new IllegalArgumentException("V must be non-negative");
            adj = new TreeMap[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeMap<Integer, Integer>();

            E = 0;

            int e = scanner.nextInt();
            if(e < 0) throw new IllegalArgumentException("E must be non-negative");

            for(int i = 0; i < e; i ++){
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                int v = scanner.nextInt();

                addEdge(a, b, v);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public WeightedGraph(String filename){
        this(filename, false);
    }

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
