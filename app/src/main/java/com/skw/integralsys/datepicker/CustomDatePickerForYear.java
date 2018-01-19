package com.skw.integralsys.datepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skw.integralsys.R;
import com.skw.integralsys.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @创建人 wangtao
 * @创建时间 15/10/09 16:23
 * @类描述 日期弹窗(整年)
 */
public class CustomDatePickerForYear extends LinearLayout {

    private WheelVerticalViewForYear year;

    private WheelVerticalViewForYear month;

    private WheelVerticalViewForYear day;

    private ChangingListener listener;

    private ArrayWheelAdapter adapter1;

    private ArrayWheelAdapter adapter2;

    private ArrayWheelAdapter adapter3;

    private Context context;

    private Calendar select_date;

    private Calendar now_date;

    private int currentYear;

    private int currentMonth;

    private int currentDay;

    private ArrayList<String> yearlist = new ArrayList<String>();

    private ArrayList<String> monthlist = new ArrayList<String>();

    private ArrayList<String> yearListTemp = new ArrayList<String>();

    private ArrayList<String> monthListTemp = new ArrayList<String>();

    private ArrayList<String> chineseYearList = new ArrayList<>();

    private ArrayList<String> chineseMonthList = new ArrayList<>();

    private ArrayList<String> chineseDayList = new ArrayList<>();

    private int gongLiYear;

    private int gongLiMonth;

    private int chineseDaySum;

    public static int isGongli = 0;                        // 0:公历
    // 1:农历

    public CustomDatePickerForYear(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        isGongli = 0;
        View v = LayoutInflater.from(context).inflate(R.layout.custom_datepicker_for_year, null);
        year = (WheelVerticalViewForYear) v.findViewById(R.id.year);
        month = (WheelVerticalViewForYear) v.findViewById(R.id.month);
        day = (WheelVerticalViewForYear) v.findViewById(R.id.day);

        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);

        for (int i = 1901; i < 2100; i++) {

            /** 农历数据 **/
            int sy = (i - 4) % 12;
            CalendarUtil cu = new CalendarUtil();
            cu.setGregorianYear(i);
            cu.computeChineseFields();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(cu.getChineseYear());
            stringBuilder.append("年");
            chineseYearList.add(stringBuilder.toString());
            yearListTemp.add(i + "");
            /** 公历数据 **/
            yearlist.add(i + "年");
        }

        for (int i = 1; i <= 12; i++) {

            /** 农历数据 **/
            CalendarUtil cu = new CalendarUtil();
            cu.setGregorianMonth(i);
            cu.computeChineseFields();
            chineseMonthList.add(cu.monthOfAlmanac[cu.getChineseMonth() - 1]);
            monthListTemp.add(i + "");
            /** 公历数据 **/
            monthlist.add(i + "月");
        }

