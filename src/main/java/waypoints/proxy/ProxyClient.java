package waypoints.proxy;

import waypoints.render.Render_Registry;

public class ProxyClient extends Proxy {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    	
    	Render_Registry.registerBlockRenderer();
    }
}
