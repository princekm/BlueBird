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

public class BorderSprite extends Sprite{
	final FixtureDef FIXTURE;
	final Body body;
	public BorderSprite(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,PhysicsWorld physicsWorld,String userdata) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		FIXTURE = PhysicsFactory.createFixtureDef(10.0f,
				0.0f, 0.2f);
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.StaticBody, FIXTURE);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				body, true, false));
		body.setUserData(userdata);
	}
	public Body getBody(){
		return body;
	}

}
