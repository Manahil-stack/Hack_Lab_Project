package hacklab.ui;


import hacklab.engine.TerminalEngine;
import hacklab.model.Mission;
import hacklab.model.Player;
import hacklab.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class MainWindow extends JFrame {

    private final Player         player;
    private final TerminalEngine engine;
    private final List<Mission>  missions;

    private PlayerHUD            hudPanel;
    private MissionPanel         missionPanel;
    private MissionBriefingPanel briefingPanel;
    private TerminalPanel        terminalPanel;
    private Mission              selectedMission;

    public MainWindow(String codename) {
        this.player   = new Player(codename);
        this.engine   = new TerminalEngine(player);
        this.missions = Mission.createDefaultMissions();

        setTitle("HackLab: Hacker Simulator — " + codename);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setIconImage(buildIcon());

        buildLayout();
        wireEvents();
    }

    // ── Layout ────────────────────────────────────
    private void buildLayout() {
        hudPanel      = new PlayerHUD(player);
        missionPanel  = new MissionPanel(missions);
        briefingPanel = new MissionBriefingPanel();
        terminalPanel = new TerminalPanel(engine);

        // Left side bar : mission list + briefing
        JPanel leftSidebar = new JPanel(new BorderLayout());
        leftSidebar.setBackground(Theme.BG_PANEL);
        leftSidebar.setPreferredSize(new Dimension(280, 0));
        leftSidebar.add(missionPanel,  BorderLayout.CENTER);
        leftSidebar.add(briefingPanel, BorderLayout.SOUTH);

        // 1- pex divider between side bar and terminal
        JPanel divider = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BORDER_DIM);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        divider.setPreferredSize(new Dimension(1, 0));

        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setBackground(Theme.BG_PANEL);
        sidebarWrapper.add(leftSidebar, BorderLayout.CENTER);
        sidebarWrapper.add(divider,     BorderLayout.EAST);

        // Root
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARKEST);
        root.add(hudPanel,       BorderLayout.NORTH);
        root.add(sidebarWrapper, BorderLayout.WEST);
        root.add(terminalPanel,  BorderLayout.CENTER);
        setContentPane(root);
    }

    // ── Event wiring ──────────────────────────────
    private void wireEvents() {

        // 1. Mission card clicked → show briefing
        missionPanel.setSelectionCallback(mission -> {
            selectedMission = mission;
            briefingPanel.showMission(mission);
        });

        // 2. Start button clicked → load mission in terminal
        briefingPanel.setStartCallback(() -> {
            if (selectedMission != null && !selectedMission.isCompleted()) {
                terminalPanel.loadMission(selectedMission);
                terminalPanel.focusInput();
            }
        });

        // 3. Mission complete → award XP/credits, refresh HUD
        terminalPanel.setOnMissionComplete(() -> {
            if (selectedMission != null && !selectedMission.isCompleted()) {
                selectedMission.markCompleted();
                player.addXP(selectedMission.getXpReward());
                player.addCredits(selectedMission.getCreditReward());
                player.completeMission();
                if (selectedMission.getBadgeReward() != null) {
                    player.earnBadge(selectedMission.getBadgeReward());
                }
                hudPanel.refresh();
                missionPanel.refreshCard(selectedMission);
                briefingPanel.showMission(selectedMission);
                showCompletionPopup();
            }
        });
    }
    // ── Mission complete popup ────────────────────
    private void showCompletionPopup() {
        JDialog popup = new JDialog(this, false);
        popup.setUndecorated(true);
        popup.setBackground(new Color(0, 0, 0, 0));

        JPanel content = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(Theme.ACCENT_CYAN);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 12, 12);
            }
        };
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel star = new JLabel("  MISSION COMPLETE  ");
        star.setFont(new Font("Consolas", Font.BOLD, 14));
        star.setForeground(Theme.ACCENT_CYAN);
        star.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rankLine = new JLabel("RANK: " + player.getRank().getTitle());
        rankLine.setFont(new Font("Consolas", Font.BOLD, 18));
        rankLine.setForeground(Theme.ACCENT_GREEN);
        rankLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stats = new JLabel("XP: " + player.getXp() + "   CREDITS: " + player.getCredits());
        stats.setFont(Theme.FONT_MONO_SMALL);
        stats.setForeground(Theme.TEXT_SECONDARY);
        stats.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(star);
        content.add(Box.createVerticalStrut(8));
        content.add(rankLine);
        content.add(Box.createVerticalStrut(4));
        content.add(stats);

        popup.setContentPane(content);
        popup.pack();
        popup.setLocationRelativeTo(this);
        popup.setVisible(true);

        // Auto-dismiss after 3 seconds
        Timer t = new Timer(3000, e -> popup.dispose());
        t.setRepeats(false);
        t.start();
    }

    // ── Programmatic app icon ─────────────────────
    private Image buildIcon() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.BG_DARKEST);
        g2.fillRoundRect(0, 0, 32, 32, 8, 8);
        g2.setColor(Theme.ACCENT_CYAN);
        g2.setStroke(new BasicStroke(2));
        int[] xp = {16, 28, 28, 16, 4, 4};
        int[] yp = {3,  8,  20, 29, 20, 8};
        g2.drawPolygon(xp, yp, 6);
        g2.setFont(new Font("Consolas", Font.BOLD, 14));
        g2.drawString("H", 10, 22);
        g2.dispose();
        return img;
    }
}
