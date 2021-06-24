package school21.gfoote.ft_hangouts.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;
import school21.gfoote.ft_hangouts.model.ContactInfo;
import school21.gfoote.ft_hangouts.utils.MaskWatcher;

public class ContactEditActivity extends AppMCompatActivity implements  View.OnClickListener{
    private static final int REQUEST_PHOTO_CAPTURE = 1, REQUEST_PHOTO_PICK = 2;
    private EditText surname, name, phone, mail, address, Phone;
    private MaskWatcher mPhoneMaskWatcher;
    private ImageView photoContact;
    private File photo;
    private Uri mImageUri = null;
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
        mPhoneMaskWatcher = new MaskWatcher(phone);
        phone.addTextChangedListener(mPhoneMaskWatcher);
        mail = findViewById(R.id.EtEmail);
        address = findViewById(R.id.Address);
        Phone = findViewById(R.id.TePhone);
        photoContact = findViewById(R.id.photo);
        photoContact.setOnClickListener(this);
    }

    private void setEditTextValues(ContactInfo contact){
        name.setText(contact.getFirstName());
        surname.setText(contact.getLastName());
        phone.setText(contact.getPhone().replaceAll("[^\\d.]", ""));
        mail.setText(contact.getMail());
        address.setText(contact.getAddress());
        mImageUri = contact.getPhoto().equals("null")? null : Uri.parse("content://media" + contact.getPhoto());
        if (mImageUri != null)
            Glide.with(ContactEditActivity.this)
                    .load(mImageUri)
                    .apply(RequestOptions.centerCropTransform())
                    .into(photoContact);
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
                if (mImageUri == null)
                    contactDb.modifyContact( contactInfo.getId(), name.getText().toString(), surname.getText().toString(),
                            phoneNum, mail.getText().toString(), address.getText().toString(), null);
                else
                    contactDb.modifyContact( contactInfo.getId(), name.getText().toString(), surname.getText().toString(),
                            phoneNum, mail.getText().toString(), address.getText().toString(), mImageUri.getPath());
                     contactDb.close();
                this.finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        showPhotoDialog();
    }

    private void showPhotoDialog() {
        final CharSequence[] items= new CharSequence[]{getString(R.string.take_new_photo), getString(R.string.select_new_photo)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, (dialog, item) -> {
            switch (item)
            {
                case 0:
                    if (ActivityCompat.checkSelfPermission(ContactEditActivity.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(ContactEditActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                    launchCamera();
                    break;
                case 1:
                    pickPhotoFromGallery();
                    break;
            }
        });
        String negativeText = this.getString(R.string.cancel);
        builder.setNegativeButton(negativeText, (dialog, which) -> {
            // negative button logic
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void launchCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            photo = createImageFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mImageUri = FileProvider.getUriForFile(this, "gfoote.ft_hangouts.activity", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        startActivityForResult(intent, REQUEST_PHOTO_CAPTURE);
    }

    public void pickPhotoFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO_PICK);
    }

    private File createImageFile() throws IOException
    {
        String path = Environment.getExternalStorageDirectory() + File.separator
                + "Contact_photo" + File.separator;
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss", Locale.US).format(new Date());
        String imageFileName;
        File image = null;
        File tmpDir = new File(path);

        imageFileName = "Contact_photo_" + timeStamp + "_";

        boolean isDirectoryCreated = tmpDir.exists();
        if (!isDirectoryCreated)
            isDirectoryCreated = tmpDir.mkdirs();
        if (isDirectoryCreated)
            image = File.createTempFile(imageFileName, ".jpg", tmpDir);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_PHOTO_CAPTURE && resultCode == RESULT_OK)
        {
            Glide.with(this)
                    .load(mImageUri.toString())
                    .apply(RequestOptions.centerCropTransform())
                    .into(photoContact);
        }
        else if (requestCode == REQUEST_PHOTO_PICK && resultCode == RESULT_OK)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tmpUri = data.getData();

                String unusablePath;
                if (tmpUri != null) {
                    unusablePath = tmpUri.getPath();
                    int startIndex = unusablePath.indexOf("external/");
                    int endIndex = unusablePath.indexOf("/ORIGINAL");
                    String embeddedPath = unusablePath.substring(startIndex, endIndex);

                    Uri.Builder builder = tmpUri.buildUpon();
                    builder.path(embeddedPath);
                    builder.authority("media");
                    mImageUri = builder.build();
                }
            }
            else {
                mImageUri = data.getData();
            }

            Glide.with(this)
                    .load(mImageUri.toString())
                    .apply(RequestOptions.centerCropTransform())
                    .into(photoContact);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(R.menu.menu_new, menu);
    }
}