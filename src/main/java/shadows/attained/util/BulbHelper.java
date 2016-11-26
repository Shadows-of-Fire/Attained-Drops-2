package shadows.attained.util;

import shadows.attained.config.BulbInfo;

public class BulbHelper
{
	public static Boolean isDropEnabled(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.EnabledBulbs0.getBoolean();
			case 1:
				return BulbInfo.EnabledBulbs1.getBoolean();
			case 2:
				return BulbInfo.EnabledBulbs2.getBoolean();
			case 3:
				return BulbInfo.EnabledBulbs3.getBoolean();
			case 4:
				return BulbInfo.EnabledBulbs4.getBoolean();
			case 5:
				return BulbInfo.EnabledBulbs5.getBoolean();
			case 6:
				return BulbInfo.EnabledBulbs6.getBoolean();
			case 7:
				return BulbInfo.EnabledBulbs7.getBoolean();
			case 8:
				return BulbInfo.EnabledBulbs8.getBoolean();
			default:
				return true;
		}
	}

	public static int getBulbRate(int dropNumber)
	{
		switch (dropNumber)
		{
			case 1:
				return BulbInfo.SproutChance0.getInt();
			case 2:
				return BulbInfo.SproutChance1.getInt();
			case 3:
				return BulbInfo.SproutChance2.getInt();
			case 4:
				return BulbInfo.SproutChance3.getInt();
			case 5:
				return BulbInfo.SproutChance4.getInt();
			case 6:
				return BulbInfo.SproutChance5.getInt();
			case 7:
				return BulbInfo.SproutChance6.getInt();
			case 8:
				return BulbInfo.SproutChance7.getInt();
			case 9:
				return BulbInfo.SproutChance8.getInt();
			default:
				return 1;
		}
	}

	public static boolean getCanSoilReset(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.SoilCanUnEnrich0.getBoolean();
			case 1:
				return BulbInfo.SoilCanUnEnrich1.getBoolean();
			case 2:
				return BulbInfo.SoilCanUnEnrich2.getBoolean();
			case 3:
				return BulbInfo.SoilCanUnEnrich3.getBoolean();
			case 4:
				return BulbInfo.SoilCanUnEnrich4.getBoolean();
			case 5:
				return BulbInfo.SoilCanUnEnrich5.getBoolean();
			case 6:
				return BulbInfo.SoilCanUnEnrich6.getBoolean();
			case 7:
				return BulbInfo.SoilCanUnEnrich7.getBoolean();
			case 8:
				return BulbInfo.SoilCanUnEnrich8.getBoolean();
			default:
				return true;
		}
	}

	public static int getSoilResetChance(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.SoilUnEnrich0.getInt();
			case 1:
				return BulbInfo.SoilUnEnrich1.getInt();
			case 2:
				return BulbInfo.SoilUnEnrich2.getInt();
			case 3:
				return BulbInfo.SoilUnEnrich3.getInt();
			case 4:
				return BulbInfo.SoilUnEnrich4.getInt();
			case 5:
				return BulbInfo.SoilUnEnrich5.getInt();
			case 6:
				return BulbInfo.SoilUnEnrich6.getInt();
			case 7:
				return BulbInfo.SoilUnEnrich7.getInt();
			case 8:
				return BulbInfo.SoilUnEnrich8.getInt();
			default:
				return 1;
		}
	}

	public static boolean canBonemealBulb(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.EnableBonemealBulb0.getBoolean();
			case 1:
				return BulbInfo.EnableBonemealBulb1.getBoolean();
			case 2:
				return BulbInfo.EnableBonemealBulb2.getBoolean();
			case 3:
				return BulbInfo.EnableBonemealBulb3.getBoolean();
			case 4:
				return BulbInfo.EnableBonemealBulb4.getBoolean();
			case 5:
				return BulbInfo.EnableBonemealBulb5.getBoolean();
			case 6:
				return BulbInfo.EnableBonemealBulb6.getBoolean();
			case 7:
				return BulbInfo.EnableBonemealBulb7.getBoolean();
			case 8:
				return BulbInfo.EnableBonemealBulb8.getBoolean();
			default:
				return false;
		}
	}

	public static int chanceForBoneMeal(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.BonemealChance0.getInt();
			case 1:
				return BulbInfo.BonemealChance1.getInt();
			case 2:
				return BulbInfo.BonemealChance2.getInt();
			case 3:
				return BulbInfo.BonemealChance3.getInt();
			case 4:
				return BulbInfo.BonemealChance4.getInt();
			case 5:
				return BulbInfo.BonemealChance5.getInt();
			case 6:
				return BulbInfo.BonemealChance6.getInt();
			case 7:
				return BulbInfo.BonemealChance7.getInt();
			case 8:
				return BulbInfo.BonemealChance8.getInt();
			default:
				return 1;
		}
	}

	public static int staticDropNumber(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.StaticDrop0.getInt();
			case 1:
				return BulbInfo.StaticDrop1.getInt();
			case 2:
				return BulbInfo.StaticDrop2.getInt();
			case 3:
				return BulbInfo.StaticDrop3.getInt();
			case 4:
				return BulbInfo.StaticDrop4.getInt();
			case 5:
				return BulbInfo.StaticDrop5.getInt();
			case 6:
				return BulbInfo.StaticDrop6.getInt();
			case 7:
				return BulbInfo.StaticDrop7.getInt();
			case 8:
				return BulbInfo.StaticDrop8.getInt();
			default:
				return 1;
		}
	}

	public static int dynamicDropNumber(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.DynamicDrop0.getInt();
			case 1:
				return BulbInfo.DynamicDrop1.getInt();
			case 2:
				return BulbInfo.DynamicDrop2.getInt();
			case 3:
				return BulbInfo.DynamicDrop3.getInt();
			case 4:
				return BulbInfo.DynamicDrop4.getInt();
			case 5:
				return BulbInfo.DynamicDrop5.getInt();
			case 6:
				return BulbInfo.DynamicDrop6.getInt();
			case 7:
				return BulbInfo.DynamicDrop7.getInt();
			case 8:
				return BulbInfo.DynamicDrop8.getInt();
			default:
				return 1;
		}
	}

	public static boolean canFortuneBulb(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.CanFortune0.getBoolean();
			case 1:
				return BulbInfo.CanFortune1.getBoolean();
			case 2:
				return BulbInfo.CanFortune2.getBoolean();
			case 3:
				return BulbInfo.CanFortune3.getBoolean();
			case 4:
				return BulbInfo.CanFortune4.getBoolean();
			case 5:
				return BulbInfo.CanFortune5.getBoolean();
			case 6:
				return BulbInfo.CanFortune6.getBoolean();
			case 7:
				return BulbInfo.CanFortune7.getBoolean();
			case 8:
				return BulbInfo.CanFortune8.getBoolean();
			default:
				return false;
		}
	}

	public static boolean canSpawnParticles(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.CanParticle0.getBoolean();
			case 1:
				return BulbInfo.CanParticle1.getBoolean();
			case 2:
				return BulbInfo.CanParticle2.getBoolean();
			case 3:
				return BulbInfo.CanParticle3.getBoolean();
			case 4:
				return BulbInfo.CanParticle4.getBoolean();
			case 5:
				return BulbInfo.CanParticle5.getBoolean();
			case 6:
				return BulbInfo.CanParticle6.getBoolean();
			case 7:
				return BulbInfo.CanParticle7.getBoolean();
			case 8:
				return BulbInfo.CanParticle8.getBoolean();
			default:
				return true;
		}
	}

	public static int particleSpawnRate(int dropNumber)
	{
		switch (dropNumber)
		{
			case 0:
				return BulbInfo.ParticleRate0.getInt();
			case 1:
				return BulbInfo.ParticleRate1.getInt();
			case 2:
				return BulbInfo.ParticleRate2.getInt();
			case 3:
				return BulbInfo.ParticleRate3.getInt();
			case 4:
				return BulbInfo.ParticleRate4.getInt();
			case 5:
				return BulbInfo.ParticleRate5.getInt();
			case 6:
				return BulbInfo.ParticleRate6.getInt();
			case 7:
				return BulbInfo.ParticleRate7.getInt();
			case 8:
				return BulbInfo.ParticleRate8.getInt();
			default:
				return 1;
		}
	}

	public static int getXPUse(int dropNumber)
	{
		switch (dropNumber)
		{
			case 1:
				return BulbInfo.XPUse0.getInt();
			case 2:
				return BulbInfo.XPUse1.getInt();
			case 3:
				return BulbInfo.XPUse2.getInt();
			case 4:
				return BulbInfo.XPUse3.getInt();
			case 5:
				return BulbInfo.XPUse4.getInt();
			case 6:
				return BulbInfo.XPUse5.getInt();
			case 7:
				return BulbInfo.XPUse6.getInt();
			case 8:
				return BulbInfo.XPUse7.getInt();
			case 9:
				return BulbInfo.XPUse8.getInt();
			default:
				return 0;
		}
	}
}
