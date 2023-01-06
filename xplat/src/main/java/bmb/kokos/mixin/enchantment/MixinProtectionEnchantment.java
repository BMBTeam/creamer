package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProtectionEnchantment.class)
public abstract class MixinProtectionEnchantment extends Enchantment {
    protected MixinProtectionEnchantment(Rarity rarity, ProtectionEnchantment.Type type, EquipmentSlot... slots) {
        super(
            rarity,
            type == ProtectionEnchantment.Type.FALL ? EnchantmentCategory.ARMOR_FEET : EnchantmentCategory.ARMOR,
            slots
        );
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
