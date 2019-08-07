// 盛水问题，完整程序在这一小节还未完成

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WaterPuzzle {

    public WaterPuzzle(){

        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[100];

        queue.add(0);
        visited[0] = true;
        while(!queue.isEmpty()){
            int cur = queue.remove();
            int a = cur / 10, b = cur % 10;
            // max a = 5, max b = 3

            ArrayList<Integer> nexts = new ArrayList<>();
            // TODO: nexts

            for(int next: nexts)
                if(!visited[next]){
                    queue.add(next);
                    visited[next] = true;

                    if(next / 10 == 4 || next % 10 == 4){
                        return;
                    }
                }
        }
    }

    public Iterable<Integer> result(){
        return null;
    }
}
