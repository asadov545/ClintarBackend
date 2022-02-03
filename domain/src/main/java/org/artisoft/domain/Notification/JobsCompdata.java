package org.artisoft.domain.Notification;

import org.artisoft.domain.ValueLabel;

import java.util.List;

public class
JobsCompdata {

    private List<ValueLabel<Long, String>> UserList;
    private List<ValueLabel<Long, String>> FormList;
    private List<ValueLabel<Long, String>> HourList;
    private List<ValueLabel<Long, String>> DayList;

    public List<ValueLabel<Long, String>> getDayList() {
        return DayList;
    }

    public void setDayList(List<ValueLabel<Long, String>> dayList) {
        DayList = dayList;
    }

    public List<ValueLabel<Long, String>> getUserList() {
        return UserList;
    }

    public void setUserList(List<ValueLabel<Long, String>> userList) {
        UserList = userList;
    }

    public List<ValueLabel<Long, String>> getFormList() {
        return FormList;
    }

    public void setFormList(List<ValueLabel<Long, String>> formList) {
        FormList = formList;
    }

    public List<ValueLabel<Long, String>> getHourList() {
        return HourList;
    }

    public void setHourList(List<ValueLabel<Long, String>> hourList) {
        HourList = hourList;
    }
}
