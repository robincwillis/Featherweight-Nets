package run;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import parts.*;
import processing.core.PApplet;
import traer.physics.*;
import elements.*;
import peasy.*;
import org.apache.commons.lang.ArrayUtils;

import com.sun.tools.javac.code.Attribute.Array;

public class Interaction {

	Tent t;
	
	public Interaction(Tent _tent){
		t = _tent;
	}
	
	public void mousePressed() {
		t.println("pressed");
		
		if(t.mouseButton == t.LEFT){
		
		switch(t.MODE){
		//PARTICLE MODE
		case 1:
			dragObject();
		break;
		//PLACE MAST MODE
		case 2:
			t.println("mode2 "+t.MODE);
			placeMast();
		break;
		//PLACE CABLE MODE
		case 3:
			t.println("mode3");
			placeCable();	
		break;
		//PLACE RNET MODE	
		case 4:
		t.println("mode4");
			placeRNet();
		break;
		}
		}
	}

	public void mouseDragged() {
		 t.focusParticle = t.selectParticle;

		//----------------------------------------------------------------------
		//move Particle
		  if (t.selectParticle!=null) {
			 if(t.keyPressed == true && t.key == 'w' || t.key == 's'){
				 t.setFixParticle(t.selectParticle);	
			 }else if(t.keyPressed == false){
				 t.setFixParticle(t.selectParticle);	 
			 }
			  	
		  }

		//----------------------------------------------------------------------
		//move Radial Net
		
		  //This is broken, need to add a centroid to regular net
		  //also need to specify if there is a radial net or regular net present
		   if(t.selectParticle != null && t.curRNet != null  && t.keyPressed == true){
			 
			    //TO DO ADD CONSTRAINTS
			   if (t.key == 't' || t.key == 't') {
				   t.curRNet.move(t.selectParticle, t.mousePlanePos[0],t.mousePlanePos[1],t.mousePlanePos[2]); 
					
				    }

			   if (t.key == 'y' || t.key == 'Y') {
				   t.curRNet.move(t.selectParticle, t.mousePlanePos[0],t.mousePlanePos[1],t.selectParticle.position().z()); 
					
				    }

			   if (t.key == 'u' || t.key == 'U') {
				   t.curRNet.move(t.selectParticle,t.selectParticle.position().x(),t.selectParticle.position().y(),t.mousePlanePos[2]); 
					
				    }
			  // t.curRNet.move(t.selectParticle, t.mousePlanePos[0],t.mousePlanePos[1],t.mousePlanePos[2]); 
		  }
		
		 //----------------------------------------------------------------------
			//move Net
		   if(t.selectParticle != null && t.curNet != null  && t.keyPressed == true){
				 
			    //TO DO ADD CONSTRAINTS
			   if (t.key == 't' || t.key == 't') {
				   t.curNet.move(t.selectParticle, t.mousePlanePos[0],t.mousePlanePos[1],t.mousePlanePos[2]); 
					
				    }

			   if (t.key == 'y' || t.key == 'Y') {
				   t.curNet.move(t.selectParticle, t.mousePlanePos[0],t.mousePlanePos[1],t.selectParticle.position().z()); 
					
				    }

			   if (t.key == 'u' || t.key == 'U') {
				   t.curNet.move(t.selectParticle,t.selectParticle.position().x(),t.selectParticle.position().y(),t.mousePlanePos[2]); 
					
				    }

			  // t.curNet.move(t.selectParticle, t.mousePlanePos[0],t.mousePlanePos[1],t.mousePlanePos[2]); 
		  }  
		  
		   
		  if(t.dragging){
			  t.cam.setMouseControlled(false);
		  }
		
	}
	
