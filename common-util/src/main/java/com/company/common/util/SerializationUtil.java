package com.company.common.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializationUtil {

    private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    private static final Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtil() {}

    public static <T> byte[] serializeToByte(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return CompressionUtil.compress(ProtobufIOUtil.toByteArray(obj, schema, buffer));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.getOrDefault(cls, null);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }

    public static <T> T deserializeFromByte(byte[] data, Class<T> cls) {
        try {
            T message = objenesis.newInstance(cls);

            Schema<T> schema = getSchema(cls);
            ProtobufIOUtil.mergeFrom(CompressionUtil.decompress(data), message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
