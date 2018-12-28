package views.clientView;

import lombok.Getter;
import views.LogMessages;

import javax.swing.*;
import java.awt.*;

public class ClientGui extends JFrame {
    // components window
    private LogMessages log;
    private JScrollPane             scroll;
    @Getter private ResourcesStats  stats;

    public ClientGui(String title) {
        //window settings
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(750, 627);
        setResizable(false);

        if (title.equals("broker")) {
            setLocation(50, 400);
        } else {
            setLocation(1650, 400);
        }

        // components initialization
        stats = new ResourcesStats();

        log = new LogMessages();
        scroll = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(260, 2, 480, 600);
    }

    public void run() {
        add(scroll);
        add(stats);

        setVisible(true);
    }

    public void append(String message) {
        log.append(message);

        scroll.getViewport().setViewPosition(
                new Point(scroll.getViewport().getViewPosition().x, scroll.getViewport().getViewPosition().y + 70)
        );
        repaint();
    }
}