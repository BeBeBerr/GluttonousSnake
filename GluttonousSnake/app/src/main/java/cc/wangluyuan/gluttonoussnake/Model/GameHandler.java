package cc.wangluyuan.gluttonoussnake.Model;

/*
 * Created by Luyuan Wang.
 * Copyright 2018 Luyuan Wang. All rights reserved.
 * 2018/6, Shenzhen.
 */

public class GameHandler {

    private GameItemType[][] currentGameData = new GameItemType[10][10];
    private Direction currentDirection = Direction.Down;

    private int foodX = 0;
    private int foodY = 0;

    private SnakeBody snakeHead = new SnakeBody(0, 0);
    private int snakeLength = 1;

    public GameHandlerDelegate delegate = null;


    public GameHandler() {
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                currentGameData[i][j] = GameItemType.Empty;
            }
        }
        currentGameData[0][0] = GameItemType.SnakeBody; //蛇的初始位置设置为左上角
        snakeHead.x = 0;
        snakeHead.y = 0;

        generateFoodRandomly();
    }

    private void generateFoodRandomly() {
        int row = (int) (Math.random() * 10);
        int col = (int) (Math.random() * 10);
        if(currentGameData[row][col] != GameItemType.Empty) {
            generateFoodRandomly();
        } else {
            currentGameData[row][col] = GameItemType.Food;
        }
        this.foodX = col;
        this.foodY = row;
    }

    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }

    public void go() {
        GameItemType frontItem = checkFrontItem(currentDirection);
        switch(frontItem) {
            case SnakeBody:
                //todo: 反向前进则继续走
                onGameOver(); //撞到自己，游戏结束
                return;
            case Empty:
                moveSnake(currentDirection);
                break;
            case Food:
                onEatFood();
                break;
        }
        //this.currentDirection = direction;
    }



    private void onEatFood() {
        SnakeBody newHead = new SnakeBody(foodX, foodY); //吃到食物，生长一节身体
        newHead.setNextBody(snakeHead);
        snakeHead = newHead;
        currentGameData[foodY][foodX] = GameItemType.SnakeBody;
        snakeLength += 1;

        generateFoodRandomly();

        delegate.onScoreChanged(snakeLength);
    }

    private void moveSnake(Direction direction) {
        //向前走一步
        SnakeBody tailBody = getSnakeBodyWithOffset(snakeLength - 1);
        SnakeBody secondLastBody = getSnakeBodyWithOffset(snakeLength - 2);
        secondLastBody.setNextBody(null); //和尾部断开
        currentGameData[tailBody.y][tailBody.x] = GameItemType.Empty;
        tailBody.setNextBody(snakeHead); //尾部变头部
        switch(direction) {
            case Up:
                if (snakeHead.y == 0) {
                    tailBody.x = snakeHead.x;
                    tailBody.y = 9; //翻到下面
                } else {
                    tailBody.x = snakeHead.x;
                    tailBody.y = snakeHead.y - 1;
                }
                break;
            case Right:
                if (snakeHead.x == 9) {
                    tailBody.y = snakeHead.y;
                    tailBody.x = 0;
                } else {
                    tailBody.y = snakeHead.y;
                    tailBody.x = snakeHead.x + 1;
                }
                break;
            case Down:
                if (snakeHead.y == 9) {
                    tailBody.x = snakeHead.x;
                    tailBody.y = 0;
                } else {
                    tailBody.x = snakeHead.x;
                    tailBody.y = snakeHead.y + 1;
                }
                break;
            case Left:
                if (snakeHead.x == 0) {
                    tailBody.y = snakeHead.y;
                    tailBody.x = 9;
                } else {
                    tailBody.y = snakeHead.y;
                    tailBody.x = snakeHead.x - 1;
                }
                break;
        }
        this.snakeHead = tailBody;
        currentGameData[snakeHead.y][snakeHead.x] = GameItemType.SnakeBody;
    }

    private GameItemType checkFrontItem(Direction direction) {
        //检查前方物体
        GameItemType result = GameItemType.Empty;
        switch(direction) {
            case Up:
                if(snakeHead.y == 0) {
                    result = currentGameData[9][snakeHead.x]; //翻到下面
                } else {
                    result = currentGameData[snakeHead.y - 1][snakeHead.x];
                }
                break;
            case Right:
                if(snakeHead.x == 9) {
                    result = currentGameData[snakeHead.y][0];
                } else {
                    result = currentGameData[snakeHead.y][snakeHead.x + 1];
                }
                break;
            case Down:
                if(snakeHead.y == 9) {
                    result = currentGameData[0][snakeHead.x];
                } else {
                    result = currentGameData[snakeHead.y + 1][snakeHead.x];
                }
                break;
            case Left:
                if(snakeHead.x == 0) {
                    result = currentGameData[snakeHead.y][9];
                } else {
                    result = currentGameData[snakeHead.y][snakeHead.x - 1];
                }
                break;
        }
        return result;
    }

    private void onGameOver() {
        if(this.delegate != null) {
            delegate.onGameOver();
        }
    }

    private SnakeBody getSnakeBodyWithOffset(int offset) {
        if(offset < 0) {
            offset = 0;
        }
        SnakeBody body = snakeHead;
        for(int i=0; i<offset; i++) {
            body = body.getNextBody();
        }
        return body;
    }

    public GameItemType[][] getGameData() {
        return currentGameData;
    }

    public void clean() {
        snakeHead = new SnakeBody(0, 0);
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                currentGameData[i][j] = GameItemType.Empty;
            }
        }
        snakeLength = 1;
        currentDirection = Direction.Down;
        currentGameData[0][0] = GameItemType.SnakeBody; //蛇的初始位置设置为左上角
        generateFoodRandomly();

        delegate.onScoreChanged(1);
    }

}
