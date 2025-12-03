# Data Structures Explanation - UNO Card Game
## Changes from Milestone 1 to Milestone 2 to Milestone 3 to Milestone 4

This document explains the data structure choices made in the UNO Flip card game implementation and how they evolved from Milestone 2 to Milestone 3 to Milestone 4 to support serialization, undo/redo, and replay functionality.

---

### New Data Structures Added in Milestone 4:

### Undo/Redo System:
1. **transient ArrayList<GameMemento> undoStack** (in Game)
2. **transient ArrayList<GameMemento> redoStack** (in Game)
3. **GameMemento class** (new class for state snapshots)
4. **SerializationUtils class** (utility for deep cloning)

### Serialization Support:
5. **serialVersionUID fields** (in all Serializable classes)
6. **transient modifiers** (on non-serializable fields)

### Replay System:
7. **int currentRound** (in Game)
8. **static final int WINNING_SCORE = 500** (in Game)

### Modified:
- All model classes now implement `Serializable`
- Game.pcs and Game.views marked `transient`
- GameUIListener added save/load methods

---

## New Data Structures in Milestone 4 - Detailed Explanation

---

### 1. Undo/Redo Data Structures

### transient ArrayList<GameMemento> undoStack

**Purpose:** Stores previous game states for multi-level undo

**Why ArrayList:**
- Need indexed access to remove last element
- Dynamic sizing for unlimited undo depth
- Efficient add/remove at end

**Why transient:**
- Prevents infinite recursion during serialization
- Undo history doesn't persist across save/load
- Re-initialized to empty after loading

**Operations:**
- `add(snapshot)` 
- `remove(size()-1)` 
- `isEmpty()` 
- `clear()` 

### GameMemento Class

**Purpose:** Immutable snapshot of complete game state

**Why this structure:**
- Memento pattern for encapsulation
- All fields final (immutable after creation)
- Deep copies prevent reference issues
- Serializable for deep cloning

---

### 2. Serialization Implementation

### Why Serializable Interface:

**Classes implementing:**
- Card, Hand, Player, AIPlayer, Game, GameMemento, Side

**Benefits:**
- Java's built-in serialization
- Automatic object graph traversal
- Version control via serialVersionUID
- Error handling built-in

**Limitations:**
- Cannot serialize UI components
- Cannot serialize listeners
- Solution: mark as transient

---

### 3. Transient Fields in Game:

**Why transient:**
1. **pcs** - Not Serializable, contains listener references
2. **views** - UI components, not Serializable
3. **undoStack/redoStack** - Session-specific, prevents recursion

---

### 4. Replay System Structures

### int currentRound

**Purpose:** Track current round number

**Serialized:** Yes (persists across save/load)

**Operations:**
- Increment in startNewRound()
- Reset in startNewGame()
- Display in UI

---

### 5. static final int WINNING_SCORE

**Purpose:** Define win condition (500 points)

**Why static final:**
- Constant value (never changes)
- Class-level (shared by all instances)
- Compile-time constant

**Value:** 500 (official UNO rules)

---

## Error Handling

### Serialization:
1. **NotSerializableException**  All models implement Serializable
2. **FileNotFoundException**  User dialog, graceful failure
3. **IOException**  Caught, logged, user notified
4. **ClassNotFoundException**  Corrupted file detection

### Undo/Redo:
1. **Empty Stack** Buttons disabled via canUndo/canRedo
2. **State Inconsistency**  Deep copying prevents issues

---

## M3 vs M4 Comparison

### Retained from M3:
- Deck system (light/dark)
- Side tracking
- Action card state
- MVC architecture

### Added in M4:
- Serialization support
- Undo/redo system
- Replay functionality
- Error handling

### Changed in M4:
- All models now Serializable
- Transient fields for UI/listeners
- Custom readObject() method

---

## Conclusion

Milestone 4 adds:
1. **Serialization** - Java built-in, lecture-based
2. **Undo/Redo** - Memento pattern, deep cloning
3. **Replay** - Simple counters, minimal overhead

All while maintaining M3's clean MVC separation.

---

**Course**: SYSC 3110  
**Milestone**: 4