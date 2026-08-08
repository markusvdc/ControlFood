package br.com.capfood;

import br.com.capfood.config.CapFoodConfig;
import net.fabricmc.api.ModInitializer;

public final class CapFood implements ModInitializer {
	public static final String MOD_ID = "capfood";

	@Override
	public void onInitialize() {
		CapFoodConfig.load();
	}
}
