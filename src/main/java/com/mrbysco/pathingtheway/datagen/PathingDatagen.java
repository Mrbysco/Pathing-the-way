package com.mrbysco.pathingtheway.datagen;

import com.mrbysco.pathingtheway.PathingTheWay;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PathingDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(true, new PathingLanguageProvider(packOutput));
	}

	public static class PathingLanguageProvider extends LanguageProvider {
		public PathingLanguageProvider(PackOutput packOutput) {
			super(packOutput, PathingTheWay.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			addConfig("common", "Common", "Common settings");

			addConfig("shovelPathing", "Shovel Pathing", "A list of additional pathing behaviors using shovels [Syntax: \"domain:block,domain:replacement\" ]\n" +
					"[Example: \"minecraft:podzol,minecraft:dirt_path\"]");
			addConfig("shovelSneaking", "Shovel Sneaking", "Defines if sneaking is required to do custom pathing using shovels");

			addConfig("pickaxeChiseling", "Pickaxe Chiseling", "A list of additional chiseling behaviors using pickaxe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
					"[Example: \"minecraft:stone,minecraft:stone_stairs\"]");
			addConfig("pickaxeSneaking", "Pickaxe Sneaking", "Defines if sneaking is required to do custom chiseling using pickaxe's");

			addConfig("axeStripping", "Axe Stripping", "A list of additional stripping behaviors using axe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
					"[Example: \"minecraft:stripped_oak_log,minecraft:oak_planks\"]");
			addConfig("axeSneaking", "Axe Sneaking", "Defines if sneaking is required to do custom stripping using axe's");

			addConfig("hoeTilling", "Hoe Tilling", "A list of additional tilling behaviors using hoe's [Syntax: \"domain:block,domain:replacement\" ]\n" +
					"[Example: \"minecraft:podzol,minecraft:farmland\"]");
			addConfig("hoeSneaking", "Hoe Sneaking", "Defines if sneaking is required to do custom tilling using hoe's");

		}

		/**
		 * Add the translation for a config entry
		 *
		 * @param path        The path of the config entry
		 * @param name        The name of the config entry
		 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
		 */
		private void addConfig(String path, String name, @Nullable String description) {
			this.add(PathingTheWay.MOD_ID + ".configuration." + path, name);
			if (description != null && !description.isEmpty())
				this.add(PathingTheWay.MOD_ID + ".configuration." + path + ".tooltip", description);
		}
	}
}
