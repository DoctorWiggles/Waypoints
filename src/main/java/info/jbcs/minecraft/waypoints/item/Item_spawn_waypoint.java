package info.jbcs.minecraft.waypoints.item;

import java.util.Random;

import info.jbcs.minecraft.waypoints.Text;
import info.jbcs.minecraft.waypoints.Waypoint;
import info.jbcs.minecraft.waypoints.Waypoints;
import info.jbcs.minecraft.waypoints.block.BlockWaypoint;
import info.jbcs.minecraft.waypoints.generation.Generate_WP;
import info.jbcs.minecraft.waypoints.network.MsgName;
import info.jbcs.minecraft.waypoints.util.BSHelper;
import info.jbcs.minecraft.waypoints.util.RNG_Names;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Item_spawn_waypoint extends Item{

	public Item_spawn_waypoint( String unlocalizedName)
    {
    	super ();
        this.maxStackSize = 1;  
        setUnlocalizedName(unlocalizedName);
        //setCreativeTab(HelpTab.HelperTools);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
	
	Random rand = new Random();
	public boolean onItemUse(ItemStack thestaff, EntityPlayer player, World world, BlockPos pos, EnumFacing theface, float fty1, float fty2, float fty3){
		if(world.isRemote)return false;
		
		if(rand.nextInt(2)==0){	    	
		Generate_WP.generate_shrine(world, rand, pos);
		}
		else 
		Generate_WP.generate_0(world, rand, pos);
		
		int a = 0;
		if(a == 0)return true;
		
		pos = pos.add(0, +1, 0);
		int x= pos.getX();
    	int y = pos.getY();
    	int z = pos.getZ();  
    	BlockWaypoint waypoint;
    	Block block = Waypoints.blockWaypoint;
    	IBlockState state = block.getDefaultState();
	
    	int size = 3;
    	
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
		
		final Waypoint src = Waypoint.getWaypoint(x, y, z, player.dimension);
        if (src == null) {
        	Text.out("not found: ", EnumChatFormatting.RED);
        	Text.out(src);return true;}
        Text.out("found: ", EnumChatFormatting.GREEN);
        Text.out(src);
        
        
        //MsgName msg = new MsgName(src, "Balls");
       // Waypoints.instance.messagePipeline.sendToServer(msg);
        String stringy = "Pumper Nicel Ice";
        stringy = "MCG SOLID V: THE PHANTOM SHITPOST";
        
        //stringy = RNG_Names.roll_defined();
        
        //stringy = RNG_Names.get_listing();
        stringy = RNG_Names.roll_name(RNG_Names.Type.RANDOM);
        src.name = stringy;
        src.changed = true;
        
        
		return true;
		
	}
	
}
