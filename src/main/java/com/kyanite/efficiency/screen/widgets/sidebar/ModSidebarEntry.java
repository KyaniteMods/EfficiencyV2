package com.kyanite.efficiency.screen.widgets.sidebar;

import com.kyanite.efficiency.Efficiency;
import masecla.modrinth4j.model.tags.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutionException;

public class ModSidebarEntry extends ObjectSelectionList.Entry<ModSidebarEntry> {
    public final Checkbox checkbox;
    private final ModSidebar sidebar;
    public final Category category;
    public boolean wasSelected = false;
    public ModSidebarEntry(ModSidebar sidebar, Category category) {
        this.sidebar = sidebar;
        this.category = category;
        this.checkbox = new Checkbox(8, 0, 20, 20, Component.literal(
                category.getName().equals("game-mechanics") ? "Mechanics" :
                        category.getName().equals("transportation") ? "Mobility" :
                        StringUtils.capitalize(category.getName())), false);
    }


    @Override
    public boolean mouseClicked(double d, double e, int i) {
        sidebar.setSelected(this);
        this.checkbox.mouseClicked(d, e, i);

        this.sidebar.browserScreen.modList.filter = this.sidebar.browserScreen.searchBox.getValue();
        this.sidebar.browserScreen.modList.facets = this.sidebar.getFacets();
        try {
            this.sidebar.browserScreen.modList.refreshMods();
        } catch (ExecutionException | InterruptedException err) {
            Efficiency.LOGGER.info(err.toString());
        }
        return true;
    }

    @Override
    public Component getNarration() {
        return Component.literal("Category");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        if(this.wasSelected != this.checkbox.selected()) {
            this.wasSelected = this.checkbox.selected();
            if(this.checkbox.selected()) {
                this.sidebar.browserScreen.modList.facets = this.sidebar.getFacets();
            }
        }

        this.checkbox.setY(j);
        this.checkbox.render(guiGraphics, i, j, k);
    }
}
