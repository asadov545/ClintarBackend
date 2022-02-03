package org.artisoft.domain.ModTask.reports;

public class DateType {

    private long from;
    private long to;

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "DateType{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
