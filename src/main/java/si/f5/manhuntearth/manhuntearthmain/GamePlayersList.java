package si.f5.manhuntearth.manhuntearthmain;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import si.f5.manhuntearth.manhuntearthmain.items.GameItem;
import si.f5.manhuntearth.manhuntearthmain.roles.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GamePlayersList {
    final List<GamePlayer> playersList;
    public GamePlayersList() {
        this.playersList = new ArrayList<>();
    }
    public void AddPlayer(GamePlayer player) {
        if(this.playersList.contains(player)) {
            return;
        }
        this.playersList.add(player);
    }
    public void AddPlayer(Player player) {
        AddPlayer(new GamePlayer(player));
    }
    public void RemovePlayer(GamePlayer player) {
        this.playersList.remove(player);
    }
    public void RemovePlayer(Player player) {
        RemovePlayer(new GamePlayer(player));
    }
    public void SetItemToAllPlayersInventory(GameItem item, int slot) {
        for (GamePlayer gamePlayer:playersList) {
            gamePlayer.SetItem(item,slot);
        }
    }
    public void SetItemToHostsInventory(GameItem item,int slot) {
        GetHost().ifPresent(v ->v.SetItem(item,slot));
    }
    public void PlaySound(Sound sound, float volume, float pitch) {
        playersList.forEach(gamePlayer -> gamePlayer.playSound(sound, volume, pitch));
    }
    public void InitializeAllPlayers() {
        ClearALlPlayers();
        ClearEffectsAllPlayers();
        SetHealthMaxAllPlayers();
        SetFoodLevelMaxAllPlayers();
    }
    public void SetAllPlayersGameMode(GameMode gameMode) {
        playersList.forEach(p -> p.SetGameMode(gameMode));
    }
    private void ClearALlPlayers() {
        playersList.forEach(GamePlayer::Clear);
    }
    private void ClearEffectsAllPlayers() {
        playersList.forEach(GamePlayer::ClearEffects);
    }
    private void SetHealthMaxAllPlayers() {
        playersList.forEach(GamePlayer::SetHealthMax);
    }
    private void SetFoodLevelMaxAllPlayers() {
        playersList.forEach(GamePlayer::SetFoodLevelMax);
    }
    public void TeamDivide(Role role1, Role role2, boolean allPlayersIntoRole1) {
        ArrayList<GamePlayer> l = new ArrayList<>(playersList);
        if(allPlayersIntoRole1) {
            l.forEach(role1::AddPlayer);
            return;
        }
        Collections.shuffle(l);
        int i;
        for (i=0;i<l.size()/2;i++) {
            role1.AddPlayer(l.get(i));
        }
        for(;i<l.size();i++) {
            role2.AddPlayer(l.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(GamePlayer gamePlayer:playersList) {
            string.append(gamePlayer.GetName());
            string.append("\n");
        }
        return string.toString();
    }
    private Optional<GamePlayer> GetHost() {
        if(playersList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(playersList.get(0));
    }
}
