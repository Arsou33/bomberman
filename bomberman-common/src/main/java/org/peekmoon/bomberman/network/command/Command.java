package org.peekmoon.bomberman.network.command;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public abstract class Command {

    public static Command extractFrom(InetAddress address, int port, ByteBuffer buffer) {
        try {
            CommandType type = CommandType.values()[buffer.getInt()];
            Constructor<? extends Command> ctor = type.getCommandClass().getConstructor(ByteBuffer.class);
            ctor.setAccessible(true);
            Command command = ctor.newInstance(buffer);
            command.address = address;
            command.port = port;
            return command;
        } catch (SecurityException | ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    // Port and address are not serialized and only set by command receiver from datagrampacket metainformation
    private InetAddress address;
    private int port;

    protected abstract void fillData(ByteBuffer buffer);

    public void fill(ByteBuffer buffer) {
        buffer.putInt(CommandType.get(this).ordinal());
        fillData(buffer);
    }

    public CommandType getType() {
        return CommandType.get(this);
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Command [address=" + address + ", port=" + port + ", getType()=" + getType() + "]";
    }

}
