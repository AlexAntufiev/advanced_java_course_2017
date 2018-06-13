package edu.technopolis.advancedjava.season2.connect;

import edu.technopolis.advancedjava.season2.LogUtils;
import edu.technopolis.advancedjava.season2.entity.Stage;
import edu.technopolis.advancedjava.season2.entity.WriteStage;
import edu.technopolis.advancedjava.season2.exchange.Exchange;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static edu.technopolis.advancedjava.season2.LogUtils.log;
import static edu.technopolis.advancedjava.season2.LogUtils.logException;

public final class Write extends WriteStage {

    public Write(
        SocketChannel socketChannel,
        SocketChannel serverSocketChannel,
        ByteBuffer byteBuffer,
        ByteBuffer serverByteBuffer,
        boolean isAccept
    ) {
        super(socketChannel, serverSocketChannel, byteBuffer, serverByteBuffer, isAccept);
    }

    @Override
    public void proceed(int op, Selector selector, Map<SocketChannel, Stage> map) {
        try {
            if (!socketChannel.isOpen()) {
                if (serverSocketChannel.isOpen()) {
                    selector.close();
                }
                return;
            }
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            if (isAccept) {
                serverByteBuffer.flip();
                map.put(
                    socketChannel,
                    new Exchange(socketChannel, serverSocketChannel, byteBuffer, serverByteBuffer)
                );
                map.put(serverSocketChannel, new Exchange(serverSocketChannel, socketChannel,
                    serverByteBuffer, byteBuffer
                ));
                socketChannel.register(selector, SelectionKey.OP_READ);
                serverSocketChannel.register(selector, SelectionKey.OP_READ);
                log("Connect completed.");
            } else {
                log("Connect rejected." + serverSocketChannel.getRemoteAddress());
            }
            log(String.format("Remote address: %s", serverSocketChannel.getRemoteAddress()));
        } catch (ClosedChannelException e) {
            logException("Catch ClosedChannelException.", e);
        } catch (IOException e) {
            logException("Catch IOException.", e);
        }
    }
}
