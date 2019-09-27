import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;


/// 暂时只支持无向带权图
public class WeightedGraph implements Cloneable{

    private int V;
    private int E;
    private TreeMap<Integer, Integer>[] adj;

    public WeightedGraph(String filename){

        File file = new File(filename);

        try(Scanner scanner = new Scanner(file)){

            V = scanner.nextInt();
            if(V < 0) throw new IllegalArgumentException("V must be non-negative");
            adj = new TreeMap[V];
            for(int i = 0; i < V; i ++)
                adj[i] = new TreeMap<Integer, Integer>();

            E = scanner.nextInt();
            if(E < 0) throw new IllegalArgumentException("E must be non-negative");

            for(int i = 0; i < E; i ++){
                int a = scanner.nextInt();
                validateVertex(a);
                int b = scanner.nextInt();
                validateVertex(b);
                int weight = scanner.nextInt();

                if(a == b) throw new IllegalArgumentException("Self Loop is Detected!");
                if(adj[a].containsKey(b)) throw new IllegalArgumentException("Parallel Edges are Detected!");

                adj[a].put(b, weight);
                adj[b].put(a, weight);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
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

    public Iterable<Integer> adj(int v){
        validateVertex(v);
        return adj[v].keySet();
    }

    public int getWeight(int v, int w){

        if(hasEdge(v, w)) return adj[v].get(w);
        throw new IllegalArgumentException(String.format("No edge %d-%d", v, w));
    }

    /// 下面的内容本小节还没有修改完成，下一小节继续：）
    /*
    public int degree(int v){
        validateVertex(v);
        return adj[v].size();
    }

    public void removeEdge(int v, int w){
        validateVertex(v);
        validateVertex(w);
        if(adj[v].contains(w)) E --;
        adj[v].remove(w);
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

        sb.append(String.format("V = %d, E = %d\n", V, E));
        for(int v = 0; v < V; v ++){
            sb.append(String.format("%d : ", v));
            for(int w : adj[v])
                sb.append(String.format("%d ", w));
            sb.append('\n');
        }
        return sb.toString();
    }
    */
}
