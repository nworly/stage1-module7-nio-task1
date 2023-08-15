package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;


public class FileReader {

    public Profile getDataFromFile(File file) {
        String data = "";

        try {
            data = getStringFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Profile profile = new Profile();
        Map<String, String> profileMap = getMapFromString(data);
        profile.setName(profileMap.get("Name"));
        profile.setAge(Integer.valueOf(profileMap.get("Age")));
        profile.setEmail(profileMap.get("Email"));
        profile.setPhone(Long.valueOf(profileMap.get("Phone")));

        return profile;
    }

    private Map<String, String> getMapFromString(String data) {
        Map<String, String> map = new HashMap<>();

        String[] pairs = data.split(System.lineSeparator());
        for (String pair : pairs) {
            if (!pair.contains(": ")) continue;
            String[] kv = pair.split(": ");
            map.put(kv[0], kv[1]);
        }

        return map;
    }

    private String getStringFromFile(File file) throws IOException {
        StringBuilder builder = new StringBuilder();

        try (RandomAccessFile aFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = aFile.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    builder.append((char) buffer.get());
                }
                buffer.clear();
            }
        }
        return builder.toString();
    }
}
