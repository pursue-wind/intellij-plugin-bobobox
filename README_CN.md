# Generator

### 行转列
#### 使用方式（快捷键）
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>R</kbd>
```
  a,b,c      a,1,2
  1,2,3  ->  b,2,4
  2,4,6      c,3,6
```

### 顺序数 
#### 使用方式（快捷键）
- 生成（<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>G</kbd>）
- ↗ （<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Up</kbd>）
- ↘ （<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Down</kbd>）

#### 支持范围
- 数字
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
#### 使用方式（postfix）
- _cc 转驼峰
- _ul 转下划线

![trans](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/0e3948eb-168b-4451-a9dd-dc00608df25e)
#### 使用方式（快捷键）
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Y</kbd> 转驼峰
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>U</kbd> 转下划线
- <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>7</kbd> 仅翻译

![trans2](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/135fb9d2-b04e-4937-af0c-696663ca3980)

### 生成log
#### 使用方式（postfix）
- _log 
- _logw
- _loge
- _logd
- _logt

![log](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/bbda7f9d-db5e-4640-a914-8a4aa1870650)

### 生成set方法 
#### 使用方式（postfix）
- _get 生成所有get方法
- _gets 生成不包括父类的所有get方法

![set](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/84a57115-4155-4122-a0ea-72a6aee9ac5e)

### 生成get方法
#### 使用方式（postfix）
- _set 生成所有set方法
- _sets 生成不包括父类的所有set方法
- _setn 生成不带默认值的所有set方法
- _setc lombok chain风格生成所有set方法
- _build 生成所有build方法
- _buildn 生成所有build方法不带默认值
- _gset 生成get和set方法

![get](https://github.com/pursue-wind/intellij-plugin-bobobox/assets/40025981/d9b9b4ed-21e0-40e2-a943-0f279cf7e263)

