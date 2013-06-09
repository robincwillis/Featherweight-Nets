package parts;

import processing.core.PApplet;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;
import elements.*;

public class Cable {

	PApplet p;
	ParticleSystem x;
	public Particle[] particles;
	Spring[] springs;
	public Particle aPoint, bPoint;
	
	float MASS = (float)0.1;
	public boolean aSet, bSet;
	float SPRING_STRENGTH = (float)0.1;
	float SPRING_DAMPING =  (float)0.1;
	float REST_LENGTH = 10;
	float STEP_SIZE =10;
	public int CHAIN_COUNT =10;
	//int GRID_YCOUNT =10;
	
	public Cable(PApplet parent, ParticleSystem physics){
		p = parent;
		x = physics;
	    particles = new Particle[CHAIN_COUNT];
		springs = new Spring[CHAIN_COUNT-1];
		
		buildCable();
		//setAPos(0,0,0);
		aSet = false;
		bSet = false;
		//setBPos(100,0,0);
	}
	
	public Cable(PApplet parent, ParticleSystem physics, Point a, Point b){
		p = parent;
		x = physics;
	    particles = new Particle[CHAIN_COUNT];
		springs = new Spring[CHAIN_COUNT-1];
		
		buildCable();
		aSet = false;
		bSet = false;
	}

	public Cable(PApplet parent, ParticleSystem physics, float aX, float aY, float aZ, float bX, float bY, float bZ){
		p = parent;
		x = physics;
	    particles = new Particle[CHAIN_COUNT];
		springs = new Spring[CHAIN_COUNT-1];
		
		buildCable();
		//setAPos(aX,aY,aZ);
		//setBPos(bX,bY,bZ);
		aSet = false;
		bSet = false;
	}
	
	
	void buildCable(){

	
		  
		buildParticles();
		//buildSprings();

		aPoint = particles[0];
		bPoint = particles[CHAIN_COUNT-1];
		 // fixEnd(particles[0]);
		 // fixEnd(particles[CHAIN_COUNT-1]);
		}
	
	void buildParticles(){
		  for (int i=0;i<CHAIN_COUNT;i++){  
		       particles[i] = x.makeParticle();   
		       particles[i].setMass(MASS);
		       //particles[i] = x.makeParticle(MASS, (float) i*STEP_SIZE ,0,0);
		      // particles[i].makeFixed();

	  }
	}
	void buildSprings(){
		  for (int j = 1;j<CHAIN_COUNT;j++){
			    springs[j-1] =  x.makeSpring(particles[j-1],particles[j], SPRING_STRENGTH, SPRING_DAMPING, REST_LENGTH);
			    

		  	}  
	}
	
	void removeSprings(){
		  for (int j = 0;j<CHAIN_COUNT-1;j++){
			    x.removeSpring(springs[j]);
		  	}  

	}
	
	void freeEnd(Particle endPoint){
		endPoint.makeFree();
	}
	void fixEnd(Particle endPoint){
		endPoint.makeFixed();
	}
	
	public void setAPos(float x, float y, float z){
		particles[0].position().set(x, y, z);
		aSet = true;
		fixEnd(particles[0]);
		aPoint = particles[0];
		buildSprings();
	}
	
	public void setBPos(float x, float y, float z){
		particles[CHAIN_COUNT-1].position().set(x, y, z);
		bSet = true;
		fixEnd(particles[CHAIN_COUNT-1]);
		buildSprings();
	}
	
	public void setAPos(Point aRef){
		particles[0].position().set(aRef.a.x, aRef.a.y, aRef.a.z);
		aSet = true;
		fixEnd(particles[0]);
		aPoint = particles[0];
		buildSprings();
	}
	
	public void setBPos(Point bRef){
		particles[CHAIN_COUNT-1].position().set(bRef.a.x, bRef.a.y, bRef.a.z+100);
		bSet = true;
		fixEnd(particles[CHAIN_COUNT-1]);
		buildSprings();
	}
	
	public void setAPos(Particle aRef){
		particles[0] = aRef;
		aSet = true;
		//fixEnd(particles[0]);
		aPoint = particles[0];
		removeSprings();
		buildSprings();
	}
	
	public void setBPos(Particle bRef){
		particles[CHAIN_COUNT-1] = bRef;
		bSet = true;
		//fixEnd(particles[CHAIN_COUNT-1]);
		removeSprings();
		buildSprings();
		//fixEnd(particles[CHAIN_COUNT-1]);
	}
	
	
	
	public void draw(){
		p.pushStyle();
		p.stroke(1);
		
		p.noFill();
		
		  
		    p.beginShape();
		    p.curveVertex(particles[0].position().x(), particles[0].position().y(),particles[0].position().z() );
		   
		    for (int i = 0; i < CHAIN_COUNT; i++)
		    {
		      p.curveVertex(particles[i].position().x(), particles[i].position().y(),particles[i].position().z());
		    }
		    p.curveVertex(particles[CHAIN_COUNT - 1].position().x(), particles[CHAIN_COUNT - 1].position().y(), particles[CHAIN_COUNT - 1].position().z());
		    p.endShape();
		  
		
		p.popStyle();
	}
	
	public void drawPoints(){
		for(int i=0;i<CHAIN_COUNT;i++){
			
				p.pushMatrix();
				p.translate(particles[i].position().x(), particles[i].position().y(), particles[i].position().z());
				
				p.sphere(2);
				p.popMatrix();
			
		}
		
	}
	
	
}
