import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App {

    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private static final String MAIN_MENU = "mainMenu";
    private static final String GROUPS = "groups";
    private static final String GROUP_INFO = "groupInfo";

    private DefaultListModel<String> groupListModel;
    private JLabel groupInfoLabel;

    public App() {
        frame = new JFrame("Менеджер музыкальных групп");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Styles.BACKGROUND_COLOR);

        mainPanel.add(createMainMenu(), MAIN_MENU);
        mainPanel.add(createGroupsPanel(), GROUPS);
        mainPanel.add(createGroupInfoPanel(), GROUP_INFO);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // error window, appear on every mistake
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createMainMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Styles.BACKGROUND_COLOR);

        JLabel title = new JLabel("Менеджер музыкальных групп", SwingConstants.CENTER);
        title.setFont(Styles.TITLE_FONT);
        JPanel topBar = new JPanel();
        topBar.setBackground(Styles.SECONDARY_COLOR);
        topBar.add(title);
        panel.add(topBar, BorderLayout.NORTH);

        // creates main menu
        JPanel grid = new JPanel(new GridLayout(2, 2, 15, 15));
        grid.setBackground(Styles.BACKGROUND_COLOR);
        grid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        grid.add(createMenuCard("Группы", "🎵", e -> cardLayout.show(mainPanel, GROUPS)));
        grid.add(createMenuCard("Гастроли", "🎤", e -> JOptionPane.showMessageDialog(frame, "Окно гастролей")));
        grid.add(createMenuCard("Хит-парад", "⭐", e -> JOptionPane.showMessageDialog(frame, "Окно хит-парада")));
        grid.add(createMenuCard("Отчет", "📑", e -> generateReport()));
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    // window with groups list
    private JPanel createGroupsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Styles.BACKGROUND_COLOR);

        groupListModel = new DefaultListModel<>();
        Database.getGroups().forEach(groupListModel::addElement);

        JList<String> groupList = new JList<>(groupListModel);
        groupList.setFont(Styles.TEXT_FONT);
        JScrollPane scroll = new JScrollPane(groupList);

        JLabel title = new JLabel("Список групп:", SwingConstants.CENTER);
        title.setFont(Styles.TITLE_FONT);
        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        groupList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && groupList.getSelectedValue() != null) {
                    groupInfoLabel.setText("Информация о группе: " + groupList.getSelectedValue());
                    cardLayout.show(mainPanel, GROUP_INFO);
                }
            }
        });

        panel.add(createBottomPanel(), BorderLayout.SOUTH);
        return panel;
    }


    // panel with xml reports and editing groups
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBackground(Styles.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createButton("Добавить группу", e -> addGroup()));
        panel.add(createButton("Загрузить из XML", e -> xmlLoad()));
        panel.add(createButton("Сохранить в XML", e -> xmlSave()));
        panel.add(createButton("← Назад", e -> cardLayout.show(mainPanel, MAIN_MENU)));

        return panel;
    }


    private JPanel createGroupInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Styles.BACKGROUND_COLOR);

        groupInfoLabel = new JLabel("", SwingConstants.CENTER);
        groupInfoLabel.setFont(Styles.TEXT_FONT);

        JButton back = createButton("← Назад", e -> cardLayout.show(mainPanel, GROUPS));

        panel.add(groupInfoLabel, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = Styles.styledButton(text);
        Styles.addHoverEffect(btn);
        btn.addActionListener(listener);
        return btn;
    }

    private JPanel createMenuCard(String title, String icon, java.awt.event.ActionListener listener) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Styles.BORDER_COLOR, 1));
        card.setBackground(Styles.BACKGROUND_COLOR);

        JButton btn = new JButton(icon);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        btn.setBackground(Styles.PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener(listener);
        Styles.addHoverEffect(btn);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(Styles.TEXT_FONT);

        card.add(btn, BorderLayout.CENTER);
        card.add(label, BorderLayout.SOUTH);
        return card;
    }

    private void addGroup() {
        try {
            String name = JOptionPane.showInputDialog(frame, "Введите название группы:");
            Validator.checkNotEmpty(name, "Название группы");

            String year = JOptionPane.showInputDialog(frame, "Введите год образования:");
            Validator.checkIsNumber(year, "Год образования");

            groupListModel.addElement(name + " (" + year + ")");
        } catch (BlankFieldError e) {
            showError(e.getMessage());
        }
    }

    private void xmlSave() {
        try {
            JFileChooser chooser = new JFileChooser(getDownloadsFolder());
            chooser.setFileFilter(new FileNameExtensionFilter("XML файлы (*.xml)", "xml"));
            if (chooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xml"))
                file = new File(file.getParentFile(), file.getName() + ".xml");

            List<String> groups = new ArrayList<>();
            for (int i = 0; i < groupListModel.size(); i++) groups.add(groupListModel.get(i));

            XMLManager.saveGroupsToXML(groups, file);
            JOptionPane.showMessageDialog(frame, "Файл XML сохранён!");
        } catch (Exception e) {
            showError("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private void xmlLoad() {
        try {
            JFileChooser chooser = new JFileChooser(getDownloadsFolder());
            chooser.setFileFilter(new FileNameExtensionFilter("XML файлы (*.xml)", "xml"));
            if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();
            List<String> groups = new ArrayList<>();
            XMLManager.loadGroupsFromXML(file, groups);

            groups.forEach(groupListModel::addElement);
            JOptionPane.showMessageDialog(frame, "Данные из XML добавлены!");
        } catch (Exception e) {
            showError("Ошибка при загрузке: " + e.getMessage());
        }
    }

    private File getDownloadsFolder() {
        String userHome = System.getProperty("user.home");
        File downloads = new File(userHome, "Downloads");
        if (!downloads.exists()) downloads = new File(userHome);
        return downloads;
    }

    private void generateReport() {
        List<String> groups = new ArrayList<>();
        for (int i = 0; i < groupListModel.size(); i++)
            groups.add(groupListModel.get(i));

        PDFReport.generateReportDialog(frame, groups, getDownloadsFolder());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}
