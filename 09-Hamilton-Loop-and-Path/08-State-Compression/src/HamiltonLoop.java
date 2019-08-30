import java.util.ArrayList;
import java.util.Collections;

public class HamiltonLoop {

    private Graph G;
    private int[] pre;
    private int end;

    public HamiltonLoop(Graph G){

        this.G = G;
        pre = new int[G.V()];
        end = -1;

        int visited = 0;
        dfs(visited, 0, 0, G.V());
    }

    private boolean dfs(int visited, int v, int parent, int left){

        visited += (1 << v);
        pre[v] = parent;
        left --;
        if(left == 0 && G.hasEdge(v, 0)){
            end = v;
            return true;
        }

        for(int w: G.adj(v))
            if((visited & (1 << w)) == 0){
                if(dfs(visited, w, v, left)) return true;
            }

        visited -= (1 << v);
        return false;
    }

    public ArrayList<Integer> result(){

        ArrayList<Integer> res = new ArrayList<>();
        if(end == -1) return res;

        int cur = end;
        while(cur != 0){
            res.add(cur);
            cur = pre[cur];
        }
        res.add(0);

        Collections.reverse(res);
        return res;
    }

    public static void main(String[] args){

        Graph g = new Graph("g.txt");
        HamiltonLoop hl = new HamiltonLoop(g);
        System.out.println(hl.result());

        Graph g2 = new Graph("g2.txt");
        HamiltonLoop hl2 = new HamiltonLoop(g2);
        System.out.println(hl2.result());
    }
}
