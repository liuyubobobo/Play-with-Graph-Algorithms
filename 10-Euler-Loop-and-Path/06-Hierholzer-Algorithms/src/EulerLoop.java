import java.util.ArrayList;
import java.util.Stack;

public class EulerLoop {

    private Graph G;

    public EulerLoop(Graph G){
        this.G = G;
    }

    private boolean hasEulerLoop(){

        CC cc = new CC(G);
        if(cc.count() > 1) return false;

        for(int v = 0; v < G.V(); v ++)
            if(G.degree(v) % 2 == 1) return false;
        return true;
    }

    public ArrayList<Integer> loop(){

        ArrayList<Integer> res = new ArrayList<>();
        if(!hasEulerLoop()) return res;

        Graph g = (Graph)G.clone();
        Stack<Integer> curPath = new Stack<>();
        int curv = 0;
        curPath.push(curv);
        while(!curPath.isEmpty()){
            if(g.degree(curv) != 0){
                curPath.push(curv);
                int w = g.adj(curv).iterator().next();
                g.removeEdge(curv, w);
                curv = w;
            }
            else{
                res.add(curv);
                curv = curPath.pop();
            }
        }
        return res;
    }

    public static void main(String[] args){

        Graph g = new Graph("g.txt");
        EulerLoop el = new EulerLoop(g);
        System.out.print(el.loop());
    }
}
