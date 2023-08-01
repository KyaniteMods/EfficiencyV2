package com.kyanite.efficiency.screen;

import com.kyanite.efficiency.Efficiency;
import com.kyanite.efficiency.screen.widgets.modlist.ModList;
import com.kyanite.efficiency.screen.widgets.modlist.ModListEntry;
import com.kyanite.efficiency.screen.widgets.sidebar.ModSidebar;
import com.kyanite.efficiency.screen.widgets.sidebar.ModSidebarEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class EfficiencyBrowserScreen extends Screen {
    private final Screen parent;

    // Components
    public EditBox searchBox;
    public ModList modList;
    public ModSidebar sideBar;


    public EfficiencyBrowserScreen(@Nullable Screen parent) {
        super(Component.literal("Efficiency Mod Manager"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        this.searchBox = new EditBox(this.font, this.minecraft.screen.width - 260, 15, 200, 20, this.searchBox, Component.translatable("selectWorld.search"));
        ImageButton searchButton = new ImageButton(this.width - 53, 15, 20, 20, 0, 0, 20, new ResourceLocation(Efficiency.MOD_ID, "textures/gui/search-button.png"), 32, 64, button -> {
            this.modList.filter = this.searchBox.getValue();
            try {
                this.modList.refreshMods();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        ImageButton backButton = new ImageButton(this.width - 30, 15, 20, 20, 0, 0, 20, new ResourceLocation(Efficiency.MOD_ID, "textures/gui/back-button.png"), 32, 64, button -> {
            this.minecraft.setScreen(this.parent);
        });

        this.modList = new ModList(this, this.minecraft, this.width / 2 + 500, this.height, 48, this.height - 5, 36);
        this.sideBar = new ModSidebar(this, this.minecraft, 100, this.height, 48, this.height - 5, 25);

        this.addRenderableWidget(searchButton);
        this.addRenderableWidget(backButton);
        this.addRenderableWidget(this.searchBox);
        this.addWidget(this.sideBar);
        this.addWidget(this.modList);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics);
        this.modList.render(graphics, mouseX, mouseY, delta);
        this.sideBar.render(graphics, mouseX, mouseY, delta);

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        return super.keyPressed(i, j, k) ? true : this.searchBox.keyPressed(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.searchBox.charTyped(c, i);
    }
}