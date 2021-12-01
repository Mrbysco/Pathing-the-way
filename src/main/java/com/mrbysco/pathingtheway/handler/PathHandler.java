package com.mrbysco.pathingtheway.handler;

import com.mrbysco.pathingtheway.config.ConfigCache;
import com.mrbysco.pathingtheway.config.PathingConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class PathHandler {

	@SubscribeEvent
	public void onServerStarting(RightClickBlock event) {
		ItemStack stack = event.getItemStack();
		BlockRayTraceResult blockRayTraceResult = event.getHitVec();
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		BlockState oldState = world.getBlockState(pos);
		ResourceLocation blockLocation = world.getBlockState(pos).getBlock().getRegistryName();

		if(blockLocation != null && !stack.isEmpty() && !stack.getToolTypes().isEmpty()) {
			PlayerEntity player = event.getPlayer();
			for (ToolType type : stack.getToolTypes()) {
				String typeName = type.getName();
				if(isSneaking(type, player) && ConfigCache.toolActionMap.containsKey(typeName)) {
					Map<ResourceLocation, ResourceLocation> actionMap = ConfigCache.toolActionMap.get(typeName);
					if(actionMap.containsKey(blockLocation)) {
						ResourceLocation newLoc = actionMap.get(blockLocation);
						Block block = ForgeRegistries.BLOCKS.getValue(newLoc);
						if(block != null) {
							BlockState newState = block.defaultBlockState();
							Direction direction = event.getFace();

							if(oldState.hasProperty(BlockStateProperties.WATERLOGGED) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
								newState.setValue(BlockStateProperties.WATERLOGGED, oldState.getValue(BlockStateProperties.WATERLOGGED));
							}
							if(newState.hasProperty(BlockStateProperties.SLAB_TYPE) && direction != Direction.DOWN && !(direction == Direction.UP || !(blockRayTraceResult.getLocation().y - (double)pos.getY() < 0.5D))) {
								newState = newState.setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP);
							}
							if(newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
								newState = newState.setValue(BlockStateProperties.HORIZONTAL_FACING, player.getDirection());
							}
							if(newState.hasProperty(BlockStateProperties.HALF)) {
								newState = newState.setValue(BlockStateProperties.HALF, direction != Direction.DOWN && (direction == Direction.UP || !(blockRayTraceResult.getLocation().y - (double)pos.getY() < 0.5D)) ? Half.BOTTOM : Half.TOP);
							}
							world.setBlockAndUpdate(pos, newState);
							if(!player.abilities.instabuild) {
								stack.hurtAndBreak(1, player, (playerEntity) -> playerEntity.broadcastBreakEvent(event.getHand()));
							}
							world.playSound(player, pos, newState.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

							break;
						}
					}
				}
			}
		}
	}

	public boolean isSneaking(ToolType type, PlayerEntity playerEntity) {
		boolean flag = playerEntity.isShiftKeyDown();
		if(type == ToolType.AXE) {
			return flag == PathingConfig.COMMON.axeSneaking.get();
		} else if(type == ToolType.PICKAXE) {
			return flag == PathingConfig.COMMON.pickaxeSneaking.get();
		} else if(type == ToolType.HOE) {
			return flag == PathingConfig.COMMON.hoeSneaking.get();
		} else if(type == ToolType.SHOVEL) {
			return flag == PathingConfig.COMMON.shovelSneaking.get();
		}
		return true;
	}
}
