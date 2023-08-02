package com.kyanite.efficiency.screen.widgets.sidebar;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.screen.EfficiencyBrowserScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import masecla.modrinth4j.model.project.ProjectType;
import masecla.modrinth4j.model.search.Facet;
import masecla.modrinth4j.model.search.FacetCollection;
import masecla.modrinth4j.model.tags.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ModSidebar extends ObjectSelectionList<ModSidebarEntry> {
    public EfficiencyBrowserScreen browserScreen;
    private List<Category> currentlyDisplayedCategories = new ArrayList<>();
    public ModSidebar(EfficiencyBrowserScreen browserScreen, Minecraft minecraft, int i, int j, int k, int l, int m) {
        super(minecraft, i, j, k, l, m);
        this.x0 = 5;
        this.y0 = 20;
        this.browserScreen = browserScreen;

        try {
            refreshCategories();
        } catch (ExecutionException | InterruptedException e) {
            Efficiency.LOGGER.info(e.toString());
        }
    }

    public void refreshCategories() throws ExecutionException, InterruptedException {
        this.clearEntries();
        if (this.currentlyDisplayedCategories == null) return;
        this.currentlyDisplayedCategories.clear();

        try {
            for (Category category : Efficiency.dataCollector.getCategories())
                if (category.getProjectType() != null && category.getProjectType().equals(ProjectType.MOD))
                    this.currentlyDisplayedCategories.add(category);
        } catch (ExecutionException | InterruptedException e) {
        }

        for (Category category : this.currentlyDisplayedCategories) {
            ModSidebarEntry entry = new ModSidebarEntry(this, category);
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

    public FacetCollection getFacets() {
        List<ModSidebarEntry> activatedCategories = children().stream().filter(modFilterEntry -> modFilterEntry.checkbox.selected()).toList();
        if(activatedCategories.stream().count() < 1) return null;
        FacetCollection facetCollection = new FacetCollection();
        for(ModSidebarEntry filterEntry : activatedCategories) {
            facetCollection.addPossibleConditions(Facet.category(filterEntry.category.getName()));
        }
        return facetCollection;
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
