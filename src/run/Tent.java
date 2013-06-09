package run;

import java.nio.*;
import processing.core.PApplet;
import processing.opengl.*;
import javax.media.opengl.*;
import peasy.*;
import io.*;
import elements.*;
import elements.Vector3D;
import controlP5.*;
import traer.physics.*;
import parts.*;
import javax.media.opengl.glu.*;
import java.util.*;
import superCAD.*;
import saito.objloader.*;

public class Tent extends PApplet {
	
	GL gl;
	GLU glu;
	
	public static Interaction interact;
	public static ParticleSystem physics;
	public static ControlP5 controlP5;
	public static PeasyCam cam;
	public static GUI gui;
	
	Stage stage;
	Scheme scheme;
	Net curNet, selectNet, focusNet;
	RadialNet curRNet, selectRNet, focusRNet;
	Cable curCable;
    Mast curMast;
    GeometryIO readWrite;
    OBJModel siteModel, SBO, DTC;
    OBJModel schemeModel;
    
    public Particle selectParticle, focusParticle, targetParticle, lastParticle;
	public Point selectPoint, focusPoint, controlPoint;
	
	public static ArrayList<Mast> masts;
	public static ArrayList<Net> nets;
	public static ArrayList<RadialNet> radialNets;
	public static ArrayList<Cable> cables;
	public static ArrayList<Point> points;
	
	int READ_MODE, MODE;
	public int NET_SIZE, X_COUNT, Y_COUNT;
	public int STEP_SIZE, RINGS, RADIALS, RNET_HEIGHT, TOP_RING;
	String cadSoftware, ext;
	
	boolean dragging, constrainVertical, constrainHorizontal;
	boolean camOn = true;
	boolean record = false;
	boolean readFile = false;
	public boolean meshOn, clothOn, springOn, focOn, siteOn,stageOn,schemeOn, drawU, drawV;
	Particle[][] oParticles;
	Particle oCentroid;

	float[] mousePos, mousePlanePos;
	
	float GRAVITY = (float)0;//-0.05;
	float DRAG = (float)0.01;
	
	public void setup() {
	  
		size(1024,728, OPENGL);

		frameRate(30);
		hint(DISABLE_OPENGL_2X_SMOOTH);
		hint(ENABLE_OPENGL_4X_SMOOTH);
		hint(ENABLE_OPENGL_ERROR_REPORT);
		
		sphereDetail(4);
		meshOn = true;
		focOn = true;
		springOn = true;
		schemeOn = false;
		stageOn = true;
		//curveTightness((float)1);
		
		gl=((PGraphicsOpenGL)g).gl;
		glu=((PGraphicsOpenGL)g).glu;

		readWrite = new GeometryIO(this);
		interact = new Interaction(this);
		physics = new ParticleSystem(0,0,GRAVITY,DRAG);
		cam = new PeasyCam(this, 1000);
		controlP5 = new ControlP5(this);
		gui = new GUI(this, this, controlP5);
		stage = new Stage(this);
		scheme = new Scheme(this);
		nets = new ArrayList<Net>();
		radialNets = new ArrayList<RadialNet>();
		cables = new ArrayList<Cable>();
		masts = new ArrayList<Mast>();
		
		DTC = readWrite.readObj("dtc.obj");
		SBO = readWrite.readObj("SBO_Site.obj");
		siteModel = SBO;
		cam.setMinimumDistance(10);
		cam.setMaximumDistance(10000);
		
		dragging = false;
		MODE = 1;
		X_COUNT = 30;
		Y_COUNT = 56;
		NET_SIZE = 10;
		//SBO
		//STEP_SIZE = 5;
		//RINGS = 8;
		//RADIALS = 16;
		//RNET_HEIGHT = 50;
		//TOP_RING = 10;
		
		//DTC
		STEP_SIZE = 8;
		RINGS = 12;
		RADIALS = 25;
		RNET_HEIGHT = 75;
		TOP_RING = 20;
		gui.initGUI();
		
		//Net laNet = new Net(this, physics, NET_SIZE, X_COUNT,Y_COUNT);
		//	nets.add(laNet);	
	}

