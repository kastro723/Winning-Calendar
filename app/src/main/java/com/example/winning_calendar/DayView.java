package com.example.winning_calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class DayView extends View{
    private OverScroller mScroller;
    private float mStartX;
    private float mStartY;
    private float mAccumulatedX;
    private float mAccumulatedY;

    final static int HORIZONTAL_BOTTOM = 0;
    final static int HORIZONTAL_CENTER = 1;
    final static int HORIZONTAL_TOP = 2;

    private int mState;

    private float mBottomY;
    private float mCenterY;
    private float mTopY;

    int mYear;
    int mMonth;
    int mDate;

    ArrayList<String> mWeekStr;

    private float mBlockGap;
    private float mWidth;
    private float mHeight;
    private float mTextSize;
    private float mBlockSize;
    private float mTitleGap;
    Paint mPaint;

    public boolean boolToday = false;

    ArrayList<DateEvent> mEvents;

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        public void onClickedItem(DateEvent event);
    }


    public void setListener(OnItemClickListener l) {
        listener = l;
    }

    public DayView(Context context) {
        super(context);
        init(context);
    }

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mScroller = new OverScroller(context, new LinearInterpolator(), 0, 0);
        mAccumulatedX = 0;
        mAccumulatedY = 0;

        mState = HORIZONTAL_BOTTOM;
        Date today = new Date();
        mYear = today.getYear() + 1900;
        mMonth = today.getMonth() + 1;
        mDate = today.getDate();

        mWeekStr = new ArrayList<String>(Arrays.asList(
                "일", "월", "화", "수", "목", "금", "토"
        ));

        CalendarView.setListener(new CalendarView.CalendarTouchListener() {
            @Override
            public void onDayClick(DateAttr dateClicked) {
                mYear = dateClicked.getYear();
                mMonth = dateClicked.getMonth();
                mDate = dateClicked.getDay();

                mState = HORIZONTAL_CENTER;
                mStartY = mCenterY;
                startScroll();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartX = getX();
        mStartY = getY();

        DateEventManager mngr = DateEventManager.getInstance();
        DateAttr dayStart = new DateAttr(mYear, mMonth, mDate, 0, 0);
        DateAttr dayEnd = new DateAttr(mYear, mMonth, mDate, 23, 59);
        mEvents = mngr.rangeCutEvent(dayStart.getDateTime(), dayEnd.getDateTime());

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mBottomY = getMeasuredHeight();
        mCenterY = getMeasuredHeight() / 2;
        mTopY = 0;
    }

    public void accumulateX(float x) {
        mAccumulatedX += x;
        if (Math.abs(mAccumulatedX) > getWidth() / 2){
            if (mAccumulatedX < 0){
            }
            else{
            }
        }
    }

    public void showToday(boolean boolToday) {
        this.boolToday = true;
        invalidate();  // 이 메소드를 호출하여 뷰를 다시 그리도록 시스템에 요청합니다.
    }


    public void accumulateY(float y) {
        mAccumulatedY += y;
        if (Math.abs(mAccumulatedY) > getHeight() / 5)
        {
            if (mAccumulatedY > 0) {
                if (mState == HORIZONTAL_BOTTOM) {
                    mStartY = mBottomY;
                }
                else if (mState == HORIZONTAL_CENTER) {
                    mState = HORIZONTAL_BOTTOM;
                    mStartY = mBottomY;
                }
                else if (mState == HORIZONTAL_TOP) {
                    mState = HORIZONTAL_CENTER;
                    mStartY = mCenterY;
                }
            }
            else {
                if (mState == HORIZONTAL_BOTTOM) {
                    mState = HORIZONTAL_CENTER;
                    mStartY = mCenterY;
                }
                else if (mState == HORIZONTAL_CENTER) {
                    mState = HORIZONTAL_TOP;
                    mStartY = mTopY;
                }
                else if (mState == HORIZONTAL_TOP) {
                    mState = HORIZONTAL_TOP;
                    mStartY = mTopY;
                }
            }

            mAccumulatedY = 0.0f;
        }
    }

    public void setStartPos(float x, float y) {
        mStartX = x;
        mStartY = y;
    }

    public void startScroll() {
        int x = (int) getX();
        int y = (int) getY();
        int wx = (int) (x - mStartX);
        int wy = (int) (y - mStartY);
        mScroller.startScroll(x, y, -wx, -wy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            setY(mScroller.getCurrY());
            Log.d("좌표 : ", mScroller.getCurrX() + " " + mScroller.getCurrY());
            invalidate();
            ViewGroup viewGroup = (ViewGroup)getParent();
            if (viewGroup != null) {
                float resize = getHeight() - (getY());
                CalendarView view = (CalendarView)viewGroup.getChildAt(0);
                if (view != null) {
                    view.setCalendarSize(1.0f, Math.max(0.5f, 1.0f - resize / getHeight()));
                    view.invalidate();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mWidth = getWidth();
        mHeight = getHeight();
        mTextSize = mHeight / 30;
        mTitleGap = mTextSize * 2;
        mBlockGap = mHeight / 50;
        mBlockSize = mHeight / 12;

        DateEventManager mngr = DateEventManager.getInstance();
        DateAttr dayStart = new DateAttr(mYear, mMonth, mDate, 0, 0);
        DateAttr dayEnd = new DateAttr(mYear, mMonth, mDate, 23, 59);
        mEvents = mngr.rangeCutEvent(dayStart.getDateTime(), dayEnd.getDateTime());

        drawDay(canvas);
        drawTodoBlock(canvas);


    }

    private void drawTodoBlock(Canvas canvas) {
        mBlockSize = Math.min(mWidth / 12, mWidth / mEvents.size());

        Collections.sort(mEvents);

        int idx = 0;
        for (DateEvent e : mEvents) {
            drawBlock(canvas, e, idx);
            idx++;
        }
    }

    private void drawBlock(Canvas canvas, DateEvent e, int idx) {

        mPaint.setColor(e.getColor());
        RectF rect = new RectF(30, mTitleGap + idx * (mBlockGap + mBlockSize), mWidth - 30,
                mTitleGap + (idx * (mBlockGap + mBlockSize) + mBlockSize));
        canvas.drawRoundRect(rect, 10, 10, mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTextSize);
        canvas.drawText(e.getDateStr() + "    " + e.getTitle() , 100 , mTitleGap + idx * (mBlockSize + mBlockGap) + mBlockSize * 2 / 3, mPaint);
    }

    private void drawDay(Canvas canvas) {
        Date myDate = new Date(mYear - 1900, mMonth - 1, 1);
        int day = myDate.getDay();
        int week = (day - 1 + mDate) % 7;
        mPaint.setTextSize(mTextSize);

        String weekly = mWeekStr.get(week);

        String month = mMonth + "";
        if (month.length() == 1) {
            month = "0" + month;
        }

        String date = mDate + "";
        if (date.length() == 1){
            date = "0" + date;
        }

        if(boolToday == true){

            Date todays = new Date();
            int mYears = todays.getYear() + 1900;
            int mMonths = todays.getMonth() + 1;
            int mDates = todays.getDate();


            Date myDates = new Date(mYears - 1900, mMonths - 1, 1);
            int days = myDates.getDay();
            int weeks = (days - 1 + mDates) % 7;

            ArrayList<String> mWeekStrs;
            mWeekStrs = new ArrayList<String>(Arrays.asList(
                    "일", "월", "화", "수", "목", "금", "토"
            ));


            String weeklys = mWeekStrs.get(weeks);


            String months = mMonths + "";
            if (months.length() == 1) {
                months = "0" + months;
            }

            String dates = mDates + "";
            if (dates.length() == 1){
                dates = "0" + dates;
            }

            canvas.drawText((mYears) + "-" + (months) + "-" + dates
                    + "(" + weeklys + ")", mTextSize, mTextSize, mPaint);

            boolToday = false;
        }
        else{
            canvas.drawText((mYear) + "-" + (month) + "-" + date
                    + "(" + weekly + ")", mTextSize, mTextSize, mPaint);
        }






    }



    DateEvent getPosItem(float x , float y){
        mBlockSize = Math.min(mWidth / 12, mWidth / mEvents.size());

        int idx = 0;
        for (DateEvent e : mEvents) {
            float gap1 = mTitleGap + idx * (mBlockGap + mBlockSize);
            float gap2 = mTitleGap + (idx * (mBlockGap + mBlockSize) + mBlockSize);
            if (gap1 < y && y < gap2){
                return e;
            }
            idx++;
        }

        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:
                DateEvent dateEvent = getPosItem(event.getX(), event.getY());
                if (dateEvent != null){
                    listener.onClickedItem(dateEvent.getParent());

                }

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

}
