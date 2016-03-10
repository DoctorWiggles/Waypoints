package info.jbcs.minecraft.waypoints.proxy;

import info.jbcs.minecraft.waypoints.render.Render_Registry;

public class ProxyClient extends Proxy {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    	
    	Render_Registry.registerBlockRenderer();
    }
}
