package com.vomiter.extradelighttfc.data;

import com.vomiter.extradelighttfc.ExtraDelightTFC;
import com.vomiter.extradelighttfc.registry.EDTFCItems;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations;
import com.vomiter.extradelighttfc.registry.EDTFCDecorations.Kind;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.world.item.DyeColor;
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
        decorations();
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

    private void decorations() {
        add("itemGroup.extradelighttfc.wood_decorations", chinese
                ? "Extra Delight TFC 裝飾方塊" : "Extra Delight TFC Decorations");
        for (Wood wood : Wood.values()) {
            Name woodName = woodName(wood);
            EDTFCDecorations.WOODS.get(wood).forEach((kind, block) -> {
                Name kindName = decorationName(kind);
                add(block.get(), chinese ? woodName.chinese() + kindName.chinese()
                        : woodName.english() + " " + kindName.english());
            });
            EDTFCDecorations.WALLPAPERS.get(wood).forEach((color, block) -> {
                Name colorName = colorName(color);
                add(block.get(), chinese ? colorName.chinese() + "壁紙（" + woodName.chinese() + "飾條）"
                        : colorName.english() + " Wallpaper with " + woodName.english() + " Molding");
            });
        }
    }

    private static Name woodName(Wood value) {
        return switch (value) {
            case ACACIA -> new Name("Acacia", "金合歡");
            case ASH -> new Name("Ash", "梣木");
            case ASPEN -> new Name("Aspen", "白楊木");
            case BIRCH -> new Name("Birch", "樺木");
            case BLACKWOOD -> new Name("Blackwood", "黑檀木");
            case CHESTNUT -> new Name("Chestnut", "栗木");
            case DOUGLAS_FIR -> new Name("Douglas Fir", "花旗松");
            case HICKORY -> new Name("Hickory", "山核桃木");
            case KAPOK -> new Name("Kapok", "木棉木");
            case MANGROVE -> new Name("Mangrove", "紅樹木");
            case MAPLE -> new Name("Maple", "楓木");
            case OAK -> new Name("Oak", "橡木");
            case PALM -> new Name("Palm", "棕櫚木");
            case PINE -> new Name("Pine", "松木");
            case ROSEWOOD -> new Name("Rosewood", "紫檀木");
            case SEQUOIA -> new Name("Sequoia", "紅杉木");
            case SPRUCE -> new Name("Spruce", "雲杉木");
            case SYCAMORE -> new Name("Sycamore", "懸鈴木");
            case WHITE_CEDAR -> new Name("White Cedar", "白雪松");
            case WILLOW -> new Name("Willow", "柳木");
        };
    }

    private static Name colorName(DyeColor value) {
        return switch (value) {
            case WHITE -> new Name("White", "白色");
            case ORANGE -> new Name("Orange", "橙色");
            case MAGENTA -> new Name("Magenta", "洋紅色");
            case LIGHT_BLUE -> new Name("Light Blue", "淺藍色");
            case YELLOW -> new Name("Yellow", "黃色");
            case LIME -> new Name("Lime", "淺綠色");
            case PINK -> new Name("Pink", "粉紅色");
            case GRAY -> new Name("Gray", "灰色");
            case LIGHT_GRAY -> new Name("Light Gray", "淺灰色");
            case CYAN -> new Name("Cyan", "青色");
            case PURPLE -> new Name("Purple", "紫色");
            case BLUE -> new Name("Blue", "藍色");
            case BROWN -> new Name("Brown", "棕色");
            case GREEN -> new Name("Green", "綠色");
            case RED -> new Name("Red", "紅色");
            case BLACK -> new Name("Black", "黑色");
        };
    }

    private static Name decorationName(Kind value) {
        return switch (value) {
            case STEP_STOOL -> new Name("Step Stool", "踏凳");
            case KNIFE_BLOCK -> new Name("Knife Block", "刀座");
            case FOOD_DISPLAY -> new Name("Food Display", "食物展示架");
            case DRIED_CORN_FENCE -> new Name("Dried Corn Fence", "乾玉米柵欄");
            case FRUIT_BOWL -> new Name("Fruit Bowl", "水果碗");
            case HALF_CABINET -> new Name("Half Cabinet", "半深櫥櫃");
            case COUNTER_CABINET -> new Name("Counter Cabinet", "檯面櫥櫃");
            case SINK -> new Name("Sink", "水槽");
        };
    }

    private record Name(String english, String chinese) {}
}
