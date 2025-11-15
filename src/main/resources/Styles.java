import javax.swing.*;
import java.awt.*;

public class Styles {

    // 🎨 Цвета
    public static final Color PRIMARY_COLOR = new Color(200, 220, 255);
    public static final Color SECONDARY_COLOR = new Color(220, 220, 220);
    public static final Color BORDER_COLOR = new Color(150, 150, 150);
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR = Color.BLACK;

    // 🖋️ Шрифты
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // 🪟 Отступы
    public static final Insets BUTTON_PADDING = new Insets(8, 15, 8, 15);

    // 🧩 Общие настройки для кнопок
    public static JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ✨ Hover эффект для кнопки (светлее при наведении)
    public static void addHoverEffect(JButton button) {
        Color normal = button.getBackground();
        Color hover = normal.brighter();

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }
}
