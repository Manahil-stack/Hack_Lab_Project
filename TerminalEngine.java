package hacklab.engine;


import hacklab.model.Mission;
import hacklab.model.Player;
import java.util.*;


public class TerminalEngine {

    private final Player player;
    private Mission activeMission;
    private List<String> executedCommands;
    private TerminalOutputListener listener;

    // ── Listener interface (Observer pattern) ─────
    public interface TerminalOutputListener {
        void onOutput(String line, OutputType type);
        void onMissionComplete();
        void onMissionFailed(String reason);
    }

    public enum OutputType {
        SYSTEM, SUCCESS, ERROR, WARNING, INFO, COMMAND
    }

    
    private static final Map<String, String[]> COMMAND_RESPONSES = new LinkedHashMap<>();

    static {
        COMMAND_RESPONSES.put("help", new String[]{
            "  Available commands:",
            "  ping <target>        — Check host reachability",
            "  nmap <target>        — Network port scanner",
            "  nmap -sV <target>    — Service version detection",
            "  nmap -p- <target>    — Scan all 65535 ports",
            "  ssh <target>         — Secure shell connection",
            "  wget <file>          — Download remote file",
            "  decrypt <file>       — Decrypt file",
            "  exploit login        — Exploit login vulnerability",
            "  scan db              — Scan database service",
            "  inject payload       — SQL injection attack",
            "  dump tables          — Extract database tables",
            "  extract              — Extract credentials",
            "  probe                — Probe firewall rules",
            "  spoof                — IP spoofing",
            "  tunnel               — Create encrypted tunnel",
            "  bypass               — Execute bypass sequence",
            "  recon                — Reconnaissance scan",
            "  load exploit         — Load zero-day exploit",
            "  execute payload      — Execute attack payload",
            "  cover tracks         — Clean log files",
            "  exfil                — Exfiltrate data",
            "  report               — Generate scan report",
            "  clear                — Clear terminal",
            "  status               — Show mission status",
        });

        COMMAND_RESPONSES.put("ping", new String[]{
            "PING target: 56 bytes of data.",
            "64 bytes from target: icmp_seq=1 ttl=64 time=2.41 ms",
            "64 bytes from target: icmp_seq=2 ttl=64 time=1.87 ms",
            "64 bytes from target: icmp_seq=3 ttl=64 time=2.03 ms",
            "--- target ping statistics ---",
            "3 packets transmitted, 3 received, 0% packet loss",
            "[+] Host is ALIVE"
        });

        COMMAND_RESPONSES.put("nmap", new String[]{
            "Starting Nmap 7.94 ( https://nmap.org )",
            "Nmap scan report for target",
            "Host is up (0.0023s latency).",
            "PORT      STATE  SERVICE",
            "22/tcp    open   ssh",
            "80/tcp    open   http",
            "443/tcp   open   https",
            "3306/tcp  open   mysql",
            "8080/tcp  open   http-proxy",
            "[+] 5 open ports discovered"
        });

        COMMAND_RESPONSES.put("nmap -sv", new String[]{
            "Starting Nmap 7.94 service scan...",
            "PORT      STATE  SERVICE   VERSION",
            "22/tcp    open   ssh       OpenSSH 7.4",
            "80/tcp    open   http      Apache 2.4.6",
            "3306/tcp  open   mysql     MySQL 5.7.28",
            "[!] MySQL 5.7.28 — CVE-2019-2534 vulnerability found!",
            "[+] Service fingerprinting complete"
        });

        COMMAND_RESPONSES.put("nmap -p-", new String[]{
            "Scanning all 65535 ports (this may take a while)...",
            "Discovered: 22, 80, 443, 3306, 8080, 31337, 54321",
            "[!] Port 31337 — Unknown service (suspicious)",
            "[!] Port 54321 — Backdoor signature detected!",
            "[+] Full port scan complete. 7 ports open."
        });

        COMMAND_RESPONSES.put("ssh", new String[]{
            "SSH connecting to target...",
            "Warning: The authenticity of host cannot be established.",
            "Attempting key-based authentication...",
            "[!] Password authentication enabled — brute force possible",
            "Connection established via fallback credentials.",
            "[+] SSH session opened"
        });

        COMMAND_RESPONSES.put("wget logs", new String[]{
            "Fetching: http://target/var/logs/system.log.enc",
            "Connecting to 10.0.0.42:80...",
            "HTTP request sent, awaiting response... 200 OK",
            "Length: 2,048,512 bytes (2.0 MB)",
            "system.log.enc    [====================] 100%",
            "[+] Encrypted log file downloaded: system.log.enc"
        });

        COMMAND_RESPONSES.put("decrypt", new String[]{
            "Loading decryption module...",
            "Detected cipher: AES-256-CBC",
            "Brute-forcing IV... [████████████████] done",
            "Key recovered: 8f3a91c4e2b076d5",
            "Decrypting system.log.enc...",
            "[+] Decryption successful: system.log (2.0 MB)"
        });

        COMMAND_RESPONSES.put("exploit login", new String[]{
            "Loading exploit module: CVE-2021-44228...",
            "Target: login panel @ /admin/login.php",
            "Injecting malformed JWT token...",
            "Bypass triggered — authentication skipped.",
            "POST /admin/dashboard HTTP/1.1 → 200 OK",
            "[+] LOGIN PANEL BREACHED. Access granted."
        });

        COMMAND_RESPONSES.put("scan db", new String[]{
            "Database scanner initializing...",
            "Detected: MySQL 5.7.28 on port 3306",
            "Testing default credentials...",
            "root:'' — FAILED | root:root — SUCCESS",
            "[!] Root access with default password!",
            "[+] Database exposed. Proceeding to injection."
        });

        COMMAND_RESPONSES.put("inject payload", new String[]{
            "SQL injection payload: ' OR 1=1; --",
            "Sending to: /search?q=",
            "Response: 200 OK (abnormal content length)",
            "Payload ' UNION SELECT * FROM users-- accepted.",
            "[!] VULNERABLE TO UNION-BASED INJECTION",
            "[+] Injection successful."
        });

        COMMAND_RESPONSES.put("dump tables", new String[]{
            "Dumping schema from information_schema...",
            "Tables found: users, sessions, payments, admin_keys",
            "Dumping: users (2,341 rows)...",
            "Dumping: admin_keys (12 rows)...",
            "[+] All tables extracted to: ./dump.sql"
        });

        COMMAND_RESPONSES.put("extract", new String[]{
            "Parsing credentials from dump.sql...",
            "admin : $2y$10$hashed_pass [MD5 — crackable]",
            "Cracking hash with rockyou.txt...",
            "admin : p@$$w0rd123",
            "[+] 2,341 credentials extracted and saved."
        });

        COMMAND_RESPONSES.put("probe", new String[]{
            "Probing firewall rule set...",
            "ACL entries: 847 rules loaded",
            "Fuzzing packet headers...",
            "[!] Rule #312 — accepts fragmented UDP on port 53",
            "[+] Firewall gap found via DNS tunnel vector."
        });

        COMMAND_RESPONSES.put("spoof", new String[]{
            "IP spoofing module loaded.",
            "Crafting packets with whitelisted source IP: 10.0.0.1",
            "Sending 1000 spoofed probes...",
            "Firewall accepting spoofed packets.",
            "[+] IP spoofing active — identity masked."
        });

        COMMAND_RESPONSES.put("tunnel", new String[]{
            "Establishing DNS-over-HTTPS tunnel...",
            "Routing traffic via port 443 (HTTPS)...",
            "Tunnel endpoint: covert-relay.onion:8443",
            "Encryption: ChaCha20-Poly1305",
            "[+] Encrypted tunnel established. Firewall blind to traffic."
        });

        COMMAND_RESPONSES.put("bypass", new String[]{
            "Executing multi-stage firewall bypass...",
            "[1/3] Fragment injection — SUCCESS",
            "[2/3] Rule spoofing — SUCCESS",
            "[3/3] Tunnel activation — SUCCESS",
            "[+] FIREWALL BYPASSED. Full network access granted."
        });

        COMMAND_RESPONSES.put("recon", new String[]{
            "OSINT gathering on target...",
            "Passive DNS: 4 subdomains mapped",
            "WHOIS: Registrar data redacted (privacy shield)",
            "Shodan fingerprint: OpenSSH 8.0, Apache 2.4",
            "LinkedIn employees: 12 with IT/Dev roles",
            "[+] Recon complete. Attack surface mapped."
        });

        COMMAND_RESPONSES.put("load exploit", new String[]{
            "Loading classified zero-day module...",
            "Exploit: CVE-2024-CLASSIFIED (CVSS 10.0)",
            "Target: Linux kernel 5.x — heap overflow",
            "Payload: remote code execution + root escalation",
            "Compiling shellcode for target architecture (x86_64)...",
            "[+] Zero-day loaded and armed."
        });

        COMMAND_RESPONSES.put("execute payload", new String[]{
            "Delivering payload to target kernel...",
            "Heap spray initiated...",
            "Overflow triggered at 0xffffc90000003e08",
            "Shellcode executing as UID=0 (root)",
            "Spawning reverse shell on 127.0.0.1:4444",
            "[+] ROOT SHELL OBTAINED. Full system compromise."
        });

        COMMAND_RESPONSES.put("cover tracks", new String[]{
            "Clearing bash history...",
            "Wiping /var/log/auth.log entries...",
            "Purging utmp/wtmp records...",
            "Timestomping modified files...",
            "Rootkit persistence: removed.",
            "[+] No trace left. Forensically clean."
        });

        COMMAND_RESPONSES.put("exfil", new String[]{
            "Compressing sensitive data: 4.2 GB to 847 MB",
            "Encrypting with AES-256 before transfer...",
            "Uploading via DNS tunnel to dead-drop server...",
            "Transfer complete: 847 MB in 43s",
            "Remote verification: SHA-256 checksum matched.",
            "[+] DATA EXFILTRATED. Mission accomplished."
        });

        COMMAND_RESPONSES.put("report", new String[]{
            "Generating scan report...",
            "Open ports: 7 | Vulnerabilities: 3 critical, 5 high",
            "CVEs identified: CVE-2019-2534, CVE-2021-3449",
            "Recommended actions: patch MySQL, disable port 54321",
            "[+] Report saved: scan_report.txt"
        });
    }

