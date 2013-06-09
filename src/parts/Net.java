package parts;

import elements.Vector3D;
import traer.physics.*;
import processing.core.PApplet;

public class Net {

	PApplet p;
	ParticleSystem x;
	public Particle[][] particles;
	public Spring[][] xSprings;
	public Spring[][] ySprings;
	float MASS = (float)0.15;
	
	public float SPRING_STRENGTH = (float)0.15;
	public float SPRING_DAMPING =  (float)0.1;
	public float STEP_SIZE =6;
	//DTC X 21
	//DTC Y 11
	int GRID_XCOUNT =120;
	int GRID_YCOUNT =60;
	
	public Net(PApplet parent, ParticleSystem pSystem){
		
		p = parent;
		x = pSystem;

		particles = new Particle[GRID_XCOUNT][GRID_YCOUNT];
		xSprings = new Spring[GRID_XCOUNT][GRID_YCOUNT-1];
		ySprings = new Spring[GRID_YCOUNT][GRID_XCOUNT-1];
		
		buildNet();
		fixCorners();

	}
	
	public Net(PApplet parent, ParticleSystem pSystem, float size, int xCount, int yCount){
		
		p = parent;
		x = pSystem;
		STEP_SIZE = size;
		GRID_XCOUNT = xCount;
		GRID_YCOUNT = yCount;
		particles = new Particle[GRID_XCOUNT][GRID_YCOUNT];
		xSprings = new Spring[GRID_XCOUNT][GRID_YCOUNT-1];
		ySprings = new Spring[GRID_YCOUNT][GRID_XCOUNT-1];
		buildNet();
		fixCorners();
	}
	
	//BUILD
	
	void buildNet(){
		
		  for (int i=0;i<GRID_XCOUNT;i++){
			     for(int j=0;j<GRID_YCOUNT;j++){
			       
			       particles[i][j] = x.makeParticle((float)MASS,(float) j*STEP_SIZE*2 ,(float) i*STEP_SIZE*2,(float)0.0);     
			    // if(j>0){
			    //   x.makeSpring(particles[i][j-1],particles[i][j], SPRING_STRENGTH, SPRING_DAMPING, STEP_SIZE).setRestLength(STEP_SIZE);
			    //  }  
			       if(i== 0 || j == 0){
			    	   
			    	  // particles[i][j].position().setX(X)
			    	   particles[i][j].makeFixed();
			       }
			       if( i== GRID_XCOUNT-1 || j == GRID_YCOUNT-1){
				    	  
			    	   particles[i][j].makeFixed();
			       }
			   }
		  }
		  
		  for (int i = 0;i<GRID_XCOUNT;i++){
			  
			    for (int j=1;j<GRID_YCOUNT;j++){

			    	xSprings[i][j-1] = x.makeSpring(particles[i][j-1],particles[i][j],SPRING_STRENGTH, SPRING_DAMPING, STEP_SIZE);   
			    
			    }

			  }
		  
		  for (int i = 0;i<GRID_YCOUNT;i++){
			  
			    for (int j=1;j<GRID_XCOUNT;j++){ 
			    	
			    	ySprings[i][j-1] = x.makeSpring(particles[j-1][i],particles[j][i], SPRING_STRENGTH, SPRING_DAMPING, STEP_SIZE);
			    }
		  
		  }
		
		  
		}
		  
	void fixCorners(){
		particles[0][0].makeFixed();
		particles[0][GRID_YCOUNT-1].makeFixed();
		particles[GRID_XCOUNT-1][0].makeFixed();
		particles[GRID_XCOUNT-1][GRID_YCOUNT-1].makeFixed();
	
		//particles[0][0].position().set(-200,-200, 0);
		//particles[0][GRID_YCOUNT-1].position().set(-200,200, 0);
		//particles[GRID_XCOUNT-1][0].position().set(200,-200, 0);
		//particles[GRID_XCOUNT-1][GRID_YCOUNT-1].position().set(200,200, 0);
	
	
	}
	
	public void testCorners(){
		particles[0][0].position().setZ(p.mouseY);
		particles[GRID_XCOUNT-1][GRID_YCOUNT-1].position().setZ(p.mouseY);
	}
	
