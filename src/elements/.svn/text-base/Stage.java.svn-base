package elements;

import processing.core.PApplet;

public class Stage {
	
	PApplet p;
	public Point[][] points;
    int stageSize = 1000;
    int gridSize = 50;
    
	public Stage(PApplet parent){
		p = parent;
		points = new Point[(stageSize/gridSize)*2][(stageSize/gridSize)*2];
		buildPoints();
	}
	
	public void buildPoints(){
		for (int i = 0; i<(stageSize/gridSize)*2;i++){
			for (int j = 0;j<(stageSize/gridSize)*2;j++){
				points[i][j] = new Point((i*gridSize)-stageSize,(j*gridSize)-stageSize,0);
				//points[i][j] = new Point(10,10,0);
				
			}
		}
		
	}
	
	public void drawPoints(){
		for (int i = 0; i<(stageSize/gridSize)*2;i++){
			for (int j = 0;j<(stageSize/gridSize)*2;j++){
				//
				points[i][j].draw(p, true, 1);
				//points[i][j] = new Point(i+gridSize-stageSize,j+gridSize-stageSize,0);
				//points[i][j] = new Point(i+10,j+10,0);
			}
		}
		
	}
	
	public Point getPoint(float mX, float mY){
		
		float screenXpos;
		float screenYpos;
		
		float x;
		float y;
		float z;
		float greedy = 5;

		for(int i=0;i<(stageSize/gridSize)*2;i++){
			for(int j=0;j<(stageSize/gridSize)*2;j++){
				
				x = points[i][j].a.x;
				y = points[i][j].a.y;
				z = points[i][j].a.z;
				
				 screenXpos=p.screenX(x,y,z);
				 screenYpos=p.screenY(x,y,z);
				
				if(mX > screenXpos-greedy && mX < screenXpos+greedy && mY > screenYpos-greedy && mY < screenYpos+greedy){
					p.pushMatrix();
					p.translate(points[i][j].a.x, points[i][j].a.y, points[i][j].a.z);
					
					p.pushStyle();
					p.fill(255,0,0);
					p.sphere(3);
					p.popStyle();
					p.popMatrix();
					
					return points[i][j];
				}
				
				
			}
		}
		
		return null;
	}
	
	public void drawSelectedPoint(Point sPoint){
		if (sPoint != null){
			p.pushMatrix();
			p.translate(sPoint.a.x, sPoint.a.y, sPoint.a.z);
			
			p.pushStyle();
			p.fill(200,200,200,100);
			p.sphere(2);
			p.popStyle();
			p.popMatrix();
			}
	}
	
	
	 public void drawStage(){

		   p.pushStyle();
		    
		    p.stroke(200,50);
		    for (int i = -stageSize; i<=stageSize;i=i+gridSize){
		    	p.line(i,-stageSize,i,stageSize);  
		    }
		    for (int j = -stageSize;j<=stageSize;j=j+gridSize){
		    	p.line(-stageSize,j,stageSize,j);
		     }
		    
		    p.stroke(200);
		    p.strokeWeight(2);
		    p.line(-stageSize, 0, 0, stageSize, 0, 0);
		    p.line(0, -stageSize, 0, 0, stageSize, 0);
		    p.line(0, 0, -stageSize, 0, 0, stageSize);
	    
		    p.popStyle();
	}
	
}
