package com.mrbysco.pathingtheway.config;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCache {
	public static Map<String, Map<ResourceLocation, ResourceLocation>> toolActionMap = new HashMap<>();

	public static void refreshCache() {
		generateContainerModifier("mineable/shovel", PathingConfig.COMMON.shovelPathing.get());
		generateContainerModifier("mineable/pickaxe", PathingConfig.COMMON.pickaxeChiseling.get());
		generateContainerModifier("mineable/axe", PathingConfig.COMMON.axeStripping.get());
		generateContainerModifier("mineable/hoe", PathingConfig.COMMON.hoeTilling.get());
	}

	private static void generateContainerModifier(String mineableTagName, List<? extends String> configValues) {
		Map<ResourceLocation, ResourceLocation> actionList = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (configValue.contains(",")) {
					String[] splitValue = configValue.split(",");
					if (splitValue.length == 2) {
						actionList.put(new ResourceLocation(splitValue[0]), new ResourceLocation(splitValue[1]));
					}
				}
			}
		}
		toolActionMap.put(mineableTagName, actionList);
	}
}
