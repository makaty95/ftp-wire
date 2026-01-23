package com.makaty.code.Common.Serialization;

import com.makaty.code.Common.Exceptions.RemoteDisconnectionException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;


public class PacketSerializer {

    /* ---------------- STRING ---------------- */

    public static void writeString(SocketChannel channel, String s) throws RemoteDisconnectionException {
        if (s == null) {
            writeInt(channel, -1);
            return;
        }
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeInt(channel, bytes.length);
        writeFully(channel, ByteBuffer.wrap(bytes));
    }

    public static String readString(SocketChannel channel) throws RemoteDisconnectionException {
        int len = readInt(channel);
        if (len < 0) return null;

        ByteBuffer buffer = ByteBuffer.allocate(len);
        readFully(channel, buffer);
        buffer.flip();

        byte[] bytes = new byte[len];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /* ---------------- STRING LIST ---------------- */

    public static void writeStrings(SocketChannel channel, List<String> list) throws RemoteDisconnectionException {
        if(list == null) {
            writeInt(channel, -1);
            return;
        }
        writeInt(channel, list.size());
        for (String s : list) {
            writeString(channel, s);
        }
    }

    public static ArrayList<String> readStrings(SocketChannel channel) throws RemoteDisconnectionException {
        int size = readInt(channel);

        if(size == -1) {
            return null; // -> list is null
        }

        ArrayList<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(readString(channel));
        }
        return list;
    }

    /* ---------------- LONG LIST ---------------- */


    public static void writeLongs(SocketChannel channel, List<Long> list) throws RemoteDisconnectionException {
        writeInt(channel, list.size());
        for (long l : list) {
            writeLong(channel, l);
        }
    }

    public static List<Long> readLongs(SocketChannel channel) throws RemoteDisconnectionException {
        int size = readInt(channel);
        ArrayList<Long> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(readLong(channel));
        }
        return list;
    }

    /* ---------------- INT ---------------- */

    public static void writeInt(SocketChannel channel, int value) throws RemoteDisconnectionException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        buffer.flip();
        writeFully(channel, buffer);
    }

    public static int readInt(SocketChannel channel) throws RemoteDisconnectionException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        readFully(channel, buffer);
        buffer.flip();
        return buffer.getInt();
    }

    /* ---------------- LONG ---------------- */

    public static long readLong(SocketChannel channel) throws RemoteDisconnectionException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        readFully(channel, buffer);
        buffer.flip();
        return buffer.getLong();
    }

    public static void writeLong(SocketChannel channel, long value) throws RemoteDisconnectionException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        buffer.flip();
        writeFully(channel, buffer);
    }

    /* ---------------- HELPERS ---------------- */

    private static void writeFully(SocketChannel channel, ByteBuffer buffer) throws RemoteDisconnectionException {
        while (buffer.hasRemaining()) {
            try {
                channel.write(buffer);
            } catch (IOException e) {
                throw new RemoteDisconnectionException("Channel closed while writing data");
            }
        }
    }

    private static void readFully(SocketChannel channel, ByteBuffer buffer) throws RemoteDisconnectionException {
        while (buffer.hasRemaining()) {
            try {
                int bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    throw new RemoteDisconnectionException("Channel closed while reading data");
                }
            } catch(IOException e) {
                throw new RemoteDisconnectionException("Channel closed while reading data");
            }

        }
    }



}
