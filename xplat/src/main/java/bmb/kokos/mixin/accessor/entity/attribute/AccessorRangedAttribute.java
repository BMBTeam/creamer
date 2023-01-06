package bmb.kokos.mixin.accessor.entity.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttribute.class)
public interface AccessorRangedAttribute {
    @Accessor
    void setMinValue(double minValue);

    @Accessor
    void setMaxValue(double maxValue);
}
