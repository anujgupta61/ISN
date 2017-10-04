package indinasportsnews.com.isnapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> news_title = new ArrayList<>() ;
    //private ArrayList<String> news_logo_path = new ArrayList<>() ;
    private ArrayList<String> news_summary = new ArrayList<>() ;
    //private ArrayList<String> news_date = new ArrayList<>() ;

    public CustomListAdapter(Activity context, ArrayList<String> news_title , ArrayList<String> news_summary , ArrayList<String> news_logo_path/* , ArrayList<String> news_date*/) {
        super(context, R.layout.list_item_recent_news , news_title);
        this . context=context;
        this . news_title = news_title ;
        //this . news_logo_path = news_logo_path ;
        this . news_summary = news_summary ;
        // this . news_date = news_date ;
    }

    public String html2text(String html) {
        html = html . replaceAll("\\<.*?\\>", "");
        return html ;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_recent_news, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.news_heading);
        TextView extratxt = (TextView) rowView.findViewById(R.id.news_summary);
  //      TextView date_text = (TextView) rowView.findViewById(R.id.date);

        if(news_summary != null && news_title != null) {
            /*
            WebView webview = (WebView) rowView.findViewById(R.id.news_web_view) ;
            String url = "http://www.indiansportsnews.com/" + news_logo_path.get(position).replaceAll("\\\\", "");
            //String url = news_logo_path . get(position) ;
            String html = "<img src=" + url + " height=\"80px\" width=\"85px\">" ;
            WebSettings webViewSettings = webview.getSettings();
            webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webViewSettings.setJavaScriptEnabled(true);
            webview.loadData(html, "text/html", null);
            */
            txtTitle.setText(html2text(news_title.get(position)));
            extratxt.setText(html2text(news_summary.get(position)));
            /*
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            Date startDate ;
            try {
                startDate = df1.parse(news_date.get(position));
                String newDateString = df2.format(news_date.get(position));
                date_text.setText(newDateString) ;
            } catch (Exception ex) {

            }
            */
        }
        return rowView;
    }
}
