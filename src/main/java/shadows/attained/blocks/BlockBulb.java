package shadows.attained.blocks;

import net.minecraft.util.text.TextFormatting;

/**
 * @author p455w0rd
 *
 */
public class BlockBulb extends BlockBulbBase {

	public BlockBulb(String name, TextFormatting color) {
		this("", name, color);
	}

	public BlockBulb(String modid, String name, TextFormatting color) {
		super(modid, name);
		setTextColor(color);
	}

}
