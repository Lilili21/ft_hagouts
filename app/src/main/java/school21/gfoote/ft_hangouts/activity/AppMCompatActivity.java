package school21.gfoote.ft_hangouts.activity;

import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import school21.gfoote.ft_hangouts.R;
import school21.gfoote.ft_hangouts.dataBase.ContactDb;

public class AppMCompatActivity extends AppCompatActivity {
    protected ContactDb contactDb;

    protected void onCreateToolBar() {
        Toolbar toolbar = findViewById(R.id.toolcustom);
        setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(int menuType, Menu menu) {
        getMenuInflater().inflate(menuType, menu);
        return true;
    }

}
