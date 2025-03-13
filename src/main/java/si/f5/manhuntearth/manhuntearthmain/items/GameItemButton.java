package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import si.f5.manhuntearth.manhuntearthmain.GamePlayer;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class GameItemButton extends GameItem implements Listener {
    final Enchantment ENCHANTMENT_FOR_IDENTIFICATION=Enchantment.UNBREAKING;
    final int LEVEL_OF_ENCHANTMENT_FOR_IDENTIFICATION=100;
    public GameItemButton(JavaPlugin javaPlugin) {
        ItemMeta itemMeta = super.GetItemStack().getItemMeta();
        Objects.requireNonNull(itemMeta).addEnchant(ENCHANTMENT_FOR_IDENTIFICATION,LEVEL_OF_ENCHANTMENT_FOR_IDENTIFICATION,true);
        itemMeta.addEnchant(Enchantment.VANISHING_CURSE,1,true);
        super.GetItemStack().setItemMeta(itemMeta);
        Bukkit.getServer().getPluginManager().registerEvents(this,javaPlugin);
    }
    @EventHandler
    public void OnInteract(PlayerInteractEvent e) {
        if(!(hasThisItemInMainHand(new GamePlayer(e.getPlayer())))) {
            return;
        }
        e.setCancelled(true);
        if(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) {
            process(e);
        }
    }
    @EventHandler
    public void OnDrop(PlayerDropItemEvent e) {
        if(IsThisItem(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
        if(IsThisItem(e.getCurrentItem())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void OnSwapHand(PlayerSwapHandItemsEvent e) {
        if(IsThisItem(e.getMainHandItem()) || IsThisItem(e.getOffHandItem())) {
            e.setCancelled(true);
        }
    }
    private boolean hasThisItemInMainHand(GamePlayer player) {
        return IsThisItem(player.GetItemInMainHand());
    }
    private boolean IsThisItem(ItemStack itemStack) {
        if(itemStack==null) return false;
        if(itemStack.getType() != this.MATERIAL()) return false;
        for(Map.Entry<Enchantment,Integer> entry:itemStack.getItemMeta().getEnchants().entrySet()) {
            if(entry.getKey().getKey().equals(ENCHANTMENT_FOR_IDENTIFICATION.getKey()) && entry.getValue()==LEVEL_OF_ENCHANTMENT_FOR_IDENTIFICATION) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected Optional<Map<Enchantment, Integer>> ENCHANTMENT() {
        return Optional.empty();
    }
    abstract protected void process(PlayerInteractEvent e);
}