        setDate(Calendar.getInstance());

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                select_date = getDate();

            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {

                if (now_date != null) {
                    Calendar date = getDate();
                    int compareTo = date.compareTo(now_date);

                    if (compareTo < 0) {
                        setDate(select_date);

                        Toast.makeText(getContext(), "不能选择此日期", Toast.LENGTH_SHORT).show();

                    }

                }

                setDayAdapter();

            }
        };

        OnWheelScrollListener scrollListener1 = new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                select_date = getDate();

            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {

                if (now_date != null) {
                    Calendar date = getDate();
                    int compareTo = date.compareTo(now_date);

                    if (compareTo < 0) {
                        setDate(select_date);
                        Toast.makeText(getContext(), "不能选择此日期", Toast.LENGTH_SHORT).show();
                    }

                }
                doListener();

            }
        };
        month.addScrollingListener(scrollListener);
        year.addScrollingListener(scrollListener);
        day.addScrollingListener(scrollListener1);

        addView(v);

    }

    /**
     * 公历数据
     **/
    private List getDaylist(int num) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i <= num; i++) {
            String d = i + "日";
            if (i < 10) {
                d = "\t" + i + "日";
            }
            list.add(d);
        }
        return list;

    }

    /**
     * 农历数据
     **/
    private List getChineseDayList(int num) {
        CalendarUtil cu = new CalendarUtil();
//		for (int i = 0; i < yearListTemp.size(); i++) { // 公历年
//			gongLiYear = Integer.valueOf(yearListTemp.get(i));
//			for (int j = 0; j < monthListTemp.size(); j++) { // 公历月
//				gongLiMonth = Integer.valueOf(monthListTemp.get(j));
        for (int m = 1; m <= num; m++) { // 公历日
//					cu.setGregorian(gongLiYear, gongLiMonth, m);
//					cu.setGregorianYear(gongLiYear);
//					cu.setGregorianMonth(gongLiMonth);
//					cu.computeChineseFields();
            cu.setGregorianDate(m);
            cu.computeChineseFields();
            cu.computeSolarTerms();
            int cd = cu.getChineseDate();
            chineseDayList.add(cu.daysOfAlmanac[cd - 1]); // 农历日List
        }
//			}
//		}
        return chineseDayList;
    }

    private void doListener() {
        if (listener != null) {
            listener.onChange(getDate());
        }

    }

    public interface ChangingListener {

        void onChange(Calendar c);
    }

    public void setDayAdapter() {
        int currentItem = year.getCurrentItem();
        int current = currentItem + 1901;
        int newValue = month.getCurrentItem() + 1;

        switch (newValue) { // 展示日
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (isGongli == 0) {
                    adapter3 = new ArrayWheelAdapter(context, getDaylist(31).toArray());
                } else {
                    adapter3 = new ArrayWheelAdapter(context, getChineseDayList(31).toArray());
                }

                break;
            case 2:
                if (current % 4 == 0) {
                    if (isGongli == 0) {
                        adapter3 = new ArrayWheelAdapter(context, getDaylist(29).toArray());
                    } else {
                        adapter3 = new ArrayWheelAdapter(context, getChineseDayList(29).toArray());
                    }
                } else {
                    if (isGongli == 0) {
                        adapter3 = new ArrayWheelAdapter(context, getDaylist(28).toArray());
                    } else {
                        adapter3 = new ArrayWheelAdapter(context, getChineseDayList(28).toArray());
                    }
                }

                break;

            case 4:
            case 6:
            case 9:
            case 11:
                if (isGongli == 0) {
                    adapter3 = new ArrayWheelAdapter(context, getDaylist(30).toArray());
                } else {
                    adapter3 = new ArrayWheelAdapter(context, getChineseDayList(30).toArray());
                }
                break;
            default:
                break;
        }

        if (isGongli == 0) { // 展示年
            adapter1 = new ArrayWheelAdapter(context, yearlist.toArray());
        } else {
            adapter1 = new ArrayWheelAdapter(context, chineseYearList.toArray());
        }

        if (isGongli == 0) { // 展示月
            adapter2 = new ArrayWheelAdapter(context, monthlist.toArray());
        } else {
            adapter2 = new ArrayWheelAdapter(context, chineseMonthList.toArray());
        }

        setTextColor(adapter1);
        setTextColor(adapter2);
        setTextColor(adapter3);
        year.setViewAdapter(adapter1);
        month.setViewAdapter(adapter2);
        day.setViewAdapter(adapter3);
        year.setCyclic(true);
        month.setCyclic(true);
        day.setCyclic(true);
        doListener();
    }

    /**
     * 设置初始日期
     *
     * @param c
     */
    public void setDate(Calendar c) {
        int y = c.get(Calendar.YEAR);
        int index1 = y - 1900;
        year.setCurrentItem(index1 - 1);
        int m = c.get(Calendar.MONTH);
        month.setCurrentItem(m);
        int d = c.get(Calendar.DAY_OF_MONTH);
        day.setCurrentItem(d - 1);
        setDayAdapter();
    }

    /**
     * 设置字体颜色
     *
     * @param adapter
     */
    public void setTextColor(AbstractWheelTextAdapter adapter) {
        adapter.setTextColor(context.getResources().getColor(R.color.color_727272));
    }

    /**
     * 设置限制日期，设置后，不能选择设置的开始日期以前的日期
     *
     * @param c
     */
    public void setNowData(Calendar c) {
        now_date = c;
    }

    /**
     * 得到日期
     *
     * @return
     */
    public Calendar getDate() {
        int y = year.getCurrentItem();
        int index1 = y + 1901;
        int m = month.getCurrentItem();
        int d = day.getCurrentItem();
        Calendar c = Calendar.getInstance();
        c.set(index1, m, d + 1);

        return c;
    }

    /**
     * 设置日期改变的监听
     *
     * @param listener
     */
    public void addChangingListener(ChangingListener listener) {
        this.listener = listener;

    }

}
