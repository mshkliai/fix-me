package broker;

import fixMessages.sender.MessageSender;
import views.clientView.ClientGui;
import views.clientView.ResourcesStats;

import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradingAlgorithm {
    private final Broker           broker;
    private final SplittableRandom rand;
    private final MessageSender    send;
    private static Logger          logger;
    private ClientGui              gui;

    private final String[] typeProducts = {
            "GOLD",
            "SLVR",
            "PLTN",
            "OIL",
            "AAPL",
            "MSFT",
            "AMZN",
            "MCD"
    };

    public TradingAlgorithm(Broker broker) {
        this.broker = broker;
        this.rand = new SplittableRandom();
        this.send = MessageSender.getSender();
        logger = Logger.getLogger(Thread.currentThread().getName());
    }

    public void startTrading() {
        gui = broker.getGui();
        ResourcesStats statsGui = gui.getStats();

        while (true) {
            try {
                statsGui.updateDate(broker.getProducts(),
                        broker.getClient().getUniqueId(),
                        broker.getMoney());

                TimeUnit.SECONDS.sleep(rand.nextInt(2, 6));

                if (!broker.getClient().getChannel().isActive()) {
                    gui.append("Connection with router broken... Please, try restart program");
                } else {
                    makeTrading();
                }
            }
            catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Some problem with timer");
            }
        }
    }

    private void makeTrading() {

        if (broker.getAvailableMarkets().size() == 0) {
            gui.append("Waiting for available markets...");
        }
        else {
            String typeProduct = typeProducts[rand.nextInt(8)];

            if (broker.getProducts().get(typeProduct).getCount() > 200) {
                sellProduct(typeProduct);
            } else {
                buyProduct(typeProduct);
            }
        }
    }

    private void buyProduct(String typeProduct) {
        String  idReceiver = broker.getAvailableMarkets().get(rand.nextInt(broker.getAvailableMarkets().size()));
        int     countProduct = rand.nextInt(100, 1000);
        int     profitPrice = broker.getProducts().get(typeProduct).getPrice();

        if (profitPrice * countProduct > broker.getMoney()) {
            sellProduct(typeProduct);
        } else {
            send.order(broker.getClient(), '1', idReceiver, typeProduct, countProduct, profitPrice);
        }
    }

    private void sellProduct(String typeProduct) {
        String idReceiver = broker.getAvailableMarkets().get(rand.nextInt(broker.getAvailableMarkets().size()));
        int    countProduct = broker.getProducts().get(typeProduct).getCount();
        int    price = broker.getProducts().get(typeProduct).getPrice() +
                ((broker.getProducts().get(typeProduct).getPrice() * 5) / 100);

        // initialization of count products to sale
        if (countProduct == 0) {
            return;
        }

        countProduct = rand.nextInt(countProduct >> 2, countProduct);
        if (countProduct == 0) {
            countProduct = broker.getProducts().get(typeProduct).getCount();
        }

        // order
        send.order(broker.getClient(), '2', idReceiver, typeProduct, countProduct, price);
    }
}