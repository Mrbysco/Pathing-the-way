package com.mrbysco.pathingtheway.config;

import com.mrbysco.pathingtheway.handler.ToolType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCache {
	public static Map<ToolType, Map<ResourceLocation, ResourceLocation>> toolActionMap = new HashMap<>();

	public static void refreshCache() {
		generateContainerModifier(ToolType.SHOVEL, PathingConfig.COMMON.shovelPathing.get());
		generateContainerModifier(ToolType.PICKAXE, PathingConfig.COMMON.pickaxeChiseling.get());
		generateContainerModifier(ToolType.AXE, PathingConfig.COMMON.axeStripping.get());
		generateContainerModifier(ToolType.HOE, PathingConfig.COMMON.hoeTilling.get());
	}

	private static void generateContainerModifier(ToolType toolType, List<? extends String> configValues) {
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
		toolActionMap.put(toolType, actionList);
	}
}
