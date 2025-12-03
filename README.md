# UNO Card Game - Milestone 4


## Project Overview
A graphical implementation of the UNO Flip card game in Java using Swing GUI and MVC architecture, supporting 2-4 players (human or AI) with all standard and Flip action cards. Features include save/load functionality, undo/redo system, and multi-round/multi-game replay.

---

## Deliverables

### Source Code - Model
- `Card.java` - Card representation with color and value
- `Hand.java` - Manages player's hand of cards  
- `Player.java` - Player with name, score, and hand
- `AIPlayer.java` - AI player extending Player with automated decision-making
- `Game.java` - Game logic and state management (Model)
- `GameStateEvent.java` - Encapsulates game state for MVC communication
- `Side.java` - Enum for tracking LIGHT/DARK game 
- `GameMemento.java` - Memento pattern for undo/redo functionality (Serializable)
- `SerializationUtils.java` - Utility for deep copying serializable objects

### Source Code - View
- `GameView.java` - Graphical user interface using Java Swing
- `GameViewInterface.java` - Interface for View components
- `PlayerSelectionDialog.java` - Dialog for selecting number of players
- `AIPlayerSelectionDialog.java` - Dialog for selecting number of AI players

### Source Code - Controller
- `GameController.java` - Controller managing Model-View interaction
- `GameUIListener.java` - Interface for UI event handling
- `Main.java` - Entry point and application initialization

### Test Files
- `CardTest.java` - 6 unit tests for Card class
- `HandTest.java` - 5 unit tests for Hand class
- `PlayerTest.java` - 6 unit tests for Player class
- `GameTest.java` - 18 unit tests for Game class
- `AIPlayerIntegrationTest.java` - 12 unit tests for AIPlayer logic
- `GameViewManualTest.java` - Manual GUI testing (not JUnit)

### Documentation
- `README.md` - This file
- `Data_Structures.md` - Data structure changes from Milestone 1 to 2 to 3 to 4
- `USER_MANUAL.md` - Comprehensive user guide for all features
- JavaDoc comments in all source files
- UML Class Diagram (`diagrams/UML_diagram`)
- Sequence Diagrams (`diagrams/sequence_DIAGRAM`)

---

## New Features in Milestone 4

### 1. Replay Functionality

**Multi-Round Play**
- Game continues after a player wins a round
- Scores are preserved between rounds
- Round counter increments (displayed in status)
- New hands are dealt for each round
- First player to 500 points wins the game

**Multi-Game Play**
- After reaching 500 points, players can start a new game
- All scores reset to 0
- Round counter resets to 1
- Same players continue with fresh start

**GUI Implementation**
- `NEW ROUND` button appears when a round ends 
- `NEW GAME` button appears when game ends at 500 points 
- Visual feedback with popup dialogs

---

### 2. Undo/Redo Functionality (30 marks)

**Undo System**
- Click `UNDO` button to reverse your last action
- Supports undoing card plays, draws, and turn advances
- Multi-level undo (can undo multiple actions in sequence)
- Undo button automatically enables/disables based on availability

**Redo System**
- Click `REDO` button after undoing to restore action
- Multi-level redo support
- Redo stack clears when new action is taken
- Redo button automatically enables/disables

**Implementation Details**
- Uses Memento design pattern (`GameMemento.java`)
- Deep copying via `SerializationUtils.java`
- Undo/redo stacks maintain complete game state snapshots
- Preserves player hands, deck state, scores, and turn order

---

### 3. Serialization/Deserialization (30 marks)

**Save Game**
- Click `File → Save Game` from menu bar
- Choose location and filename
- Saves with `.uno` extension
- Preserves complete game state

**Load Game**
- Click `File → Load Game` from menu bar
- Select `.uno` file
- Game resumes exactly where it was saved
- Confirmation dialog prevents accidental overwrites

**What Gets Saved:**
- All player information (names, scores, hands)
- Current game state (side, round number, top card)
- Deck and discard pile contents
- Turn order and current player
- Wild card color selections
- Pending skips and special states

