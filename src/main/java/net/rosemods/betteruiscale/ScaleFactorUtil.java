package net.rosemods.betteruiscale;

import net.minecraft.util.math.MathHelper;

public class ScaleFactorUtil {
    // This is just an optimized version of calcScaleFactorMojang
    public static int calcScaleFactor(int guiScale, boolean forceUnicodeFont, int fbWidth, int fbHeight) {
        int scale;
        if (guiScale == 1) {
            scale = 1;
        } else {
            if (guiScale == 0) {
                // just any high enough value
                scale = Math.min(fbWidth, fbHeight);
            } else {
                scale = Math.max(guiScale, 1);
                scale = Math.min(scale, fbWidth);
                scale = Math.min(scale, fbHeight);
            }

            scale = Math.min(scaleInternalInverse(fbWidth, 320), scale);
            scale = Math.min(scaleInternalInverse(fbHeight, 240), scale);
            scale = Math.max(scale, 1);
        }

        if (forceUnicodeFont && scale % 2 != 0) {
            scale++;
        }

        return scale;
    }

    // This function is `scaleInternal(size, scale+1) < reference`, solved for scale
    public static int scaleInternalInverse(int size, double reference) {
        return MathHelper.ceil((size / (reference - 1) - 1) * 11);
    }

    // This is based on how mojang calculates the scale factor used as a reference
    public static int calcScaleFactorMojang(int guiScale, boolean forceUnicodeFont, int fbWidth, int fwHeight) {
        int scale;
        for (scale = 1; scale != guiScale; scale++) {
            if (scale >= fbWidth) break;
            if (scale >= fwHeight) break;
            if (scaleInternal(fbWidth, scale + 1) < 320) break;
            if (scaleInternal(fwHeight, scale + 1) < 240) break;
        }

        if (forceUnicodeFont && scale % 2 != 0) {
            scale++;
        }

        return scale;
    }

    public static int scaleInternal(int size, int internalScale) {
        double scale = fromInternalScaleFactor(internalScale);
        return MathHelper.ceil(size / scale);
    }

    public static int fromInternalScaleFactor(int scale) {
        // certain scale factors produce rendering errors due to incorrect rounding
        // using a prime number (11) avoids that (I think)
        scale = (scale - 1) / 11 + 1;
        return scale;
    }
}
