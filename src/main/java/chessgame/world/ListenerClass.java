package chessgame.world;

import chessgame.app.Game;
import chessgame.entities.*;
import chessgame.server.pings.NextMapPing;
import chessgame.server.pings.P2WaitingPing;
import chessgame.utils.EntityManager;
import chessgame.utils.GameSound;
import chessgame.utils.Rumble;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class ListenerClass implements ContactListener{
	
	private PhysicsWorld world;
	EntityManager manager;
	Player player;
	IEnemies enemy;
	
	private List<String> unjumpable = new ArrayList<String>();
	
	ListenerClass(PhysicsWorld world){
		prepareJumpable();
		this.world = world;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		//checks if the fixture is the "groundCheck-platter" for the player, if it is, we change the players controller isGrounded to true		
		if(fixtureA.getUserData() == "foot" && checkJumpable(fixtureB.getUserData())) {
		 	player = (Player) fixtureA.getBody().getUserData();

		 	if(!(fixtureB.getBody().getUserData() instanceof IEnemies))
		 		player.playDust();
		 	if(player.controller != null)
		 		player.controller.isGrounded = true;
		}
		else if(fixtureB.getUserData() == "foot" && checkJumpable(fixtureA.getUserData())) {
			player = (Player) fixtureB.getBody().getUserData();

		 	if(!(fixtureA.getBody().getUserData() instanceof IEnemies))
		 		player.playDust();
			if(player.controller != null)
				player.controller.isGrounded = true;
		}
		//checks if the fixture is a enemy weakpoint
		if(fixtureA.getUserData() == "weakpoint" && fixtureB.getUserData() == "foot") {
			player = (Player) fixtureB.getBody().getUserData();
		 	enemy = (IEnemies) fixtureA.getBody().getUserData();
		 	enemy.takeDamage(player.getAttack());
			player.myBody.setLinearVelocity(player.myBody.getLinearVelocity().x, 0);
			player.jump(10000f);
			if(player.controller != null)
				player.controller.isGrounded = false;
		}
		else if(fixtureB.getUserData() == "weakpoint" && fixtureA.getUserData() == "foot") {
			player = (Player) fixtureA.getBody().getUserData();
			enemy = (IEnemies) fixtureB.getBody().getUserData();
			enemy.takeDamage(player.getAttack());
			player.myBody.setLinearVelocity(player.myBody.getLinearVelocity().x, 0);
			player.jump(10000f);
			if(player.controller != null)
				player.controller.isGrounded = false;
		}
		//Checks if there is an object above the player
		if(fixtureA.getUserData() != "air" && fixtureA.getUserData() != "door" && fixtureA.getUserData() != "Object" && fixtureB.getUserData() == "sky") {
			player = (Player) fixtureB.getBody().getUserData();
			if(player.controller != null)
				player.controller.clearJump = false;
		}
		else if(fixtureB.getUserData() != "air" && fixtureB.getUserData() != "door" && fixtureB.getUserData() != "Object" && fixtureA.getUserData() == "sky") {
			player = (Player) fixtureA.getBody().getUserData();
			if(player.controller != null)
				player.controller.clearJump = false;
		}
		//Checks if the player is colliding with an object
		if(fixtureA.getUserData() == "Object" && fixtureB.getUserData() == "Player") {
			IObjects object = (IObjects) fixtureA.getBody().getUserData();
			player = (Player) fixtureB.getBody().getUserData();
			object.itemFunction(player);
		}
		else if(fixtureB.getUserData() == "Object" && fixtureA.getUserData() == "Player") {
			IObjects object = (IObjects) fixtureB.getBody().getUserData();
			player = (Player) fixtureA.getBody().getUserData();
			object.itemFunction(player);
		}
		//TODO make an invulnerability system or something. to prevent player from,
		//dying when jumping on enemy.
		
		//Checks whether the player interacts with an enemy, if so, take damage.
		if(fixtureA.getUserData() == "Enemy" && fixtureB.getUserData() == "Player") {
			IEnemies enemy = (IEnemies) fixtureA.getBody().getUserData();
			player = (Player) fixtureB.getBody().getUserData();
			
			//Calulates angle to launch player after damage
			Vector2 playerPos = player.getPosition();
			Vector2 enemyPos = enemy.getPosition();
			Vector2 test = new Vector2(Math.abs(playerPos.x-enemyPos.x), Math.abs(playerPos.y-enemyPos.y));
			
			player.myBody.applyForceToCenter(new Vector2(5000f * test.x, 5000f * test.y), true);
			player.takeDamage(enemy.getAttack());
		}
		else if(fixtureB.getUserData() == "Enemy" && fixtureA.getUserData() == "Player") {
			IEnemies enemy = (IEnemies) fixtureB.getBody().getUserData();
			player = (Player) fixtureA.getBody().getUserData();
			
			//Calulates angle to launch player after damage
			Vector2 playerPos = player.getPosition();
			Vector2 enemyPos = enemy.getPosition();
			
			Vector2 test = new Vector2(Math.abs(playerPos.x-enemyPos.x), Math.abs(playerPos.y-enemyPos.y));
			
			player.myBody.applyForceToCenter(new Vector2(5000f * test.x, 5000f * test.y), true);
			player.takeDamage(enemy.getAttack());
		}
		//Checks if player touches the Portal
		if(fixtureA.getUserData() == "Portal" && fixtureB.getUserData() == "Player") {
			player = (Player) fixtureB.getBody().getUserData();
			world.entityManager.removePlayer(player);
			if(!Game.isMultiplayer)
				Portal.victory();
			else if((Game.isHost && player.getPlayerId() == "player1") || (!Game.isHost && player.getPlayerId() == "player2")) {
				Game.isWaiting = true;
				Game.getClient().getClient().sendTCP(new P2WaitingPing(player.getPlayerId()));


			}
			else if(((!Game.isHost && player.getPlayerId() == "player1") || (Game.isHost && player.getPlayerId() == "player2"))&& Game.isWaiting) {
				Game.getClient().getClient().sendTCP(new NextMapPing());
			}
		}
		else if(fixtureB.getUserData() == "Portal" && fixtureA.getUserData() == "Player") {
			player = (Player) fixtureA.getBody().getUserData();
			world.entityManager.removePlayer(player);
			if(!Game.isMultiplayer)
				Portal.victory();
			else if((Game.isHost && player.getPlayerId() == "player1") || (!Game.isHost && player.getPlayerId() == "player2")) {
				Game.isWaiting = true;
				Game.getClient().getClient().sendTCP(new P2WaitingPing(player.getPlayerId()));

			}
			else if(((!Game.isHost && player.getPlayerId() == "player1") || (Game.isHost && player.getPlayerId() == "player2")) && Game.isWaiting) {
				Game.getClient().getClient().sendTCP(new NextMapPing());
			}
		}
		
		//checks if an entities jump-sensor is hitting any non-entity objects, making sure
		//it differenciates leftJumpSensor from rightJunpSensor depending on linearvelocity.
		if((fixtureA.getUserData() == "rightJumpSensor" || fixtureA.getUserData() == "leftJumpSensor") && (fixtureB.getBody().getUserData() == "ground")) {
			if (fixtureA.getUserData() == "rightJumpSensor" || fixtureA.getUserData() == "rightJumpSensor2" && fixtureA.getBody().getLinearVelocity().x > 0) {
				IEnemies enemy = (IEnemies) fixtureA.getBody().getUserData();
				enemy.jump();
			}
			else if(fixtureA.getUserData() == "leftJumpSensor" && fixtureA.getBody().getLinearVelocity().x < 0) {
				IEnemies enemy = (IEnemies) fixtureA.getBody().getUserData();
				enemy.jump();
			}
			
		}

		else if((fixtureB.getUserData() == "rightJumpSensor" || fixtureB.getUserData() == "leftJumpSensor") && (fixtureA.getBody().getUserData() == "ground")) {
			if (fixtureB.getUserData() == "rightJumpSensor" && fixtureB.getBody().getLinearVelocity().x > 0) {
				IEnemies enemy = (IEnemies) fixtureB.getBody().getUserData();
				enemy.jump();
			}
			else if(fixtureB.getUserData() == "leftJumpSensor" && fixtureB.getBody().getLinearVelocity().x < 0) {
				IEnemies enemy = (IEnemies) fixtureB.getBody().getUserData();
				enemy.jump();
			}
		}
		
		if (fixtureA.getUserData() == "stopper" && fixtureB.getUserData() != "Object") {
			Tower tower = (Tower) fixtureA.getBody().getUserData();
			tower.stopped();
		} else if(fixtureB.getUserData() == "stopper" && fixtureA.getUserData() != "Object"){
			Tower tower = (Tower) fixtureB.getBody().getUserData();
			tower.stopped();
		}
		
		
		//Checks if the Knight enemy has landed
				if (fixtureA.getUserData() == "hoof" && checkJumpable(fixtureB.getUserData())) {
					if (fixtureA.getBody().getUserData().getClass().equals(Knight.class)) {
						Knight knight = (Knight) fixtureA.getBody().getUserData();
						knight.grounded();
					}
					if (fixtureA.getBody().getUserData().getClass().equals(KnightBoss.class)) {
						KnightBoss knight = (KnightBoss) fixtureA.getBody().getUserData();
						knight.grounded();
					}
					
				} else if (fixtureB.getUserData() == "hoof" && checkJumpable(fixtureA.getUserData())) {
					if (fixtureB.getBody().getUserData().getClass().equals(Knight.class)) {
						Knight knight = (Knight) fixtureB.getBody().getUserData();
						knight.grounded();
					} 
					if (fixtureB.getBody().getUserData().getClass().equals(KnightBoss.class)) {
						KnightBoss knight = (KnightBoss) fixtureB.getBody().getUserData();
						knight.grounded();
					}
				}
				//Checks if the Knight landed on the player or an other enemy
				if (fixtureA.getUserData() == "hoof" && fixtureB.getUserData() == "Player" || fixtureA.getUserData() == "hoof" && fixtureB.getUserData() == "weakpoint") {
					if (fixtureA.getBody().getUserData().getClass().equals(Knight.class)) {
						Knight knight = (Knight) fixtureA.getBody().getUserData();
						knight.jump();
						if (fixtureB.getBody().getUserData().getClass().equals(Player.class)) {
							Player player = (Player) fixtureB.getBody().getUserData();
							player.takeDamage(knight.getAttack());
						} else {
							IEnemies entity = (IEnemies) fixtureB.getBody().getUserData();
							entity.takeDamage(knight.getAttack());
						}
					} else {
						KnightBoss knight = (KnightBoss) fixtureA.getBody().getUserData();
						knight.jump();
						if (fixtureB.getBody().getUserData().getClass().equals(Player.class)) {
							Player player = (Player) fixtureB.getBody().getUserData();
							player.takeDamage(knight.getAttack());
						} else {
							IEnemies entity = (IEnemies) fixtureB.getBody().getUserData();
							entity.takeDamage(knight.getAttack());
						}
					}
				} else if (fixtureB.getUserData() == "hoof" && fixtureA.getUserData() == "Player" || fixtureB.getUserData() == "hoof" && fixtureA.getUserData() == "weakpoint" && fixtureB.getUserData() == "hoof" && fixtureA.getUserData() == "sky") {
					if (fixtureB.getBody().getUserData().getClass().equals(Knight.class)) {
						Knight knight = (Knight) fixtureB.getBody().getUserData();
						knight.jump();
						if (fixtureA.getBody().getUserData().getClass().equals(Player.class)) {
							Player player = (Player) fixtureA.getBody().getUserData();
							player.takeDamage(knight.getAttack());
						} else {
							IEnemies entity = (IEnemies) fixtureA.getBody().getUserData();
							entity.takeDamage(knight.getAttack());
						}
					} else {
						KnightBoss knight = (KnightBoss) fixtureB.getBody().getUserData();
						knight.jump();
						if (fixtureA.getBody().getUserData().getClass().equals(Player.class)) {
							Player player = (Player) fixtureA.getBody().getUserData();
							player.takeDamage(knight.getAttack());
						} else {
							IEnemies entity = (IEnemies) fixtureA.getBody().getUserData();
							entity.takeDamage(knight.getAttack());
						}
					}
					
				}
				
				//Checks if the knightBoss bumps a wall
				if (fixtureA.getUserData() == "bumper" && checkJumpable(fixtureB.getUserData())) {
					KnightBoss knight = (KnightBoss) fixtureA.getBody().getUserData();
					knight.bump();
				}
				if (fixtureB.getUserData() == "bumper" && checkJumpable(fixtureA.getUserData())) {
					KnightBoss knight = (KnightBoss) fixtureB.getBody().getUserData();
					knight.bump();
				}
		
				
			if((fixtureB.getUserData() == "Bullet")) {
				Bullet bullet = ((Bullet) fixtureB.getBody().getUserData());
				if (fixtureA.getBody().getUserData() instanceof Player){
					Player player = (Player) fixtureA.getBody().getUserData();
					player.takeDamage(1);
					bullet.kill();
				} 
			}
			else if((fixtureA.getUserData() == "Bullet")) {
				Bullet bullet = ((Bullet) fixtureA.getBody().getUserData());
				if (fixtureB.getBody().getUserData() instanceof IEnemies){
					IEnemies enemy = (IEnemies) fixtureB.getBody().getUserData();
					enemy.takeDamage(1);
					bullet.kill();
				} else if(fixtureB.getBody().getUserData() instanceof Player) {
					
				}
				else {
					bullet.kill();
				}
			}		
		
		if((fixtureB.getUserData() == "EnemyBullet")) {
			Bullet bullet = ((Bullet) fixtureB.getBody().getUserData());
			if (fixtureA.getBody().getUserData() instanceof Player){
				Player player = (Player) fixtureA.getBody().getUserData();
				player.takeDamage(1);
				bullet.kill();
			} 
		}
		else if((fixtureA.getUserData() == "EnemyBullet")) {
			Bullet bullet = ((Bullet) fixtureA.getBody().getUserData());
			if (fixtureB.getBody().getUserData() instanceof Player){
				Player player = (Player) fixtureB.getBody().getUserData();
				player.takeDamage(1);
				bullet.kill();
			} 
		}
		
		TheTowerSensors(fixtureA, fixtureB);
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		//checks if the fixture is the "groundCheck-platter" for the player, if it is, we change the players controller isGrounded to falsew
		if(fixtureA.getUserData() == "foot" && checkJumpable(fixtureB.getUserData()) && fixtureB.getUserData() != "door") {
		 	player = (Player) fixtureA.getBody().getUserData();
		 	if(player.controller != null)
		 			player.controller.isGrounded = false;
		}
		if(fixtureB.getUserData() == "foot" && checkJumpable(fixtureA.getUserData()) && fixtureA.getUserData() != "door") {
			player = (Player) fixtureB.getBody().getUserData();
			if(player.controller != null)
				player.controller.isGrounded = false;
		}
		if(fixtureA.getUserData() == "sky") {
			player = (Player) fixtureA.getBody().getUserData();
			if(player.controller != null)
				player.controller.clearJump = true;
		}
		if(fixtureB.getUserData() == "sky") {
			player = (Player) fixtureB.getBody().getUserData();
			if(player.controller != null)
				player.controller.clearJump = true;
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	private void prepareJumpable() {
		unjumpable.add("Object");
		unjumpable.add("Enemy");
		unjumpable.add("Portal");
		unjumpable.add("rightJumpSensor");
		unjumpable.add("leftJumpSensor");
		unjumpable.add("Player");
		unjumpable.add("Bullet");
		unjumpable.add("sky");
		unjumpable.add("ColorBlockOff");
		unjumpable.add("Wave");
	}
	
	private <T> boolean checkJumpable(T name) {
		return !unjumpable.contains(name);
	}

	private void TheTowerSensors(Fixture fixtureA, Fixture fixtureB) {
		//Checks for TheTowerBoss
		if(fixtureA.getUserData() == "TLHand") {
			TheTower tower = (TheTower) fixtureA.getBody().getUserData();
			
			if(!(fixtureB.getBody().getUserData() instanceof Player))
				tower.freezeHands(true);
			if(!(fixtureB.getBody().getUserData() instanceof Player)) {
				tower.returnHand = true;
				tower.smash = false;
				tower.freezeHands(true);
				Rumble.rumble(0.2f, 0.2f);
				GameSound.playSoundEffect(2, 1);
			} else if (!tower.frozen) {
				if(fixtureB.getBody().getUserData() instanceof Player) {
					player = (Player) fixtureB.getBody().getUserData();
					if(!tower.returnHand)
						player.takeDamage(tower.getAttack());
				}
				tower.returnHand = true;
				tower.smash = false;
				tower.quickReturn = true;
				Rumble.rumble(0.2f, 0.2f);
				GameSound.playSoundEffect(2, 1);
			}
		}
		else if(fixtureB.getUserData() == "TLHand") {
			TheTower tower = (TheTower) fixtureB.getBody().getUserData();
			
			if(!(fixtureB.getBody().getUserData() instanceof Player))
				tower.freezeHands(true);
			if(!(fixtureB.getBody().getUserData() instanceof Player)) {
				tower.returnHand = true;
				tower.smash = false;
				tower.freezeHands(true);
				Rumble.rumble(0.2f, 0.2f);
				GameSound.playSoundEffect(2, 1);
			} else if (!tower.frozen) {
				if(fixtureA.getBody().getUserData() instanceof Player) {
					player = (Player) fixtureA.getBody().getUserData();
					if(!tower.returnHand)
						player.takeDamage(tower.getAttack());
				}
				tower.returnHand = true;
				tower.smash = false;
				tower.quickReturn = true;
				Rumble.rumble(0.2f, 0.2f);
				GameSound.playSoundEffect(2, 1);
			}
		}
		
		if(fixtureA.getUserData() == "Tfoot") {
			TheTower tower = (TheTower) fixtureA.getBody().getUserData();
			if(tower.getCurrentState() == tower.jumpState) {
				Rumble.rumble(.4f, .4f);
				tower.shockWave();
				if(tower.jumpCounter == 0) {
					tower.changeState(tower.idleState);
				}
				if(fixtureB.getBody().getUserData() instanceof Player)
					player.takeDamage(tower.getAttack());
				tower.jump = false;
			}
		}
		else if(fixtureB.getUserData() == "Tfoot") {
			TheTower tower = (TheTower) fixtureB.getBody().getUserData();
			if(tower.getCurrentState() == tower.jumpState) {
				Rumble.rumble(.4f, .4f);
				tower.shockWave();
				tower.changeState(tower.idleState);
				if(fixtureA.getBody().getUserData() instanceof Player)
					player.takeDamage(tower.getAttack());
				tower.jump = false;
			}
		}
		if(fixtureA.getUserData() == "Wave" && fixtureB.getBody().getUserData() instanceof Player) {
			TheTower tower = (TheTower) fixtureA.getBody().getUserData();
			player = (Player) fixtureB.getBody().getUserData();
			player.takeDamage(tower.getAttack());
			tower.shockWave = false;
		}
		if(fixtureB.getUserData() == "Wave" && fixtureA.getBody().getUserData() instanceof Player) {
			TheTower tower = (TheTower) fixtureB.getBody().getUserData();
			player = (Player) fixtureA.getBody().getUserData();
			tower.shockWave = false;
			player.takeDamage(tower.getAttack());
		}
	}
}
