package br.com.capfood.mixin;

import br.com.capfood.config.CapFoodConfig;
import br.com.capfood.gameplay.DawnStockOffer;
import br.com.capfood.gameplay.DawnStockRepair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerDawnStockMixin {
	@Inject(method = "getOffers", at = @At("RETURN"))
	private void capfood$applyStockMode(CallbackInfoReturnable<MerchantOffers> callback) {
		AbstractVillager merchant = (AbstractVillager)(Object)this;
		boolean doubled = merchant instanceof Villager && CapFoodConfig.dawnRestock()
			&& merchant.level() instanceof ServerLevel level && level.getServer().isSingleplayer();
		if (merchant instanceof Villager && merchant.level() instanceof ServerLevel level
			&& level.getServer().isSingleplayer()) {
			DawnStockRepair.removeDuplicateControlTradeOffers(callback.getReturnValue());
		}
		for (MerchantOffer offer : callback.getReturnValue()) {
			((DawnStockOffer)offer).capfood$setDoubleStock(doubled);
		}
	}
}
