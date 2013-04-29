package towers;

import bullets.Bullet;
import bullets.Pebble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import enemies.Enemy;

import java.util.ArrayList;


public class SlingshotTower extends Tower
{

	private final int CALLS_BETWEEN_TOGGLE = 15;
	private int current_tex;
	private Texture[] tex;
	
	public SlingshotTower()
	{
		// attributes
		name = "SlingshotTower";
		width = 40;
		height = 48;
		range = 130;
		cost = 800;
		firing_speed = 2.0f;	// shoot every x seconds
		upgradecost = cost * 3 / 4;
	}
	
	public SlingshotTower(ArrayList<Enemy> enemies, float x, float y, AssetManager manager)
	{
		super(enemies, x, y, manager);
		
		// attributes
		name = "SlingshotTower";
		width = 40;
		height = 50;
		range = 200;
		cost = 800;
		firing_speed = 1.5f;	// shoot every x seconds
		upgradecost = cost * 3 / 4;
		
		damagemultiplier = 1;
		level = 1;
		
		center_x = x + width/2;
		center_y = y + height/2;
		collider = new Rectangle(x, y, width, height);
		
		tex = new Texture[3];
		tex[0] = this.manager.get("data/towers/SlingShotTower.png");
		tex[1] = this.manager.get("data/towers/SlingShotTowerUpgrade.png");
		tex[2] = this.manager.get("data/towers/SlingShotTowerUpgrade.png");
		//tex[2] = this.manager.get("data/towers/charz.png");
		//add textures 2-5 for lvls 2 and 3
		
		current_tex = 0;
		//toggle_count = CALLS_BETWEEN_TOGGLE;
	}
	
	public void levelUp()
	{
		level++;
		damagemultiplier++;
		firing_speed = firing_speed*0.75f;
		range = range*1.25f;
		current_tex++;
	}
	
	
	public void update()
	{
		for (int i = 0; i < bullets.size(); i++)
		{
			bullets.get(i).update();
			if (!bullets.get(i).isActive())
				bullets.remove(i--);
		}
		time_since_last_shot += Gdx.graphics.getDeltaTime();
		if (time_since_last_shot > firing_speed)
		{
			for (int i = 0; i < enemies.size(); i++)
			{
				float xE = (center_x - enemies.get(i).getX());
				float yE = (center_y - enemies.get(i).getY());
				if (Math.sqrt(xE*xE + yE*yE) < range)
				{
					target = enemies.get(i);
					bullets.add(new Pebble(target, center_x, center_y, damagemultiplier, manager));
					time_since_last_shot = 0;
					break;
				}
			}
		}
	}
	
	public void render(SpriteBatch batch)
	{
		// render Tower
		/* 2 different textures for each level of tower
		 * if (level = 1)
		 * { batch.draw(tex[current_tex], collider.x, collider.y; }
		 * if (level = 2)
		 * { batch.draw(tex[current_tex+2], collider.x, collider.y; }
		 * if (level = 3)
		 * { batch.draw(tex[current_tex+4], collider.x, collider.y; }
		 */
		batch.draw(tex[current_tex], collider.x, collider.y);
		
		// toggle textures
		//if (--toggle_count < 0)
		//{
		//	current_tex = (current_tex + 1) % tex.length;
		//	toggle_count = CALLS_BETWEEN_TOGGLE;
		//}
		
		// render Bullets
		for (Bullet bullet : bullets)
			bullet.render(batch);
	}
}
