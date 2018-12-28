package views.routerView;

import views.LogMessages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RouterGui extends JFrame {
    private LogMessages logMessages;
    private LogMessages logDb;

    private JScrollPane scrollMessages;
    private JScrollPane scrollDb;

    public RouterGui() {
        // window settings
        super("router");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(850, 400, 750, 627);
        setLayout(null);

        // components initialization
        logMessages = new LogMessages();
        scrollMessages = new JScrollPane(logMessages, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollMessages.setBounds(280, 10, 450, 580);

        logDb = new LogMessages();
        scrollDb = new JScrollPane(logDb, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollDb.setBounds(5, 10, 280, 580);
    }

    public void run() {
        add(scrollMessages);
        add(scrollDb);

        setVisible(true);

        append("Router started listening ports 5000, 5001 ...");
    }

    public void append(String message) {
        logMessages.append(message);

        scrollMessages.getViewport().setViewPosition(
                new Point(scrollMessages.getViewport().getViewPosition().x, scrollMessages.getViewport().getViewPosition().y + 70)
        );
        repaint();
    }

    public void updateTransactions(ArrayList<String> transactions) {
        logDb.setText(null);
        logDb.setRows(transactions.size());

        for (String str : transactions) {
            logDb.append(str);
        }

        scrollDb.getViewport().setViewPosition(
                new Point(scrollDb.getViewport().getViewPosition().x, transactions.size() * 175)
        );
        repaint();
    }
}