	public void mouseReleased(){
		t.println("released");
		
		//JOIN PARTICLE
		if(t.MODE == 1){
		if(t.selectParticle != null && t.focusParticle != null){
			
			
			//RADIAL NET NEW //TODO ADD KEYPRESSED
			
			if(t.selectRNet != null && t.focusRNet != null && t.selectRNet != t.focusRNet){
				joinRParticles();
			}
			
			//NET //TODO ADD KEYPRESSED
			
			if(t.selectNet != null && t.focusNet !=null &&t.selectNet != t.focusNet){
				
				joinParticles();
			
		}
		}
		
		//SET PARTICLE
		
		if(t.selectParticle != null && t.focusPoint != null ){
			if(t.curRNet != null && t.selectParticle == t.curRNet.baseCentroid){
				t.curRNet.move(t.selectParticle, t.focusPoint.a.x, t.focusPoint.a.y , t.focusPoint.a.z);
			}

			t.selectParticle.position().set(t.focusPoint.a.x, t.focusPoint.a.y , t.focusPoint.a.z );
			
		}
		
		//RESET
		
		t.dragging = false;
		t.selectParticle = null;
		t.targetParticle = null;
	}
		t.cam.setMouseControlled(true);
	}
	//MOVE OBJECTS
	
	void dragObject(){
		
		t.println("mode1 "+t.MODE);
		
		//MOVE PARTICLE
		
		if(t.focusParticle != null){
			if(t.keyPressed == true && t.key == 'w' || t.key == 's' ){
				t.selectParticle = t.focusParticle;
				t.selectNet = t.focusNet;
				t.selectRNet = t.focusRNet;
				if (t.selectParticle.isFixed()){
					t.selectParticle.makeFree();
				}
			       
				  t.dragging = true;	
			}else if(t.keyPressed  == false){
				t.selectParticle = t.focusParticle;
				t.selectNet = t.focusNet;
				t.selectRNet = t.focusRNet;
				if (t.selectParticle.isFixed()){
					t.selectParticle.makeFree();
				}
			       
				  t.dragging = true;	
			}

			  
		}
		
		//MOVE RADIAL NET
		 if(t.focusParticle != null && t.focusParticle.isFixed() && t.focusRNet != null && t.keyPressed == true){
			  t.selectParticle = t.focusParticle;
			  
			  t.dragging = true;
		  }
		 
		 //MOVE NET
		 if(t.focusParticle != null && t.focusParticle.isFixed() && t.focusNet != null && t.keyPressed == true){
			  t.selectParticle = t.focusParticle;
			  
			  t.dragging = true;
		  }
	}
	
	//JOIN PARTICLE
	
	void joinParticles(){
	
		Particle newParticle = t.physics.makeParticle(t.selectParticle.mass(),t.mousePlanePos[0],t.mousePlanePos[1],t.mousePlanePos[2]);
		
		ArrayList<Net> focusNets = new ArrayList<Net>();
		//Make list For Nets
		for(int i=0;i<t.nets.size();i++){
			for(int j=0;j<t.nets.get(i).particles.length;j++){
				for(int k=0;k<t.nets.get(i).particles[j].length;k++){
					if(t.nets.get(i).particles[j][k] == t.focusParticle){
						focusNets.add(t.nets.get(i));
					}
				}
			}
		}
		
		
		
		Spring[] focusSprings = new Spring[0];
		Spring[] selectSprings = t.selectNet.getConnectedSprings(t.selectParticle);
		
		
		for(int i=0;i<focusNets.size();i++){
		
		    Spring[] focusSpringSet = focusNets.get(i).getConnectedSprings(t.focusParticle);
			focusSprings = t.concat(focusSprings, focusSpringSet);
		}
		
		Spring[] totalSprings = t.concat(selectSprings,focusSprings);
		
		Particle[] selConPart = t.selectNet.getConnectedParticles(t.selectParticle, selectSprings);
		Particle[] focConPart = t.focusNet.getConnectedParticles(t.focusParticle, focusSprings);
		
		Particle[] cleanConPart = new Particle[0];
		
		t.println(selConPart.length);
		
		for(int i=0;i<selConPart.length;i++){
			boolean dup = false;
			
			for(int j=0;j<focConPart.length;j++){
				
				if(selConPart[i].equals(focConPart[j])){
					dup = true;
					t.println("DUUUP");
				}
			}
			if(!dup){
				cleanConPart = (Particle[]) t.append(cleanConPart,selConPart[i]);
			}
			
		}
			
			
		Particle[] totalPart = t.concat(cleanConPart,focConPart);
		
		
		t.println(totalPart.length);
		for(int i=0;i<t.selectNet.particles.length;i++){
		
			for(int j=0;j<t.selectNet.particles[i].length;j++ ){
				
				if(t.selectNet.particles[i][j].equals(t.selectParticle)){
				
				
				t.selectNet.particles[i][j] = newParticle;
				
				
				
				for(int k=0;k<totalPart.length;k++){
					
					//create the spring, but dont make it twice if two objects share a particle
					Spring newSpring = t.physics.makeSpring(newParticle, totalPart[k],t.selectNet.SPRING_STRENGTH, t.selectNet.SPRING_DAMPING, t.selectNet.STEP_SIZE);					 
					
					//add spring to the object
					t.selectNet.setSpring(t.selectParticle, totalPart[k], newParticle, totalPart[k]);
					t.println(k);
				}
				}				
			}			
		}
		
		for (int h=0;h<focusNets.size();h++){
		
		for(int i=0;i<focusNets.get(h).particles.length;i++){
			
			for(int j=0;j<focusNets.get(h).particles[i].length;j++ ){
				
				
				if(focusNets.get(h).particles[i][j].equals(t.focusParticle)){
					
					focusNets.get(h).particles[i][j] = newParticle;
					for(int k=0;k<totalPart.length;k++){
						
						focusNets.get(h).setSpring(t.focusParticle, totalPart[k], newParticle, totalPart[k]);
					
					}
				}
			}
		
		}
		}
		for(int i=0;i<totalSprings.length;i++){
			t.physics.removeSpring(totalSprings[i]);
		}
		t.physics.removeParticle(t.selectParticle);
		t.physics.removeParticle(t.focusParticle);
	
	}
	
