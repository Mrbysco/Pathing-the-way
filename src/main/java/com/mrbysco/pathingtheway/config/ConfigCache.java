package com.mrbysco.pathingtheway.config;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCache {
	public static Map<String, Map<ResourceLocation, ResourceLocation>> toolActionMap = new HashMap<>();
	public static boolean shovelSneaking;
	public static boolean pickaxeSneaking;
	public static boolean axeSneaking;
	public static boolean hoeSneaking;

	public static void refreshCache() {
		generateContainerModifier("shovel", PathingConfig.COMMON.shovelPathing.get());
		generateContainerModifier("pickaxe", PathingConfig.COMMON.pickaxeChiseling.get());
		generateContainerModifier("axe", PathingConfig.COMMON.axeStripping.get());
		generateContainerModifier("hoe", PathingConfig.COMMON.hoeTilling.get());
		shovelSneaking = PathingConfig.COMMON.shovelSneaking.get();
		pickaxeSneaking = PathingConfig.COMMON.pickaxeSneaking.get();
		axeSneaking = PathingConfig.COMMON.axeSneaking.get();
		hoeSneaking = PathingConfig.COMMON.hoeSneaking.get();
	}

	private static void generateContainerModifier(String toolname, List<? extends String> configValues) {
		Map<ResourceLocation, ResourceLocation> actionList = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (configValue.contains(",")) {
					String[] splitValue = configValue.split(",");
					if(splitValue.length == 2) {
						actionList.put(new ResourceLocation(splitValue[0]), new ResourceLocation(splitValue[1]));
					}
				}
			}
		}
		toolActionMap.put(toolname, actionList);
	}
}
