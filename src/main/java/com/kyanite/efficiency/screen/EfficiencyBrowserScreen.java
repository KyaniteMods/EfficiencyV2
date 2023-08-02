package com.kyanite.efficiency.screen;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.screen.widgets.modlist.ModList;
import com.kyanite.efficiency.screen.widgets.modlist.ModListEntry;
import com.kyanite.efficiency.screen.widgets.sidebar.ModSidebar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EfficiencyBrowserScreen extends Screen {
    private final Screen parent;
    private List<FormattedCharSequence> toolTip;

    // Components
    public EditBox searchBox;
    public ModList modList;
    public ModSidebar sideBar;
    public Button modViewBackButton;
    public Button modViewInstallButton;


    public EfficiencyBrowserScreen(@Nullable Screen parent) {
        super(Component.literal("Efficiency Mod Browser"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.initModViewButtons();
        this.searchBox = this.addRenderableWidget(new EditBox(this.font, this.minecraft.screen.width - 260, 15, 200, 20, this.searchBox, Component.translatable("selectWorld.search")));

        // Search Button
        this.addRenderableWidget(new ImageButton(this.width - 53, 15, 20, 20, 0, 0, 20, new ResourceLocation(Efficiency.MOD_ID, "textures/gui/search-button.png"), 32, 64, button -> {
            this.modList.filter = this.searchBox.getValue();
            try {
                this.modList.refreshMods();
            } catch (ExecutionException | InterruptedException e) {
                Efficiency.LOGGER.info(e.toString());
            }
        }));

        // Back Button
        this.addRenderableWidget(new ImageButton(this.width - 30, 15, 20, 20, 0, 0, 20, new ResourceLocation(Efficiency.MOD_ID, "textures/gui/back-button.png"), 32, 64, button -> {
            this.minecraft.setScreen(this.parent);
        }));

        // Clear filters button
        this.addRenderableWidget(Button.builder(Component.translatable("efficiency.clear_filters"), (button) -> {
            try {
                this.sideBar.refreshCategories();
            } catch (ExecutionException | InterruptedException e) {
                Efficiency.LOGGER.info(e.toString());
            }
        }).bounds(5, this.height - 23, 95, 18).build());

        this.sideBar = this.addWidget(new ModSidebar(this, this.minecraft, 100, this.height, 48, this.height - 28, 25));
        this.modList = this.addWidget(new ModList(this, this.minecraft, this.width / 2 + 500, this.height, 48, this.height - 5, 48));

        this.setInitialFocus(this.searchBox);
    }

    public void initModViewButtons() {
        // Mod View Buttons
        this.modViewBackButton = this.addRenderableWidget(Button.builder(Component.literal("Close"), (button) -> {
            this.modList.setModView(false);
        }).bounds(110, this.height - 23, 100, 18).build());
        this.modViewBackButton.active = false;
        this.modViewBackButton.visible = false;

        this.modViewInstallButton = this.addRenderableWidget(Button.builder(Component.literal("Install"), (button) -> {

        }).bounds(110, this.height - 43, 100, 18).build());
        this.modViewInstallButton.active = false;
        this.modViewInstallButton.visible = false;
    }
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        this.toolTip = null;

        this.modList.render(graphics, mouseX, mouseY, delta);
        this.sideBar.render(graphics, mouseX, mouseY, delta);

        if(modList.children().size() != 0){
            graphics.drawCenteredString(minecraft.font, Component.translatable("efficiency.mod_count", modList.children().size()), 150, 34, 0xFFFFFF);
        }

        graphics.drawString(minecraft.font, Component.translatable("efficiency.filters"), 25, 8, 0xFFFFFF);
        graphics.blit(new ResourceLocation(Efficiency.MOD_ID, "icon.png"), 5, 3, 0.0F, 0.0F, 16, 16, 16, 16);

        if (this.toolTip != null) {
            graphics.renderTooltip(this.font, this.toolTip, mouseX, mouseY);
        }

        super.render(graphics, mouseX, mouseY, delta);
    }

    public void setToolTip(List<FormattedCharSequence> list) {
        this.toolTip = list;
    }
}