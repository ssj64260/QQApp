QQApp
=====

## 仿Android版腾讯QQ 6.0 的UI，本项目仅仅用于练手，代码可能写得并不精致，但是界面我觉得已模仿得90%像了，因为我是开布局显示然后截图来对着调的。

### 1、UI预览
#### 登录页面预览图（帐号：1056125823，密码：654321）当然真正的密码并不是这个(￣▽￣)"
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212604.png)

#### 消息页面预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212621.png)

#### 左侧菜单栏预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212653.png)

#### 通知栏预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212635.png)

### 2、所使用的控件
#### 2.1、MySlidingMenu
         这是一个自定义的左右滑动ViewGroup，是我参考[这个贴](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0909/6612.html)进行构思的，然后通过观察腾讯QQ的效果来进行微调。
         
#### 2.2、FlycoTabLayout
         这个是tabbar样式集合的一个第三方库，里面的样式比较齐全，[传送门](https://github.com/H07000223/FlycoTabLayout)
         
#### 2.3、XRecyclerView
         这个就不用我说了，自从用RecyclerView代替ListView后，下拉刷新和上拉加载就离不开这个控件了，[传送门](https://github.com/jianghejie/XRecyclerView)

#### 2.4、Glide
         Glide这么出名你都不知道，那就out了，头像画圆就用了Glide的transform方法，画圆和边框是自定义的GlideCircleTransform，感觉大才小用啊(lll￢ω￢)，[传送门](https://github.com/bumptech/glide)
         
#### 2.5、QQMessageService
         本项目有一个服务，是用来定时发送消息，并推送到通知栏，本来应该是两部手机相互发送，等有时间加个Socket通过局域网进行对话聊天功能(￣▽￣)"

### 3、最后放一些动态预览图
  图片好像不能循环播放，所以可能要刷新一下(lll￢ω￢)<br>
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/20170508224328-1.gif)
