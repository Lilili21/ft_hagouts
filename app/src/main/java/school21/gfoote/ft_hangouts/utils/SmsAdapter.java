package school21.gfoote.ft_hangouts.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.model.SmsInfo;

public class SmsAdapter extends ArrayAdapter<SmsInfo> {
    Context context;
    List<SmsInfo> data = null;

    public SmsAdapter(Context context, int layoutResourceId, List<SmsInfo> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (data.get(position).getWho().contains("me")) {
            Log.i("Sms", data.get(position).getMessage());
            convertView = inflater.inflate(R.layout.sms_screen_my_message, null);
            TextView text = (TextView) convertView.findViewById(R.id.my_message);
            text.setText(data.get(position).getMessage().toString());
        } else {
            ContactDb DbManager = new ContactDb(getContext());
            Log.i("Sms", data.get(position).getMessage());
            convertView = inflater.inflate(R.layout.sms_screen_other_message, null);
            TextView nameSms = (TextView) convertView.findViewById(R.id.name);
            TextView text = (TextView) convertView.findViewById(R.id.other_message);
            text.setText(data.get(position).getMessage().toString());
            nameSms.setText(DbManager.getNameContact(data.get(position).getPhone().toString()));
        }
        return (convertView);
    }
}
