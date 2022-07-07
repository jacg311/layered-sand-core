package net.jacg.layered_sand_core.mixin;

import net.minecraft.block.FallingBlock;
import net.minecraft.block.LandingBlock;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends BlockMixin implements LandingBlock {
    public FallingBlockMixin(Settings settings) {
        super(settings);
    }
    @Shadow protected abstract void configureFallingBlockEntity(FallingBlockEntity entity);
}
