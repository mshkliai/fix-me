package broker;

import Client.*;
import lombok.Getter;
import lombok.Setter;
import models.Trader;
import views.clientView.ClientGui;

import javax.swing.*;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Broker extends Trader {
    @Getter private final Client                client;
    private final TradingAlgorithm              algorithm;
    @Getter final private  ClientGui            gui;

    @Getter @Setter private ArrayList<String>   availableMarkets;

    public Broker() {
        client = new Client(new ClientInitializer( new BrokerMessageHandler(this) ), 5000);
        algorithm = new TradingAlgorithm(this);
        gui = new ClientGui("broker");

        availableMarkets = new ArrayList<>();
    }

    public void run() throws InterruptedException, UnknownHostException {
        client.run();
        SwingUtilities.invokeLater(gui::run);
        algorithm.startTrading();
    }
}