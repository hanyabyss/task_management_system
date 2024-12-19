package observer;

import javax.swing.*;

public class DynamicNotification extends JLabel implements Observer {
    public DynamicNotification() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        setBackground(java.awt.Color.DARK_GRAY);
        setForeground(java.awt.Color.WHITE);
    }

    @Override
    public void update(String message) {
        setText(message);
        setVisible(true);

        // إخفاء الإشعار بعد 3 ثوانٍ
        Timer timer = new Timer(3000, e -> setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
}
