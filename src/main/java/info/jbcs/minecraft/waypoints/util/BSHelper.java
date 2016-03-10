package info.jbcs.minecraft.waypoints.util;

import info.jbcs.minecraft.waypoints.Text;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BSHelper {
	/**
	Butchered up from Block.class methods
	Work around since nbt can't store states
	Hopefully works
	 * **/
	
	//int meta= (world.getBlockState(pos2).getBlock()).getStateId(world.getBlockState(pos2));
	
	/**Extract an ID integer from a blockstate**/
	public static int returnID(World world, BlockPos pos ){
		int GET = 0;		
		GET =(world.getBlockState(pos).getBlock()).getStateId(world.getBlockState(pos));		
		return GET;
	}
	
	/**Extract a blockstate from an ID integer**/
	public static IBlockState returnState(int id)
    {
        int j = id & 4095;
        int k = id >> 12 & 15;
        return Block.getBlockById(j).getStateFromMeta(k);
    }
	
	/**Extracts a block from an ID integer**/
	public static Block returnBlock(int id){
		
		int j = id & 4095;
        int k = id >> 12 & 15;
        return Block.getBlockById(j);
	}
	
	/**Extracts a Meta value from an ID integer**/
	public static int returnMeta(int id)
    {
        int j = id & 4095;
        int k = id >> 12 & 15;
        return k;
    }
	
	/**Extracts a Block from a State via bridge**/
	public static Block getBlockfromState (World world, BlockPos pos){
		
		Block blocky = Blocks.air;
		
		int ID = returnID(world, pos);
		blocky = returnBlock(ID);
		//Text.out(blocky+""+pos);
		return blocky;
	}
	
	/**Extracts a Meta Value from a State via bridge**/
	public static int getMetafromState (World world, BlockPos pos){
		int meta = 0;
		
		int ID = returnID(world, pos);
		meta = returnMeta(ID);
				
		return meta;
	}
	
	/**Extracts a block ID from an ID integer**/
		public static int returnBlock_ID(int id){
			
			int j = id & 4095;
	        int k = id >> 12 & 15;
	        return j;
		}
		
		public static int getblock_ID(World world, BlockPos pos){
			int id;
			id = returnBlock_ID(returnID(world, pos));
			return id;
		}
		
		/*
		public static void setStatefromMeta(World world, BlockPos pos, int meta, int flags)
	    {
			Block block = getBlockfromState(world, pos);
			int ID = returnID(world, pos);
			
			//IBlockState state = returnState(ID+meta);
			IBlockState state = returnState(meta);
			world.setBlockState(pos, state, flags);
	        return; 
	    }
		*/
		/*
		public static void setStatefromMeta(World world, BlockPos pos, int meta, int flags)
	    {
			Block block = getBlockfromState(world, pos);
			int ID = returnID(world, pos);
			
			//IBlockState state = returnState(ID+meta);
			IBlockState state = returnState(meta);
			
			IBlockState state2 =  Block.getBlockById(ID).getStateFromMeta(meta);
			
			//IBlockState state2 =  Block.getBlockById(ID).getStateFromMeta(meta);
			world.setBlockState(pos, state2, flags);
	        return; 
	    }
		*/
		public static void setStatefromMeta(World world, BlockPos pos, int meta, int flags)
	    {	//Text.out("Meta in: " +meta+ " @ "+pos);
			Block block = getBlockfromState(world, pos);			
			IBlockState state2 =  block.getStateFromMeta(meta);
			//System.out.println("Meta Value: "+meta);
			//IBlockState state2 =  Block.getBlockById(ID).getStateFromMeta(meta);
			world.setBlockState(pos, state2, flags);
			int meta2 = getMetafromState(world, pos);
			//Text.out("Meta out: " +meta2+ " @ "+pos);
	        return; 
	    }
		
}
