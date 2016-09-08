package marketflow;

import engine.Game;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

import java.util.Map;

/**
 * Created by Nark on 9/3/2016.
 */
public class Player extends Entity
{
    private Vector2f _carrot = new Vector2f(0,0);
    private int _gear;
    private int _gearSize;
    private int _maxGear;
    private int _minGear;
    private float _angle;
    private float _handling;
    private enum yaw
    {
        HARD_LEFT,
        LEFT,
        NONE,
        RIGHT,
        HARD_RIGHT,
    }
    private yaw _yaw;

    public Player(String id, String desc, Map<String, Stock> st_ref, int x, int y, int gear, int max, int min, float handling)
    {
        super(id, desc, st_ref, x, y, "res/ship.png");
        _gear = 0;
        _gearSize=gear;
        _maxGear =max;
        _minGear =min;
        _angle = (float)Math.PI/2;
        _handling=handling;
        _yaw = yaw.NONE;
    }

    public void update(int count)
    {
        //Determine amount to turn
        switch(_yaw)
        {
            case HARD_RIGHT:
                _angle+=_handling*2;
                break;
            case RIGHT:
                _angle+=_handling;
                break;
            case LEFT:
                _angle-=_handling;
                break;
            case HARD_LEFT:
                _angle-=_handling*2;
                break;
        }

        //Set Ship Velocity
        _carrot.x = (float)(Speed() * Math.cos(_angle));
        _carrot.y = (float)(Speed() * Math.sin(_angle));

        //Keep ship centered
        _posX = (Game.WIDTH/2)-Game.mf.Map().OffsetX();
        _posY = (Game.HEIGHT/2)-Game.mf.Map().OffsetY();
    }

    public void tick(int count)
    {

    }

    public void render(GameContainer game, Graphics g)
    {
        super.render(game, g);
        int panx = Game.mf.Map().PanX();
        int pany = Game.mf.Map().PanY();
        int x = (Game.WIDTH/2)+panx;
        int y = (Game.HEIGHT/2)+pany;

        g.setColor(Color.red);
        g.drawLine(x,y,x- _carrot.x*5,y- _carrot.y*5);
        g.setColor(Color.black);

        g.drawString("Gear: "+ _gear , x+50,y+50);
        g.drawString("Speed: "+ Speed(), x+50,y+70);
        g.drawString("X: "+ _carrot.x+" Y: "+ _carrot.y, x+50,y+90);
        g.drawString("Yaw: "+_yaw.toString(), x+50,y+110);
    }

    public void shiftUp()
    {
        if (_gear < _maxGear)
        {
            _gear++;
        }
    }

    public void shiftDown()
    {
        if (_gear > _minGear)
        {
            _gear--;
        }
    }

    public void tackLeft()
    {
        if(_yaw!=yaw.HARD_LEFT)
        {
            _yaw=_yaw.values()[_yaw.ordinal()-1];
        }
    }

    public void tackRight()
    {
        if(_yaw!=yaw.HARD_RIGHT)
        {
            _yaw=_yaw.values()[_yaw.ordinal()+1];
        }
    }

    public int Speed(){return _gear * _gearSize;}
    public int Gear(){return _gear;}
    public void GearSize(int amt){_gearSize =amt;}
    public Vector2f Velocity(){return _carrot;}
    public float Angle(){return _angle;}
    public float Handling(){return _handling;}
    public void Handling(int amt){_handling=amt;}
    public int MaxGear(){return _maxGear;}
    public int MinGear(){return _minGear;}
}
