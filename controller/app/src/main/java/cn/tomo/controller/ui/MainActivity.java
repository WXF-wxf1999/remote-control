package cn.tomo.controller.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.lang.reflect.Field;

import cn.tomo.controller.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton menuButton = findViewById(R.id.top_menu_button);
        menuButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.top_menu_button:
                popMenu(v);
                break;

        }

    }

    private void popMenu(View v) {

        PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);

        // load menu resource
        popupMenu.getMenuInflater().inflate(R.menu.top_menu, popupMenu.getMenu());

        // listen event from menu
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.login:
                        Toast.makeText(MainActivity.this,"login", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.control:
                        Toast.makeText(MainActivity.this,"control",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.file:
                        Toast.makeText(MainActivity.this,"file",Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        popupMenu.show();

    }
}