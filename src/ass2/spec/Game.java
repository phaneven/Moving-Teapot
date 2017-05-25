package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 */
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;
    private Camera camera;
    private KeyBoard kb;
    private Light light;
    
    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        camera = new Camera(myTerrain, 0, myTerrain.getGridAltitude(0,0), 0);
        light = new Light(myTerrain);
        kb = new KeyBoard(camera, light, terrain);
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
          panel.addKeyListener(kb);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0,1,1,1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	
		gl.glEnable(GL2.GL_LIGHTING);
		
		light.setLight(gl);
		
		
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		gl.glEnable(GL2.GL_POLYGON_OFFSET_POINT);
		gl.glEnable(GL2.GL_POLYGON_OFFSET_LINE);
		gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
		
		camera.setCamera(gl);
		myTerrain.meshGenerator(gl);
		myTerrain.draw(gl);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		camera.reshape(gl, x, y, width, height);
	}
}
