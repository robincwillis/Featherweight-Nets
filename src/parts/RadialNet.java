package parts;

import java.awt.Stroke;

import processing.core.PApplet;
import traer.physics.Particle;
import traer.physics.ParticleSystem;
import traer.physics.Spring;
import elements.*;

public class RadialNet {

	PApplet p;
	ParticleSystem x;
	public Point pos;
	public Particle centroid; 
	public Particle baseCentroid;
	public Particle[][] particles;
	public Spring[][] uSprings;
	public Spring[][] vSprings;
	public Spring[] cSprings;
	
	float MASS = (float)0.15;
	public float SPRING_STRENGTH = (float)0.1;
	public float SPRING_DAMPING =  (float)0.1;
	
	public float STEP_SIZE =20;
	int HEIGHT = 300;
	int RADIALS = 20;
	int RINGS = 10;
	public int TOP_RING = 20;
	
	public boolean SET;
	
	public RadialNet(PApplet parent, ParticleSystem physics){
		p = parent;
		x = physics;
		baseCentroid = x.makeParticle();
		centroid = x.makeParticle(MASS,0,0,HEIGHT);
		centroid.makeFixed();
		particles = new Particle[RINGS][RADIALS];
		uSprings = new Spring[RADIALS][RINGS-1];
		vSprings = new Spring[RINGS][RADIALS];
		cSprings = new Spring[RADIALS];
		buildNet();
		fixEdge();
	}
	
	public RadialNet(PApplet parent, ParticleSystem physics, int _STEP_SIZE, int _RINGS, int _RADIALS, int _HEIGHT, int _TOP_RING){
		
		
		p = parent;
		x = physics;
		STEP_SIZE = _STEP_SIZE;
		RINGS = _RINGS;
		RADIALS = _RADIALS;
		HEIGHT = _HEIGHT;
		TOP_RING = _TOP_RING;
		SET = false;
		
		
		particles = new Particle[RINGS][RADIALS];
		uSprings = new Spring[RADIALS][RINGS-1];
		vSprings = new Spring[RINGS][RADIALS];
		cSprings = new Spring[RADIALS];
		
		//buildNet();
		//fixEdge();
	}
	
	public void buildNet(){
		
		baseCentroid = x.makeParticle();
		centroid = x.makeParticle(MASS,pos.a.x,pos.a.y,pos.a.z+HEIGHT);
		centroid.makeFixed();
		float angle;
		float radius;
		
		for(int i=0; i<RINGS;i++){
			
			radius =TOP_RING + i * STEP_SIZE;
			
			for(int j=0;j<RADIALS;j++){
				angle = (360/RADIALS)*j;
				
				 float px = p.cos(p.radians(angle))*(radius);
				 float py = p.sin(p.radians(angle))*(radius);
				 
				particles[i][j] = x.makeParticle(MASS,pos.a.x+px ,pos.a.y+py,pos.a.z+(float)0.0);
				
				 if( i == RINGS-1){
					 particles[i][j].position().set(pos.a.x+px*2, pos.a.y+py*2,pos.a.z+0);
				 }
				
			}
		}
		

		
		for(int i=0;i<RINGS;i++){
			for(int j =0;j<RADIALS;j++){
				 if(j == 0){
					vSprings[i][j] =  x.makeSpring(particles[i][RADIALS-1],particles[i][0], SPRING_STRENGTH, SPRING_DAMPING, STEP_SIZE);
				}else{
	
					vSprings[i][j] =  x.makeSpring(particles[i][j-1],particles[i][j], SPRING_STRENGTH, SPRING_DAMPING, STEP_SIZE);
					
				}
			}
		}
		
		
		for(int i=0;i<RADIALS;i++){
			for(int j =1;j<RINGS;j++){


			uSprings[i][j-1] =  x.makeSpring(particles[j-1][i],particles[j][i],SPRING_STRENGTH*2, SPRING_DAMPING, STEP_SIZE);   
					
			}
		}
				
	}
	
