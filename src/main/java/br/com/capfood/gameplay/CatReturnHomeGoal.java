package br.com.capfood.gameplay;

import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.animal.feline.Cat;

public final class CatReturnHomeGoal extends MoveTowardsRestrictionGoal {
	private final Cat cat;

	public CatReturnHomeGoal(Cat cat) {
		super(cat, Cat.WALK_SPEED_MOD);
		this.cat = cat;
	}

	private boolean canReturn() {
		return CatTerritory.enabled(this.cat) && this.cat.isOrderedToSit() && this.cat.hasHome()
			&& !this.cat.isLeashed() && !this.cat.isPassenger();
	}

	@Override
	public boolean canUse() {
		return canReturn() && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return canReturn() && !this.cat.isWithinHome() && super.canContinueToUse();
	}

	@Override
	public void stop() {
		this.cat.getNavigation().stop();
		super.stop();
	}
}
