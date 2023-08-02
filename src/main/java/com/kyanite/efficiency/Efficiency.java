package com.kyanite.efficiency;

import com.kyanite.efficiency.utility.ModrinthDataCollector;
import masecla.modrinth4j.client.agent.UserAgent;
import masecla.modrinth4j.main.ModrinthAPI;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class Efficiency implements ModInitializer {
    public static final String MOD_ID = "efficiency";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ModrinthAPI client;
    public static ModrinthDataCollector dataCollector;

    @Override
    public void onInitialize() {
        UserAgent agent = UserAgent.builder().projectName("Efficiency Mod Browser")
                .authorUsername("kyanite-mods")
                .projectVersion("1.0.0").build();

        Efficiency.client = ModrinthAPI.rateLimited(agent, "https://api.modrinth.com/v2", "");
        Efficiency.dataCollector = new ModrinthDataCollector(Efficiency.client);

        LOGGER.info("Efficiency connected to Modrinth!");
    }
}
