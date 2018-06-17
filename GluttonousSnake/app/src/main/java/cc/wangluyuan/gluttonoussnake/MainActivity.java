package cc.wangluyuan.gluttonoussnake;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import cc.wangluyuan.gluttonoussnake.Model.Direction;
import cc.wangluyuan.gluttonoussnake.Model.GameHandler;
import cc.wangluyuan.gluttonoussnake.Model.GameHandlerDelegate;
import cc.wangluyuan.gluttonoussnake.Model.GameItemType;

/*
 * Created by Luyuan Wang.
 * Copyright 2018 Luyuan Wang. All rights reserved.
 * 2018/6, Shenzhen.
 */

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GameHandlerDelegate {

    private GameHandler gameHandler = new GameHandler();

    private GameItemType[] gameData = new GameItemType[100];
    private DisplayItemAdapter adapter;
    private GridView gridView;
    private TextView scoreTextView;

    private GestureDetector gestureDetector;

    private Timer timer;

    private static final int TAG = 1;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureDetector = new GestureDetector(this, this);

        gameHandler.delegate = this;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case TAG:
                        gameHandler.go();
                        refreshGameDisplay();
                        break;
                }
            }
        };

        initTimer();

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setEnabled(false);

        scoreTextView = (TextView) findViewById(R.id.textView);
        scoreTextView.setEnabled(false);

        adapter = new DisplayItemAdapter(MainActivity.this, R.layout.display_item, gridView, Arrays.asList(gameData));

        gridView.setAdapter(adapter);

        refreshGameDisplay();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        float startX = motionEvent.getX();
        float startY = motionEvent.getY();
        float endX = motionEvent1.getX();
        float endY = motionEvent1.getY();

        float deltaX = endX - startX;
        float deltaY = endY - startY;

        if(Math.abs(deltaY) > Math.abs(deltaX)) {
            if(deltaY > 0) {
                gameHandler.setCurrentDirection(Direction.Down);
            } else {
                gameHandler.setCurrentDirection(Direction.Up);
            }
        } else {
            if(deltaX > 0) {
                gameHandler.setCurrentDirection(Direction.Right);
            } else {
                gameHandler.setCurrentDirection(Direction.Left);
            }
        }

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }



    public void refreshGameDisplay() {
        for(int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                this.gameData[i*10 + j] = gameHandler.getGameData()[i][j];
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(TAG);
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Override
    public void onGameOver() {
        timer.cancel();
        timer = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("游戏结束")
                .setMessage("是否再来一局？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameHandler.clean();
                        initTimer();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onScoreChanged(int score) {
        scoreTextView.setText("当前得分：" + score);
    }

}
