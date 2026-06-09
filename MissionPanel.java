package hacklab.ui;


import hacklab.model.Mission;
import hacklab.util.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.function.Consumer;


public class MissionPanel extends JPanel {

    private Consumer<Mission> selectionCallback;
    private Mission selectedMission;
    private final List<Mission> missions;

    public MissionPanel(List<Mission> missions) {
        this.missions = missions;
        setBackground(Theme.BG_PANEL);
        setPreferredSize(new Dimension(280, 0));
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {

        // ── Header bar ────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_DARKEST);
        header.setBorder(new EmptyBorder(16, 16, 12, 16));

        JLabel title = new JLabel("AVAILABLE MISSIONS");
        title.setFont(new Font("Consolas", Font.BOLD, 11));
        title.setForeground(Theme.ACCENT_CYAN);

        JLabel count = new JLabel(missions.size() + " ops");
        count.setFont(Theme.FONT_MONO_SMALL);
        count.setForeground(Theme.TEXT_DIM);

        header.add(title, BorderLayout.WEST);
        header.add(count, BorderLayout.EAST);

        // ── Scrollable card list ───────────────────────
        JPanel listPanel = new JPanel();
        listPanel.setBackground(Theme.BG_PANEL);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        for (Mission m : missions) {
            listPanel.add(buildCard(m));
            listPanel.add(Box.createVerticalStrut(6));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(Theme.BG_PANEL);

        add(header,  BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
    }

    private JPanel buildCard(Mission mission) {
        JPanel card = new JPanel() {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                    public void mouseClicked(MouseEvent e) { selectMission(mission); }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean sel = (mission == selectedMission);
                if (sel) {
                    g2.setColor(new Color(0, 245, 200, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.setColor(Theme.ACCENT_CYAN);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                    g2.fillRoundRect(0, 0, 3, getHeight(), 3, 3); // Left accent
                } else if (hovered) {
                    g2.setColor(Theme.BG_HOVER);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                } else {
                    g2.setColor(Theme.BG_CARD);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                    g2.setColor(Theme.BORDER_DIM);
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                }
            }
        };
        card.setLayout(new BorderLayout(8, 4));
        card.setBorder(new EmptyBorder(10, 14, 10, 14));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Status dot
        JLabel dot = new JLabel(mission.isCompleted() ? "✔" : "○");
        dot.setFont(new Font("Consolas", Font.PLAIN, 14));
        dot.setForeground(mission.isCompleted() ? Theme.ACCENT_GREEN : Theme.TEXT_DIM);
        card.add(dot, BorderLayout.WEST);

        // Center content
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = new JLabel(mission.getTitle());
        name.setFont(new Font("Consolas", Font.BOLD, 12));
        name.setForeground(mission.isCompleted() ? Theme.TEXT_SECONDARY : Theme.TEXT_PRIMARY);

        JLabel target = new JLabel("TARGET: " + mission.getTarget());
        target.setFont(Theme.FONT_MONO_SMALL);
        target.setForeground(Theme.TEXT_DIM);

        JLabel diff = new JLabel(mission.getDifficulty().getBar() + "  " + mission.getDifficulty().getLabel());
        diff.setFont(new Font("Consolas", Font.PLAIN, 10));
        diff.setForeground(difficultyColor(mission.getDifficulty()));

        info.add(name);
        info.add(Box.createVerticalStrut(2));
        info.add(target);
        info.add(Box.createVerticalStrut(3));
        info.add(diff);
        card.add(info, BorderLayout.CENTER);

        // XP label
        JLabel xp = new JLabel("+" + mission.getXpReward() + " XP");
        xp.setFont(new Font("Consolas", Font.BOLD, 10));
        xp.setForeground(Theme.ACCENT_CYAN);
        card.add(xp, BorderLayout.EAST);

        return card;
    }

    private Color difficultyColor(Mission.Difficulty d) {
        switch (d) {
            case EASY:   return new Color(0x00, 0xE5, 0x70);
            case MEDIUM: return new Color(0xFF, 0xCC, 0x00);
            case HARD:   return new Color(0xFF, 0x88, 0x00);
            case ELITE:  return new Color(0xFF, 0x33, 0x55);
            default:     return Theme.TEXT_SECONDARY;
        }
    }

    private void selectMission(Mission mission) {
        this.selectedMission = mission;
        repaint();
        if (selectionCallback != null) selectionCallback.accept(mission);
    }

    public void setSelectionCallback(Consumer<Mission> cb) { this.selectionCallback = cb; }

    public void refreshCard(Mission mission) {
        SwingUtilities.invokeLater(this::repaint);
    }
}