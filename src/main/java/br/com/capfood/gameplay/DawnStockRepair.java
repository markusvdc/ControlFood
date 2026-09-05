package br.com.capfood.gameplay;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public final class DawnStockRepair {
	private static final Map<Item, Integer> CONTROL_TRADE_INPUTS = Map.of(
		Items.EGG, 20, Items.COCOA_BEANS, 20, Items.HONEYCOMB, 10,
		Items.SPIDER_EYE, 15, Items.ENDER_PEARL, 3, Items.REDSTONE, 20,
		Items.LAPIS_LAZULI, 20, Items.BONE, 20, Items.ARROW, 15
	);

	private DawnStockRepair() {
	}

	public static void removeDuplicateControlTradeOffers(MerchantOffers offers) {
		if (!FabricLoader.getInstance().isModLoaded("smarttrade")) {
			return;
		}
		Map<Item, MerchantOffer> originals = new HashMap<>();
		var iterator = offers.iterator();
		while (iterator.hasNext()) {
			MerchantOffer offer = iterator.next();
			ItemStack input = offer.getBaseCostA();
			Integer count = CONTROL_TRADE_INPUTS.get(input.getItem());
			if (count == null || input.getCount() != count || offer.getMaxUses() != 12
				|| offer.getXp() != 2 || Float.compare(offer.getPriceMultiplier(), 0.05F) != 0
				|| !offer.shouldRewardExp() || !offer.getCostB().isEmpty()
				|| !ItemStack.matches(input, new ItemStack(input.getItem(), count))
				|| !ItemStack.matches(offer.getResult(), new ItemStack(Items.EMERALD))) {
				continue;
			}
			MerchantOffer original = originals.putIfAbsent(input.getItem(), offer);
			if (original != null) {
				// Retain the first offer's prices and all consumed uses, even when
				// the combined uses exhaust the stock until the next dawn.
				((DawnStockOffer)original).capfood$mergeUsedStock(offer.getUses());
				iterator.remove();
			}
		}
	}
}
