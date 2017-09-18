# Indoor
一个类似微信的android app，并且实现了多种功能扩展，需要在face++,高德地图，Bmob平台注册账号并作相关配置。

* 其中会话部分存在头像显示有误的问题，可能跟本地缓存有关，这个bug还没解决掉
* 黑名单还没做
* 消息通知、声音、震动关闭与否也还没做
* 背景设置已做完，但是在会话里面没有去设置
* 购物部分的“点击购买”当然是不能购买的
* 新闻跟购物也就放了些测试数据，没有实时去爬数据
* 附近的人也是头像显示有问题，估计也是跟本地缓存有关
* 朋友分布点击之后的导航设置没做，其实可以做，几行代码（因为在生活周边里面导航已经实现了，跳转过去传个位置过去就行了）
* 朋友圈很鸡肋，就只有看，发的话也只能发图片加文字，不可以单独，主要是布局麻烦，就没做了
* 会话里面的表情发送还没做，不过现在貌似手机都可以直接发，自动解析的
* 发现里面就做了催眠大师、每日一文和how old
* 大概就是这样了......

## 随便写点什么

* apk文件夹下是一个打包好的apk，可以直接运行
* 以下展示下基于[BmobNewIM SDK demo](http://www.bmob.cn)改造而来的应用，添加了一些功能，修改了一些例如session之类的bug和之前demo没有实现的功能等


<table>
  <tr>
    <td style="vertical-align:bottom; text-align:center;">
     <img width="216" height="384" src="images/Screenshot_2017-04-25-18-57-45-088_Indoor.png"/>
    </td>
  </tr>
  <tr>
    <td style="vertical-align:bottom; text-align:center;">
     <img width="216" height="384" src="images/Screenshot_2017-04-25-18-57-45-088_Indoor.png"/>
    </td>
  </tr>
  <tr>
    <td style="vertical-align:bottom; text-align:center;">
     <img width="216" height="384" src="images/Screenshot_2017-04-25-18-57-45-088_Indoor.png"/>
    </td>
  </tr>
</table>
