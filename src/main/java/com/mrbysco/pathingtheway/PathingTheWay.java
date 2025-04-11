package com.mrbysco.pathingtheway;

import com.mojang.logging.LogUtils;
import com.mrbysco.pathingtheway.config.PathingConfig;
import com.mrbysco.pathingtheway.handler.PathHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(PathingTheWay.MOD_ID)
public class PathingTheWay {
	public static final String MOD_ID = "pathingtheway";
	public static final Logger LOGGER = LogUtils.getLogger();

	public PathingTheWay(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.COMMON, PathingConfig.commonSpec);
		eventBus.register(PathingConfig.class);

		NeoForge.EVENT_BUS.register(new PathHandler());

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}
}
