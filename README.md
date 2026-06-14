                                                                HackLab: Hacker Simulator

Java OOP Project Analysis   
Student: Manahil Eman   
Subject: Object-Oriented Programming   
Development Environment: Eclipse IDE (Java 11+)   

Project Overview
A detailed structural analysis of architecture, design patterns, and the four foundational pillars of Object-Oriented Programming (OOP) within the HackLab simulation environment.  

Application Architecture
The project is decoupled into distinct structural layers to enforce a strict separation of concerns and maintain a loosely coupled codebase:  

Utilities: Centralizes theme tokens and global design constants.  

Model-View-Controller (MVC) Pattern
The application architecture strictly adheres to the MVC pattern:  

Model: Player.java and Mission.java safely hold state data with zero knowledge of the UI layer.  
View: Swing panels (HUD, Terminal, Briefing) dynamically render visual elements based on data provided by the controller.  
Controller: MainWindow orchestrates all real-time interactions between the Data (Model) and UI (View) layers.  

The Four Pillars of OOP
1. Encapsulation
Data integrity and class safety are handled heavily within Player.java:  + 1
Private Fields: Core variables such as xp, credits, and badges are strictly hidden from external classes.  
Controlled Mutation: State updates are guarded; for example, XP can only be modified through the addXP(int) method.  
Validation: Internal logic ensures that critical values (like XP) never drop below zero.  
Immutability: Mission objects are explicitly declared final upon instantiation to prevent accidental runtime modification.  

XP 
new
​
 =XP 
old
​
 +Reward
 
2. Inheritance
HackLab leverages Java Swing's class hierarchy to build custom, reusable interface components without rebuilding core window logic:  + 1
UI Extension: MainWindow extends JFrame to inherit robust window-management functionality.  
Custom Panels: TerminalPanel extends JPanel to directly override paintComponent() for bespoke rendering.  
Anonymous Classes: Employed locally to implement unique graphics operations like card layouts and custom glow borders.  

3. Polymorphism
Dynamic runtime behavior is achieved through polymorphic design patterns:  + 2
Enums with Behavior: Ranks and difficulty scales adapt their internal execution pathways based on the provided state input.  
Output Color Typing: The terminal system dynamically maps outputs to colors based on message status:  

Java
switch(type) {
    case SUCCESS: return GREEN;
    case ERROR: return RED;
    // ...
}

Runtime Rank Evaluation: The Rank enum evaluates a player's standing on the fly using their total earned XP.  
+ 1

4. Abstraction
Decoupling implementation logic from high-level definitions focuses strictly on what the system does rather than how it does it:  
TerminalOutputListener: An abstraction interface that lets the core engine report back events without having any direct dependencies on the Swing GUI.  
+ 1
Theme Abstraction: Design parameters and visual tokens are entirely centralized, enabling instant, global styling updates across the app.  
Data Models: Missions abstractly define high-level objectives rather than hardcoding step-by-step execution sequences.  

Game Design Data
Rank XP Requirements
The system maps total player XP to the following milestone tiers:  
+ 1

Rank Title	Required XP
Legend	
15,000+ XP  

Elite Hacker	
7,000+ XP  

Novice Hacker	
500+ XP  

Script Kiddie	
0 XP  

OOP Implementation Matrix Summary
OOP Pillar	Implementation Area	Primary Benefit
Encapsulation	Player.java / Mission.java	
Data Integrity & Safety  

Inheritance	Swing Panels & Components	
UI Component Reuse  

Polymorphism	Output Type & Rank Enums	
Dynamic Runtime Behavior  

Abstraction	TerminalOutputListener	
Layer Decoupling  

Feature Roadmap
The planned engineering milestones for HackLab include:

Persistence: Add a dedicated Save/Load ecosystem utilizing JSON/Gson libraries to track and store player progress natively.
Categories: Introduce specialized puzzle paths focusing specifically on Cryptography challenges and Database breaches.
Shop System: Build an in-game economy overlay allowing players to spend accumulated credits on elite virtual hacking tools.
Multiplayer: Implement real-time, competitive hacking matches backed by Java Net Sockets.

System Status: ANALYSIS COMPLETE
Secure Exit: TRUE
