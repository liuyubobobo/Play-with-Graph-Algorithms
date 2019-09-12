import java.util.ArrayList;
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

        /// Kruskal 算法还未实现完成，下一小节继续：）
    }

    public ArrayList<WeightedEdge> result(){
        return mst;
    }
}
