package net.jacg.layered_sand_core.mixin;

import net.jacg.layered_sand_core.util.SandBlockAccess;
import net.jacg.layered_sand_core.util.Tags;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SandBlock.class)
public abstract class SandBlockMixin extends FallingBlockMixin implements Waterloggable, SandBlockAccess {
	public Item itemBelongsTo;
	private static final IntProperty LAYERS = Properties.LAYERS;
	private static final VoxelShape[] LAYER_SHAPES = new VoxelShape[]{VoxelShapes.empty(), shape(2.0), shape(4.0), shape(6.0), shape(8.0), shape(10.0), shape(12.0), shape(14.0), shape(16.0)};

	public SandBlockMixin(Settings settings) {
		super(settings);
	}

	@Override
	protected void addStates(AbstractBlock.Settings settings, CallbackInfo ci) {
		this.setDefaultState(this.stateManager.getDefaultState().with(LAYERS, 8).with(Properties.WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LAYERS, Properties.WATERLOGGED);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.getStackInHand(hand).isIn(Tags.SAND_SHOVELS)) {
			if (!world.isClient()) {
				int layers = state.get(Properties.LAYERS);
				if (layers > 1) {
					world.setBlockState(pos, state.with(Properties.LAYERS, layers - 1));
				} else {
					world.breakBlock(pos, false);
				}
				if (!player.getAbilities().creativeMode) {
					ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), this.itemBelongsTo.getDefaultStack());
				}
			}
			return ActionResult.SUCCESS;

		}
		return ActionResult.PASS;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		if (state.get(Properties.WATERLOGGED)) {
			return Fluids.WATER.getStill(false);
		}
		return Fluids.EMPTY.getDefaultState();
	}

	@Override
	protected void configureFallingBlockEntity(FallingBlockEntity entity) {
		entity.dropItem = false;
	}

	@Override
	public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
		super.onLanding(world, pos, fallingBlockState, currentStateInPos, fallingBlockEntity);
	}

	@Override
	public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
		BlockState state = fallingBlockEntity.getBlockState();
		BlockState stateDown = world.getBlockState(pos.down());
		System.out.println(world.getBlockState(pos.down()).getBlock().getTranslationKey());
		if (world.getBlockState(pos.down()).getBlock() instanceof SandBlock) {
			System.out.println(stateDown.get(LAYERS));
		}
	}

	/*
	 * prevent water-logging when sand is a full block, since it's quite annoying.
	 * super call crashes the game, and I'm too stupid to find out why. so code duplication it is :)
	 */
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return state.get(LAYERS) != 8 || (!state.get(Properties.WATERLOGGED) && fluid == Fluids.WATER);
	}

	//@Override
	//public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {

	//}

	// Shape Stuff
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LAYER_SHAPES[state.get(LAYERS)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LAYER_SHAPES[state.get(LAYERS)];
	}

	@Override
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return LAYER_SHAPES[state.get(LAYERS)];
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LAYER_SHAPES[state.get(LAYERS)];
	}

	private static VoxelShape shape(double maxY) {
		return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, maxY, 16.0);
	}

	@Override
	public void setBelongsTo(Item item) {
		this.itemBelongsTo = item;
	}
}
