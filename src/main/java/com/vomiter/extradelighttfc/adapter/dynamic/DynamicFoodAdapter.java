package com.vomiter.extradelighttfc.adapter.dynamic;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface DynamicFoodAdapter<S, F, R extends Recipe<?>, C extends DynamicFoodContext<R>> {

    /**
     * 執行 result 上與容器有關的額外處理。
     *
     * 例如將 bowl、jar 或其他 container 的 food 資料
     * 套用至輸出物品。
     */
    default void applyContainer(
            ItemStack result,
            S source,
            R recipe
    ) {
    }

    /**
     * 從工作站取得本次 recipe 使用的輸入物品。
     *
     * Calculator 之後會自行：
     * <ol>
     * <li>排除 empty stack</li>
     * <li>將 count 正規化成 1 </li>
     * <li>排序</li>
     * </ol>
     */
    List<ItemStack> getInputs(S source, R recipe);

    List<FluidStack> getInputFluids(F source, R recipe);


    /**
     * 將原始輸入轉換成實際用於計算 FoodData 的形式。
     *
     * 例如：
     * raw meat -> cooked meat
     * raw dough -> baked food
     *
     * 回傳 empty 代表不進行轉換，Calculator 會使用原始 input。
     */
    default ItemStack transformIngredient(
            ItemStack ingredient,
            S source,
            R recipe,
            Level level
    ) {
        return ingredient;
    }

    /**
     * 建立 individual modifier 使用的 context。
     */
    C createIndividualContext(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            ItemStack stack,
            S source,
            F fluid,
            R recipe
    );

    /**
     * 建立 total modifier 使用的 context。
     */
    C createTotalContext(
            List<ItemStack> inputs,
            List<FluidStack> inputFluids,
            ItemStack result,
            S source,
            F fluid,
            R recipe
    );

    /**
     * 聚合後套用的食材數量平衡倍率。
     */
    default float getBalanceMultiplier(
            int foodCount,
            S source,
            R recipe
    ) {
        return Math.max(
                0f,
                1f - 0.05f * foodCount
        );
    }

    /**
     * 輸出食物的基礎 decay modifier。
     */
    default float getDecayModifier(
            S source,
            R recipe
    ) {
        return 4.5f;
    }
}