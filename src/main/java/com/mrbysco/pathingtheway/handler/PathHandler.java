package com.mrbysco.pathingtheway.handler;

import com.mrbysco.pathingtheway.config.ConfigCache;
import com.mrbysco.pathingtheway.config.PathingConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;

import java.util.Map;

public class PathHandler {

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onRightClickBlock(RightClickBlock event) {
		final ItemStack stack = event.getItemStack();
		final BlockHitResult blockHitResult = event.getHitVec();
		final BlockPos pos = event.getPos();
		final Level level = event.getLevel();
		BlockState oldState = level.getBlockState(pos);
		ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos).getBlock());

		if (blockLocation != null && !stack.isEmpty() && stack.has(DataComponents.TOOL)) {
			final Player player = event.getEntity();
			ItemAbility action = getToolType(stack);
			if (isSneaking(action, player)) {
				Map<ResourceLocation, ResourceLocation> actionMap = ConfigCache.toolActionMap.get(action);
				if (actionMap.containsKey(blockLocation)) {
					ResourceLocation newLoc = actionMap.get(blockLocation);
					Block block = BuiltInRegistries.BLOCK.get(newLoc);
					if (block != null) {
						BlockState newState = block.defaultBlockState();
						final Direction direction = event.getFace();

						if (oldState.hasProperty(BlockStateProperties.WATERLOGGED) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
							newState.setValue(BlockStateProperties.WATERLOGGED, oldState.getValue(BlockStateProperties.WATERLOGGED));
						}
						if (newState.hasProperty(BlockStateProperties.SLAB_TYPE) && direction != Direction.DOWN && !(direction == Direction.UP ||
								!(blockHitResult.getLocation().y - (double) pos.getY() < 0.5D))) {
							newState = newState.setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP);
						}
						if (newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
							newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, player.getDirection());
						}
						if (newState.hasProperty(BlockStateProperties.HALF)) {
							newState = newState.setValue(BlockStateProperties.HALF, direction != Direction.DOWN && (direction == Direction.UP ||
									!(blockHitResult.getLocation().y - (double) pos.getY() < 0.5D)) ? Half.BOTTOM : Half.TOP);
						}
						level.setBlockAndUpdate(pos, newState);
						if (!player.getAbilities().instabuild) {
							stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(event.getHand()));
						}
						level.playSound(player, pos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	public boolean isSneaking(ItemAbility ability, Player playerEntity) {
		if (ability == null) return false;
		boolean flag = playerEntity.isShiftKeyDown();
		if (ability == ItemAbilities.AXE_DIG) {
			return flag == PathingConfig.COMMON.axeSneaking.get();
		} else if (ability == ItemAbilities.PICKAXE_DIG) {
			return flag == PathingConfig.COMMON.pickaxeSneaking.get();
		} else if (ability == ItemAbilities.HOE_DIG) {
			return flag == PathingConfig.COMMON.hoeSneaking.get();
		} else if (ability == ItemAbilities.SHOVEL_DIG) {
			return flag == PathingConfig.COMMON.shovelSneaking.get();
		}
		return true;
	}

	public ItemAbility getToolType(ItemStack stack) {
		if (stack.canPerformAction(ItemAbilities.AXE_DIG)) {
			return ItemAbilities.AXE_DIG;
		} else if (stack.canPerformAction(ItemAbilities.PICKAXE_DIG)) {
			return ItemAbilities.PICKAXE_DIG;
		} else if (stack.canPerformAction(ItemAbilities.HOE_DIG)) {
			return ItemAbilities.HOE_DIG;
		} else if (stack.canPerformAction(ItemAbilities.SHOVEL_DIG)) {
			return ItemAbilities.SHOVEL_DIG;
		}
		return null;
	}
}
