package cn.tomo.controller.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.tomo.controller.R;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;
import cn.tomo.controller.robot.CursorRobot;
import cn.tomo.controller.robot.FileRobot;
import cn.tomo.controller.robot.KeyboardRobot;
import cn.tomo.controller.robot.ScreenRobot;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final NettyClient nettyClient = new NettyClient();
    private static AlertDialog dialog = null;
    private ImageView imageView = null;
    private Spinner spinner = null;
    private EditText editText = null;
    private ListView listView = null;
    private Handler handler = null;
//    private ExpandableListView fileView = null;
//    private ExpandableListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        Configure.setMainActivity(this);
        Configure.initConfig(getResources().openRawResource(R.raw.controller));

        verifyStoragePermissions(this);

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {

                Bundle bundle = msg.getData();
                String content = bundle.getString("controller");
                switch (msg.what) {
                    case 0:
                        innerUpdateSpinner(content);
                        break;
                    case 1:
                        innerInsertFileItem(content);
                        break;
                    case 2:
                        showMessage(content);
                        break;
                }
                return false;
            }

        });

    }

    private void initView() {

        initMenuButton();
        imageView = findViewById(R.id.image_view);
        spinner = findViewById(R.id.spinner_view);
        editText = findViewById(R.id.keyboard_view);
        listView = findViewById(R.id.list_view);
        spinner.setVisibility(View.GONE);

    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void innerUpdateSpinner(String contents) {

        String[] gradeArray = contents.split("6666666");
        ArrayAdapter<String> gradeAdapter=new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,gradeArray);
        // set style
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(gradeAdapter);
        spinner.setVisibility(View.VISIBLE);
    }

    public Handler getHandler() {
        return handler;
    }
    public void updateSpinner(String contents) {

        // must send message to ui thread, or some crashes occur
        Bundle data = new Bundle();
        data.putString("controller", contents);
        Message message = Message.obtain();
        message.what = 0;
        message.setData(data);
        this.handler.sendMessage(message);

    }

    private void innerInsertFileItem(String files) {

        String[] file = files.split("6666666");
        listView.setAdapter(null);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,file);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String filePath = ((TextView)view).getText().toString();
                FileRobot.setFileName(filePath);
                FileRobot.requestFileData(filePath);
                return false;
            }
        });
        listView.setVisibility(View.VISIBLE);
    }

    public void insertFileItem(String files) {

        Bundle data = new Bundle();
        data.putString("controller", files);
        Message message = Message.obtain();
        message.what = 1;
        message.setData(data);
        this.handler.sendMessage(message);
    }

    public Spinner getSpinner() {
        return spinner;
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    // require privilege
    public void verifyStoragePermissions(Activity activity) {


        try {
            // check write privilege
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // require and pop window
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.top_menu_button) {
            popMenu(v);
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
                        popLoginWindow();
                        break;
                    case R.id.control:
                        // start control
                        FileRobot.setFileMode(false);
                        listView.setVisibility(View.GONE);
                        startControl();
                        break;
                    case R.id.file:

                        startFile();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        popupMenu.show();

    }

    private void startFile() {

        if(Configure.getSessionId() == 0) {

            Toast.makeText(MainActivity.this, "please login first!", Toast.LENGTH_LONG).show();
            return;
        }
        FileRobot.setFileMode(true);
        SystemClock.sleep(1000);
        FileRobot.requestDirectory(".");
        KeyboardRobot keyBoardRobot = new KeyboardRobot();
        editText.addTextChangedListener(keyBoardRobot);
        editText.setOnKeyListener(keyBoardRobot);
    }

    private void popLoginWindow() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.activity_login, null);

        EditText loginEditText = view.findViewById(R.id.session_edittext);
        Button button = view.findViewById(R.id.login_button);

        builder.setTitle("Login")
                .setIcon(R.mipmap.ic_launcher_round)
                .setView(view)
                .create();

        dialog = builder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = loginEditText.getText().toString();
                if(sessionId.length() == 0) {
                    Toast.makeText(MainActivity.this, "please input the session id", Toast.LENGTH_LONG).show();
                    return;
                }
                Configure.setSessionId(Integer.parseInt(sessionId));
                nettyClient.start();
           }
        });

    }

    public static void shutDownLoginWindow() {
        dialog.dismiss();
    }

    public void startControl() {

        // check user has login
        if(Configure.getSessionId() == 0) {

            Toast.makeText(MainActivity.this, "please login first!", Toast.LENGTH_LONG).show();
            return;
        }

        ScreenRobot.requestScreen();

        // set touch listen event for cursor
        imageView.setEnabled(true);
        imageView.setOnTouchListener(new CursorRobot());

        // to simulate the keyboard
        KeyboardRobot keyBoardRobot = new KeyboardRobot();

        editText.addTextChangedListener(keyBoardRobot);
        editText.setOnKeyListener(keyBoardRobot);
    }

    public void imageShow(Bitmap srcBitmap) {
        //Log.i("getScreen","fsfsf   " + srcBitmap.getByteCount());
        synchronized(this) {
            Bitmap bitmap = Bitmap.createScaledBitmap(srcBitmap, imageView.getWidth(), imageView.getHeight(), true);
        try{
            imageView.setImageBitmap(bitmap);
            imageView.postInvalidate();
        }finally {
            //Log.i("getScreen","fsfsf");
        }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMenuButton() {

        ImageButton menuButton = findViewById(R.id.top_menu_button);
        menuButton.bringToFront();
        menuButton.setBackgroundColor(Color.TRANSPARENT);
        menuButton.getBackground().setAlpha(200);

        final int screenHeight = getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;

        Configure.setControllerScreenWidth(screenWidth);
        Configure.setControllerScreenHeight(screenHeight);

        // make the button movable
        menuButton.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int ea = event.getAction();
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;

                        if (left < 0) {

                            left = 0;
                            right = left + v.getWidth();

                        }
                        if (right > screenWidth) {

                            right = screenWidth;
                            left = right - v.getWidth();

                        }
                        if (top < 0) {

                            top = 0;
                            bottom = top + v.getHeight();

                        }
                        if (bottom > screenHeight) {

                            bottom = screenHeight;
                            top = bottom - v.getHeight();

                        }
                        // reset button location
                        v.layout(left, top, right, bottom);//按钮重画

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                }
                return false;
            }
        });


        menuButton.setOnClickListener(this);
    }
}