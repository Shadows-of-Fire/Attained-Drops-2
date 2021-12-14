package shadows.attained.blocks;

import java.util.function.Supplier;

import net.minecraft.world.item.ItemStack;
import shadows.attained.api.IAttainedType;

public class CustomBulbType implements IAttainedType {

	String name;
	int color;
	ItemStack stack;
	Supplier<ItemStack> resolver;

	public CustomBulbType(String name, int color, Supplier<ItemStack> stackSupplier) {
		this.name = name;
		this.color = color;
		this.resolver = stackSupplier;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public ItemStack getDrop() {
		return stack == null ? stack = resolver.get() : stack;
	}

	@Override
	public String name() {
		return name;
	}

}
