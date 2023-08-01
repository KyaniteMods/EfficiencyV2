package com.kyanite.efficiency.screen.widgets.sidebar;

import com.kyanite.efficiency.screen.EfficiencyBrowserScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class ModSidebar extends ObjectSelectionList<ModSidebarEntry> {
    public EfficiencyBrowserScreen browserScreen;
    public ModSidebar(EfficiencyBrowserScreen browserScreen, Minecraft minecraft, int i, int j, int k, int l, int m) {
        super(minecraft, i, j, k, l, m);
        this.x0 = 5;
        this.y0 = 5;
        this.browserScreen = browserScreen;
        for (int i3 = 0; i3 < 10; i3++) {
            ModSidebarEntry entry = new ModSidebarEntry(this);
            this.addEntry(entry);
        }
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft();
    }

    @Override
    protected int addEntry(ModSidebarEntry entry) {
        return super.addEntry(entry);
    }

    public int getRowBottom(int i) {
        return this.getRowTop(i);
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() - 80;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        for(ModSidebarEntry filterEntry : children()) {
            filterEntry.checkbox.mouseClicked(d, e, i);
        }
        return false;
    }

    @Override
    protected void renderList(GuiGraphics guiGraphics, int i, int j, float f) {
        int k = this.getRowLeft();
        int l = this.getRowWidth();
        int m = this.itemHeight - 4;
        int n = this.getItemCount();

        for(int o = 0; o < n; ++o) {
            int p = this.getRowTop(o);
            int q = this.getRowBottom(o);
            if (q >= this.y0 && p <= this.y1) {
                this.renderItem(guiGraphics, i, j, f, o, k, p, l, m);
            }
        }
    }

    @Override
    protected void renderSelection(GuiGraphics guiGraphics, int i, int j, int k, int l, int m) {
        int n = this.x0 + (this.getRowWidth() - j);
        int o = this.x0 + (this.getRowWidth() + j) - 330;
        guiGraphics.fill(n, i - 2, o, i + k + 2, l);
        guiGraphics.fill(n + 1, i - 1, o - 1, i + k + 1, m);
    }
}
