package org.peekmoon.bomberman.network;

public class HeartBeat implements Runnable {

    private final CommandSender sender;

    public HeartBeat(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                sender.ping();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

    }

}
