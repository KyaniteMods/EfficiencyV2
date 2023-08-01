package com.kyanite.efficiency.screen.widgets.modlist;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.screen.EfficiencyBrowserScreen;
import masecla.modrinth4j.endpoints.SearchEndpoint;
import masecla.modrinth4j.model.search.FacetCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ModList extends ObjectSelectionList<ModListEntry> {
    public final EfficiencyBrowserScreen browserScreen;
    private List<SearchEndpoint.SearchResult> currentlyDisplayedMods;

    public String filter;
    public FacetCollection facets;

    public ModList(EfficiencyBrowserScreen browserScreen, Minecraft minecraft, int i, int j, int k, int l, int m) {
        super(minecraft, i, j, k, l, m);
        this.x0 = 110;
        this.browserScreen = browserScreen;
        try {
            refreshMods();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft() - this.width / 2 + 115;
    }

    public void refreshMods() throws ExecutionException, InterruptedException {
        SearchEndpoint.SearchRequest.SearchRequestBuilder query = SearchEndpoint.SearchRequest.builder()
                .limit(50).query(filter);

        if(this.facets != null)
            query.facets(this.facets);
        this.currentlyDisplayedMods = Efficiency.dataCollector.getProjects(query.build());

        this.clearEntries();
        if (currentlyDisplayedMods == null) return;

        for (SearchEndpoint.SearchResult searchHit : currentlyDisplayedMods) {
            ModListEntry entry = new ModListEntry(minecraft, this, searchHit);
            this.addEntry(entry);
        }
    }
    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (i == 0 && d < (double)this.getScrollbarPosition() && e >= (double)this.y0 && e <= (double)this.y1) {
            int j = this.getRowLeft();
            int k = this.getScrollbarPosition();
            int l = (int)Math.floor(e - (double)this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
            int m = l / this.itemHeight;
            if (d >= (double)j && d <= (double)k && m >= 0 && l >= 0 && m < this.getItemCount()) {
                ModListEntry modListEntry = this.getEntryAtPosition2(d, e);
                if(modListEntry != null) {
                    modListEntry.mouseClicked(d, e, i);
                }
            }
        }
        return false;
    }

    public ModListEntry getEntryAtPosition2(double d, double e) {
        int i = this.getRowWidth() + 180;
        int j = this.x0 + this.width / 2;
        int k = j - i;
        int l = j + i;
        int i1 = Mth.floor(e - (double)this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int j1 = i1 / this.itemHeight;
        return d < (double)this.getScrollbarPosition() && d >= (double)k && d <= (double)l && j1 >= 0 && i1 >= 0 && j1 < this.getItemCount() ? this.children().get(j1) : null;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.minecraft.screen.width - 7;
    }

    @Override
    public int getRowRight() {
        return super.getRowRight() + 290;
    }

    @Override
    protected void renderSelection(GuiGraphics guiGraphics, int i, int j, int k, int l, int m) {
        int n = this.x0 ;
        int o = this.x0 + (this.width + j) + 1500;
        guiGraphics.fill(n, i - 2, o, i + k + 2, l);
        guiGraphics.fill(n + 1, i - 1, o - 1, i + k + 1, m);
    }
}
