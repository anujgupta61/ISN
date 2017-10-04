package indinasportsnews.com.isnapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

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
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyService extends Service {
    private Notification _foregroundNotification;
    int notificationId = 1 ;
    ArrayList<String> newsHeading = new ArrayList<>() ;
    ArrayList<String> newsSummary = new ArrayList<>() ;
    ArrayList<Integer> newsId = new ArrayList<>() ;
    Boolean notify = false ;
    //ArrayList<String> newsLogoPath = new ArrayList<>() ;
    int lastNewsId ;

    @Override
    public IBinder onBind(Intent intent) {
        // Service is not binded to any activity , hence no binding required
        return null;
    }

    private void startInForeground() {
        int notification_icon = R.mipmap.ic_launcher ;
        String notificationTrickertext = "ISN app about to start";
        long notificationTimeStamp = System.currentTimeMillis();
        String notificationTitleText = "ISN App";
        String notificationBodyText = "ISN App is Currently running";
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        _foregroundNotification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(notification_icon)
                .setTicker(notificationTrickertext)
                .setWhen(notificationTimeStamp)
                .setContentText(notificationBodyText)
                .setContentTitle(notificationTitleText)
                .setContentIntent(notificationPendingIntent)
                .build();
        startForeground(notificationId, _foregroundNotification);
    }

    // Function to check Internet connectivity
    boolean checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected ;
    }

    class FetchNotification extends TimerTask {
        public void run() {
            Set set = SaveSharedPreferences . getPrefSportsList(getApplicationContext()) ;
            ArrayList<String> selectedStrings = new ArrayList<>() ;
            try {
                selectedStrings.addAll(set);
            } catch(Exception ex) {

            }
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
                FetchNews(catid1 , catid2);
            }
            for(int i = 0 ; i < newsHeading . size() ; i ++) {
                Log.v("ISN : " , notify + "") ;
                if(notify)
                    show_notification(newsHeading.get(i), newsSummary.get(i), newsId.get(i));
                if(newsId . get(i) >= lastNewsId) {
                    lastNewsId = newsId . get(i) ;
                    SaveSharedPreferences . setLastId(getApplicationContext() , lastNewsId);
                }
            }
            if(! notify)
                notify = true ;
            newsId . clear() ;
            newsHeading . clear() ;
            newsSummary . clear() ;
            //newsLogoPath . clear() ;
        }
    }

    public String html2text(String html) {
        html = html . replaceAll("\\<.*?\\>", "");
        return html ;
    }

    void FetchNews(final int catid1 , final int catid2) {
        class wrapper {
            String newsHead ;
            String newsDesc ;
            int newsId ;
            //String newsLogoPath ;
        }

        class SendPostReqAsyncTask extends AsyncTask<String, Void, wrapper[]> {
            wrapper[] w ;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected wrapper[] doInBackground(String... params) {

                String data = "";
                try {
                    data = URLEncoder.encode("news_id", "UTF-8")
                            + "=" + URLEncoder.encode("-2" , "UTF-8") + "&" +
                            URLEncoder.encode("catid1", "UTF-8")
                            + "=" + URLEncoder.encode(catid1 + "" , "UTF-8") + "&" +
                            URLEncoder.encode("catid2", "UTF-8")
                            + "=" + URLEncoder.encode(catid2 + "" , "UTF-8") + "&" +
                            URLEncoder.encode("last_news_id", "UTF-8")
                            + "=" + URLEncoder.encode(lastNewsId + "" , "UTF-8") ;
                } catch (UnsupportedEncodingException e) {

                }


                BufferedReader reader = null;
                HttpURLConnection conn = null;

                // Send data
                try {

                    // Defined URL  where to send data
                    URL url = new URL("https://anujgupta200463.000webhostapp.com/fetch_recent_news.php");

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
                        w[i].newsId = news.getInt("id") ;
                        //w[i].newsLogoPath = news.getString("img_src");
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
                if(w != null) {
                    for (int i = 0; i < w.length; i++) {
                        try {
                            newsHeading.add(html2text(w[i].newsHead));
                            newsSummary.add(html2text(w[i].newsDesc));
                            newsId.add(w[i].newsId);
                            //newsLogoPath.add(w[i].newsLogoPath);
                        } catch(Exception ex) {

                        }
                    }
                }
            }
        }
        if(checkConnection(getApplicationContext())) {
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute();
        }
    }

    void show_notification(String newsHead , String newsSum , int newsId) {
        // type 1 means emergency alert
        int notification_icon = R.mipmap.ic_launcher ;
        String notificationTitleText ;
        String notificationTrickertext = "ISN app about to start" ;
        notificationTitleText = newsHead ;
        long notificationTimeStamp = System.currentTimeMillis();
        String notificationBodyText = newsSum ;
        Intent intent = new Intent(this , DetailedNews.class) ;
        intent . setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        intent . putExtra(Intent . EXTRA_TEXT , newsId + "") ;

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, intent , PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ;

        Notification Notification1 = new Notification.Builder(getApplicationContext())
                .setSmallIcon(notification_icon)
                .setTicker(notificationTrickertext)
                .setWhen(notificationTimeStamp)
                .setContentText(notificationBodyText)
                .setContentTitle(notificationTitleText)
                .setContentIntent(notificationPendingIntent)
                .setSound(alarmSound)
                .build();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId + 1 , Notification1);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lastNewsId = SaveSharedPreferences . getLastId(getApplicationContext()) ;
        if(lastNewsId == 0)
            notify = false ;
        else
            notify = true ;
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new FetchNotification(), 0, 500);
        startInForeground(); // start in foreground
    }
}