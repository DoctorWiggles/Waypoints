package waypoints;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

/** Debugging Helper **/
public class Text {
	
	public static void out (String string){	
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		Text.out(player, string, EnumChatFormatting.WHITE);			
	}
	
	public static void out (Object obj){
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if (obj == null){
			Text.out(player, "NULL OBJECT", EnumChatFormatting.RED);	
			return;}
		
		String string = obj.toString();
		Text.out(player, string, EnumChatFormatting.WHITE);			
	}
	
	public static void out (Object obj, EnumChatFormatting color){	
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if (obj == null){
			Text.out(player, "NULL OBJECT", EnumChatFormatting.RED);	
			return;}
		String string = obj.toString();
		Text.out(player, string, color);			
	}
	
	/** Final Method **/
	public static void out (EntityPlayer player, String string, EnumChatFormatting color){		
		ChatComponentTranslation text = new ChatComponentTranslation(color + string, new Object[0]);
		player.addChatComponentMessage(text);				
	}
	
	public static void out (EntityPlayer player, String string){		
		Text.out(player, string, EnumChatFormatting.WHITE);			
	}
	
	
	
	public static void out (EntityPlayer player, Object obj, EnumChatFormatting color){
		String string = obj.toString();
		Text.out(player, string, color);		
	}
	
	public static void out (EntityPlayer player, Object obj){		
		String string = obj.toString();
		Text.out(player, string);			
	}

}
