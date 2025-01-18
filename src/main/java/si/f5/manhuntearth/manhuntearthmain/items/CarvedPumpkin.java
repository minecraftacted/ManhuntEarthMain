package si.f5.manhuntearth.manhuntearthmain.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.*;

public class CarvedPumpkin extends GameItem{
    @Override
    protected Material MATERIAL() {
        return Material.CARVED_PUMPKIN;
    }

    @Override
    protected Optional<String> NAME() {
        return Optional.of("J A C K");
    }

    @Override
    protected List<String> LORE() {
        return new ArrayList<>(Collections.singletonList("CURSE OF JACK"));
    }

    @Override
    protected Optional<Map<Enchantment, Integer>> ENCHANTMENT() {
        Map<Enchantment,Integer> enchantment=new HashMap<>();
        enchantment.put(Enchantment.BINDING_CURSE,1);
        return Optional.of(enchantment);
    }
}
