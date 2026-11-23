# Text Adventure Engine

A reusable engine for text adventure games, written in Java.

The engine doesn't contain any game content. Rooms, items, doors and win conditions are all read from a JSON file at startup, so the same code can run completely different games. The repo includes two: a medieval castle (`world.json`) and a sci-fi space station (`world2.json`).

This was a team project for the Programming Methodology Lab at the International Hellenic University (spring 2026).

## Example

```
--- Castle Entrance ---
You stand at the entrance of an ancient castle.
On the floor you see: lantern

> pick up the lantern
You picked up: lantern

> go east
--- Hidden Garden ---
On the floor you see: crown
```

## Commands

| Command | What it does |
|---|---|
| `go <direction>` | move (if the exit isn't locked) |
| `look` | describe the room again |
| `inspect <item>` | look at an item more closely |
| `take` / `drop <item>` | pick up or drop an item |
| `inventory` | show what you're carrying |
| `open <thing>` | open a door, if you meet its condition |
| `unlock <thing> with <item>` | unlock something with an item |
| `use <item>` | use an item (this is how you win) |
| `read <item>` | read notes, maps etc. |
| `save` / `load` | save or load the game |
| `history` | show the commands you've typed |
| `quit` | exit |

The parser understands synonyms and ignores articles, so `grab the key`, `pick up key` and `take key` all do the same thing. The synonyms are in `synonyms.json`, not in the code.

## How it works

```
src/main/java/adventure
├── engine/     Main, game loop (GameEngine), game state (GameState)
├── commands/   parser, synonyms, command registry, one class per command
├── loader/     reads the world from JSON (JsonGameLoader)
├── model/      Room, Player, Item, Exit, Door, Condition
└── ui/         console input/output behind an interface
```

Some of the design decisions:

- **Command pattern.** Every command is a class that implements `Command`. A `CommandRegistry` maps each verb to its class with a `HashMap`, so there are no long if/else chains. Adding a command means writing one new class and registering it.
- **Data-driven world.** The loader reads the JSON in two passes. First it creates all the rooms, then it connects the exits, because exits point to rooms that might not exist yet.
- **Conditions as strategies.** A door can have a `Condition` (for example `HasItemCondition`) that decides whether it opens. New kinds of conditions don't need changes in `Door`.
- **Constructor injection.** Commands that need the game state get it through their constructor.

To make a new game you only write a new JSON file. No Java changes are needed.

## Running it

You need JDK 17+ and Maven (or just IntelliJ, which has both).

```bash
mvn package
java -jar target/text-adventure-engine.jar              # castle
java -jar target/text-adventure-engine.jar world2.json  # space station
```

Run it from the project folder, because it looks for the JSON files there. In IntelliJ you can run `adventure.engine.Main` directly and pass `world2.json` as a program argument for the second game.

## My part

I worked on the core of the engine: the game loop and game state (`GameEngine`, `GameState`, the flag system used for winning), the command parser and synonym handling (`CommandParser`, `SynonymMap`), and the JSON loader (`JsonGameLoader`).

## Team

- Ντένισον Μπαρδη (Denison Bardhi)
- Κωνσταντίνος Παναγής
- Μιλτιάδης Κομμάτας
- Ζήσης Τσιάμης

## Things I'd improve

- Many commands still print with `System.out` directly instead of going through the `ConsoleUI` interface
- The code that describes a room is repeated in a few places
- Items are found by name, so two items with the same name would clash
- Only one save file
- NPCs with dialogue were planned but never made it in
- No automated tests yet
