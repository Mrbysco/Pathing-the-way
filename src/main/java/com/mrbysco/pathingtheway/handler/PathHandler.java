package com.mrbysco.pathingtheway.handler;

import com.mrbysco.pathingtheway.config.ConfigCache;
import com.mrbysco.pathingtheway.config.PathingConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class PathHandler {

	@SubscribeEvent
	public void onRightClickBlock(RightClickBlock event) {
		final ItemStack stack = event.getItemStack();
		final BlockHitResult blockHitResult = event.getHitVec();
		final BlockPos pos = event.getPos();
		final Level level = event.getLevel();
		BlockState oldState = level.getBlockState(pos);
		ResourceLocation blockLocation = ForgeRegistries.BLOCKS.getKey(level.getBlockState(pos).getBlock());

		if (blockLocation != null && !stack.isEmpty() && stack.getItem() instanceof DiggerItem toolItem && toolItem.blocks.isFor(Registries.BLOCK)) {
			final Player player = event.getEntity();
			TagKey<Block> mineableTag = toolItem.blocks;
			String tagName = mineableTag.location().getPath();
			if (isSneaking(mineableTag, player) && ConfigCache.toolActionMap.containsKey(tagName)) {
				Map<ResourceLocation, ResourceLocation> actionMap = ConfigCache.toolActionMap.get(tagName);
				if (actionMap.containsKey(blockLocation)) {
					ResourceLocation newLoc = actionMap.get(blockLocation);
					Block block = ForgeRegistries.BLOCKS.getValue(newLoc);
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
							stack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(event.getHand()));
						}
						level.playSound(player, pos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
						event.setCanceled(true);
					}
				}
			}
		}
	}

	public boolean isSneaking(TagKey<Block> mineableTag, Player playerEntity) {
		boolean flag = playerEntity.isShiftKeyDown();
		if (mineableTag == BlockTags.MINEABLE_WITH_AXE) {
			return flag == PathingConfig.COMMON.axeSneaking.get();
		} else if (mineableTag == BlockTags.MINEABLE_WITH_PICKAXE) {
			return flag == PathingConfig.COMMON.pickaxeSneaking.get();
		} else if (mineableTag == BlockTags.MINEABLE_WITH_HOE) {
			return flag == PathingConfig.COMMON.hoeSneaking.get();
		} else if (mineableTag == BlockTags.MINEABLE_WITH_SHOVEL) {
			return flag == PathingConfig.COMMON.shovelSneaking.get();
		}
		return true;
	}
}
