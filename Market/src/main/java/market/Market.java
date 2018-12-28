package market;

import Client.*;
import lombok.Getter;
import models.Trader;
import views.clientView.ClientGui;

import javax.swing.*;
import java.net.UnknownHostException;

public class Market extends Trader {
    @Getter private final Client            client;
    @Getter private final OrderAnalyst      analyst;
    @Getter private final ClientGui         gui;

    public Market() {
        client = new Client(new ClientInitializer( new MarketMessageHandler(this) ), 5001);
        analyst = new OrderAnalyst(this);
        gui = new ClientGui("market");

        this.setMoney( getMoney() + 50000 );
    }

    public void run() throws UnknownHostException, InterruptedException {
        client.run();
        SwingUtilities.invokeLater(gui::run);
    }
}