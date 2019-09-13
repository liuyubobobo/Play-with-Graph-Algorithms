import java.util.ArrayList;

public class Prim {

    private WeightedGraph G;
    private ArrayList<WeightedEdge> mst;

    public Prim(WeightedGraph G){

        this.G = G;
        mst = new ArrayList<>();

        CC cc = new CC(G);
        if(cc.count() > 1) return;

        boolean[] visited = new boolean[G.V()];
        visited[0] = true;
        for(int i = 1; i < G.V(); i ++){

            WeightedEdge minEdge = new WeightedEdge(-1, -1, Integer.MAX_VALUE);
            for(int v = 0; v < G.V(); v ++)
                if(visited[v])
                    for(int w: G.adj(v))
                        if(!visited[w] && G.getWeight(v, w) < minEdge.getWeight())
                            minEdge = new WeightedEdge(v, w, G.getWeight(v, w));
            mst.add(minEdge);
            visited[minEdge.getV()] = true;
            visited[minEdge.getW()] = true;
        }
    }

    public ArrayList<WeightedEdge> result(){
        return mst;
    }

    public static void main(String[] args){

        WeightedGraph g = new WeightedGraph("g.txt");
        Prim prim = new Prim(g);
        System.out.println(prim.result());
    }
}
