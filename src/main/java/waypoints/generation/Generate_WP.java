package waypoints.generation;


import waypoints.Waypoints;
import waypoints.block.BlockWaypoint;
import waypoints.util.BSHelper;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class Generate_WP extends WorldGenerator
{

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {		
		int x= pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();
    	BlockPos pos_final = pos;

        while (world.isAirBlock(new BlockPos(x,y,z)) && y > 2)
        {
            --y;
        }
        //if(!BSHelper.getBlockfromState(world,pos_final.add(0, -1, 0)).isBlockNormalCube()){return false;}
        pos_final=  new BlockPos(x,y,z);
        
        if(world.getBlockState(pos_final).getBlock().getMaterial() == Material.leaves){return false;}
        
        if(!BSHelper.getBlockfromState(world,pos_final.add(0, 0, 0)).isBlockNormalCube()){return false;}
        
        BlockWaypoint waypoint;
    	Block block = Waypoints.blockWaypoint;
    	IBlockState state = block.getDefaultState();
        //world.setBlockState(pos_final.add(0, 2, 0), Blocks.gold_block.getDefaultState());
        
        
        //world.setBlockState(pos_final, state);
    	System.out.println("Generating Waypoint @ "+ pos_final);
        //Waypoint_Spawner.Create_Waypoint(world, pos_final, 3);
    	
    	if(rand.nextInt(2)==0){
    	generate_shrine(world, rand, pos_final);}
    	else 
    	generate_0(world, rand, pos_final);
    	
    	
		return false;
	}
	
	public static boolean generate_shrine(World world, Random rand, BlockPos pos){
		
		pos = pos.add(0, 1, 0);
		
		Waypoint_Spawner.Create_Waypoint(world, pos.add(0, 0, 0), 2);
		IBlockState brick = Blocks.stonebrick.getDefaultState();
		IBlockState n_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		IBlockState s_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		IBlockState e_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
		IBlockState w_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
		IBlockState pillar = Blocks.stone.getDefaultState()
				.withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH);
		
		IBlockState torch = Blocks.torch.getDefaultState();
		//IBlockState cap = Blocks.stonebrick.getDefaultState()
		//		.withProperty(BlockStoneSlab.VARIANT, BlockStoneBrick.EnumType.CHISELD);
		
		//okay using the enums is too much work
		IBlockState cap = BSHelper.returnState(12386);
		IBlockState rock = BSHelper.returnState(4);
		//IBlockState rock = Blocks.cobblestone.getDefaultState();
		
		IBlockState slab = Blocks.stone_slab.getDefaultState()
				.withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK);
		
		
		/*
		BlockPos pos2 = pos.add(-2, 0, -2);
		
				for (int x1 = 0; x1 < 6; x1++) {
			for (int z1 = 0; z1 < 6; z1++) {
				if (rand.nextInt(2)==0){
				world.setBlockState(pos2.add(x1, 0, z1), slab);}
				}
			}	
				
			*/	
				world.setBlockState(pos.add(0, 0, 0), rock);
				world.setBlockState(pos.add(0, 0, 1), rock);
				world.setBlockState(pos.add(1, 0, 0), rock);
				world.setBlockState(pos.add(1, 0, 1), rock);
		
		
				BlockPos stair = pos.add(-1, 0, 0);
				world.setBlockState(stair.add(0, 0, 0), e_stair);
				world.setBlockState(stair.add(0, 0, 1), e_stair);
				
				world.setBlockState(stair.add(3, 0, 0), w_stair);
				world.setBlockState(stair.add(3, 0, 1), w_stair);
				
				world.setBlockState(stair.add(2, 0, 2), n_stair);
				world.setBlockState(stair.add(1, 0, 2), n_stair);
				
				world.setBlockState(stair.add(2, 0, -1), s_stair);
				world.setBlockState(stair.add(1, 0, -1), s_stair);
				
				
				
		//BlockPos pos = pos.add(-1, 0, -1);
		//BlockPos pos3 = pos.add(0, 1,0);
				for(int y1 = 0; y1 < 2; y1++){
					world.setBlockState(pos.add(2, y1, 2), pillar);
					world.setBlockState(pos.add(-1, y1, -1), pillar);
					world.setBlockState(pos.add(-1, y1, 2), pillar);
					world.setBlockState(pos.add(2, y1, -1), pillar);
				}
				
				//world.setBlockToAir(pos.add(-1, 1, 2));	
				//world.setBlockState(pos.add(-1, 0, 2), cap);	
				capper_set(world, pos.add(-1, 0, 2), rand, cap, torch);
				//world.setBlockState(pos.add(2, 2, -1), cap);	
				capper_set(world, pos.add(2, 2, -1), rand, cap, torch);
				//world.setBlockState(pos.add(2, 1, 2), cap);
				capper_set(world, pos.add(2, 1, 2), rand, cap, torch);
				//world.setBlockState(pos.add(-1, 0, 2), cap);
				capper_set(world, pos.add(-1, 1, -1), rand, cap, torch);
				
		//world.setBlockState(pos.add(0, 0, 0), brick);
		//world.setBlockState(pos.add(1, 0, 0), brick);
		//world.setBlockState(pos.add(0, 0, 1), brick);
		//world.setBlockState(pos.add(1, 0, 1), brick);
		
		
		return false;
	}
	
