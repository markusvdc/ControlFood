package br.com.capfood.client;

import br.com.capfood.config.CapFoodConfig;
import java.util.Locale;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumable;

public final class CapFoodClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
			if (!CapFoodConfig.showFoodProperties()
				|| (!CapFoodConfig.isAllowed(stack.getItem()) && !stack.is(Items.COOKED_BEEF))
				|| !Minecraft.getInstance().hasShiftDown()) {
				return;
			}

			TooltipProperties properties = getProperties(stack);
			if (properties == null) {
				return;
			}

			lines.add(Component.translatable("capfood.tooltip.hunger", properties.hunger()).withStyle(ChatFormatting.GRAY));
			lines.add(Component.translatable(
				"capfood.tooltip.saturation",
				formatDecimal(properties.saturation())
			).withStyle(ChatFormatting.GRAY));
			lines.add(Component.translatable(
				"capfood.tooltip.speed",
				formatDecimal(properties.consumeSeconds())
			).withStyle(ChatFormatting.GRAY));
		});
	}

	private static TooltipProperties getProperties(ItemStack stack) {
		FoodProperties food = stack.get(DataComponents.FOOD);
		Consumable consumable = stack.get(DataComponents.CONSUMABLE);

		if (food != null && consumable != null) {
			return new TooltipProperties(food.nutrition(), food.saturation(), consumable.consumeSeconds());
		}

		if (stack.is(Items.CAKE)) {
			return new TooltipProperties(2, 0.4F, 0.0F);
		}

		return null;
	}

	private static String formatDecimal(float value) {
		return String.format(Locale.ROOT, "%.1f", value);
	}

	private record TooltipProperties(int hunger, float saturation, float consumeSeconds) {
	}
}
