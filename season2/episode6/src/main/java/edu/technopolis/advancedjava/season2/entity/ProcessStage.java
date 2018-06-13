package edu.technopolis.advancedjava.season2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@NoArgsConstructor
@AllArgsConstructor
public abstract class ProcessStage implements Stage {
    protected SocketChannel socketChannel;
    protected ByteBuffer byteBuffer;
}
