package com.kyanite.efficiency.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;

public class GenericUtils {
    public static boolean isMouseWithin(int x, int y, int width, int height, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static void drawSimpleBadge(GuiGraphics graphics, Minecraft minecraft, int x, int y, int tagWidth, FormattedCharSequence text, int fillColor, int textColor) {
        graphics.fill(x - 1, y - 1, x + tagWidth + 1, y + minecraft.font.lineHeight - 2 + 1, Color.decode(String.valueOf(fillColor)).darker().getRGB());
        graphics.fill(x + 1, y, x + tagWidth, y + minecraft.font.lineHeight - 2, fillColor);
        graphics.drawString(minecraft.font, text, (int) (x + 1 + (tagWidth - minecraft.font.width(text)) / (float) 2), y, textColor);
    }

    public static int getBadgeColor(String category) {
        if(category.equals("library")) {
            return 0xff354247;
        }else if(category.equals("optimization")) {
            return 0xff107454;
        }else if(category.equals("adventure") || category.equals("cursed")) {
            return 0xff9c3030;
        }else if(category.equals("decoration")) {
            return 0xffbf5513;
        }else if(category.equals("utility") || category.equals("management")) {
            return 0xff47769D;
        }
        else{
            return 0xff107454;
        }
    }
}
