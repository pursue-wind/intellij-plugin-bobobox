# Generator
![set](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/84a57115-4155-4122-a0ea-72a6aee9ac5e){:height="200px" width="200px"}
![get](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/d9b9b4ed-21e0-40e2-a943-0f279cf7e263){:height="200px" width="200px"}

![log](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/bbda7f9d-db5e-4640-a914-8a4aa1870650){:height="200px" width="200px"}




# InLine
[![Version](https://img.shields.io/jetbrains/plugin/v/21051-inline.svg?color=aa3030)](https://plugins.jetbrains.com/plugin/21051-inline)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/21051-inline.svg?color=aa3090)](https://plugins.jetbrains.com/plugin/21051-inline)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/21051-inline?color=30aa30)](https://plugins.jetbrains.com/plugin/21051-inline)
<!-- Plugin description -->
### InLine is highly customizable plugin that shows errors and hints inline. 
#### Also supports gutter icons, colorful background and special effects.

* Errors are filtered on the line by priority
* Supports different fonts and languages
* Supports different hints styles __After Line, Rust Style__

<kbd>Settings</kbd> > <kbd>Appearance & Behaviour</kbd> > <kbd>⚙ InLine</kbd>
### In plugin settings you can:
* Show or hide specific __level of errors__ - _error, warning, weak warning, information_
* Change __text colors & text visibility__ for each error level
* Change __text style__ to make it appear _after line end_ or _under line_
* Change __background colors__ & __background visibility__ for each error level
* Change __gutter icons visibility__ for each error level and select number of gutter icons
* Apply additional effect
* Change font of the hints
* __Ignore__ some errors by description

![](https://raw.githubusercontent.com/IoaNNUwU/InLine/main/media/example.png)

### Inspired by
* [Inspection Lens](https://plugins.jetbrains.com/plugin/19678-inspection-lens) - Plugin I've used before.
It lacks customization and features
* [Inline Problems](https://plugins.jetbrains.com/plugin/20789-inlineproblems) - Amazing plugin I stole box effect idea from.

### Choices

There are already multiple plugins like this inspired by VSCode ErrorLens extension - check <kbd>Inspired by</kbd> section.
I've decided to make my own because I want to have some extra features for code __writing and demonstration__ purposes such as:
* Fill __background of whole line__
* Show __error icons__ in gutter area for different kinds of errors
* Turn __hint text off__ but make __background stay__

### Future

In the future I am planning to add more customization such as:
* New effects
* Error description changing
* Additional ways to filter errors

### Contribution

This plugin is open source. You can report bugs and contribute at [GitHub](https://github.com/IoaNNUwU/InLine).

<!-- Plugin description end -->
### Change notes:
<!-- Change notes -->
* __1.1.1__ - Update to latest IntelliJ version
* __1.1.0__ - Add __Rust Style Errors__ that are shown under line similar to __Rust compiler__ messages
* __1.0.1__ - Bug fixes & Font change support (Fix Chinese characters being shown as `?`)
* __1.0__ - Release
<!-- Change notes end -->
