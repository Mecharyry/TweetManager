package com.github.mecharyry.db;

public final class TweetTable {

    public enum TABLES {
        TWEET_TABLE("tweet");

        private final String tableName;

        TABLES(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }
    }

    public enum COLUMNS {
        COLUMN_ID("_id"),
        COLUMN_SCREEN_NAME("screen_name"),
        COLUMN_LOCATION("location"),
        COLUMN_TWEET_TEXT("text"),
        COLUMN_THUMB_IMAGE("thumb_image"),
        COLUMN_CATEGORY("category");

        private final String columnHeader;

        COLUMNS(String columnHeader) {
            this.columnHeader = columnHeader;
        }

        public String getColumnHeader() {
            return columnHeader;
        }

        public static String[] names() {
            COLUMNS[] columns = values();
            String[] names = new String[columns.length];

            for (int i = 0; i < columns.length; i++) {
                names[i] = columns[i].getColumnHeader();
            }

            return names;
        }
    }

    public enum Category {
        ANDROID_DEV_TWEETS,
        MY_STREAM_TWEETS;
    }
}
