package edu.technopolis.advancedjava.season2.authenticate;

import edu.technopolis.advancedjava.season2.entity.ProcessStage;
import edu.technopolis.advancedjava.season2.entity.Stage;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;

import static edu.technopolis.advancedjava.season2.CodeEnum.AUTH_METHOD;
import static edu.technopolis.advancedjava.season2.CodeEnum.AUTH_REJECT;
import static edu.technopolis.advancedjava.season2.CodeEnum.SOCKS_VERSION;
import static edu.technopolis.advancedjava.season2.LogUtils.logException;

@AllArgsConstructor
public final class Read extends ProcessStage {

    public Read(SocketChannel socketChannel, ByteBuffer byteBuffer) {
        super(socketChannel, byteBuffer);
    }

    @Override
    public void proceed(int op, Selector selector, Map<SocketChannel, Stage> map) {
        try {
            if (socketChannel.read(byteBuffer) < 3) {
                return;
            }
            byteBuffer.flip();
            Optional<ByteBuffer> optional = Optional.of(byteBuffer);
            optional
                .filter(buffer -> buffer.get() == SOCKS_VERSION.getCode())
                .filter(buffer -> isAcceptableAuthMethod(buffer.get(), buffer));
            byteBuffer.clear();
            if (optional.isPresent()) {
                byteBuffer.put(SOCKS_VERSION.getCode()).put(AUTH_METHOD.getCode());
                byteBuffer.flip();
                map.put(socketChannel, new Write(socketChannel, byteBuffer, true));
            } else {
                byteBuffer.put(SOCKS_VERSION.getCode()).put(AUTH_REJECT.getCode());
                byteBuffer.flip();
                map.put(socketChannel, new Write(socketChannel, byteBuffer, false));
            }
            socketChannel.register(selector, SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            logException("Socket channel is closed.", e);
        } catch (IOException e) {
            logException("Catch IOException.", e);
        }
    }

    private boolean isAcceptableAuthMethod(int methodsNumber, ByteBuffer buffer) {
        if (methodsNumber < 1) {
            return false;
        }
        for (int i = 0; i < methodsNumber; i++) {
            if (buffer.get() == AUTH_METHOD.getCode()) {
                return true;
            }
        }
        return false;
    }
}
