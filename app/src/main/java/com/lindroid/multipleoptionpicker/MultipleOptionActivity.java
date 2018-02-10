package com.lindroid.multipleoptionpicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Lin
 */
public class MultipleOptionActivity extends AppCompatActivity {

    private OptionsPickerView multipleOp;
    private List<String> monthList = new ArrayList<>();
    private List<String> optionYears = new ArrayList<>();
    private List<List<String>> optionMonths = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_option);
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
    }


    public void showPickerView(View view) {

    }
}
