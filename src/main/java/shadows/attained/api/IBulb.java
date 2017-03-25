package shadows.attained.api;

import net.minecraft.util.text.TextFormatting;

/**
 * Implement this Interface on your bulb block for it to be recognized by AD2 as
 * a Bulb type<br>
 * Be sure to require AD2 after your mod as a dependency in your
 * {@link net.minecraftforge.fml.common.Mod @Mod} annotion in your mod container
 * class.<br>
 * Use {@link shadows.attained.util.AD2Util#registerAD2 AD2Util#registerAD2} to
 * register your bulb
 *
 * @author p455w0rd
 *
 */
public interface IBulb {

	String getUnlocalizedName();

	TextFormatting getTextColor();

}
