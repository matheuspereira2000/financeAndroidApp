package com.example.android.effectivenavigation;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillingCycles extends Fragment {

    public CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    public TextView textView;
    public TextView textView2;
    public Button button1;
    public Button button2;
    public Button button3;
    public EditText editText1;
    public EditText editText2;
    public EditText editText3;
    String user_month;
    String user_day;
    String user_year;
    String full_date;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.billing_cycles_fragment, container, false);

        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        textView = (TextView) view.findViewById(R.id.billing2textview1);
        textView2 = (TextView) view.findViewById(R.id.billingTextView);

        textView.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        button1 = (Button) view.findViewById(R.id.billing_button1);
        button2 = (Button) view.findViewById(R.id.billing_button2);
        button3 = (Button) view.findViewById(R.id.billing_button3);

        editText1 = (EditText) view.findViewById(R.id.billing_editText1);
        editText2 = (EditText) view.findViewById(R.id.billing_editText2);
        editText3 = (EditText) view.findViewById(R.id.billing_editText3);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if (dateClicked != null) {
                    textView2.setText(dateClicked.toString());
                    //Log.d("Tag", dateClicked.toString());
                } else {
                    textView2.setText("Nothing added this day. ");
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText1.getText().toString().isEmpty() ||
                        editText2.getText().toString().isEmpty() ||
                        editText3.getText().toString().isEmpty())
                {
                    textView2.setText("Please enter all entries. ");
                    return;
                }

                user_month = editText1.getText().toString();
                user_day = editText2.getText().toString();
                int correct_day = (Integer.parseInt(user_day)+1);
                String final_day = String.valueOf(correct_day);
                user_year = editText3.getText().toString();

                Log.d("Month", user_month);
                Log.d("Day", final_day);
                Log.d("Year", user_year);

                full_date = user_month + " " + correct_day + " " + user_year + " 00:00:00.000 UTC";
                Log.d("Full Date: ", full_date);

                // Add the user's date to the calendar
                String str = full_date;
                SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
                Date date = null;

                try {
                    date = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long epoch = date.getTime();

                Event ev1 = new Event(Color.CYAN, epoch, "My day");
                compactCalendarView.addEvent(ev1);

            }
        });


    }



}
