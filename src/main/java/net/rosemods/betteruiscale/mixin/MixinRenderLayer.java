package net.rosemods.betteruiscale.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(RenderLayer.class)
public class MixinRenderLayer {
    @ModifyConstant(
            method = "method_34834",
            constant = @Constant(intValue = 0, ordinal = 0, log = true),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderPhase$Texture;<init>(Lnet/minecraft/util/Identifier;Z)V", ordinal = 0, shift = At.Shift.BY, by = -2),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderPhase$Texture;<init>(Lnet/minecraft/util/Identifier;Z)V", ordinal = 0)
            ))

    private static int enableBilinear(int bool) {
        return 1;
    }

}
