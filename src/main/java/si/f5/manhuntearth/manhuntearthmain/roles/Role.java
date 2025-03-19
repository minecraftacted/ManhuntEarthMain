package si.f5.manhuntearth.manhuntearthmain.roles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import si.f5.manhuntearth.manhuntearthmain.GamePlayer;
import si.f5.manhuntearth.manhuntearthmain.GameTime;
import si.f5.manhuntearth.manhuntearthmain.items.GameItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Role {
    final Team bukkitTeam;
    public Role() {
        ScoreboardManager scoreboardManager= Bukkit.getScoreboardManager();
        Scoreboard scoreboard= Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        bukkitTeam=scoreboard.registerNewTeam(BUKKIT_TEAM_NAME());
        bukkitTeam.setAllowFriendlyFire(true);
        bukkitTeam.setCanSeeFriendlyInvisibles(true);
        bukkitTeam.setColor(BUKKIT_TEAM_COLOR());
        bukkitTeam.setDisplayName(BUKKIT_TEAM_DISPLAY_NAME());
        bukkitTeam.setPrefix(BUKKIT_TEAM_COLOR()+"["+ BUKKIT_TEAM_DISPLAY_NAME()+"]");
        bukkitTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS);
        bukkitTeam.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS);
        bukkitTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
    }
    final public void AddPlayer(GamePlayer gamePlayer){
        bukkitTeam.addPlayer(gamePlayer.getBukkitPlayer());
        OnPlayerAdded(gamePlayer);
    }
    public void SetItemToAllPlayers(GameItem item, int slot) {
        bukkitTeam.getPlayers().forEach(p-> GamePlayer.New(p).SetItem(item,slot));
    }
    final public void SetItemToHeadOfAllPlayers(GameItem item) {
        bukkitTeam.getPlayers().forEach(p-> GamePlayer.New(p).SetItemToHead(item));
    }
    final public void ClearAllPlayersItems() {
        bukkitTeam.getPlayers().forEach(p-> GamePlayer.New(p).Clear());
    }
    final public void AddEffectAllPlayers(PotionEffectType type, GameTime duration, int amplifier, boolean particles) {
        bukkitTeam.getPlayers().forEach(p-> GamePlayer.New(p).AddEffect(type,duration,amplifier,particles));
    }
    final public void SendMessage(String message) {
        bukkitTeam.getPlayers().forEach(p -> GamePlayer.New(p).sendMessage(message));
    }
    final public void PlaySound(Sound sound, float volume, float pitch) {
        bukkitTeam.getPlayers().forEach(p-> GamePlayer.New(p).playSound(sound,volume,pitch));
    }
    final public void ShowTitle(String title, String subTitle, GameTime fadeIn, GameTime stay, GameTime fadeOut) {
        bukkitTeam.getPlayers().forEach(p-> GamePlayer.New(p).ShowTitle(title,subTitle,fadeIn,stay,fadeOut));
    }
    final public boolean Contains(GamePlayer gamePlayer) {
        return Contains(gamePlayer.getBukkitPlayer());
    }
    final public boolean Contains(OfflinePlayer player) {
        return bukkitTeam.hasPlayer(player);
    }
    final public boolean DoesNotContain(GamePlayer gamePlayer) {
        return DoesNotContain(gamePlayer.getBukkitPlayer());
    }
    final public boolean DoesNotContain(OfflinePlayer player) {
        return !(Contains(player));
    }
    final public int Size() {
        return bukkitTeam.getSize();
    }
    final public List<GamePlayer> GetGamePlayers() {
        ArrayList<GamePlayer> gamePlayers= new ArrayList<>();
        bukkitTeam.getPlayers().forEach(op-> gamePlayers.add(GamePlayer.New(op)));
        return gamePlayers;
    }
    final public Team GetBukkitTeam() {
        return bukkitTeam;
    }
    public static void RemoveAllTeams() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = Objects.requireNonNull(scoreboardManager).getMainScoreboard();
        for(Team team:scoreboard.getTeams()) {
            team.unregister();
        }
    }
    abstract void OnPlayerAdded(GamePlayer addedPlayer);
    abstract String BUKKIT_TEAM_NAME();
    public abstract ChatColor BUKKIT_TEAM_COLOR();
    public abstract String BUKKIT_TEAM_DISPLAY_NAME();
}
