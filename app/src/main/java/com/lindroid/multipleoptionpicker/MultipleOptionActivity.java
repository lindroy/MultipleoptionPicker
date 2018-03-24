package com.lindroid.multipleoptionpicker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Lin
 */
public class MultipleOptionActivity extends AppCompatActivity implements OptionsPickerView.OnOptionsSelectListener {

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

       /* OptionsPickerView op = createBuilder().build();
        op.setPicker(数据1,数据2);
        op.show();*/
    }

    /**
     * 比較開始時候和結束時間
     *
     * @param startTime
     * @param endTime
     * @return true表示時間正確
     */
    public boolean compareStartEndTime(String startTime, String endTime) {
        boolean isTimeCorrect;
        if (TextUtils.equals("1990以前", startTime)
                || TextUtils.equals("至今", endTime)) {
            //开始时间是1990以前，或者结束时间是至今，则时间绝对正确
            isTimeCorrect = true;
        } else if (TextUtils.equals("至今", startTime)
                && !TextUtils.equals("至今", endTime)) {
            //开始时间是至今，而结束时间不是至今，则时间有错
            isTimeCorrect = false;
        } else if (!TextUtils.equals("1990以前", startTime)
                && TextUtils.equals("1990以前", endTime)) {
            //开始时间不是1990以前，结束时间是1990以前，则时间有错
            isTimeCorrect = false;
        } else {
            String[] startTimeArray = startTime.split("\\.");
            String[] endTimeArray = endTime.split("\\.");
            if (startTimeArray[0].equals(endTimeArray[0])) {
                //年份相同，比较月份
                if (Integer.valueOf(startTimeArray[1]) <= Integer.valueOf(endTimeArray[1])) {
                    isTimeCorrect = true;
                } else {
                    isTimeCorrect = false;
                }
            } else if (Integer.valueOf(startTimeArray[0]) < Integer.valueOf(endTimeArray[0])) {
                isTimeCorrect = true;
            } else {
                isTimeCorrect = false;
            }
        }
        return isTimeCorrect;
    }

    /**
     * 设置滚轮样式
     *
     * @return
     */
    private OptionsPickerView.Builder createBuilder() {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(MultipleOptionActivity.this, this)
                .setBgColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setSubmitText("确定")
                .setCancelText("取消");
        //下面可以继续设置样式
        return builder;
    }

    /**
     * 滚轮的监听事件
     *
     * @param options1
     * @param options2
     * @param options3
     * @param v
     */
    @Override
    public void onOptionsSelect(int options1, int options2, int options3, View v) {
        switch (v.getId()) {
            //根据所点击的控件Id来区分点击事件
            case R.id.btn_show:
                break;
            default:
                break;
        }
    }
}
