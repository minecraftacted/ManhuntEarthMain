package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import si.f5.manhuntearth.manhuntearthmain.roles.HunterTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.RunnerTeam;

public class HunterSuperPunch implements Listener {
    private final RunnerTeam runnerTeam;
    private final HunterTeam hunterTeam;
    public HunterSuperPunch(RunnerTeam runnerTeam, HunterTeam hunterTeam) {
        this.runnerTeam = runnerTeam;
        this.hunterTeam = hunterTeam;
    }
    @EventHandler
    public void hunterSuperPunch(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        if(!(e.getDamager() instanceof Player)) return;
        GamePlayer runner = GamePlayer.New((Player)e.getEntity());
        GamePlayer hunter = GamePlayer.New((Player)e.getDamager());
        if(runnerTeam.DoesNotContain(runner)) return;
        if(hunterTeam.DoesNotContain(hunter)) return;
        e.setDamage(20);
    }
}