	void joinRParticles(){
		Particle newParticle = t.physics.makeParticle(t.selectParticle.mass(),t.mousePlanePos[0],t.mousePlanePos[1],t.mousePlanePos[2]);
		
		ArrayList<RadialNet> focusNets = new ArrayList<RadialNet>();
		//Make list For Nets
		for(int i=0;i<t.radialNets.size();i++){
			for(int j=0;j<t.radialNets.get(i).particles.length;j++){
				for(int k=0;k<t.radialNets.get(i).particles[j].length;k++){
					if(t.radialNets.get(i).particles[j][k] == t.focusParticle){
						focusNets.add(t.radialNets.get(i));
					}
				}
			}
		}
		t.println("focus nets: "+focusNets.size());
		
		Spring[] focusSprings = new Spring[0];
		Spring[] selectSprings = t.selectRNet.getConnectedSprings(t.selectParticle);
		
		
		for(int i=0;i<focusNets.size();i++){
		
		    Spring[] focusSpringSet = focusNets.get(i).getConnectedSprings(t.focusParticle);
			focusSprings = t.concat(focusSprings, focusSpringSet);
		}
		
		Spring[] totalSprings = t.concat(selectSprings,focusSprings);
		
		Particle[] selConPart = t.selectRNet.getConnectedParticles(t.selectParticle, selectSprings);
		//******
		Particle[] focConPart = t.focusRNet.getConnectedParticles(t.focusParticle, focusSprings);
		//******
		Particle[] cleanConPart = new Particle[0];
		
		t.println(selConPart.length);
		
		for(int i=0;i<selConPart.length;i++){
			boolean dup = false;
			
			for(int j=0;j<focConPart.length;j++){
				
				if(selConPart[i].equals(focConPart[j])){
					dup = true;
					t.println("DUUUP");
				}
			}
			if(!dup){
				cleanConPart = (Particle[]) t.append(cleanConPart,selConPart[i]);
			}
			
		}
			
			
		Particle[] totalPart = t.concat(cleanConPart,focConPart);
		
		
		
		t.println(totalPart.length);
		
		//SET NEW SELECT NET SPRINGS
		//------------------------------------------------------
		
		for(int i=0;i<t.selectRNet.particles.length;i++){
		
			for(int j=0;j<t.selectRNet.particles[i].length;j++ ){
				
				if(t.selectRNet.particles[i][j].equals(t.selectParticle)){
				
				
				t.selectRNet.particles[i][j] = newParticle;
				
				
				
				for(int k=0;k<totalPart.length;k++){
					
					//create the spring, but dont make it twice if two objects share a particle
					Spring newSpring = t.physics.makeSpring(newParticle, totalPart[k],t.selectRNet.SPRING_STRENGTH, t.selectRNet.SPRING_DAMPING, t.selectRNet.STEP_SIZE);					 
					
					//add spring to the object
					t.selectRNet.setSpring(t.selectParticle, totalPart[k], newParticle, totalPart[k]);
					t.println(k);
				}
				}				
			}			
		}
		//SET NEW FOCUS NET SPRINGS
		//------------------------------------------------------
		for(int h=0;h<focusNets.size();h++){
		for(int i=0;i<focusNets.get(h).particles.length;i++){
			
			for(int j=0;j<focusNets.get(h).particles[i].length;j++ ){
				
				
				if(focusNets.get(h).particles[i][j].equals(t.focusParticle)){
					
					focusNets.get(h).particles[i][j] = newParticle;
					for(int k=0;k<totalPart.length;k++){
						focusNets.get(h).setSpring(t.focusParticle, totalPart[k], newParticle, totalPart[k]);
					}
				}
			}
		
		}
		}
		
		for(int i=0;i<totalSprings.length;i++){
			t.physics.removeSpring(totalSprings[i]);
		}
		t.physics.removeParticle(t.selectParticle);
		t.physics.removeParticle(t.focusParticle);
	
	}
	
