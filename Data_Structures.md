# Data Structures Explanation - UNO Card Game
## Changes from Milestone 1 to Milestone 2 to Milestone 3

This document explains the data structure choices made in the UNO Flip card game implementation and how they evolved from Milestone 2 to Milestone 3 to support AI players and dark-side Flip cards.

---

### New Data Structures Added in Milestone 3:

1. **Side currentSide** (in Game class)
2. **ArrayList<Card> lightDeck** (in Game class)
3. **ArrayList<Card> darkDeck** (in Game class)
4. **List<Card> lightDiscard** (in Game class)
5. **List<Card> darkDiscard** (in Game class)
6. **Card.Color darkWildColor** (in Game class)
7. **Integer skipEveryoneFinalPlayer** (in Game class)
8. **static final Card.Color[] LIGHT_COLORS** (in Game class)
9. **static final Card.Color[] DARK_COLORS** (in Game class)
10. **boolean hasPlayedThisTurn** (in GameController class)

### Modified Structures:
1. **Card.Color enum** - Extended to include dark-side colors (TEAL, PURPLE, PINK, ORANGE)
2. **Card.Value enum** - Extended to include Flip cards (FLIP, DRAW_FIVE, SKIP_EVERYONE, WILD_DRAW_COLOR)
3. **ArrayList<Card> deck** - Now switches between lightDeck and darkDeck based on game side
4. **pendingSkips** - Enhanced logic to handle SKIP_EVERYONE card

---

## Milestone 2 Data Structures (Retained)

All core data structures from Milestone 2 were retained with enhancements:

- **ArrayList<Card> deck** - Now dynamically switches between light/dark
- **List<Player> players** - Now supports AIPlayer subclass
- **List<Card> cards (in Hand)** - Unchanged
- **PropertyChangeSupport pcs** - Unchanged, still drives MVC pattern
- **GameStateEvent** - Enhanced with dark wild color fields
- **int pendingSkips** - Enhanced for SKIP_EVERYONE logic

---

## New Data Structures in Milestone 3 - Detailed Explanation

### 1. Side currentSide (in Game class)

**Why chosen:**
- Simple enum (LIGHT or DARK) to track which side of cards is active
- Determines which deck is being used for drawing
- Affects card validation (can't play dark cards on light side)
- Affects which discard pile cards go into

**Operations used:**
- Direct assignment: `currentSide = Side.DARK`
- Ternary operations: `currentSide == Side.LIGHT ? lightDeck : darkDeck`
- Equality checks: `if (currentSide == Side.DARK)`

---

### 2. ArrayList<Card> lightDeck and ArrayList<Card> darkDeck (in Game class)

**Why chosen:**
- UNO Flip has two complete sets of cards (light and dark)
- Need to maintain separate decks that don't mix
- Both decks need same operations (shuffle, draw, reshuffle)
- Must preserve cards when switching sides

**Operations used:**
- `add()` - Building decks during initialization
- `remove(0)` - Drawing cards from active deck
- `Collections.shuffle()` - Shuffling on side switch
- `addAll()` - Adding discard pile back when reshuffling
- `clear()` - Clearing deck when transferring to discard

---

### 3. List<Card> lightDiscard and List<Card> darkDiscard (in Game class)

**Why chosen:**
- Each side needs its own discard pile
- Prevents light cards from being reshuffled into dark deck
- Maintains card integrity when switching sides
- Supports proper reshuffling for each side

**Operations used:**
- `add()` - Adding played cards to appropriate discard
- `size()` - Checking if enough cards to reshuffle
- `remove()` - Removing cards for reshuffling
- `clear()` - Clearing after reshuffle back to deck

---

### 4. Card.Color darkWildColor (in Game class)

**Why chosen:**
- WILD_DRAW_COLOR card requires remembering chosen dark color
- Separate from `topWild` which handles light wild cards
- Must persist between game state updates
- Used in two-phase WILD_DRAW_COLOR logic

**Operations used:**
- Assignment: `darkWildColor = color`
- Null checks: `if (darkWildColor != null)`
- Equality comparison: `drawn.getColor() == darkWildColor`
- Reset to null after use

---

### 5. Integer skipEveryoneFinalPlayer (in Game class)

**Why chosen:**
- SKIP_EVERYONE requires complex turn calculation
- Must skip all other players and land on specific player
- Integer wrapper class allows null when not active
- Stores the calculated final landing position

**Operations used:**
- Assignment: `skipEveryoneFinalPlayer = finalTarget`
- Null checks: `if (skipEveryoneFinalPlayer != null)`
- Reading: `currentPlayerIndex = skipEveryoneFinalPlayer`
- Reset: `skipEveryoneFinalPlayer = null`

---

### 6. static final Card.Color[] LIGHT_COLORS and DARK_COLORS (in Game class)

**Why chosen:**
- Need to iterate over all colors when building decks
- Static and final = constants that never change
- Arrays allow easy iteration in for-each loops
- Clearly separates light and dark color sets

**Operations used:**
- For-each iteration: `for (Card.Color color : LIGHT_COLORS)`
- Array access (implicit in enhanced for loops)

---

### 7. boolean hasPlayedThisTurn (in GameController class)

**Why chosen:**
- Prevents human players from playing multiple cards per turn
- Works with GUI event system (multiple clicks possible)
- Must be controller-side (not model) for UI locking
- Simple boolean flag for binary state

**Operations used:**
- Check: `if (hasPlayedThisTurn)`
- Set true: `hasPlayedThisTurn = true`
- Reset false: `hasPlayedThisTurn = false`

---

## Enhanced Structures from Milestone 2

### Card.Color enum - Extended

**New values added:**

**Why extended:**
- Dark side cards require four new colors
- Maintains same enum structure for consistency
- No changes needed to existing light card code
- Type-safe color handling for both sides

**Impact:**
- `getColorForCardColor()` in GameView needed new cases
- Wild color dialogs now support both light and dark
- Card validation logic unchanged (color matching still works)

---

### Card.Value enum - Extended

**New values added:**

**Why extended:**
- Flip cards need distinct value identifiers
- Maintains same enum structure for consistency
- Action card detection automatically includes new cards
- Scoring system can distinguish all card types

**Impact:**
- `handleActionCard()` added cases for new values
- `getCardScore()` added point values for new cards
- `checkIfActionCard()` automatically handles new action cards

---

## AI Player Implementation - Data Structure Choices

### AIPlayer extends Player

**Why chosen:**
- IS-A relationship: AI player is a type of Player
- Inherits all Player functionality (hand, score, name)
- No additional data structures needed in AIPlayer class
- Differentiated by class type, not internal data

**Why no AI-specific data:**
- AI doesn't need to remember past decisions
- Strategy is stateless (based on current game state only)
- Keeps Model simple and focused

---

## Conclusion

The transition from Milestone 2 to Milestone 3 required significant data structure additions to support:

**Key structural additions:**
1. Dual deck system (light/dark)
2. Dual discard system (light/dark)
3. Side tracking enum
4. Dark wild color tracking
5. Complex skip mechanics (SKIP_EVERYONE)
6. AI player identification
7. Turn locking flag

**Design philosophy:**
- Minimal changes to existing structures
- New structures for new features only
- Maintain MVC separation
- Keep Model logic-focused, Controller UI-focused

All changes maintain or improve the efficiency and maintainability of the original M2 design while enabling full UNO Flip gameplay with AI opponents.

---

**Course**: SYSC 3110  
**Milestone**: 3