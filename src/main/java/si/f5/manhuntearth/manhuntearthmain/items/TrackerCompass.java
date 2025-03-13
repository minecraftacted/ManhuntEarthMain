package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.manhuntearth.manhuntearthmain.GamePlayer;
import si.f5.manhuntearth.manhuntearthmain.GameTime;
import si.f5.manhuntearth.manhuntearthmain.roles.HunterTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.RunnerTeam;

import java.util.*;

public class TrackerCompass extends GameItemButton {

    public TrackerCompass(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @Override
    protected Material MATERIAL() {
        return Material.COMPASS;
    }

    @Override
    protected Optional<String> NAME() {
        return Optional.of("追跡機");
    }

    @Override
    protected List<String> LORE() {
        return new ArrayList<>(Arrays.asList("最も近い標的を指す。", "位置情報は設定された時間が経過した後に更新される。", "追われる者が持っても効果がない。"));
    }

    @Override
    protected Optional<Map<Enchantment, Integer>> ENCHANTMENT() {
        return Optional.empty();
    }

    @Override
    protected void process(PlayerInteractEvent e) {
    }

    public void TryUpdate() {
    }

    private void Update() {

    }
}