    // ── Constructor ───────────────────────────────
    public TerminalEngine(Player player) {
        this.player           = player;
        this.executedCommands = new ArrayList<>();
    }

    public void setListener(TerminalOutputListener listener) {
        this.listener = listener;
    }

    public void setActiveMission(Mission mission) {
        this.activeMission = mission;
        this.executedCommands.clear();
    }

    // ── Command processor ─────────────────────────
    public void processCommand(String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) return;
        String input = rawInput.trim().toLowerCase();

        emit("> " + rawInput, OutputType.COMMAND);

        if (input.equals("clear")) {
            if (listener != null) listener.onOutput("__CLEAR__", OutputType.SYSTEM);
            return;
        }
        if (input.equals("status")) { showStatus(); return; }

        String matchedKey = null;
        for (String key : COMMAND_RESPONSES.keySet()) {
            if (input.equals(key) || input.startsWith(key + " ")) {
                matchedKey = key;
                break;
            }
        }

        if (matchedKey == null) {
            emit("bash: " + rawInput + ": command not found", OutputType.ERROR);
            emit("Type 'help' to see available commands.", OutputType.INFO);
            return;
        }

        for (String line : COMMAND_RESPONSES.get(matchedKey)) {
            OutputType type = OutputType.SYSTEM;
            if (line.startsWith("[+]"))  type = OutputType.SUCCESS;
            else if (line.startsWith("[!]")) type = OutputType.WARNING;
            else if (line.startsWith("bash:")) type = OutputType.ERROR;
            emit(line, type);
        }

