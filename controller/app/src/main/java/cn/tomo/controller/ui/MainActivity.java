package cn.tomo.controller.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import cn.tomo.controller.R;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final NettyClient nettyClient = new NettyClient(MainActivity.this);
    private static AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configure.initConfig(getResources().openRawResource(R.raw.controller));
        initMenuButton();
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

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, null, null);
        // send command
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);

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