	public void fixEdge(){
		int fixCount = 6;
		int fix = 2;
		Particle[] outerRing = getOuterRing();
		
		for(int i=0;i<outerRing.length;i++){

		if(i==fix){
				outerRing[i].makeFixed();
				fix = fix + fixCount;
			//	 p.println("fix"+i);
			}
		}
	
	}
	
	//Draw
	
	public void drawSprings(){

			p.pushStyle();
			 p.stroke(255,255,0,100);
			  p.strokeWeight((float) .75);
			

			  for(int i=0; i< uSprings.length;i++){
				  for(int j=0; j<uSprings[i].length;j++){
					    Spring e = uSprings[i][j];
					    Particle a = e.getOneEnd();
					    Particle b = e.getTheOtherEnd();
					    p.line(a.position().x(),a.position().y(),a.position().z(),b.position().x(),b.position().y(),b.position().z());
				  }
			  }
			  p.stroke(255,0,255,100);
			  for(int i=0; i< vSprings.length;i++){
				  for(int j=0; j<vSprings[i].length;j++){
					    Spring e = vSprings[i][j];
					    Particle a = e.getOneEnd();
					    Particle b = e.getTheOtherEnd();
					    p.line(a.position().x(),a.position().y(),a.position().z(),b.position().x(),b.position().y(),b.position().z());
				  }
			  }
			 
			 p.popStyle();	
	}
	
	public void drawMesh(){
		
		p.pushStyle();
		p.fill(220,50);
		//p.stroke(0);
		//p.strokeWeight(1);
		p.beginShape(p.TRIANGLES);
		
		for(int i=0;i<RINGS-1;i++){
			for(int j=0;j<RADIALS;j++){
				
				if(j<RADIALS-1){
				//first half
				 p.vertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
				 p.vertex(particles[i][j+1].position().x(), particles[i][j+1].position().y(),particles[i][j+1].position().z());
				 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
				
				 p.vertex(particles[i+1][j+1].position().x(), particles[i+1][j+1].position().y(),particles[i+1][j+1].position().z());
				 p.vertex(particles[i][j+1].position().x(), particles[i][j+1].position().y(),particles[i][j+1].position().z());
				 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
				}
				 //Close object
				 
				else if( j == RADIALS-1 ){
					 p.vertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
					 p.vertex(particles[i][0].position().x(), particles[i][0].position().y(),particles[i][0].position().z());
					 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
					 
					 p.vertex(particles[i+1][0].position().x(), particles[i+1][0].position().y(),particles[i+1][0].position().z());
					 p.vertex(particles[i][0].position().x(), particles[i][0].position().y(),particles[i][0].position().z());
					 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
	
				 }
				 
				 // p.vertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
					
			}
			
		}
		
		p.endShape();
		
		p.popStyle();
	}
	
	public void drawCloth(){
		p.pushStyle();
		p.stroke(1);
		p.noFill();
	

		for (int i = 0; i < RINGS; i++)
		  {
		    p.beginShape();
		    
		    for (int j = 0; j < RADIALS; j++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }
	
		    for (int j = 0; j < 3; j++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }      
		    p.endShape();
		  }
		  
		  
		  for (int j = 0; j < RADIALS; j++)
		  {
		    p.beginShape();
		    	p.curveVertex(particles[0][j].position().x(), particles[0][j].position().y(),particles[0][j].position().z());
		    for (int i = 0; i < RINGS; i++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }
		    	p.curveVertex(particles[RINGS - 1][j].position().x(), particles[RINGS - 1][j].position().y(),particles[RINGS - 1][j].position().z());
		    p.endShape();
		  }
		
		 p.popStyle();
	}
	
	public void drawUCloth(){
		p.pushStyle();
		p.stroke(1);
		p.noFill();
	


		  
		  for (int j = 0; j < RADIALS; j++)
		  {
		    p.beginShape();
		    	p.curveVertex(particles[0][j].position().x(), particles[0][j].position().y(),particles[0][j].position().z());
		    for (int i = 0; i < RINGS; i++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }
		    	p.curveVertex(particles[RINGS - 1][j].position().x(), particles[RINGS - 1][j].position().y(),particles[RINGS - 1][j].position().z());
		    p.endShape();
		  }
		
		 p.popStyle();
	}
	
