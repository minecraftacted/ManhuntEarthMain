package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class GameWorld {
    final private World bukkitOverWorld;
    private final int WORLD_BORDER_DIAMETER=2048;
    public static int WORLD_SPAWN_X = 0;
    public static int WORLD_SPAWN_Z = 0;
    public GameWorld(final Plugin plugin) {
        bukkitOverWorld=Bukkit.getWorlds().stream().filter(world -> world.getEnvironment() == World.Environment.NORMAL).findFirst().get();
        bukkitOverWorld.getWorldBorder().setCenter(new Location(bukkitOverWorld,WORLD_SPAWN_X,0,WORLD_SPAWN_Z));
        bukkitOverWorld.getWorldBorder().setSize(WORLD_BORDER_DIAMETER);

        Location highestBlock = bukkitOverWorld.getHighestBlockAt(WORLD_SPAWN_X,WORLD_SPAWN_Z).getLocation();
        Location topOfHighestBlock= highestBlock.clone();
        topOfHighestBlock.setY(topOfHighestBlock.getY()+1);
        for(int x=-1;x<=1;x++) {
            for(int z=-1;z<=1;z++) {
                Location loc =highestBlock.clone();
                loc.setX(loc.getX()+x);
                loc.setZ(loc.getZ()+z);
                if(!loc.getBlock().isPassable()) continue;
                loc.getBlock().setType(Material.STONE);
            }
        }
        bukkitOverWorld.setSpawnLocation(topOfHighestBlock);
        bukkitOverWorld.setGameRule(GameRule.SPAWN_RADIUS,0);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void HoldBackPlayer(PlayerMoveEvent e) {
                if(Main.GetGameState()!=GameState.BEFORE_THE_GAME) return;
                if(Objects.requireNonNull(e.getTo()).distance(bukkitOverWorld.getSpawnLocation())<20) return;
                GamePlayer player = GamePlayer.New(e.getPlayer());
                Location locToHoldBack = bukkitOverWorld.getSpawnLocation();
                locToHoldBack.setX(locToHoldBack.getX()+0.5);
                locToHoldBack.setZ(locToHoldBack.getZ()+0.5);
                locToHoldBack.setYaw(e.getFrom().getYaw());
                locToHoldBack.setPitch(e.getFrom().getPitch());
                player.Teleport(locToHoldBack);
                player.SendActionbarMessage(ChatColor.RED+"開始前はこれ以上初期スポーンから離れられません！");
                player.playSound(Sound.ENTITY_ENDERMAN_TELEPORT,1f,0.5f);
            }
        },plugin);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void preventsBlockUnderHunterBreak(BlockBreakEvent e) {
                if(Main.GetGameState()!=GameState.IN_HUNTER_WAITING_TIME) return;
                if(highestBlock.equals(e.getBlock().getLocation())){
                    e.setCancelled(true);
                }
            }
        },plugin);
    }
    public void StartTheGame() {
        bukkitOverWorld.setTime(0);
        bukkitOverWorld.setStorm(false);
        bukkitOverWorld.setThundering(false);
        Bukkit.getOnlinePlayers().forEach(player -> GamePlayer.New(player).Teleport(GetOverWorld().getSpawnLocation().add(0.5,0,0.5)));
    }
    public World GetOverWorld() {
        return bukkitOverWorld;
    }
}