public static boolean generate_0(World world, Random rand, BlockPos pos){
		
		pos = pos.add(0, 1, 0);
		
		Waypoint_Spawner.Create_Waypoint(world, pos.add(0, 0, 0), 2);
		IBlockState brick = Blocks.stonebrick.getDefaultState();
		IBlockState n_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		IBlockState s_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		IBlockState e_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST);
		IBlockState w_stair = Blocks.stone_brick_stairs
				.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST);
		IBlockState pillar = Blocks.stone.getDefaultState()
				.withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH);
		
		IBlockState torch = Blocks.torch.getDefaultState();
		//okay using the enums is too much work
		IBlockState cap = BSHelper.returnState(12386);
		IBlockState rock = BSHelper.returnState(4);
		
		IBlockState slab = Blocks.stone_slab.getDefaultState()
				.withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK);
			
				world.setBlockState(pos.add(0, 0, 0), rock);
				world.setBlockState(pos.add(0, 0, 1), rock);
				world.setBlockState(pos.add(1, 0, 0), rock);
				world.setBlockState(pos.add(1, 0, 1), rock);
		
				BlockPos stair = pos.add(-1, 0, 0);
				world.setBlockState(stair.add(0, 0, 0), e_stair);
				world.setBlockState(stair.add(0, 0, 1), e_stair);
				
				world.setBlockState(stair.add(3, 0, 0), w_stair);
				world.setBlockState(stair.add(3, 0, 1), w_stair);
				
				world.setBlockState(stair.add(2, 0, 2), n_stair);
				world.setBlockState(stair.add(1, 0, 2), n_stair);
				
				world.setBlockState(stair.add(2, 0, -1), s_stair);
				world.setBlockState(stair.add(1, 0, -1), s_stair);
				
				
				for(int y1 = 0; y1 < 2; y1++){
					world.setBlockState(pos.add(2, y1, 2), pillar);
					world.setBlockState(pos.add(-1, y1, -1), pillar);
					world.setBlockState(pos.add(-1, y1, 2), pillar);
					world.setBlockState(pos.add(2, y1, -1), pillar);
				}
				
				capper_set(world, pos.add(-1, 0, 2), rand, cap, torch);
				capper_set(world, pos.add(2, 0, -1), rand, cap, torch);
				capper_set(world, pos.add(2, 0, 2), rand, cap, torch);
				capper_set(world, pos.add(-1, 0, -1), rand, cap, torch);
			
		
		
		return false;
	}
		
	/**
	public static void capper_randomizer(World world, BlockPos pos, Random rand, IBlockState cap, IBlockState cap2){
		
		pos = pos.add(0, 1, 0);
		BlockPos pos1 = pos.add(-1, 0, 2);
		BlockPos pos2 = pos.add(2, 0, -1);
		BlockPos pos3 = pos.add(2, 0, 2);
		BlockPos pos4 = pos.add(-1, 0, -1);
		
		BlockPos tallb = pos.add(0, 1, 0);
		BlockPos shortb = pos.add(0, -1, 0);
		
		//tall
		capper_set(world, pos1.add(0, 1, 0), rand, cap, cap2);
		//small
		capper_set(world, pos1.add(0, -1, 0), rand, cap, cap2);
		//others
		capper_set(world, pos1, rand, cap, cap2);
		capper_set(world, pos1, rand, cap, cap2);
		
		
		
		boolean taken_1 = false;
		boolean taken_2 = false;
		boolean taken_3 = false;
		boolean taken_4 = false;
		
		//tall
		//random 4
		// if 3 && 3!taken
		// put tall 3
		for(int next = 0; next < 4; next++){
			
			
		}
		
		int start = rand.nextInt(4)+1;
		
		//tall
		if (start == 1){
			taken_1 = true;
			//set tall
		}
		switch(start){
		case 1: //set tall
			taken_1 = true;
		case 2: //set tall
			taken_2 = true;
		case 3: //set tall
			taken_3 = true;
		case 4: //set tall
			taken_4 = true;
		}
		
		
		
		
	}
       **/ 
	
	public static void capper_set(World world, BlockPos pos, Random rand, IBlockState cap, IBlockState cap2){
		
		world.setBlockState(pos.add(0, 0, 0), cap);
		if(rand.nextInt(4)==0){
			world.setBlockState(pos.add(0, 1, 0), cap2);
		}
		else world.setBlockToAir(pos.add(0, 1, 0));	
		
	}
}