	public void draw() {

		physics.tick();
		background(250);
		lights();
		//DRAWING
		
		if(record){
		   if(ext=="dxf"){
			   beginRaw(DXF, "output.dxf");

		   }else{
			   beginRaw("superCAD."+cadSoftware, "output."+ext);  
		   }
		   
		   }
		//hint(ENABLE_DEPTH_TEST);
		
		drawElements();
		drawFills();
		//hint(DISABLE_DEPTH_SORT);
		  if (record) {
			    endRaw();
			    record = false;
		 }
		  if(readFile){ //READING NEEDS TO BE ON THIS THREAD
			  readFile = false;
			  switch (READ_MODE){
			  case 1:
				  readLines();
				  break;
			  case 2:
				  readPoints();
				  break;
			  case 3:
				  readOBJ();
				  break;
			  }
		  }
		//INTERACTION
		  
		drawCursor();
		mousePos = getMouse3D();
		
		if(focusParticle !=null){
			mousePlanePos = getMousePerp();
		}
		if(stageOn){
			focusPoint = stage.getPoint(mouseX,mouseY);
		}
		if(schemeOn){
			focusPoint = scheme.getPoint(mouseX,mouseY);
		}
			focusParticle = getParticle(mouseX, mouseY);
		focusNet = getNet(focusParticle);
		
		focusRNet = getRNet(focusParticle);
		if(focusParticle != null && MODE==1){
		curNet = getNet(focusParticle);
		curRNet = getRNet(focusParticle);
		}//HUD
		cam.beginHUD();
		
		noLights();
		hint(DISABLE_DEPTH_TEST);
		translate(0,0,-284);
		gui.updateLabels();
		controlP5.draw();
		
		cam.endHUD(); // always!
		
	}
	
	//PARTICLE PUSHING
	Net getNet(Particle focusParticle){
		if(!nets.isEmpty()){
			for(int i=0;i<nets.size();i++){
				for(int j=0;j<nets.get(i).particles.length;j++){
					for(int k=0;k<nets.get(i).particles[j].length;k++){
						if(focusParticle == nets.get(i).particles[j][k]){
							return nets.get(i);
						}
					}
				}
			}
		}
		return null;
	}
	
	RadialNet getRNet(Particle focusParticle){
		if(!radialNets.isEmpty()){
			for(int i=0;i<radialNets.size();i++){
				for(int j=0;j<radialNets.get(i).particles.length;j++){
					for(int k=0;k<radialNets.get(i).particles[j].length;k++){
						if(focusParticle == radialNets.get(i).particles[j][k] || focusParticle == radialNets.get(i).baseCentroid){
							return radialNets.get(i);
						}
					}
				}
			}
		}
		return null;
	}
	
	public int getNetIndex(){
		int index = -1;
		if(!nets.isEmpty()){
			for(int i =0; i<nets.size();i++){
				if(nets.get(i).equals(curNet)){
					index = i;
				}
			}
		}
		
		return index;
	}
	
	public int getRNetIndex(){
		int index = -1;
		if(!radialNets.isEmpty()){
			for(int i =0; i<radialNets.size();i++){
				if(radialNets.get(i).equals(curRNet)){
					index = i;
				}
			}
		}
		
		return index;
	}
	
	Particle getParticle(float mX, float mY){
		float screenXpos;
		float screenYpos;
		float x;
		float y;
		float z;
		
	    for(int i=0;i<physics.numberOfParticles();i++){

				x = physics.getParticle(i).position().x();
				y = physics.getParticle(i).position().y();
				z = physics.getParticle(i).position().z();
				
				 screenXpos=screenX(x,y,z);
				 screenYpos=screenY(x,y,z);
				 
				    if (mX>screenXpos-7 && mX<screenXpos+7 && mY>screenYpos-7 && mY<screenYpos+7) {
				    	
				    	if(physics.getParticle(i) != selectParticle){
					    	pushMatrix();
							translate(physics.getParticle(i).position().x(), physics.getParticle(i).position().y(), physics.getParticle(i).position().z());
							pushStyle();
							fill(255,0,0,50);
							sphere(3);
							popStyle();
							popMatrix();
							return physics.getParticle(i);
				    	}
				      }
	    }
			
	    return null;
		
	}
	
