package router;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import views.routerView.RouterGui;

public class RouterInitializer extends ChannelInitializer<SocketChannel> {
    private RouterGui   gui;

    public RouterInitializer(RouterGui gui) {
        this.gui = gui;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new StringDecoder())
                .addLast(new StringEncoder())
                .addLast(new RouterHandler(gui));
    }
}