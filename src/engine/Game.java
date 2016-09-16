package engine;
import org.newdawn.slick.*;

import java.util.logging.Level;
import java.util.logging.Logger;

//Version 1.0

public class Game extends BasicGame
{
	//Screen
	public static int WIDTH, HEIGHT;						//screen dimensions
	private static float SCALE=1f;								//zoom value
	//States
	enum State
	{//What state the game is running in. Controls what logic is done and what is rendered
		UI,
		MARKETFLOW,
		SUBBATTLE,
		MYESTATE
	}
	static State state = State.MARKETFLOW;			//Starting State

	//Timing
	private int[] _counts = new int[State.values().length];	//Fast Logic Timers
	private int[] _ticks =  new int[State.values().length];	//One Second Logic Timers
	private int _sec = 0;									//# of Fast Logic ticks until One Second Logic fires

	//Input
	private static KeyMap keymap;							//Keyboard Bindings
	private static MouseMap mousemap;						//Mouse Bindings

	//Modules
	private static ui.Init ui;								//Menu mode.
	public static marketflow.Init mf;						//World map mode.
	private static subbattle.Init sb;						//Combat mode.
	private static myestate.Init me;						//City builder mode.

	public Game(String name)
	{
		super(name);
	}				//Constructor

	static void setState(State s)
	{//Changes the game state enum and resets the bound keys for that state
		state = s;
		keymap.mapKeys(s);
		mousemap.mapMouse(s);
	}
	@Override
	public void keyPressed(int key, char c)
	{//Sends Keyboard Input to KeyMap class for processing
		super.keyPressed(key, c);
		keymap.press(key,true);
	}
	@Override
	public void keyReleased(int key, char c)
	{//Sends Keyboard Input to KeyMap class for processing
		super.keyPressed(key, c);
		keymap.press(key,false);
	}

	@Override
	public void mousePressed(int button, int x, int y)
	{//Sends Mouse Input to MouseMap class for processing
		super.mousePressed(button, x, y);
		mousemap.press(button, true, (int)(x/SCALE), (int)(y/SCALE));
	}
	@Override
	public void mouseReleased(int button, int x, int y)
	{//Sends Mouse Input to MouseMap class for processing
		super.mousePressed(button, x, y);
		mousemap.press(button, false, x, y);
	}

	@Override
	public void init(GameContainer game) throws SlickException
	{
		XMLHandler xmlh = new XMLHandler();
		keymap = new KeyMap(xmlh);
		mousemap = new MouseMap(xmlh);

		ui = new ui.Init(xmlh);
		mf = new marketflow.Init(xmlh);
		sb = new subbattle.Init(xmlh);
		me = new myestate.Init(xmlh);
	}

	@Override
	public void update(GameContainer game, int i) throws SlickException
	{//Slick2D game loop
		//One Second Logic
		int fps = game.getFPS();
		if (_sec++>=fps&&fps>0)
		{
			int t = ++_ticks[state.ordinal()];
			switch(state)
			{
				case UI:
					ui.tick(t);
					break;
				case MARKETFLOW:
					mf.tick(t);
					break;
				case SUBBATTLE:
					sb.tick(t);
					break;
				case MYESTATE:
					me.tick(t);
					break;
			}
			_sec -=fps;
		}
		//Fast logic
		int c = ++_counts[state.ordinal()];
		switch(state)
		{
			case UI:
				ui.update(c);
				break;
			case MARKETFLOW:
				mf.update(c);
				break;
			case SUBBATTLE:
				sb.update(c);
				break;
			case MYESTATE:
				me.update(c);
				break;
		}
	}

	@Override
	public void render(GameContainer game, Graphics g) throws SlickException
	{//Slick2D render loop
		game.getGraphics().scale(SCALE,SCALE);
		g.setColor(Color.gray);
		g.fillRect(0,0,WIDTH,HEIGHT);
		g.setColor(Color.black);
		switch(state)
		{
			case UI:
				ui.render(game,g);
				break;
			case MARKETFLOW:
				mf.render(game,g);
				break;
			case SUBBATTLE:
				sb.render(game,g);
				break;
			case MYESTATE:
				me.render(game,g);
				break;
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println("HEYO");
		try
		{
			AppGameContainer agc;
			agc = new AppGameContainer(new Game("Sub Game"));
			agc.setDisplayMode(1600, 900, false);
            agc.setTargetFrameRate(60);
			WIDTH=(int)(agc.getWidth()/SCALE);
			HEIGHT=(int)(agc.getHeight()/SCALE);
			agc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}