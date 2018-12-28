package broker;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BrokerMessageHandler extends SimpleChannelInboundHandler<String> {
    private final BrokerMessageParser   parser;

    public BrokerMessageHandler(Broker broker) {
        this.parser = new BrokerMessageParser(broker);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        parser.manageMessage(s);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("Some defects: " + cause.getMessage());
    }
}