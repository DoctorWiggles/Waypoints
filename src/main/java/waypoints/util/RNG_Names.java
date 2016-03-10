package waypoints.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//  :^)
public class RNG_Names {	
	
	
	public enum Type {
	    NONE, DEFINED, RANDOM, ALL
	}
	
	public static Random rand = new Random();
	
	//Random Name Generation
	public static String roll_name(RNG_Names.Type type){
		String name = "Mystery Waypoint";
		
		if(type == RNG_Names.Type.NONE){
			return name;
			}
		
		if(type == RNG_Names.Type.RANDOM){
			return name = roll_prefix()+ roll_noun()+ roll_suffix();	
		}
		
		if(type == RNG_Names.Type.DEFINED){
			return name = roll_defined();		
		}
		
		if(type == RNG_Names.Type.ALL){
			
		}
		
		return name;
	}
	
	public static String roll_prefix(){		
		String pre = "Spooky";
		
		List<String> list = new ArrayList<String>();
		list.add("Spooky");
		list.add("Haunted");
		list.add("Bright");
		list.add("Musky");
		list.add("Shallow");
		list.add("Tempting");
		list.add("Shadey");
		list.add("Hidden");
		list.add("Demonic");
		list.add("Cheerful");
		list.add("Quite");
		list.add("Reclusive");
		list.add("New");
		list.add("Old");
		list.add("Ancient");
		list.add("Living");
		list.add("Dead");
		list.add("Hostile");
		list.add("Friendly");
		list.add("Magical");
		list.add("Mundane");
		list.add("Special");		
		list.add("Bad");
		list.add("Legendary");
		list.add("Mythic");
		list.add("Violent");
		list.add("Peaceful");
		list.add("Naughty");
		list.add("Cursed");
		list.add("Enchanted");
		//list.add("The");
		//list.add("Another");
		list.add("Mysterous");
		//list.add("Famous");
		list.add("Molten");
		list.add("Frozen");
		//list.add("Common");
		//list.add("Rare");
		
		pre = roll_list(list);
		
		return pre + " ";		
	}
	
	public static String roll_noun(){		
		String noun = "Skeleton";
		
		List<String> list = new ArrayList<String>();
		//list.add("Walrus");
		list.add("Ocelot");
		list.add("Horse");
		//list.add("Giraffe");
		list.add("Demon");
		list.add("Sheep");
		list.add("Cow");
		list.add("Creeper");
		list.add("Zombie");
		list.add("Rabbit");
		list.add("Chicken");
		list.add("Enderman");
		list.add("Sheep");
		list.add("Steve");
		list.add("Donkey");
		list.add("Wolf");
		list.add("Dragon");
		list.add("Witch");
		list.add("Notch");
		list.add("Slime");
		list.add("Mooshroom");
		list.add("Pigmen");	
		list.add("Villager");	
		list.add("Ghast");
		list.add("Bat");
		list.add("Ghast");
		list.add("Skeleton");
		list.add("Spider");
		list.add("Silverfish");
		list.add("Squid");
		list.add("Blaze");
		list.add("Guardian");
		list.add("Fish");
		list.add("Bedrock");
		list.add("Nether");
		list.add("Twilight");
		list.add("Mystery");
		
		noun = roll_list(list);
		
		
		return noun +" ";		
	}
	
	public static String roll_suffix(){		
		String suf = "Island";
		
		List<String> list = new ArrayList<String>();
		list.add("Town");
		list.add("Island");
		list.add("Plains");
		//list.add("Hallow");
		//list.add("Pines");
		list.add("Cave");
		list.add("Plains");
		list.add("Beach");
		list.add("Zone");
		list.add("Isle");
		list.add("Reach");
		list.add("Tree");
		list.add("Village");
		list.add("Ruins");
		list.add("Area");
		list.add("Bungalo");
		list.add("Expanse");
		list.add("City");
		list.add("Stronghold");
		list.add("Ravine");
		list.add("Mine");
		list.add("Ocean");
		list.add("Cliff");
		list.add("Terrace");
		list.add("Temple");
		list.add("Shrine");
		list.add("House");
		list.add("Boulder");
		list.add("Pond");
		list.add("Lake");
		list.add("Dimension");
		list.add("Arena");
		list.add("Valley");
		list.add("Mountain");
		list.add("Hill");
		list.add("Jungle");
		list.add("Desert");
		list.add("Stream");
		list.add("River");
		list.add("Waypoint");
		list.add("Paradise");
		
		suf = roll_list(list);
		
		return suf;		
	}
	
	public static String roll_defined(){		
		String name = "Tristram";
		
		
		List<String> list = new ArrayList<String>();
		list.add("Tristram");
		list.add("Hades");
		list.add("Cyril");
		list.add("Jigoku");
		list.add("Happy Town");	
		//config.add("a name here ");
		
		/*
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		*/
		name = roll_list(list);
		
		
		return name;		
	}
	
	public static String roll_list(List list){
		String name = "Mystery";
		
		/*
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}		
		*/
		int random = rand.nextInt(list.size());
		name = list.get(random).toString();
		
		return name;
	}
	

	///////////////////////////////////////////////////////////////
	
	public static String get_listing(){
		String name = "Mystery";
		List<String> list = new ArrayList<String>();
		add_extra(list);
		
		int random = rand.nextInt(list.size());
		name = list.get(random).toString();
		
		return name;
	}	
	
	public static List add_extra(List list){
		
		add_names(list);
		add_names2(list);
		return list;
	}
	
	public static List add_names(List list){
		
		list.add("Hades");
		list.add("Cyril");
		
		return list;
	}
	
public static List add_names2(List list){
		
		list.add("beens");
		list.add("tookish");
		list.add("plant");
		list.add("tan");
		
		
		return list;
	}

	
	
	
	
	
	
	

}
