# CustumeView
自定义控件集合

1.ReservationView
效果：
  针对预约选择的一种控件
使用方法：
setStartDate();方法接收“2017-02-24”格式日期字符串，没做容错处理
setItemClickListener()设置点击监听器
setReservationTime()设置可预约的时间，接收以上日期格式字符串集合，没做容错处理

2.WaveLoadDrawable
效果：
  类似一种波浪下载的效果，不过是自定义Drawable实现
使用方法：
  new WaveLoafDrawable（Drawable drawable）
  或  new WaveLoadDrawable(Context contect,int resId)从资源文件中获取Drawable对象
  
  通过startWaving以及cancelWaving来控制波浪效果
  暂不支持xml定义，以及波浪的效果  
  
 3.HornView
 效果：
   类似微信语音播放的喇叭效果
 使用方法：
  不支持xml定义
4.RefreshIndicator刷新指南对话框
效果:一种类似的下载加载刷新指示框
使用方法：setFlag（int ）三种Flag：
    public static final int FLAG_STATE_REFRESHING;
    public static final int FLAG_STATE_SUCCESS_END;
    public static final int FLAG_STATE_FAILED_END;分别代表刷新中，刷新成功，刷新失败
   
  
5.动态的更新条形图ChartView
