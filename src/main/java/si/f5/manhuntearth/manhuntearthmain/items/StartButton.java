package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import si.f5.manhuntearth.manhuntearthmain.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StartButton extends GameItemButton {
    public StartButton(JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    @Override
    public Material MATERIAL() {
        return Material.LIME_DYE;
    }

    @Override
    public Optional<String> NAME() {
        return Optional.of("スタート");
    }

    @Override
    protected List<String> LORE() {
        return new ArrayList<>(Collections.singletonList("ゲームを開始します。"));
    }

    @Override
    protected void Process(PlayerInteractEvent e) {
        Main.StartFlag();
    }
}
