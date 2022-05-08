package cn.tomo.controller.robot;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;

public class KeyboardRobot implements TextWatcher, View.OnKeyListener {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(!FileRobot.isFileMode()) {
            if(s.toString().length() == 0) {
                return;
            }
            sendKeyBoard(s.toString());
            s.clear();
        }
    }

    // capture the delete key and enter key
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_DEL) {

            if(!FileRobot.isFileMode()) {

                sendKeyBoard("DELETE");
            }
        }
        if(keyCode == KeyEvent.KEYCODE_ENTER) {


            if(!FileRobot.isFileMode()) {

                sendKeyBoard("ENTER");
            } else {

                EditText editText = (EditText)v;
                FileRobot.sendFileName(editText.getText().toString(),
                        Configure.getMainActivity().getSpinner().getSelectedItem().toString());
                editText.setText("");
            }

        }
        return false;
    }

    public void sendKeyBoard(String msg) {

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.KEYBOARD_CONTROL, msg.getBytes(), null);
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
    }

//    public void sendFileName(String fileName, String driverName) {
//
//        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.FILE_RESEARCH,
//                driverName.getBytes(), fileName.getBytes());
//        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
//    }
}

