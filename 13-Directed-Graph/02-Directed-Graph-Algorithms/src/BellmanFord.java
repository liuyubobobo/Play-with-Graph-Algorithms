import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BellmanFord {

    private WeightedGraph G;
    private int s;
    private int[] dis;
    private int[] pre;
    private boolean hasNegCycle = false;

    public BellmanFord(WeightedGraph G, int s){

        this.G = G;

        G.validateVertex(s);
        this.s = s;

        dis = new int[G.V()];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[s] = 0;

        pre = new int[G.V()];
        Arrays.fill(pre, -1);

        for(int pass = 1; pass < G.V(); pass ++){
            for(int v = 0; v < G.V(); v ++)
                for(int w: G.adj(v))
                    if(dis[v] != Integer.MAX_VALUE &&
                       dis[v] + G.getWeight(v, w) < dis[w]){
                        dis[w] = dis[v] + G.getWeight(v, w);
                        pre[w] = v;
                    }
        }

        for(int v = 0; v < G.V(); v ++)
            for(int w : G.adj(v))
                if(dis[v] != Integer.MAX_VALUE &&
                   dis[v] + G.getWeight(v, w) < dis[w])
                    hasNegCycle = true;
    }

    public boolean hasNegativeCycle(){
        return hasNegCycle;
    }

    public boolean isConnectedTo(int v){
        G.validateVertex(v);
        return dis[v] != Integer.MAX_VALUE;
    }

    public int distTo(int v){
        G.validateVertex(v);
        if(hasNegCycle) throw new RuntimeException("exist negative cycle.");
        return dis[v];
    }

    public Iterable<Integer> path(int t){

        ArrayList<Integer> res = new ArrayList<Integer>();
        if(!isConnectedTo(t)) return res;

        int cur = t;
        while(cur != s){
            res.add(cur);
            cur = pre[cur];
        }
        res.add(s);

        Collections.reverse(res);
        return res;
    }

    static public void main(String[] args){

        WeightedGraph g = new WeightedGraph("wg.txt", true);
        BellmanFord bf = new BellmanFord(g, 0);
        if(!bf.hasNegativeCycle()){
            for(int v = 0; v < g.V(); v ++)
                System.out.print(bf.distTo(v) + " ");
            System.out.println();

            System.out.println(bf.path(1));
        }
        else
            System.out.println("exist negative cycle.");

        WeightedGraph g2 = new WeightedGraph("wg2.txt", true);
        BellmanFord bf2 = new BellmanFord(g2, 0);
        if(!bf2.hasNegativeCycle()){
            for(int v = 0; v < g2.V(); v ++)
                System.out.print(bf2.distTo(v) + " ");
            System.out.println();
        }
        else
            System.out.println("exist negative cycle.");
    }
}
