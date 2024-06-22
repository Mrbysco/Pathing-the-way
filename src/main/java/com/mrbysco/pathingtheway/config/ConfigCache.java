package com.mrbysco.pathingtheway.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCache {
	public static Map<ToolAction, Map<ResourceLocation, ResourceLocation>> toolActionMap = new HashMap<>();

	public static void refreshCache() {
		generateContainerModifier(ToolActions.SHOVEL_DIG, PathingConfig.COMMON.shovelPathing.get());
		generateContainerModifier(ToolActions.PICKAXE_DIG, PathingConfig.COMMON.pickaxeChiseling.get());
		generateContainerModifier(ToolActions.AXE_DIG, PathingConfig.COMMON.axeStripping.get());
		generateContainerModifier(ToolActions.HOE_DIG, PathingConfig.COMMON.hoeTilling.get());
	}

	private static void generateContainerModifier(ToolAction toolAction, List<? extends String> configValues) {
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
		toolActionMap.put(toolAction, actionList);
	}
}
