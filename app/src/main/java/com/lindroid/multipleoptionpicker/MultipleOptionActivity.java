package com.lindroid.multipleoptionpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Lin
 */
public class MultipleOptionActivity extends AppCompatActivity {

    private TextView tvTime;

    private List<String> monthList = new ArrayList<>();
    private List<String> optionYears = new ArrayList<>();
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
                    //
                    tvTime.setText(new StringBuffer(optionYears.get(options1)).append("—").append(monthList.get(options2)));
                }
            }
        }).setTitleText("请选择时间")
                .build();
        multipleOp.setPicker(optionYears, optionMonths);
        multipleOp.show();
    }
}
