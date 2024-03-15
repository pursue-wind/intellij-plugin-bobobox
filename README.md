# [BoboBox](https://plugins.jetbrains.com/plugin/22661-bobobox/)

Search for BoboBox in IDEA's plugin repository and download it.

### Row to Column
#### Usage (Shortcut)
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>R</kbd>
```
  a,b,c      a,1,2
  1,2,3  ->  b,2,4
  2,4,6      c,3,6
```

### Sequential Numbers
#### Usage (Shortcut)
- generate（<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>G</kbd>）
- ↗ （<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Up</kbd>）
- ↘ （<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Down</kbd>）

#### Supported Ranges
- Numbers
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


### CamelCase/Underscore Conversion, Supports Direct Translation from Chinese
#### Usage (Postfix)
- _cc (to CamelCase)
- _ul (to Underscore)

![trans](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/0e3948eb-168b-4451-a9dd-dc00608df25e)
#### Usage (Shortcut)
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Y</kbd> (to CamelCase)
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>U</kbd> (to Underscore)
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>7</kbd> (Translation Only)

![trans2](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/135fb9d2-b04e-4937-af0c-696663ca3980)

### Generate Log
#### Usage (postfix)
- `_log` Generate log.info code.
- `_logw` Generate log.warn code.
- `_loge` Generate log.error code.
- `_logd` Generate log.debug code.
- `_logt` Generate log.trace code.

![log](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/bbda7f9d-db5e-4640-a914-8a4aa1870650)

### Generate Set Methods
#### Usage (postfix)
- `_get` Generate all get methods.
- `_gets` The build does not include all get methods of the parent class.

![set](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/84a57115-4155-4122-a0ea-72a6aee9ac5e)

### Generate Get Methods
#### Usage (postfix)
- `_set` generates all set methods.
- `_sets` generates all set methods excluding those from the parent class.
- `_setn` generates all set methods without default values.
- `_setc` generates all set methods in lombok chain style.
- `_build` generates all build methods.
- `_buildn` generates all build methods without default values.
- `_gset` generates both get and set methods.

![get](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/d9b9b4ed-21e0-40e2-a943-0f279cf7e263)

