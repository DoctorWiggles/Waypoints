package waypoints.gui;

import waypoints.Waypoint;
import waypoints.Waypoints;
import waypoints.config.Config;
import waypoints.network.MsgDelete;
import waypoints.network.MsgTeleport;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static java.lang.Math.min;

public class GuiWaypoints extends GuiScreenPlus {
    ArrayList<Waypoint> waypoints;
    int currentWaypointId;

    ArrayList<GuiWaypointButton> waypointButtons = new ArrayList<GuiWaypointButton>();
    GuiWaypointButton selectedButton;
    GuilScrolledBox scroller;

    GuiExButton gotoButton;
    GuiExButton deleteButton;

    
    //TODO pinpoint invisible cursor bug
    public GuiWaypoints(int cid, ArrayList<Waypoint> l) {
        super(227, 227, "waypoints:textures/gui-waypoints.png");

        waypoints = l;
        currentWaypointId = cid;

        addChild(scroller = new GuilScrolledBox(0, 22, 227, 199 - 12 - 22));

        //TODO int buttonHeight = Waypoints.compactView ? 14 : 36;
        int buttonHeight = Config.compactView ? 14 : 36;

        int i = 0;
        for (final Waypoint w : waypoints) {
            GuiWaypointButton button;

            scroller.addChild(button = new GuiWaypointButton(0, 4 + (buttonHeight + 4) * waypointButtons.size(), 220, buttonHeight, w) {
                @Override
                public void onClick() {
                    if (selectedButton == this) {
                        if (selectedButton == null) return;
                        final Waypoint wp = selectedButton.waypoint;
                        if (wp == null) return;
                        MsgTeleport msg = new MsgTeleport(Waypoint.getWaypoint(currentWaypointId), Waypoint.getWaypoint(wp.id));
                        Waypoints.instance.messagePipeline.sendToServer(msg);
                        closeWaypoints();
                        return;
                    }

                    for (GuiWaypointButton button : waypointButtons)
                        button.selected = button == this;

                    selectedButton = this;
                }
            });

            if (w.id == currentWaypointId) {
                button.selected = true;
                selectedButton = button;

                scroller.offset = min((50 - buttonHeight * waypointButtons.size()), ((buttonHeight - 14) * i) / 2);
            }

            waypointButtons.add(button);
            i++;
        }


        addChild(gotoButton = new GuiExButton(12, 199, 64, 20, "Go to") {
            @Override
            public void onClick() {
                if (selectedButton == null) return;
                final Waypoint wp = selectedButton.waypoint;
                if (wp == null) return;
                MsgTeleport msg = new MsgTeleport(Waypoint.getWaypoint(currentWaypointId), Waypoint.getWaypoint(wp.id));
                Waypoints.instance.messagePipeline.sendToServer(msg);
                closeWaypoints();
            }
        });

        addChild(deleteButton = new GuiExButton(82, 199, 64, 20, "Delete") {
            @Override
            public void onClick() {
                final Waypoint wp = selectedButton.waypoint;
                if (wp == null) return;
                MsgDelete msg = new MsgDelete(Waypoint.getWaypoint(wp.id));
                Waypoints.instance.messagePipeline.sendToServer(msg);
                selectedButton.waypoint = null;
            }
        });

        addChild(new GuiExButton(152, 199, 64, 20, "Cancel") {
            @Override
            public void onClick() {
                closeWaypoints();
            }
        });
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GL11.glPushMatrix();
        GL11.glTranslatef(screenX, screenY, 0);
        root.render();
        GL11.glPopMatrix();

        drawCenteredStringWithShadow("Waypoints", screenX + 114, screenY + 12, 0xffffffff);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int fx, int fy) {
    }

    public void closeWaypoints() {
        Minecraft.getMinecraft().thePlayer.closeScreen();
        inventorySlots.onContainerClosed(Minecraft.getMinecraft().thePlayer);
        close();
    }

}
