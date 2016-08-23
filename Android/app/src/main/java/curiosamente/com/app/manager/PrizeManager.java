package curiosamente.com.app.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import curiosamente.com.app.R;
import curiosamente.com.app.data.DbContract;
import curiosamente.com.app.data.DbHelper;
import curiosamente.com.app.model.Prize;

public class PrizeManager {

    public static boolean storePrize(Prize prize, Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.PrizesEntry.COLUMN_BAR_ID, prize.getIdBar());
        contentValues.put(DbContract.PrizesEntry.COLUMN_USER_ID, prize.getIdUser());
        contentValues.put(DbContract.PrizesEntry.COLUMN_BAR_NAME, prize.getName());
        contentValues.put(DbContract.PrizesEntry.COLUMN_PRIZE_DATE, prize.getDate().getTime());
        String resource = context.getString(R.string.prize_icon_logo_prefix) + 0;
        contentValues.put(DbContract.PrizesEntry.COLUMN_PRIZE_IMAGE, resource);
        contentValues.put(DbContract.PrizesEntry.COLUMN_PRIZE_COLLECTED, 0);

        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(DbContract.PrizesEntry.TABLE_NAME, null, contentValues);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void createAndStorePrize(Context context){
        Prize prize = new Prize();
        prize.setIdBar(BarManager.getBarId(context));
        prize.setName(BarManager.getBarName(context));
        prize.setIdUser(LogInManager.getCurrentUserID());
        prize.setDate(new Date());
        storePrize(prize, context);
    }

    public static Prize collectPrize(Context context, Prize prize) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.PrizesEntry.COLUMN_PRIZE_COLLECTED, 1);
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.update(DbContract.PrizesEntry.TABLE_NAME, contentValues, DbContract.PrizesEntry._ID + "=" + prize.getSeqId(), null);
            Cursor cursor = sqLiteDatabase.rawQuery(DbContract.PrizesEntry.QUERY_SELECT_ROW_BY_SEQ_ID + prize.getSeqId(), null);
            Prize cashedPrize = getPrizeFromCursorAtPosition(cursor, 0);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
            return cashedPrize;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Prize> getPrizesList(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        //TODO delete this
        //provisoryAddPrizes(sqLiteDatabase, context);
        String query = DbContract.PrizesEntry.QUERY_SELECT_ALL_ROWS + LogInManager.getCurrentUserID();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        ArrayList<Prize> prizes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                prizes.add(getPrizeFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return prizes;
    }

    public static Prize getPrizeFromCursorAtPosition(Cursor cursor, int position) {
        Prize prize = null;
        if (cursor.moveToPosition(position)) {
            prize = getPrizeFromCursor(cursor);
        }
        return prize;
    }

    public static Prize getPrizeFromCursor(Cursor cursor) {
        Prize prize = new Prize();
        prize.setSeqId(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PrizesEntry._ID)));
        prize.setIdUser(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PrizesEntry.COLUMN_USER_ID)));
        prize.setName(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PrizesEntry.COLUMN_BAR_NAME)));
        prize.setDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DbContract.PrizesEntry.COLUMN_PRIZE_DATE))));
        prize.setImageSrc(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.PrizesEntry.COLUMN_PRIZE_IMAGE)));
        prize.setCollected(cursor.getInt(cursor.getColumnIndexOrThrow(DbContract.PrizesEntry.COLUMN_PRIZE_COLLECTED)) != 0);
        return prize;
    }
}
