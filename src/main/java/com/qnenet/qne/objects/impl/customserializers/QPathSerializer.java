package com.qnenet.qne.objects.impl.customserializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QPathSerializer extends Serializer<Path> {

    @Override
    public void write(Kryo kryo, Output output, Path path) {
        byte[] bytes = path.toString().getBytes();
        output.write(bytes);
    }

    @Override
    public Path read(Kryo kryo, Input input, Class<? extends Path> aClass) {
        byte[] bytes = null;
        try {
            bytes = new byte[input.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        input.readBytes(bytes);

        String pathString = new String(bytes);

        return Paths.get(pathString);
    }

}

