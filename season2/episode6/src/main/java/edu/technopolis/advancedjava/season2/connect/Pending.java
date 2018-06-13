package edu.technopolis.advancedjava.season2.connect;

import edu.technopolis.advancedjava.season2.LogUtils;
import edu.technopolis.advancedjava.season2.entity.ExtendProcessStage;
import edu.technopolis.advancedjava.season2.entity.Stage;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static edu.technopolis.advancedjava.season2.CodeEnum.ADDRESS_TYPE;
import static edu.technopolis.advancedjava.season2.CodeEnum.ERROR_SOCKS_SERVER;
import static edu.technopolis.advancedjava.season2.CodeEnum.RESERVED_BYTE;
import static edu.technopolis.advancedjava.season2.CodeEnum.SOCKS_VERSION;
import static edu.technopolis.advancedjava.season2.LogUtils.logException;

@AllArgsConstructor
public final class Pending extends ExtendProcessStage {

    public Pending(
        SocketChannel socketChannel,
        SocketChannel serverSocketChannel,
        ByteBuffer byteBuffer
    ) {
        super(socketChannel, serverSocketChannel, byteBuffer, null);
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
            if (serverSocketChannel.finishConnect()) {
                Stage stage = new Write(
                    socketChannel,
                    serverSocketChannel,
                    byteBuffer,
                    ByteBuffer.allocate(300),
                    true
                );
                map.put(socketChannel, stage);
                map.put(serverSocketChannel, stage);
                serverSocketChannel.register(selector, SelectionKey.OP_READ);
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            } else {
                byteBuffer.clear();
                byteBuffer.put(SOCKS_VERSION.getCode())
                    .put(ERROR_SOCKS_SERVER.getCode())
                    .put(RESERVED_BYTE.getCode())
                    .put(ADDRESS_TYPE.getCode())
                    .put(new byte[4])
                    .putShort((short) 0);
                byteBuffer.flip();
                socketChannel.register(selector, SelectionKey.OP_WRITE);
                map.put(
                    socketChannel,
                    new Write(socketChannel, serverSocketChannel, byteBuffer, ByteBuffer.allocate(300), false)
                );
                if (serverSocketChannel.isOpen()) {
                    serverSocketChannel.close();
                }
            }
        } catch (ClosedChannelException e) {
            logException("Socket channel is closed.", e);
        } catch (IOException e) {
            logException("Catch IOException.", e);
        }
    }

}
