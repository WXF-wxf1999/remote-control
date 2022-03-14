
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import com.wxw.notes.protobuf.proto.DemoProto;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;


public class Test {
    static class demotest {
        public int id;
        public String code;
        public String myName;
        public byte[] array;
        public void setId(int Id) {
            id = Id;
        }

        public void setCode(String Code) {
            code = Code;
        }

        public void setMyName(String myName) {
            this.myName = myName;
        }

        public void setArray(byte[] array) {
            this.array = array;
        }
    }
    /**
     * ProtoBuffer object to POJO
     */
    private static <T> T fromProtoBuffer(GeneratedMessageV3 pbObject, Class<T> modelClass) {
        T model = null;

        try {
            model = modelClass.newInstance();
            Field[] modelFields = modelClass.getDeclaredFields();
            if (modelFields.length > 0) {
                for (Field modelField : modelFields) {
                    String fieldName = modelField.getName();
                    String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    Class<?> fieldType = modelField.getType();
                    try {
                        Method pbGetMethod = pbObject.getClass().getMethod("get" + name);
                        Object value = pbGetMethod.invoke(pbObject);

                        Method modelSetMethod = modelClass.getMethod("set" + name, fieldType);
                        modelSetMethod.invoke(model, value);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    public static void main(String[] args) throws IOException {
        DemoProto.Demo.Builder demo = DemoProto.Demo.newBuilder();
        byte[] dataByte = {1,2,3,4};
        demo.setId(12)
                .setCode("code")
                .setName("ke")
                .setId(99)
                .setArray(ByteString.copyFrom(dataByte));

        // 序列化
        DemoProto.Demo build = demo.build();
        byte[] s = build.toByteArray();


        DemoProto.Demo demo1;
        demo1 = DemoProto.Demo.parseFrom(s);
        //System.out.println(demo1.getArray());
        byte[] var = demo1.getArray().toByteArray();
        System.out.println(Arrays.toString(var));

        demotest hu = fromProtoBuffer(demo1,demotest.class);
        System.out.println(hu.code);
        System.out.println(hu.id);
        System.out.println(hu.myName);
        System.out.println(Arrays.toString(hu.array));




























    }

}