	Spring getSpring(Particle a, Particle b){
	
		for(int i=0;i<physics.numberOfSprings();i++){
			if(physics.getSpring(i).getOneEnd() == a && physics.getSpring(i).getTheOtherEnd() == b || 
			   physics.getSpring(i).getOneEnd() == b && physics.getSpring(i).getTheOtherEnd() == a){
				return physics.getSpring(i);
			}
		}
		return null;
	}
	
	public void setFixParticle (Particle fP){

		    if(constrainVertical){
		    	fP.position().setZ(mousePlanePos[2]);
		    }else if (constrainHorizontal){
		    	fP.position().setX(mousePlanePos[0]);
		    	fP.position().setY(mousePlanePos[1]);
		    }else{
		    	
		    	fP.position().setX(mousePlanePos[0]);
		    	fP.position().setY(mousePlanePos[1]);
		    	fP.position().setZ(mousePlanePos[2]);
		    }
		    
		    fP.makeFixed();
	}

	Spring[] concat(Spring[] A, Spring[] B) {
		   Spring[] C= new Spring[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);

		   return C;
		}
	
	Particle[] concat(Particle[] A, Particle[] B) {
		   Particle[] C= new Particle[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);

		   return C;
		}
	
	public void removeParticles(Particle[][] particles){
		for(int i=0; i< particles.length;i++){
			for(int j=0;j<particles[i].length;j++){
				physics.removeParticle(particles[i][j]);
			}
			
		}
	}
	
	public void removeSprings(Spring[][] springs){
		for(int i=0; i< springs.length;i++){
			for(int j=0;j<springs[i].length;j++){
				physics.removeSpring(springs[i][j]);
			}
			
		}
	}
	
	//DRAWING
	void drawFills(){
		
		pushStyle();
		fill(100,50);
		if(siteOn){
			siteModel.draw();
		}
		
		popStyle();
		
		if(meshOn){
		if(!radialNets.isEmpty()){
			for(int i=0;i<radialNets.size();i++){
				if(radialNets.get(i).SET){
					radialNets.get(i).drawMesh();
				}
			}
		}
		
		if(!nets.isEmpty()){
			for(int i=0;i<nets.size();i++){
				nets.get(i).drawMesh();
			}
		}
		}
	}
	
	void drawElements(){
		if(schemeOn){
		scheme.draw();
		}
		if(stageOn){
		stage.drawStage();
		stage.drawPoints();
		}
		//stage.drawSelectedPoint(selectPoint);
		if(springOn){
			drawAllSprings();
		}
		
		if(!nets.isEmpty()){
			for(int i=0;i<nets.size();i++){
				if( nets.get(i).equals(curNet) && focOn){
					nets.get(i).drawSprings();
				}
				if(clothOn){
					nets.get(i).draw();
					
				}
			}
		}
		if(!radialNets.isEmpty()){
			for(int i=0;i<radialNets.size();i++){
			
			if(radialNets.get(i).SET){
				radialNets.get(i).draw();
				if( radialNets.get(i).equals(curRNet) && focOn){
					radialNets.get(i).drawSprings();
				}
				if(clothOn){
					radialNets.get(i).drawCloth();
				}
				
				if(drawU){
					radialNets.get(i).drawUCloth();
				}
				
				if(drawV){
					radialNets.get(i).drawVCloth();
				}
				
			}
				
			}
		}
		
		if(!masts.isEmpty()){
			for(int i=0;i<masts.size();i++){
				masts.get(i).draw();
			}
			if(curMast.aSet && !curMast.bSet){
				drawWaiting(curMast.aPoint);
				println("asdf");
			}
			
		}
		
		
		if(!cables.isEmpty()){
			for(int i=0;i<cables.size();i++){
				cables.get(i).draw();
	
			}
			curCable.drawPoints();
			if(curCable.aSet && !curCable.bSet){
				drawWaiting(curCable.aPoint);
				println("asdf");
			}
		}
		
		if(physics.numberOfParticles()>0){
			for(int i=0;i<physics.numberOfParticles();i++){
				if(physics.getParticle(i).isFixed()){
					pushMatrix();
					translate(physics.getParticle(i).position().x(),
								physics.getParticle(i).position().y(),
								physics.getParticle(i).position().z());
					
					sphere(1);
					popMatrix();
				}
			}
		}
	}
	
