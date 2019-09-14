import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    public static void main(String[] args){

        WeightedGraph g = new WeightedGraph("g.txt");
        Kruskal kruskal = new Kruskal(g);
        System.out.println(kruskal.result());
    }
}
