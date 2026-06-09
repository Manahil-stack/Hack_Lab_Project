package hacklab.ui;

import hacklab.engine.TerminalEngine;
import hacklab.model.Mission;
import hacklab.util.Theme;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

 class TerminalPanel extends JPanel {

    private final JTextPane  outputPane;
    private final JTextField inputField;
    private final TerminalEngine engine;
    private Runnable onMissionComplete;

    public TerminalPanel(TerminalEngine engine) {
        this.engine = engine;
        setBackground(Theme.BG_DARKEST);
        setLayout(new BorderLayout());

        // ── Header bar ────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0x08, 0x0C, 0x10));
        header.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel termTitle = new JLabel("REAL TERMINAL  —  HACKLAB OS v2.4");
        termTitle.setFont(new Font("Consolas", Font.BOLD, 11));
        termTitle.setForeground(Theme.ACCENT_CYAN);

        JPanel dots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        dots.setOpaque(false);
        for (Color c : new Color[]{Theme.ACCENT_RED, Theme.ACCENT_YELLOW, Theme.ACCENT_GREEN}) {
            JLabel d = new JLabel("●");
            d.setForeground(c);
            d.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 10));
            dots.add(d);
        }
        header.add(termTitle, BorderLayout.WEST);
        header.add(dots,      BorderLayout.EAST);

        // ── Output pane ───────────────────────────────
        outputPane = new JTextPane();
        outputPane.setBackground(Theme.BG_DARKEST);
        outputPane.setForeground(Theme.TEXT_PRIMARY);
        outputPane.setFont(Theme.FONT_MONO);
        outputPane.setEditable(false);
        outputPane.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
        outputPane.setCaretColor(Theme.ACCENT_CYAN);

        JScrollPane scroll = new JScrollPane(outputPane);
        scroll.setBorder(null);
        scroll.setBackground(Theme.BG_DARKEST);

        // ── Input row ─────────────────────────────────
        JPanel inputRow = new JPanel(new BorderLayout(8, 0));
        inputRow.setBackground(new Color(0x08, 0x0C, 0x10));
        inputRow.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_DIM),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        JLabel prompt = new JLabel("root@hacklab:~$ ");
        prompt.setFont(new Font("Consolas", Font.BOLD, 13));
        prompt.setForeground(Theme.ACCENT_GREEN);

        inputField = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        inputField.setBackground(new Color(0x08, 0x0C, 0x10));
        inputField.setForeground(Theme.TEXT_PRIMARY);
        inputField.setCaretColor(Theme.ACCENT_CYAN);
        inputField.setFont(new Font("Consolas", Font.PLAIN, 13));
        inputField.setBorder(null);
        inputField.addActionListener(e -> submitCommand());

        JButton runBtn = buildRunButton();

        inputRow.add(prompt,    BorderLayout.WEST);
        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(runBtn,    BorderLayout.EAST);

        add(header,   BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);
        add(inputRow, BorderLayout.SOUTH);

        // ── Register engine listener ──────────────────
        engine.setListener(new TerminalEngine.TerminalOutputListener() {
            @Override public void onOutput(String line, TerminalEngine.OutputType type) {
                if ("__CLEAR__".equals(line)) clearTerminal();
                else appendLine(line, colorFor(type));
            }
            @Override public void onMissionComplete() {
                if (onMissionComplete != null) SwingUtilities.invokeLater(onMissionComplete);
            }
            @Override public void onMissionFailed(String reason) {
                appendLine("MISSION FAILED: " + reason, Theme.TEXT_ERROR);
            }
        });

        printWelcome();
    }

    // ── Public API ────────────────────────────────
    public void loadMission(Mission mission) {
        engine.setActiveMission(mission);
        appendLine("", Theme.TEXT_PRIMARY);
        appendLine("═══════════════════════════════════════════════════", Theme.BORDER_DIM);
        appendLine("  MISSION LOADED: " + mission.getTitle(), Theme.ACCENT_CYAN);
        appendLine("  TARGET  : " + mission.getTarget(), Theme.TEXT_WARNING);
        appendLine("  " + mission.getDescription().replace("\n", "\n  "), Theme.TEXT_SECONDARY);
        appendLine("  DIFFICULTY: " + mission.getDifficulty().getLabel()
                   + "  " + mission.getDifficulty().getBar(), Theme.TEXT_INFO);
        appendLine("  REWARD  : +" + mission.getXpReward() + " XP  |  +" + mission.getCreditReward() + " CREDITS",
                   Theme.ACCENT_GREEN);
        appendLine("  Type 'status' to see objectives.", Theme.TEXT_INFO);
        appendLine("═══════════════════════════════════════════════════", Theme.BORDER_DIM);
        appendLine("", Theme.TEXT_PRIMARY);
        inputField.requestFocus();
    }

    public void setOnMissionComplete(Runnable r) { this.onMissionComplete = r; }
    public void focusInput()                      { inputField.requestFocus(); }

    // ── Private helpers ───────────────────────────
    private void submitCommand() {
        String cmd = inputField.getText().trim();
        inputField.setText("");
        if (!cmd.isEmpty()) {
            engine.processCommand(cmd);
            SwingUtilities.invokeLater(() ->
                outputPane.setCaretPosition(outputPane.getDocument().getLength()));
        }
    }

    public void appendLine(String text, Color color) {
        SwingUtilities.invokeLater(() -> {
            StyledDocument doc   = outputPane.getStyledDocument();
            Style          style = outputPane.addStyle("", null);
            StyleConstants.setForeground(style, color);
            StyleConstants.setFontFamily(style, "Consolas");
            StyleConstants.setFontSize(style, 13);
            try {
                doc.insertString(doc.getLength(), text + "\n", style);
                outputPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException ignored) {}
        });
    }

    private void clearTerminal() {
        SwingUtilities.invokeLater(() -> {
            outputPane.setText("");
            printWelcome();
        });
    }

    private void printWelcome() {
        appendLine("╔══════════════════════════════════════════════════╗", Theme.ACCENT_CYAN);
        appendLine("║     H A C K L A B   O S   v 2 . 4               ║", Theme.ACCENT_CYAN);
        appendLine("║     Secure terminal. All ops simulated.          ║", Theme.TEXT_SECONDARY);
        appendLine("╚══════════════════════════════════════════════════╝", Theme.ACCENT_CYAN);
        appendLine("", Theme.TEXT_PRIMARY);
        appendLine("Type 'help' for available commands.", Theme.TEXT_INFO);
        appendLine("Select a mission from the left panel to begin.", Theme.TEXT_INFO);
        appendLine("", Theme.TEXT_PRIMARY);
    }

    private static Color colorFor(TerminalEngine.OutputType type) {
        switch (type) {
            case SUCCESS: return Theme.TEXT_SUCCESS;
            case ERROR:   return Theme.TEXT_ERROR;
            case WARNING: return Theme.TEXT_WARNING;
            case COMMAND: return Theme.TEXT_CMD;
            case INFO:    return Theme.TEXT_INFO;
            default:      return Theme.TEXT_PRIMARY;
        }
    }

    private JButton buildRunButton() {
        JButton btn = new JButton("RUN") {
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
                g2.setColor(hovered ? Theme.ACCENT_GREEN : Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(hovered ? Theme.BG_DARKEST : Theme.ACCENT_GREEN);
                if (!hovered) {
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 4, 4);
                }
                g2.setFont(new Font("Consolas", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setPreferredSize(new Dimension(48, 28));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> submitCommand());
        return btn;
    }
}
