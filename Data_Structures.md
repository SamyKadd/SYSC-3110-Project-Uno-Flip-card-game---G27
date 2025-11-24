# Data Structures Explanation - UNO Card Game
## Changes from Milestone 1 to Milestone 2

This document explains the data structure choices made in the UNO card game implementation and how they evolved from Milestone 1 to Milestone 2 to support the MVC architecture and GUI implementation.

---

## Summary of Changes

### New Data Structures Added in Milestone 2:
1. **PropertyChangeSupport pcs** (in Game class)
2. **int pendingSkips** (in Game class)
3. **GameStateEvent class** (new class)
4. **ArrayList<JButton> cardButtons** (in GameView class)
5. **GameUIListener interface** (new interface)

### Modified Structures:
1. **Card.Color topWild** - Changed from using top card color to separate tracking
2. **Scanner input** - Removed in favor of GUI event handling

---

## Milestone 1 Data Structures (Retained)

### ArrayList<Card> deck (in Game class)

**Why chosen:**
- Need to remove cards from the top when dealing and drawing
- Need to shuffle the entire deck at game start
- Need to add cards back to bottom when avoiding action card start
- Dynamic sizing as cards are drawn throughout the game

**Operations used:**
- `add()` - Adding cards during initialization and putting action cards at bottom
- `remove(0)` - Drawing cards from top of deck
- `Collections.shuffle()` - Randomizing deck at start
- `isEmpty()` - Checking if deck is exhausted

**Milestone 2 Changes:**
- Added reshuffleFromDiscard() logic to replenish deck from discard pile
- Maintains same core functionality but with automatic reshuffling

---

### List<Player> players (in Game class)

**Why chosen:**
- Player order matters for turn rotation
- Need indexed access to get current player using `currentPlayerIndex`
- Size is fixed during gameplay (2-4 players)

**Operations used:**
- `add()` - Adding players before game starts
- `get(index)` - Getting current player by index
- `size()` - Validating player count and calculating next turn
- `remove()` - Removing players

**Milestone 2 Changes:**
- No structural changes, but now accessed via exportState() for MVC pattern
- Used in score calculations and winner determination

---

### List<Card> cards (in Hand class)

**Why chosen:**
- Cards must maintain display order for player selection (0, 1, 2, 3...)
- Need indexed access when player selects card by number
- Dynamic size as cards are drawn and played

**Operations used:**
- `add()` - Drawing cards
- `get(index)` - Viewing card at index
- `remove(index)` - Playing a card
- `size()` - Checking hand size and win condition
- `getCardsList()` - Returns list for GameStateEvent population

**Milestone 2 Changes:**
- Added getCardsList() method to support MVC state transfer
- Same structure but accessed differently through Controller

---

## List<Card> discardedPile (in Game class)

**Why chosen:**
- Tracks all played cards in order
- Only needs append operations
- May be used for future features like reshuffling

**Operations used:**
- `add()` - Adding played cards to discard pile
- `size()` - Tracking number of cards played
- `remove()` - Removing cards for reshuffling
- `clear()` - Clearing pile after reshuffling

**Milestone 2 Changes:**
- Now actively used in reshuffleFromDiscard() method
- Critical for continuous gameplay without deck exhaustion

---

## New Data Structures in Milestone 2

### PropertyChangeSupport pcs (in Game class)

**Why chosen:**
- Core component of Java Event Model implementation
- Enables Model to notify Controller/View of state changes
- Maintains proper MVC separation 
- Supports multiple listeners if needed in future

**Operations used:**
- `addPropertyChangeListener()` - Registering Controller as observer
- `removePropertyChangeListener()` - Cleanup (optional)
- `firePropertyChange()` - Broadcasting state changes to listeners

**Why this design:**
- Decouples Model from View - Game doesn't know about GUI
- Follows Observer pattern for reactive updates
- Standard Java approach for MVC event handling
- Allows Model to focus purely on game logic

---

### GameStateEvent class (new class)

**Why chosen:**
- Encapsulates all information View needs to render
- Prevents View from directly accessing Model internals
- Makes state transfer atomic and immutable
- Simplifies Controller logic for updating 

**Why this design:**
- Single source of truth for GUI rendering
- View only needs GameStateEvent, not entire Game object
- Easy to serialize for future save/load features
- Clear contract between Model and View
- Supports stateless View rendering


### ArrayList<JButton> cardButtons (in GameView class)

**Why chosen:**
- Need to track all card buttons for enabling/disabling
- Must maintain 1-to-1 correspondence with hand indices
- Allow batch operations on all card buttons
- Support dynamic recreation as hand changes

**Operations used:**
- `clear()` - Removing old buttons when hand updates
- `add()` - Creating buttons for new hand state
- `get(index)` - Accessing specific card button
- Iteration for batch enabling/disabling

**Why this design:**
- Direct mapping between hand index and button
- Simplifies event handling with ActionCommand
- Allows View to manage its own components
- Supports dynamic hand sizes throughout game

---

### GameUIListener interface (new interface)

**Why chosen:**
- Defines contract between View and Controller
- Enables loose coupling - View doesn't need Controller reference
- Supports interface-based programming for flexibility
- Makes testing easier with mock implementations

**Why this design:**
- Clear separation of UI events from business logic
- Controller implements interface to handle events
- View only knows about interface, not concrete Controller
- Easy to swap Controller implementations for testing

---

## Removed Data Structures

### Scanner input (removed from Game class)

**Why removed:**
- Text-based I/O replaced by GUI components
- User input now comes from button clicks, not console
- Controller handles input validation instead of Model
- No longer needed with event-driven architecture

**Replaced by:**
- JButton click events in GameView
- GameUIListener interface for event handling
- GameController for input validation

---

## Conclusion

The transition from Milestone 1 to Milestone 2 required significant data structure additions and modifications to support:
- MVC architecture pattern
- GUI event-driven programming
- Proper separation of concerns
- Java Event Model implementation

Key structural changes:
- Added PropertyChangeSupport for observer pattern
- Created GameStateEvent for controlled data transfer
- Added pendingSkips for GUI-friendly game flow
- Removed Scanner in favor of event handling

All changes maintain or improve the efficiency of the original design while enabling a clean, maintainable MVC implementation.

---

**Course**: SYSC 3110  
**Milestone**: 2