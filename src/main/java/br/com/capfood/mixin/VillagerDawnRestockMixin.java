package br.com.capfood.mixin;

import br.com.capfood.config.CapFoodConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.timeline.Timelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public abstract class VillagerDawnRestockMixin {
	@Unique private long capfood$stockDay = Long.MIN_VALUE;
	@Unique private boolean capfood$restockingAtDawn;
	@Shadow private int numberOfRestocksToday;

	@Unique
	private boolean capfood$enabled() {
		Villager villager = (Villager)(Object)this;
		return CapFoodConfig.dawnRestock()
			&& villager.level() instanceof ServerLevel level && level.getServer().isSingleplayer();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void capfood$checkDawn(CallbackInfo callback) {
		Villager villager = (Villager)(Object)this;
		if (!(villager.level() instanceof ServerLevel level)) {
			return;
		}
		long day = level.registryAccess().get(Timelines.OVERWORLD_DAY)
			.map(timeline -> (long)timeline.value().getPeriodCount(level.clockManager()))
			.orElse(Long.MIN_VALUE);
		if (day == Long.MIN_VALUE) {
			return;
		}
		if (!capfood$enabled() || this.capfood$stockDay == Long.MIN_VALUE || day < this.capfood$stockDay) {
			// Initial activation and clock rewinds must not grant extra stock.
			this.capfood$stockDay = day;
			return;
		}
		if (day == this.capfood$stockDay) {
			return;
		}
		// One refill even after several unloaded days; no accumulated refills.
		this.capfood$stockDay = day;
		this.numberOfRestocksToday = 0;
		this.capfood$restockingAtDawn = true;
		try {
			// Keep vanilla demand updates and synchronize an open trading screen.
			villager.restock();
		} finally {
			this.capfood$restockingAtDawn = false;
		}
	}

	@Inject(method = "shouldRestock", at = @At("HEAD"), cancellable = true)
	private void capfood$skipWorkRestocks(ServerLevel level, CallbackInfoReturnable<Boolean> callback) {
		if (capfood$enabled()) {
			// Also bypass vanilla catch-up, which resets offer uses on its own.
			callback.setReturnValue(false);
		}
	}

	@Inject(method = "restock", at = @At("HEAD"), cancellable = true)
	private void capfood$onlyRestockAtDawn(CallbackInfo callback) {
		if (capfood$enabled() && !this.capfood$restockingAtDawn) {
			callback.cancel();
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void capfood$saveStockDay(ValueOutput output, CallbackInfo callback) {
		if (this.capfood$stockDay != Long.MIN_VALUE) {
			output.putLong("capfood:stock_day", this.capfood$stockDay);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void capfood$loadStockDay(ValueInput input, CallbackInfo callback) {
		this.capfood$stockDay = input.getLongOr("capfood:stock_day", Long.MIN_VALUE);
	}
}
