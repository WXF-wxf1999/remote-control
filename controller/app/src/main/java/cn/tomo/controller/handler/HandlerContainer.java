package cn.tomo.controller.handler;


import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.tomo.controller.common.Command;

public class HandlerContainer {

    public static final Map<Command, AbstractHandler> handlers = new ConcurrentHashMap<>();
    public static final Map<Command, Class> handlerNames = new HashMap<Command, Class>();

    static {
        handlerNames.put(Command.CONTROLLER_LOGIN, LoginHandler.class);
        handlerNames.put(Command.DEVICE_NOT_ONLINE, LoginHandler.class);

        // handlerNames.put(Command.  ,"FileHandler");
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
            Log.e(HandlerContainer.class.getName(),e.toString());
        }
        return (AbstractHandler)object;
    }


}