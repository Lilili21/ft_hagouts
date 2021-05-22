package school21.gfoote.ft_hangouts.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper {

    public DbManager(Context context) {
        super (context, UtilsDb.DATABASE_NAME, null, UtilsDb.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UtilsDb.CREATE_CONTACT_TABLE);
        db.execSQL( UtilsDb.CREATE_SMS_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