	//PLACE OBJECTS
	
	public void placeRNet(){
		boolean rNetNull = false;
		boolean focusPointNull = false;
		
		if(t.curRNet != null){
			rNetNull = true;
		}
		if(t.focusPoint != null){
			focusPointNull = true;
		}
		t.println("rNetNull: "+ rNetNull +" focusPointNull: "+focusPointNull+" netSet: "+t.curRNet.SET );
		
		if(t.curRNet != null && !t.curRNet.SET && t.focusPoint != null){
			
			//if(){
				t.curRNet.pos = t.focusPoint;
				
			//}else{
			//	Point placePos = new Point(t.mousePos[0],t.mousePos[1],t.mousePos[2]);
			//	t.curRNet.pos = placePos;
			//}
	
			t.curRNet.buildNet();
			t.curRNet.fixEdge();
			t.curRNet.SET = true;
			t.MODE =1;
		}
		
	}
	
	public void placeMast(){
		if(t.focusPoint!= null && t.focusParticle == null){
			
			if(!t.curMast.aSet && !t.curMast.bSet){			
				t.curMast.setAPos(t.focusPoint);				
			}else if(t.curMast.aSet && !t.curMast.bSet){			
				t.curMast.setBPos(t.focusPoint);		
			}else if(!t.curMast.aSet && t.curMast.bSet){	
				t.curMast.setAPos(t.focusPoint);
			}else if(t.curMast.aSet && t.curMast.bSet){
				t.MODE = 1;					
			}
		}
					
		if(t.focusPoint == null && t.focusParticle != null ){
			
			if(!t.curMast.aSet && !t.curMast.bSet){
				t.curMast.setAPos(t.focusParticle);
			}else if(!t.curMast.aSet && t.curMast.bSet){
				t.curMast.setAPos(t.focusParticle);
			}else if(t.curMast.aSet && !t.curMast.bSet){
				t.curMast.setBPos(t.focusParticle);
			}else if(t.curMast.aSet && t.curMast.bSet){
				t.MODE = 1;
			}
		}
		
		if(t.focusPoint == null && t.focusParticle == null && t.curMast != null){
			if(t.curMast.aSet && !t.curMast.bSet){
				
				//t.curMast.setBPos(t.mousePos[0], t.mousePos[1], t.mousePos[2]);
				//This doesnt work, maybe should use anchors
				t.setFixParticle(t.curMast.bPoint);
			
			}
		}
	}
	
