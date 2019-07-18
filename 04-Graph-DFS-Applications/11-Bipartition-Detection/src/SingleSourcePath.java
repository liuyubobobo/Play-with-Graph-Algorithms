import java.util.ArrayList;
import java.util.Collections;

public class SingleSourcePath {

    private Graph G;
    private int s;

    private int[] pre;

    public SingleSourcePath(Graph G, int s){

        this.G = G;
        this.s = s;
        pre = new int[G.V()];
        for(int i = 0; i < pre.length; i ++)
            pre[i] = -1;

        dfs(s, s);
    }

    private void dfs(int v, int parent){

        pre[v] = parent;
        for(int w: G.adj(v))
            if(pre[w] == -1)
                dfs(w, v);
    }

    public boolean isConnectedTo(int t){
        G.validateVertex(t);
        return pre[t] != -1;
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

    public static void main(String[] args){

        Graph g = new Graph("g.txt");
        SingleSourcePath sspath = new SingleSourcePath(g, 0);
        System.out.println("0 -> 6 : " + sspath.path(6));
        System.out.println("0 -> 5 : " + sspath.path(5));
    }
}
