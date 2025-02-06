package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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
    static ArrayList<Map.Entry<UUID, Location>> runnersLocations = new ArrayList<>();
    static Map<UUID, UUID> huntersAndTargetsOfThem = new HashMap<>();
    RunnerTeam runnerTeam;

    public TrackerCompass(JavaPlugin javaPlugin, RunnerTeam runnerTeam) {
        super(javaPlugin);
        this.runnerTeam=runnerTeam;
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
    protected void Process(PlayerInteractEvent e) {
        selectTarget(e);
    }

    public void TryUpdate(HunterTeam hunterTeam, RunnerTeam runnerTeam, List<GameTime> updateTime, GameTime now, World overWorld, Plugin plugin) {
        if (updateTime.contains(now)) {
            if (updateTime.indexOf(now) == (updateTime.size() - 1)) {
                Update(hunterTeam, runnerTeam, Optional.empty(), overWorld, plugin);
            } else {
                Update(hunterTeam, runnerTeam, Optional.of(updateTime.get(updateTime.indexOf(now) + 1)), overWorld, plugin);
            }
        }
    }

    private void Update(HunterTeam hunterTeam, RunnerTeam runnerTeam, Optional<GameTime> nextUpdate, World overWorld, Plugin plugin) {
        hunterTeam.PlaySound(Sound.BLOCK_BELL_RESONATE, 1, 1);
        runnerTeam.PlaySound(Sound.BLOCK_BELL_RESONATE, 1, 1);
        overWorld.getWorldBorder().setWarningDistance(1000000);
        new BukkitRunnable() {
            @Override
            public void run() {
                overWorld.getWorldBorder().setWarningDistance(0);
            }
        }.runTaskLater(plugin, new GameTime(0, 3).Tick());
        NAME().ifPresent(name -> hunterTeam.SendMessage(name + "の位置情報が更新された..."));
        runnerTeam.SendMessage(hunterTeam.BUKKIT_TEAM_DISPLAY_NAME() + "に位置情報が送信された...");
        if (nextUpdate.isPresent()) {
            hunterTeam.SendMessage("次の更新: [" + nextUpdate.get().Format() + "]");
            runnerTeam.SendMessage("次の更新: [" + nextUpdate.get().Format() + "]");
        } else {
            hunterTeam.SendMessage("次の更新: [なし]");
            runnerTeam.SendMessage("次の更新: [なし]");
        }

        //逃走者の位置を更新
        runnersLocations.clear();
        runnerTeam.GetGamePlayers().forEach(
                gamePlayer -> gamePlayer.getOnlinePlayer().ifPresent(
                        player -> runnersLocations.add(new AbstractMap.SimpleEntry<>(player.getUniqueId(), player.getLocation()))));
        //ここからはハンター側をいじる
        for (GamePlayer gHunter : hunterTeam.GetGamePlayers()) {
            if (gHunter.getOnlinePlayer().isEmpty()) continue;
            Player hunter = gHunter.getOnlinePlayer().get();
            //ハンターがリストに含まれていたら
            if (huntersAndTargetsOfThem.containsKey(hunter.getUniqueId())) {
                OfflinePlayer target = Bukkit.getPlayer(huntersAndTargetsOfThem.get(hunter.getUniqueId()));
                //ハンターのターゲットが今も逃走者チームにいたら≒今も生きてたら
                if (runnerTeam.Contains(target)) {
                    hunter.setCompassTarget(runnersLocations.stream().filter(entry -> entry.getKey().equals(target.getUniqueId())).findFirst().get().getValue());
                }
                //ハンターのターゲットが今はもう逃走者チームにいなかったら≒もう死んでたら
                else {
                    runnersLocations.removeIf(entry -> entry.getKey().equals(target.getUniqueId()));
                    chooseNewTarget(hunter, runnerTeam);
                }
            }
            //ハンターがリストに含まれていなかったら
            else {
                chooseNewTarget(hunter, runnerTeam);
            }
        }
    }

    private void chooseNewTarget(Player hunter, RunnerTeam runnerTeam) {
        Optional<Map.Entry<UUID, Location>> optionalNewTarget = runnersLocations.stream().filter(entry -> runnerTeam.Contains(Bukkit.getPlayer(entry.getKey()))).findFirst();
        //逃走者がいなかったらもう試合終了なのであとはどうでもいい
        if (optionalNewTarget.isEmpty()) return;
        //ここからは逃走者がいた場合
        UUID newTarget = optionalNewTarget.get().getKey();
        huntersAndTargetsOfThem.put(hunter.getUniqueId(), newTarget);
        hunter.setCompassTarget(Bukkit.getPlayer(newTarget).getLocation());
        direction(hunter.getUniqueId(),newTarget);
    }

    private void selectTarget(PlayerEvent event) {
        UUID nowTarget = huntersAndTargetsOfThem.get(event.getPlayer().getUniqueId());
        if (runnersLocations.stream().noneMatch(entry -> entry.getKey().equals(nowTarget))) {
            chooseNewTarget(event.getPlayer(), this.runnerTeam);
        }
        int index = -1;
        for (Map.Entry<UUID, Location> runnersLocation : runnersLocations) {
            if (runnersLocation.getKey().equals(nowTarget)) {
                index = runnersLocations.indexOf(runnersLocation);
            }
        }
        Map.Entry<UUID, Location> nextTarget = null;
        try {
            nextTarget = runnersLocations.get(index + 1);
        } catch (IndexOutOfBoundsException e) {
            nextTarget = runnersLocations.get(0);
        }
        event.getPlayer().setCompassTarget(nextTarget.getValue());
        direction(event.getPlayer().getUniqueId(),nextTarget.getKey());
    }

    private void direction(UUID uHunter,UUID uRunner) {
        Player pHunter = Bukkit.getPlayer(uHunter);
        if(pHunter==null) return;
        Player pRunner = Bukkit.getPlayer(uRunner);
        if(pRunner==null) return;
        GamePlayer hunter = new GamePlayer(pHunter);
        GamePlayer runner = new GamePlayer(pRunner);
        hunter.PlaySound(Sound.UI_BUTTON_CLICK,1,1);
        hunter.SendMessage(runnerTeam.BUKKIT_TEAM_COLOR()+"["+runnerTeam.BUKKIT_TEAM_DISPLAY_NAME()+"]"+runner.GetName()+ChatColor.RESET+" の位置情報を表示中");
    }
}
