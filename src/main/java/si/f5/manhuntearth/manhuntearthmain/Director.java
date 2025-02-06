package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import si.f5.manhuntearth.manhuntearthmain.roles.GameTeam;

public class Director {
    public Director() {

    }
    public void start(World bukkitOverWorld, GameTeam... teams) {
        Bukkit.broadcastMessage("ゲーム開始");
        bukkitOverWorld.playSound(bukkitOverWorld.getSpawnLocation(), Sound.MUSIC_DISC_PIGSTEP, SoundCategory.MASTER,3,1);
        bukkitOverWorld.playSound(bukkitOverWorld.getSpawnLocation(),Sound.ITEM_GOAT_HORN_SOUND_2,3,1);
        for(GameTeam team:teams) {
            team.ShowTitle(team.titleOnStartDirection(),team.subtitleOnStartDirection(),new GameTime(0,1),new GameTime(0,3),new GameTime(0,1));
            Bukkit.broadcastMessage(team.BUKKIT_TEAM_COLOR()+"=="+team.BUKKIT_TEAM_DISPLAY_NAME()+"の勝利条件==");
            for(String winCondition:team.winConditions()) {
                Bukkit.broadcastMessage("・"+winCondition);
            }
            Bukkit.broadcastMessage(team.BUKKIT_TEAM_COLOR()+"=="+team.BUKKIT_TEAM_DISPLAY_NAME()+"メンバー==");
            for(GamePlayer gameplayer:team.GetGamePlayers()) {
                Bukkit.broadcastMessage(gameplayer.GetName());
            }
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("");
        }
    }
    public void releaseHunter(GamePlayersList gamePlayersList, GameTeam... teams) {
        Bukkit.broadcastMessage("鬼が開放された...");
        gamePlayersList.PlaySound(Sound.EVENT_MOB_EFFECT_RAID_OMEN,1,1);
        for(GameTeam team:teams) {
            team.ShowTitle(team.titleOnReleaseHunter(),team.subtitleOnReleaseHunter(),new GameTime(0,1),new GameTime(0,3),new GameTime(0,1));
        }
    }
}
