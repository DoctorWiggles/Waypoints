package waypoints.generation;

import waypoints.Text;
import waypoints.Waypoint;
import waypoints.Waypoints;
import waypoints.block.BlockWaypoint;
import waypoints.util.BSHelper;
import waypoints.util.RNG_Names;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class Waypoint_Spawner {

	
	public static void Create_Waypoint(World world, BlockPos pos, int size){
		
		
		pos = pos.add(0, +1, 0);
		int x= pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();  
    	BlockWaypoint waypoint;
    	Block block = Waypoints.blockWaypoint;
    	IBlockState state = block.getDefaultState();
	
    	//int size = 3;
    	
    	if(size == 2  ||size ==  3){
		world.setBlockState(new BlockPos(x,y,z), state);
		world.setBlockState(new BlockPos(x,y,z+1), state);
		world.setBlockState(new BlockPos(x+1,y,z), state);
		world.setBlockState(new BlockPos(x+1,y,z+1), state);
    	}
		
		//
    	if(size ==  3){
		world.setBlockState(new BlockPos(x+1,y,z+2), state);
		world.setBlockState(new BlockPos(x,y,z+2), state);
		
		world.setBlockState(new BlockPos(x+2,y,z), state);
		world.setBlockState(new BlockPos(x+2,y,z+1), state);
		world.setBlockState(new BlockPos(x+2,y,z+2), state);
    	}
		
		
		
		try{
		IBlockState block2 = world.getBlockState(pos);
		Block block3 = BSHelper.getBlockfromState(world, pos);
		
		((BlockWaypoint) block3).activateStructure(world, x, y, z);
		}
		catch(Exception e){
			Text.out(e);
		}
		
		final Waypoint src;
		src = Waypoint.getWaypoint(x, y, z, world.provider.getDimensionId());
        if (src == null) {
        	//Text.out("not found: ", EnumChatFormatting.RED);
        	//Text.out(src);
        	///////        	
        	}
        //Text.out("found: ", EnumChatFormatting.GREEN);
        //Text.out(src);
        
        
        //MsgName msg = new MsgName(src, "Balls");
       // Waypoints.instance.messagePipeline.sendToServer(msg);
        String stringy = "Mystery Waypoint";
        //stringy = "MCG SOLID V: THE PHANTOM SHITPOST";
        
        //stringy = RNG_Names.roll_defined();
        
        //stringy = RNG_Names.get_listing();
        try{
        stringy = RNG_Names.roll_name(RNG_Names.Type.RANDOM);
        }
        catch(Exception e){}
        src.name = stringy;
        src.changed = true;
                
	}
}
