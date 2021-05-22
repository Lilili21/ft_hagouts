package school21.gfoote.ft_hangouts.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.model.ContactInfo;

public class ContactAdapter extends ArrayAdapter<ContactInfo> {
    Context context;
    List<ContactInfo> data = null;

    public ContactAdapter(Context context, int layoutResourceId, List<ContactInfo> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View outerContiner = inflater.inflate(R.layout.contacts_list_pattern, parent, false);

        TextView text = (TextView) outerContiner.findViewById(R.id.list1);
        TextView phone = (TextView) outerContiner.findViewById(R.id.list2);

        String phoneNum = data.get(position).getPhone();
        text.setText(data.get(position).getFirstName());
        phone.setText(String.format("+%s (%s)-%s-%s", phoneNum.substring(0,1), phoneNum.substring(1, 4), phoneNum.substring(4, 7),
                phoneNum.substring(7, 11)));

        return outerContiner;
    }
}
