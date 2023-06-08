package com.example.winning_calendar;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton mBtn;
    private DateAttr mDate;
    private MenuItem mItem;
    private FloatingActionButton fab;
    private long lastTimeBackPressed;




    CalendarLayout mLayout;
    CalendarView mView;
    DayView mDayView;


    //수정 1
    enum state {
        CALENDAER,
        ADDDATE,
        CHANGEDATE
    }

    state mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDate = new DateAttr(0);
        DateEventManager mngr = DateEventManager.getInstance();
        mngr.init(this);

        mState = state.CALENDAER;

        mngr.LoadingC(mngr);

        mLayout = findViewById(R.id.CalendarLayout);
        mView = findViewById(R.id.CalendarView);
        mDayView = findViewById(R.id.DayView);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // 전체화면인 DrawerLayout 객체 참조
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Drawer 화면(뷰) 객체 참조
        final View drawerView = (View) findViewById(R.id.drawer);


        fab = findViewById(R.id.fab);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ham_btn);
        TextView tv = new TextView(getApplicationContext());
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        tv.setLayoutParams(lp);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        tv.setTextColor(Color.BLACK);
        tv.setText("액션바 텍스트");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);

        actionBar.setDisplayHomeAsUpEnabled(true);

        mDayView.setListener(new DayView.OnItemClickListener() {
            @Override
            public void onClickedItem(DateEvent event) {




                DateAttr start = event.getStart();
                DateAttr end = event.getEnd();

                String startTime = getDateAttrStr(start);
                String endTime = getDateAttrStr(end);



                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // 다이얼로그의 제목과 메시지를 설정합니다.
                builder.setTitle(event.getTitle())
                        .setMessage(startTime + " ~ " + endTime +  "\n\n" + event.getContent());

                // OK 버튼을 추가하고 클릭 이벤트를 설정합니다.
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 수정 버튼이 클릭되면 실행될 코드를 여기에 작성합니다.
                    }
                });



                builder.setNegativeButton("수정", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                mLayout.setEnabled(false);
                mState = state.CHANGEDATE;
                mBtn.setEnabled(false);


                ActionBar actionBar = getSupportActionBar();
                actionBar.setHomeAsUpIndicator(R.drawable.backarrow_foreground);
                TextView tv = new TextView(getApplicationContext());
                ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                tv.setLayoutParams(lp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                tv.setTextColor(Color.BLACK);
                tv.setText("일정 수정");

                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(tv);

                actionBar.setDisplayHomeAsUpEnabled(true);


                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                        .addToBackStack(null)
                        .replace(R.id.contents, FragmentTodo.newInstance(event))
                        .commit();


                    }
                });

                // 다이얼로그를 생성하고 보여줍니다.
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        mBtn = (FloatingActionButton) findViewById(R.id.fab);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setEnabled(false);
                mState = state.ADDDATE;
                mBtn.setEnabled(false);

                ActionBar actionBar = getSupportActionBar();
                actionBar.setHomeAsUpIndicator(R.drawable.backarrow_foreground);
                TextView tv = new TextView(getApplicationContext());
                ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                tv.setLayoutParams(lp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                tv.setTextColor(Color.BLACK);
                tv.setText("일정 추가");


                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(tv);

                actionBar.setDisplayHomeAsUpEnabled(true);


                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.enter, R.anim.exit)
                        .addToBackStack(null)
                        .replace(R.id.contents, FragmentTodo.newInstance(null))
                        .commit();
            }
        });

        CalendarView view = (CalendarView) findViewById(R.id.CalendarView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Close the drawer
                drawerLayout.closeDrawer(GravityCompat.START);

                // Handle navigation view item clicks here
                switch (menuItem.getItemId()) {
                    case R.id.menu1:
                        // Handle the click on the "주간" menu item
                        Intent intent1 = new Intent(getApplicationContext(), WeekMainActivity.class);
                        startActivity(intent1);
                        return true;

                    case R.id.menu2:
                        // Handle the click on the "월간" menu item
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent2);
                        return true;

                    case R.id.menu3:
                        // Handle the click on the "대학시간표" menu item
                        Intent intent3 = new Intent(getApplicationContext(), ScheduleView.class);
                        startActivity(intent3);


                        return true;

                    default:
                        return false;
                }
            }
        });
        view.setListener(new CalendarView.CalendarMonthListener() {
            @Override
            public void changeMonth(DateAttr date) {
                mDate.copyTo(date);


                ActionBar actionBar = getSupportActionBar();
                actionBar.setHomeAsUpIndicator(R.drawable.ham_btn);
                TextView tv = new TextView(getApplicationContext());
                ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                tv.setLayoutParams(lp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                tv.setTextColor(Color.BLACK);
                tv.setText((date.getMonth()) + "월");

                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                actionBar.setCustomView(tv);

                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        });
    }
    public FloatingActionButton getFab() {
        return fab;
    }

    public void disableFragment() {
        mState = state.CALENDAER;
        mLayout.setEnabled(true);
        mBtn.setEnabled(true);
        mItem.setTitle("오늘");



        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ham_btn);
        TextView tv = new TextView(getApplicationContext());
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        tv.setLayoutParams(lp);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        tv.setTextColor(Color.BLACK);
        tv.setText(mDate.getMonth() + "월");

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);

        actionBar.setDisplayHomeAsUpEnabled(true);



        mView.invalidate();
        mDayView.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mItem = menu.findItem(R.id.action_btn01);

        if (mState == state.CALENDAER) {
            mItem.setTitle("오늘");
            mItem.setIcon(R.drawable.search);
        } else if (mState == state.ADDDATE) {
            mItem.setTitle("추가");
            mItem.setIcon(R.drawable.check);
        } else if (mState == state.CHANGEDATE) {
            mItem.setTitle("수정");
            mItem.setIcon(R.drawable.change);
        }



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (mState == state.CALENDAER) {
            if (id == R.id.action_btn01) {
                Date today = new Date();
                int year = today.getYear() + 1900;
                int month = today.getMonth() + 1;
                int date = today.getDate();
                mView.setDate(new DateAttr(year, month, date, 0, 0));
                mDayView.boolToday = true;
                mDayView.showToday(mDayView.boolToday);

                return true;
            }


        }

        if (id == android.R.id.home) {


            if (isFragmentActive()) {
                return false;
            }

            // 전체화면인 DrawerLayout 객체 참조
            //final
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);


            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);

    }
    private boolean isFragmentActive() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                return true;
            }
        }
        return false;
    }

    public String getTimeStr(int time){
        String timeStr = time + "";
        if (timeStr.length() == 1) {
            timeStr = "0" + timeStr;
        }

        return timeStr;
    }

    public String getDateAttrStr(DateAttr date){
        String month = getTimeStr(date.getMonth());
        String day = getTimeStr(date.getDay());
        String hour = getTimeStr(date.getHour());
        String minute = getTimeStr(date.getMinute());
        return hour + "시 " + minute + "분";

        /*
        return date.getYear() + "년 " + month + "월 " + day + "일\n"
                + hour + " : " + minute;

         */
    }

@Override
public void onBackPressed() {

    //프래그먼트 onBackPressedListener사용
    List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
    for(Fragment fragment : fragmentList){
        if(fragment instanceof onBackPressedListener){
            ((onBackPressedListener)fragment).onBackPressed();
            return;
        }
    }

    DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.closeDrawer(GravityCompat.START);
    } else {
        super.onBackPressed();
        /*
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("앱을 종료하시겠습니까?");


        alert.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });


        alert.setTitle("종료");
        AlertDialog alerts = alert.create();
        alerts.show();

         */
    }

}
}



