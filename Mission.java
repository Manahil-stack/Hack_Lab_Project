package hacklab.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hacking mission.
 * OOP: Abstraction — exposes what a mission IS, not how it's run.
 */
public class Mission {

    public enum Difficulty {
        EASY  ("EASY",   "▮▯▯▯▯"),
        MEDIUM("MEDIUM", "▮▮▮▯▯"),
        HARD  ("HARD",   "▮▮▮▮▯"),
        ELITE ("ELITE",  "▮▮▮▮▮");

        private final String label;
        private final String bar;
        Difficulty(String label, String bar) { this.label = label; this.bar = bar; }
        public String getLabel() { return label; }
        public String getBar()   { return bar; }
    }

    private final String id;
    private final String title;
    private final String description;
    private final String target;
    private final Difficulty difficulty;
    private final int xpReward;
    private final int creditReward;
    private final List<String> requiredCommands;
    private final String badgeReward;
    private boolean completed;

    public Mission(String id, String title, String description, String target,
                   Difficulty difficulty, int xpReward, int creditReward,
                   List<String> requiredCommands, String badgeReward) {
        this.id               = id;
        this.title            = title;
        this.description      = description;
        this.target           = target;
        this.difficulty       = difficulty;
        this.xpReward         = xpReward;
        this.creditReward     = creditReward;
        this.requiredCommands = requiredCommands;
        this.badgeReward      = badgeReward;
        this.completed        = false;
    }

    public void markCompleted() { this.completed = true; }

    // ── Getters ────────────────────────────────────
    public String getId()                        { return id; }
    public String getTitle()                     { return title; }
    public String getDescription()               { return description; }
    public String getTarget()                    { return target; }
    public Difficulty getDifficulty()            { return difficulty; }
    public int getXpReward()                     { return xpReward; }
    public int getCreditReward()                 { return creditReward; }
    public List<String> getRequiredCommands()    { return requiredCommands; }
    public String getBadgeReward()               { return badgeReward; }
    public boolean isCompleted()                 { return completed; }

    // ── Factory: default mission set ──────────────
    public static List<Mission> createDefaultMissions() {
        List<Mission> missions = new ArrayList<>();

        missions.add(new Mission("M001", "HACK LOGIN PANEL",
            "Target company suspected of data leaks.\nYour mission: breach their login panel.",
            "192.168.1.105", Difficulty.EASY, 150, 200,
            List.of("ping", "nmap", "exploit login"),
            "First Blood"));

        missions.add(new Mission("M002", "ANALYSE SERVER LOGS",
            "Infiltrate the server farm and extract\nencrypted log files before detection.",
            "10.0.0.42", Difficulty.MEDIUM, 300, 450,
            List.of("nmap", "ssh", "wget logs", "decrypt"),
            "Log Master"));

        missions.add(new Mission("M003", "FIND OPEN PORTS",
            "Map the entire network infrastructure\nand identify all vulnerable entry points.",
            "172.16.0.1", Difficulty.MEDIUM, 280, 400,
            List.of("nmap -sV", "nmap -p-", "report"),
            "Port Scanner"));

        missions.add(new Mission("M004", "BYPASS FIREWALL",
            "Advanced firewall blocking all traffic.\nFind the gap. Get through. Leave no trace.",
            "203.0.113.77", Difficulty.HARD, 600, 800,
            List.of("probe", "spoof", "tunnel", "bypass"),
            "Ghost Mode"));

        missions.add(new Mission("M005", "SQL INJECTION ATTACK",
            "Target's database is poorly protected.\nInject your payload and extract credentials.",
            "10.10.10.99", Difficulty.HARD, 650, 900,
            List.of("scan db", "inject payload", "dump tables", "extract"),
            "SQL Ninja"));

        missions.add(new Mission("M006", "ZERO-DAY EXPLOIT",
            "CLASSIFIED. Eyes only. Deploy the\nzero-day exploit on the government mainframe.",
            "CLASSIFIED", Difficulty.ELITE, 2000, 3000,
            List.of("recon", "load exploit", "execute payload", "cover tracks", "exfil"),
            "Zero Day"));

        return missions;
    }
}
