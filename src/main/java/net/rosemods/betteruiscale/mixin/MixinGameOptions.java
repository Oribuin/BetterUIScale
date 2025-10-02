package net.rosemods.betteruiscale.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.rosemods.betteruiscale.MaxSuppliableIntSliderCallbacks;
import net.rosemods.betteruiscale.ScaleFactorUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GameOptions.class)
public class MixinGameOptions {
    @ModifyArgs(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/option/SimpleOption.<init> (Ljava/lang/String;Lnet/minecraft/client/option/SimpleOption$TooltipFactory;Lnet/minecraft/client/option/SimpleOption$ValueTextGetter;Lnet/minecraft/client/option/SimpleOption$Callbacks;Ljava/lang/Object;Ljava/util/function/Consumer;)V"
        ),
        allow = 1,
        slice = @Slice(
            from = @At(
                value = "FIELD",
                opcode = Opcodes.PUTFIELD,
                shift = At.Shift.AFTER,
                target = "Lnet/minecraft/client/option/GameOptions;gamma:Lnet/minecraft/client/option/SimpleOption;"
            ),
            to = @At(
                value = "FIELD",
                opcode = Opcodes.PUTFIELD,
                shift = At.Shift.BEFORE,
                target = "Lnet/minecraft/client/option/GameOptions;guiScale:Lnet/minecraft/client/option/SimpleOption;"
            )
        )
    )
    private void modifyGuiScaleOption(Args args) {
        args.set(3, new MaxSuppliableIntSliderCallbacks(0, () -> {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            if (!minecraftClient.isRunning()) {
                return 0x7FFFFFFE;
            }
            return minecraftClient.getWindow().calculateScaleFactor(0, minecraftClient.forcesUnicodeFont());
        }, 0x7FFFFFFE));
        SimpleOption.ValueTextGetter<Integer> textGetter = MixinGameOptions::guiScaleValueToText;
        args.set(2, textGetter);
    }

    @Unique
    private static Text guiScaleValueToText(Text optionText, Integer value) {
        if (value == 0) {
            return GameOptions.getGenericValueText(optionText, Text.translatable("options.guiScale.auto"));
        } else {
            double scale = ScaleFactorUtil.fromInternalScaleFactor(value);
            return Text.translatable("options.percent_value", optionText, (int) (scale * 100f));
        }
    }
}
