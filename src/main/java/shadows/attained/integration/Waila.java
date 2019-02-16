package shadows.attained.integration;

/*
public class Waila {

	public static void callbackRegister(IWailaRegistrar registrar) {
		registrar.registerStackProvider(new Provider(), BlockSoil.class);
		registrar.registerStackProvider(new Provider(), BlockBulb.class);
		registrar.registerStackProvider(new Provider(), BlockPlant.class);
		registrar.registerBodyProvider(new Provider(), BlockPlant.class);
		registrar.registerBodyProvider(new Provider(), BlockSoil.class);
		registrar.registerBodyProvider(new Provider(), BlockCreator.class);
	}

	public static class Provider implements IWailaDataProvider {

		@Override
		public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
			if (accessor.getBlock() != null) {
				IBlockState state = accessor.getWorld().getBlockState(accessor.getPosition());
				Block block = state.getBlock();
				if (block instanceof BlockSoil) return state.getValue(BlockSoil.SOIL).get();
				if (block instanceof BlockBulb) return state.getValue(BlockBulb.BULB).get();
				if (block instanceof BlockPlant) return new ItemStack(block, 1, 0);
			}
			return null;
		}

		@Override
		public List<String> getWailaBody(ItemStack stack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
			if (accessor.getBlock() != null) {
				BlockPos pos = accessor.getPosition();
				World world = accessor.getWorld();
				IBlockState state = world.getBlockState(pos);
				Block block = state.getBlock();
				if (block instanceof BlockPlant) {
					currenttip.add(I18n.format("tooltip.attaineddrops2.growth") + " " + (int) (100 * ((float) state.getValue(BlockPlant.AGE) / ((BlockPlant) ModRegistry.PLANT).getMaxAge())) + "%");
					int k = (int) (100 * (1F / (5F - (float) state.getValue(BlockPlant.CHARGE))));
					currenttip.add(I18n.format("tooltip.attaineddrops2.revertchance") + " " + (k > 20 ? k : 0) + "%");
					IBlockState state2 = BlockSoil.getBulbFromState(world.getBlockState(pos.down()));
					currenttip.add(I18n.format("tooltip.attaineddrops2.growing") + " " + (state2 == null ? I18n.format("tooltip.attaineddrops2.nothing") : ModRegistry.BULB.getPickBlock(state2, null, null, null, null).getDisplayName()));
				} else if (block instanceof BlockSoil) {
					if (state.getValue(BlockSoil.SOIL) != SoilType.NONE) {
						currenttip.add(I18n.format("tooltip.attaineddrops2.enrichedwith", BulbType.values()[state.getValue(BlockSoil.SOIL).ordinal() - 1].getDisplayName()));
					}
				} else if (block instanceof BlockCreator) {
					currenttip.add(I18n.format("tooltip.attaineddrops2.creatorcharge", state.getValue(BlockCreator.CHARGE)));
				}
			}
			return currenttip;
		}

		@Override
		public List<String> getWailaHead(ItemStack stack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
			return currenttip;
		}

		@Override
		public List<String> getWailaTail(ItemStack stack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
			return currenttip;
		}

		@Override
		public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
			return tag;
		}
	}

}
*/