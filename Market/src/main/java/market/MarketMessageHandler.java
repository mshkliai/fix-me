package market;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MarketMessageHandler extends SimpleChannelInboundHandler<String> {
    private final MarketMessageParser   parser;

    public MarketMessageHandler(Market market) {
        this.parser = new MarketMessageParser(market);
    }

    @Override
    protected void  channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        parser.manageMessage(s);
    }

    @Override
    public void     exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("Some defects: " + cause.getMessage());
    }
}
