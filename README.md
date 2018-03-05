# Android之文本日期混合滚轮选择器（仿Boss直聘）
## 1、需求分析
GitHub上面有一款iOS风格的滚轮选择器[Android-PickerView](https://github.com/Bigkoo/Android-PickerView "Android-PickerView")，它分为时间选择器代码`TimePickerView`和选项选择器`OptionsPickerView`，不但可以选择时间日期，可以选择我们自定义的数据，比如性别、年龄等。我一直都用它。直到最近遇到了一个需求，它的选项里面既有文字也有时间，大体效果如Boss直聘添加项目经验中的时间选择功能：

![至今](https://raw.githubusercontent.com/Lindroy/MultipleoptionPicker/master/%E8%87%B3%E4%BB%8A.jpg)

![1900以前](https://raw.githubusercontent.com/Lindroy/MultipleoptionPicker/master/1900%E4%BB%A5%E5%89%8D.jpg)

从图中我们可以看出，除了常规的年份和月份的选择，选项中还包含了文本。其中，最新的时间是“至今”，而最早可供选择的时间则是“1900以前”。所以看起来似乎`TimePickerView`和`OptionsPickerView`都无法实现这个功能。我们都沮丧地认为这下要么得自定义控件，要么得修改**Android-PickerView**这个库了。但我转念一想，为什么要把“时间选择”和“选项选择”分得那么开呢？时间选择其实也是选项选择的一种嘛。比如我要选择2017年12月，那就是从年份中选择2017，从月份中选择12。只要设置好一级选项和二级选项就可以了。

## 2、选项结构分析

有了思路之后，我们来分析一下选项的数据结构。年份可以分为3种情况：
1. 最新年份，其实也是最新的时间：“至今”；
2. 常规的年份：1990~当前年份（2018）；
3. 最早的年份，也就是最早的时间：“1990以前”。

我在Boss直聘的基础上加了一些限制：当前年份下对应的可供选择的月份范围只能是从月到当前月份，比如现在是2018年2月，那么选好年份为2018后，月份就只能选择1和2。这样一来，月份就有四种情况了：
1. 最新月份：“至今”；
2. 当前年份下对应的月份范围：1~当前月份；
3. 完整的月份，即1~12；
4. 最早月份：“1990以前”。

可以总结为如下的表格：

|年份   |  月份 |
| :------------: | :------------: |
| 最新年份“至今”  |  最新年份“至今”   |
|  当前年份 | 1~当前月份  |
| 1990~当前年份-1  | 月份1~12  |
| 最早年份“1990以前”  | 最早月份“1990以前”  |

## 3、书写代码
在开始写代码之前，我建议你先去GitHub上看看[Android-PickerView](https://github.com/Bigkoo/Android-PickerView "Android-PickerView")的使用用法，它使用了构造者模式，用起来很简单。

现在，就开始写我们的代码了。
### 3.1 界面布局
布局就是一个按钮，点击后弹出滚轮选择器，选好后点击确认即将数据在TextView上显示出来。

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:textAllCaps="false"
        android:text="显示PickerView"
        android:onClick="showPickerView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/tv_time"
        android:textSize="16sp"
        android:gravity="center"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</LinearLayout>
```

### 3.2 Activity代码

借助强大的**PickerView**，我们实现起来很简单，请看如下的代码：

```java
public class MultipleOptionActivity extends AppCompatActivity {

    private TextView tvTime;

    /**
     * 完整的月份数据1~12
     */
    private List<String> monthList = new ArrayList<>();
    /**
     * 滚轮选择器中年份的选项数据
     */
    private List<String> optionYears = new ArrayList<>();
    /**
     * 滚轮选择器中月份的选项数据
     */
    private List<List<String>> optionMonths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_option);
        tvTime = (TextView) findViewById(R.id.tv_time);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //设置完整的月份数据，即1~12
        for (int i = 1; i <= 12; i++) {
            monthList.add(String.valueOf(i));
        }
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        //月份获取到的数据是0~11，所以要加1
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        for (int i = curYear + 1; i >= 1989; i--) {
            //对应年份的月份数据集合
            List<String> tempMonths = new ArrayList<>();
            if (i == curYear + 1) {
                //设置最新时间“至今”
                optionYears.add("至今");
                tempMonths.add("至今");
                optionMonths.add(tempMonths);
            } else if (i == curYear) {
                //设置当前年份及其对应的月份
                optionYears.add(String.valueOf(i));
                for (int j = 1; j <= curMonth; j++) {
                    tempMonths.add(String.valueOf(j));
                }
                optionMonths.add(tempMonths);
            } else if (i == 1989) {
                //设置最早时间“1900以前”
                optionYears.add("1990以前");
                tempMonths.add("1990以前");
                optionMonths.add(tempMonths);
            } else {
                //设置常规时间
                optionYears.add(String.valueOf(i));
                optionMonths.add(monthList);
            }
        }
    }
    /**
     * 显示滚轮
     *
     * @param view
     */
    public void showPickerView(View view) {
        OptionsPickerView multipleOp = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1 == 0 || options1 == optionYears.size() - 1) {
                    //选中最新和最早时间时直接显示文字，不需要拼接月份
                    tvTime.setText(optionYears.get(options1));
                } else {
                    //常规的时间，需要拼接年份和月份
                    tvTime.setText(new StringBuffer(optionYears.get(options1)).append("—").append(monthList.get(options2)));
                }
            }
        }).setTitleText("请选择时间")
                .build();
        multipleOp.setPicker(optionYears, optionMonths);
        multipleOp.show();
    }
}
```

代码很少，注释我也写得很清楚了，相信大家很容易理解。我们重点关注`OptionsPickerView`的`setPicker`方法，它可以传入三个参数，每个参数都是集合，但每个参数的类型都不同。第一个参数是`List`，第二个参数是`List<List>`，第三个参数是`List<List<list>>`。看到这里你就明白了，我们每个年份对应的月份数据就是一个集合（当然，集合大小不相同），比如年份2017，对应的月份就是有着12个元素的集合。理清楚这一点之后，也就理解`initData`方法里面对数据的设置了。

最后在TextView中显示数据时自然也要分类了，对于“至今”和“1990以前”我们至今显示文本，其他的再拼接一下，看起来像是时间就行了。

看看我们最后实现的效果图：
![最终效果图](https://raw.githubusercontent.com/Lindroy/MultipleoptionPicker/master/%E6%96%87%E5%AD%97%E6%97%A5%E6%9C%9F%E6%B7%B7%E5%90%88%E6%BB%9A%E8%BD%AE%E9%80%89%E6%8B%A9%E6%A1%86.gif)

## 4、总结

在项目中使用一些好的第三方库是可以大大节省我们的开发时间的，但是在使用过程中也要灵活一点。比如我们在一个页面中需要多次用到滚轮选择器（比如选择开始时间和结束时间），那么每次都要设置一遍滚轮的样式和写一次点击事件也太麻烦了。这时，我们就可以将滚轮样式的设置代码抽取出来：

```java
    /**
     * 设置滚轮样式
     * @return
     */
    private OptionsPickerView.Builder createBuilder(){
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(MultipleOptionActivity.this,this)
                .setBgColor(ContextCompat.getColor(this,R.color.colorAccent))
                .setSubmitText("确定")
                .setCancelText("取消");
        //下面可以继续设置样式
        return builder;
    }
```

然后显示滚轮的时候只要这样写：

```java
        OptionsPickerView op = createBuilder().build();
        op.setPicker(数据1,数据2);
        op.show();
```

点击事件也可以封装起来，让我们的Activity继承`OptionsPickerView.OnOptionsSelectListener`，然后实现点击事件：

```java
    /**
     * 滚轮的监听事件
     * @param options1
     * @param options2
     * @param options3
     * @param v
     */
    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        switch (v.getId()){
            //根据所点击的控件Id来区分点击事件
            case R.id.btn_show:
                break;
            default:
                break;
        }
    }
```

那么`OptionsPickerView`怎么获取到点击View的id的呢？我们在调用`show`方法的时候传入点击View的对象就可以了。以上是我个人的一点心得，希望对大家有所帮助。

最后给一下源码吧：[源码](https://github.com/Lindroy/MultipleoptionPicker)





