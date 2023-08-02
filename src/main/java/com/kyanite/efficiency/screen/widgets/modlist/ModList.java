package com.kyanite.efficiency.screen.widgets.modlist;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.utility.EfficiencyStorage;
import com.kyanite.efficiency.screen.EfficiencyBrowserScreen;
import com.kyanite.efficiency.utility.GenericUtils;
import masecla.modrinth4j.endpoints.SearchEndpoint;
import masecla.modrinth4j.model.search.FacetCollection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ModList extends ObjectSelectionList<ModListEntry> {
    public final EfficiencyBrowserScreen browserScreen;
    private List<SearchEndpoint.SearchResult> currentlyDisplayedMods;

    public String filter;
    public FacetCollection facets;

    public boolean modView = false;

    public ModList(EfficiencyBrowserScreen browserScreen, Minecraft minecraft, int i, int j, int k, int l, int m) {
        super(minecraft, i, j, k, l, m);
        this.x0 = 110;
        this.browserScreen = browserScreen;
        try {
            refreshMods();
        } catch (ExecutionException | InterruptedException e) {
            Efficiency.LOGGER.info(e.toString());
        }
    }

    public void setModView(boolean value) {
        this.modView = value;
        this.x0 = value ? 250 : 110;
        browserScreen.modViewBackButton.active = value;
        browserScreen.modViewBackButton.visible = value;

        if(value) browserScreen.modViewInstallButton.active = this.getSelected().supported;
        else browserScreen.modViewInstallButton.active = false;
        browserScreen.modViewInstallButton.visible = value;
    }

    @Override
    public void setSelected(@Nullable ModListEntry entry) {
        super.setSelected(entry);
        setModView(true);
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft() - this.width / 2 + 115;
    }

    public void refreshMods() throws ExecutionException, InterruptedException {
        setModView(false);
        SearchEndpoint.SearchRequest.SearchRequestBuilder query = SearchEndpoint.SearchRequest.builder()
                .limit(40).query(filter);

        query.facets(this.browserScreen.sideBar.getFacets());
        this.currentlyDisplayedMods = Efficiency.dataCollector.getProjects(query.build());

        this.clearEntries();
        if (currentlyDisplayedMods == null) return;

        for (SearchEndpoint.SearchResult searchHit : currentlyDisplayedMods) {
            ModListEntry entry = new ModListEntry(minecraft, this, searchHit);
            EfficiencyStorage.registerIcon(searchHit, entry);
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
    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if(this.children().size() == 0) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(1.4f, 1.4f, 1.4f);
            guiGraphics.drawCenteredString(minecraft.font, Component.translatable("efficiency.no_mods"), this.width / 2 - 150, this.height / 2 - 30, 0xFFFFFF);
            guiGraphics.pose().popPose();
        }

        if(modView) renderModView(guiGraphics, i, j, f);
        super.render(guiGraphics, i, j, f);
    }

    public void renderModView(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.setColor(0.125F, 0.125F, 0.125F, 1.0F);
        guiGraphics.blit(Screen.BACKGROUND_LOCATION, 105, this.y0, 0, 0, 100, 140, 250, 32, 32);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.drawString(minecraft.font, this.getSelected().project.getTitle().substring(0, Math.min(this.getSelected().project.getTitle().length(), 18)), 145, 55, 16777215);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.7f, 0.7f, 0.7f);
        guiGraphics.drawString(minecraft.font, this.getSelected().project.getAuthor(), 208, 95, 16777215);
        guiGraphics.drawString(minecraft.font, Component.literal(this.getSelected().project.getDownloads() + " Downloads"), 208, 105, 16777215);
        guiGraphics.pose().popPose();
        guiGraphics.blit(this.getSelected().imageTexture == null ? EfficiencyStorage.ICON_MISSING : this.getSelected().imageTexture, 110, 50, 0.0F, 0.0F, 32, 32, 32, 32);
        guiGraphics.drawWordWrap(minecraft.font, FormattedText.of(this.getSelected().project.getDescription()), 113, 97, 120, 16777215);

        String category = this.getSelected().project.getCategories().get(0);
        GenericUtils.drawSimpleBadge(
                guiGraphics, minecraft,
                110, 85,
                minecraft.font.width(category + 15), FormattedCharSequence.forward(StringUtils.capitalize(category), Style.EMPTY),
                GenericUtils.getBadgeColor(category), 0xCACACA);
    }
}
