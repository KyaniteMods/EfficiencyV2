package com.kyanite.efficiency.screen.widgets.modlist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class ModList extends ObjectSelectionList<ModListEntry> {
    public ModList(Minecraft minecraft, int i, int j, int k, int l, int m) {
        super(minecraft, i, j, k, l, m);
        this.x0 = 110;
    }
}
