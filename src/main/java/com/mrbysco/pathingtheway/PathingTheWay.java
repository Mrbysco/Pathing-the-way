package com.mrbysco.pathingtheway;

import com.mrbysco.pathingtheway.config.PathingConfig;
import com.mrbysco.pathingtheway.handler.PathHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("pathingtheway")
public class PathingTheWay
{
    public static final Logger LOGGER = LogManager.getLogger("pathingtheway");

    public PathingTheWay() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(Type.COMMON, PathingConfig.serverSpec);
        eventBus.register(PathingConfig.class);

        MinecraftForge.EVENT_BUS.register(new PathHandler());
    }
}