	//DRAW
	
	public void drawSprings(){
		p.pushStyle();
		 p.stroke(255,255,0,100);
		  p.strokeWeight((float) .75);
		

		  for(int i=0; i< xSprings.length;i++){
			  for(int j=0; j<xSprings[i].length;j++){
				    Spring e = xSprings[i][j];
				    Particle a = e.getOneEnd();
				    Particle b = e.getTheOtherEnd();
				    p.line(a.position().x(),a.position().y(),a.position().z(),b.position().x(),b.position().y(),b.position().z());
			  }
		  }
		  p.stroke(255,0,255,100);
		  for(int i=0; i< ySprings.length;i++){
			  for(int j=0; j<ySprings[i].length;j++){
				    Spring e = ySprings[i][j];
				    Particle a = e.getOneEnd();
				    Particle b = e.getTheOtherEnd();
				    p.line(a.position().x(),a.position().y(),a.position().z(),b.position().x(),b.position().y(),b.position().z());
			  }
		  }
		 
		 p.popStyle();		 
	}
	
	public void drawCloth(){
		p.pushStyle();
		p.stroke(1);
		p.noFill();
		
		for (int i = 0; i < GRID_XCOUNT; i++)
		  {
		    p.beginShape();
		    p.curveVertex(particles[i][0].position().x(), particles[i][0].position().y(),particles[i][0].position().z() );
		   
		    for (int j = 0; j < GRID_YCOUNT; j++)
		    {
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }
		    p.curveVertex(particles[i][GRID_YCOUNT - 1].position().x(), particles[i][GRID_YCOUNT - 1].position().y(), particles[i][GRID_YCOUNT - 1].position().z());
		    p.endShape();
		  }
		  
		  
		  for (int j = 0; j < GRID_YCOUNT; j++)
		  {
		    p.beginShape();
		    p.curveVertex(particles[0][j].position().x(), particles[0][j].position().y(),particles[0][j].position().z());
		    for (int i = 0; i < GRID_XCOUNT; i++)
		    {
		      //ellipse(particles[i][j].position().x(), particles[i][j].position().y(), 5, 5);
		      p.curveVertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
		    }
		    p.curveVertex(particles[GRID_XCOUNT - 1][j].position().x(), particles[GRID_XCOUNT - 1][j].position().y(),particles[GRID_XCOUNT - 1][j].position().z());
		    p.endShape();
		  }
		
		 p.popStyle();
	}
	
	public void draw(){
		
		drawCloth();
		//drawPoints();

	}
	
	public void drawMesh(){
		
		p.pushStyle();
		p.fill(220,50);
		//p.stroke(0);
		//p.strokeWeight(1);
		p.beginShape(p.TRIANGLES);
		
		for(int i=0;i<GRID_XCOUNT-1;i++){
			for(int j=0;j<GRID_YCOUNT-1;j++){
				
				//if(j<GRID_YCOUNT-1){
				//first half
				 p.vertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
				 p.vertex(particles[i][j+1].position().x(), particles[i][j+1].position().y(),particles[i][j+1].position().z());
				 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
				
				 p.vertex(particles[i+1][j+1].position().x(), particles[i+1][j+1].position().y(),particles[i+1][j+1].position().z());
				 p.vertex(particles[i][j+1].position().x(), particles[i][j+1].position().y(),particles[i][j+1].position().z());
				 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
			//	}
				 //Close object
				 
//				else if( j == RADIALS-1 ){
//					 p.vertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
//					 p.vertex(particles[i][0].position().x(), particles[i][0].position().y(),particles[i][0].position().z());
//					 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
//					 
//					 p.vertex(particles[i+1][0].position().x(), particles[i+1][0].position().y(),particles[i+1][0].position().z());
//					 p.vertex(particles[i][0].position().x(), particles[i][0].position().y(),particles[i][0].position().z());
//					 p.vertex(particles[i+1][j].position().x(), particles[i+1][j].position().y(),particles[i+1][j].position().z());
//	
//				 }
				 
				 // p.vertex(particles[i][j].position().x(), particles[i][j].position().y(),particles[i][j].position().z());
					
			}
			
		}
		
		p.endShape();
		
		p.popStyle();
	}
	
