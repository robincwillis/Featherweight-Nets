package parts;
import traer.physics.*;
import processing.core.PApplet;
import elements.*;

public class Mast {

	PApplet p;
	ParticleSystem x;
	public Particle aPoint;
	public Particle bPoint;
	public Spring shaft;
	float[] m;
	
	public boolean aSet, bSet;
	float STRENGTH =(float) 0.6;
	float RESTLENGTH =(float) 100;
	float DAMPING =(float) 0.1;
	
	public Mast(PApplet parent,float[] mousePos, ParticleSystem physics){
		p = parent;
		x = physics;
		m = mousePos;
		aPoint = x.makeParticle();
		bPoint = x.makeParticle();
		aSet = false;
		bSet = false;
		
	}
	
	public Mast(PApplet parent, float[] mousePos, ParticleSystem physics, Particle a, Particle b){
		p = parent;
		x = physics;
		m = mousePos;
		aPoint = a;
		bPoint = b;
		shaft = x.makeSpring(aPoint, bPoint, STRENGTH, DAMPING, RESTLENGTH);
		aSet = false;
		bSet = false;
	}
	
	//set length
	public void setLength(float l){
		this.shaft.setRestLength(l);
	}
	
	//set damping
	public void setDamping(float d){
		this.shaft.setDamping(d);
	}
	//set strength
	public void setStrength(float ks){
		this.shaft.setStrength(ks);
	}
	
	public void setAPos(Point aRef){
		aPoint.position().set(aRef.a.x, aRef.a.y, aRef.a.z);
		//bPoint.position().set(aRef.a.x, aRef.a.y, aRef.a.z+100);
		aPoint.setMass(1);
		aPoint.makeFixed();
		//bPoint.makeFixed();
		aSet = true;
		makeSpring();
	}
	
	public void setAPos(Particle aRef){
		aPoint = aRef;
		aSet = true;
		makeSpring();
	}
	
	public void setBPos(Point bRef){
		bPoint.position().set(bRef.a.x, bRef.a.y, bRef.a.z);
		bPoint.setMass(1);
		bPoint.makeFixed();
		bSet = true;
		makeSpring();
	}
	
	public void setBPos(float x, float y, float z){
		bPoint.position().set(x, y, z);
		bPoint.setMass(1);
		bPoint.makeFixed();
		bSet = true;
		makeSpring();
	}
	
	public void setBPos(Particle bRef){
		bPoint = bRef;
		bSet = true;
		makeSpring();
	}
	
	public void makeSpring(){
		if(aSet && bSet){
			shaft = x.makeSpring(aPoint, bPoint, STRENGTH, DAMPING, RESTLENGTH);
		}
	}
	
	//draw
	public void draw(){
		
		if(aSet && bSet){
		p.pushStyle();
		p.stroke(10);
		p.strokeWeight(3);
		p.line(
		aPoint.position().x(),aPoint.position().y(),aPoint.position().z(),
		bPoint.position().x(),bPoint.position().y(),bPoint.position().z()
		);
		
		p.popStyle();
		}
		

		
	}
	
}
