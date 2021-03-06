package net.tropicraft.world.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.tropicraft.registry.TCBlockRegistry;

public class WorldGenTallTree extends TCGenBase {

	private static final int VINE_CHANCE = 5;
	private static final int SMALL_LEAF_CHANCE = 3;
	private static final int SECOND_CANOPY_CHANCE = 3;
	
	private static final Block WOOD_BLOCK = TCBlockRegistry.logs;
	private static final int WOOD_META = 1;
	private static final Block LEAF_BLOCK = TCBlockRegistry.rainforestLeaves;
	private static final int LEAF_META = 1;
	
	public WorldGenTallTree(World world, Random random) {
		super(world, random);
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k)
	{
		return generate(i,j,k);
	}
	
	@Override
	public boolean generate(int i, int j, int k) {		
		
		Block blockUnder = worldObj.getBlock(i, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
			return false;
		}
		blockUnder = worldObj.getBlock(i + 1, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
			return false;
		}
		blockUnder = worldObj.getBlock(i - 1, j - 1, k);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
			return false;
		}
		blockUnder = worldObj.getBlock(i, j - 1, k + 1);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
			return false;
		}
		blockUnder = worldObj.getBlock(i, j - 1, k - 1);
		if(blockUnder != Blocks.dirt && blockUnder != Blocks.grass) {
			return false;
		}
		
		int height = rand.nextInt(15) + 15;
		
		for(int y = j; y < j + height + 6; y++)
		{
			for(int x = i - 1; x <= i + 1; x++)
			{
				for(int z = k - 1; z <= k + 1; z++)
				{
					Block block = worldObj.getBlock(x, y, z);
					if(block != Blocks.air && block != Blocks.tallgrass && block != LEAF_BLOCK)
					{
						return false;
					}
				}
			}
		}

		worldObj.setBlock(i, j, k, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i - 1, j, k, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i + 1, j, k, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i, j, k - 1, Blocks.dirt, 0, blockGenNotifyFlag);
		worldObj.setBlock(i, j, k + 1, Blocks.dirt, 0, blockGenNotifyFlag);
		for(int y = j; y < j + height; y++) {
			worldObj.setBlock(i, y, k, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i - 1, y, k, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i + 1, y, k, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i, y, k - 1, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			worldObj.setBlock(i, y, k + 1, WOOD_BLOCK, WOOD_META, blockGenNotifyFlag);
			if(y - j > height / 2 && rand.nextInt(SMALL_LEAF_CHANCE) == 0) {
				int nx = rand.nextInt(3) - 1 + i;
				int nz = rand.nextInt(3) - 1 + k;
				
				genCircle(nx, y + 1, nz, 1, 0, LEAF_BLOCK, LEAF_META, false);
				genCircle(nx, y, nz, 2, 1, LEAF_BLOCK, LEAF_META, false);
				for(int x = nx - 3; x <= nx + 3; x++) {
					for(int z = nz - 3; z <= nz + 3; z++) {
						for(int y1 = y - 1; y1 <= y; y1++) {
							if(rand.nextInt(VINE_CHANCE) == 0) {
								genVines(x, y1, z);
							}
						}
					}
				}
			}
			if(y - j > height - (height / 4) && y - j < height - 3 && rand.nextInt(SECOND_CANOPY_CHANCE) == 0) {
				int nx = i + rand.nextInt(9) - 4;
				int nz = k + rand.nextInt(9) - 4;
				
				int leafSize = rand.nextInt(3) + 5;
				
				genCircle(nx, y + 3, nz, leafSize - 2, 0, LEAF_BLOCK, LEAF_META, false);
				genCircle(nx, y + 2, nz, leafSize - 1, leafSize - 3, LEAF_BLOCK, LEAF_META, false);
				genCircle(nx, y + 1, nz, leafSize, leafSize - 1, LEAF_BLOCK, LEAF_META, false);
				
				placeBlockLine(new int[] { i, y - 2, k }, new int[] { nx, y + 2, nz }, WOOD_BLOCK, WOOD_META);
				for(int x = nx - leafSize; x <= nx + leafSize; x++) {
					for(int z = nz - leafSize; z <= nz + leafSize; z++) {
						for(int y1 = y; y1 <= y + 2; y1++) {
							if(rand.nextInt(VINE_CHANCE) == 0) {
								genVines(x, y1, z);
							}
						}
					}
				}
			}
		}
		
		int leafSize = rand.nextInt(5) + 9;
		
		genCircle(i, j + height, k, leafSize - 2, 0, LEAF_BLOCK, LEAF_META, false);
		genCircle(i, j + height - 1, k, leafSize - 1, leafSize - 4, LEAF_BLOCK, LEAF_META, false);
		genCircle(i, j + height - 2, k, leafSize, leafSize - 1, LEAF_BLOCK, LEAF_META, false);
		
		for(int x = i - leafSize; x <= i + leafSize; x++) {
			for(int z = k - leafSize; z <= k + leafSize; z++) {
				for(int y1 = j + height + 3; y1 <= j + height + 6; y1++) {
					if(rand.nextInt(VINE_CHANCE) == 0) {
						genVines(x, y1, z);
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean genVines(int i, int j, int k) {
		int m = 2;

        do {
            if (m > 5) {
            	return false;
            }

            if (Blocks.vine.canPlaceBlockOnSide(worldObj, i, j, k, m) && worldObj.getBlock(i, j, k) == Blocks.air) {
                worldObj.setBlock(i, j, k, Blocks.vine, 1 << Direction.facingToDirection[Facing.oppositeSide[m]], blockGenNotifyFlag);
                break;
            }

            m++;
        }
        while (true);
        
        int length = rand.nextInt(4) + 4;
        
        for(int y = j - 1; y > j - length; y--) {
        	if(worldObj.getBlock(i, y, k) == Blocks.air) {
        		worldObj.setBlock(i, y, k, Blocks.vine, 1 << Direction.facingToDirection[Facing.oppositeSide[m]], blockGenNotifyFlag);        	
        	} else {
        		return true;
        	}
        }
        
		return true;
	}
	
}