**Implementation Details**
- All model classes implement `Serializable`
- `transient` keyword for non-serializable fields
- Custom `readObject()` re-initializes transient fields
- Static `loadGame()` returns new Game instance
- Controller's `reconnectModel()` switches to loaded game
- Error handling for corrupted/missing files

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
- Connected model and view with PropertyChangeListener for automatic GUI updates
- Improved turn logic to prevent multiple plays per turn and reset properly after each turn.
- Fixed and refined action card behavior (Skip, Reverse, Draw One, Wild Draw Two).
- Completed UML class diagrams
- Implemented full light/dark side system with side tracking and colors.
- Implemented FLIP card behavior with deck switching and top-card flipping.
- Implemented WILD_DRAW_COLOR logic, including dark-color selection flow.
- Added light/dark decks with separate discard piles.
- Updated play validation and reshuffle logic for light/dark side compatibility.
- Completed UML class diagram
  

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
- Created and implemented GameView file and began GUI layout (panels, labels, component initialization)
- Implemented bottom panel, hand display, top-card rendering, card button creation, card color formatting, and card string formatting
- Implemented view update methods for current player, selected color, and scoreboard
- Implemented getters for card buttons, draw card button, and next-player indicator
- Created GameViewTest for View inspection
- Updated DataStructures file with all Milestone 2 changes (MVC structures and new fields)
- Fixed game logic bugs (draw-one/draw-two not adding cards, Wild Draw Two color prompt issue, other minor errors)
- Implemented top-card color update after wild draw two card color selection
- Updated README.md with updated milestone information
- Added basic test cases to confirm game logic after MVC integration
- Added AI player selection dialog and integrated AI player count into game setup
- Modified Main to enforce maximum of 4 total players including AI
- Created AIPlayer class and initial AI behaviour structure
- Implemented SKIP_EVERYONE action card logic and DRAW_FIVE card logic and integrated into model action-card handling
- Created test cases for the AIPlayer class
- Fixed issue where human players could play more than one card per turn

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
- Created Helper class to assign the new uno flip points
- Altered the calculateAndAwardScore to implement the helper class 
- Implemented tests
- Created Sequence diagram for flipcard
- Created Sequence diagram for ai turn

**Seham Khalifa**
- Implemented startCards() method in Hand class (deal 7 cards)
- Added ArrayList import to Hand class
- Created startGame() method to initialize game and display starting hands
- Implemented getHand() method in Player class
- Added displayHand() functionality
- Initialized deck ArrayList in Game class
- Created 5 PlayerTest unit tests including toString() test
- Created a method in Hand to access all the player's cards directly
- Added a proper View interface and updated controller to use it
- Model now supports multiple views with add/remove view methods
- Rebuilt GameStateEvent: correct naming, extends EventObject, private fields, full getters/setters
- Fixed controller–model separation by moving validation logic back into the model
- Completed GameController methods
- Updated calculateAndAwardScore() to correctly compute and assign the winners points
- Updated GameView’s render() method to use the new getters and correctly display all state changes
- Updated GameController’s onPlayCard() and onDraw() methods to rely on the model’s validity checks instead of making decisions in the controller

---

### How to Run

To run, build the Main class, and the game will begin.


### Run Tests:
Run test files through your IDE or JUnit test runner.

---

## How to Play


### Starting the Game
1. Select number of human players (2-4) from the first dialog
2. Select number of AI players (0-3) from the second dialog (if space allows)
3. Total players must be between 2-4
4. Click OK to start the game
5. Each player is dealt 7 cards automatically

### During Your Turn

**Human Players:**
1. **Play a Card**: Click on a card in your hand to play it
   - Card must match color OR value of top card
   - Wild cards can always be played
2. **Draw a Card**: Click "DRAW CARD" button to draw one card and end your turn
3. **Next Player**: Click "NEXT PLAYER" after playing/drawing to advance turn

**AI Players:**
- AI players take their turn automatically
- AI will play a legal card or draw if none available
- Click "NEXT PLAYER" after AI is done playing/drawing to advance turn

### Special Cards
- **SKIP**: Next player loses their turn (automatically handled)
- **REVERSE**: Turn order reverses (in 2-player acts like Skip)
- **DRAW ONE**: Next player draws 1 card and loses their turn
- **WILD**: Choose a color from dialog, then click Next Player
- **WILD DRAW TWO**: Next player draws 2 cards and loses their turn, then choose a color

### Dark Side Action Cards
- **DRAW FIVE**: Next player draws 5 cards and loses their turn
- **SKIP EVERYONE**: All other players are skipped, current player plays again
- **WILD DRAW COLOUR**: Choose a dark color (Teal, Purple, Pink, Orange), next player draws until they get that color and loses their turn

### Features
- **Save**: File → Save Game
- **Load**: File → Load Game
- **Undo**: Click UNDO
- **Redo**: Click REDO
- **New Round**: Auto-appears when round ends
- **New Game**: Auto-appears at 500 points

---

## Known Issues

1. **No UNO Call**: Players are not required to call "UNO" when down to one card.

2. **Card Hand Visibility**: All players see the current player's hand (designed for same-device play).

3. **AI Difficulty**: AI uses a simple strategy. No advanced or variable difficulty levels.

4. **Limited AI Strategy**: AI doesn't track other players' card counts or optimize for specific winning strategies.

---

**Course**: SYSC 3110  
**Milestone**: 4  
**Date**: 2025-12-05
