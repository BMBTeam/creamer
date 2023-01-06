package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MultiShotEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiShotEnchantment.class)
public abstract class MixinMultiShotEnchantment extends Enchantment {
    protected MixinMultiShotEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.CROSSBOW, slots);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
