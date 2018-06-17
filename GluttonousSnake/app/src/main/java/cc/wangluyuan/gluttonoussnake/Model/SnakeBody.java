package cc.wangluyuan.gluttonoussnake.Model;

/*
 * Created by Luyuan Wang.
 * Copyright 2018 Luyuan Wang. All rights reserved.
 * 2018/6, Shenzhen.
 */

public class SnakeBody {
    int x = 0;
    int y = 0;
    private SnakeBody nextBody = null; //由蛇头指向蛇尾

    public SnakeBody(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SnakeBody getNextBody() {
        return nextBody;
    }

    public void setNextBody(SnakeBody nextBody) {
        this.nextBody = nextBody;
    }
}
