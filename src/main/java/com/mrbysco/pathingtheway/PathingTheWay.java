package com.mrbysco.pathingtheway;

import com.mojang.logging.LogUtils;
import com.mrbysco.pathingtheway.config.PathingConfig;
import com.mrbysco.pathingtheway.handler.PathHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(PathingTheWay.MOD_ID)
public class PathingTheWay {
	public static final String MOD_ID = "pathingtheway";
	public static final Logger LOGGER = LogUtils.getLogger();

	public PathingTheWay(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PathingConfig.serverSpec);
		eventBus.register(PathingConfig.class);

		NeoForge.EVENT_BUS.register(new PathHandler());
	}
}
