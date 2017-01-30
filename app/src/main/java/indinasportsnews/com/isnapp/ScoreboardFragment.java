package indinasportsnews.com.isnapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ScoreboardFragment extends Fragment{

    public ScoreboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_scores , container, false) ;
        WebView webview = (WebView) rootView . findViewById(R.id.score_web_view) ;
        String html = "<center><iframe src=\"http://cricketlive.indianexpress.com/other-matches.html\" width=\"300\" height=\"190\" scrolling=\"no\">Sorry No Frame</iframe></center>" ;
        WebSettings webViewSettings = webview.getSettings();
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setBuiltInZoomControls(true);
        webview.loadData(html, "text/html", null);
        return rootView ;
    }
}