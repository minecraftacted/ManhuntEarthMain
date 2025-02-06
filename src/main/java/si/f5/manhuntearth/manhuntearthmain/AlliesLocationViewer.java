package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;
import si.f5.manhuntearth.manhuntearthmain.roles.HunterTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.RunnerTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlliesLocationViewer {
    public static void update(HunterTeam hunterTeam,RunnerTeam runnerTeam) {
        for(GamePlayer hunter:hunterTeam.GetGamePlayers()) {
            List<GamePlayer> allies = hunterTeam.GetGamePlayers().stream().filter(gamePlayer -> !(gamePlayer.equals(hunter))).collect(Collectors.toList());
            StringBuilder titleBuilder = new StringBuilder();
            titleBuilder.append(ChatColor.GOLD+"[味方の位置] "+ChatColor.RESET);
            allies.forEach(ally -> titleBuilder.append(
                    ChatColor.GOLD+"|"+ChatColor.RESET
                    +ally.GetName()+" : "
                    +ally.GetLocation().getBlockX()+","+ally.GetLocation().getBlockY()+","+ally.GetLocation().getBlockZ()
                    +ChatColor.GOLD+"|"+ChatColor.RESET));
            String title = titleBuilder.toString();
            hunter.SendActionbarMessage(title);
        }
        for(GamePlayer runner:runnerTeam.GetGamePlayers()) {
            List<GamePlayer> allies = runnerTeam.GetGamePlayers().stream().filter(gamePlayer -> !(gamePlayer.equals(runner))).collect(Collectors.toList());
            StringBuilder titleBuilder = new StringBuilder();
            titleBuilder.append(ChatColor.GOLD+"[味方の位置] "+ChatColor.RESET);
            allies.forEach(ally -> titleBuilder.append(
                    ChatColor.GOLD+"|"+ChatColor.RESET
                            +ally.GetName()+" : "
                            +ally.GetLocation().getBlockX()+","+ally.GetLocation().getBlockY()+","+ally.GetLocation().getBlockZ()
                            +ChatColor.GOLD+"|"+ChatColor.RESET));
            String title = titleBuilder.toString();
            runner.SendActionbarMessage(title);
        }
    }
}
