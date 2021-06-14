package school21.gfoote.ft_hangouts.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.utils.MaskWatcher;

public class ContactNewActivity extends AppMCompatActivity {
    private EditText    First, Name, Phone, Mail, Address;
    private MaskWatcher mPhoneMaskWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_new);
        Phone = findViewById(R.id.TePhone);
        mPhoneMaskWatcher = new MaskWatcher(Phone);
        Phone.addTextChangedListener(mPhoneMaskWatcher);
        super.onCreateToolBar();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveContact() {
        int err;

        First = findViewById(R.id.TeFirst);
        Name = findViewById(R.id.TeName);

        Mail = findViewById(R.id.TeEmail);
        Address = findViewById(R.id.TePostal);
        err = 1;
        if (First.length() == 0) {
            First.setError(getString(R.string.nameError));
            err = -1;
        }
        if (Name.length() == 0) {
            Name.setError(getString(R.string.surnameError));
            err = -1;
        }
        if (Phone.getText().toString().replaceAll("[^\\d.]", "").length() != 11) {
            Phone.setError(getString(R.string.phoneError));
            err = -1;
        }
        if (Mail.length() == 0) {
            Mail.setError(getString(R.string.mailError));
            err = -1;
        }
        if (Address.length() == 0) {
            Address.setError(getString(R.string.postalError));
            err = -1;
        }
        if (err == 1) {
            contactDb = new ContactDb(ContactNewActivity.this);
            contactDb.newContact(First.getText().toString(), Name.getText().toString(),
                    Phone.getText().toString().replaceAll("[^\\d.]", ""), Mail.getText().toString(), Address.getText().toString());
            contactDb.close();
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(R.menu.menu_new, menu);
    }
}