	public void drawVCloth(){
		p.pushStyle();
		p.stroke(1);
		p.noFill();
	
		for (int i = 0; i < RINGS-1; i++)
		  {
		    p.beginShape();
		    
		    for (int j = 0; j < RADIALS; j++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }
	
		    for (int j = 0; j < 3; j++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }      
		    p.endShape();
		  }
		  
		 p.popStyle();
	}
	
	public void drawControls(Vector3D base){
		
		 p.pushMatrix();
		 //Change this to draw from Particle
		 p.translate((float)base.getX(), (float)base.getY(),(float) base.getZ());
	//	 p.sphere(5);
		 p.popMatrix();
		 p.pushStyle();
		 p.stroke(0);
		 p.strokeWeight(1);
		 
	    p.line((float)base.getX(),(float)base.getY(),(float)base.getZ(),centroid.position().x(),centroid.position().y(),centroid.position().z());
		p.popStyle();
		 //draw line from baseCentroid to Centroid
		 //draw Lines from centroid to inner ring
		
	}
	
 	public void draw(){
 	
 	
 		drawControls(baseCentroid(getOuterRing()));
 		updateBaseCentroid(baseCentroid(getOuterRing()));
		updateInnerRing(baseCentroid(getOuterRing()));
	}
	
 	//Update
 	
 	public void updateInnerRing(Vector3D base){
 		
 		Particle[] innerRing = getInnerRing();
		
 		//get centroid position from particle
		 Vector3D cent = new Vector3D(centroid.position().x(),centroid.position().y(),centroid.position().z());
		 Vector3D stem = cent.subtract(base);
		 

		 
		 Vector3D sXY = new Vector3D(stem.getX(),stem.getY(),0);
		 Vector3D sZY = new Vector3D(0,stem.getY(),stem.getZ());
		 Vector3D sZX = new Vector3D(stem.getX(),0,stem.getZ());
		 
		 
		 Vector3D uX;
		 uX = new Vector3D(1,0,0);
		 Vector3D uY;
		 uY = new Vector3D(0,1,0);
		 Vector3D uZ;
		 uZ = new Vector3D(0,0,1);
		 
		 if(stem.getX()<1){
			 uX = new Vector3D(-1,0,0);
		 }if(stem.getY()<1){
			 uY = new Vector3D(0,-1,0);
		 }if(stem.getZ()<1){
			 uZ = new Vector3D(0,0,-1);
		 }

		 
		 float aX = (float)Vector3D.angle(uX, sXY);
		 float aY = (float)Vector3D.angle(uY, sXY);
		 
		 float uZsZX = (float)Vector3D.angle(uZ, sZX);
		 float uZsZY = (float)Vector3D.angle(uZ, sZY);
		 
		 float uXsZX = (float)Vector3D.angle(uX, sZX);
		 float uYsZY = (float)Vector3D.angle(uY, sZY);
		 
		 
		// p.println(aX+ " "+aY+" "+aZ);
		 
		float angle;
	
		for(int i=0;i<RADIALS;i++){
			angle = (360/RADIALS)*i;
			 float px = p.cos(p.radians(angle))*(TOP_RING);
			 float py = p.sin(p.radians(angle))*(TOP_RING);
			 Vector3D point = new Vector3D();
			 //= new Vector3D(px,py,0);
		
			 p.pushStyle();
			 p.fill(100,100);
			 boolean xOn = false;
			 
		if(uZsZX < uXsZX && uZsZY < uYsZY){
			//Z
			
			
			point = new Vector3D(px,py,0);
			//p.println(" zX "+uZsZX+" lX "+uXsZX+" zY "+uZsZY+" lY "+uYsZY);
			
		//	p.rect(-100, -100, 0, 100,100,0);
				
		}else if(aX > aY){
			//YZ
			//p.println("y");
		//	p.rect(0, -100, -100, 0,100,100);
			point = new Vector3D(px,0,py);
			
		}else if(aY > aX){
			//XZ
			xOn = true;
		//	p.rect(-100,0,-100,100,0,100);
			p.println("x");
			if(stem.getX()>0){
			point = new Vector3D(0,px,-py);
			}else{
				point = new Vector3D(0,-px,py);
			}
		}
		 p.popStyle();
		 
		 Vector3D crossA = Vector3D.crossProduct(point, stem);
		 Vector3D cross = Vector3D.crossProduct(crossA, stem);
		if(xOn){
			cross = crossA;
			//cross = Vector3D.crossProduct(stem, crossA);
		}
		 cross = cross.negate();
		 cross = cross.normalize();
		 cross = cross.scalarMultiply(TOP_RING*2);
		 cross = cross.add(cent);
		
		p.pushStyle();
		p.strokeWeight(1);

		p.stroke(255,0,0);
		
		//p.line(0,0,0, (float)point.getX(), (float)point.getY(),(float)point.getZ());
		//p.line((float)crossA.getX(), (float)crossA.getY(), (float)crossA.getZ(), 0,0,0);
		p.line((float)cent.getX(),(float)cent.getY(),(float)cent.getZ(),(float)cross.getX(),(float)cross.getY(),(float)cross.getZ());
		 
		 p.popStyle();
		
		innerRing[i].position().set((float)cross.getX(),(float)cross.getY(),(float)cross.getZ());
		innerRing[i].makeFixed();

		}
		
	}
 	
