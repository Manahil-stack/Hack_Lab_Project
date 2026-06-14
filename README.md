HackLab: Hacker Simulator

Java OOP Project Analysis   
PDF


Student: Manahil Eman   
PDF


Subject: Object-Oriented Programming   
PDF


Development Environment: Eclipse IDE (Java 11+)   
PDF

Project Overview
A detailed structural analysis of architecture, design patterns, and the four foundational pillars of Object-Oriented Programming (OOP) within the HackLab simulation environment.  
PDF

Application Architecture
The project is decoupled into distinct structural layers to enforce a strict separation of concerns and maintain a loosely coupled codebase:  
PDF


Model (Data): Manages the core state logic for players and missions.  
PDF


UI / View: Built using Java Swing components and layout panels.  
PDF


Engine: Handles core terminal command processing mechanics.  
PDF


Utilities: Centralizes theme tokens and global design constants.  
PDF

Model-View-Controller (MVC) Pattern
The application architecture strictly adheres to the MVC pattern:  
PDF


Model: Player.java and Mission.java safely hold state data with zero knowledge of the UI layer.  
PDF


View: Swing panels (HUD, Terminal, Briefing) dynamically render visual elements based on data provided by the controller.  
PDF


Controller: MainWindow orchestrates all real-time interactions between the Data (Model) and UI (View) layers.  
PDF

The Four Pillars of OOP
1. Encapsulation
Data integrity and class safety are handled heavily within Player.java:  
PDF
+ 1


Private Fields: Core variables such as xp, credits, and badges are strictly hidden from external classes.  
PDF


Controlled Mutation: State updates are guarded; for example, XP can only be modified through the addXP(int) method.  
PDF


Validation: Internal logic ensures that critical values (like XP) never drop below zero.  
PDF


Immutability: Mission objects are explicitly declared final upon instantiation to prevent accidental runtime modification.  
PDF

XP 
new
​
 =XP 
old
​
 +Reward
2. Inheritance
HackLab leverages Java Swing's class hierarchy to build custom, reusable interface components without rebuilding core window logic:  
PDF
+ 1


UI Extension: MainWindow extends JFrame to inherit robust window-management functionality.  
PDF


Custom Panels: TerminalPanel extends JPanel to directly override paintComponent() for bespoke rendering.  
PDF


Anonymous Classes: Employed locally to implement unique graphics operations like card layouts and custom glow borders.  
PDF

3. Polymorphism
Dynamic runtime behavior is achieved through polymorphic design patterns:  
PDF
+ 2


Enums with Behavior: Ranks and difficulty scales adapt their internal execution pathways based on the provided state input.  
PDF


Output Color Typing: The terminal system dynamically maps outputs to colors based on message status:  
PDF

Java
switch(type) {
    case SUCCESS: return GREEN;
    case ERROR: return RED;
    // ...
}

Runtime Rank Evaluation: The Rank enum evaluates a player's standing on the fly using their total earned XP.  
PDF
+ 1

4. Abstraction
Decoupling implementation logic from high-level definitions focuses strictly on what the system does rather than how it does it:  
PDF


TerminalOutputListener: An abstraction interface that lets the core engine report back events without having any direct dependencies on the Swing GUI.  
PDF
+ 1


Theme Abstraction: Design parameters and visual tokens are entirely centralized, enabling instant, global styling updates across the app.  
PDF


Data Models: Missions abstractly define high-level objectives rather than hardcoding step-by-step execution sequences.  
PDF

Game Design Data
Rank XP Requirements
The system maps total player XP to the following milestone tiers:  
PDF
+ 1

Rank Title	Required XP
Legend	
15,000+ XP  
PDF

Elite Hacker	
7,000+ XP  
PDF

Novice Hacker	
500+ XP  
PDF

Script Kiddie	
0 XP  
PDF

OOP Implementation Matrix Summary
OOP Pillar	Implementation Area	Primary Benefit
Encapsulation	Player.java / Mission.java	
Data Integrity & Safety  
PDF

Inheritance	Swing Panels & Components	
UI Component Reuse  
PDF

Polymorphism	Output Type & Rank Enums	
Dynamic Runtime Behavior  
PDF

Abstraction	TerminalOutputListener	
Layer Decoupling  
PDF

Feature Roadmap
The planned engineering milestones for HackLab include:

Persistence: Add a dedicated Save/Load ecosystem utilizing JSON/Gson libraries to track and store player progress natively.

Categories: Introduce specialized puzzle paths focusing specifically on Cryptography challenges and Database breaches.

Shop System: Build an in-game economy overlay allowing players to spend accumulated credits on elite virtual hacking tools.

Multiplayer: Implement real-time, competitive hacking matches backed by Java Net Sockets.

System Status: ANALYSIS COMPLETE
Secure Exit: TRUE
