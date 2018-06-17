package cc.wangluyuan.gluttonoussnake.Model;

/*
 * Created by Luyuan Wang.
 * Copyright 2018 Luyuan Wang. All rights reserved.
 * 2018/6, Shenzhen.
 */

public interface GameHandlerDelegate {
    void onGameOver();
    void onScoreChanged(int score);
}
