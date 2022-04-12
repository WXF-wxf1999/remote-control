package cn.tomo.controller.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import cn.tomo.controller.R;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;
import cn.tomo.controller.robot.ScreenRobot;
import kotlin.jvm.Synchronized;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final NettyClient nettyClient = new NettyClient();
    private static AlertDialog dialog = null;
    private ImageView imageView = null;
    private Handler handler = null;
    private int screenHeight = 0;
    private int screenWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        screenHeight = getScreenHeight(this);
        screenWidth = getScreenWidth(this);

        // initialize the configure object
        Configure.setMainActivity(this);
        Configure.initConfig(getResources().openRawResource(R.raw.controller));

        verifyStoragePermissions(this);

        initMenuButton();

        handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                }
                return false;
            }

        });
    }

    // 获得屏幕的宽度
    public static int getScreenWidth(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels; // 返回屏幕的宽度数值
    }

    // 获得屏幕的高度
    public static int getScreenHeight(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels; // 返回屏幕的高度数值
    }
    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
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
                        startControl();
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

    }

    public void imageShow(byte[] screenData) {

        synchronized(this) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(screenData,0, screenData.length);
            bitmap = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), true);

        try{
            imageView.setImageBitmap(bitmap);
            imageView.postInvalidate();
        }finally {
            //Log.i("screen","screen");
        }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initMenuButton() {

        ImageButton menuButton = findViewById(R.id.top_menu_button);
        menuButton.setBackgroundColor(Color.TRANSPARENT);
        menuButton.getBackground().setAlpha(200);

        // make the button movable
        menuButton.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            final int screenHeight = getResources().getDisplayMetrics().heightPixels;
            final int screenWidth = getResources().getDisplayMetrics().widthPixels;

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