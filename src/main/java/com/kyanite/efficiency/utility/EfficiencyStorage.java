package com.kyanite.efficiency.utility;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.screen.widgets.modlist.ModListEntry;
import com.mojang.blaze3d.platform.NativeImage;
import masecla.modrinth4j.endpoints.SearchEndpoint;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EfficiencyStorage {
    public static List<ResourceLocation> MOD_ICONS = new ArrayList<>();
    public static ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");

    public static void registerIcon(SearchEndpoint.SearchResult searchHit, ModListEntry entry) {
        CompletableFuture.runAsync(() -> {
            if (!MOD_ICONS.contains(new ResourceLocation(Efficiency.MOD_ID, searchHit.getSlug()))) {
                try {
                    if(!searchHit.getIconUrl().endsWith(".png") || searchHit.getIconUrl() == null) {
                        entry.imageTexture = ICON_MISSING;
                        return;
                    }
                    NativeImage icon = null;
                    icon = NativeImage.read(new URL(searchHit.getIconUrl()).openStream());
                    DynamicTexture dynamicTexture = new DynamicTexture(icon);
                    if (dynamicTexture != null && icon != null) {
                        entry.minecraft.getTextureManager().register(new ResourceLocation(Efficiency.MOD_ID, searchHit.getSlug()), dynamicTexture);
                        entry.imageTexture = new ResourceLocation(Efficiency.MOD_ID, searchHit.getSlug());
                        MOD_ICONS.add(new ResourceLocation(Efficiency.MOD_ID, searchHit.getSlug()));
                    }else{
                        entry.imageTexture = ICON_MISSING;
                    }
                }catch (Exception e) {
                    Efficiency.LOGGER.info(e.toString());
                }
            }else{
                entry.imageTexture = new ResourceLocation(Efficiency.MOD_ID, searchHit.getSlug());
            }
        });
    }
}
