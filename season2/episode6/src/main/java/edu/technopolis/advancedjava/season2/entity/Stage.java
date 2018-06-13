package edu.technopolis.advancedjava.season2.entity;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

public interface Stage {

    void proceed(int op, Selector selector, Map<SocketChannel, Stage> map);
}
