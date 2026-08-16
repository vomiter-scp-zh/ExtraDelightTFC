package com.vomiter.extradelighttfc;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExtraDelightTFCConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue OVEN_REQUIRES_SHEET;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        OVEN_REQUIRES_SHEET = builder
                .comment("If true, the Extra Delight oven requires a sheet in the container slot to process heating and FirmaLife oven recipes.")
                .define("ovenRequiresSheet", true);

        SPEC = builder.build();
    }
}