package edu.technopolis.advancedjava.season2.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@NoArgsConstructor
@AllArgsConstructor
public abstract class ExtendProcessStage extends ProcessStage {
    protected ByteBuffer serverByteBuffer;
    protected SocketChannel serverSocketChannel;

    public ExtendProcessStage(
        SocketChannel socketChannel,
        SocketChannel serverSocketChannel,
        ByteBuffer byteBuffer,
        ByteBuffer serverByteBuffer
    ) {
        super(socketChannel, byteBuffer);
        this.serverByteBuffer = serverByteBuffer;
        this.serverSocketChannel = serverSocketChannel;
    }
}
