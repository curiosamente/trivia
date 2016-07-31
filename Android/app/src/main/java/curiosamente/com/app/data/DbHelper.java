/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package curiosamente.com.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "curiosamente.db";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PRIZES_TABLE = "CREATE TABLE " + DbContract.PrizesEntry.TABLE_NAME + " (" +
               DbContract.PrizesEntry._ID + " INTEGER PRIMARY KEY," +
               DbContract.PrizesEntry.COLUMN_BAR_ID + " TEXT NOT NULL, " +
               DbContract.PrizesEntry.COLUMN_BAR_NAME + " TEXT NOT NULL, " +
               DbContract.PrizesEntry.COLUMN_PRIZE_DATE + " DATE NOT NULL, " +
               DbContract.PrizesEntry.COLUMN_PRIZE_IMAGE + " TEXT NOT NULL, " +
               DbContract.PrizesEntry.COLUMN_PRIZE_COLLECTED + " INTEGER NOT NULL DEFAULT 0 " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_PRIZES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.PrizesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
