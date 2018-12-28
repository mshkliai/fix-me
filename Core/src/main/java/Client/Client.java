package Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Client {
    @Setter @Getter private String                  uniqueId = "";
    @Getter         private Channel                 channel;
    private final ChannelInitializer<SocketChannel> handler;
    private final int                               port;

    public Client(ChannelInitializer<SocketChannel> handler, int port) {
        this.handler = handler;
        this.port = port;
    }

    public void run() throws UnknownHostException, InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrapClient = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(handler)
                    .remoteAddress(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), port));

            channel = bootstrapClient.connect().sync().channel();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            worker.shutdownGracefully();
            System.exit(0);
        }
    }
}