# UNO Flip Card Game - User Manual
**SYSC 3110 | Team G27**

---

## Table of Contents
1. [Getting Started](#getting-started)
2. [How to Play](#how-to-play)
3. [Game Features](#game-features)
4. [Scoring System](#scoring-system)
5. [Advanced Features](#advanced-features)

---

## Getting Started

### System Requirements
- **Java Version:** Java 8 or higher
- **Operating System:** Windows, macOS, or Linux

### Installation

#### Option 1: Running the JAR File (Recommended)
1. Locate the `GroupProj.jar` file
2. Double-click the JAR file, OR
3. Open terminal/command prompt and run:
   ```bash
   java -jar GroupProj.jar
   ```

#### Option 2: Running from Source Code
1. Ensure all `.java` files are in the same directory
2. Compile the code:
   ```bash
   javac Main.java
   ```
3. Run the game:
   ```bash
   java Main
   ```

### First Launch

When you start the game, you'll see two dialogs:

**Dialog 1: Select Number of Human Players**
- Choose between 2-4 human players
- Click "OK" to proceed

**Dialog 2: Select Number of AI Players**
- Choose 0-3 AI players
- **Total players (human + AI) must be 2-4**
- AI players are marked with "(AI)" suffix
- Click "OK" to start the game

---

## How to Play

### Game Objective
Be the first player to reach **500 points** by winning multiple rounds!

### Round Objective
Empty your hand by playing all your cards before other players.

### Your Turn (Human Players)

On your turn, you have **three options**:

#### 1. Play a Card
- Click on any card in your hand at the bottom of the screen
- The card must be **valid** (see [Valid Plays](#valid-plays) below)
- After playing, click "NEXT PLAYER" to continue

#### 2. Draw a Card
- Click the "DRAW CARD" button
- You'll receive one card from the draw pile
- Your turn automatically ends
- Click "NEXT PLAYER" to advance

#### 3. Pass (No Valid Cards)
- If you have no valid cards and don't want to draw
- Click "NEXT PLAYER" to skip your turn
- **Note:** You must either play a card OR draw before advancing

### AI Player Turns

When it's an AI player's turn:
1. The AI automatically selects and plays a card (or draws)
2. You'll see a status message indicating what the AI did
3. Click "NEXT PLAYER" to advance to the next player

**AI players cannot be controlled manually**

### Valid Plays

A card is **valid** if it matches **ONE** of these criteria:

1. **Same Color** as the top card
   - Example: Play RED 5 on RED SKIP 

2. **Same Number/Action** as the top card
   - Example: Play BLUE 7 on RED 7 

3. **Wild Card** (always valid)
   - WILD, WILD DRAW TWO, WILD DRAW COLOR

4. **Matching the chosen wild color**
   - If a wild was played and RED was chosen
   - Any RED card is now valid

### Side-Specific Rules

**Light Side Cards** can only be played on the **Light Side**
**Dark Side Cards** can only be played on the **Dark Side**

The FLIP card switches between sides!

---

## Game Features

### 1. Save Game

**How to Save:**
1. Click "File" in the menu bar (top-left)
2. Select "Save Game"
3. Choose a location on your computer
4. Enter a filename (e.g., "my_uno_game")
5. Click "Save"

**What Gets Saved:**
- All player hands and scores
- Current round number
- Turn order and current player
- Light/Dark side status
- Draw and discard piles
- Wild card colors

**File Format:** `.uno` extension

### 2. Load Game

**How to Load:**
1. Click "File" in the menu bar
2. Select "Load Game"
3. Confirm you want to replace the current game
4. Navigate to your saved `.uno` file
5. Click "Open"

**After Loading:**
- Game resumes exactly where it was saved
- All players, scores, and cards are restored
- Continue playing immediately

**Important:** Loading replaces your current game. Make sure to save first if you want to keep it!

### 3. Undo Move

**How to Undo:**
- Click the "UNDO" button (bottom-left)
- Your last action is reversed
- Game state returns to before that action

**What Can Be Undone:**
- Card plays
- Card draws
- Turn advances

**Multi-Level Undo:**
- Click "UNDO" multiple times to go back several moves
- Button is **disabled** when there's nothing to undo

### 4. Redo Move

**How to Redo:**
- Click the "REDO" button (bottom-left)
- Only available after using UNDO
- Restores the action you just undid

**Important:**
- REDO is cleared when you take a new action
- You can redo multiple times in a row

### 5. Multi-Round Play

**How Rounds Work:**
1. When a player empties their hand, they **win the round**
2. Points are calculated from opponents' remaining cards
3. Winner's score increases
4. A "NEW ROUND" button appears (green)
5. Click "NEW ROUND" to deal new hands

**Between Rounds:**
- All scores are preserved
- Round number increments (shown in status)
- Same players continue
- Turn order resets to Player 1

### 6. Multi-Game Play

**How Games Work:**
1. When a player reaches **500 points**, they **win the game**
2. A "NEW GAME" button appears (blue)
3. Click "NEW GAME" to restart with same players

**After New Game:**
- All scores reset to 0
- Round counter resets to 1
- New hands are dealt
- Fresh game begins

---

## Scoring System

### How Points Are Calculated

When a player wins a round:
1. Count all cards remaining in **opponents'** hands
2. Add up the point values (see below)
3. Winner receives that total as their score

### Winning Conditions

**Win a Round:**
- Empty your hand completely
- Receive points from opponents' cards
- Click "NEW ROUND" to continue

**Win the Game:**
- Be the first to reach **500 points**
- Click "NEW GAME" to play again

---

## Advanced Features

### AI Strategy

AI players use a simple but effective strategy:

1. **Prefer color matches** over value matches
2. **Save wild cards** for when needed
3. **Play action cards** strategically
4. **Auto-select wild colors** based on their hand

**AI Difficulty:** Medium (no advanced tracking)

### Status Messages

Pay attention to the status bar (below top card):

- **"[Player]'s turn!"** - Current player indicator
- **"SKIP played..."** - Action card effects
- **"FLIP to DARK/LIGHT"** - Side changes
- **"[Player] draws X"** - Draw effects
- **"Choose a color..."** - Wild card prompts

---

## About This Game

**Developed by:** Team G27
- Joodi Al-Asaad
- Mahdi Bouakline
- Samy Kaddour
- Seham Khalifa

---

**Thank you for playing UNO Flip!**

Enjoy the game!