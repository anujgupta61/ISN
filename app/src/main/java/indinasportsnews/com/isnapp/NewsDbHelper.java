package indinasportsnews.com.isnapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1 ;
    public static final String DATABASE_NAME = "isn.db" ;
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + NewsDBContract.NewsEntry.TABLE_NAME + " (" +
                    NewsDBContract.NewsEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY ," +
                    NewsDBContract.NewsEntry.COLUMN_NAME_CATID + " INTEGER ," +
                    NewsDBContract.NewsEntry.COLUMN_NAME_TITLE + " VARCHAR(255) ," +
                    NewsDBContract.NewsEntry.COLUMN_NAME_INTROTEXT + " TEXT ," +
                    NewsDBContract.NewsEntry.COLUMN_NAME_FULLTEXT + " TEXT ," +
                    NewsDBContract.NewsEntry.COLUMN_NAME_ICON_PATH + " TEXT " +
                    /*NewsDBContract.NewsEntry.COLUMN_NAME_DATE + " VARCHAR(25) " +*/ " ) ;" ;

    private static final String SQL_DELETE_ENTRIES1 =
            "DROP TABLE IF EXISTS " + NewsDBContract.NewsEntry.TABLE_NAME ;


    public NewsDbHelper(Context context) {
        super(context , DATABASE_NAME , null , DATABASE_VERSION) ;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES) ;
    }

    public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES1) ;
        onCreate(db) ;
    }

    public void onDowngrade(SQLiteDatabase db , int oldVersion , int newVersion) {
        onUpgrade(db , oldVersion , newVersion) ;
    }
}