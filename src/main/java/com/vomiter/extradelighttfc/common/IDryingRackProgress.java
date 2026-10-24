package com.vomiter.extradelighttfc.common;

public interface IDryingRackProgress {
    void edtfc$checkCalendarUpdate();
    void edtfc$resetCalendar(int slot);
    int edtfc$getDryingTime(int slot);
    void edtfc$finishDrying(int slot);
}
