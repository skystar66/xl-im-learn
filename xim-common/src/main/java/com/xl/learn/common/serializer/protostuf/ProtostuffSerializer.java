/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xl.learn.common.serializer.protostuf;

import com.xl.learn.common.exception.SerializerException;
import com.xl.learn.common.serializer.ISerializer;
import com.xl.learn.common.serializer.SchemaCache;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xl 2017/11/11
 */
@SuppressWarnings("unchecked")
public class ProtostuffSerializer implements ISerializer {

    private static final SchemaCache SCHEMA_CACHE = SchemaCache.getInstance();
    private static final Objenesis OBJENESIS = new ObjenesisStd(true);


    private static <T> Schema<T> getSchema(Class<T> cls) {
        return (Schema<T>) SCHEMA_CACHE.get(cls);
    }


    @Override
    public byte[] serialize(Object obj) throws SerializerException {
        Class cls = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.writeTo(outputStream, obj, schema, buffer);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }


    @Override
    public void serialize(Object obj, OutputStream outputStream) throws SerializerException {
        Class cls = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.writeTo(outputStream, obj, schema, buffer);
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }


    @Override
    public <T> T deSerialize(byte[] param, Class<T> cls) throws SerializerException {
        T object;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(param)) {
            object = OBJENESIS.newInstance(cls);
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(inputStream, object, schema);
            return object;
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deSerialize(InputStream inputStream, Class<T> cls) throws SerializerException {
        T object;
        try {
            object = OBJENESIS.newInstance(cls);
            Schema schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(inputStream, object, schema);
            return object;
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}

