package school21.gfoote.ft_hangouts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.dataBase.SmsDb;
import school21.gfoote.ft_hangouts.model.ContactInfo;

public class ContactEditActivity extends AppMCompatActivity {
    private EditText surname, name, phone, mail, address;
    private long contactOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        Intent intent = getIntent();
        super.onCreateToolBar();
        initEditText();

        if (intent != null) {
            contactOrder = intent.getLongExtra("contactOrder", 0);
        }
        contactDb = new ContactDb(ContactEditActivity.this);
        ContactInfo contact = contactDb.findContactByNumInBase((int) contactOrder + 1);
        contactDb.close();
        setEditTextValues(contact);
    }

    private void initEditText(){
        surname = findViewById(R.id.Name);
        name = findViewById(R.id.EtFirst);
        phone = findViewById(R.id.Phone);
        mail = findViewById(R.id.EtEmail);
        address = findViewById(R.id.Address);
    }

    private void setEditTextValues(ContactInfo contact){
        name.setText(contact.getFirstName());
        surname.setText(contact.getLastName());
        phone.setText(contact.getPhone().replaceAll("[^\\d.]", ""));
        mail.setText(contact.getMail());
        address.setText(contact.getAddress());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String phoneNum = phone.getText().toString().replaceAll("[^\\d.]", "");
        if (item.getItemId() == R.id.save) {
            boolean err = false;

            if (name.length() == 0) {
                name.setError(getString(R.string.nameError));
                err = true;
            }
            if (surname.length() == 0) {
                surname.setError(getString(R.string.surnameError));
                err = true;
            }
            if (phoneNum.length() != 11) {
                phone.setError(getString(R.string.phoneError));
                err = true;
            }
            if (mail.length() == 0) {
                mail.setError(getString(R.string.mailError));
                err = true;
            }
            if (address.length() == 0) {
                address.setError(getString(R.string.postalError));
                err = true;
            }
            if (!err) {
                contactDb = new ContactDb(ContactEditActivity.this);

                ContactInfo contactInfo = contactDb.findContactByNumInBase((int) contactOrder + 1);
                contactDb.modifyContact( contactInfo.getId(), name.getText().toString(), surname.getText().toString(),
                        phoneNum, mail.getText().toString(), address.getText().toString());
                contactDb.close();
                this.finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(R.menu.menu_new, menu);
    }
}