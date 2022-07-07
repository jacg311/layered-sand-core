package net.jacg.layered_sand_core.mixin;

import net.jacg.layered_sand_core.LayeredSandCore;
import net.jacg.layered_sand_core.util.SandBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyArg(
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=sand")
            ),
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;",
                    ordinal = 0
            ),
            index = 1)
    private static Block better_beaches$addSandItem(Block block) {
        ((SandBlockAccess)block).setBelongsTo(LayeredSandCore.SAND_PILE);
        return block;
    }

    @ModifyArg(
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=red_sand")
            ),
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;",
                    ordinal = 0
            ),
            index = 1)
    private static Block better_beaches$addRedSandItem(Block block) {
        ((SandBlockAccess)block).setBelongsTo(LayeredSandCore.RED_SAND_PILE);
        return block;
    }
}
