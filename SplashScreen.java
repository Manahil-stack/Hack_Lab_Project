package hacklab.ui;


import hacklab.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel       statusLabel;
    private JTextField   codenameField;
    private JButton      launchBtn;
    private Timer        animTimer;
    private int          progress = 0;

    private final String[] bootMessages = {
        "Initializing kernel modules...",
        "Loading exploit database...",
        "Connecting to dark network...",
        "Bypassing firewall protocols...",
        "Encrypting comms channel...",
        "Establishing secure tunnel...",
        "Loading mission database...",
        "System ready. Welcome, Agent."
    };

    public SplashScreen() {
        setSize(600, 400);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Background
                g2.setColor(Theme.BG_DARKEST);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Grid lines
                g2.setColor(new Color(0, 245, 200, 8));
                for (int x = 0; x < getWidth();  x += 30) g2.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 30) g2.drawLine(0, y, getWidth(), y);
                // Outer glow border
                g2.setColor(new Color(0, 245, 200, 80));
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(1, 1, getWidth()-3, getHeight()-3);
                g2.setColor(new Color(0, 245, 200, 25));
                g2.setStroke(new BasicStroke(6));
                g2.drawRect(4, 4, getWidth()-9, getHeight()-9);
            }
        };
        root.setOpaque(false);

        // ── Header ───────────────────────────────────────
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BG_DARKEST);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        JLabel shield = new JLabel("⬡", SwingConstants.CENTER);
        shield.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 60));
        shield.setForeground(Theme.ACCENT_CYAN);
        shield.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("H A C K L A B", SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 28));
        title.setForeground(Theme.ACCENT_CYAN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("[ HACKER SIMULATOR v2.4 ]", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_MONO_SMALL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(shield);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        // ── Center: boot progress → then login ───────────
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        statusLabel = new JLabel("Initializing...", SwingConstants.CENTER);
        statusLabel.setFont(Theme.FONT_MONO_SMALL);
        statusLabel.setForeground(Theme.ACCENT_GREEN);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                int w = (int)((double) getValue() / getMaximum() * getWidth());
                if (w > 0) {
                    GradientPaint gp = new GradientPaint(0, 0, Theme.ACCENT_CYAN, w, 0, Theme.ACCENT_GREEN);
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, w, getHeight(), 6, 6);
                }
                g2.setColor(Theme.BORDER_DIM);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
            }
        };
        progressBar.setPreferredSize(new Dimension(400, 8));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Codename input (hidden until boot completes)
        JLabel codenameLabel = new JLabel("ENTER CODENAME:");
        codenameLabel.setFont(Theme.FONT_MONO_SMALL);
        codenameLabel.setForeground(Theme.TEXT_SECONDARY);
        codenameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        codenameLabel.setVisible(false);

        codenameField = new JTextField("AGENT_X") {
            @Override protected void paintComponent(Graphics g) {
                ((Graphics2D) g).setColor(Theme.BG_CARD);
                ((Graphics2D) g).fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        codenameField.setFont(new Font("Consolas", Font.BOLD, 16));
        codenameField.setForeground(Theme.ACCENT_CYAN);
        codenameField.setCaretColor(Theme.ACCENT_CYAN);
        codenameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        codenameField.setHorizontalAlignment(JTextField.CENTER);
        codenameField.setMaximumSize(new Dimension(300, 40));
        codenameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        codenameField.setVisible(false);

        launchBtn = createCyberButton("[ INITIATE MISSION ]");
        launchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        launchBtn.setVisible(false);

        center.add(statusLabel);
        center.add(Box.createVerticalStrut(12));
        center.add(progressBar);
        center.add(Box.createVerticalStrut(30));
        center.add(codenameLabel);
        center.add(Box.createVerticalStrut(8));
        center.add(codenameField);
        center.add(Box.createVerticalStrut(14));
        center.add(launchBtn);

        // ── Footer ───────────────────────────────────────
        JLabel footer = new JLabel("CODDYKIT © 2025 — ALL EXPLOITS SIMULATED", SwingConstants.CENTER);
        footer.setFont(new Font("Consolas", Font.PLAIN, 10));
        footer.setForeground(Theme.TEXT_DIM);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);

        // ── Boot animation timer ──────────────────────────
        final int[] msgIdx = {0};
        animTimer = new Timer(40, null);
        animTimer.addActionListener(e -> {
            progress++;
            progressBar.setValue(progress);
            if (progress % 12 == 0 && msgIdx[0] < bootMessages.length) {
                statusLabel.setText(bootMessages[msgIdx[0]++]);
            }
            if (progress >= 100) {
                animTimer.stop();
                statusLabel.setText("System online. Identify yourself.");
                codenameLabel.setVisible(true);
                codenameField.setVisible(true);
                launchBtn.setVisible(true);
                codenameField.requestFocus();
                codenameField.selectAll();
            }
        });

        launchBtn.addActionListener(e -> launch());
        codenameField.addActionListener(e -> launch());
    }

    private JButton createCyberButton(String text) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovered) {
                    g2.setColor(Theme.ACCENT_CYAN);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                    g2.setColor(Theme.BG_DARKEST);
                } else {
                    g2.setColor(Theme.BG_CARD);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                    g2.setColor(Theme.ACCENT_CYAN);
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
                }
                g2.setFont(new Font("Consolas", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth()  - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        btn.setPreferredSize(new Dimension(260, 40));
        btn.setMaximumSize (new Dimension(260, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void launch() {
        String codename = codenameField.getText().trim();
        if (codename.isEmpty()) codename = "AGENT_X";
        dispose();
        new MainWindow(codename).setVisible(true);
    }

   
    public void showAndProceed() {
        setVisible(true);
        animTimer.start();
    }
}
