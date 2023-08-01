package com.kyanite.efficiency.screen.widgets.modlist;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class ModListEntry extends ObjectSelectionList.Entry<ModListEntry> {

    @Override
    public Component getNarration() {
        return Component.literal("Modrinth Entry");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {

    }
}