	void drawAllSprings(){
				pushStyle();
				 stroke(50,50);
				  strokeWeight((float) .75);
				
				  beginShape( LINES );
				  for ( int i = 0; i < physics.numberOfSprings(); ++i )
				  {
				    Spring e = physics.getSpring( i );
				    Particle a = e.getOneEnd();
				    Particle b = e.getTheOtherEnd();
				    vertex( a.position().x(), a.position().y(),a.position().z() );
				    vertex( b.position().x(), b.position().y(),b.position().z() );
				  }
				 endShape();
				 popStyle();		 
				 
		 
	}
	
	void drawWaiting(Particle point){
		pushStyle();
		stroke(100);
		strokeWeight(1);
		line(
		point.position().x(),point.position().y(),point.position().z(),
		mousePos[0],mousePos[1],mousePos[2]
		);
		popStyle();
	}
	
	void drawCursor(){
		
	    float offset = 10;
	    
	    if(MODE == 4){
	    	pushStyle();
	    	stroke(255,0,0);
	    	strokeWeight(1);
	    	line(mousePos[0]-offset,mousePos[1],mousePos[2],
	    		 mousePos[0]+offset,mousePos[1],mousePos[2]);
	    	line(mousePos[0],mousePos[1]-offset,mousePos[2],
		    	 mousePos[0],mousePos[1]+offset,mousePos[2]);
	    	line(mousePos[0],mousePos[1],mousePos[2]-offset,
		    	 mousePos[0],mousePos[1],mousePos[2]+offset);
		    		
	    	popStyle();
	    }
	    
		if(dragging){

		    pushStyle();
		    stroke(3);
		    strokeWeight(1);
		    line(selectParticle.position().x()-offset,  selectParticle.position().y(),selectParticle.position().z(),
		    	 selectParticle.position().x()+offset,  selectParticle.position().y(),selectParticle.position().z()
		         );
		    
		    line(selectParticle.position().x(),  selectParticle.position().y()-offset,selectParticle.position().z(),
		    	 selectParticle.position().x(),  selectParticle.position().y()+offset,selectParticle.position().z()
			         );
		    
		    line(selectParticle.position().x(),  selectParticle.position().y(),selectParticle.position().z()-offset,
		    	 selectParticle.position().x(),  selectParticle.position().y(),selectParticle.position().z()+offset
			         );
		    
		    popStyle();
		    
			   if(focusParticle != null && focusNet != null && selectNet != null && focusNet != selectNet){
					Spring[] selectSprings = selectNet.getConnectedSprings(selectParticle);
					Spring[] focusSprings = focusNet.getConnectedSprings(focusParticle);
					Spring[] totalSprings = concat(selectSprings,focusSprings);
					Particle[] selConPart = selectNet.getConnectedParticles(selectParticle, selectSprings);
					Particle[] focConPart = focusNet.getConnectedParticles(focusParticle, focusSprings);
					Particle[] totalPart = concat(selConPart,focConPart);
					
					pushStyle();
					strokeWeight(2);
					stroke(100,100);
					for(int i=0;i<totalSprings.length;i++){
						float aX = totalSprings[i].getOneEnd().position().x();
						float aY = totalSprings[i].getOneEnd().position().y();
						float aZ = totalSprings[i].getOneEnd().position().z();
						float bX = totalSprings[i].getTheOtherEnd().position().x();
						float bY = totalSprings[i].getTheOtherEnd().position().y();
						float bZ = totalSprings[i].getTheOtherEnd().position().z();
						line(aX,aY,aZ,bX,bY,bZ);
					}
					noStroke();
					fill(100,100);
					for(int i=0;i<totalPart.length;i++){
						pushMatrix();
						translate(totalPart[i].position().x(), totalPart[i].position().y(), totalPart[i].position().z());
						sphere(4);
						popMatrix();
					}
					popStyle();
				   }
		    
			   if(focusParticle != null && focusRNet != null && selectNet != null && focusRNet != selectRNet){
					Spring[] selectSprings = selectRNet.getConnectedSprings(selectParticle);
					Spring[] focusSprings = focusRNet.getConnectedSprings(focusParticle);
					Spring[] totalSprings = concat(selectSprings,focusSprings);
					Particle[] selConPart = selectRNet.getConnectedParticles(selectParticle, selectSprings);
					Particle[] focConPart = focusRNet.getConnectedParticles(focusParticle, focusSprings);
					Particle[] totalPart = concat(selConPart,focConPart);
					
					pushStyle();
					strokeWeight(2);
					stroke(100,100);
					for(int i=0;i<totalSprings.length;i++){
						float aX = totalSprings[i].getOneEnd().position().x();
						float aY = totalSprings[i].getOneEnd().position().y();
						float aZ = totalSprings[i].getOneEnd().position().z();
						float bX = totalSprings[i].getTheOtherEnd().position().x();
						float bY = totalSprings[i].getTheOtherEnd().position().y();
						float bZ = totalSprings[i].getTheOtherEnd().position().z();
						line(aX,aY,aZ,bX,bY,bZ);
					}
					noStroke();
					fill(100,100);
					for(int i=0;i<totalPart.length;i++){
						pushMatrix();
						translate(totalPart[i].position().x(), totalPart[i].position().y(), totalPart[i].position().z());
						sphere(4);
						popMatrix();
					}
					popStyle();
				   }
			   
		}
	}
	
