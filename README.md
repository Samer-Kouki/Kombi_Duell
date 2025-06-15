# Kombi-Duell 🂡🂢🂣

**Kombi-Duell** is a turn-based card game for 2 players, implemented in Kotlin using the BoardGameWork framework.  
It was developed as part of a university programming course.

---

## 🎮 Game Overview

Kombi-Duell is played with a standard 52-card deck. Each player starts with 7 cards, and the goal is to score as many points as possible by playing valid card combinations.

---

## 🧩 Game Components

- **52 cards** (4 suits × 13 values)
- **Handcards**: 7 per player at game start
- **Exchange Area**: 3 open cards available for swapping
- **Draw Pile**: 35 cards face down
- **Discard Pile**: one per player

---

## 🎯 Goal

Players try to collect points by playing valid combinations:

| Combination Type | Description                            | Points           |
|------------------|----------------------------------------|------------------|
| Drilling         | Three cards of same value              | 10 points        |
| Vierling         | Four cards of same value               | 15 points        |
| Sequence         | 3+ consecutive cards of same suit      | 2 points per card|

Sequences wrap around: `D-K-A-2-3` is valid.

---

## 🔁 Gameplay

Players alternate turns. In each turn, a player can perform **one or two different actions**:

1. **Draw Card**  
   - Draw from draw pile (if not empty)  
   - Max 10 cards in hand

2. **Swap Card**  
   - Swap one hand card with one in the exchange area

3. **Play Combination(s)**  
   - Play one or more valid combos to score points

4. **Pass**  
   - Do nothing

---

## 🏁 End of Game

The game ends when:
- A player has no more hand cards **or**
- Both players pass on two actions in a row

The player with the **highest score** wins.

---

## 🖥 Features

- Kotlin application using [BoardGameWork](https://bgw.tu-dortmund.de)
- Hotseat mode (both players use the same device)
- Hidden hand cards for non-active player (via transition screen)
- Winner display at game end
- Configurable player names at start

---

## 🚀 Getting Started

### Prerequisites
- IntelliJ IDEA with Kotlin support
- JDK 17+

### Run the game

1. Clone the repo:
   ```bash
   git clone https://github.com/Samer-Kouki/Kombi_Duell.git
