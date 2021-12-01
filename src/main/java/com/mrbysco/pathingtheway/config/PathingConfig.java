package com.mrbysco.pathingtheway.config;

import com.mrbysco.pathingtheway.PathingTheWay;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class PathingConfig {
	public static class Common {
		public final BooleanValue shovelSneaking;
		public final ConfigValue<List<? extends String>> shovelPathing;
		public final BooleanValue pickaxeSneaking;
		public final ConfigValue<List<? extends String>> pickaxeChiseling;
		public final BooleanValue axeSneaking;
		public final ConfigValue<List<? extends String>> axeStripping;
		public final BooleanValue hoeSneaking;
		public final ConfigValue<List<? extends String>> hoeTilling;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Common settings")
					.push("common");

			shovelPathing = builder
					.comment("A list of additional pathing behaviors using shovels [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:podzol,minecraft:grass_path\"]")
					.defineList("shovelPathing", Collections.singletonList(""), PathingConfig::isValidOption);

			shovelSneaking = builder
					.comment("Defines if sneaking is required to do custom pathing using shovels")
					.define("shovelSneaking", false);

			pickaxeChiseling = builder
					.comment("A list of additional chiseling behaviors using pickaxe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:stone,minecraft:stone_stairs\"]")
					.defineList("pickaxeChiseling", Collections.singletonList(""), PathingConfig::isValidOption);

			pickaxeSneaking = builder
					.comment("Defines if sneaking is required to do custom chiseling using pickaxe's")
					.define("pickaxeSneaking", false);

			axeStripping = builder
					.comment("A list of additional stripping behaviors using axe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:stripped_oak_log,minecraft:oak_planks\"]")
					.defineList("axeStripping", Collections.singletonList(""), PathingConfig::isValidOption);

			axeSneaking = builder
					.comment("Defines if sneaking is required to do custom stripping using axe's")
					.define("axeSneaking", false);

			hoeTilling = builder
					.comment("A list of additional tilling behaviors using hoe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:podzol,minecraft:farmland\"]")
					.defineList("hoeTilling", Collections.singletonList(""), PathingConfig::isValidOption);

			hoeSneaking = builder
					.comment("Defines if sneaking is required to do custom tilling using hoe's")
					.define("hoeSneaking", false);

			builder.pop();
		}
	}

	public static boolean isValidOption(Object object) {
		boolean flag = object instanceof String;
		if(flag) {
			String value = (String) object;
			if(value.isEmpty()) {
				return true;
			} else {
				if(value.contains(",")) {
					String[] splitValue = value.split(",");
					if(splitValue.length == 2) {
						return isResouceNameValid(splitValue[0]) && isResouceNameValid(splitValue[1]);
					}
				}
			}
		}
		return false;
	}

	private static boolean isResouceNameValid(String resourceName) {
		String[] astring = ResourceLocation.decompose(resourceName, ':');
		return ResourceLocation.isValidNamespace(org.apache.commons.lang3.StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0]) && ResourceLocation.isValidPath(astring[1]);
	}

	public static final ForgeConfigSpec serverSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		serverSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		PathingTheWay.LOGGER.debug("Loaded Pathing The Way's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading configEvent) {
		PathingTheWay.LOGGER.debug("Pathing The Way's config just got changed on the file system!");
	}

	@SubscribeEvent
	public static void onReload(final ModConfig.ModConfigEvent configEvent) {
		ConfigCache.refreshCache();
	}
}
