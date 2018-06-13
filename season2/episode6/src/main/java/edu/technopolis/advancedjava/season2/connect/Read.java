package edu.technopolis.advancedjava.season2.connect;

import edu.technopolis.advancedjava.season2.LogUtils;
import edu.technopolis.advancedjava.season2.entity.ExtendProcessStage;
import edu.technopolis.advancedjava.season2.entity.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Optional;

import static edu.technopolis.advancedjava.season2.CodeEnum.ADDRESS_TYPE;
import static edu.technopolis.advancedjava.season2.CodeEnum.CMD_NUMBER;
import static edu.technopolis.advancedjava.season2.CodeEnum.CONNECTION_PROVIDED_CODE;
import static edu.technopolis.advancedjava.season2.CodeEnum.ERROR_SOCKS_SERVER;
import static edu.technopolis.advancedjava.season2.CodeEnum.IPV4_BYTES;
import static edu.technopolis.advancedjava.season2.CodeEnum.RESERVED_BYTE;
import static edu.technopolis.advancedjava.season2.CodeEnum.SOCKS_VERSION;
import static edu.technopolis.advancedjava.season2.LogUtils.log;
import static edu.technopolis.advancedjava.season2.LogUtils.logException;

public final class Read extends ExtendProcessStage {

    @Override
    public void proceed(int op, Selector selector, Map<SocketChannel, Stage> map) {
        try {
            if (!socketChannel.isOpen()) {
                return;
            }
            int bytes = socketChannel.read(byteBuffer);
            byteBuffer.flip();
            if (bytes == -1) {
                if (socketChannel.isOpen()) {
                    socketChannel.close();
                }
                log(String.format("Socket channel [%s] close", socketChannel));
                return;
            }

            Optional<ByteBuffer> optional = Optional.of(byteBuffer);
            optional.filter(buffer -> buffer.position() < 10)
                .filter(buffer -> buffer.get() == SOCKS_VERSION.getCode())
                .filter(buffer -> buffer.get() == CMD_NUMBER.getCode())
                .filter(buffer -> buffer.get() == RESERVED_BYTE.getCode())
                .filter(buffer -> buffer.get() == ADDRESS_TYPE.getCode());
            if (!optional.isPresent()) {
                byteBuffer.clear();
                byteBuffer.put(SOCKS_VERSION.getCode())
                    .put(ERROR_SOCKS_SERVER.getCode())
                    .put(RESERVED_BYTE.getCode())
                    .put(ADDRESS_TYPE.getCode())
                    .put(new byte[4])
                    .putShort((short) 0);
                byteBuffer.flip();
                map.put(socketChannel, new Write(socketChannel,
                    serverSocketChannel, byteBuffer, null, false
                ));
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }

            byte[] ipv4 = new byte[IPV4_BYTES.getCode()];
            byteBuffer.get(ipv4);
            short port = byteBuffer.getShort();

            serverSocketChannel = SocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.connect(new InetSocketAddress(InetAddress.getByAddress(ipv4), port));
            serverSocketChannel.finishConnect();

            byteBuffer.clear();
            byteBuffer.put(SOCKS_VERSION.getCode())
                .put(CONNECTION_PROVIDED_CODE.getCode())
                .put(RESERVED_BYTE.getCode())
                .put(ADDRESS_TYPE.getCode())
                .put(ipv4)
                .putShort(port);
            byteBuffer.flip();

            Stage stage = new Pending(socketChannel, serverSocketChannel, byteBuffer);
            map.put(socketChannel, stage);
            map.put(serverSocketChannel, stage);

            serverSocketChannel.register(selector, SelectionKey.OP_CONNECT);
            socketChannel.register(selector, SelectionKey.OP_READ);

        } catch (UnknownHostException e) {
            logException("Host is unknown.", e);
        } catch (ClosedChannelException e) {
            logException("Socket channel is closed.", e);
        } catch (IOException e) {
            logException("Catch IOException.", e);
        }
    }

}
