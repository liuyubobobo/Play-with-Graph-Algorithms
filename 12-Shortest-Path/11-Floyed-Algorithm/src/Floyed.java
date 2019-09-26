import java.util.Arrays;

public class Floyed {

    private WeightedGraph G;
    private int[][] dis;
    private boolean hasNegCycle = false;

    public Floyed(WeightedGraph G){

        this.G = G;

        dis = new int[G.V()][G.V()];
        for(int v = 0; v < G.V(); v ++)
            Arrays.fill(dis[v], Integer.MAX_VALUE);

        for(int v = 0; v < G.V(); v ++){
            dis[v][v] = 0;
            for(int w: G.adj(v))
                dis[v][w] = G.getWeight(v, w);
        }

        for(int t = 0; t < G.V(); t ++)
            for(int v = 0; v < G.V(); v ++)
                for(int w = 0; w < G.V(); w ++)
                    if(dis[v][t] != Integer.MAX_VALUE && dis[t][w] != Integer.MAX_VALUE
                       && dis[v][t] + dis[t][w] < dis[v][w])
                        dis[v][w] = dis[v][t] + dis[t][w];

        for(int v = 0; v < G.V(); v ++)
            if(dis[v][v] < 0)
                hasNegCycle = true;
    }

    public boolean hasNegativeCycle(){
        return hasNegCycle;
    }

    public boolean isConnectedTo(int v, int w){
        G.validateVertex(v);
        G.validateVertex(w);
        return dis[v][w] != Integer.MAX_VALUE;
    }

    public int distTo(int v, int w){
        G.validateVertex(v);
        G.validateVertex(w);
        return dis[v][w];
    }

    static public void main(String[] args){

        WeightedGraph g = new WeightedGraph("g.txt");
        Floyed floyed = new Floyed(g);
        if(!floyed.hasNegativeCycle()){
            for(int v = 0; v < g.V(); v ++){
                for(int w = 0; w < g.V(); w ++)
                    System.out.print(floyed.distTo(v, w) + " ");
                System.out.println();
            }
        }
        else
            System.out.println("exist negative cycle.");

        WeightedGraph g2 = new WeightedGraph("g2.txt");
        Floyed floyed2 = new Floyed(g2);
        if(!floyed2.hasNegativeCycle()){
            for(int v = 0; v < g.V(); v ++){
                for(int w = 0; w < g.V(); w ++)
                    System.out.print(floyed2.distTo(v, w) + " ");
                System.out.println();
            }
        }
        else
            System.out.println("exist negative cycle.");
    }
}
