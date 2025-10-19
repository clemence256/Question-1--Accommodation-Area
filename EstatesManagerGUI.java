import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class AccommodationArea {
    protected String name;
    protected int occupants;
    protected boolean[] lights = new boolean[3]; // lights 1–3

    public AccommodationArea(String name) {
        this.name = name;
        this.occupants = 0;
    }

    public void addOccupants(int n) {
        occupants += n;
    }

    public void removeOccupants(int n) {
        occupants = Math.max(0, occupants - n);
    }

    public void switchOnLight(int n) {
        if (n >= 1 && n <= 3) lights[n - 1] = true;
    }

    public void switchOffLight(int n) {
        if (n >= 1 && n <= 3) lights[n - 1] = false;
    }

    public String getLightsStatus() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append("Light ").append(i + 1).append(": ")
              .append(lights[i] ? "💡 ON" : "⚫ OFF").append("   ");
        }
        return sb.toString();
    }

    public String reportStatus() {
        return "🏢 Area: " + name + "\n" +
               "👥 Occupants: " + occupants + "\n" +
               getLightsStatus() + "\n";
    }
}

class GymArea extends AccommodationArea {
    public GymArea() { super("Gym Area"); }
}

class SwimmingArea extends AccommodationArea {
    public SwimmingArea() { super("Swimming Area"); }
}

public class EstatesManagerGUI extends JFrame {

    private final JComboBox<String> areaSelector;
    private final JTextField occupantField;
    private final JTextField lightField;
    private final JTextArea outputArea;

    private final AccommodationArea gym = new GymArea();
    private final AccommodationArea swim = new SwimmingArea();
    private AccommodationArea activeArea = gym;

    public EstatesManagerGUI() {
        // --- Window settings ---
        setTitle("🏢 Estates Manager - Speke Apartments");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        Color bgColor = new Color(240, 248, 255); // Light blue
        Color btnColor = new Color(0, 102, 204);  // Deep blue
        Color textColor = Color.WHITE;

        // --- Title Banner ---
        JLabel title = new JLabel("🏠 Estates Manager System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(btnColor);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // --- Main Panel ---
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // --- Area Selection ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(bgColor);
        topPanel.add(new JLabel("Active Area: "));
        areaSelector = new JComboBox<>(new String[]{"Gym", "Swimming"});
        areaSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaSelector.addActionListener(e -> switchArea());
        topPanel.add(areaSelector);
        mainPanel.add(topPanel);

        // --- Occupant Controls ---
        JPanel occPanel = new JPanel();
        occPanel.setBackground(bgColor);
        occPanel.add(new JLabel("Occupants: "));
        occupantField = new JTextField(6);
        occPanel.add(occupantField);

        JButton addBtn = styledButton("Add Occupants", "➕", btnColor, textColor);
        JButton removeBtn = styledButton("Remove Occupants", "➖", btnColor, textColor);
        occPanel.add(addBtn);
        occPanel.add(removeBtn);
        mainPanel.add(occPanel);

        // --- Light Controls ---
        JPanel lightPanel = new JPanel();
        lightPanel.setBackground(bgColor);
        lightPanel.add(new JLabel("Light No (1–3): "));
        lightField = new JTextField(6);
        lightPanel.add(lightField);

        JButton onBtn = styledButton("Switch ON", "💡", btnColor, textColor);
        JButton offBtn = styledButton("Switch OFF", "💤", btnColor, textColor);
        lightPanel.add(onBtn);
        lightPanel.add(offBtn);
        mainPanel.add(lightPanel);

        add(mainPanel, BorderLayout.CENTER);

        // --- Output Area ---
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(new Color(255, 255, 255));
        outputArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        outputArea.setText("Welcome to Estates Manager!\n\n");
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // --- Bottom Bar ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bgColor);
        JButton reportBtn = styledButton("Report", "📊", btnColor, textColor);
        JButton quitBtn = styledButton("Quit", "🚪", new Color(200, 0, 0), Color.WHITE);
        bottomPanel.add(reportBtn);
        bottomPanel.add(quitBtn);
        add(bottomPanel, BorderLayout.PAGE_END);

        // --- Button Actions ---
        addBtn.addActionListener(e -> handleAdd());
        removeBtn.addActionListener(e -> handleRemove());
        onBtn.addActionListener(e -> handleLight(true));
        offBtn.addActionListener(e -> handleLight(false));
        reportBtn.addActionListener(e -> handleReport());
        quitBtn.addActionListener(e -> System.exit(0));
    }

    private JButton styledButton(String text, String icon, Color bg, Color fg) {
        JButton btn = new JButton(icon + " " + text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void switchArea() {
        String selected = (String) areaSelector.getSelectedItem();
        activeArea = selected.equals("Gym") ? gym : swim;
        output("🔄 Switched to " + selected + " Area.\n");
    }

    private void handleAdd() {
        try {
            int n = Integer.parseInt(occupantField.getText().trim());
            if (n < 0) throw new NumberFormatException();
            activeArea.addOccupants(n);
            output("✅ " + n + " occupants added.\n");
        } catch (NumberFormatException ex) {
            error("❌ Invalid input! Enter a positive integer for occupants.\n");
        }
    }

    private void handleRemove() {
        try {
            int n = Integer.parseInt(occupantField.getText().trim());
            if (n < 0) throw new NumberFormatException();
            activeArea.removeOccupants(n);
            output("➖ " + n + " occupants removed.\n");
        } catch (NumberFormatException ex) {
            error("❌ Invalid input! Enter a positive integer for occupants.\n");
        }
    }

    private void handleLight(boolean on) {
        try {
            int n = Integer.parseInt(lightField.getText().trim());
            if (n < 1 || n > 3) throw new NumberFormatException();
            if (on) {
                activeArea.switchOnLight(n);
                output("💡 Light " + n + " switched ON.\n");
            } else {
                activeArea.switchOffLight(n);
                output("💤 Light " + n + " switched OFF.\n");
            }
        } catch (NumberFormatException ex) {
            error("❌ Invalid light number! Enter 1, 2, or 3.\n");
        }
    }

    private void handleReport() {
        output("📊 STATUS REPORT:\n" + activeArea.reportStatus());
    }

    private void output(String msg) {
        outputArea.append(msg);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new EstatesManagerGUI().setVisible(true);
        });
    }
}