	public void placeCable(){
		if(t.focusParticle == null && t.focusPoint != null){
			t.selectPoint = t.focusPoint;
			//TEMP FIX FOR SCALED POINT VALUES
			t.selectPoint.a.x = t.selectPoint.a.x*6;
			t.selectPoint.a.y = t.selectPoint.a.y*6;
			t.selectPoint.a.z = t.selectPoint.a.z*6;
			
			if(!t.curCable.aSet && !t.curCable.bSet){
				t.curCable.setAPos(t.selectPoint);
				
			}
			else if(t.curCable.aSet && !t.curCable.bSet){
				t.curCable.setBPos(t.selectPoint);
			}
			else if(t.curCable.aSet && t.curCable.bSet){
				t.MODE = 1;
			}
		}
		
		
		if(t.focusParticle != null && t.focusPoint == null){
			
			t.selectParticle = t.focusParticle;

			if(!t.curCable.aSet && !t.curCable.bSet){
				t.curCable.setAPos(t.selectParticle);
			}

			else if(t.curCable.aSet && !t.curCable.bSet){
				t.curCable.setBPos(t.selectParticle);

			}else if(t.curCable.aSet && t.curCable.bSet){
				t.MODE = 1;
			}

			//if both points are set
//			if(focusParticle == curCable.particles[0]){
//				curCable.aPoint.makeFree();
//			}
//			if(focusParticle == curCable.particles[curCable.CHAIN_COUNT-1]){
//				curCable.bPoint.makeFree();
//			}
		}
	}

	//FOR WELDING
	
	public static Particle[] removeElements(Particle[] input, Particle deleteMe) {
	    List result = new LinkedList();

	    for(Particle item : input)
	        if(!deleteMe.equals(item))
	            result.add(item);

	    return (Particle[]) result.toArray(input);
	}
	
	//KEY PRESSED
	public void keyPressed(){
		 if (t.key == 'p' || t.key == 'P') {
		      t.reset();
		    }
		 if(t.key =='W' || t.key == 'w'){
			 t.constrainVertical = true;
		 }
		 if(t.key =='S' || t.key == 's'){
			 t.constrainHorizontal = true;
		 }
		 if(t.key =='Q' || t.key == 'q'){
			 t.exportOBJ();
		 }
		 if(t.key =='A' || t.key == 'a'){
			 t.exportDXF();
		 }
		 if(t.key =='1'){
			 t.cam.reset();
		 }
		 if(t.key =='2'){
			
			 t.cam.rotateX((double)t.radians(90));
			// t.cam.rotateY((double)90);
		 }
		 if(t.key =='3'){
			// t.cam.reset();
			 t.cam.rotateX((double)t.radians(90));
			 t.cam.rotateY((double)t.radians(90));			 
		 }
		 if(t.key =='4'){
			// t.cam.reset();
			 t.cam.rotateX((double)t.radians(90));
			 t.cam.rotateY((double)t.radians(-90));
		 }
		 
		 if(t.key=='n'){
			 t.addNet();
		 }
		 
		 //TODO IMPORTING CASES
		 
		 //EXPORTING CASES
		 
//		 case 'd': 
//		    	if(!rFill){
//		    		rFill=true;
//		    	}
//		    	cadSoftware = "ObjFile"; 
//		        ext = "obj";
//		    	record=true;
//		    break;
//		    case 'f': 
//		    	if(!rFill){
//		    		rFill=true;
//		    	}
//		        cadSoftware = "Rhino"; 
//		        ext = "rvb"; 
//		    	record=true;
//		    break;
//		    case 'm': 
//		    	if(!rFill){
//		    		rFill=true;
//		    	}
//		    	cadSoftware = "Maya"; 
//		        ext = "mel";
//		        record=true;
//		        break; 
//		    case 'p': 
//		        makePDF= true;
//		        record=true;
//		        break; 
//		       
//		   default:
//		    break;
		 
	}
	
	public void keyReleased(){
		t.constrainVertical = false;
		t.constrainHorizontal = false;
	}
	
}
