# RTextView


## 欢迎使用 RTextView 

### 特点

> 1. RTextView 让你从此不再编写和管理大量 selector 文件（这个太可恨了）
> 
> 2. RTextView 改造了 drawableLeft/drawableXXX 图片的大小，从此你不在需要使用 LinearLayout + ImageView + TextView 就能够直接实现文字带图片的功能，关键还能设置icon大小
> 
> 3. RTextView 能够直接设置各种圆角效果： 四周圆角，某一方向圆角，甚至椭圆，圆形都简单实现。 边框效果，虚线边框都是一个属性搞定
> 
> 4. RTextView 不仅能够定义默认状态的背景，边框，连按下/点击状态通通一起搞定
> 
> 5. RTextView 按下变色支持：背景色，边框，文字，drawableLeft/xxx （这个赞啊）



### 话不多说直接上图
![](corner.png)   ![](state.gif)


> 如何，有没有一个控件搞定所有布局的既视感？
> 
> 是不是常用的TextView的功能都在这里了？

###属性说明

| 属性			|说明			 |
| ------------- |  :-------------|
| corner_radius      			|	圆角		四周		|
| corner_radius_top_left        |   圆角		左上		|
| corner_radius_top_right 		|	圆角		右上		|
| corner_radius_bottom_left 	|   圆角		左下		|
| corner_radius_bottom_right 	|   圆角		右下 	|
| border_dash_width 			|   虚线边框 	宽度 	|
| border_dash_gap 				|   虚线边框 	间隔 	|
| border_width_normal 			|   边框宽度 	默认 	|
| border_width_pressed 			|   边框宽度 	按下 	|
| border_width_unable 			|   边框宽度 	不可点击 |
| border_color_normal 			|   边框颜色 	默认		|
| border_color_pressed 			|   边框颜色 	按下 	|
| border_color_unable 			|   边框颜色 	不可点击 |
| background_normal 			|   背景颜色 	默认 	|
| background_pressed 			|   背景颜色 	按下 	|
| background_unable 			|   背景颜色  	不可点击 |
| text_color_normal 			|   文字颜色 	默认 	|
| text_color_pressed      		|   文字颜色 	按下 	|
| text_color_unable      		| 	文字颜色 	不可点击 |
| icon_src_normal      			|   drawable icon 	默认 		|
| icon_src_pressed      		|   drawable icon 	按下 		|
| icon_src_unable      			| 	drawable icon 	不可点击 	|
| icon_height      				| 	drawable icon 	高 			|
| icon_width      				|   drawable icon 	宽 			|
| icon_direction      			|   drawable icon 	位置{left,top,right,bottom} |

###XML使用
    <com.ruffian.library.RTextView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:gravity="center"
        android:text="文本控件"

        //背景颜色 对应三个状态
        rtv:background_normal="#3F51B5"
        rtv:background_pressed="#FF450F21"
        rtv:background_unable="#c3c3c3"

        //边框颜色 对应三个状态
        rtv:border_color_normal="#FF4081"
        rtv:border_color_pressed="#3F51B5"
        rtv:border_color_unable="#c3c3c3"

        //边框宽度 对应三个状态 一般设置相同值
        rtv:border_width_normal="3dp"
        rtv:border_width_pressed="4dp"
        rtv:border_width_unable="5dp"

        //虚线边框 1.虚线边框宽度 2.虚线间隔
        rtv:border_dash_width="10dp"
        rtv:border_dash_gap="4dp"

        //圆角度数 1.四周统一值 2.四个方向各个值
        //xml:  通过xml 设置了 corner_radius ，则 corner_radius_xxx 不起作用
        //java: 通过java代码设置 corner_radius_xxx ，则 corner_radius 不起作用
        rtv:corner_radius="10dp"
        rtv:corner_radius_top_left="10dp"
        rtv:corner_radius_bottom_left="15dp"
        rtv:corner_radius_bottom_right="20dp"
        rtv:corner_radius_top_right="25dp"

        //drawableXXX icon 对应三个状态
        rtv:icon_src_normal="@mipmap/icon_phone_normal"
        rtv:icon_src_pressed="@mipmap/icon_phone_pressed"
        rtv:icon_src_unable="@mipmap/icon_phone_unable"

        //drawableXXX icon 方向 {上,下,左,右}
        rtv:icon_direction="top"

        //drawableXXX icon 宽/高
        rtv:icon_height="30dp"
        rtv:icon_width="30dp"

        //文字颜色 对应三个状态
        rtv:text_color_normal="#c3c3c3"
        rtv:text_color_pressed="#3F51B5"
        rtv:text_color_unable="#FF4081"
        />


### 以上属性均提供Java代码 set/get 方法
        RTextView textView=(RTextView) findViewById(R.id.text1);
        //set...
        textView.setIconNormal(getDrawable(R.mipmap.ic_launcher))
                .setIconHeight(10)
                .setIconWidth(20)
                .setIconDirection(RTextView.ICON_DIR_TOP);
        //get...
        int iconHeight=textView.getIconHeight();


###开发者们，根据你们的需求尽情去撸码吧，使用过程中遇到什么问题请大方的提个Issues，觉得实用的话就star 以便及时获取最新版本

###BLOG：[http://blog.csdn.net/u014702653?viewmode=contents](http://blog.csdn.net/u014702653?viewmode=contents)