 	public void updateCentroid(Vector3D c){
		centroid.position().set((float)c.getX(), (float)c.getY(), (float)c.getZ());
		centroid.makeFixed();
 	}

	public void updateBaseCentroid(Vector3D bC){
		
		baseCentroid.position().set((float)bC.getX(), (float)bC.getY(), (float)bC.getZ());
		baseCentroid.makeFixed();
	}
 	
	public void move(Particle selectParticle, float x, float y, float z){
		
		float sX = selectParticle.position().x();
		float sY = selectParticle.position().y();
		float sZ = selectParticle.position().z();
		Vector3D sVec = new Vector3D(sX,sY,sZ);
		
		for(int i=0;i<RINGS;i++){
			for(int j=0;j<RADIALS;j++){
				if(particles[i][j].isFixed()){
				
				
				float  otX=particles[i][j].position().x();
				float  otY=particles[i][j].position().y();
				float  otZ=particles[i][j].position().z();
								
				Vector3D oVec = new Vector3D(otX,otY,otZ);
				Vector3D dVec = sVec.subtract(oVec);
				
				p.pushStyle();
				p.strokeWeight(1);
				p.stroke(0,20);
				//p.line(otX,otY,otZ,(float)dVec.getX()+otX,(float)dVec.getY()+otY,(float)dVec.getZ()+otZ);
				p.line(x, y, z,x-(float) dVec.getX(),y-(float)dVec.getY(),z-(float)dVec.getZ());
				p.popStyle();

				particles[i][j].position().set(x-(float)dVec.getX(),y-(float)dVec.getY(),z-(float)dVec.getZ());		
				
				}
				
				
			}
		}
		
		float cX  = centroid.position().x();
		float cY  = centroid.position().y();
		float cZ  = centroid.position().z();
		
		Vector3D cVec = new Vector3D(cX,cY,cZ);
		Vector3D dVec = sVec.subtract(cVec);
		centroid.position().set(x-(float)dVec.getX(),y-(float)dVec.getY(),z-(float)dVec.getZ());
	//	
	//	updateInnerRing(baseCentroid(getOuterRing()));
	//	updateCentroid(centroid(getInnerRing()));
	//	centroid.position().set(otCX+x,otCY+y,otCZ+z);
		//updateInnerRing(baseCentroid(getOuterRing()));
		//UPDATE = true;
	}
	
	//Centroid // Edges
	
