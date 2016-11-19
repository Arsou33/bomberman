package org.peekmoon.bomberman.network;

import java.lang.reflect.Constructor;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public abstract class Message {
    
    private static int nextId = 0;
    
    private int id;

    public static Message extractFrom(DatagramPacket packet, ByteBuffer buffer) {
        return extractFrom(Message.class, packet, buffer);
    }
    
    public static <T extends Message> T extractFrom(Class<T> clazz, DatagramPacket packet, ByteBuffer buffer) {
        try {
            MessageType type = MessageType.values()[buffer.getInt()];
            Constructor<? extends Message> ctor = type.getMessageClass().getConstructor(ByteBuffer.class);
            ctor.setAccessible(true);
            int id = buffer.getInt();
            Message message = ctor.newInstance(buffer);
            message.id = id;
            message.address = packet.getAddress();
            message.port = packet.getPort();
            return clazz.cast(message);
        } catch (SecurityException | ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    // Port and address are not serialized and only set by command receiver from datagrampacket metainformation
    private InetAddress address;
    private int port;

    protected abstract void fillData(ByteBuffer buffer);

    public void fill(ByteBuffer buffer) {
        buffer.putInt(MessageType.get(this).ordinal());
        buffer.putInt(nextId++);
        fillData(buffer);
    }

    public MessageType getType() {
        return MessageType.get(this);
    }
    
    public <T extends Message> T getAs(Class<T> clazz) {
        return clazz.cast(this);
    }
    
    public boolean isAfter(Message other) {
        return (other == null) || (id > other.id);
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        if (address == null) {
            return "Command [getType()=" + getType() + "]";
        } else {
            return "Command [address=" + address + ", port=" + port + ", getType()=" + getType() + "]";

        }
    }

}
