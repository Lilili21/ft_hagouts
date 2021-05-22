package school21.gfoote.ft_hangouts.dataBase;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import school21.gfoote.ft_hangouts.model.ContactInfo;

public class ContactDb extends DbManager {
    private static final String tableName = "Contact";

    public ContactDb(Context context) {
        super (context);
    }

    public void newContact(String first, String name, String phone, String mail, String address) {
        if (contactExist(phone))
            throw new RuntimeException("Contact already exist");
        name.replace("'", "''");
        String parameters = "'" + first + "', '" + name + "', '" + phone + "', '"
                + mail + "', '" + address + "'";
        this.getWritableDatabase().execSQL( UtilsDb.INSERT_ELEM(ContactInfo.ContactDbFormat(),parameters));
    }

    public List<ContactInfo> readAll() {
        List<ContactInfo> contacts = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery(UtilsDb.SELECT_FROM_TABLE(tableName,null), null);
        cursor.moveToFirst();

        while (! cursor.isAfterLast())
        {
            ContactInfo data = new ContactInfo(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            contacts.add(data);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    public boolean contactExist(String phone) {
        Cursor cursor = this.getReadableDatabase().rawQuery(UtilsDb.SELECT_FROM_TABLE(tableName,null), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            if (phone.equals(cursor.getString(3))) {
                cursor.close();
                return true;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return false;
    }

    public ContactInfo findContactByNumInBase(int step) {
        Cursor cursor = this.getReadableDatabase().rawQuery(UtilsDb.SELECT_FROM_TABLE(tableName,null), null);
        cursor.moveToFirst();
        int     i = 1;

        while (! cursor.isAfterLast() && i < step)
        {
            cursor.moveToNext();
            i++;
        }
        ContactInfo data = new ContactInfo(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        cursor.close();
        return data;
    }

    public String getNameContact(String NumeroPhone) {
        String nameReturn = "";
        Cursor cursor = this.getReadableDatabase().rawQuery(UtilsDb.SELECT_FROM_TABLE(tableName,new String[]{"phone", NumeroPhone}), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {
            nameReturn = cursor.getString(1);
            cursor.moveToNext();
        }
        cursor.close();
        return nameReturn;
    }

    public void modifyContact(int id, String first, String name, String phone, String mail, String address) {
        String parameters = "firstName = '" + first + "', lastName = '" + name +
                "', phone = '" + phone + "', mail = '"
                + mail + "', address = '" + address + "'";

        this.getWritableDatabase().execSQL( UtilsDb.UPDATE_ELEM(tableName, parameters, new String[]{"id", String.valueOf(id)}) );
    }

    public void dropContact (int _id) {
        this.getWritableDatabase().execSQL( UtilsDb.DELETE_FROM_TABLE(tableName, new String[]{"id", String.valueOf(_id)}) );
    }
}
