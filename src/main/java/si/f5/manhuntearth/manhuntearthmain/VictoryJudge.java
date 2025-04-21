package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import si.f5.manhuntearth.manhuntearthmain.roles.GameTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.HunterTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.RunnerTeam;
import si.f5.manhuntearth.manhuntearthmain.roles.SpectatorRole;

public class VictoryJudge implements Listener {
    final GamePlayersList gamePlayersList;
    final HunterTeam hunterTeam;
    final RunnerTeam runnerTeam;
    final SpectatorRole spectatorRole;
    public VictoryJudge (GamePlayersList gamePlayersList, HunterTeam hunterTeam, RunnerTeam runnerTeam, SpectatorRole spectatorRole) {
        this.gamePlayersList=gamePlayersList;
        this.hunterTeam=hunterTeam;
        this.runnerTeam=runnerTeam;
        this.spectatorRole=spectatorRole;
    }
    private void GameOver(GameTeam winningTeam,GameTeam losingTeam,GameOverReason gameOverReason) {
        Main.StopFlag();
        Bukkit.broadcastMessage(winningTeam.BUKKIT_TEAM_COLOR()+winningTeam.BUKKIT_TEAM_DISPLAY_NAME()+
                "の勝利!");
        winningTeam.PlaySound(Sound.UI_TOAST_CHALLENGE_COMPLETE,1,1);
        losingTeam.PlaySound(Sound.ITEM_TRIDENT_THUNDER,1,1);
        spectatorRole.PlaySound(Sound.BLOCK_NOTE_BLOCK_BIT,1,1);
        String winningTeamSubTitle = "";
        String losingTeamSubTitle = "";
        String spectatorSubTitle = "";
        switch (gameOverReason){
            case ANNIHILATION:
                winningTeamSubTitle=ChatColor.GOLD+losingTeam.BUKKIT_TEAM_DISPLAY_NAME()+"が全滅した！";
                losingTeamSubTitle=ChatColor.DARK_RED+"全滅してしまった...";
                spectatorSubTitle=winningTeam.BUKKIT_TEAM_COLOR()+losingTeam.BUKKIT_TEAM_DISPLAY_NAME()+"が全滅した！";
                break;
            case TIME_UP:
                winningTeamSubTitle=ChatColor.GOLD+"逃げ切った！";
                losingTeamSubTitle=ChatColor.DARK_RED+"逃げ切られてしまった...";
                spectatorSubTitle=winningTeam.BUKKIT_TEAM_COLOR()+"時間切れになった！";
                break;
        }
        winningTeam.ShowTitle("勝利",winningTeamSubTitle,new GameTime(0,1),new GameTime(0,3),new GameTime(0,1));
        losingTeam.ShowTitle("敗北",losingTeamSubTitle,new GameTime(0,1),new GameTime(0,3),new GameTime(0,1));
        spectatorRole.ShowTitle(winningTeam.BUKKIT_TEAM_DISPLAY_NAME()+"の勝利",spectatorSubTitle,new GameTime(0,1),new GameTime(0,3),new GameTime(0,1));
        gamePlayersList.playersList.forEach(spectatorRole::AddPlayer);
    }
    @EventHandler
    public void onPlayerBelongsToEitherTeamQuit(PlayerQuitEvent e) {
        if(Main.GetGameState()==GameState.IN_THE_GAME) {
            if(hunterTeam.Contains(e.getPlayer())&&hunterTeam.Size()!=1) {
                e.setQuitMessage(e.getQuitMessage()+"\n"+hunterTeam.BUKKIT_TEAM_COLOR()+hunterTeam.BUKKIT_TEAM_DISPLAY_NAME()+"は残り"+hunterTeam.Size()+"人");
            }
            if(runnerTeam.Contains(e.getPlayer())&&runnerTeam.Size()!=1) {
                e.setQuitMessage(e.getQuitMessage()+"\n"+runnerTeam.BUKKIT_TEAM_COLOR()+runnerTeam.BUKKIT_TEAM_DISPLAY_NAME()+"は残り"+runnerTeam.Size()+"人");
            }
            onDecreaseInPlayers(new GamePlayer(e.getPlayer()));
        }
    }
    @EventHandler
    public void onPlayerBelongsToEitherTeamDie(PlayerDeathEvent e) {
        if(Main.GetGameState()==GameState.IN_THE_GAME) {
            onDecreaseInPlayers(new GamePlayer(e.getEntity()));
        }
    }
    public void onTimeIsUp() {
        GameOver(runnerTeam,hunterTeam,GameOverReason.TIME_UP);
    }
    private void onDecreaseInPlayers(GamePlayer gamePlayer) {
        gamePlayersList.PlaySound(Sound.ENTITY_ENDER_DRAGON_HURT,1,0.5f);
        spectatorRole.AddPlayer(gamePlayer);
        if(hunterTeam.Size()==0) {
            GameOver(runnerTeam,hunterTeam,GameOverReason.ANNIHILATION);
        }
        else if(runnerTeam.Size()==0) {
            GameOver(hunterTeam,runnerTeam,GameOverReason.ANNIHILATION);
        }
    }
}
enum GameOverReason {
    TIME_UP,ANNIHILATION
}
