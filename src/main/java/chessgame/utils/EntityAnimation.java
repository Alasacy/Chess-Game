package chessgame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import chessgame.entities.IEntities;

public class EntityAnimation {
	
	Texture spriteSheet;
	int frames;
	float framesPerSecond;
	IEntities entity;
	int width;
	int height;
	boolean looping = true;
	
	TextureRegion animationFrames[];
	Animation<?> animation;
	float elapsedTime;
	
	public EntityAnimation(Texture spriteSheet, int frames, float framesPerSecond, IEntities entity, Vector2 spriteSize){
		this.spriteSheet = spriteSheet;
		this.frames = frames;
		this.framesPerSecond = 1f/framesPerSecond;
		this.entity = entity;
		this.width = (int) spriteSize.x;
		this.height = (int) spriteSize.y;
		
		buildAnimation();
		
		elapsedTime = 0;
		animation = new Animation(this.framesPerSecond, animationFrames);
	}
	public EntityAnimation(Texture spriteSheet, int frames, float framesPerSecond, IEntities entity, Vector2 spriteSize, boolean looping){
		this.spriteSheet = spriteSheet;
		this.frames = frames;
		this.framesPerSecond = 1f/framesPerSecond;
		this.entity = entity;
		this.width = (int) spriteSize.x;
		this.height = (int) spriteSize.y;
		this.looping = looping;
		
		buildAnimation();
		
		elapsedTime = 0;
		animation = new Animation(this.framesPerSecond, animationFrames);
	}
	
	public void buildAnimation() {
		animationFrames = new TextureRegion[frames];
		int index = 0;
		
		TextureRegion[] [] tmpFrames = TextureRegion.split(spriteSheet, width, height);
		for (int i = 0; i < frames; i++) {
				animationFrames[index++] = tmpFrames [0] [i];
		}
	}
	
	public void render(Batch batch) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
		
		Vector2 position = entity.getPosition();
		
		if(batch != null) {
			batch.draw(currentFrame, position.x, position.y, width/32, height/32);
		}
	}
	public void render(Batch batch, float xPos, float yPos, boolean fix) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
		
		if(batch != null) {
			if(fix)
				batch.draw(currentFrame, xPos, yPos, width/16, height/16);
			else
				batch.draw(currentFrame, xPos, yPos, width/32, height/32);
		}
	}
	public boolean playOnce(Batch batch) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
		
		Vector2 position = entity.getPosition();
		
		if(batch != null) {
			batch.draw(currentFrame, position.x, position.y, width/32, height/32);
		}
		if(animation.getKeyFrameIndex(elapsedTime) == frames-1) {
			elapsedTime = 0;
			return false;
		}
		return true;
	}
	public boolean playOnce(Batch batch, float xPos, float yPos, Boolean state) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
		
		Vector2 position = entity.getPosition();
		
		if(batch != null) {
			batch.draw(currentFrame, xPos, yPos, width/16, height/16);
		}
		if(animation.getKeyFrameIndex(elapsedTime) == frames-1) {
			elapsedTime = 0;
			return false;
		}
		return true;
	}
	public boolean playOnce(Batch batch, float xPos, float yPos) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
		
		if(batch != null) {
			batch.draw(currentFrame, xPos, yPos, width/32, height/32);
		}
		if(animation.getKeyFrameIndex(elapsedTime) == frames-1) {
			elapsedTime = 0;
			return false;
		}
		return true;
	}
	
	public void changeSheet(Texture spriteSheet) {
		this.spriteSheet = spriteSheet;
		buildAnimation();
		animation = new Animation(this.framesPerSecond, animationFrames);
	}
}
