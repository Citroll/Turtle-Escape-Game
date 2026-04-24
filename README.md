# Turtle Escape Game

A 2D top-down adventure game where players control a turtle navigating through polluted ocean environments while avoiding enemies and collecting items. Built as a group project for SFU's CMPT 276 Software Engineering course.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Maven](https://img.shields.io/badge/Build-Maven-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

## 📖 Overview

**Turtle Escape** is a fully-featured 2D game that combines engaging gameplay with an educational message about ocean pollution. Players guide a turtle protagonist through various tile-based maps, avoiding hazardous enemies and collecting essential items to survive.

### Key Features

- **Engaging Gameplay**: Top-down 2D adventure with smooth player movement and collision detection
- **Intelligent Enemies**: AI-powered enemies with pathfinding algorithms (A* pathfinding)
- **Interactive Objects**: Collectibles and obstacles that affect gameplay (jellyfish, plastic bags, ice cream)
- **Multiple Maps**: Tile-based level design with diverse environments
- **Audio System**: Background music and sound effects for immersive experience
- **User Interface**: Main menu, settings panel, and in-game UI elements
- **Game Controls**: Keyboard input handling for intuitive player control

## 🎮 Gameplay

### Objective
Guide your turtle through polluted ocean maps while avoiding enemies and collecting beneficial items to progress through levels.

### Controls
- **Arrow Keys / WASD**: Move the turtle up, down, left, right
- **ESC**: Access game menu / pause
- **Settings**: Adjust audio and game preferences from the menu

### Game Elements
- **Player**: The turtle you control
- **Enemies**: Various sea creatures that patrol and hunt the player (includes SuperEnemy with advanced AI)
- **Objects**: Collectibles and obstacles scattered throughout the map
  - 🪡 Jellyfish: Hazard to avoid
  - 🛍️ Plastic Bag: Collectible (environmental awareness)
  - 🍦 Ice Cream: Health/points collectible

## 🛠️ Technical Architecture

### Technology Stack
- **Language**: Java 17
- **Build Tool**: Maven
- **GUI Framework**: Java Swing
- **Testing**: JUnit 5
- **Code Coverage**: JaCoCo

### Project Structure
```
turtle-escape/
├── src/main/
│   ├── java/ca/sfu/cmpt276/turtleescape/
│   │   ├── Main.java                 # Application entry point
│   │   ├── UI/                       # User interface components
│   │   │   ├── GamePanel.java        # Main game loop and rendering
│   │   │   ├── UI.java               # HUD and menus
│   │   │   └── Button.java           # UI button elements
│   │   ├── entity/                   # Game entities
│   │   │   ├── Player.java           # Turtle player character
│   │   │   └── Entity.java           # Base entity class
│   │   ├── enemy/                    # Enemy AI and behavior
│   │   │   └── SuperEnemy.java       # Advanced enemy with pathfinding
│   │   ├── ai/                       # Pathfinding algorithms
│   │   │   ├── Pathfinder.java       # A* pathfinding implementation
│   │   │   └── Node.java             # Graph nodes for pathfinding
│   │   ├── tile/                     # Tile-based map system
│   │   │   ├── TileManager.java      # Tile loading and management
│   │   │   └── Tile.java             # Individual tile properties
│   │   ├── object/                   # Collectible and interactive objects
│   │   │   ├── SuperObject.java      # Base object class
│   │   │   ├── OBJ_Jellyfish.java    # Jellyfish obstacle
│   │   │   └── OBJ_IceCream.java     # Health/collectible item
│   │   ├── collision/                # Collision detection system
│   │   │   └── CollisionChecker.java # Collision logic
│   │   ├── input/                    # Keyboard input handling
│   │   │   └── KeyHandler.java       # Key event processing
│   │   ├── audio/                    # Sound system
│   │   │   ├── AudioManager.java     # Audio playback control
│   │   │   └── Sound.java            # Sound effect objects
│   │   └── UtilityTool.java          # Helper utilities
│   └── resources/                    # Game assets
│       ├── tiles/                    # Tileset graphics
│       ├── player/                   # Player character sprites
│       ├── enemies/                  # Enemy sprites
│       ├── objects/                  # Object/collectible sprites
│       ├── maps/                     # Level data files
│       └── sound/                    # Audio files
└── pom.xml                           # Maven configuration
```

### Design Patterns
- **Model-View-Controller**: Separation of game logic, rendering, and input
- **Observer Pattern**: Event-driven input handling
- **Factory Pattern**: Object and entity creation
- **Strategy Pattern**: Different AI behaviors for enemy types

## 📋 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Citroll/Turtle-Escape-Game.git
   cd Turtle-Escape-Game/turtle-escape
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the game:**
   ```bash
   mvn exec:java
   ```
   Or compile and run the JAR:
   ```bash
   mvn package
   java -jar target/turtle-escape-1.0-SNAPSHOT.jar
   ```

## 🧪 Testing

Run the test suite with JUnit:
```bash
mvn test
```

Generate code coverage report with JaCoCo:
```bash
mvn test jacoco:report
```
Coverage reports are generated in `target/site/jacoco/index.html`

## 📚 Documentation

Comprehensive project documentation is available in the `/Design` and `/Documents` directories:
- **Design Documents**: System architecture, UML diagrams, use cases
- **Phase Reports**: Detailed reports from each development phase
- **Code Reviews**: Peer review documentation and feedback

## 👥 Team

This project was developed as a group effort for CMPT 276 (Software Engineering) at Simon Fraser University:
- **Group 11** - Development team collaborating on design, implementation, and testing

## 🎯 Learning Outcomes

This project demonstrates proficiency in:
- **Software Engineering Principles**: Requirements gathering, design, implementation, and testing
- **Object-Oriented Programming**: Class hierarchies, encapsulation, and design patterns
- **Game Development**: Game loops, collision detection, sprite rendering, and event handling
- **Algorithm Implementation**: A* pathfinding for intelligent NPC behavior
- **Build Automation**: Maven configuration and dependency management
- **Team Collaboration**: Group project coordination and code reviews
- **UI/UX Design**: Creating intuitive game interfaces with Java Swing

## 📝 License

This project is provided as-is for educational purposes.

## 🤝 Contributing

This was a course project completed for CMPT 276. The repository is maintained as a portfolio piece. For inquiries or suggestions, please contact the project authors.

---

**Note**: This is an educational project created to demonstrate software engineering principles and game development concepts learned in CMPT 276 at Simon Fraser University.
