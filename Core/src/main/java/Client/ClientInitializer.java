package Client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SimpleChannelInboundHandler<String> messageHandler;

    public ClientInitializer(SimpleChannelInboundHandler<String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new StringEncoder())
                .addLast(new StringDecoder())
                .addLast(messageHandler);
    }
}
