import java.util.ArrayList;

public class BipartitionDetection {

    private Graph G;

    private boolean[] visited;
    private int[] colors;
    private boolean isBipartite = true;

    public BipartitionDetection(Graph G){

        this.G = G;
        visited = new boolean[G.V()];
        colors = new int[G.V()];
        for(int i = 0; i < G.V(); i ++)
            colors[i] = -1;

        for(int v = 0; v < G.V(); v ++)
            if(!visited[v])
                if(!dfs(v, 0)){
                    isBipartite = false;
                    break;
                }
    }

    private boolean dfs(int v, int color){

        visited[v] = true;
        colors[v] = color;
        for(int w: G.adj(v))
            if(!visited[w]){
                if(!dfs(w, 1 - color)) return false;
            }
            else if(colors[w] == colors[v])
                return false;
        return true;
    }

    public boolean isBipartite(){
        return isBipartite;
    }

    public int[] colors(){ return colors;}

    public static void main(String[] args){

        Graph g = new Graph("g.txt");
        BipartitionDetection bipartitionDetection = new BipartitionDetection(g);
        System.out.println(bipartitionDetection.isBipartite);
        // true

        Graph g2 = new Graph("g2.txt");
        BipartitionDetection bipartitionDetection2 = new BipartitionDetection(g2);
        System.out.println(bipartitionDetection2.isBipartite);
        // false

        Graph g3 = new Graph("g3.txt");
        BipartitionDetection bipartitionDetection3 = new BipartitionDetection(g3);
        System.out.println(bipartitionDetection3.isBipartite);
        // true
    }
}
