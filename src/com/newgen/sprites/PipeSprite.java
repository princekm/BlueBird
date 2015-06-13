package com.newgen.sprites;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PipeSprite extends Sprite{
	final FixtureDef FIXTURE;
	final Body body;
	public PipeSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,PhysicsWorld physicsWorld) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		FIXTURE = PhysicsFactory.createFixtureDef(10.0f,
				0.0f, 5.0f);
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.KinematicBody, FIXTURE);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				body, true, false));
		body.setLinearVelocity(new Vector2(-3,0));
		body.setUserData("pipe");
	}
	public Body getBody(){
		return body;
	}

}
