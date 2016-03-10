package info.jbcs.minecraft.waypoints.item;

import info.jbcs.minecraft.waypoints.util.BSHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Debug_States extends Item{
	
	public Debug_States( String unlocalizedName)
    {
    	super ();
        this.maxStackSize = 1;  
        setUnlocalizedName(unlocalizedName);
        //setCreativeTab(HelpTab.HelperTools);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
	
	public boolean onItemUse(ItemStack thestaff, EntityPlayer player, World world, BlockPos pos, EnumFacing theface, float fty1, float fty2, float fty3){
		
		if(player.isSneaking()){
			 BSHelper.setStatefromMeta(world, pos, 1, 3);
		 }
		 if (!world.isRemote){ return false;}		 
		 
		 if(!player.isSneaking()){
		String State = "State: " + world.getBlockState(pos);		
		String Meta = "Meta: " + BSHelper.getMetafromState(world,pos);
		String ID = "ID: " + BSHelper.returnID(world,pos);
		String Local = "Local : " + world.getBlockState(pos).getBlock().getLocalizedName();
		String Unlocal = "Unlocal : " + world.getBlockState(pos).getBlock().getUnlocalizedName();
		String posi = "" + pos;
		String blockid = "#: " + BSHelper.returnBlock_ID(BSHelper.returnID(world, pos));
		
		ChatComponentTranslation chatmessy2 = new ChatComponentTranslation(
				EnumChatFormatting.WHITE+ posi 
				+EnumChatFormatting.YELLOW+ Local, new Object[0]);
		player.addChatComponentMessage(chatmessy2);
		ChatComponentTranslation chatmessy1 = new ChatComponentTranslation(EnumChatFormatting.GRAY + State, new Object[0]);
		player.addChatComponentMessage(chatmessy1);		
		
		ChatComponentTranslation chatmessy = new ChatComponentTranslation(
				//EnumChatFormatting.GRAY +
				//Messy + " "+
				blockid+ " "+
				EnumChatFormatting.GREEN +Meta + " "+
				EnumChatFormatting.BLUE +ID + " "+
				EnumChatFormatting.RED + Unlocal
				
				, new Object[0]);
		(player).addChatComponentMessage(chatmessy);
		
		/**
		ChatComponentTranslation chatmessy2 = new ChatComponentTranslation(
				EnumChatFormatting.GRAY + "" + world, new Object[0]);
		player.addChatComponentMessage(chatmessy2);
		**/
		 }
		 
		
		
		
		return false;
	   
	    	
	    }
	
	 public void drop_blockItem(World world, BlockPos pos1){
		   
		   (world.getBlockState(pos1).getBlock()).dropBlockAsItem(world, pos1, world.getBlockState(pos1), 0);
	   }

}
