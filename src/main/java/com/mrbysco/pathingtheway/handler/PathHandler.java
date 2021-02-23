package com.mrbysco.pathingtheway.handler;

import com.mrbysco.pathingtheway.config.ConfigCache;
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
		BlockPos pos = blockRayTraceResult.getPos();
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
							BlockState newState = block.getDefaultState();
							Direction direction = event.getFace();

							if(oldState.hasProperty(BlockStateProperties.WATERLOGGED) && newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
								newState.with(BlockStateProperties.WATERLOGGED, oldState.get(BlockStateProperties.WATERLOGGED));
							}
							if(newState.hasProperty(BlockStateProperties.SLAB_TYPE) && direction != Direction.DOWN && !(direction == Direction.UP || !(blockRayTraceResult.getHitVec().y - (double)pos.getY() < 0.5D))) {
								newState = newState.with(BlockStateProperties.SLAB_TYPE, SlabType.TOP);
							}
							if(newState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
								newState = newState.with(BlockStateProperties.HORIZONTAL_FACING, player.getHorizontalFacing());
							}
							if(newState.hasProperty(BlockStateProperties.HALF)) {
								newState = newState.with(BlockStateProperties.HALF, direction != Direction.DOWN && (direction == Direction.UP || !(blockRayTraceResult.getHitVec().y - (double)pos.getY() < 0.5D)) ? Half.BOTTOM : Half.TOP);
							}
							world.setBlockState(pos, newState);
							if(!player.abilities.isCreativeMode) {
								stack.damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(event.getHand()));
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
		boolean flag = playerEntity.isSneaking();
		if(type == ToolType.AXE) {
			return flag == ConfigCache.axeSneaking;
		} else if(type == ToolType.PICKAXE) {
			return flag == ConfigCache.pickaxeSneaking;
		} else if(type == ToolType.HOE) {
			return flag == ConfigCache.hoeSneaking;
		} else if(type == ToolType.SHOVEL) {
			return flag == ConfigCache.shovelSneaking;
		}
		return true;
	}
}
