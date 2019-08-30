import java.util.ArrayList;
import java.util.Collections;

public class HamiltonLoop {

    private Graph G;
    private boolean[] visited;
    private int[] pre;
    private int end;

    public HamiltonLoop(Graph G){

        this.G = G;
        visited = new boolean[G.V()];
        pre = new int[G.V()];
        end = -1;
        dfs(0, 0);
    }

    private boolean dfs(int v, int parent){

        visited[v] = true;
        pre[v] = parent;

        for(int w: G.adj(v))
            if(!visited[w]){
                if(dfs(w, v)) return true;
            }
            else if(w == 0 && allVisited()){
                end = v;
                return true;
            }
        visited[v] = false;
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

    private boolean allVisited(){
        for(int v = 0; v < G.V(); v ++)
            if(!visited[v]) return false;
        return true;
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
