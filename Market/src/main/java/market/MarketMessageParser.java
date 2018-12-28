package market;

import defines.PrefixMessage;
import defines.TypeMessage;
import fixMessages.sender.MessageSender;
import views.clientView.ClientGui;

public class MarketMessageParser {
    private final Market                        market;

    // Instruments
    private MessageSender                       send;

    public MarketMessageParser(Market market) {
        this.market = market;
        send = MessageSender.getSender();
    }

    public void manageMessage(String message) {
        String[]    brokenMessage = message.split("\1");
        ClientGui   gui = market.getGui();

        if (brokenMessage.length < 6) {
            return;
        }
        else {
            brokenMessage[2] = brokenMessage[2].replaceFirst(PrefixMessage.type, "");

            if (brokenMessage[2].charAt(0) == TypeMessage.acceptConnection) {
                parseUniqueId(brokenMessage[7]);
            }
            else if (brokenMessage[2].charAt(0) == TypeMessage.order) {
                brokenMessage[7] = brokenMessage[7].replaceFirst(PrefixMessage.typeOrder, "");
                parseOrderMessage(brokenMessage);
            }
        }

        // gui updating
        gui.append(
                (brokenMessage[4].contains("ROUTER") ? "ROUTER->MARKET: " : "BROKER->MARKET: ")
                        + message.replace('\1', '|') );

        gui.getStats().updateDate(market.getProducts(), market.getClient().getUniqueId(), market.getMoney());
    }

    private void    parseUniqueId(String personalId) {
        market.getClient().setUniqueId( personalId.replaceFirst(PrefixMessage.body, "") );
    }

    private void    parseOrderMessage(String[] brokenMessage) {
        String  typeProduct = brokenMessage[8].replace(PrefixMessage.typeProduct, "");

        int     count = Integer.parseInt( brokenMessage[9].replace(PrefixMessage.countProduct, "") );
        int     price = Integer.parseInt( brokenMessage[10].replace(PrefixMessage.price, "") );

        boolean response = market.getAnalyst().analyzeOrder(brokenMessage[7].charAt(0), typeProduct, count, price);

        send.orderResponse(market.getClient(), brokenMessage, (response ? TypeMessage.execute : TypeMessage.reject));
    }
}