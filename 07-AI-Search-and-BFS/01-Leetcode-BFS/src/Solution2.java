/// Leetcode 1091
/// 针对这个问答：https://coding.imooc.com/learn/questiondetail/276642.html
/// 对于 [[0]] 的用例不做特判

import java.util.Queue;
import java.util.LinkedList;

class Solution2 {

    private int[][] dirs = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1},
            {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
    private int R, C;

    public int shortestPathBinaryMatrix(int[][] grid) {

        R = grid.length;
        C = grid[0].length;
        boolean[][] visited = new boolean[R][C];
        int[][] dis = new int[R][C];

        if(grid[0][0] == 1) return -1;

        // 不对这种情况做特判
//        if(R == 1 && C == 1) return 1;

        // BFS
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0][0] = true;
        dis[0][0] = 1;
        while(!queue.isEmpty()){
            int cur = queue.remove();
            int curx = cur / C, cury = cur % C;

            if(curx == R - 1 && cury == C - 1)
                return dis[curx][cury];

            for(int d = 0; d < 8; d ++){
                int nextx = curx + dirs[d][0];
                int nexty = cury + dirs[d][1];
                if(inArea(nextx, nexty) && !visited[nextx][nexty] && grid[nextx][nexty] == 0){
                    queue.add(nextx * C + nexty);
                    visited[nextx][nexty] = true;
                    dis[nextx][nexty] = dis[curx][cury] + 1;
                }
            }
        }
        return -1;
    }

    private boolean inArea(int x, int y){
        return x >= 0 && x < R && y >= 0 && y < C;
    }
}