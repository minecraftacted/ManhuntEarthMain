package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class GameItem{
    protected abstract Material MATERIAL();
    protected abstract Optional<String> NAME();
    protected abstract List<String> LORE();
    protected abstract Optional<Map<Enchantment,Integer>> ENCHANTMENT();
    private final ItemStack itemStack;
    public GameItem() {
        itemStack= new ItemStack(MATERIAL());
        ItemMeta itemMeta=itemStack.getItemMeta();
        NAME().ifPresent(name -> Objects.requireNonNull(itemMeta).setDisplayName(name));
        Objects.requireNonNull(itemMeta).setLore(LORE());
        ENCHANTMENT().ifPresent(m-> m.forEach((key, value) -> itemMeta.addEnchant(key, value, true)));
        itemStack.setItemMeta(itemMeta);
    }
    public ItemStack GetItemStack() {
        return itemStack;
    }
}
