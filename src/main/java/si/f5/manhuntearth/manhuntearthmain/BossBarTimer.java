package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.Optional;

public class BossBarTimer {
    private final BossBar bossBar;
    public BossBarTimer(GamePlayersList gamePlayersList) {
        bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID,BarFlag.DARKEN_SKY);
        for(GamePlayer gamePlayer : gamePlayersList.playersList) {
            gamePlayer.getOnlinePlayer().ifPresent(bossBar::addPlayer);
        }
    }
    public void Update(GameTime max, GameTime now, Optional<String> titlePrefix) {
        bossBar.setProgress(((double) now.tick() /(double) max.tick()));
        StringBuilder title=new StringBuilder();
        titlePrefix.ifPresent(tp-> {
            title.append(tp);
            title.append(" ");
        });
        title.append(now.format());
        bossBar.setTitle(title.toString());
    }
    public void Remove() {
        bossBar.removeAll();
    }
}
