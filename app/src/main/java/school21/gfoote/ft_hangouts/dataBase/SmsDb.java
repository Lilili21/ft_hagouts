package school21.gfoote.ft_hangouts.dataBase;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import school21.gfoote.ft_hangouts.model.SmsInfo;

public class SmsDb extends DbManager{

    private static final String tableName = "Sms";

    public SmsDb(Context context) {
        super (context);
    }

    public void newSms(String phone, String message, String who) {
        String parameters = "'" + phone + "', '" + message + "', '" + who + "'";
        this.getWritableDatabase().execSQL(UtilsDb.INSERT_ELEM(SmsInfo.SmsDbFormat(),parameters));
    }

    public List<SmsInfo> getSms(String Phone) {
        List<SmsInfo> smsList = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery(UtilsDb.SELECT_FROM_TABLE(tableName,
                new String[]{"phone", Phone}), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            SmsInfo data = new SmsInfo(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));
            smsList.add(data);
            cursor.moveToNext();
        }
        cursor.close();
        return smsList;
    }

    public void dropSms (String phone) {
        this.getWritableDatabase().execSQL( UtilsDb.DELETE_FROM_TABLE(tableName, new String[]{"Phone", phone}) );
    }
    
}
