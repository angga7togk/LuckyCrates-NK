# <img src='https://github.com/angga7togk/LuckyCrates-PNX/blob/8169905ea0640a2df6d8c0cc6e89e829375e1c1d/treasure-chest.png' width='50'> LuckyCrates-PNX
LuckyCrates-PNX is an exciting and customizable plugin for Nukkit, designed to bring a new level of fun and rewards to your server through engaging crate systems. Whether you want to offer common items, custom creations, enchanted gear, or execute commands upon crate opening, LuckyCrates-PNX has got you covered.

This plugin is inspired by [PiggyCrates](https://github.com/DaPigGuy/PiggyCrates).

This plugin is for [PowerNukkitX](https://github.com/PowerNukkitX/PowerNukkitX).

## Preview
<img src='https://github.com/angga7togk/LuckyCrates-PNX/blob/2cdbc2c2030429f84a859b75069ef15f40ecd5fa/Crates.jpg' width='250'> <img src='https://github.com/angga7togk/LuckyCrates-PNX/blob/2cdbc2c2030429f84a859b75069ef15f40ecd5fa/PreviewCrate.jpg' width='150'> <img src='https://github.com/angga7togk/LuckyCrates-PNX/blob/2cdbc2c2030429f84a859b75069ef15f40ecd5fa/OpenCrate.jpg' width='150'>

## Features

- **Easy Configuration:** Set up your crates effortlessly using the `crates.yml` file.
- **Item Drops:** Define a variety of drops, including normal items, custom items, enchanted items, and commands.
- **Multilingual Support:** Seamless support for both English and Indonesian (Bahasa Indonesia) languages.
- **Crate Types:** Choose between instant rewards and roulette-style crates for diverse player experiences.

## Getting Started

### Installation
1. Download the latest release from [LuckyCrates](https://cloudburstmc.org/resources/luckycrates.952/).
2. Download the depend plugin [FakeInventories v1.1.5](https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.5).
3. Place the JAR file into the `plugins` folder of your Nukkit server.
4. Start or reload your server.

### Configuration

Edit the `crates.yml` file in the plugin folder to customize your crates. Refer to the provided examples for detailed instructions on configuring your crates.
```YAML
# Name crates
Common: 
  # maximum drop is 36 slots, if it is higher it will be ignored
  drops:
    # Example of Normal Items
    - id: 1
      meta: 0 
      amount: 16 
      chance: 25 # chance 1 - 100
    # Example of Custom Item
    # name = customName
    # lore = description
    - id: 263
      meta: 0
      name: "Item Custom"
      lore: "nice item made by angga7togk"
      chance: 75
      amount: 10
    # Example of item + enchant
    - id: 278
      meta: 0
      amount: 1
      chance: 10
      name: "Telepathic Pickaxe"
      lore: "Items mined are transported into your inventory"
      enchantments:
        - name: "unbreaking"
          level: 1
        - name: "knockback"
          level: 1
    # Example of using Command
    - id: 49
      meta: 0
      amount: 32
      chance: 15
      name: "Money 1000"
      commands:
        - "givemoney {player} 1000"
        - "give {player} bread 1"
  # amount, is the number of keys needed when opening crates
  amount: 1
  # Commands are commands that are executed when the player opens the crate
  # just remove the command if you don't want to use the command
  commands: 
    - "say {player} has opened an Common Crate"
  # floating text, which will appear on crates / blocks that have been made into crates in the world
  floating-text: "Common Crate"
```
### Commands and Permissions

- `/setcrate <crate>` - Set a crate by breaking a block.
  - Permission: `crates.command.setcrate`

- `/key <crate> <amount> <player>` - Give a specified amount of keys for a crate to a player.
  - Permission: `crates.command.key`

- `/keyall <crate> <amount>` - Give a specified amount of keys for a crate to all online players.
  - Permission: `crates.command.keyall`

### Multilingual Support

LuckyCrates-PNX seamlessly supports both English and Indonesian (Bahasa Indonesia) languages, automatically detecting the player's language preference.

### Crate Types

LuckyCrates-PNX offers two crate types:
- **Instant Crates:** Immediate rewards upon opening.
- **Roulette Crates:** Players spin the roulette for a chance to win different items.

## Icon

Icon By [Flaticon](https://www.flaticon.com)

## Additional Information

This plugin is tested and compatible with [FakeInventories v1.1.5](https://github.com/IWareQ/FakeInventories/releases/tag/v1.1.5).

Feel free to contribute, report issues, or suggest improvements on [GitHub](https://github.com/angga7togk/LuckyCrates-PNX/issues).

Enjoy the thrill of LuckyCrates-PNX on your Nukkit server!
