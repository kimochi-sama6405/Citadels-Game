# Citadels-Game

**Overview**

This project is a command-line implementation of the board/card game Citadels, developed in Java with a focus on object-oriented design, game logic correctness, and extensibility. The game supports full Character Selection and Turn phases, computer-controlled players, character special abilities, scoring, and persistent game state through JSON-based save and load functionality. The project was designed iteratively, beginning with pseudocode and class planning, followed by implementation and refactoring to improve readability, maintainability, and reuse.

**Game Features**

- Full implementation of Citadels core gameplay

*Character Selection Phase*:

- Crown rotation

- Drafting logic

- Hidden information handling

*Turn Phase*:

- Sequential character turns

- Character-specific abilities (e.g. Assassin, Thief, Architect)

- Gold collection, card drawing, and district building

*Computer Player AI*:

- Automated decision-making for non-human players

*Scoring System*:

- District values

- Bonus conditions

*Save & Load*:

- Game state persistence using JSON

*Text-based User Interface*:

- Clear prompts and commands

- Deterministic turn flow for ease of testing

**Running the Game**

*Using Gradle*

If the project is configured with Gradle:

./gradlew run

*Using an IDE*

Run the main method in:

App.java

The game runs entirely in the terminal and will guide the player through character selection, turn execution, and available commands.

**Project Structure**
- App.java                    # Entry point and main game loop
- Player.java                 # Player state, hand, gold, and city
- CharacterCards.java         # Character definitions and abilities
- CharacterSelectionPhase.java# Character drafting logic
- TurnPhase.java              # Turn execution and ability resolution
- DistrictCard.java           # District definitions and properties

**Design & Architecture**

*Object-Oriented Design*:

- Clear separation of responsibilities between game phases, players, and cards

*Phase-Based Architecture*:

- Character selection and turn execution handled independently

*Extensible Logic*:

- New characters or rules can be added with minimal changes

*Testability*:

- Deterministic turn flow and modular classes support unit testing

*Persistence*:

- Game state serialized to and from JSON for saving/loading sessions

**Technologies Used**

- Java

- Gradle

- JUnit (testing & coverage)

- JSON (game persistence)

- Git

- Visual Studio Code

**Example Gameplay Flow**

1. Game initializes players and decks

2. Character Selection Phase

3. Players draft characters in turn order

4. Turn Phase

5. Characters act in rank order

6. Abilities are executed

7. Players collect gold/cards and build districts

8. Scoring is calculated when end conditions are met

9.Game can be saved or loaded at any time

**Future Improvements**

- Enhanced AI decision-making

- Difficulty levels for computer players

- Expanded command set and in-game help

- Optional graphical or web-based interface

- Additional Citadels expansions or variants