	public void setDegree(float d){
		curveTightness(d);
	}
	
	//ADD //NAVIGATE OBJECTS
	public void addNet(){
		MODE = 1;
		println(X_COUNT+" "+Y_COUNT);
		curNet = new Net(this, physics, NET_SIZE, X_COUNT,Y_COUNT);
		nets.add(curNet);
		gui.r.activate(0);

	}
	
	public void addRadialNet(){
		if(MODE != 4){
		MODE = 4;
		//curRNet = new RadialNet(this,physics);
		curRNet = new RadialNet(this,physics, STEP_SIZE, RINGS, RADIALS, RNET_HEIGHT, TOP_RING);
		radialNets.add(curRNet);
		gui.r.activate(0);
		}
	}
	
	public void addMast(){
		//camOn = false;
	   MODE = 2;
	   println("mast Mode " + MODE);
	   curMast = new Mast(this,mousePos, physics);
	   masts.add(curMast);
	   gui.r.activate(1);
	}
	
	public void addCable(){
		MODE = 3;
		curCable = new Cable(this,physics);
		cables.add(curCable);
		gui.r.activate(2);
	}
	
	public void nextNet(){
		if(!nets.isEmpty()){
			int curNetIndex = getNetIndex();
			
			if(curNetIndex < nets.size()-1){
				curNet = nets.get(curNetIndex+1);
			}
			
		}
	}
	
	public void prevNet(){
		if(!nets.isEmpty()){
			int curNetIndex = getNetIndex();
			
			if(curNetIndex > 0){
				curNet = nets.get(curNetIndex-1);
			}
			
		}
	}
	
	public void nextRNet(){
		if(!radialNets.isEmpty()){
			int curRNetIndex = getRNetIndex();
			
			if(curRNetIndex < radialNets.size()-1){
				curRNet = radialNets.get(curRNetIndex+1);
				//if (!curRNet.SET){
				//	curRNet.SET = true;
				//}
				println("SET:"+ curRNet.SET);

			}
			
		}
		
	}
	
	public void prevRNet(){
		if(!radialNets.isEmpty()){
			int curRNetIndex = getRNetIndex();
			
			if(curRNetIndex > 0){
				curRNet = radialNets.get(curRNetIndex-1);
				println("SET:"+ curRNet.SET);
			}
			
		}
	}
	
	public void deleteNet(){
		if(curNet != null){
		
		removeParticles(curNet.particles);
		removeSprings(curNet.xSprings);
		removeSprings(curNet.ySprings);
			
		int netIndex = getNetIndex();
		nets.remove(netIndex);
		}
	}
	
