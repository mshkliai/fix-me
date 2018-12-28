package broker;

import database.DataBase;
import defines.PrefixMessage;
import defines.TypeMessage;
import models.Product;

public class BrokerMessageParser {
    private final Broker            broker;
    private DataBase                db;

    public BrokerMessageParser(Broker broker) {
        this.broker = broker;
        db = DataBase.getDb();
    }

    public void     manageMessage(String message) {
        String[] brokenMessage = message.split("\1");
        brokenMessage[2] = brokenMessage[2].replaceFirst(PrefixMessage.type, "");

        if (brokenMessage.length < 6) {
            return;
        }
        else {
            switch (brokenMessage[2].charAt(0)) {
                case TypeMessage.acceptConnection: parseUniqueId(brokenMessage[7]);             break;
                case TypeMessage.marketConnected: addConnectedMarket(brokenMessage[7]);         break;
                case TypeMessage.marketDisconnected: removeConnectedMarket(brokenMessage[7]);   break;
                case TypeMessage.execute: executeOrder(brokenMessage);                          break;
            }
        }

        broker.getGui().append(
                (brokenMessage[4].contains("ROUTER") ? "ROUTER->BROKER: " : "MARKET->BROKER: ")
                + message.replace('\1', '|')
        );
    }

    /***************** Connection managers ****************/

    private void    parseUniqueId(String personalId) {
        broker.getClient().setUniqueId( personalId.replaceFirst(PrefixMessage.body, "") );
    }

    private void    addConnectedMarket(String connectedMarket) {
        broker.getAvailableMarkets().add( connectedMarket.replaceFirst(PrefixMessage.body, "") );
    }

    private void    removeConnectedMarket(String disconnectedMarket) {
        broker.getAvailableMarkets().remove( disconnectedMarket.replaceFirst(PrefixMessage.body, "") );
    }

    /****************** Order managers ***********************/

    private void    executeOrder(String[] brokenMessage) {
        // Trying get product
        Product product = broker.getProducts()
                .get(brokenMessage[8].replaceFirst(PrefixMessage.typeProduct, ""));

        if (product == null) {
            return;
        }

        // Executing order
        int     count = Integer.parseInt( brokenMessage[9].replaceFirst(PrefixMessage.countProduct, "") );
        int     price = Integer.parseInt( brokenMessage[10].replaceFirst(PrefixMessage.price, "") );

        if (brokenMessage[7].replaceFirst(PrefixMessage.typeOrder, "").equals("1")) {
            executePurchase(product, count, price);
        } else {
            executeSale(product, count, price);
        }

        // new note in DataBase
        addNewTransaction(brokenMessage, count, price);
    }

    private void    executePurchase(Product product, int count, int price) {
        product.setCount(product.getCount() + count);
        broker.setMoney(broker.getMoney() - count * price);
    }

    private void    executeSale(Product product, int count, int price) {
        product.setCount(product.getCount() - count);
        broker.setMoney(broker.getMoney() + count * price);
    }

    private void    addNewTransaction(String[] brokenMessage, int count, int price) {
        String  typeProduct = brokenMessage[8].replaceFirst(PrefixMessage.typeProduct, "");
        String  typeOrder = brokenMessage[7].replaceFirst(PrefixMessage.typeOrder, "");
        String  idSender = brokenMessage[4].replaceFirst(PrefixMessage.sender, "");
        String  time = brokenMessage[6].replaceFirst(PrefixMessage.time, "");

        String  seller = (typeOrder.equals("1") ? idSender : broker.getClient().getUniqueId());
        String  buyer = (typeOrder.equals("2") ? idSender : broker.getClient().getUniqueId());

        db.newNote(time, seller, buyer, typeProduct, count, price);
    }
}