package cn.tomo.puppet.handler;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.common.Log;

public class HandlerContainer {

    public static final Map<Command, AbstractHandler> handlers = new ConcurrentHashMap<>();
    public static final Map<Command, Class> handlerNames = new HashMap<Command, Class>();

    static {
        handlerNames.put(Command.PUPPET_LOGIN, LoginHandler.class);
        handlerNames.put(Command.DESKTOP_CONTROL, DesktopHandler.class);
        handlerNames.put(Command.CURSOR_CONTROL_LEFT_DOWN, CursorHandler.class);
        handlerNames.put(Command.CURSOR_CONTROL_RIGHT_DOWN, CursorHandler.class);
        handlerNames.put(Command.CURSOR_CONTROL_WHEEL, CursorHandler.class);
        handlerNames.put(Command.KEYBOARD_CONTROL, KeyboardHandler.class);
        handlerNames.put(Command.DRIVER_REQUEST, FileHandler.class);
        handlerNames.put(Command.FILE_RESEARCH, FileHandler.class);
        handlerNames.put(Command.FILE_REQUEST, FileHandler.class);
    }

    public static AbstractHandler getHandler(Command command) {

        AbstractHandler handler = null;

        handler = handlers.get(command);

        // load it
        if(handler == null) {
            handler = loadHandler(command);
        }
        return handler;
    }

    private static AbstractHandler loadHandler(Command command) {

        Object object = null;
        try {
            Class<?> c = handlerNames.get(command);
            Constructor<?> constructor = c.getConstructor();
            object = constructor.newInstance();
        } catch (Exception e) {
            Log.error(e.toString());
        }
        return (AbstractHandler)object;
    }


}
