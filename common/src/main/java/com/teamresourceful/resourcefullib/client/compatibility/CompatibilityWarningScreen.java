package com.teamresourceful.resourcefullib.client.compatibility;

import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import com.teamresourceful.resourcefullib.client.screens.BaseCursorScreen;
import com.teamresourceful.resourcefullib.common.comptibility.CompatabilityManager;
import com.teamresourceful.resourcefullib.common.comptibility.options.CompatabilityOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompatibilityWarningScreen extends BaseCursorScreen {

    private static final int PADDING = 10;

    protected CompatibilityWarningScreen() {
        super(CommonComponents.EMPTY);
    }

    @Override
    protected void init() {
        SelectionList<ComptabilityEntry> list = new SelectionList<>(
                PADDING, PADDING,
                this.width - PADDING * 2,
                this.height - PADDING * 3 - 20, 16,
                s -> {}, true
        );

        for (var entry : CompatabilityManager.issues().entrySet()) {
            String id = entry.getKey();
            List<CompatabilityOption<?>> options = entry.getValue();

            list.addEntry(new ComptabilityEntry(
                Component.literal(id + " could not be loaded due to the following reasons.")
            ));

            for (var option : options) {
                list.addEntry(new ComptabilityEntry(
                    false,
                    Component.literal(" - ").append(option.title()),
                    option.url()
                ));

                for (Component component : option.description()) {
                    list.addEntry(new ComptabilityEntry(true, component, option.url()));
                }
            }
            list.addEntry(new ComptabilityEntry());
        }

        addRenderableWidget(list);

        addRenderableWidget(Button.builder(
                Component.translatable("menu.quit"),
                s -> Minecraft.getInstance().stop()
            )
            .pos(this.width / 2 - 100, this.height - PADDING - 20)
            .size(200, 20)
            .build()
        );
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, float f) {
        this.renderDirtBackground(graphics);
        super.render(graphics, i, j, f);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public static Screen tryOpen() {
        if (!CompatabilityManager.issues().isEmpty()) {
            return new CompatibilityWarningScreen();
        }
        return null;
    }
}
