package views;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LogMessages extends JTextArea {
    private static final String path = "../resources/fonts/font.ttf";
    private Font                font = null;

    public LogMessages() {
        super(1, 1);

        setEditable(false);
        setLineWrap(true);
    }

    @Override
    public void append(String message) {
        if (font == null) {
            loadFont();
        }
        super.append(message + "\n\n");
        setRows( getRows() + 1 );
    }

    private void    loadFont() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(13f);
            setFont(font);
        } catch (FontFormatException | IOException e) {
            System.err.println("Some error with font loading: " + e.getMessage());
        }
    }
}
