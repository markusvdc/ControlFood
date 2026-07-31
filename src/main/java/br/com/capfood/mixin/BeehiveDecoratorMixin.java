package br.com.capfood.mixin;

import br.com.capfood.config.CapFoodConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeehiveDecorator.class)
public abstract class BeehiveDecoratorMixin {
	private static final float SAPLING_BEE_NEST_CHANCE = 0.05F;
	private static final float INCREASED_SAPLING_BEE_NEST_CHANCE = 0.20F;

	@ModifyExpressionValue(
		method = "place",
		at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;probability:F")
	)
	private float capfood$increaseSaplingBeeNestChance(float original, TreeDecorator.Context context) {
		if (
			CapFoodConfig.increasedSaplingBeeNestChance()
				&& context.level() instanceof ServerLevel
				&& Float.compare(original, SAPLING_BEE_NEST_CHANCE) == 0
		) {
			return INCREASED_SAPLING_BEE_NEST_CHANCE;
		}

		return original;
	}
}