	//UPDATE
	
	public void move(Particle selectParticle, float x, float y, float z){
		float sX = selectParticle.position().x();
		float sY = selectParticle.position().y();
		float sZ = selectParticle.position().z();
		Vector3D sVec = new Vector3D(sX,sY,sZ);
		
		for(int i=0; i<GRID_XCOUNT; i++){
			for(int j=0;j<GRID_YCOUNT;j++){
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
	}
	
	//ELEMENTS
	
	public Spring[] getConnectedSprings(Particle particle){
		
		Spring[] springSet = new Spring[0];
		
		//xSprings
		for(int i=0;i<this.xSprings.length;i++){
			for(int j=0;j<this.xSprings[i].length;j++){
				if (this.xSprings[i][j].getOneEnd() == particle || this.xSprings[i][j].getTheOtherEnd() == particle){
					
					springSet = (Spring[]) p.append(springSet,this.xSprings[i][j]);
				}
				
			}
		}
		
		//ySprings
		for(int i=0;i<this.ySprings.length;i++){
			for(int j=0;j<this.ySprings[i].length;j++){
				if (this.ySprings[i][j].getOneEnd() == particle || this.ySprings[i][j].getTheOtherEnd() == particle){
					
					springSet = (Spring[]) p.append(springSet,this.ySprings[i][j]);
				}
			}
		}
		
		return springSet;
		
	}
	
	public void setSpring(Particle oldA, Particle oldB, Particle newA, Particle newB){
		for(int i=0;i<xSprings.length;i++){
			for(int j=0;j<xSprings[i].length;j++){
				if(xSprings[i][j] == getSpring(oldA,oldB)){
					xSprings[i][j] = getSpring(newA,newB);
				}
			}
		}
		for(int i=0;i<ySprings.length;i++){
			for(int j=0;j<ySprings[i].length;j++){
				if(ySprings[i][j] == getSpring(oldA,oldB)){
					ySprings[i][j] = getSpring(newA,newB);
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
	
	public void drawPoints(){
		for(int i=0;i<GRID_XCOUNT;i++){
			for(int j=0;j<GRID_YCOUNT;j++){
				p.pushMatrix();
				p.translate(particles[i][j].position().x(), particles[i][j].position().y(), particles[i][j].position().z());
				p.sphere(2);
				p.popMatrix();
			}
		}
		
	}

	public Particle getPoint(float xPos, float yPos, float zPos){
		
		float x;
		float y;
		float z;
		float greedy = 15;
		for(int i=0;i<GRID_XCOUNT;i++){
			for(int j=0;j<GRID_YCOUNT;j++){
				x = particles[i][j].position().x();
				y = particles[i][j].position().y();
				z = particles[i][j].position().z();
				
				if(xPos > x-greedy && xPos < x+greedy &&
				   yPos > y-greedy && yPos < y+greedy &&
				   zPos > z-greedy && zPos < z+greedy){
					p.pushMatrix();
					p.translate(particles[i][j].position().x(), particles[i][j].position().y(), particles[i][j].position().z());
					p.sphere(2);
					p.popMatrix();
					
					return particles[i][j];
				}
				
				
			}
		}
		
		return null;
		
		//return particles[x][y];
		
	}
	
	//SETTINGS
	
	public void setStrength(float ks){
		for(int i=0;i<xSprings.length;i++){
			for(int j=0;j<xSprings[i].length;j++){
				xSprings[i][j].setStrength(ks);
				ySprings[j][i].setStrength(ks);
			}
		}
	}
	
	public void setMass(float m){
		for(int i=0;i<particles.length;i++){
			for(int j=0;j<particles[i].length;j++){
				particles[i][j].setMass(m);
			}
		}
	}
	
	public void setDamping(float d){
		for(int i=0;i<xSprings.length;i++){
			for(int j=0;j<xSprings[i].length;j++){
				xSprings[i][j].setDamping(d);
			}
		}
		
		for(int i=0;i<ySprings.length;i++){
			for(int j=0;j<ySprings[i].length;j++){
				ySprings[i][j].setDamping(d);
			}
		}
		
	}
	
	
}
