package indinasportsnews.com.isnapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Set;

public class RecentNewsFragment extends Fragment{

    ArrayList<String> newsHeading = new ArrayList<>() ;
    ArrayList<String> newsSummary = new ArrayList<>() ;
    ArrayList<Integer> newsId = new ArrayList<>() ;
    ArrayList<String> newsLogoPath = new ArrayList<>() ;
    ArrayList<Integer> newsCatId = new ArrayList<>() ;
    //ArrayList<String> newsDate = new ArrayList<>() ;
    ArrayList<String> selectedStrings = new ArrayList<>() ;

    SwipeRefreshLayout mySwipeRefreshLayout ;
    //String lastNewsDate = null ;

    CustomListAdapter adapter ;
    ListView listview ;
    //Boolean show_dialog = true ;
    public RecentNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //String lastNews = SaveSharedPreferences . getLastDate(getContext()) ;
        //Toast.makeText(getContext() , "Last News Date 3 : " + lastNews , Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recent_news , container, false) ;
        mySwipeRefreshLayout = (SwipeRefreshLayout) rootView . findViewById(R.id.swiperefresh) ;
        Set set = SaveSharedPreferences . getPrefSportsList(getContext()) ;
        try {
            selectedStrings.addAll(set);
        } catch(Exception ex) {

        }
        fetchNewsData() ;
        adapter = new CustomListAdapter(getActivity() , newsHeading , newsSummary , newsLogoPath/* , newsDate*/) ;
        listview = (ListView) rootView.findViewById(R.id.listview_recent_news) ;
        try {
            listview.setAdapter(adapter);
        } catch(Exception ex) {

        }
        listview . setOnItemClickListener(new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView , View view , int pos , long l) {
                int news_id = newsId . get(pos) ;
                Intent intent = new Intent(getActivity() , DetailedNews.class) ;
                intent . putExtra(Intent . EXTRA_TEXT , news_id + "") ;
                intent . putExtra("news_head" , newsHeading . get(pos)) ;
                intent . putExtra("intro_text" , newsSummary . get(pos)) ;
                intent . putExtra("news_icon" , newsLogoPath . get(pos)) ;
                //intent . putExtra("news_date" , newsDate . get(pos)) ;
                startActivity(intent) ;
            }
        } ) ;
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //Toast.makeText(getContext(), "News updating 3 ...", Toast.LENGTH_SHORT).show();
                        fetchNewsData();
                        mySwipeRefreshLayout . setRefreshing(false) ;
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                }
        );
        return rootView ;
    }

    void fetchNewsData() {
        newsHeading . clear() ;
        newsSummary . clear() ;
        newsLogoPath . clear() ;
        //newsDate . clear() ;
        for(int i = 0 ; i < selectedStrings . size() ; i ++) {
            String title = selectedStrings . get(i) ;
            int catid1 = 0 , catid2 = 0 ;
            switch(title) {
                case "Cricket" : {
                    catid1 = 908 ;
                    catid2 = 983 ;
                    break ;
                }
                case "Football" : {
                    catid1 = 28 ;
                    catid2 = 1025 ;
                    break ;
                }
                case "Basketball" : {
                    catid1 = 44 ;
                    catid2 = 979 ;
                    break ;
                }
                case "Badminton" : {
                    catid1 = 1007 ;
                    catid2 = 0 ;
                    break ;
                }
                case "Chess" : {
                    catid1 = 26 ;
                    catid2 = 1043 ;
                    break ;
                }
                case "Table Tennis" : {
                    catid1 = 32 ;
                    catid2 = 1050 ;
                    break ;
                }
                case "Golf" : {
                    catid1 = 29 ;
                    catid2 = 975 ;
                    break ;
                }
                case "Hockey" : {
                    catid1 = 986 ;
                    catid2 = 1035 ;
                    break ;
                }
                case "Boxing" : {
                    catid1 = 27 ;
                    catid2 = 1029 ;
                    break ;
                }
                case "Tennis" : {
                    catid1 = 30 ;
                    catid2 = 1024 ;
                    break ;
                }
                case "Racing" : {
                    catid1 = 33 ;
                    catid2 = 1026 ;
                    break ;
                }
                case "Shooting" : {
                    catid1 = 995 ;
                    catid2 = 1042 ;
                    break ;
                }
                case "Wrestling" : {
                    catid1 = 998 ;
                    catid2 = 0 ;
                    break ;
                }
                case "Athletics" : {
                    catid1 = 1002 ;
                    catid2 = 982 ;
                    break ;
                }
                case "Snooker/Billiards" : {
                    catid1 = 1006 ;
                    catid2 = 45 ;
                    break ;
                }
                case "BLOG" : {
                    catid1 = 1065 ;
                    catid2 = 0 ;
                    break ;
                }
            }
            fetchLocalNewsData(catid1 , catid2);
            FetchData(catid1 , catid2) ;
        }
    }

    void FetchData(final int catid1 , final int catid2) {
        //Toast.makeText(getContext() , "FetchData", Toast.LENGTH_SHORT).show();
        class wrapper {
            String newsHead ;
            String newsDesc ;
            int news_Id ;
            String newsPath ;
            //String news_date ;
        }

        class SendPostReqAsyncTask extends AsyncTask<String, Void, wrapper[]> {
            wrapper[] w ;

            //ProgressDialog dialog ;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mySwipeRefreshLayout . setRefreshing(true) ;
                /*
                if(show_dialog) {
                    dialog = ProgressDialog.show(getActivity() , "",
                            "Fetching news ...", true);
                    dialog.show();
                    dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface arg0, int keyCode,
                                             KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                //getActivity().finish();
                                dialog.dismiss();
                            }
                            return true;
                        }
                    });
                    show_dialog = false ;
                }
                */
            }
            @Override
            protected wrapper[] doInBackground(String... params) {

                String data = "";
                try {
                    data = URLEncoder.encode("news_id", "UTF-8")
                            + "=" + URLEncoder.encode("-1" , "UTF-8") + "&" +
                            URLEncoder.encode("catid1", "UTF-8")
                            + "=" + URLEncoder.encode(catid1 + "" , "UTF-8") + "&" +
                            URLEncoder.encode("catid2", "UTF-8")
                            + "=" + URLEncoder.encode(catid2 + "" , "UTF-8") + "&" +
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
                        w[i].news_Id = news. getInt("id") ;
                        w[i].newsPath = news.getString("img_src");
                        //w[i].news_date = news.getString("created") ;
                    }

                } catch (Exception j){
                    Log.v("Expection","Fetching of news");
                }
                finally {
                    if (conn != null) {
                        conn.disconnect();
                        try {
                            reader.close();
                        } catch (Exception e){
                            Log.e("ReaderMap","Reader was not opened");
                        }
                    }
                }
                return w;
            }

            @Override
            protected void onPostExecute(wrapper w[]) {
                super.onPostExecute(w) ;
                try {
                    mySwipeRefreshLayout . setRefreshing(false) ;
                    //dialog.dismiss();
                } catch(Exception ex) {

                }
                /*
                Date lastDate = null ;
                String lastNewsDate1 = null ;
                try {
                    lastNewsDate1 = SaveSharedPreferences.getLastDate(getContext());
                } catch(Exception ex) {

                }
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    lastDate = df1.parse(lastNewsDate1);
                } catch (Exception ex) {

                }
                */
                if(w != null) {
                    for (int i = 0; i < w.length; i++) {
                        try {
                            /*
                            Date currDate = null ;
                            try {
                                currDate = df1.parse(w[i].news_date);
                            } catch(Exception ex) {

                            }
                            Boolean cond ;
                            if(lastDate != null)
                                cond = lastDate . before(currDate) ;
                            else
                                cond = true ;

                            if(cond) {
                            */
                                newsHeading.add(w[i].newsHead);
                                newsSummary.add(w[i].newsDesc);
                                newsId.add(w[i].news_Id);
                                newsLogoPath.add(w[i].newsPath);
                                newsCatId.add(catid1);
                                //newsDate.add(w[i].news_date);
                                //lastNewsDate1 = w[i].news_date ;
                            //}
                        } catch(Exception ex) {

                        }
                    }
                    /*
                    try {
                        SaveSharedPreferences.setLastDate(getContext(), lastNewsDate1);
                    } catch(Exception ex) {

                    }
                    */
                    saveNewsLocally() ;
                }
                adapter . notifyDataSetChanged() ;
            }
        }
        if(checkConnection(getContext())) {
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute();
        } else {
            Toast. makeText(getContext() , "No internet" , Toast . LENGTH_SHORT) . show() ;
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

    void fetchLocalNewsData(int catid1 , int catid2) {
        //Toast.makeText(getContext() , "fetchLocalNewsData", Toast.LENGTH_SHORT).show();
        NewsDbHelper resDbHelper = new NewsDbHelper(getContext());
        SQLiteDatabase mydb = resDbHelper.getReadableDatabase();
        String qry = "SELECT id , title , introtext , icon_path FROM news WHERE ( catid = " + catid1 + " OR catid = " + catid2 + " ) ORDER BY id DESC" ; // AND date >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) ORDER BY date DESC" ;
        Cursor c = mydb.rawQuery(qry , null);
        /*String last_news_date = SaveSharedPreferences . getLastDate(getContext()) ;
        Date lastDate = null ;
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            lastDate = df1.parse(last_news_date);
        } catch (Exception ex) {

        }
        */
        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_ID));
            String title = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_TITLE));
            String introtext = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_INTROTEXT));
            String icon_path = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_ICON_PATH));
            /*
            String news_date = null ;
            Date currDate = null ;
            try {
                news_date = c.getString(c.getColumnIndexOrThrow(NewsDBContract.NewsEntry.COLUMN_NAME_DATE));
                currDate = df1.parse(news_date);
            } catch(Exception ex) {

            }
            Boolean cond ;
            if(lastDate != null && currDate != null)
                cond = lastDate . before(currDate) ;
            else
                cond = true ;
            if(cond) {
            */
                //newsDate.add(news_date);
                newsId.add(id);
                newsHeading.add(title);
                newsSummary.add(introtext);
                newsLogoPath.add(icon_path);
              //  lastNewsDate = news_date ;
            //}
        }
        try {
            adapter.notifyDataSetChanged();
        } catch(Exception ex) {

        }
        c.close();
    }
    
    void saveNewsLocally() {
        NewsDbHelper resDbHelper = null ;
        SQLiteDatabase mydb = null ;
        try {
            resDbHelper = new NewsDbHelper(getContext());
            mydb = resDbHelper.getWritableDatabase();
        } catch(Exception ex) {

        }

        for (int i = 0; i < newsId.size(); i++) {
            // Creating a new map
            try {
                //Toast.makeText(getContext() , "insertWithOnConflict", Toast.LENGTH_SHORT).show();
                ContentValues values = new ContentValues();
                values.put(NewsDBContract.NewsEntry.COLUMN_NAME_ID, newsId.get(i));
                values.put(NewsDBContract.NewsEntry.COLUMN_NAME_CATID, newsCatId.get(i));
                values.put(NewsDBContract.NewsEntry.COLUMN_NAME_TITLE, newsHeading.get(i));
                values.put(NewsDBContract.NewsEntry.COLUMN_NAME_INTROTEXT, newsSummary.get(i));
                values.put(NewsDBContract.NewsEntry.COLUMN_NAME_ICON_PATH, newsLogoPath.get(i));
                //values.put(NewsDBContract.NewsEntry.COLUMN_NAME_DATE, newsDate.get(i));

                //Inserting a new row
                if (mydb != null) {
                    long newRowID = mydb.insertWithOnConflict(
                            NewsDBContract.NewsEntry.TABLE_NAME,
                            null ,
                            values ,
                            SQLiteDatabase.CONFLICT_REPLACE
                    );
                }

            } catch(Exception ex) {

            }
        }
    }
}
