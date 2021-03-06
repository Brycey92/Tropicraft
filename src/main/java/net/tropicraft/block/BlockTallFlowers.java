package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTallFlowers extends BlockDoublePlant {

	/** Icon for the stem */
	@SideOnly(Side.CLIENT)
	protected IIcon bottomIcon;

	/** Icons for the top of the block, aka the different flower types */
	@SideOnly(Side.CLIENT)
	protected IIcon[] topIcons;

	/** Names of the flowers this block holds */
	protected String[] names;

	public BlockTallFlowers(String[] names) {
		super();
		this.setBlockName(TCNames.tallFlower);
		this.names = names;
		this.setBlockTextureName(TCNames.tallFlower);
		this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
	}
	
    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) != this) 
        	return super.canBlockStay(world, x, y, z); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        int l = world.getBlockMetadata(x, y, z);
        return l > 0 ? world.getBlock(x, y - 1, z) == this :
        	world.getBlock(x, y + 1, z) == this && 
        	super.canBlockStay(world, x, y, z);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {		
		topIcons = new IIcon[names.length];

		for (int i = 0 ; i < names.length ; i++) {
			topIcons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[i]);
		}

		bottomIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + TCNames.stem);
	}
	
	/**
	 * @return The unlocalized block name
	 */
	@Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }
	
	/**
	 * Get the true name of the block
	 * @param unlocalizedName tile.%truename%
	 * @return The actual name of the block, rather than tile.%truename%
	 */
	protected String getActualName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
	}
	
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
    	for (int i = 0; i < names.length; i++) {
    		list.add(new ItemStack(item, 1, i + 1));
    	}
    }

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? bottomIcon : topIcons[meta - 1];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon func_149888_a(boolean renderFlag, int meta) {
		return renderFlag && meta > 0 ? this.topIcons[meta - 1] : bottomIcon;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int unused) {
		if (meta == 0) {
			return null;
		}
		else {
			return Item.getItemFromBlock(this);
		}
	}

	@Override
	public int getRenderType() {
		return 1;
	}
	
    /**
     * Called when the block is placed in the world.
     */
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        world.setBlock(x, y + 1, z, this, itemstack.getItemDamage(), 3);
    }
	
	/**
	 * canFertilize
	 */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean var5) {
		return false;
	}

	/**
	 * shouldFertilize
	 */
	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		return false;
	}

	/**
	 * fertilize
	 */
	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {

	}
}
