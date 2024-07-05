package com.mrbysco.pathingtheway.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCache {
	public static Map<ItemAbility, Map<ResourceLocation, ResourceLocation>> toolActionMap = new HashMap<>();

	public static void refreshCache() {
		generateContainerModifier(ItemAbilities.SHOVEL_DIG, PathingConfig.COMMON.shovelPathing.get());
		generateContainerModifier(ItemAbilities.PICKAXE_DIG, PathingConfig.COMMON.pickaxeChiseling.get());
		generateContainerModifier(ItemAbilities.AXE_DIG, PathingConfig.COMMON.axeStripping.get());
		generateContainerModifier(ItemAbilities.HOE_DIG, PathingConfig.COMMON.hoeTilling.get());
	}

	private static void generateContainerModifier(ItemAbility itemAbility, List<? extends String> configValues) {
		Map<ResourceLocation, ResourceLocation> actionList = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (configValue.contains(",")) {
					String[] splitValue = configValue.split(",");
					if (splitValue.length == 2) {
						actionList.put(ResourceLocation.tryParse(splitValue[0]), ResourceLocation.tryParse(splitValue[1]));
					}
				}
			}
		}
		toolActionMap.put(itemAbility, actionList);
	}
}
