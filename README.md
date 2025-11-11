# UNO Card Game - Milestone 2

## Project Overview
A graphical implementation of UNO card game in Java using Swing GUI and MVC architecture, supporting 2-4 players with all standard action cards.

---

## Deliverables

### Source Code - Model
- `Card.java` - Card representation with color and value
- `Hand.java` - Manages player's hand of cards  
- `Player.java` - Player with name, score, and hand
- `Game.java` - Game logic and state management (Model)
- `GameState.java` - Encapsulates game state for MVC communication

### Source Code - View
- `GameView.java` - Graphical user interface using Java Swing
- `PlayerSelectionDialog.java` - Dialog for selecting number of players

### Source Code - Controller
- `GameController.java` - Controller managing Model-View interaction
- `GameUIListener.java` - Interface for UI event handling
- `Main.java` - Entry point and application initialization

### Test Files
- `CardTest.java` - 8 unit tests for Card class
- `HandTest.java` - 9 unit tests for Hand class
- `PlayerTest.java` - 6 unit tests for Player class
- `GameTest.java` - 13 unit tests for Game class
- `GameViewManualTest.java` - Manual GUI testing (not JUnit)

### Documentation
- `README.md` - This file
- `Data_Structures.md` - Data structure changes from Milestone 1 to 2
- JavaDoc comments in all source files
- UML Class Diagram (`diagrams/UML_diagram`)
- Sequence Diagrams (`diagrams/sequence_DIAGRAM`)

---

## Team Contributions

**Joodi Al-Asaad**
- Refactored Card class to use enums (Color and Value)
- Implemented action card detection logic (checkIfActionCard)
- Added Player attributes (name, score, hand) with getters/setters
- Implemented Skip card logic and handleActionCard() method
- Added currentPlayerIndex tracking and turn flow logic
- Implemented isValidPlay() and card play validation logic
- Implemented Scanner for player input and card selection
- Added validation for invalid card indexes and safe user retry
- Added toString() to Player and uncommented addScore()
- Finalized Card, Hand, and Game classes for M1
- Created all test files (CardTest, HandTest, PlayerTest, GameTest)
- Implemented 6 HandTest unit tests
- Added card play validation and turn advancement logic
- Completed UML class diagrams 

**Mahdi Bouakline**
- Implemented initial Card.java and Hand.java skeleton code
- Implemented deck initialization with 108 cards and shuffling
- Implemented complete game logic and game loop in Game.java
- Implemented isValidPlay() method for card validation
- Added JavaDoc documentation to all classes
- Fixed wild card toString() to handle null color properly
- Implemented do-while loop to prevent starting with action cards
- Created initial README.md and project documentation
- Created the data structures file
- Implemented three test cases in GameTest

**Samy Kaddour**
- Implemented WILD card functionality with color selection
- Implemented WILD_DRAW_TWO card logic
- Implemented DRAW_ONE card functionality
- Implemented REVERSE card logic
- Added helper functions for action card processing
- Completed CardTest with 5 comprehensive unit tests
- Completed GameTest with 11 comprehensive unit tests
- Completed Sequence Diagram UML
- Added rendering to GameView
- Completed 3 Sequence Diagrams to simulate main game scenarios 
- Created and completed GameController
- Created GameUIListener interface, along with the methods(w/o logic implementation)

**Seham Khalifa**
- Implemented startCards() method in Hand class (deal 7 cards)
- Added ArrayList import to Hand class
- Created startGame() method to initialize game and display starting hands
- Implemented getHand() method in Player class
- Added displayHand() functionality
- Initialized deck ArrayList in Game class
- Created 5 PlayerTest unit tests including toString() test
- Created a method in Hand to access all the player's cards directly
- Completed GameController methods

---

### How to Run

To run, build the Main class, and the game will begin.


### Run Tests:
Run test files through your IDE or JUnit test runner.

---

## How to Play


### Starting the Game
1. Select number of players (2-4) from the dialog
2. Click OK to start the game
3. Each player is dealt 7 cards automatically

### During Your Turn
1. **Play a Card**: Click on a card in your hand to play it
   - Card must match color OR value of top card
   - Wild cards can always be played
2. **Draw a Card**: Click "DRAW CARD" button to draw one card and end your turn
3. **Next Player**: Click "NEXT PLAYER" after playing/drawing to advance turn

### Special Cards
- **SKIP**: Next player loses their turn (automatically handled)
- **REVERSE**: Turn order reverses (in 2-player acts like Skip)
- **DRAW ONE**: Next player draws 1 card and loses their turn
- **WILD**: Choose a color from dialog, then click Next Player
- **WILD DRAW TWO**: Next player draws 2 cards and loses their turn, then choose a color

---

### Model (Game.java)
- Manages game state, deck, players, and turn logic
- Validates card plays and handles action cards
- Fires PropertyChangeEvents when state changes
- No knowledge of View or Controller

### View (GameView.java)
- Displays game state using Swing components
- Renders cards, top card, player info, status messages
- Captures user input (button clicks, card selection)
- No direct access to Model

### Controller (GameController.java)
- Listens for View events via GameUIListener interface
- Validates and forwards actions to Model
- Observes Model changes via PropertyChangeListener
- Updates View when Model state changes
- Manages turn locking to prevent multiple actions per turn

---

## Known Issues

1. **No Multi-Round Support**: Game ends after one round. Winner is declared but game cannot continue to next round.

2. **No AI Players**: All players must be human.

3. **No UNO Call**: Players are not required to call "UNO" when down to one card.

4. **Card Hand Visibility**: All players see the current player's hand (designed for same-device play).

5. **No Save/Load**: Game state cannot be saved and resumed.

---

**Course**: SYSC 3110  
**Milestone**: 1  
**Date**: 2025-11-10
