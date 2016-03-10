package info.jbcs.minecraft.waypoints.network;

import info.jbcs.minecraft.waypoints.Waypoint;
import info.jbcs.minecraft.waypoints.WaypointPlayerInfo;
import info.jbcs.minecraft.waypoints.Waypoints;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;
import java.util.ArrayList;

public class Packets {


    public static void sendWaypointsToPlayer(EntityPlayerMP player, final int srcWaypointId) throws IOException {
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();

        //final WaypointPlayerInfo info = WaypointPlayerInfo.get(player.getDisplayName());
        final WaypointPlayerInfo info = WaypointPlayerInfo.get(player.getDisplayName().toString());
        if (info == null) return;

        info.addWaypoint(srcWaypointId);

        for (Waypoint w : Waypoint.existingWaypoints)
            if (info.discoveredWaypoints.containsKey(w.id))
                waypoints.add(w);
        MsgWaypointsList msg = new MsgWaypointsList(srcWaypointId, waypoints);
        Waypoints.instance.messagePipeline.sendTo(msg, player);
    }

}
