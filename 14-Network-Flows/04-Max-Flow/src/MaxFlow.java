import java.util.ArrayList;

public class MaxFlow {

    private WeightedGraph network;
    private int s, t;

    private WeightedGraph rG;
    private int maxFlow = 0;

    public MaxFlow(WeightedGraph network, int s, int t){

        if(!network.isDirected())
            throw new IllegalArgumentException("MaxFlow only works in directed graph.");

        if(network.V() < 2)
            throw new IllegalArgumentException("The network should has at least 2 vertices.");

        network.validateVertex(s);
        network.validateVertex(t);
        if(s == t)
            throw new IllegalArgumentException("s and t should be different.");

        this.network = network;
        this.s = s;
        this.t = t;

        this.rG = new WeightedGraph(network.V(), true);
        for(int v = 0; v < network.V(); v ++)
            for(int w: network.adj(v)){
                int c = network.getWeight(v, w);
                rG.addEdge(v, w, c);
                rG.addEdge(w, v, 0);
            }

        while(true){

            ArrayList<Integer> augPath = getAugmentingPath();
            if(augPath.size() == 0) break;

            int f = Integer.MAX_VALUE;
            // TODO: 计算增广路径上的最小值

            maxFlow += f;

            // TODO: 根据增广路径更新 rG
        }
    }

    private ArrayList<Integer> getAugmentingPath(){
        return null;
    }
}
