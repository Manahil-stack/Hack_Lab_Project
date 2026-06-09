package hacklab.ui;


import hacklab.model.Mission;
import hacklab.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Bottom section of the left sidebar.
 * Shows details and objectives for the currently selected mission.
 * OOP: Pure view — fires start event via Runnable callback.
 */
public class MissionBriefingPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel targetLabel;
    private JLabel descLabel;
    private JLabel rewardLabel;
    private JPanel objectivesPanel;
    private JButton startBtn;
    private Runnable startCallback;

    public MissionBriefingPanel() {
        setBackground(Theme.BG_DARKEST);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_DIM));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 230));
        buildUI();
        showEmpty();
    }

    private void buildUI() {
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel section = new JLabel("MISSION BRIEFING");
        section.setFont(new Font("Consolas", Font.BOLD, 10));
        section.setForeground(Theme.TEXT_DIM);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleLabel = new JLabel("—");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        targetLabel = new JLabel("TARGET: —");
        targetLabel.setFont(Theme.FONT_MONO_SMALL);
        targetLabel.setForeground(Theme.TEXT_WARNING);
        targetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        descLabel = new JLabel("");
        descLabel.setFont(Theme.FONT_MONO_SMALL);
        descLabel.setForeground(Theme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rewardLabel = new JLabel("");
        rewardLabel.setFont(new Font("Consolas", Font.BOLD, 11));
        rewardLabel.setForeground(Theme.ACCENT_GREEN);
        rewardLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        objectivesPanel = new JPanel();
        objectivesPanel.setOpaque(false);
        objectivesPanel.setLayout(new BoxLayout(objectivesPanel, BoxLayout.Y_AXIS));
        objectivesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        startBtn = buildStartButton();
        startBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(section);
        inner.add(Box.createVerticalStrut(6));
        inner.add(titleLabel);
        inner.add(Box.createVerticalStrut(3));
        inner.add(targetLabel);
        inner.add(Box.createVerticalStrut(6));
        inner.add(descLabel);
        inner.add(Box.createVerticalStrut(6));
        inner.add(rewardLabel);
        inner.add(Box.createVerticalStrut(8));
        inner.add(objectivesPanel);
        inner.add(Box.createVerticalStrut(10));
        inner.add(startBtn);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    public void showMission(Mission m) {
        titleLabel.setText(m.getTitle());
        targetLabel.setText("TARGET: " + m.getTarget());
        String desc = m.getDescription().replace("\n", "<br>");
        descLabel.setText("<html><body style='width:220px;color:#7A9AAB;font-family:Consolas;font-size:11px'>"
            + desc + "</body></html>");
        rewardLabel.setText("+" + m.getXpReward() + " XP  |  +" + m.getCreditReward() + " CREDITS");

        objectivesPanel.removeAll();
        JLabel objTitle = new JLabel("OBJECTIVES:");
        objTitle.setFont(new Font("Consolas", Font.BOLD, 10));
        objTitle.setForeground(Theme.TEXT_DIM);
        objectivesPanel.add(objTitle);
        for (String cmd : m.getRequiredCommands()) {
            JLabel obj = new JLabel("  o  " + cmd);
            obj.setFont(Theme.FONT_MONO_SMALL);
            obj.setForeground(Theme.TEXT_SECONDARY);
            objectivesPanel.add(obj);
        }

        startBtn.setEnabled(!m.isCompleted());
        startBtn.setVisible(true);
        revalidate();
        repaint();
    }

    public void showEmpty() {
        titleLabel.setText("Select a mission");
        targetLabel.setText("");
        descLabel.setText("<html><body style='width:220px;color:#3A5260;font-family:Consolas;font-size:11px'>"
            + "Choose an operation from the list above to view details.</body></html>");
        rewardLabel.setText("");
        objectivesPanel.removeAll();
        startBtn.setVisible(false);
        revalidate();
        repaint();
    }

    public void setStartCallback(Runnable r) { this.startCallback = r; }

    private JButton buildStartButton() {
        JButton btn = new JButton("LAUNCH MISSION") {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { if (isEnabled()) { hovered = true;  repaint(); } }
                    public void mouseExited (MouseEvent e) {                    hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (!isEnabled()) {
                    g2.setColor(Theme.BG_CARD);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                    g2.setColor(Theme.TEXT_DIM);
                } else if (hovered) {
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
                g2.setFont(new Font("Consolas", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btn.setPreferredSize(new Dimension(240, 34));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> { if (startCallback != null) startCallback.run(); });
        return btn;
    }
}