package indinasportsnews.com.isnapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

class GridItemView extends FrameLayout {

    private TextView textView ;

    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_grid , this);
        textView = (TextView) getRootView().findViewById(R.id.text);
    }

    public void display(String text, boolean isSelected) {
        textView.setText(text);
        display(isSelected);
    }

    public void display(boolean isSelected) {
        if(isSelected) {
            textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.skyblue));
        }
    }
}
