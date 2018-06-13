package edu.technopolis.advancedjava.season2.exchange;

import edu.technopolis.advancedjava.season2.entity.ExtendProcessStage;
import edu.technopolis.advancedjava.season2.entity.Stage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static edu.technopolis.advancedjava.season2.LogUtils.logException;

public final class Exchange extends ExtendProcessStage {

    public Exchange(SocketChannel client, SocketChannel server, ByteBuffer clientBuffer, ByteBuffer serverBuffer) {
        super(client, server, clientBuffer, serverBuffer);
    }

    @Override
    public void proceed(int op, Selector selector, Map<SocketChannel, Stage> map) {
        try {
            if (!socketChannel.isOpen()) {
                if (serverSocketChannel.isOpen()) {
                    serverSocketChannel.close();
                }
                return;
            }
            if (!serverSocketChannel.isOpen()) {
                if (socketChannel.isOpen()) {
                    socketChannel.close();
                }
                return;
            }
            if (op == SelectionKey.OP_READ) {
                if (byteBuffer.hasRemaining()) {
                    return;
                }
                byteBuffer.clear();
                int bytes = socketChannel.read(byteBuffer);
                byteBuffer.flip();
                if (bytes > 0) {
                    serverSocketChannel.register(selector, SelectionKey.OP_WRITE);
                } else {
                    if (socketChannel.isOpen()) {
                        socketChannel.close();
                    }
                    if (selector.isOpen()) {
                        serverSocketChannel.close();
                    }
                }
            } else if (op == SelectionKey.OP_WRITE) {
                while (serverByteBuffer.hasRemaining()) {
                    socketChannel.write(serverByteBuffer);
                }
                socketChannel.register(selector, SelectionKey.OP_READ);
            }

        } catch (ClosedChannelException e) {
            logException("Socket channel is closed.", e);
        } catch (IOException e) {
            logException("Catch IOException.", e);
        }
    }
}
