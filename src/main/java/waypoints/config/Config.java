package waypoints.config;

import waypoints.Text;
import waypoints.Waypoints;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Config {

	public static Configuration configFile;
	
	
	public static void preinit(FMLPreInitializationEvent event) {
		
		  configFile = new Configuration(event.getSuggestedConfigurationFile());
		    syncConfig();		
	}
	
	
    public static boolean compactView = true;
    public static String default_recipe = "3x2,minecraft:stone:1,minecraft:stone:1,minecraft:stone:1,minecraft:stone:1,minecraft:ender_pearl:1,minecraft:stone:1";
    public static String recipe;
    public static boolean craftable = true;
    public static boolean allowActivation = true;
    public static boolean classic_style = false;
	
	
	public static void syncConfig() {
			   				
		compactView = configFile.getBoolean("compact view", Configuration.CATEGORY_GENERAL,compactView, "Only show one line in Waypoint GUI, in order to fit more waypoints on the screen");
        recipe = configFile.getString("recipe",Configuration.CATEGORY_GENERAL, default_recipe, "You can change crafting recipe here");
        craftable = configFile.getBoolean("craftable", Configuration.CATEGORY_GENERAL, craftable,"Set to false to completely disable crafting recipe");
        
        allowActivation = configFile.getBoolean("can non-ops activate", Configuration.CATEGORY_GENERAL, allowActivation, "If set to false only ops can enable Waypoins");
        classic_style = configFile.getBoolean("classic style", Configuration.CATEGORY_GENERAL,classic_style, "Set to true to rollback to classic art");
        
        
	    if(configFile.hasChanged())
	      configFile.save();
	  }
	
	
	
	
	//TODO recipe
    
    public static void addRecipe(ItemStack itemStack, String string) {
        String[] string_array = string.split(",");
        String[] itemstr;
        ItemStack[] itemstack_array = new ItemStack[9];
        int recipe_column = Integer.parseInt(string_array[0].split("x")[0]);
        int recipe_row = Integer.parseInt(string_array[0].split("x")[1]);
        String a = "", b = "", c = "";
        for (int i = 0; i < recipe_row; i++) {
            for (int j = 0; j < recipe_column; j++) {
                itemstr = string_array[i * recipe_column + j + 1].split(":");
                if (!(itemstr.length < 3)) {
                    /*TODO Double check working ->*/// itemstack_array[i * recipe_column + j] = GameRegistry.findItemStack(itemstr[0], itemstr[1], Integer.parseInt(itemstr[2]));
                    itemstack_array[i * recipe_column + j] = new ItemStack(GameRegistry.findItem(itemstr[0], itemstr[1]), Integer.parseInt(itemstr[2]));
                    if (i == 0) a += Character.toString((char) (i * recipe_column + j + 65));
                    if (i == 1) b += Character.toString((char) (i * recipe_column + j + 65));
                    if (i == 2) c += Character.toString((char) (i * recipe_column + j + 65));
                } else {
                    itemstack_array[i * recipe_column + j] = null;
                    if (i == 0) a += " ";
                    if (i == 1) b += " ";
                    if (i == 2) c += " ";
                }
            }
        }
        if (recipe_row == 1)
            CraftingManager.getInstance().addRecipe(itemStack,
                    a,
                    (char) 65, itemstack_array[0],
                    (char) 66, itemstack_array[1],
                    (char) 67, itemstack_array[2]
            );
        if (recipe_row == 2)
            CraftingManager.getInstance().addRecipe(itemStack,
                    a, b,
                    (char) 65, itemstack_array[0],
                    (char) 66, itemstack_array[1],
                    (char) 67, itemstack_array[2],
                    (char) 68, itemstack_array[3],
                    (char) 69, itemstack_array[4],
                    (char) 70, itemstack_array[5]
            );
        if (recipe_row == 3)
            CraftingManager.getInstance().addRecipe(itemStack,
                    a, b, c,
                    (char) 65, itemstack_array[0],
                    (char) 66, itemstack_array[1],
                    (char) 67, itemstack_array[2],
                    (char) 68, itemstack_array[3],
                    (char) 69, itemstack_array[4],
                    (char) 70, itemstack_array[5],
                    (char) 71, itemstack_array[6],
                    (char) 72, itemstack_array[7],
                    (char) 73, itemstack_array[8]
            );
    }
	

}
