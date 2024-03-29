<div align="center">
<img src="app/src/main/ic_launcher-playstore.png"
    alt="Get it on F-Droid"
    height="80">
<h1>Dooz/Tic-Tac-Tao</h1>
</div>

[![Android CI](https://github.com/yamin8000/Dooz/actions/workflows/android.yml/badge.svg)](https://github.com/yamin8000/Dooz/actions/workflows/android.yml)

Dooz or Tic-tac-toe is a simple pen-and-paper game. I created this implementation using Kotlin and
Jetpack Compose for Android Devices.

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
alt="Get it on F-Droid"
height="80">](https://f-droid.org/packages/io.github.yamin8000.dooz)

## Preview

### [Featured on AndroidDevNotes](https://twitter.com/androiddevnotes/status/1604571703350161409)

<img src="./fastlane/metadata/android/en-US/images/phoneScreenshots/1.jpg" alt="preview" width="300"/>

https://github.com/yamin8000/Dooz/assets/5001708/bd9f0905-2677-45d5-b5f3-c1ed8ef894d5

## Compatibility

**SDK21+** or **Android 5.0+**

## Usage

Just play it!

## Download

- GitHub releases: [here](https://github.com/yamin8000/Dooz/releases)
- F-Droid: [here](https://f-droid.org/packages/io.github.yamin8000.dooz)
- ~~Bazaar: [here](https://cafebazaar.ir/app/io.github.yamin8000.dooz)~~

## Features

### Technical

- Jetpack Compose
- Material3 and Dynamic Color

### Game

- Variable game board grid size from three-by-three to seven-by-seven (Larger than seven-by-seven is
  simply ridiculous and unplayable on mobile phones)
- Variable game AI difficulty (easy, medium, hard)

### Variations

#### Simple Game

The Simple game is the simplest variation of Tic-tac-toe with a three-by-three grid with two
players. The player who succeeds in placing three of their marks horizontally, vertically, or
diagonally is the winner.

<img src="https://upload.wikimedia.org/wikipedia/commons/3/32/Tic_tac_toe.svg" alt="preview" width="200"/>

##### Simple Game AI

Currently, in hard mode, AI uses a strategy rather than an AI search algorithm like MinMax. This
strategy consists of these steps: Win, Block, Fork, Block Fork, Center Play, Corner Play, and Side
Play.

Strategy's source: Flexible Strategy Use in Young Children's Tic-Tac-Toe by Kevin Crowley, Robert S.
Siegler

[More info on the strategy here](https://onlinelibrary.wiley.com/doi/abs/10.1207/s15516709cog1704_3)

On Easy difficulty, the AI plays a random empty cell in the grid. In Medium difficulty, AI chooses
between Easy and Hard Difficult for each move based on a 50/50 chance.

## Changelog

- [Releases](https://github.com/yamin8000/Dooz/releases)

## License

> Owl is licensed under the **[GNU General Public License v3.0](./LICENSE)**  
> Permissions of this strong copyleft license are conditioned on making  
> available complete source code of licensed works and modifications,  
> which include larger works using a licensed work, under the same  
> license. Copyright and license notices must be preserved. Contributors  
> provide an express grant of patent rights.
