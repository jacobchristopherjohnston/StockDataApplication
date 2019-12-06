import java.time.LocalDateTime;

public class IntraDayData {
    LocalDateTime date;
    Double open, high, low, close, volume;

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getClose() {
        return close;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getHigh() {
        return high;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getLow() {
        return low;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getOpen() {
        return open;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getVolume() {
        return volume;
    }
}
