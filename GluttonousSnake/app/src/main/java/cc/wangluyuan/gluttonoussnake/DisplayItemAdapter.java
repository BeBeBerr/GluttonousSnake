package cc.wangluyuan.gluttonoussnake;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.List;

import cc.wangluyuan.gluttonoussnake.Model.GameItemType;

/*
 * Created by Luyuan Wang.
 * Copyright 2018 Luyuan Wang. All rights reserved.
 * 2018/6, Shenzhen.
 */

public class DisplayItemAdapter extends BaseAdapter {

    private List<GameItemType> data;

    private Context context;
    private int resourceId;
    private GridView gridView;


    public DisplayItemAdapter(Context context, int resourceId, GridView gridView, List<GameItemType> objects) {
        this.context = context;
        this.resourceId = resourceId;
        this.gridView = gridView;
        this.data = objects;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GameItemType type = (GameItemType) getItem(position);
        View view = LayoutInflater.from(context).inflate(resourceId, null);
        View displayItemView = view.findViewById(R.id.displayItemView);
        LinearLayout displayItemLayout = view.findViewById(R.id.layout);
        int color = Color.parseColor("#ffffff");
        switch (type) {
            case Empty:
                color = Color.parseColor("#ffffff");
                break;
            case SnakeBody:
                color = Color.parseColor("#00ff00");
                break;
            case Food:
                color = Color.parseColor("#ff0000");
                break;
        }

        int radius = gridView.getWidth() / 10;

        LinearLayout.LayoutParams parmas = new LinearLayout.LayoutParams(radius, radius);
        displayItemLayout.setLayoutParams(parmas);

        displayItemView.setBackgroundColor(color);
        return view;
    }
}
