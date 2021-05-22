package school21.gfoote.ft_hangouts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.utils.ContactAdapter;
import school21.gfoote.ft_hangouts.model.ContactInfo;

public class ListContactFragment extends Fragment {
    private ListView l1;
    private ContactDb contactDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.contacts_list_fragment, container, true );

        l1 = ( ListView ) rootView.findViewById(R.id.ListContact);
        setListContact();

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(rootView.getContext(), ContactInfoActivity.class);
                i.putExtra("contactOrder", id);
                startActivity(i);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        setListContact();
    }

    public void setListContact() {
        contactDb = new ContactDb(getContext());
        List<ContactInfo> contacts =  contactDb.readAll();

        ContactAdapter contactAdapter = new ContactAdapter(getContext(), R.layout.contacts_list_fragment, contacts);
        l1.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
        contactDb.close();
    }
}