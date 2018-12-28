package views.clientView;

import models.Product;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourcesStats extends JPanel {
    private static String           path;

    private JLabel                  id;
    private Font                    font;

    private HashMap<String, JLabel>  labels;

    public ResourcesStats() {
        path = "../resources/";

        setSize(250, 600);
        setLayout(null);
        setLocation(10, 10);

        initLabels();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawRect(2, 2, 250, 600);
        g.drawLine(2, 60, 252, 60);
    }

    public void updateDate(HashMap<String, Product> products, String idClient, int money) {
        id.setText("ID: " + idClient);
        labels.get("money").setText(" : " + money);

        for (Map.Entry<String, Product> product : products.entrySet()) {
            labels.get(product.getKey()).setText(" : " + product.getValue().getCount());
        }
    }

    private void    initLabels() {
        loadFont();
        fillMap();
        adaptationMap();
    }

    private void    fillMap() {
        labels = new HashMap<>();

        id = new JLabel("");
        id.setLocation(15, 10);
        id.setSize(250, 40);
        id.setFont(font);
        add(id);

        JLabel  money = new JLabel("");
        money.setLocation(5, 70);
        money.setSize(250, 40);

        JLabel  gold = new JLabel("");
        gold.setLocation(5, 130);
        gold.setSize(250, 40);

        JLabel  silver = new JLabel("");
        silver.setLocation(5, 190);
        silver.setSize(250, 40);

        JLabel  platinum = new JLabel("");
        platinum.setLocation(5, 250);
        platinum.setSize(250, 40);

        JLabel  oil = new JLabel("");
        oil.setLocation(5, 310);
        oil.setSize(250, 40);

        JLabel  apple = new JLabel("");
        apple.setLocation(5, 370);
        apple.setSize(250, 40);

        JLabel  microsoft = new JLabel("");
        microsoft.setLocation(5, 430);
        microsoft.setSize(250, 40);

        JLabel  amazon = new JLabel("");
        amazon.setLocation(5, 490);
        amazon.setSize(250, 40);

        JLabel  mcdonalds = new JLabel("");
        mcdonalds.setLocation(5, 550);
        mcdonalds.setSize(250, 40);

        labels.put("money", money);
        labels.put("GOLD", gold);
        labels.put("SLVR", silver);
        labels.put("PLTN", platinum);
        labels.put("OIL", oil);
        labels.put("AAPL", apple);
        labels.put("MSFT", microsoft);
        labels.put("AMZN", amazon);
        labels.put("MCD", mcdonalds);
    }

    private void    adaptationMap() {
        for (Map.Entry<String, JLabel> label : labels.entrySet()) {
            label.getValue().setIcon(new ImageIcon(path + "icons/" + label.getKey() + ".png"));
            label.getValue().setFont(font);
            add(label.getValue());
        }
    }

    private void    loadFont() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path + "fonts/font.ttf")).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font: " + e.getMessage());
        }
    }
}
