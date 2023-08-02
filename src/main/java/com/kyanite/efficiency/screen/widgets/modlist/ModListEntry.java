package com.kyanite.efficiency.screen.widgets.modlist;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.utility.EfficiencyStorage;
import com.kyanite.efficiency.utility.GenericUtils;
import masecla.modrinth4j.endpoints.SearchEndpoint;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ModListEntry extends ObjectSelectionList.Entry<ModListEntry> {
    public final SearchEndpoint.SearchResult project;
    public final ModList modList;
    public final Minecraft minecraft;
    public final ImageButton installButton;
    public ResourceLocation imageTexture;;
    public boolean supported = false;

    public ModListEntry(Minecraft minecraft, ModList modList, SearchEndpoint.SearchResult project) {
        this.modList = modList;
        this.project = project;
        this.minecraft = minecraft;
        this.installButton = new ImageButton(5, 10, 12, 12, 0, 0, 12, new ResourceLocation(Efficiency.MOD_ID, "textures/gui/install-button.png"), 32, 64, button -> {});

        CompletableFuture.runAsync(() -> {
            try {
                if(Efficiency.dataCollector.getVersions(project.getSlug())
                        .stream().filter((version -> version.getLoaders().contains("fabric") && version.getGameVersions().contains(SharedConstants.getCurrentVersion().getName())))
                        .count() > 0) {
                    supported = true;
                }
            } catch (ExecutionException | InterruptedException e) {
                Efficiency.LOGGER.info(e.toString());
            }
        });
    }

    @Override
    public Component getNarration() {
        return Component.literal("Modrinth Entry");
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        this.installButton.mouseClicked(d, e, i);
        modList.setSelected(this);
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        this.installButton.setY(j);
        this.installButton.render(guiGraphics, i, j, k);

        guiGraphics.drawString(minecraft.font, this.project.getTitle(), k + 32 + 3, j + 1, 16777215);
        guiGraphics.drawWordWrap(minecraft.font, FormattedText.of(this.project.getDescription().substring(0, Math.min(this.project.getDescription().length(), 90)) + "..."), k + 32 + 3, j + 10, 200, 0xD6D5CB);
        guiGraphics.blit(this.imageTexture == null ? EfficiencyStorage.ICON_MISSING : this.imageTexture, k, j, 0.0F, 0.0F, 32, 32, 32, 32);

        if(supported) {
            guiGraphics.blit(new ResourceLocation("textures/gui/checkmark.png"), k - 5, j, 0.0F, 0.0F, 8, 8, 8, 8);

            if(GenericUtils.isMouseWithin(k -5, j, 8, 8,n, o)) {
                FormattedCharSequence sequence = Component.translatable("efficiency.mod_supports", SharedConstants.getCurrentVersion().getName()).getVisualOrderText();
                this.modList.browserScreen.setToolTip(Collections.singletonList(sequence));
            }
        }

        if(GenericUtils.isMouseWithin(k + 32 + 3, j + 1, this.minecraft.font.width(this.project.getTitle()) + 5, 5,n, o)) {
            FormattedCharSequence sequence = Component.translatable("efficiency.slug_tooltip", this.project.getSlug()).getVisualOrderText();
            this.modList.browserScreen.setToolTip(Collections.singletonList(sequence));
        }

        if(!this.project.getCategories().isEmpty()) {
            int startX = k + minecraft.font.width(this.project.getTitle()) + 40;
            for (int i2 = 0; i2 < this.project.getCategories().stream().count(); i2++) {
                if(i2 > 3) return;
                String category = this.project.getCategories().get(i2);
                if(category.equals("fabric") || category.equals("forge") || category.equals("fabric")) return;
                else if(category.equals("game-mechanics")) category = "mechanics";
                else if(category.equals("transportation")) category = "mobility";

                GenericUtils.drawSimpleBadge(
                        guiGraphics, minecraft,
                        startX, j,
                        minecraft.font.width(category + 15), FormattedCharSequence.forward(StringUtils.capitalize(category), Style.EMPTY),
                        GenericUtils.getBadgeColor(category), 0xCACACA);

                startX = startX + 20 + minecraft.font.width(category);
            }
        }
    }
}
