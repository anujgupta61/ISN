package indinasportsnews.com.isnapp;

import android.provider.BaseColumns;

public final class NewsDBContract {
    public NewsDBContract() {}

    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news" ;
        public static final String COLUMN_NAME_ID = "id" ;
        public static final String COLUMN_NAME_CATID = "catid" ;
        public static final String COLUMN_NAME_TITLE = "title" ;
        public static final String COLUMN_NAME_INTROTEXT = "introtext" ;
        public static final String COLUMN_NAME_FULLTEXT = "fulltext" ;
        //public static final String COLUMN_NAME_DATE = "date" ;
        public static final String COLUMN_NAME_ICON_PATH = "icon_path" ;
    }

}
