package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener {
	Camera camera;
	Light light;
	Terrain terrain;
	private double ang = Math.PI/18;
	private double dis = 0.3;
	
	KeyBoard (Camera camera, Light light, Terrain terrain) {
		this.camera = camera;
		this.light = light;
		this.terrain = terrain;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
        	this.camera.turnLeft(ang);
            break;

        case KeyEvent.VK_RIGHT:
        	this.camera.turnRight(ang);
            break;

        case KeyEvent.VK_UP:
        	this.camera.moveForward(dis);
            break;

        case KeyEvent.VK_DOWN:
        	this.camera.moveBackward(dis);
            break;
            
        case KeyEvent.VK_1:
        	this.camera.switch_to_first_person();
        	break;
		
        case KeyEvent.VK_3:
        	this.camera.switch_to_third_person();
        	break;
        	
        case KeyEvent.VK_T: // torch on/off
        	this.camera.torchControl();
        	break;
        case KeyEvent.VK_N: // night mode on/off
        	this.light.nightModeControl();
        	break;
        case KeyEvent.VK_2: // sun move mode on/off
        	this.light.startSunMove();
        	break;
        case KeyEvent.VK_O: // other
        	this.terrain.otherOpen = !this.terrain.otherOpen;
        	break;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
