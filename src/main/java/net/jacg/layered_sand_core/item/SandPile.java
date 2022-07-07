package net.jacg.layered_sand_core.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SandPile extends Item {
    private final Identifier belongsToBlock;

    public SandPile(Settings settings, Identifier identifier) {
        super(settings);
        this.belongsToBlock = identifier;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking() && !user.getAbilities().creativeMode && !world.isClient()) {
            ItemStack stack = user.getStackInHand(hand);
            int amount = stack.getCount() / 8;
            if (amount != 0) {
                stack.decrement(amount * 8);
                ItemStack newStack = new ItemStack(Registry.BLOCK.get(belongsToBlock));
                newStack.setCount(amount);
                ItemScatterer.spawn(world, user.getX(), user.getY(), user.getZ(), newStack);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            BlockState blockState = world.getBlockState(context.getBlockPos());
            if (blockState.getBlock() == Registry.BLOCK.get(this.belongsToBlock)) {
                if (blockState.get(Properties.LAYERS) < 8) {
                    world.setBlockState(context.getBlockPos(), blockState.with(Properties.LAYERS, blockState.get(Properties.LAYERS) + 1));
                    context.getStack()
                            .decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }
}
