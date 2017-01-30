package indinasportsnews.com.isnapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private String[] strings;
    public List selectedPositions;


    public GridAdapter(String[] strings, Context mContext) {
        this.strings = strings;
        this.mContext = mContext;
        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItemView customView = (convertView == null) ? new GridItemView(mContext) : (GridItemView) convertView;
        customView.display(strings[position], selectedPositions.contains(position));

        return customView ;
    }

}