 	public Vector3D centroid(Particle[] ring){
		Vector3D sum = new Vector3D(0,0,0);
		Vector3D[] basePoints = new Vector3D[RADIALS];
		  
		for (int i = 0; i < RADIALS; i++)
		   {  
		      
		     Vector3D basePoint = new Vector3D(ring[i].position().x(),ring[i].position().y(),ring[i].position().z());
		     
		     sum = sum.add(basePoint); // Add location
		     
		   }
		   return sum.div(RADIALS);
 	}
 	
	public Vector3D baseCentroid(Particle[] ring){
		
		Vector3D sum = new Vector3D(0,0,0);
		Vector3D[] basePoints = new Vector3D[RADIALS];
		  
		for (int i = 0; i < RADIALS; i++)
		   {  
		      
		     Vector3D basePoint = new Vector3D(ring[i].position().x(),ring[i].position().y(),ring[i].position().z());
		     
		     sum = sum.add(basePoint); // Add location
		     
		   }
		   return sum.div(RADIALS);
		
	}
 	
	public Particle[] getInnerRing(){
		Particle[] innerRing = new Particle[RADIALS];
		
		for(int i=0;i<RADIALS;i++){
			innerRing[i] = particles[0][i];
		}
		
		return innerRing;
	}
		
	public Particle[] getOuterRing(){

		Particle[] outerRing = new Particle[RADIALS];
		
		for(int i=0;i<RADIALS;i++){
			outerRing[i] = particles[RINGS-1][i];
		}
		
		return outerRing;
	}
	
	
	public Spring[] getConnectedSprings(Particle particle){
		
		Spring[] springSet = new Spring[0];
		
		//xSprings
		for(int i=0;i<this.uSprings.length;i++){
			for(int j=0;j<this.uSprings[i].length;j++){
				if (this.uSprings[i][j].getOneEnd() == particle || this.uSprings[i][j].getTheOtherEnd() == particle){
					
					springSet = (Spring[]) p.append(springSet,this.uSprings[i][j]);
				}
				
			}
		}
		
		//ySprings
		for(int i=0;i<this.vSprings.length;i++){
			for(int j=0;j<this.vSprings[i].length;j++){
				if (this.vSprings[i][j].getOneEnd() == particle || this.vSprings[i][j].getTheOtherEnd() == particle){
					
					springSet = (Spring[]) p.append(springSet,this.vSprings[i][j]);
				}
			}
		}
		
		return springSet;
		
	}
	
	public void setSpring(Particle oldA, Particle oldB, Particle newA, Particle newB){
		for(int i=0;i<uSprings.length;i++){
			for(int j=0;j<uSprings[i].length;j++){
				if(uSprings[i][j] == getSpring(oldA,oldB)){
					uSprings[i][j] = getSpring(newA,newB);
				}
			}
		}
		for(int i=0;i<vSprings.length;i++){
			for(int j=0;j<vSprings[i].length;j++){
				if(vSprings[i][j] == getSpring(oldA,oldB)){
					vSprings[i][j] = getSpring(newA,newB);
				}
			}
		}
	}
	
	Spring getSpring(Particle a, Particle b){
		
		for(int i=0;i<x.numberOfSprings();i++){
			if(x.getSpring(i).getOneEnd() == a && x.getSpring(i).getTheOtherEnd() == b || 
			   x.getSpring(i).getOneEnd() == b && x.getSpring(i).getTheOtherEnd() == a){
				return x.getSpring(i);
			}
		}
		return null;
	}
	
	public Particle[] getConnectedParticles(Particle particle, Spring[] springs){
		
		Particle[] connectedParticles = new Particle[0];
		
		for(int i=0;i<springs.length;i++){
			if(springs[i].getOneEnd() == particle){
				connectedParticles =(Particle[]) p.append(connectedParticles, springs[i].getTheOtherEnd());
			}else if(springs[i].getTheOtherEnd() == particle){
				connectedParticles =(Particle[]) p.append(connectedParticles, springs[i].getOneEnd());
			}
		}
		
		return connectedParticles;
		
	}

}
