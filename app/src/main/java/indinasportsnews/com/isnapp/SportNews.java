package indinasportsnews.com.isnapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.ArrayList;

import static indinasportsnews.com.isnapp.R.id.toolbar;

public class SportNews extends AppCompatActivity {

    ArrayList<String> newsHeading = new ArrayList<>() ;
    ArrayList<String> newsSummary = new ArrayList<>() ;
    ArrayList<Integer> newsId = new ArrayList<>() ;
    ArrayList<String> newsLogoPath = new ArrayList<>() ;
    //ArrayList<String> newsDate = new ArrayList<>() ;
    CustomListAdapter sport_adapter ;
    ListView listview ;
    int catid1 = 0 , catid2 = 0 ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_news);
        Toolbar myToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnt = new Intent(SportNews.this , MainActivity.class) ;
                intnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intnt);
                finish();
            }
        });
        Intent sportIntent = this.getIntent();
        String title = null ;
        if (sportIntent != null) {
            catid1 = sportIntent.getIntExtra("catid1" , 0) ;
            catid2 = sportIntent.getIntExtra("catid2" , 0) ;
            title = sportIntent.getStringExtra("title") ;
        }
        try {
            getSupportActionBar().setTitle(title);
        } catch(Exception ex) {

        }
        //FetchSportData();
        fetchLocalNewsData();
        sport_adapter = new CustomListAdapter(this , newsHeading , newsSummary , newsLogoPath /*, newsDate*/) ;
        listview = (ListView) findViewById(R.id.listview_sport_news) ;
        try {
            listview.setAdapter(sport_adapter);
        } catch(Exception ex) {

        }
        listview . setOnItemClickListener(new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView , View view , int pos , long l) {
                int news_id = newsId . get(pos) ;
                Intent intent = new Intent(SportNews.this , DetailedNews.class) ;
                intent . putExtra(Intent . EXTRA_TEXT , news_id + "") ;
                intent . putExtra("news_head" , newsHeading . get(pos)) ;
                intent . putExtra("intro_text" , newsSummary . get(pos)) ;
                intent . putExtra("news_icon" , newsLogoPath . get(pos)) ;
                //intent . putExtra("news_date" , newsDate . get(pos)) ;
                startActivity(intent) ;
            }
        } ) ;
    }

    void FetchSportData() {
        class wrapper {
            String newsHead ;
            String newsDesc ;
            int newsId ;
            String newsLogoPath ;
        }

        class SendPostReqAsyncTask extends AsyncTask<String, Void, wrapper[]> {
            wrapper[] w ;
            ProgressDialog dialog = ProgressDialog.show(SportNews.this , "", "Fetching news ...", true);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
            }
            @Override
            protected wrapper[] doInBackground(String... params) {

                String data = "";
                try {
                    data = URLEncoder.encode("catid1", "UTF-8")
                            + "=" + URLEncoder.encode(catid1 + "" , "UTF-8") + "&" +
                            URLEncoder.encode("catid2", "UTF-8")
                            + "=" + URLEncoder.encode(catid2 + "" , "UTF-8") ;
                } catch (UnsupportedEncodingException e) {

                }

                BufferedReader reader = null;
                HttpURLConnection conn = null;

                // Send data
                try {

                    // Defined URL  where to send data
                    URL url = new URL("https://anujgupta200463.000webhostapp.com/fetch_sport_news.php");

                    // Send POST data request

                    conn =(HttpURLConnection) url.openConnection();
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
                    if(n == 0)
                        return null ;
                    w = new wrapper[n];
                    for (int i = 0; i < n; i++) {
                        final JSONObject news = geodata.getJSONObject(i);
                        w[i] = new wrapper();
                        w[i].newsHead = news.getString("title");
                        w[i].newsDesc = news.getString("introtext");
                        w[i].newsId = news . getInt("id") ;
                        w[i].newsLogoPath = news.getString("img_src");
                    }

                } catch (Exception j){
                    Log.v("Expection","Fetching of news");
                }
                finally {
                    if (conn != null) {
                        conn.disconnect();
                        try {
                            reader.close();
                        }catch (Exception e){
                            Log.e("ReaderMap","Reader was not opened");
                        }
                    }
                }
                return w;
            }

            @Override
            protected void onPostExecute(wrapper w[]) {
                super.onPostExecute(w) ;
                dialog . dismiss() ;
                if(w != null) {
                    for (int i = 0; i < w.length; i++) {
                        try {
                            newsHeading.add(w[i].newsHead);
                            newsSummary.add(w[i].newsDesc);
                            newsId.add(w[i].newsId);
                            newsLogoPath.add(w[i].newsLogoPath);
                        } catch(Exception ex) {

                        }
                    }
                }
                sport_adapter . notifyDataSetChanged() ;
            }
        }
        if(checkConnection(getApplicationContext())) {
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute();
        } else {
            Toast. makeText(getApplicationContext() , "No internet" , Toast . LENGTH_SHORT) . show() ;
        }
    }

    boolean checkConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected ;
    }
    
    void fetchLocalNewsData() {
        NewsDbHelper resDbHelper = new NewsDbHelper(getApplicationContext());
        SQLiteDatabase mydb = resDbHelper.getReadableDatabase();
        String qry = "SELECT id , title , introtext , icon_path FROM news WHERE ( catid = " + catid1 + " OR catid = " + catid2 + " ) ORDER BY id DESC" ; // AND date >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) ORDER BY date DESC" ;
        Cursor c = mydb.rawQuery(qry , null);
        //Toast.makeText(this, "News is being fetched locally ...", Toast.LENGTH_LONG).show();
        while(c.moveToNext()) {
            String str ;
            int id = c.getInt(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_ID));
            String title = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_TITLE));
            String introtext = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_INTROTEXT));
            String icon_path = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_ICON_PATH));
            newsId . add(id) ;
            newsHeading . add(title) ;
            newsSummary . add(introtext) ;
            newsLogoPath . add(icon_path) ;
            /*
            try {
                String news_date = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_DATE));
                newsDate.add(news_date);
            } catch(Exception ex) {

            }
            */
        }
    }
}
