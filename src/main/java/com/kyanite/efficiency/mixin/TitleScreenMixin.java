package com.kyanite.efficiency.mixin;

import com.kyanite.efficiency.screen.EfficiencyBrowserScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"), cancellable = true)
    public void addButton(CallbackInfo ci) {
        int l =  this.height / 4 + 48;

        this.addRenderableWidget(Button.builder(Component.literal("Browse Mods"), (button) -> {
            this.minecraft.setScreen(new EfficiencyBrowserScreen(this));
        }).bounds(this.width / 2 - 100, l + 72 + 35, 200, 20).build());
    }
}