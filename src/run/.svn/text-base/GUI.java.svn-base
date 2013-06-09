package run;

import processing.core.PApplet;
import elements.*;
import controlP5.*;

public class GUI {
	
	PApplet p;
	Tent t;
	ControlP5 controlP5;
	Bang bang;
	Button button;
	Slider slider;
	Toggle toggle;
	public RadioButton r;
	Textlabel netList, mastList, cableList, springList, particleList;
	Textlabel title, title1, title2, title3;
	Textlabel fPart, sPart, fPoint;
	Textlabel curNet, curRNet;
	int padding = 15;
	int bPadding = 25;
	int right;
	
	public GUI(PApplet parent, Tent tent, ControlP5 gui){
		p = parent;
		t = tent;
		controlP5 = gui;
		right = p.width- padding*8;
	}
	
	public void initGUI(){
		int red = p.color(255,0,0);
		controlP5.setAutoDraw(false);
		controlP5.setColorLabel(0);
		//controlP5.setColorActive(red);
		controlP5.setColorBackground(100);

		mainControl();
		elementControl();
		ioControl();
		displayControl();
	}
	
	void mainControl(){
		title = controlP5.addTextlabel("label1","TOFU TENT",padding,padding/2);
		title.setColorValue(0);
		
		//GLOBAL / NET
		title1 = controlP5.addTextlabel("label2","GLOBAL",padding,padding*2);
		title1.setColorValue(0);
		
		slider = controlP5.addSlider("setGravity", (float)-0.5, (float)0.5,  padding, padding*2,100, 10);	
		slider = controlP5.addSlider("setDrag",     0, (float)0.1,     padding, padding*3,100, 10);	
		slider = controlP5.addSlider("setStrength", (float)0.05, (float).25, padding, padding*4,100, 10);	
		slider = controlP5.addSlider("setDamping",  0, (float)0.1,  padding, padding*5,100, 10);	
		slider = controlP5.addSlider("setMass",  (float)0.15, (float)0.2,  padding, padding*6,100, 10);	
			
		slider = controlP5.addSlider("mastLength", 0, 1000,100,           padding, padding*7,100, 10);	
		slider = controlP5.addSlider("mastStrength", 0, 1,(float)0.1,     padding, padding*8,100, 10);
		//bang = controlP5.addBang("REMOVE ELEMENT",    padding*12, padding*3, 100, 10);
		//slider = controlP5.addSlider("STRENGTH", 0, 1, padding*10, padding*4,100, 10);	
		//slider = controlP5.addSlider("DAMPING", 0, 1,  padding*10, padding*5,100, 10);	
		
		 r = controlP5.addRadioButton("MODE",padding,padding*9);
		 
		 addToRadioButton(r,"NET MODE",1);
		 addToRadioButton(r,"MAST MODE",2);
		 addToRadioButton(r,"CABLE MODE",3);
		
			fPart = controlP5.addTextlabel("fPart", "FOCUS PARTICLE: "+ t.focusParticle,padding, padding*12);	
			sPart = controlP5.addTextlabel("sPart", "SELECT PARTICLE: "+ t.selectParticle,padding, padding*13);
			fPoint = controlP5.addTextlabel("fPoint", "FOCUS POINT: "+ t.focusPoint,padding, padding*14);
			
			fPart.setColorValue(0);
			sPart.setColorValue(0);
			fPoint.setColorValue(0);
	}
	
