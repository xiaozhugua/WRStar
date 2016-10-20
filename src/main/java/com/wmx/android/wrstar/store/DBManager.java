package com.wmx.android.wrstar.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wmx.android.wrstar.entities.WacthRecord;
import com.wmx.android.wrstar.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static final String Tag = "DBManager";
    private DBHandler dbHandler;

    public int Size = 20;


    public DBManager(Context context) {
        dbHandler = new DBHandler(context);
    }


    public boolean insertWatchRecord(String videoId, String timestamp, String imgurl, String type,String title) {


        SQLiteDatabase wDatabase = dbHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHandler.VIDEO_ID, videoId);
        contentValues.put(DBHandler.TIMESTAMP, timestamp);
        contentValues.put(DBHandler.IMG_URL, imgurl);
        contentValues.put(DBHandler.TYPE, type);
        contentValues.put(DBHandler.TITLE, title);

        boolean flag = wDatabase.insert(DBHandler.TABLE_NAME, null,
                contentValues) == -1 ? false : true;
        wDatabase.close();
        LogUtil.i("插入数据:" + flag);
        return flag;
    }

    public  List<WacthRecord> queryRecord(int page) {

        List<WacthRecord> list = new ArrayList<WacthRecord>();

        // select * from newsCmd where species = 0 ORDER BY KEY desc LIMIT 1 ;
        String sql = "select * from " + DBHandler.TABLE_NAME
                + "  ORDER BY " + DBHandler.TIMESTAMP + " desc LIMIT " + Size + " offset " + page * Size;

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql, null);

        while (rawQuery.moveToNext()) {
            WacthRecord wacthRecord = new WacthRecord();


            wacthRecord.videoId = rawQuery.getString(rawQuery
                    .getColumnIndex(DBHandler.VIDEO_ID));

            wacthRecord.timeStamp = rawQuery.getString(rawQuery
                    .getColumnIndex(DBHandler.TIMESTAMP));
            wacthRecord.type = rawQuery.getString(rawQuery
                    .getColumnIndex(DBHandler.TYPE));
            wacthRecord.imgURL = rawQuery.getString(rawQuery
                    .getColumnIndex(DBHandler.IMG_URL));
            wacthRecord.title = rawQuery.getString(rawQuery
                    .getColumnIndex(DBHandler.TITLE));
            list.add(wacthRecord);

        }

        rawQuery.close();
        rDatabase.close();

        return  list ;
    }


    /**
     * 查询 新闻分类名称 的新闻条数
     *
     * @param
     * @return
     */
    public int getRecordCount() {
        long count = -1;
        String sql = "select count(*) from " + DBHandler.TABLE_NAME;


        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql,null);
        if (rawQuery.moveToFirst()) {
            count = rawQuery.getLong(0);
        }
        rawQuery.close();
        rDatabase.close();

        return (int)count;
    }

    /**
     * @param key
     * @return 返回 -1 表示，不存在该 key.
     */
    private long queryTypeCountByKey(String key) {
        long count = 0;
        String sql = "select count(*) from " + DBHandler.TABLE_NAME
                + " where key =?";

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql, new String[]{key});
        if (rawQuery.moveToFirst()) {
            count = rawQuery.getLong(0);
        }
        rawQuery.close();
        rDatabase.close();
        return count;
    }

    /**
     * 查询历史新闻
     *
     * @param key
     * @return
     */
    public String queryOld(String key) {
        String content = null;
        // select cmdContent from newsCmd where key <'o800' and key like '%o%'
        // limit 1;
        String sql = "select cmdContent from " + DBHandler.TABLE_NAME
                + " where key < ? and key like ? limit 1";

        SQLiteDatabase rDatabase = dbHandler.getReadableDatabase();
        Cursor rawQuery = rDatabase.rawQuery(sql,
                new String[]{key, "%" + key.substring(0, 1) + "%"});
        if (rawQuery.moveToFirst()) {
            do {
                content = rawQuery.getString(rawQuery
                        .getColumnIndex("cmdContent"));
            } while (rawQuery.moveToNext());
        }
        rawQuery.close();
        rDatabase.close();

        return content;
    }




}
