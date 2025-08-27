# Software Engineering Game Project

## Introduction
This project was developed as part of the *Software Engineering 1* course at the University of Vienna.  
It implements a **client-server based strategy game** where two autonomous clients (AI-controlled) compete against each other on a dynamically generated map. Each client must explore the map, find its own treasure, and then capture the opponent’s fortress.

The server coordinates the entire process: generating and combining maps, handling client registration, validating movements, and determining win/loss conditions. Communication between clients and the server is handled via a **REST-based protocol** using XML messages.

### Key Features
- Random generation of map halves by each client.
- Server-controlled combination of maps and treasure placement.
- Turn-based movement with terrain rules (grass, mountains, water).
- Strict time and round limits (max. 320 turns / 10 minutes).
- Automatic detection of win and loss conditions.
- CLI-based visualization of map exploration and game progress.

---

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/reckmin/Software-Engineering.git
   cd Software-Engineering
2. Make sure you have Java 17 or higher installed:
   ```bash
   java -version
3. Build the project
   ```bash
   gradle build
---

### Usage
1. Start the server (It is provided by my uni, not accesseble now)
2. Start a new game via browser or endpoint:
   ```bash
   http://<domain>:<port>/games
3. Run the client with the provided GameID:
   ```bash
    java -jar client/target/client.jar <GameID>

### Game Rules Summary:
- Each client generates a half-map which the server combines.
- Clients move step by step, revealing terrain and searching for treasures.
- After collecting their treasure, clients must capture the opponent’s fortress.
- A game is limited to 320 turns and 10 minutes maximum.

Watch the CLI visualization as the clients explore, collect treasures, and attempt to capture the enemy fortress.

