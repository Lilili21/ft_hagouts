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
import android.util.Log;
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
import school21.gfoote.ft_hangouts.utils.MaskWatcher;

public class ContactNewActivity extends AppMCompatActivity implements  View.OnClickListener{
    private static final int    REQUEST_PHOTO_CAPTURE = 1, REQUEST_PHOTO_PICK = 2;
    private EditText    First, Name, Phone, Mail, Address;
    private MaskWatcher mPhoneMaskWatcher;
    private ImageView   photoContact;
    private File        photo;
    private Uri         mImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_new);

        photoContact = findViewById(R.id.photo);
        photoContact.setOnClickListener(this);
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
            if (mImageUri == null)
                contactDb.newContact(First.getText().toString(), Name.getText().toString(),
                        Phone.getText().toString().replaceAll("[^\\d.]", ""), Mail.getText().toString(), Address.getText().toString(),null);
            else
                contactDb.newContact(First.getText().toString(), Name.getText().toString(),
                    Phone.getText().toString().replaceAll("[^\\d.]", ""), Mail.getText().toString(), Address.getText().toString(), mImageUri.getPath().toString());

            contactDb.close();
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(R.menu.menu_new, menu);
    }

    @Override
    public void onClick(View v) {
        showPhotoDialog();
    }

    private void showPhotoDialog() {
        final CharSequence[] items= new CharSequence[]{getString(R.string.take_new_photo), getString(R.string.select_new_photo)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (ActivityCompat.checkSelfPermission(ContactNewActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ContactNewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        builder.setItems(items, (dialog, item) -> {
            switch (item)
            {
                case 0:
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mImageUri = FileProvider.getUriForFile(this, "gfoote.ft_hangouts.activity", photo);
        }
        else
            mImageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        startActivityForResult(intent, REQUEST_PHOTO_CAPTURE);
    }

    public void pickPhotoFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO_PICK);
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
}