	public void deleteRNet(){
		if(curRNet != null){
			physics.removeParticle(curRNet.centroid);
			physics.removeParticle(curRNet.baseCentroid);
			removeParticles(curRNet.particles);
			removeSprings(curRNet.uSprings);
			removeSprings(curRNet.vSprings);
			//removeSprings(curRNet.cSprings);
			int rNetIndex = getRNetIndex();
			radialNets.remove(rNetIndex);
			}
	}
		
	//PHYSICS PROPERTIES
	public void setGravity(float g){
		physics.setGravity(0, 0, g);
		//physics.setGravity(g);
	}
	
	public void setDrag(float d){
		physics.setDrag(d);
	}
	
	//NET PROPERTIES //DOESNT WORK FOR RNET
	public void setStrength(float ks){
		curNet.setStrength(ks);
	}
	
	public void setMass(float m){
		curNet.setMass(m);
	}
	
	public void setDamping(float d){
		curNet.setDamping(d);
	}

	public void movePoint(){
		
	}
	
	public void setXcount(float x){
		
			int iX = (int)x;
			X_COUNT = iX;
	}
	
	public void setYcount(float y){
		
		int iY = (int)y;
		Y_COUNT = iY;
	}
	
	public void setRadius(float r){
		if(curRNet != null){
			curRNet.TOP_RING = (int)r;
		}
	}
	
	public void setRings(float u){
		RINGS = (int)u;
	}
	
	public void setRadials(float v){
		RADIALS = (int)v;
	}
	
	public void setStepSize(float s){
		STEP_SIZE = (int)s;
	}
	
	public void setHeight(float h){
		RNET_HEIGHT = (int)h;
	}
	
	//MAST PROPERTIES
	void mastStrength(float ks){
		
		for(int i=0;i<masts.size();i++){
			masts.get(i).shaft.setStrength(ks);
			
	}
	}
	
	void mastLength(float l){
		for(int i=0;i<masts.size();i++){
			masts.get(i).shaft.setRestLength(l);
			
	}

	}
	
	public void reset(){
		physics.clear();
		nets.clear();
		radialNets.clear();
		masts.clear();
		cables.clear();
	}
	
	
	//IMPORT/EXPORT
	
	//IN
	
	public void importOBJ(){
		READ_MODE = 3;
		readFile = true;
	
	}
	
	public void readOBJ(){
		siteModel = readWrite.readObj();
	}

	public void importLines(){
		READ_MODE = 1;
		readFile = true;
		
	}
	
	public void importPoints(){
		READ_MODE = 2;
		readFile = true;
	}
	
	public void readLines(){
	Line[] lineLayer = readWrite.readLines();
	scheme.addLineLayer(lineLayer);
	}
	
	public void readPoints(){
		Point[] pointLayer = readWrite.readPoints();
		scheme.addPointLayer(pointLayer);
	}
	
	//OUT
	
	public void exportOBJ(){
	      //RhinoScript is bust in supercad, this is a bummer because I want lines
		  cadSoftware = "ObjFile"; 
	      ext = "obj";
	      record=true;
	}
	
	public void exportDXF(){
		ext = "dxf";
		record = true;
	}
	
	//WRAPPERS FOR INTERACTION CLASS
	public void mousePressed(){
		interact.mousePressed();
	}
	
	public void mouseDragged(){
		interact.mouseDragged();
	}
	
	public void mouseReleased(){
		interact.mouseReleased();
	}
	
	public void keyPressed(){
		interact.keyPressed();
	}
	
	public void keyReleased(){
		interact.keyReleased();
	}
	
