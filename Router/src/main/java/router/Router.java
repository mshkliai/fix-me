package router;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import views.routerView.RouterGui;

import javax.swing.*;

public class Router {
    private final RouterGui     gui;

    private static final int[]  ports = new int[2];
    private ServerBootstrap     server;
    private Channel[]           channels = new Channel[2];

    public Router() {
        gui = new RouterGui();

        ports[0] = 5000;
        ports[1] = 5001;
    }

    public void run() {
        SwingUtilities.invokeLater(gui::run);

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup workers = new NioEventLoopGroup();

        server = new ServerBootstrap()
                .group(boss, workers)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RouterInitializer(gui));

        try {
            initChannels();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        } finally {
            boss.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }

    private void    initChannels() throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            channels[i] = server.bind(ports[i]).sync().channel();
        }
        for (int i = 0; i < 2; i++) {
            channels[i].closeFuture().sync();
        }
    }
}