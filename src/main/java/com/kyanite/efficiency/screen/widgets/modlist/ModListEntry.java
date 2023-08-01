package com.kyanite.efficiency.screen.widgets.modlist;

import masecla.modrinth4j.endpoints.SearchEndpoint;
import masecla.modrinth4j.model.project.Project;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ModListEntry extends ObjectSelectionList.Entry<ModListEntry> {
    public final SearchEndpoint.SearchResult project;
    public final ModList modList;
    public final Minecraft minecraft;

    public ModListEntry(Minecraft minecraft, ModList modList, SearchEndpoint.SearchResult project) {
        this.modList = modList;
        this.project = project;
        this.minecraft = minecraft;
    }
    @Override
    public Component getNarration() {
        return Component.literal("Modrinth Entry");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        guiGraphics.drawString(minecraft.font, this.project.getTitle(), k + 32 + 3, j + 1, 16777215);
        guiGraphics.drawString(minecraft.font, this.project.getDescription().substring(0, Math.min(this.project.getDescription().length(), 75)) + "...", k + 32 + 3, j + 10, 0xD6D5CB);
        guiGraphics.blit(new ResourceLocation("textures/misc/unknown_server.png"), k, j, 0.0F, 0.0F, 32, 32, 32, 32);
    }
}
