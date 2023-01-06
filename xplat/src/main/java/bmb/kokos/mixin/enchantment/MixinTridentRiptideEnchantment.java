package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.TridentRiptideEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TridentRiptideEnchantment.class)
public class MixinTridentRiptideEnchantment extends Enchantment {
    protected MixinTridentRiptideEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.TRIDENT, slots);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
