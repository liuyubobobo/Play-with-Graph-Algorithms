import java.util.Arrays;
import java.util.PriorityQueue;


public class Dijkstra {

    private WeightedGraph G;
    private int s;
    private int[] dis;
    private boolean[] visited;

    private class Node implements Comparable<Node>{

        public int v, dis;

        public Node(int v, int dis){
            this.v = v;
            this.dis = dis;
        }

        @Override
        public int compareTo(Node another){
            return dis - another.dis;
        }
    }

    public Dijkstra(WeightedGraph G, int s){

        this.G = G;

        G.validateVertex(s);
        this.s = s;

        dis = new int[G.V()];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[s] = 0;

        visited = new boolean[G.V()];

        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        pq.add(new Node(s, 0));
        while(!pq.isEmpty()){

            int cur = pq.remove().v;

            if(visited[cur]) continue;

            visited[cur] = true;
            for(int w: G.adj(cur))
                if(!visited[w]){
                    if(dis[cur] + G.getWeight(cur, w) < dis[w]){
                        dis[w] = dis[cur] + G.getWeight(cur, w);
                        pq.add(new Node(w, dis[w]));
                    }
                }
        }
    }

    public boolean isConnectedTo(int v){

        G.validateVertex(v);
        return visited[v];
    }

    public int distTo(int v){

        G.validateVertex(v);
        return dis[v];
    }

    static public void main(String[] args){

        WeightedGraph g = new WeightedGraph("g.txt");
        Dijkstra dij = new Dijkstra(g, 0);
        for(int v = 0; v < g.V(); v ++)
            System.out.print(dij.distTo(v) + " ");
        System.out.println();
    }
}
