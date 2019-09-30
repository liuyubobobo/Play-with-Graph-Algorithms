import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class DirectedEulerLoop {

    private Graph G;

    public DirectedEulerLoop(Graph G){
        if(!G.isDirected())
            throw new IllegalArgumentException("DirectedEulerLopp only works in directed graph");
        this.G = G;
    }

    private boolean hasEulerLoop(){

        for(int v = 0; v < G.V(); v ++)
            if(G.indegree(v) != G.outdegree(v))
                return false;
        return true;
    }

    public ArrayList<Integer> result(){

        ArrayList<Integer> res = new ArrayList<>();
        if(!hasEulerLoop()) return res;

        Graph g = (Graph)G.clone();

        Stack<Integer> stack = new Stack<>();
        int curv = 0;
        stack.push(curv);
        while(!stack.isEmpty()){
            if(g.outdegree(curv) != 0){
                stack.push(curv);
                int w = g.adj(curv).iterator().next();
                g.removeEdge(curv, w);
                curv = w;
            }
            else{
                res.add(curv);
                curv = stack.pop();
            }
        }
        Collections.reverse(res);
        return res;
    }

    public static void main(String[] args){

        Graph g = new Graph("ug.txt", true);
        DirectedEulerLoop el = new DirectedEulerLoop(g);
        System.out.println(el.result());

        Graph g2 = new Graph("ug2.txt", true);
        DirectedEulerLoop el2 = new DirectedEulerLoop(g2);
        System.out.println(el2.result());
    }
}
