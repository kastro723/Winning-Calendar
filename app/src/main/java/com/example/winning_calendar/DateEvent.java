package com.example.winning_calendar;

public class DateEvent implements Comparable<DateEvent>{
    private DateEvent parent;
    private String title;
    private String content;

    private boolean mIsRepeat;

    private DateAttr start;
    private DateAttr end;

    private int color;

    DateEvent(String title, String content, int color, DateAttr s, DateAttr e) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.start = s;
        this.end = e;
    }

    public void setParent(DateEvent parent) {
        this.parent = parent;
    }

    public DateEvent getParent() {
        return parent;
    }

    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }



    public boolean ismIsRepeat() {
        return mIsRepeat;
    }

    public DateAttr getStart() {
        return start;
    }

    public DateAttr getEnd() {
        return end;
    }

    public int getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

        public void setContent(String content) {
            this.content = content;
        }



    public void setmIsRepeat(boolean mIsRepeat) {
        this.mIsRepeat = mIsRepeat;
    }

    public void setStart(DateAttr start) {
        this.start = start;
    }

    public void setEnd(DateAttr end) {
        this.end = end;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDateStr(){
        String startHour = start.getHour() + "";
        if (startHour.length() == 1) {
            startHour = "0" + startHour;
        }
        String startMinute = start.getMinute() + "";
        if (startMinute.length() == 1) {
            startMinute = "0" + startMinute;
        }
        String endHour = end.getHour() + "";
        if (endHour.length() == 1) {
            endHour = "0" + endHour;
        }
        String endMinute = end.getMinute() + "";
        if (endMinute.length() == 1) {
            endMinute = "0" + endMinute;
        }
        return  startHour + ":" + startMinute +"~" + endHour + ":" + endMinute;
    }

    @Override
    public int compareTo(DateEvent dateEvent) {
        if (isSameRangeEvent(dateEvent)) return 0;
        if (this.isAllDay())return 1;
        if (dateEvent.isAllDay()) return -1;

        if (start.getHour() < dateEvent.start.getHour()){
            return 1;
        }
        else if(start.getHour() > dateEvent.start.getHour()){
            return -1;
        }
        else {
            if (start.getMinute() < dateEvent.start.getMinute()){
                return 1;
            }
            else if(start.getMinute() > dateEvent.start.getMinute()){
                return -1;
            }
        }

        return 0;
    }

    private boolean isAllDay(){
        if (start.isSameDay(end)){
            if (start.getHour() == 0 && start.getMinute() == 0 && end.getHour() == 23 && end.getMinute() == 59)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isSameRangeEvent(DateEvent event){
        if (start.isSameDate(event.start) && end.isSameDay(event.end)){
            return true;
        }

        return false;
    }
}
