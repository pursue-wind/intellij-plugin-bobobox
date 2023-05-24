# Generator
### 顺序数 
生成（CTRL + SHIFT + ALT + G） 增加 （CTRL + SHIFT + ALT + ⬆ ）减少 （CTRL + SHIFT + ALT + ⬇ ）
- a-z A-Z
- 零, 一, 二, 三, 四, 五, 六, 七, 八, 九, 十...
- 周一, 周二, 周三, 周四, 周五, 周六, 周日
- 星期一, 星期二, 星期三, 星期四, 星期五, 星期六, 星期日
- Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday...
- MON, TUE, WED, THU, FRI, SAT, SUN...
- 一月, 二月, 三月, 四月, 五月, 六月...
- January, February, March, April, May, June...
- JAN, FEB, MAR, APR, MAY, JUN...
- 甲, 乙, 丙, 丁, 戊, 己, 庚, 辛, 壬, 癸
- 红, 橙, 黄, 绿, 青, 蓝, 紫
- RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE
![gen3](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/afd094c2-cc2a-4cb1-9747-3487c523effb)

![gen1](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/a2b0e97d-4c88-4a32-bea3-f275de9478ca)


### 驼峰/下划线 转换，支持直接从中文转换，如果为中文会先翻译
postfix (_get, _gets)
![trans](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/0e3948eb-168b-4451-a9dd-dc00608df25e)

CTRL SHIFT ALT Y
CTRL SHIFT ALT U
CTRL SHIFT ALT 7
![trans2](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/135fb9d2-b04e-4937-af0c-696663ca3980)

### 生成log
postfix (_log, _logw, _loge, _logd, _logt)
![log](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/bbda7f9d-db5e-4640-a914-8a4aa1870650)
### 生成set方法 
postfix (_get, _gets)
![set](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/84a57115-4155-4122-a0ea-72a6aee9ac5e)
### 生成get方法
postfix (_set, _sets, _setn, _setc, _build, _buildn, _gset)
![get](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/d9b9b4ed-21e0-40e2-a943-0f279cf7e263)





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
