package si.f5.manhuntearth.manhuntearthmain;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import si.f5.manhuntearth.manhuntearthmain.items.GameItem;

import java.util.Objects;
import java.util.Optional;

public class GamePlayer {
    private final OfflinePlayer bukkitPlayer;
    public GamePlayer(OfflinePlayer bukkitPlayer) {
        this.bukkitPlayer=bukkitPlayer;
    }
    public static GamePlayer New(OfflinePlayer bukkitPlayer) {
        return new GamePlayer(bukkitPlayer);
    }
    public void sendMessage(String message) {
        getOnlinePlayer().ifPresent(p-> p.sendMessage(message));
    }
    public void SendActionbarMessage(String message) {
        getOnlinePlayer().ifPresent(p-> {
            TextComponent component = new TextComponent();
            component.setText(message);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,component);
        });
    }
    public void SetItem(GameItem item, int slot) {
        getOnlinePlayer().ifPresent(p-> p.getInventory().setItem(slot, item.GetItemStack()));
    }
    public void SetItemToHead(GameItem item) {
        getOnlinePlayer().ifPresent(p-> p.getInventory().setHelmet(item.GetItemStack()));
    }
    public void Clear() {
        getOnlinePlayer().ifPresent(p-> p.getInventory().clear());
    }
    public void playSound(Sound sound, float volume, float pitch) {
        getOnlinePlayer().ifPresent(p-> p.playSound(p,sound, SoundCategory.MASTER,volume,pitch));
    }
    public void ShowTitle(String title,String subTitle,GameTime fadeIn,GameTime stay,GameTime fadeOut) {
        getOnlinePlayer().ifPresent(p-> p.sendTitle(title,subTitle,fadeIn.tick(),stay.tick(),fadeOut.tick()));
    }
    public void AddEffect(PotionEffectType type,GameTime duration,int amplifier,boolean particles) {
        getOnlinePlayer().ifPresent(p-> p.addPotionEffect(new PotionEffect(type,duration.tick(),amplifier,false,particles)));
    }
    public void ClearEffects() {
        getOnlinePlayer().ifPresent(
                p-> p.getActivePotionEffects().forEach(
                        e->p.removePotionEffect(e.getType())
                )
        );
    }
    public void SetHealthMax() {
        getOnlinePlayer().ifPresent(
                p-> p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue())
        );
    }
    public void SetFoodLevelMax() {
        getOnlinePlayer().ifPresent(p ->
                {
                    p.setFoodLevel(20);
                    p.setSaturation(20);
                }
        );
    }
    public ItemStack GetItemInMainHand() {
        if(!(getBukkitPlayer().isOnline())) throw new IllegalStateException("Couldn't get "+bukkitPlayer.getName()+"'s item in their main hand because they are offline");
        Player player = (Player) getBukkitPlayer();
        return player.getInventory().getItemInMainHand();
    }
    public void SetGameMode(GameMode gameMode) {
        getOnlinePlayer().ifPresent(p-> p.setGameMode(gameMode));
    }
    public void Teleport(Location location) {
        getOnlinePlayer().ifPresent(p-> p.teleport(location));
    }
    public void setCompassTarget(Location location) {
        getOnlinePlayer().ifPresent(p->p.setCompassTarget(location));
    }
    public Location GetLocation() {
        return getBukkitPlayer().getLocation();
    }
    public String GetName() {
        return getBukkitPlayer().getName();
    }
    @Override
    public boolean equals(Object obj){
        if(obj==this) return true;
        if(obj==null) return false;
        if(!(obj instanceof GamePlayer)) return false;
        GamePlayer gamePlayer = (GamePlayer) obj;
        return this.getBukkitPlayer().getUniqueId().equals(gamePlayer.getBukkitPlayer().getUniqueId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.getBukkitPlayer().getUniqueId());
    }

    public OfflinePlayer getBukkitPlayer() {
        return bukkitPlayer;
    }
    public Optional<Player> getOnlinePlayer() {
        if(getBukkitPlayer().isOnline()) {
            return Optional.of((Player)getBukkitPlayer());
        } else {
            return Optional.empty();
        }
    }
}
