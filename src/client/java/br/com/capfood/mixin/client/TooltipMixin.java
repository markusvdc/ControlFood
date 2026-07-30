package br.com.capfood.mixin.client;

import br.com.capfood.client.screen.component.GlobalOptionTooltipLine;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Tooltip.class)
public abstract class TooltipMixin {
	private static final int CAPFOOD_GLOBAL_OPTION_TOOLTIP_WIDTH = (int)(170 * 2.5F);

	@Shadow
	@Final
	private Component message;

	@Inject(method = "toCharSequence", at = @At("HEAD"), cancellable = true)
	private void capfood$widenGlobalOptionTooltip(
		Minecraft minecraft,
		CallbackInfoReturnable<List<FormattedCharSequence>> callback
	) {
		if (this.message.getContents() instanceof TranslatableContents contents
			&& contents.getKey().startsWith("capfood.options.")
			&& contents.getKey().endsWith(".description")) {
			List<FormattedCharSequence> lines = minecraft.font.split(
				this.message,
				CAPFOOD_GLOBAL_OPTION_TOOLTIP_WIDTH
			);
			List<FormattedCharSequence> spacedLines = new ArrayList<>(lines.size());
			for (int index = 0; index < lines.size(); index++) {
				spacedLines.add(new GlobalOptionTooltipLine(lines.get(index), index == 0));
			}
			callback.setReturnValue(spacedLines);
		}
	}
}
