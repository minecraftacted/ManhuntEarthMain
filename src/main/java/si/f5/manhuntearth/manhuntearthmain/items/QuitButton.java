package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class QuitButton extends GameItemButton{
    public QuitButton(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @Override
    protected void Process(PlayerInteractEvent e) {
        e.getPlayer().kickPlayer("ゲームを退出しました。");
    }

    @Override
    protected Material MATERIAL() {
        return Material.RED_BED;
    }

    @Override
    protected Optional<String> NAME() {
        return Optional.of("ゲームを退出");
    }

    @Override
    protected List<String> LORE() {
        return new ArrayList<>(Collections.singletonList("ゲームを退出します。"));
    }
}
