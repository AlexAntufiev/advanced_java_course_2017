package edu.technopolis.advancedjava.season2.authenticate;

import edu.technopolis.advancedjava.season2.LogUtils;
import edu.technopolis.advancedjava.season2.entity.Stage;
import edu.technopolis.advancedjava.season2.entity.WriteStage;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

import static edu.technopolis.advancedjava.season2.LogUtils.log;
import static edu.technopolis.advancedjava.season2.LogUtils.logException;

@AllArgsConstructor
public final class Write extends WriteStage {

    public Write(
        SocketChannel serverSocketChannel,
        ByteBuffer byteBuffer,
        boolean isAccept
    ) {
        super(serverSocketChannel, byteBuffer, isAccept);
    }

    @Override
    public void proceed(int op, Selector selector, Map<SocketChannel, Stage> map) {
        try {
            if (!socketChannel.isOpen()) {
                return;
            }
            while (byteBuffer.hasRemaining()) {
                socketChannel.write(byteBuffer);
            }
            byteBuffer.clear();
            if (isAccept) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                map.put(socketChannel, new Read(socketChannel, byteBuffer));
                log("Authenticate completed.");
            } else {
                if (socketChannel.isOpen()) {
                    socketChannel.close();
                }
                log("Authenticate rejected.");
            }
            log("Remote address: " + socketChannel.getRemoteAddress());
        } catch (ClosedChannelException e) {
            logException("Socket channel is closed.", e);
        } catch (IOException e) {
            logException("Catch IOException.", e);
        }
    }
}
