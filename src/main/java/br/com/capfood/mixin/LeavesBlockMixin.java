package br.com.capfood.mixin;

import br.com.capfood.gameplay.FastLeafDecay;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void capfood$decayOnAcceleratedTick(
		BlockState state,
		ServerLevel level,
		BlockPos pos,
		RandomSource random,
		CallbackInfo callback
	) {
		if (FastLeafDecay.decayIfDue(level, pos, state)) {
			callback.cancel();
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void capfood$scheduleAcceleratedDecay(
		BlockState state,
		ServerLevel level,
		BlockPos pos,
		RandomSource random,
		CallbackInfo callback
	) {
		FastLeafDecay.scheduleVanillaDecay(level, pos, level.getBlockState(pos));
	}

	@Inject(method = "randomTick", at = @At("HEAD"))
	private void capfood$forgetVanillaDecay(
		BlockState state,
		ServerLevel level,
		BlockPos pos,
		RandomSource random,
		CallbackInfo callback
	) {
		FastLeafDecay.forgetVanillaDecay(level, pos);
	}
}
