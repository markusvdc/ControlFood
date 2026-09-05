package br.com.capfood.mixin;

import br.com.capfood.gameplay.DawnStockOffer;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantOffer.class)
public abstract class MerchantOfferDawnStockMixin implements DawnStockOffer {
	@Shadow @Final private int maxUses;
	@Shadow private int uses;
	@Unique private boolean capfood$doubleStock;

	@Override
	public void capfood$setDoubleStock(boolean doubled) {
		this.capfood$doubleStock = doubled;
	}

	@Override
	public int capfood$getEffectiveStock() {
		return this.capfood$doubleStock ? (int)Math.min(Integer.MAX_VALUE, (long)this.maxUses * 2) : this.maxUses;
	}

	@Override
	public void capfood$mergeUsedStock(int additionalUses) {
		this.uses = (int)Math.min(Integer.MAX_VALUE, (long)this.uses + Math.max(0, additionalUses));
	}

	// getMaxUses and the disk codec retain the original offer identity for
	// ControlTrade. Only stock checks and the network use the effective limit.
	@Redirect(method = {"isOutOfStock", "setToOutOfStock", "updateDemand"},
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/trading/MerchantOffer;maxUses:I"))
	private int capfood$effectiveStock(MerchantOffer offer) {
		return this.capfood$getEffectiveStock();
	}

	@Redirect(method = "writeToStream", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/item/trading/MerchantOffer;getMaxUses()I"))
	private static int capfood$sendEffectiveStock(MerchantOffer offer) {
		return ((DawnStockOffer)offer).capfood$getEffectiveStock();
	}

	@Inject(method = "copy", at = @At("RETURN"))
	private void capfood$copyStockMode(CallbackInfoReturnable<MerchantOffer> callback) {
		((DawnStockOffer)callback.getReturnValue()).capfood$setDoubleStock(this.capfood$doubleStock);
	}
}
