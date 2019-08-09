// Leetcode 773, 完整程序这一小节未完成

import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;


public class Solution {

    public int slidingPuzzle(int[][] board) {

        Queue<String> queue = new LinkedList<>();
        HashMap<String, Integer> visited = new HashMap<>();

        String initalState = boardToString(board);
        if(initalState.equals("123450")) return 0;

        queue.add(initalState);
        visited.put(initalState, 0);
        while(!queue.isEmpty()){
            String cur = queue.remove();

            ArrayList<String> nexts = getNexts(cur);

            for(String next: nexts)
                if(!visited.containsKey(next)){
                    queue.add(next);
                    visited.put(next, visited.get(cur) + 1);
                    if(next.equals("123450"))
                        return visited.get(next);
                }
        }
        return -1;
    }

    private ArrayList<String> getNexts(String s){

        return null;
    }

    private String boardToString(int[][] board){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 2; i ++)
            for(int j = 0; j < 3; j ++)
                sb.append(board[i][j]);
        return sb.toString();
    }
}
