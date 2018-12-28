package router;

import database.DataBase;
import defines.PrefixMessage;
import defines.TypeMessage;
import fixMessages.sender.MessageSender;
import io.netty.channel.Channel;
import views.routerView.RouterGui;

public class RouterMessageParser {
    private static final MessageSender  send = MessageSender.getSender();
    private RouterGui                   gui;
    private DataBase                    db;

    public RouterMessageParser(RouterGui gui) {
        this.gui = gui;
        db = DataBase.getDb();
    }

    public void     manageMessage(String message) throws Exception {
        String[] brokenMessage = message.split("\1");

        if (brokenMessage.length < 6) {
            gui.append("Bad format of message");
        } else if (!controlCheckSum(brokenMessage, message)) {
            gui.append("Invalid check sum of message");
        }
        else {
            brokenMessage[2] = brokenMessage[2].replaceFirst(PrefixMessage.type, "");

            if (brokenMessage[2].charAt(0) == TypeMessage.order) {
                sendMessageToMarket(brokenMessage, message);
            } else {
                sendMessageToBroker(brokenMessage, message);
            }

            if (brokenMessage[2].charAt(0) == TypeMessage.execute) {
                gui.updateTransactions( db.getNotes() );
            }
        }
    }

    private boolean     controlCheckSum(String[] brokenMessage, String message) {
        String  messageWithoutCheckSum = message.replaceFirst(brokenMessage[brokenMessage.length - 1] + "\1", "");
        int     shownCheckSum = Integer.parseInt(brokenMessage[brokenMessage.length - 1].replaceFirst(PrefixMessage.checkSum, ""));
        int     realCheckSum = 0;

        for (byte b : messageWithoutCheckSum.getBytes()) {
            realCheckSum += b;
        }

        return (realCheckSum & 255) == shownCheckSum;
    }

    private void        sendMessageToMarket(String[] brokenMessage, String message) {
        String  idReceiver = brokenMessage[5].replace(PrefixMessage.receiver, "");
        Channel receiver = RouterHandler.getMarkets().get(idReceiver);

        if (receiver == null) {
            gui.append("Market excepted");
        } else {
            gui.append("BROKER->MARKET: " + message.replace('\1', '|'));
            send.formedMessage(receiver, message);
        }
    }

    private void        sendMessageToBroker(String[] brokenMessage, String message) {
        String  idReceiver = brokenMessage[5].replace(PrefixMessage.receiver, "");
        Channel receiver = RouterHandler.getBrokers().get(idReceiver);

        if (receiver == null) {
            gui.append("Broker excepted");
        } else {
            gui.append("MARKET->BROKER: " + message.replace('\1', '|'));
            send.formedMessage(receiver, message);
        }
    }
}