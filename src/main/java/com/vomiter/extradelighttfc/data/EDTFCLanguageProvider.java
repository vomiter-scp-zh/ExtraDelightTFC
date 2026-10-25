package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Map;

public final class EDTFCLanguageProvider extends LanguageProvider {
    private static final Map<Metal.Default, Name> NAMES = Map.of(
            Metal.Default.COPPER, new Name("Copper", "銅"),
            Metal.Default.BRONZE, new Name("Bronze", "青銅"),
            Metal.Default.BISMUTH_BRONZE, new Name("Bismuth Bronze", "鉍青銅"),
            Metal.Default.BLACK_BRONZE, new Name("Black Bronze", "黑青銅"),
            Metal.Default.WROUGHT_IRON, new Name("Wrought Iron", "鍛鐵"),
            Metal.Default.STEEL, new Name("Steel", "鋼"),
            Metal.Default.BLACK_STEEL, new Name("Black Steel", "黑鋼"),
            Metal.Default.BLUE_STEEL, new Name("Blue Steel", "藍鋼"),
            Metal.Default.RED_STEEL, new Name("Red Steel", "紅鋼"));
    private final boolean chinese;

    public EDTFCLanguageProvider(PackOutput output, String locale) {
        super(output, ExtraDelightTFC.MOD_ID, locale);
        chinese = locale.equals("zh_tw");
    }

    @Override
    protected void addTranslations() {
        if (!chinese) {
            add("config.jade.plugin_extradelighttfc.feast_decay", "Feast Decay");
        }
        add("extradelighttfc.tooltip.food_trait.dried", chinese ? "已乾燥" : "Dried");
        pestles();
        add(EDTFCItems.STONE_SPOON_HEAD.get(), chinese ? "石製湯匙頭" : "Stone Spoon Head");
        add(EDTFCItems.STONE_SPOON.get(), chinese ? "石製湯匙" : "Stone Spoon");
        EDTFCItems.METAL_SPOONS.forEach((metal, item) -> {
            Name name = NAMES.get(metal);
            add(item.get(), chinese ? name.chinese() + "湯匙" : name.english() + " Spoon");
        });
    }

    private void pestles() {
        add(EDTFCItems.STONE_PESTLE.get(), chinese ? "石製研杵" : "Stone Pestle");
        EDTFCItems.METAL_PESTLES.forEach((metal, item) -> {
            Name name = NAMES.get(metal);
            add(item.get(), chinese ? name.chinese() + "研杵" : name.english() + " Pestle");
        });
    }

    private record Name(String english, String chinese) {}
}
