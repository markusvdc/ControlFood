package br.com.capfood.mixin;

import br.com.capfood.gameplay.CatTerritory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SitWhenOrderedToGoal.class)
public abstract class SitWhenOrderedToGoalMixin {
	@Shadow @Final private TamableAnimal mob;

	@Inject(method = {"canUse", "canContinueToUse"}, at = @At("HEAD"), cancellable = true)
	private void capfood$allowTerritoryWalk(CallbackInfoReturnable<Boolean> callback) {
		if (CatTerritory.enabled(this.mob)) {
			callback.setReturnValue(false);
		}
	}
}
