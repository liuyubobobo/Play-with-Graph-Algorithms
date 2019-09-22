import java.util.Arrays;

public class BellmanFord {

    private WeightedGraph G;
    private int s;
    private int[] dis;
    private boolean hasNegCycle = false;

    public BellmanFord(WeightedGraph G, int s){

        this.G = G;

        G.validateVertex(s);
        this.s = s;

        dis = new int[G.V()];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[s] = 0;

        for(int pass = 1; pass < G.V(); pass ++){
            for(int v = 0; v < G.V(); v ++)
                for(int w: G.adj(v))
                    if(dis[v] != Integer.MAX_VALUE &&
                       dis[v] + G.getWeight(v, w) < dis[w])
                        dis[w] = dis[v] + G.getWeight(v, w);
        }

        for(int v = 0; v < G.V(); v ++)
            for(int w : G.adj(v))
                if(dis[v] != Integer.MAX_VALUE &&
                   dis[v] + G.getWeight(v, w) < dis[w])
                    hasNegCycle = true;
    }

    boolean hasNegativeCycle(){
        return hasNegCycle;
    }

    boolean isConnectedTo(int v){
        G.validateVertex(v);
        return dis[v] != Integer.MAX_VALUE;
    }

    int distTo(int v){
        G.validateVertex(v);
        if(hasNegCycle) throw new RuntimeException("exist negative cycle.");
        return dis[v];
    }
}
