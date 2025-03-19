package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import si.f5.manhuntearth.manhuntearthmain.GamePlayer;
import si.f5.manhuntearth.manhuntearthmain.GameTime;
import si.f5.manhuntearth.manhuntearthmain.roles.GameTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.HunterTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.RunnerTeam;

import java.util.*;

public class TrackerCompass extends GameItemButton {
    final private List<RunnerLocation> runnerLocations = new ArrayList<>();
    final private List<HunterTarget> hunterTargets = new ArrayList<>();
    final private HunterTeam hunterTeam;
    final private RunnerTeam runnerTeam;

    public TrackerCompass(final JavaPlugin javaPlugin, final HunterTeam hunterTeam, final RunnerTeam runnerTeam) {
        super(javaPlugin);
        this.hunterTeam = hunterTeam;
        this.runnerTeam = runnerTeam;
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
        return new ArrayList<>(Arrays.asList("標的の位置を指す。", "右クリックで標的を切り替える。", "位置情報は設定された時間が経過した後に更新される。", "逃走者が持っても効果がない。"));
    }

    @Override
    protected Optional<Map<Enchantment, Integer>> ENCHANTMENT() {
        return Optional.empty();
    }

    @Override
    protected void process(PlayerInteractEvent e) {
        //使用者はハンターでなければならない
        if(this.hunterTeam.DoesNotContain(e.getPlayer())) return;
        selectTarget(GamePlayer.New(e.getPlayer()));
    }

    public void tryUpdate(final HunterTeam hunterTeam, final RunnerTeam runnerTeam, final List<GameTime> updateTime, final GameTime now, final World overWorld, final Plugin plugin) {
        if (!updateTime.contains(now)) return;

        int nextUpdateIndex = updateTime.indexOf(now)+1;
        Optional<GameTime> nextUpdate;
        try {
            nextUpdate = Optional.of(updateTime.get(nextUpdateIndex));
        } catch (IndexOutOfBoundsException e) {
            nextUpdate = Optional.empty();
        }

        updateDirection(hunterTeam,runnerTeam,overWorld,plugin,nextUpdate);
        update(runnerTeam);
    }

    private void updateDirection(final HunterTeam hunterTeam, final RunnerTeam runnerTeam, final World overWorld, final Plugin plugin, final Optional<GameTime> nextUpdate) {
        hunterTeam.PlaySound(Sound.BLOCK_BELL_RESONATE, 1, 1);
        runnerTeam.PlaySound(Sound.BLOCK_BELL_RESONATE, 1, 1);

        overWorld.getWorldBorder().setWarningDistance(1000000);
        new BukkitRunnable() {
            @Override
            public void run() {
                overWorld.getWorldBorder().setWarningDistance(0);
            }
        }.runTaskLater(plugin,new GameTime(0,3).tick());

        NAME().ifPresent(name -> hunterTeam.SendMessage(name + "の位置情報が更新された..."));
        runnerTeam.SendMessage(hunterTeam.BUKKIT_TEAM_DISPLAY_NAME() + "に位置情報が送信された...");

        Set<GameTeam> teams = new HashSet<>();
        teams.add(hunterTeam);
        teams.add(runnerTeam);
        nextUpdate.ifPresentOrElse(
                n -> teams.forEach(t -> t.SendMessage("次の更新: [" + n.format() + "]")),
                () -> teams.forEach(t -> t.SendMessage("次の更新: [なし]"))
        );
    }

    private void update(final RunnerTeam runnerTeam) {
        runnerLocations.clear();
        runnerTeam.GetGamePlayers().forEach(gamePlayer -> runnerLocations.add(new RunnerLocation(gamePlayer,gamePlayer.GetLocation())));
    }

    private void selectTarget(final GamePlayer hunter) {
        if(runnerLocations.isEmpty()) return;
        Optional<HunterTarget> optionalNowTarget = hunterTargets.stream().filter(hunterTarget -> hunterTarget.isSameHunter(hunter)).findFirst();
        if(optionalNowTarget.isPresent()) {
            HunterTarget nowTarget = optionalNowTarget.get();
            int index = runnerLocations.indexOf(nowTarget.runnerLocation());
            try {
                setTarget(new HunterTarget(hunter, runnerLocations.get(index + 1)));
            } catch (IndexOutOfBoundsException e) {
                setTarget(new HunterTarget(hunter,runnerLocations.get(0)));
            }
        } else {
            setTarget(new HunterTarget(hunter,runnerLocations.get(0)));
        }
    }

    private void setTarget(final HunterTarget hunterTarget) {
        hunterTargets.removeIf(ht -> ht.isSameHunter(hunterTarget));
        hunterTargets.add(hunterTarget);
        hunterTarget.setCompassTarget();
    }
}

class RunnerLocation {
    final private GamePlayer runner;
    final private Location location;
    public RunnerLocation(final GamePlayer gamePlayer, final Location location) {
        this.runner = gamePlayer;
        this.location = location;
    }
    public Location location() {
        return location;
    }
    public String name() {
        return runner.GetName();
    }
}

class HunterTarget {
    final private GamePlayer hunter;
    final private RunnerLocation runnerLocation;
    public HunterTarget(final GamePlayer gamePlayer, final RunnerLocation runnerLocation) {
        this.hunter = gamePlayer;
        this.runnerLocation = runnerLocation;
    }
    public boolean isSameHunter(final GamePlayer hunter) {
        return this.hunter.equals(hunter);
    }
    public boolean isSameHunter(final HunterTarget hunterTarget) {
        return this.hunter.equals(hunterTarget.hunter);
    }
    public void setCompassTarget() {
        hunter.setCompassTarget(runnerLocation.location());
        hunter.playSound(Sound.UI_BUTTON_CLICK,1,1);
        hunter.sendMessage(runnerLocation.name()+"の位置情報を表示中");
    }
    public RunnerLocation runnerLocation() {
        return  runnerLocation;
    }
}
