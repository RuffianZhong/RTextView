# RTextView


## 欢迎使用 RTextView 

## 强势上线升级版，针对安卓基础控件的封装 [RWidgetHelper](https://github.com/RuffianZhong/RWidgetHelper) 

### 1.特点

> 1. RTextView 让你从此不再编写和管理大量 selector 文件（这个太可恨了）
> 
> 2. RTextView 改造了 drawableLeft/drawableXXX 图片的大小，从此你不在需要使用 LinearLayout + ImageView + TextView 就能够直接实现文字带图片的功能，关键还能设置icon大小
> 
> 3. RTextView 能够直接设置各种圆角效果： 四周圆角，某一方向圆角，甚至椭圆，圆形都简单实现。 边框效果，虚线边框都是一个属性搞定
> 
> 4. RTextView 不仅能够定义默认状态的背景，边框，连按下/点击状态通通一起搞定
> 
> 5. RTextView 按下变色支持：背景色，边框，文字，drawableLeft/xxx （这个赞啊）



### 2.效果图

> 示例效果图，不限于如此，更多使用详细参考相关属性

![](corner.png)   ![](state.gif)


`V1.0.1` Typeface 字体样式

![](version_typeface.png)


### 3.属性说明

> 开发者根据实际需要选择使用对应的功能属性



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
| text_typeface      			|   字体样式 |

### 4.使用
> ### 4.1  Gradle （版本号根据更新历史使用最新版）

    compile 'com.ruffian.library:RTextView:1.0.11'

> ### 4.2 XML使用

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

        //字体样式
        rtv:text_typeface="fonts/RobotoMono-Thin.ttf"
        />


> ### 4.3 以上属性均提供Java代码 get/set 方法

    RTextView textView=(RTextView) findViewById(R.id.text1);
        //set...
        textView.setIconNormal(getDrawable(R.mipmap.ic_launcher))
                .setIconHeight(10)
                .setIconWidth(20)
                .setIconDirection(RTextView.ICON_DIR_TOP);
        //get...
        int iconHeight=textView.getIconHeight();


> ### 4.4 备注

	1. 圆角边框，圆角背景等属性需要配合 `background_xxx` 自定义背景属性使用，原生 `background` 没有效果

### 5.版本历史

**v1.0.11**　`2018.08.20`　优化 `onTouchEvent` 方法

**v1.0.10**　`2018.06.20`　修改BUG [issues #9](https://github.com/RuffianZhong/RTextView/issues/9)

**v1.0.9**　`2018.04.13`　修复原生属性 `background` 为纯颜色时没效果bug

**v1.0.8**　`2018.04.09`　兼容5.0以下 `VectorDrawable` (矢量图)  [issues #8](https://github.com/RuffianZhong/RTextView/issues/8)

**v1.0.7**　`2018.04.04`　未设置icon宽高时，默认使用icon大小

**v1.0.6**　`2018.03.06`　修改BUG

**v1.0.4 ~ v1.0.5**　`2018.02.26`　修改BUG

**v1.0.2 ~ v1.0.3**　`2017.11.14`　修改BUG

**v1.0.1**　`2017.10.26`　添加 `Typeface`,完善代码

**v1.0.0**　`2017.09.07`　发布第一版本

## License

```
MIT License

Copyright (c) 2018 Ruffian-痞子

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


