package school21.gfoote.ft_hangouts.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.dataBase.SmsDb;
import school21.gfoote.ft_hangouts.model.ContactInfo;
import school21.gfoote.ft_hangouts.utils.SmsAdapter;
import school21.gfoote.ft_hangouts.model.SmsInfo;

public class SmsScreenActivity extends AppCompatActivity {

    private SmsDb smsDb;
    private SmsInfo smsInfo;
    private List<SmsInfo> smsList;

    private ContactDb contactDb;
    ContactInfo contactInfo;

    private ImageButton Ib1;
    private EditText sms;
    private String msg;

    private ListView l1;

    IntentFilter intentFilter;

    private long contactOrder;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().getString("number").equals(smsInfo.getPhone())) {
                setList();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_screen);
        Intent intent = getIntent();
        if (intent != null) {
            contactOrder = intent.getLongExtra("contactOrder", 0);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, 1000);
        }

        l1 = findViewById(R.id.messages_view);
        setList();

        Toolbar toolbar = findViewById(R.id.toolcustom);
        toolbar.setTitle(contactInfo.getFirstName() + " " + contactInfo.getLastName());
         setSupportActionBar(toolbar);

        Ib1 = findViewById(R.id.smsButton);
        Ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms = findViewById(R.id.smsSend);
                msg = sms.getText().toString();
                sendMsg(contactInfo.getPhone(), msg);
                smsDb.newSms(contactInfo.getPhone(), msg, "me");
                setList();
                sms.setText("");
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
    }

    private void sendMsg(String number, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        try {
            smsManager.sendTextMessage(number, null, msg, null, null);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void setList() {
        contactDb = new ContactDb(SmsScreenActivity.this);
        contactInfo = contactDb.findContactByNumInBase((int)contactOrder + 1);
        smsDb = new SmsDb(SmsScreenActivity.this);
        smsList =  smsDb.getSms(contactInfo.getPhone());
        SmsAdapter smsAdapter = new SmsAdapter(SmsScreenActivity.this, R.layout.sms_screen, smsList);
        l1.setAdapter(smsAdapter);
        smsAdapter.notifyDataSetChanged();
        scrollMyListViewToBottom();
        smsDb.close();
        contactDb.close();
    }

    private void scrollMyListViewToBottom() {
        l1.post(new Runnable() {
            @Override
            public void run() {
                l1.setSelection(smsList.size() - 1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(getApplicationContext(), "Non", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}