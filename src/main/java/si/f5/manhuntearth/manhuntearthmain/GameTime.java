package si.f5.manhuntearth.manhuntearthmain;

import java.util.Objects;

public class GameTime {
    private final int tick;
    private static final int SECOND=20;
    private static final int MINUTE =60*SECOND;
    public GameTime(final int tick) {
        if(tick<0) throw new IllegalArgumentException("0以上でなければいけません");
        this.tick=tick;
    }
    public GameTime(final int minutes,final int seconds) {
        this(minutes*MINUTE+seconds*SECOND);
    }
    public GameTime(GameTime time) {
        this(time.tick);
    }
    public GameTime Add(GameTime time) {
        return new GameTime(this.tick+ time.tick);
    }
    public GameTime Increment() {
        return new GameTime(this.tick+1);
    }
    public GameTime Decrement() {
        return new GameTime(this.tick-1);
    }
    public String format() {
        StringBuilder format=new StringBuilder();
        if(Minutes()<10) {
            format.append(0);
        }
        format.append(Minutes());
        format.append(":");
        if(Seconds()<10) {
            format.append(0);
        }
        format.append(Seconds());
        return format.toString();
    }
    private int Minutes() {
        return this.tick/ MINUTE;
    }
    private int Seconds() {
        return (this.tick% MINUTE)/ SECOND;
    }
    public boolean isZero() {
        return tick==0;
    }
    public int tick() {
        return tick;
    }
    @Override
    public boolean equals(Object o) {
        if(o==this) return true;
        if(o==null) return false;
        if(!(o instanceof GameTime)) return false;
        GameTime other = (GameTime) o;
        return this.tick == other.tick;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.tick);
    }
}
