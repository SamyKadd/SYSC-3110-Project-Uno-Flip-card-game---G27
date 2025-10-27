# Data Structures Explanation - UNO Card Game

This document explains the data structure choices made in the UNO card game implementation and their relevant operations.

---

## ArrayList<Card> deck (in Game class)

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

---

## List<Player> players (in Game class)

**Why chosen:**
- Player order matters for turn rotation
- Need indexed access to get current player using `currentPlayerIndex`
- Size is fixed during gameplay (2-4 players)

**Operations used:**
- `add()` - Adding players before game starts
- `get(index)` - Getting current player by index
- `size()` - Validating player count and calculating next turn
- `remove()` - Removing players

---

## List<Card> cards (in Hand class)

**Why chosen:**
- Cards must maintain display order for player selection (0, 1, 2, 3...)
- Need indexed access when player selects card by number
- Dynamic size as cards are drawn and played

**Operations used:**
- `add()` - Drawing cards
- `get(index)` - Viewing card at index
- `remove(index)` - Playing a card
- `size()` - Checking hand size and win condition

---

## List<Card> discardedPile (in Game class)

**Why chosen:**
- Tracks all played cards in order
- Only needs append operations
- May be used for future features like reshuffling

**Operations used:**
- `add()` - Adding played cards to discard pile
- `size()` - Tracking number of cards played

---

## Scanner input (in Game class)

**Why chosen:**
- Single Scanner instance prevents resource leaks
- Reused throughout game for card selection and color choice

**Operations used:**
- `nextLine()` - Reading player input
- `trim()` - Remove whitespace from input
- `toUpperCase()` - Normalize color selection

---

**Course**: SYSC 3110  
**Milestone**: 1