	//INTERACTION
	public float[] getMousePerp(){
			 
		  float[] camPos = cam.getPosition();
		  float[] lookAt = cam.getLookAt();
		  float[] curVert = new float[3];
		  curVert[0] = focusParticle.position().x();
		  curVert[1] = focusParticle.position().y();
		  curVert[2] = focusParticle.position().z();
		  
		  float[] mouse = getMouse3D(); //return values from getMouse3D();
		  
		  //VECTOR FROM MOUSE POSITION TO CAMERA POSITION
		   Vector3D dirVec = new Vector3D(camPos[0]-mouse[0],camPos[1]-mouse[1],camPos[2]-mouse[2]);
		   
		  //VECTOR FROM CAMERA POSITION TO CAMEREA TARGET
		  Vector3D lookAtVec = new Vector3D(camPos[0]-lookAt[0],camPos[1]-lookAt[1],camPos[2]-lookAt[2]);
		 	
		  //VECTOR FROM CAMERA POSITION TO CURRENT VERTEX
		  Vector3D vertVec = new Vector3D(camPos[0]-curVert[0],camPos[1]-curVert[1],camPos[2]-curVert[2]);

		  //THE DISTANCE BETWEEN MOUSEPOS AND VERT
		  float vertDist = dist(camPos[0],camPos[1],camPos[2],curVert[0], curVert[1], curVert[2]);
		  
		  Vector3D dirVecUnit = dirVec.normalize();
		  Vector3D lookVecUnit = lookAtVec.normalize();

		  float aVL =cos((float) Vector3D.angle(vertVec, lookAtVec));

		  float normDist = aVL * vertDist;
		  
		  Vector3D normVec = lookVecUnit.scalarMultiply(normDist);
	  
		  float aVD = cos((float)Vector3D.angle(normVec, dirVecUnit));

		  float finDist = abs(normDist/aVD);
		 
		  float naX = (float) (camPos[0]-dirVecUnit.getX()*finDist);
		  float naY = (float) (camPos[1]-dirVecUnit.getY()*finDist);
		  float naZ = (float) (camPos[2]-dirVecUnit.getZ()*finDist);
			 
		  return new float[]{naX,naY,naZ};
		 }
		
	public float[] getMouse3D()
	{
	  ((PGraphicsOpenGL)g).beginGL();
	//have to get processing to dump all it's matricies into GL, so the functions work.

	  int viewport[] = new int[4];
	//For the viewport matrix... not sure what all the values are, I think the first two are width and height, and all Matricies in GL seem to be 4 or 16...

	  double[] proj=new double[16];
	//For the Projection Matrix, 4x4

	  double[] model=new double[16];
	//For the Modelview Matrix, 4x4

	  gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
	//fill the viewport matrix

	  gl.glGetDoublev(GL.GL_PROJECTION_MATRIX,proj,0);
	//projection matrix

	  gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX,model,0);
	//modelview matrix

	  FloatBuffer fb=ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	//set up a floatbuffer to get the depth buffer value of the mouse position

	  gl.glReadPixels(mouseX, height-mouseY, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, fb);
	//Get the depth buffer value at the mouse position. have to do height-mouseY, as GL puts 0,0 in the bottom left, not top left.

	  fb.rewind(); //finish setting up this.

	  double[] mousePosArr=new double[4];
	//the result x,y,z will be put in this.. 4th value will be 1, but I think it's "scale" in GL terms, but I think it'll always be 1.

	 glu.gluUnProject((double)mouseX,height-(double)mouseY,(double)fb.get(0),model,0,proj,0,viewport,0,mousePosArr,0);
	
	 //glu.gluUnProject((double)mouseX,height-(double)mouseY,(double)0.5, model,0,proj,0,viewport,0,mousePosArr,0); 
	 //the magic function. You put all the values in, and magically the x,y,z values come out :)

	  ((PGraphicsOpenGL)g).endGL();
	  
	  return new float[]{(float)mousePosArr[0],(float)mousePosArr[1],(float)mousePosArr[2]};
	//The values are all doubles, so throw them into floats to make life easier.
	} 

	public void controlEvent(ControlEvent theEvent) {
	//println(theEvent.controller().name());
	
	
	
	if(theEvent.isGroup() && theEvent.group().name().equals("MODE")){
//		print("got an event from "+theEvent.group().name()+"\t");
//		  for(int i=0;i<theEvent.group().arrayValue().length;i++) {
//			    print((theEvent.group().arrayValue()[i]));
//			  }
//		println("\t "+(int)theEvent.group().value());
		//This doesnt work - bug in controlp5
		//	MODE = (int)theEvent.group().value();
	}
	
	//println("control event");
	//
	cam.setMouseControlled(false);
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { run.Tent.class.getName() });
	}
}



