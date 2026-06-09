package hacklab.model;


import java.util.ArrayList;
import java.util.List;


public class Player {

    public enum Rank {
        SCRIPT_KIDDIE("Script Kiddie", 0),
        NOVICE_HACKER("Novice Hacker", 500),
        GREY_HAT("Grey Hat", 1500),
        BLACK_HAT("Black Hat", 3500),
        ELITE_HACKER("Elite Hacker", 7000),
        LEGEND("L E G E N D", 15000);

        private final String title;
        private final int requiredXP;

        Rank(String title, int requiredXP) {
            this.title = title;
            this.requiredXP = requiredXP;
        }

        public String getTitle()     { return title; }
        public int getRequiredXP()   { return requiredXP; }

        public static Rank fromXP(int xp) {
            Rank current = SCRIPT_KIDDIE;
            for (Rank r : values()) {
                if (xp >= r.requiredXP) current = r;
            }
            return current;
        }
    }

    private String codename;
    private int xp;
    private int credits;
    private int completedMissions;
    private List<String> unlockedTools;
    private List<String> badges;

    public Player(String codename) {
        this.codename          = codename;
        this.xp                = 0;
        this.credits           = 500;
        this.completedMissions = 0;
        this.unlockedTools     = new ArrayList<>();
        this.badges            = new ArrayList<>();
        unlockedTools.add("nmap");
        unlockedTools.add("ping");
    }

    public void addXP(int amount)         { this.xp += amount; }
    public void addCredits(int amount)    { this.credits += amount; }
    public void completeMission()         { this.completedMissions++; }

    public void unlockTool(String tool) {
        if (!unlockedTools.contains(tool)) unlockedTools.add(tool);
    }

    public void earnBadge(String badge) {
        if (!badges.contains(badge)) badges.add(badge);
    }

    public Rank getRank() { return Rank.fromXP(xp); }

    public int getXPToNextRank() {
        Rank current = getRank();
        Rank[] ranks = Rank.values();
        for (int i = 0; i < ranks.length - 1; i++) {
            if (ranks[i] == current) return ranks[i + 1].getRequiredXP() - xp;
        }
        return 0;
    }

    // ── Getters & Setters ──────────────────────────
    public String getCodename()             { return codename; }
    public int getXp()                      { return xp; }
    public int getCredits()                 { return credits; }
    public int getCompletedMissions()       { return completedMissions; }
    public List<String> getUnlockedTools()  { return unlockedTools; }
    public List<String> getBadges()         { return badges; }
    public void setCodename(String c)       { this.codename = c; }
}