	void elementControl(){
		
		//FEATURES
		title3 = controlP5.addTextlabel("label4","FEATURES",p.width-padding*8,padding*1);
		title3.setColorValue(0);
		
		//Particles
		//Springs
		netList = controlP5.addTextlabel("aList","Nets: "+  t.nets.size(),      p.width- padding*8, padding*3);
		mastList = controlP5.addTextlabel("bList","Masts: "+ t.masts.size(),    p.width- padding*8, padding*4);
		cableList = controlP5.addTextlabel("cList","Cables: "+ t.cables.size(), p.width- padding*8, padding*5);
		particleList = controlP5.addTextlabel("pList", "Particles: "+ t.physics.numberOfParticles(), p.width- padding*8, padding*6);
		springList = controlP5.addTextlabel("sList", "Springs: "+ t.physics.numberOfSprings(), p.width- padding*8, padding*7);
		
		//slider.alignValueLabel(controlP5.BOTTOM);		
		netList.setColorValue(0);
		mastList.setColorValue(0);
		cableList.setColorValue(0);
		springList.setColorValue(0);
		particleList.setColorValue(0);
		
		//CONTROLS
		title2 = controlP5.addTextlabel("label3","CONTROLS",p.width-padding*8,bPadding*6);
		title2.setColorValue(0);
		
		bang = controlP5.addBang("addNet",         right, bPadding*7,  100, 10);
		bang = controlP5.addBang("addRadialNet",   right, bPadding*8,  100, 10);
		bang = controlP5.addBang("addMast",        right, bPadding*9,  100, 10);
		bang = controlP5.addBang("addCable",       right, bPadding*10, 100, 10);
		//bang.valueLabel().
		
		//Current Net
		curNet = controlP5.addTextlabel("curNet", "Focus Net:" + t.getNetIndex(), padding, padding*16);
		curNet.setColorValue(0);
		//next net
		bang = controlP5.addBang("nextNet",padding+55, bPadding*10,  45, 10);
		bang = controlP5.addBang("prevNet",padding, bPadding*10,  45, 10);
		bang = controlP5.addBang("deleteNet",padding, bPadding*11,  100, 10);
		slider = controlP5.addSlider("setXcount",  (float)5, (float)300,  padding, bPadding*12,100, 10);
		slider = controlP5.addSlider("setYcount",  (float)5, (float)300,  padding, bPadding*13,100, 10);	
		
		
		//Current rNet
		curRNet = controlP5.addTextlabel("curRNet", "Focus Net:" + t.getRNetIndex(), padding, padding*24);
		curRNet.setColorValue(0);
		bang = controlP5.addBang("nextRNet",padding+55, bPadding*15,  45, 10);
		bang = controlP5.addBang("prevRNet",padding, bPadding*15,  45, 10);
		//next rNet
		bang = controlP5.addBang("deleteRNet",padding, bPadding*16,  100, 10);
		slider = controlP5.addSlider("setRadius",  (float)5, (float)200,  padding, bPadding*17,100, 10);	
		slider = controlP5.addSlider("setRings",  (float)5, (float)25,  padding, bPadding*18,100, 10);
		slider = controlP5.addSlider("setRadials",  (float)5, (float)50,  padding, bPadding*19,100, 10);	
		slider = controlP5.addSlider("setStepSize",  (float)5, (float)200,  padding, bPadding*20,100, 10);	
		slider = controlP5.addSlider("setHeight",  (float)5, (float)200,  padding, bPadding*21,100, 10);	
		
		//prev rNet
		//delete rNet
		//Inner Ring Radius
		
		//net
		//mast
		//cable
		//focus point
		//selected point
		//next mast		

		//bang = controlP5.addBang("nextMast",  right, bPadding*13, 100, 10);
		//bang = controlP5.addBang("prevMast",  right, bPadding*14, 100, 10);
	
	}
	
	void displayControl(){
		//draw springs on/off
		//draw points on/off
		//draw stage on/off
		
		toggle = controlP5.addToggle("springOn", t.springOn, right, bPadding*20, 100, 10);
		toggle = controlP5.addToggle("meshOn", t.meshOn, right, bPadding*21, 100, 10);
		toggle = controlP5.addToggle("clothOn", t.clothOn, right, bPadding*22, 100, 10);
		toggle = controlP5.addToggle("focOn", t.focOn, right, bPadding*23, 100, 10);
		toggle = controlP5.addToggle("siteOn", t.siteOn, right, bPadding*24, 100, 10);
		toggle = controlP5.addToggle("stageOn", t.stageOn, right, bPadding*25, 100, 10);
		toggle = controlP5.addToggle("schemeOn", t.schemeOn, right, bPadding*26, 100, 10);
		toggle = controlP5.addToggle("drawU", t.drawU, right, bPadding*27, 100, 10);
		toggle = controlP5.addToggle("drawV", t.drawV, right, bPadding*28, 100, 10);
		//slider = controlP5.addSlider("setDegree", 0, 1,(float)0.5,     right, bPadding*25,100, 10);
		
	}
	
	void ioControl(){
		//import Lines
		bang = controlP5.addBang("importLines",   right, bPadding*12, 100, 10);
		//import Obj
		//import Points as Particles
		//import Points as Points
		bang = controlP5.addBang("importPoints",   right, bPadding*13, 100, 10);
		//import Mesh
		bang = controlP5.addBang("importOBJ",   right, bPadding*14, 100, 10);
		bang.setCaptionLabel("Load Geometry");
		bang = controlP5.addBang("exportOBJ",   right, bPadding*15, 100, 10);
		bang.setCaptionLabel("Save Geometry");
		//save state
		bang = controlP5.addBang("saveState", right, bPadding*16, 100,10);
		//clear state
		bang = controlP5.addBang("clearState", right, bPadding*17, 100,10);
		//load state
		bang = controlP5.addBang("dump", right, bPadding*18, 100,10);
		//export Obj
		//dump
	}
	
	void updateLabels(){
		netList.setValueLabel("NETS: "+ t.radialNets.size());
		mastList.setValueLabel("MASTS: "+ t.masts.size());
		cableList.setValueLabel("CABLES: "+ t.cables.size());
		particleList.setValueLabel("PARTICLES: "+ t.physics.numberOfParticles());
		springList.setValueLabel("SPRINGS: "+ t.physics.numberOfSprings());
		
		fPart.setValueLabel("FOCUS PARTICLE: "+ t.focusParticle);
		sPart.setValueLabel("SELECT PARTICLE: "+ t.selectParticle);
		fPoint.setValueLabel("FOCUS POINT: "+ t.focusPoint);
		
		curNet.setValueLabel("NET: "+ t.getNetIndex());
		curRNet.setValueLabel("RNET: "+ t.getRNetIndex());
		
	}
	
	void addToRadioButton(RadioButton theRadioButton, String theName, int theValue ) {
		  Toggle t = theRadioButton.addItem(theName,theValue);
		  t.captionLabel().style().movePadding(2,0,-1,2);
		  t.captionLabel().style().moveMargin(-2,0,0,-3);
		}
}
