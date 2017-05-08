QQApp
=====

## 仿Android版腾讯QQ 6.0 的UI，本项目仅仅用于练手，代码可能写得并不精致

### 1、UI预览
#### 登录页面预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212604.png)

#### 消息页面预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212621.png)

#### 左侧菜单栏预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212653.png)

#### 通知栏预览图
![](https://raw.githubusercontent.com/ssj64260/QQApp/master/image/Screenshot_20170508-212635.png)

### 2、所使用的控件
#### 2.1、MySlidingMenu
         这是一个自定义的左右滑动ViewGroup，是我仿照[这个贴](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0909/6612.html)写的，然后通过观察腾讯QQ的效果来进行微调。
         
#### 2.2、FlycoTabLayout
         这个是tabbar样式集合的一个第三方库，里面的样式比较齐全，[传送门](https://github.com/H07000223/FlycoTabLayout)
         
#### 2.3、XRecyclerView
         这个就不用我说了，自从用RecyclerView代替ListView后，下拉刷新和上拉加载就离不开这个控件了，[传送门](https://github.com/jianghejie/XRecyclerView)

#### 2.4、Glide
         Glide这么出名你都不知道，那就out了，头像画圆就用了Glide的transform方法，画圆和边框是自定义的GlideCircleTransform，感觉大才小用啊(lll￢ω￢)，[传送门](https://github.com/bumptech/glide)
