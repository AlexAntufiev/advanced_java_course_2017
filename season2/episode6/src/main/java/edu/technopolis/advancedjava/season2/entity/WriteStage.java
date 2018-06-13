package edu.technopolis.advancedjava.season2.entity;

import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@NoArgsConstructor
public abstract class WriteStage extends ExtendProcessStage {
    protected boolean isAccept;

    public WriteStage(
        SocketChannel socketChannel,
        SocketChannel serverSocketChannel,
        ByteBuffer byteBuffer,
        ByteBuffer serverByteBuffer,
        boolean isAccept
    ) {
        super(socketChannel, serverSocketChannel, byteBuffer, serverByteBuffer);
        this.isAccept = isAccept;
    }

    public WriteStage(SocketChannel serverSocketChannel, ByteBuffer serverByteBuffer, boolean isAccept) {
        super(serverByteBuffer, serverSocketChannel);
        this.isAccept = isAccept;
    }
}