        if (activeMission != null) trackMissionProgress(input);
    }

    private void trackMissionProgress(String input) {
        List<String> required = activeMission.getRequiredCommands();
        for (String req : required) {
            if (input.startsWith(req.toLowerCase()) && !executedCommands.contains(req)) {
                executedCommands.add(req);
                emit("  checkmark Objective: '" + req + "' — COMPLETE", OutputType.SUCCESS);
                break;
            }
        }
        if (executedCommands.size() >= required.size()) {
            emit("", OutputType.SYSTEM);
            emit("╔══════════════════════════════════╗", OutputType.SUCCESS);
            emit("║     MISSION COMPLETE              ║", OutputType.SUCCESS);
            emit("╚══════════════════════════════════╝", OutputType.SUCCESS);
            emit("+  XP: +" + activeMission.getXpReward(), OutputType.SUCCESS);
            emit("+  Credits: +" + activeMission.getCreditReward(), OutputType.SUCCESS);
            if (listener != null) listener.onMissionComplete();
        }
    }

    private void showStatus() {
        if (activeMission == null) {
            emit("No active mission. Select a mission from the panel.", OutputType.WARNING);
            return;
        }
        emit("── MISSION STATUS ──────────────────", OutputType.INFO);
        emit("Mission  : " + activeMission.getTitle(), OutputType.INFO);
        emit("Target   : " + activeMission.getTarget(), OutputType.INFO);
        for (String cmd : activeMission.getRequiredCommands()) {
            String s = executedCommands.contains(cmd) ? "[DONE]" : "[    ]";
            emit("  " + s + " " + cmd,
                 executedCommands.contains(cmd) ? OutputType.SUCCESS : OutputType.SYSTEM);
        }
        emit("Progress : " + executedCommands.size() + "/" +
             activeMission.getRequiredCommands().size(), OutputType.INFO);
    }

    private void emit(String line, OutputType type) {
        if (listener != null) listener.onOutput(line, type);
    }
}
