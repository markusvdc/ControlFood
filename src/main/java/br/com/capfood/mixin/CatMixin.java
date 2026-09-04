package br.com.capfood.mixin;

import br.com.capfood.gameplay.CatReturnHomeGoal;
import br.com.capfood.gameplay.CatTerritory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {
	@Unique private BlockPos capfood$home;
	@Unique private String capfood$homeDimension = "";
	@Unique private boolean capfood$territoryRunning;

	@Shadow
	private void setRelaxStateOne(boolean relaxed) {
		throw new AssertionError();
	}

	protected CatMixin(EntityType<? extends TamableAnimal> type, Level level) {
		super(type, level);
	}

	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void capfood$addReturnHomeGoal(CallbackInfo callback) {
		this.goalSelector.addGoal(2, new CatReturnHomeGoal((Cat)(Object)this));
	}

	@Redirect(method = "mobInteract", at = @At(value = "INVOKE",
		target = "Lnet/minecraft/world/entity/animal/feline/Cat;setOrderedToSit(Z)V"))
	private void capfood$markTerritory(Cat cat, boolean orderedToSit) {
		if (CatTerritory.enabled(cat)) {
			capfood$setHomeHere();
			// Keep the order, but not its pose: vanilla then blocks following, owner
			// teleportation (including panic), and seeking beds or the sleeping owner.
			cat.setOrderedToSit(true);
			capfood$stopPreviousMovement();
			this.capfood$territoryRunning = true;
		} else {
			cat.setOrderedToSit(orderedToSit);
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void capfood$updateTerritory(CallbackInfo callback) {
		if (this.level().isClientSide()) {
			return;
		}
		if (!CatTerritory.enabled(this)) {
			if (this.capfood$home != null) {
				this.capfood$home = null;
				this.capfood$homeDimension = "";
				this.clearHome();
				this.setOrderedToSit(false);
				capfood$stopPreviousMovement();
			}
			this.capfood$territoryRunning = false;
			return;
		}
		if (this.capfood$home == null && !this.isOrderedToSit()) {
			return;
		}
		if (this.capfood$home == null
			|| !this.level().dimension().identifier().toString().equals(this.capfood$homeDimension)) {
			capfood$setHomeHere();
		}
		this.setOrderedToSit(true);
		// A leash can clear the vanilla home. Keep our chosen anchor separately.
		this.setHomeTo(this.capfood$home, CatTerritory.RADIUS);
		if (!this.capfood$territoryRunning) {
			capfood$stopPreviousMovement();
			this.capfood$territoryRunning = true;
		}
	}

	@Unique
	private void capfood$setHomeHere() {
		this.capfood$home = this.blockPosition();
		this.capfood$homeDimension = this.level().dimension().identifier().toString();
		this.setHomeTo(this.capfood$home, CatTerritory.RADIUS);
	}

	@Unique
	private void capfood$stopPreviousMovement() {
		this.goalSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
		this.getNavigation().stop();
		this.setInSittingPose(false);
		Cat cat = (Cat)(Object)this;
		cat.setLying(false);
		this.setRelaxStateOne(false);
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void capfood$saveTerritory(ValueOutput output, CallbackInfo callback) {
		if (this.capfood$home != null) {
			output.store("capfood:cat_home", BlockPos.CODEC, this.capfood$home);
			output.putString("capfood:cat_home_dimension", this.capfood$homeDimension);
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void capfood$loadTerritory(ValueInput input, CallbackInfo callback) {
		this.capfood$home = input.read("capfood:cat_home", BlockPos.CODEC).orElse(null);
		this.capfood$homeDimension = input.getStringOr("capfood:cat_home_dimension", "");
		this.capfood$territoryRunning = false;
	}
}
