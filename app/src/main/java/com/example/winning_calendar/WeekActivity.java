package com.example.winning_calendar;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekActivity extends AppCompatActivity {

    private TextView monthTextView;
    private RecyclerView calendarRecyclerView;
    private RecyclerView timeRecyclerView;
    private CalendarAdapter calendarAdapter;
    private TimeAdapter timeAdapter;
    private List<String> weekDates;

    private List<String> eventList;

    private List<String> generateWeekDates() {
        List<String> weekDates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  // 요일을 일요일로 설정

        for (int i = 0; i < 7; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());

            Date date = calendar.getTime();
            String formattedDate = sdf.format(date);
            weekDates.add(formattedDate);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return weekDates;
    }

    private TimeAdapter createTimeAdapter() {
        List<String> times = generateTimeList();
        return new TimeAdapter(times);
    }

    private List<String> generateTimeList() {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String time = String.format(Locale.getDefault(), "%02d:00", i);
            times.add(time);
        }
        return times;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        eventList = new ArrayList<>(); // 일정 추가를 위한 리스트

        monthTextView = findViewById(R.id.monthTextView);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        timeRecyclerView = findViewById(R.id.timeRecyclerView);

        GridLayout scheduleGridLayout = findViewById(R.id.scheduleGridLayout);
        LayoutInflater inflater = LayoutInflater.from(this);

        int numColumns = 7; // 요일 수
        int numRows = 8; // 시각 간격 수

        scheduleGridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 레이아웃이 완료된 후에 실행되는 부분
                int cellWidth = scheduleGridLayout.getWidth() / numColumns;

                // 셀 그리드 생성 및 설정
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numColumns; col++) {
                        LinearLayout cellLayout = (LinearLayout) inflater.inflate(R.layout.schedull_cell, scheduleGridLayout, false);
                        TextView cellTextView = cellLayout.findViewById(R.id.scheduleCellTextView);
                        cellTextView.setText(""); // 초기값은 빈 문자열로 설정

                        final int rowIndex = row;
                        final int colIndex = col;

                        cellLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String selectedTime = timeAdapter.getTimeList().get(rowIndex); // 선택한 시간
                                String selectedDate = weekDates.get(colIndex + 1); // 요일에 해당하는 날짜

                                // 일정 추가 로직을 구현한 후에는 cellTextView에 일정을 표시하도록 설정합니다.
                                String event = "일정 추가한 내용";
                                cellTextView.setText(event);
                                eventList.add(event); // 일정을 이벤트 리스트에 추가

                                Toast.makeText(WeekActivity.this, "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                        layoutParams.width = cellWidth;
                        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT; // 세로폭은 내용에 따라 자동으로 조정
                        cellLayout.setLayoutParams(layoutParams);

                        scheduleGridLayout.addView(cellLayout);
                    }
                }

                // 레이아웃 변경 감지를 위해 리스너 제거
                scheduleGridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        // 월 이름만 표시하고 연도는 제외
        SimpleDateFormat monthFormat = new SimpleDateFormat("M월", Locale.KOREAN);
        String month = monthFormat.format(Calendar.getInstance().getTime());
        monthTextView.setText(month);
        monthTextView.setGravity(Gravity.CENTER);

        // 주간 날짜 생성
        weekDates = generateWeekDates();

        // Calendar RecyclerView 설정
        GridLayoutManager calendarLayoutManager = new GridLayoutManager(this, 7);
        calendarRecyclerView.setLayoutManager(calendarLayoutManager);
        calendarAdapter = new CalendarAdapter(weekDates, new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { // 일정 추가
                String selectedDate = weekDates.get(position);
                Toast.makeText(WeekActivity.this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
        calendarRecyclerView.setAdapter(calendarAdapter);

        // 시간 목록 어댑터 생성
        timeAdapter = createTimeAdapter();

        // 시간 목록 RecyclerView 설정
        LinearLayoutManager timeLayoutManager = new LinearLayoutManager(this);
        timeRecyclerView.setLayoutManager(timeLayoutManager);
        timeRecyclerView.setAdapter(timeAdapter);
    }

    private static class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

        private List<String> dates;
        private OnItemClickListener listener;

        public CalendarAdapter(List<String> dates, OnItemClickListener listener) {
            this.dates = dates;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);
            return new ViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String date = dates.get(position);
            holder.dateTextView.setText(date);
            holder.dateTextView.setPadding(8, 0, 0, 0); // 상단 맨 왼쪽 공백 추가
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView dateTextView;
            OnItemClickListener listener;

            public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
                super(itemView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                this.listener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }

    private static class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

        private List<String> times;

        public TimeAdapter(List<String> times) {
            this.times = times;
        }

        public List<String> getTimeList() {
            return times;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_cell, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String time = times.get(position);
            holder.timeTextView.setText(time);
            holder.timeTextView.setPadding(8, 0, 0, 0); // 상단 맨 왼쪽 공백 추가
        }

        @Override
        public int getItemCount() {
            return times.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView timeTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                timeTextView = itemView.findViewById(R.id.timeTextView);
            }
        }
    }
    public void onBackPressed() {

        super.onBackPressed();

    }

}