# 本地留言系统

## 一、项目简介

由于自己的需求分析有偏差，对企业内部的行文批准流程不熟悉，本系统只能当作一个普通的本地留言系统使用。
由于是数据库设计，系统具有基本的CRUD功能。

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/12.png)



## 二、设计内容

### 1、需求分析

#### 	内部行文管理系统需要完成功能主要有以下几点：

- 行文管理信息系统使用人员的用户名和密码信息，每个部门有—位管理员，具体负责行文管理信息系统的使用，该管理员的用户名和密码由后台的数据库管理员（就是我）预先设定。

- 文稿的撰写，包括行文号、拟稿人、行文标题、时间、正文信息的输入，输入后选样要发送的部门。

- 对所保存的未发送文稿的修改、发送功能。

- 对方部门、管理员所发文的接收、查看以及评论回复功能。

- 行文查询，可以查询本部门所发送的行文、末发送的行文以及别的部门所发送过来的行文。

  **总的来说就是：登陆、注销、写、发、收、查询功能**

### 2、系统功能模块设计

根据系统功能要求，可以将系统分解成几个模块来分别设计应用程序界面，如图所示：

![模块](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/1.png)

### 3、与其它系统的关系

可接入人事管理系统（如果有的话(～￣▽￣)～）



### 4、数据流程图

![数据流程图](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/2.png)

## 三、 数据库设计

### 1、数据库需求分析 

通过对企业内部行文管理的内容和数据流程分析，设计的数据项和数据结构如下：

- 用户口令信息。包括的数据项有**用户名**和**口令**
- 内部行文信息。包括的数据项有**文件号**、**撰写人**、**标题**、**正文**、**日期**、**行文状态**标志(0未发送，1发送成功，2已删除)、**是否已读**标志(0未读，1已读)
- 内部行文处理信息。包括的数据项有文件号、撰写人、接收人、标题、正文、日期、审核意见等。

### 2、数据库概念结构设计 

本系统所需数据的 E-R 模型图。

 ![E-R图](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/3.png)

### 3、数据库逻辑结构设计

在上面实体以及实体之间关系的基础上，形成数据库中的表格以及各个表格之间的关系。内部行文管理系统数据库中各个表格的设计结果如下所示。

**USER\_PSWD 用户登陆信息表：**

 ![用户登陆信息表](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/4.png)

**表 2 XWCL 行文处理信息表**

 ![行文处理信息表](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/5.png)

### 4、数据库的建立

向表USER\_PSWD输入初始数据

 ![建立数据库](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/6.png)

## 四、各功能模块的设计与实现

软件主要采用JavaFX和MySQL进行设计。JavaFX是基于Java的GUI设计框架，用它设计出的界面较为现代，且其内部UI控件可用CSS进行进一步的美化，所以我选择了它。但本人之前未接触过JavaFX，且课设时间只有两周，光是基本逻辑的实现和学习JavaFX基本功能就花费大量时间，故没有进一步美化界面（当时我也不会CSS）。下面是具体模块的设计与实现：



### 1、登陆模块：

#### 	(1)、分析与设计

 ![登录模块设计](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/7.png)

​	此模块实现进入系统管理前的验证操作，只有正确的用户名和密码才能进入系统并进行各种操作。其结构如图4所示

#### 	(2)、实现：通过JDBC连接数据库

通过抓取用户输入的用户名和密码，并将其与数据库中录入的用户信息进行比对。若合法，则进入主界面，不合法则提示信息有误。

#### 	(3)、效果展示



 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/8.png)

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/9.png)

### 2、主页面模块

#### 	(1)、分析与设计

主页面是用户通过身份认证后的界面，也是操作行文系统各个功能的平台。

主页面模块的结构图：

 ![主页模块结构](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/10.png)

#### 	(2)、实现

采用`JavaFX`作为GUI设计框架，主界面采用`BorderPane`边界布局。上部分和左部分是不变的，中部分和右部分随着所选功能的不同而变化。

顶部包括一个菜单栏和一个随功能变化内容的Title：菜单栏实现了程序退出和用户注销两个功能（点击注销后回到登陆界面）；Title初始文本为Welcome，当用户选择写信息时，Title会变为Write，选择收件箱时会变为Receive...

左边从上到下为4个按钮，依次为：写信息、收件箱、草稿箱、垃圾箱。

### 3、行文撰写模块

#### 	(1)、分析与设计

此模块实现对行文的撰写，包括行文标题、内容、日期等基本资料，写完后可选择发送行文或另存为至草稿箱。模块结构如图所示

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/11.png)

#### 	(2)、实现

单击写信息按钮后，`BorderPane`的Center部分为内容编辑区，Right部分为发送区。

编辑区中的文稿号、作者、日期已经自动填好，作者为当前用户、日期为当前时间，只需填写标题和正文即可。

发送区上半部分为选择联系人区域，打勾即为选中，支持多选，下半部分有两个按钮，分别为另存为和发送。另存为会将1条行文存至数据库，行文发送标志写为0。而发送则会写入n条数据（n为收件人数）且将发送标志写为1。

#### 	(3)、效果展示

撰写文案

 ![撰写](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/12.png)

另存文案

 ![另存](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/13.png)

发送文案

 ![发送](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/14.png)

### 4、行文接收、修改、删除模块

这三个模块功能类似，只是有一些细小的差别，故放在一起

#### 	(1)、分析与设计

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/15.png)

- 接收模块实现对收到行文的查看，并在查看后进行反馈。

- 修改模块实现对未发送行文的修改，并可在修改后发送。

- 删除模块实现对已收到行文的删除，误删除后也可还原。

  

  #### (2)、实现

将`BorderPane`界面Right部分为null，Center部分建立查询区，构成主要界面。

查询区既可选择按日期、发件人、收件人、主题等进行条件查询，也可查询全部。当在接收模块查询时，SQL查询语句中要有&quot;已发送标志为1且接收人为当前用户且删除标志为0&quot;的条件。在草稿中查询语句有&quot;已发送标志为0且发送人为当前用户且删除标志为0&quot;的条件。在删除模块中条件则变为&quot;删除标志为1且收件人为当前用户&quot;。

#### 	(3)、效果展示：

首先将账户切换至研发部（刚刚总经理给他发信息了）

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/16.png)

点击收件箱

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/17.png)

点击查询所有

单击查看

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/18.png)

单击保存

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/19.png)

返回到收件箱

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/20.png)

点击删除后再刷新，行文信息消失在收件箱中，出现在垃圾箱中

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/21.png)

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/22.png)

### 5、其他功能的实现

#### 	(1)、数据库异常报错

当数据库操作出错时，会弹出对话框并有详细的错误提示(提示是给程序员看的...)

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/23.png)

详细信息如下图

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/24.png)

#### (2)、用户不规范操作报错

用户不规范操作报错

比如用户在发送时未选择联系人会提示：

 ![](https://github.com/Paulpaulzmx/-Java-mysql-/blob/master/images/25.png)

## 五、 **总结和体会**

1. 软件的设计比具体的代码实现更重要。我在设计上花的时间要比实际写代码所花的时间多得多，其间还因为准备的不充分而大改了一次。
2. 对企业内部的行文处理方式了解不够充分（需求分析不到位），导致只按自己的想法设计软件。企业内部处理行文方式大致为：一个部门再给另一个部门发送行文时，要通过审核才能发送出去，而不是想发就发。且撰写行文的人员不只是行文管理人员，其他的员工也可以撰写行文，行文管理人员负责审核和反馈。还有，普通员工和管理员登陆后所拥有的权限应该是不同的，看到的界面也应不同。
3. 高冗余的代码会降低开发效率。
