package router;

import defines.TypeMessage;
import fixMessages.sender.MessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import views.routerView.RouterGui;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RouterHandler extends SimpleChannelInboundHandler<String> {
    // Route Tables
    @Getter private static final Map<String, Channel>   brokers = new HashMap<>();
    @Getter private static final Map<String, Channel>   markets = new HashMap<>();
    private static final ArrayList<String>              busyId = new ArrayList<>();

    // Instruments
    private static final MessageSender                  send = MessageSender.getSender();
    private final RouterMessageParser                   parser;
    private  RouterGui                                  gui;

    // Info about handler
    private String                                      port;
    private String                                      uniqueId;

    public RouterHandler(RouterGui gui) {
        this.gui = gui;
        parser = new RouterMessageParser(this.gui);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws java.lang.Exception {
        Channel newChannel = ctx.channel();
        port = StringUtils.right(newChannel.localAddress().toString(), 4);

        this.detectUniqueId();
        this.fillRouteTable(newChannel);

        send.connectionAccepted(newChannel, uniqueId);

        if (port.equals("5000")) {
            sendAvailableMarkets(newChannel);
        } else {
            notifyBrokers(TypeMessage.marketConnected);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws java.lang.Exception {
        if (port.equals("5000")) {
            removeBrokerFromRouteTable();
        }
        else {
            removeMarketFromRouteTable();
            notifyBrokers(TypeMessage.marketDisconnected);
        }
    }

    @Override
    protected void  channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        parser.manageMessage(s);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("Some defects: " + cause.getMessage());
    }

    /****************** Working with route table *****************/

    private void    removeBrokerFromRouteTable() {
        brokers.remove(uniqueId);
        busyId.remove(uniqueId);

        gui.append("broker (" + uniqueId + ") has been disconnected from router.");
    }

    private void    removeMarketFromRouteTable() {
        markets.remove(uniqueId);
        busyId.remove(uniqueId);

        gui.append("market (" + uniqueId + ") has been disconnected from router.");
    }

    private void    fillRouteTable(Channel newChannel) {
        if (port.equals("5000")) {
            brokers.put(uniqueId, newChannel);

            gui.append("broker (" + uniqueId + ") connected.");
        }
        else {
            markets.put(uniqueId, newChannel);

            gui.append("market (" + uniqueId + ") connected.");
        }
    }

    /***************** NOTIFIER *****************/

    private void    notifyBrokers(char typeMessage) {
        for ( Map.Entry<String, Channel> broker : brokers.entrySet() ) {
            send.newsFromRouter(broker.getValue(), typeMessage, uniqueId);
        }
    }

    private void    sendAvailableMarkets(Channel newChannel) {
        // lambda (implementation of Runnable)
        new Thread( () -> {
                        for ( Map.Entry<String, Channel> market : markets.entrySet() ) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(50);
                            } catch (InterruptedException e) {
                                System.err.println(e.getMessage());
                            }
                            send.newsFromRouter(newChannel, TypeMessage.marketConnected, market.getKey());
                        }
        } ).run();
    }

    /***************** Unique id detector *****************/

    private void    detectUniqueId() {
        SplittableRandom    rand = new SplittableRandom();
        int                 id = rand.nextInt(100000, 999999);

        for (int i = 0; i < busyId.size(); i++) {
            if ( busyId.get(i).equals(Integer.toString(id)) ) {
                id = rand.nextInt(100000, 999999);
                i = 0;
           }
       }

       uniqueId = Integer.toString(id);
       busyId.add(uniqueId);
    }
}