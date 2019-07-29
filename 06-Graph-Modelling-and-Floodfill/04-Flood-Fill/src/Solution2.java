/// Leetcode 695

class Solution2 {

    private int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private int R, C;
    private int[][] grid;
    private boolean[][] visited;

    public int maxAreaOfIsland(int[][] grid){

        if(grid == null) return 0;
        R = grid.length;
        if(R == 0) return 0;

        C = grid[0].length;
        if(C == 0) return 0;

        this.grid = grid;

        visited = new boolean[R][C];
        int res = 0;
        for(int i = 0; i < R; i ++)
            for(int j = 0; j < C; j ++)
                if(grid[i][j] == 1 && !visited[i][j])
                    res = Math.max(res, dfs(i, j));
        return res;
    }

    private int dfs(int x, int y){

        visited[x][y] = true;
        int res = 1;
        for(int d = 0; d < 4; d ++){
            int nextx = x + dirs[d][0], nexty = y + dirs[d][1];
            if(inArea(nextx, nexty) && grid[nextx][nexty] == 1 && !visited[nextx][nexty])
                res += dfs(nextx, nexty);
        }
        return res;
    }

    private boolean inArea(int x, int y){
        return x >= 0 && x < R && y >= 0 && y < C;
    }
}
