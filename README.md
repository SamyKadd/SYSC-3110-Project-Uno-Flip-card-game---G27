# UNO Card Game - Milestone 1

## Project Overview
A text-based implementation of UNO card game in Java supporting 2-4 players with all standard action cards.

---

## Deliverables

### Source Code
- `Card.java` - Card representation with color and value
- `Hand.java` - Manages player's hand of cards  
- `Player.java` - Player with name, score, and hand
- `Game.java` - Main game logic and controller
- `Main.java` - Entry point

### Test Files
- `CardTest.java` - 5 unit tests for Card class
- `HandTest.java` - 6 unit tests for Hand class
- `PlayerTest.java` - 6 unit tests for Player class
- `GameTest.java` - Unit tests for Game class

### Documentation
- `README.md` - This file
- JavaDoc comments in all source files
- UML Class Diagram (`diagrams/UML_diagram`)
- Sequence Diagrams (`diagrams/sequence_DIAGRAM`)
- Data Structures Explanation Document

---

## Team Contributions

### Milestone 1

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

**Seham Khalifa**
- Implemented startCards() method in Hand class (deal 7 cards)
- Added ArrayList import to Hand class
- Created startGame() method to initialize game and display starting hands
- Implemented getHand() method in Player class
- Added displayHand() functionality
- Initialized deck ArrayList in Game class
- Created 5 PlayerTest unit tests including toString() test

---

### How to Run

To run, build the Main class, and the game will begin.


### Run Tests:
Run test files through your IDE or JUnit test runner.

---

## How to Play

1. Enter card number (0, 1, 2...) to play that card
2. Enter 'D' to draw a card and pass your turn
3. For WILD cards, enter R/G/Y/B to choose color
4. Match color OR value of top card to play
5. First player with 0 cards wins

---

## Known Issues

These will all be resolved according to the instructions in the upcoming Milestones.

1. **Deck Exhaustion**: If deck runs empty, players cannot draw cards. Future versions will reshuffle discard pile.

2. **Single Round Only**: Game ends after one round. Multiple round scoring not yet implemented.

3. **No AI Players**: All players must be human.

4. **Initial Hand Display**: Starting hands are displayed to all players.

---

## Data Structures Used

- **ArrayList<Card> deck** - For drawing cards and shuffling
- **List<Player> players** - For turn order and indexed access
- **List<Card> cards (in Hand)** - For displaying numbered cards to players
- **List<Card> discardedPile** - Tracks all played cards

See data structures document for detailed explanation.

---

**Course**: SYSC 3110  
**Milestone**: 1  
**Date**: 2025-10-27
