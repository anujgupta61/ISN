package indinasportsnews.com.isnapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

class GridItemView extends FrameLayout {

    private TextView textView;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.sports_selection, this);
        textView = (TextView) getRootView().findViewById(R.id.text);
    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        textView.setBackgroundResource(isSelected ? R.drawable.square1 : R.drawable.square2);
    }
}