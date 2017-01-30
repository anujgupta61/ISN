package indinasportsnews.com.isnapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static indinasportsnews.com.isnapp.R.id.sharebtn;
import static indinasportsnews.com.isnapp.R.id.toolbar;

public class DetailedNews extends AppCompatActivity {

    String news = " " , newsId , newsHead , newsSumm , newsIcon , newsDetail ;//news_Date
    SwipeRefreshLayout mySwipeRefreshLayout1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);
        Toolbar myToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(Exception ex) {

        }
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnt = new Intent(DetailedNews.this , MainActivity.class) ;
                intnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intnt);
                finish();
            }
        });

        try {
            getSupportActionBar().setTitle("Detailed News");
        } catch(Exception ex) {

        }
        mySwipeRefreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.swiperefresh1) ;

        Intent detailedIntent = this.getIntent();
        if (detailedIntent != null && detailedIntent.hasExtra(Intent.EXTRA_TEXT)) {
            newsId = detailedIntent.getStringExtra(Intent.EXTRA_TEXT);
            newsHead = detailedIntent.getStringExtra("news_head") ;
            newsSumm = detailedIntent.getStringExtra("intro_text") ;
            newsIcon = detailedIntent.getStringExtra("news_icon") ;
            //news_Date = detailedIntent.getStringExtra("news_date") ;
            news = newsSumm ;
            TextView newsTitle = (TextView) findViewById(R.id.news_title) ;
            TextView newsFull = (TextView) findViewById(R.id.news_full) ;
            //TextView newsDate = (TextView) findViewById(R.id.date2) ;
            newsTitle . setText(html2text(newsHead)) ;
            newsFull . setText(html2text(newsSumm)) ;
           /*
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            Date startDate ;
            try {
                startDate = df1.parse(news_Date);
                String newDateString = df2.format(startDate);
                newsDate.setText(newDateString) ;
            } catch (Exception ex) {

            }
            */
            WebView webview = (WebView) findViewById(R.id.news_web_view) ;
            String url = "http://www.indiansportsnews.com/" + newsIcon.replaceAll("\\\\", "") ;
            //String url = newsIcon ;
            String html = "<img src=" + url + " height=\"130px\" width=\"290px\">" ;
            WebSettings webViewSettings = webview.getSettings();
            webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webViewSettings.setJavaScriptEnabled(true);
            webview.loadData(html, "text/html", null);
            fetch() ;
        }
        FloatingActionButton sharebutton = (FloatingActionButton) findViewById(sharebtn);

        sharebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = html2text(news) ;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ISN app");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        mySwipeRefreshLayout1.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fetch() ;
                        finish();
                        startActivity(getIntent());
                    }
                }
        );
    }

    void fetch() {
        String detailed_news = fetchNewsDetailed() ;
        if(detailed_news == null) {
            FetchData();
            saveNewsDetailed();
        } else {
            try {
                newsDetail = detailed_news ;
                TextView newsFull = (TextView) findViewById(R.id.news_full) ;
                newsFull . setText(html2text(newsSumm + newsDetail)) ;
                news = html2text(newsSumm + newsDetail) ;
            } catch (Exception ex) {

            }
        }
    }

    void FetchData() {
        class wrapper {
            //String newsHead;
            String newsDetailed;
            //String newsLogoPath ;
        }

        class SendPostReqAsyncTask extends AsyncTask<String, Void, wrapper> {
            wrapper w;
            //ProgressDialog dialog = ProgressDialog.show(DetailedNews.this , "",
              //      "Opening news ...", true);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySwipeRefreshLayout1 . setRefreshing(true) ;

            /*
                dialog.show();
                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            //finish();
                            dialog.dismiss();
                        }
                        return true;
                    }
                });
            */
            }

            @Override
            protected wrapper doInBackground(String... params) {

                String data = "";
                try {
                    data = URLEncoder.encode("news_id", "UTF-8")
                            + "=" + URLEncoder.encode(newsId , "UTF-8") + "&" +
                            URLEncoder.encode("catid1", "UTF-8")
                            + "=" + URLEncoder.encode("0" , "UTF-8") + "&" +
                            URLEncoder.encode("catid2", "UTF-8")
                            + "=" + URLEncoder.encode("0" , "UTF-8") + "&" +
                            URLEncoder.encode("last_news_id", "UTF-8")
                            + "=" + URLEncoder.encode("0" , "UTF-8") ;
                } catch (UnsupportedEncodingException e) {

                }

                BufferedReader reader = null;
                HttpURLConnection conn = null;

                // Send data
                try {

                    // Defined URL  where to send data
                    URL url = new URL("http://www.indiansportsnews.com/static/fetch_recent_news.php");

                    // Send POST data request

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    // Get the server response

                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    int fetchedData = reader.read();
                    // Reading json string from server
                    String json_str = "{ \"news\": ";
                    while (fetchedData != -1) {
                        char current = (char) fetchedData;
                        fetchedData = reader.read();
                        json_str = json_str + current;
                    }

                    json_str = json_str + "}";
                    final JSONObject obj = new JSONObject(json_str);
                    final JSONArray geodata = obj.getJSONArray("news");
                    final int n = geodata.length();
                    if (n == 0)
                        return null;
                    w = new wrapper();
                    for (int i = 0; i < n; i++) {
                        final JSONObject news = geodata.getJSONObject(i);
                        //w.newsHead = news.getString("title");
                        w.newsDetailed = /*news.getString("introtext") +*/ news.getString("fulltext");
                        //w.newsLogoPath = news.getString("img_src");
                    }

                } catch (Exception j) {
                    Log.v("Expection", "Opening of news");
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                        try {
                            reader.close();
                        } catch (Exception e) {
                            Log.e("ReaderMap", "Reader was not opened");
                        }
                    }
                }
                return w;
            }

            @Override
            protected void onPostExecute(wrapper w) {
                super.onPostExecute(w) ;
                mySwipeRefreshLayout1 . setRefreshing(false) ;
                //dialog . dismiss() ;
                if(w != null) {
                    try {
                        newsDetail = w . newsDetailed ;
                        //TextView newsTitle = (TextView) findViewById(R.id.news_title) ;
                        TextView newsFull = (TextView) findViewById(R.id.news_full) ;
                        //newsTitle . setText(html2text(w . newsHead)) ;
                        newsFull . setText(html2text(newsSumm + w . newsDetailed)) ;
                        news = html2text(newsSumm + w . newsDetailed) ;
                    } catch (Exception ex) {

                    }
                }
            }
        }
        if(checkConnection(getApplicationContext())) {
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute();
        } else {
            Toast. makeText(getApplicationContext() , "No internet" , Toast . LENGTH_SHORT) . show() ;
        }
    }

    public String html2text(String html) {
        html = html . replaceAll("\\<.*?\\>", "");
        return html ;
    }

    boolean checkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected ;
    }

    String fetchNewsDetailed() {
        //Toast.makeText(getApplicationContext() , "fetchLocalNewsData1", Toast.LENGTH_SHORT).show();
        NewsDbHelper resDbHelper = null ;
        SQLiteDatabase mydb = null ;
        try {
            resDbHelper = new NewsDbHelper(getApplicationContext());
            mydb = resDbHelper.getWritableDatabase();
        } catch(Exception ex) {

        }
        String qry = "SELECT " + NewsDBContract.NewsEntry.COLUMN_NAME_FULLTEXT + " FROM " + NewsDBContract.NewsEntry.TABLE_NAME + " WHERE " + NewsDBContract.NewsEntry.COLUMN_NAME_ID + "=" + newsId ;
        Cursor c = mydb.rawQuery(qry , null);
        String newsDetail = null ;
        while(c.moveToNext()) {
            newsDetail = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_FULLTEXT));
        }
        c.close();
        return newsDetail ;
    }

    void saveNewsDetailed() {
        //Toast.makeText(getApplicationContext() , "SaveLocalNewsData1", Toast.LENGTH_SHORT).show();
        NewsDbHelper resDbHelper = null ;
        SQLiteDatabase mydb = null ;
        try {
            resDbHelper = new NewsDbHelper(getApplicationContext());
            mydb = resDbHelper.getWritableDatabase();
        } catch(Exception ex) {

        }

        /*
        Toast.makeText(getContext() , "insertWithOnConflict", Toast.LENGTH_SHORT).show();
        ContentValues values = new ContentValues();
        values.put(NewsDBContract.NewsEntry.COLUMN_NAME_ID, newsId.get(i));
        values.put(NewsDBContract.NewsEntry.COLUMN_NAME_CATID, newsCatId.get(i));
        values.put(NewsDBContract.NewsEntry.COLUMN_NAME_TITLE, newsHeading.get(i));
        values.put(NewsDBContract.NewsEntry.COLUMN_NAME_INTROTEXT, newsSummary.get(i));
        values.put(NewsDBContract.NewsEntry.COLUMN_NAME_ICON_PATH, newsLogoPath.get(i));
        values.put(NewsDBContract.NewsEntry.COLUMN_NAME_DATE, newsDate.get(i));

        //Inserting a new row
        if (mydb != null) {
            long newRowID = mydb.insertWithOnConflict(
                    NewsDBContract.NewsEntry.TABLE_NAME,
                    null ,
                    values ,
                    SQLiteDatabase.CONFLICT_REPLACE
            );
        }
        */
        String qry = "INSERT INTO " + NewsDBContract.NewsEntry.TABLE_NAME + "(" + NewsDBContract.NewsEntry.COLUMN_NAME_FULLTEXT + ") VALUES(" + newsDetail + ") WHERE " + NewsDBContract.NewsEntry.COLUMN_NAME_ID + "=" + newsId ;
        try {
            mydb.execSQL(qry);
        } catch(Exception ex) {

        }

    }

}
