package school21.gfoote.ft_hangouts.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.dataBase.SmsDb;
import school21.gfoote.ft_hangouts.model.ContactInfo;

public class ContactInfoActivity extends AppMCompatActivity {
    private FloatingActionButton goChat, goTalk;
    private TextView fio, phone, mail, address;
    private long contactOrder;
    ContactInfo contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info);

        Intent intent = getIntent();
        super.onCreateToolBar();
        initTextView();

        if (intent != null) {
            contactOrder = intent.getLongExtra("contactOrder", 0);
        }
        setTextView();
        goChat = findViewById(R.id.goChat);
        goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactInfoActivity.this, SmsScreenActivity.class);
                i.putExtra("contactOrder", contactOrder);
                startActivity(i);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.CALL_PHONE}, 1000);

        goTalk = findViewById(R.id.goTalk);
        goTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL,  Uri.parse("tel:" + contact.getPhone()));
                startActivity(intent);
            }
        });
    }

    private void initTextView(){
        fio = findViewById(R.id.NamFirstView);
        phone = findViewById(R.id.PhoneView);
        mail = findViewById(R.id.MailView);
        address = findViewById(R.id.AddressView);
    }

    public void setTextView() {
        contactDb = new ContactDb(ContactInfoActivity.this);
        contact =  contactDb.findContactByNumInBase((int) contactOrder + 1);
        contactDb.close();

        String phoneNum = contact.getPhone();
        fio.setText(contact.getFirstName() + " " + contact.getLastName());
        phone.setText(String.format("+%s (%s)-%s-%s", phoneNum.substring(0,1), phoneNum.substring(1, 4), phoneNum.substring(4, 7),
                phoneNum.substring(7, 11)));
        mail.setText(contact.getMail());
        address.setText(contact.getAddress());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent i = new Intent(ContactInfoActivity.this, ContactEditActivity.class);
                i.putExtra("contactOrder", contactOrder);
                startActivity(i);
                return true;
            case R.id.deleteContact:
                contactDb = new ContactDb(ContactInfoActivity.this);
                contactDb.dropContact(contact.getId());
                contactDb.close();
                SmsDb smsDb = new SmsDb(ContactInfoActivity.this);
                smsDb.dropSms(contact.getPhone());
                smsDb.close();
                this.finish();
                Intent i2 = new Intent(ContactInfoActivity.this, ContactListActivity.class);
                i2.putExtra("show", false);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(R.menu.menu_contact, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextView();
    }
}