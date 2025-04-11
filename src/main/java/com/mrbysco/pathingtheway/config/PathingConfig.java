package com.mrbysco.pathingtheway.config;

import com.mrbysco.pathingtheway.PathingTheWay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

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

		Common(ModConfigSpec.Builder builder) {
			builder.comment("Common settings")
					.push("common");

			shovelPathing = builder
					.comment("A list of additional pathing behaviors using shovels [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:podzol,minecraft:dirt_path\"]")
					.defineListAllowEmpty("shovelPathing", List::of, String::new, PathingConfig::isValidOption);

			shovelSneaking = builder
					.comment("Defines if sneaking is required to do custom pathing using shovels")
					.define("shovelSneaking", false);

			pickaxeChiseling = builder
					.comment("A list of additional chiseling behaviors using pickaxe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:stone,minecraft:stone_stairs\"]")
					.defineListAllowEmpty("pickaxeChiseling", List::of, String::new, PathingConfig::isValidOption);

			pickaxeSneaking = builder
					.comment("Defines if sneaking is required to do custom chiseling using pickaxe's")
					.define("pickaxeSneaking", false);

			axeStripping = builder
					.comment("A list of additional stripping behaviors using axe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:stripped_oak_log,minecraft:oak_planks\"]")
					.defineListAllowEmpty("axeStripping", List::of, String::new, PathingConfig::isValidOption);

			axeSneaking = builder
					.comment("Defines if sneaking is required to do custom stripping using axe's")
					.define("axeSneaking", false);

			hoeTilling = builder
					.comment("A list of additional tilling behaviors using hoe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
							"[Example: \"minecraft:podzol,minecraft:farmland\"]")
					.defineListAllowEmpty("hoeTilling", List::of, String::new, PathingConfig::isValidOption);

			hoeSneaking = builder
					.comment("Defines if sneaking is required to do custom tilling using hoe's")
					.define("hoeSneaking", false);

			builder.pop();
		}
	}

	public static boolean isValidOption(Object object) {
		boolean flag = object instanceof String;
		if (flag) {
			String value = (String) object;
			if (value.isEmpty()) {
				return true;
			} else {
				if (value.contains(",")) {
					String[] splitValue = value.split(",");
					if (splitValue.length == 2) {
						return ResourceLocation.tryParse(splitValue[0]) != null &&
								ResourceLocation.tryParse(splitValue[1]) != null;
					}
				}
			}
		}
		return false;
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		PathingTheWay.LOGGER.debug("Loaded Pathing The Way's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		PathingTheWay.LOGGER.debug("Pathing The Way's config just got changed on the file system!");
	}

	@SubscribeEvent
	public static void onReload(final ModConfigEvent configEvent) {
		if (configEvent.getConfig().getModId().equals(PathingTheWay.MOD_ID)) {
			ConfigCache.refreshCache();
		}
	}
}
