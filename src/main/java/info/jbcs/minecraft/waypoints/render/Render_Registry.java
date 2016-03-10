package info.jbcs.minecraft.waypoints.render;


import info.jbcs.minecraft.waypoints.Waypoints;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

/**http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-8/first-item/**/
public final class Render_Registry {

	public static void registerItemRenderer() {
		
		
		
	}
	
	
	public static void registerBlockRenderer(){
		System.out.println("Registering Block Renders");
		
		
		ModelBakery.addVariantName(Item.getItemFromBlock(Waypoints.blockWaypoint), 
				  modid + ":" + "waypoint_base",
				  
				  modid + ":" + "waypoint_s1",
				modid + ":" + "waypoint_s2",
				modid + ":" + "waypoint_s3",
				modid + ":" + "waypoint_s4",
				modid + ":" + "waypoint_s5",
				modid + ":" + "waypoint_s6",
				modid + ":" + "waypoint_s7",
				modid + ":" + "waypoint_s8",
				modid + ":" + "waypoint_s9"
				  );
		
		
		
		//reg(Waypoints.blockWaypoint);
		reg(Waypoints.blockWaypoint, 0, "waypoint_base");
		for (int i = 1; i < 10; ++i)
        {
		reg(Waypoints.blockWaypoint, i, "waypoint_"+i);
			}
	}

	//==========================================================================//

	public static String modid = Waypoints.MODID;

	public static void reg(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register
		(item, 0, new ModelResourceLocation(modid + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		//Main.logger.info(modid + ":" + item.getUnlocalizedName().substring(5), "inventory");
	}
	
	public static void Meta_reg(Item item, int meta, String file) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register
	    (item, meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
	    //Main.logger.info(modid + ":" + file, "inventory");
	   
	}
	

	public static void reg(Block block) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
		
	}
	
	public static void reg(Block block, int meta, String file) {
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
	    .register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(modid + ":" + file, "inventory"));
	}

	
}