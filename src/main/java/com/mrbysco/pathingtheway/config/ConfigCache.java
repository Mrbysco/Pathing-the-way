package com.mrbysco.pathingtheway.config;

import com.mrbysco.pathingtheway.handler.ToolType;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCache {
	public static Map<ToolType, Map<Identifier, Identifier>> toolActionMap = new HashMap<>();

	public static void refreshCache() {
		generateContainerModifier(ToolType.SHOVEL, PathingConfig.COMMON.shovelPathing.get());
		generateContainerModifier(ToolType.PICKAXE, PathingConfig.COMMON.pickaxeChiseling.get());
		generateContainerModifier(ToolType.AXE, PathingConfig.COMMON.axeStripping.get());
		generateContainerModifier(ToolType.HOE, PathingConfig.COMMON.hoeTilling.get());
	}

	private static void generateContainerModifier(ToolType toolType, List<? extends String> configValues) {
		Map<Identifier, Identifier> actionList = new HashMap<>();
		if (!configValues.isEmpty()) {
			for (String configValue : configValues) {
				if (configValue.contains(",")) {
					String[] splitValue = configValue.split(",");
					if (splitValue.length == 2) {
						actionList.put(Identifier.tryParse(splitValue[0]), Identifier.tryParse(splitValue[1]));
					}
				}
			}
		}
		toolActionMap.put(toolType, actionList);
	}
}
