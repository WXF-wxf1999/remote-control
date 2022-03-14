package cn.tomo.puppet.netty;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Serialization {

    private static final Map<Class<?>,Schema<?>> cachedSchema=new ConcurrentHashMap<>();
    private static final Objenesis objenesis = new ObjenesisStd(true);
    private Serialization() {
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls){
        Schema<T> schema=(Schema<T>)cachedSchema.get(cls);
        if(schema==null){
            schema= RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls,schema);
        }
        return schema;
    }

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj){
        Class<T> cls=(Class<T>)obj.getClass();
        LinkedBuffer buffer= LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try{
            Schema<T> schema=getSchema(cls);
            // change and return binary data
            return ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        }catch(Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }finally {
            buffer.clear();
        }
    }

    public static <T> T deSerialize(byte[] data,Class<T> cls){
        try{
            T message = objenesis.newInstance(cls);
            // get the structure of class
            Schema<T> schema = getSchema(cls);
            // fill the object's field with data
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        }catch (Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

}
