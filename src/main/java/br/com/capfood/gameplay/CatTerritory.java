package br.com.capfood.gameplay;

import br.com.capfood.config.CapFoodConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;

public final class CatTerritory {
	public static final int RADIUS = 64;

	private CatTerritory() {
	}

	public static boolean enabled(TamableAnimal animal) {
		return CapFoodConfig.catTerritory() && animal instanceof Cat && animal.isTame()
			&& animal.level() instanceof ServerLevel level && level.getServer().isSingleplayer();
	}
}
