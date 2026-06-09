package hacklab.ui;


import hacklab.model.Player;
import hacklab.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;


public class PlayerHUD extends JPanel {

    private final Player player;
    private JLabel rankLabel;
    private JLabel codenameLabel;
    private JLabel xpLabel;
    private JLabel creditsLabel;
    private JLabel missionsLabel;
    private JProgressBar xpBar;

    public PlayerHUD(Player player) {
        this.player = player;
        setBackground(Theme.BG_DARKEST);
        setPreferredSize(new Dimension(0, 68));
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {

        // ── Left: brand ───────────────────────────────
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        left.setOpaque(false);

        JLabel logo = new JLabel("HACKLAB");
        logo.setFont(new Font("Consolas", Font.BOLD, 18));
        logo.setForeground(Theme.ACCENT_CYAN);

        JLabel ver = new JLabel("OS v2.4");
        ver.setFont(Theme.FONT_MONO_SMALL);
        ver.setForeground(Theme.TEXT_DIM);

        left.add(logo);
        left.add(ver);

        // ── Center: codename + rank + XP bar ──────────
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        codenameLabel = new JLabel("AGENT: " + player.getCodename());
        codenameLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        codenameLabel.setForeground(Theme.TEXT_PRIMARY);
        codenameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rankLabel = new JLabel(player.getRank().getTitle());
        rankLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        rankLabel.setForeground(Theme.ACCENT_CYAN);
        rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        xpBar = new JProgressBar(0, 100) {
            @Override protected void paintComponent(Graphics g) {Graphics2D g2 = (Graphics2D) g;

float[] fractions = {0f, 0.5f, 1f};

Color[] colors = {
    Theme.BG_DARKEST,
    Theme.ACCENT_CYAN,
    Theme.BG_DARKEST
};

LinearGradientPaint paint =
    new LinearGradientPaint(
        new Point2D.Float(0, 0),
        new Point2D.Float(getWidth(), 0),
        fractions,
        colors
    );

g2.setPaint(paint);
g2.fillRect(0, 0, getWidth(), 1);}
        };
        xpBar.setMaximumSize(new Dimension(200, 5));
        xpBar.setPreferredSize(new Dimension(200, 5));
        xpBar.setBorderPainted(false);
        xpBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(codenameLabel);
        center.add(Box.createVerticalStrut(2));
        center.add(rankLabel);
        center.add(Box.createVerticalStrut(3));
        center.add(xpBar);

        // ── Right: stats ──────────────────────────────
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        right.setOpaque(false);

        xpLabel       = makeStatLabel("XP",       String.valueOf(player.getXp()),                 Theme.ACCENT_CYAN);
        creditsLabel  = makeStatLabel("CREDITS",  String.valueOf(player.getCredits()),             Theme.ACCENT_YELLOW);
        missionsLabel = makeStatLabel("OPS DONE", String.valueOf(player.getCompletedMissions()),   Theme.ACCENT_GREEN);

        right.add(xpLabel);
        right.add(makeDivider());
        right.add(creditsLabel);
        right.add(makeDivider());
        right.add(missionsLabel);

        // ── Wrapper with padding ──────────────────────
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        wrapper.add(left,   BorderLayout.WEST);
        wrapper.add(center, BorderLayout.CENTER);
        wrapper.add(right,  BorderLayout.EAST);

        // Bottom separator gradient
        JPanel sep = new JPanel() {JPanel sep = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g;

                float[] fractions = {0f, 0.5f, 1f};

                Color[] colors = {
                    Theme.BG_DARKEST,
                    Theme.ACCENT_CYAN,
                    Theme.BG_DARKEST
                };

                LinearGradientPaint paint =
                    new LinearGradientPaint(
                        new Point2D.Float(0, 0),
                        new Point2D.Float(getWidth(), 0),
                        fractions,
                        colors
                    );

                g2.setPaint(paint);
                g2.fillRect(0, 0, getWidth(), 1);
            }
        };};
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setOpaque(false);

        add(wrapper, BorderLayout.CENTER);
        add(sep,     BorderLayout.SOUTH);
    }

    // ── Helpers ───────────────────────────────────
    private JLabel makeStatLabel(String title, String value, Color color) {
        return new JLabel("<html>" +
            "<span style='color:#3A5260;font-size:9px;font-family:Consolas'>" + title + "</span><br>" +
            "<span style='color:" + hex(color) + ";font-size:13px;font-family:Consolas;font-weight:bold'>" + value + "</span>" +
            "</html>");
    }

    private JPanel makeDivider() {
        JPanel d = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BORDER_DIM);
                g.fillRect(0, 4, 1, getHeight() - 8);
            }
        };
        d.setOpaque(false);
        d.setPreferredSize(new Dimension(1, 30));
        return d;
    }

    private String hex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    /** Call after player stats change to redraw labels and XP bar. */
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            codenameLabel.setText("AGENT: " + player.getCodename());
            rankLabel.setText(player.getRank().getTitle());
            xpLabel      .setText(makeStatLabel("XP",       String.valueOf(player.getXp()),               Theme.ACCENT_CYAN  ).getText());
            creditsLabel .setText(makeStatLabel("CREDITS",  String.valueOf(player.getCredits()),           Theme.ACCENT_YELLOW).getText());
            missionsLabel.setText(makeStatLabel("OPS DONE", String.valueOf(player.getCompletedMissions()), Theme.ACCENT_GREEN ).getText());
            xpBar.repaint();
            revalidate();
            repaint();
        });
    }
}