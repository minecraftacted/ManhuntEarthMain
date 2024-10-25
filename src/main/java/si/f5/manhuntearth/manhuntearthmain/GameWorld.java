package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class GameWorld {
    final private World bukkitOverWorld;
    private final int WORLD_BORDER_DIAMETER=2048;
    public GameWorld(final Plugin plugin) {
        bukkitOverWorld=Bukkit.getWorlds().stream().filter(world -> world.getEnvironment() == World.Environment.NORMAL).findFirst().get();
        bukkitOverWorld.getWorldBorder().setCenter(new Location(bukkitOverWorld,0,0,0));
        bukkitOverWorld.getWorldBorder().setSize(WORLD_BORDER_DIAMETER);

        Location highestBlock = bukkitOverWorld.getHighestBlockAt(0,0).getLocation();
        Location topOfHighestBlock= highestBlock.clone();
        topOfHighestBlock.setY(topOfHighestBlock.getY()+1);
        highestBlock.getBlock().setType(Material.STONE);
        bukkitOverWorld.setSpawnLocation(topOfHighestBlock);
        bukkitOverWorld.setGameRule(GameRule.SPAWN_RADIUS,0);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void HoldBackPlayer(PlayerMoveEvent e) {
                if(Main.GetGameState()!=GameState.BEFORE_THE_GAME) return;
                if(Objects.requireNonNull(e.getTo()).distance(bukkitOverWorld.getSpawnLocation())<20) return;
                GamePlayer player = GamePlayer.New(e.getPlayer());
                Location locToHoldBack = bukkitOverWorld.getSpawnLocation();
                locToHoldBack.setYaw(e.getFrom().getYaw());
                locToHoldBack.setPitch(e.getFrom().getPitch());
                player.Teleport(locToHoldBack);
                player.SendMessage("開始前はこれ以上初期スポーンから離れられません！");
            }
        },plugin);
    }
    public void StartTheGame() {
        bukkitOverWorld.setTime(0);
        bukkitOverWorld.setStorm(false);
        bukkitOverWorld.setThundering(false);
    }
    public World GetOverWorld() {
        return bukkitOverWorld;
    }
}