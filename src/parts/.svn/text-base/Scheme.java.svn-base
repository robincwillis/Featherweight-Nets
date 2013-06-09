package parts;

import processing.core.PApplet;
import java.util.ArrayList;
import elements.*;

public class Scheme {

	PApplet p;
	
	ArrayList<Line[]> lineLayers;
	ArrayList<Surface[]> meshLayers;
	ArrayList<Point[]> pointLayers;
	
	public float SCALE = 6;
	
	public Scheme(PApplet parent){
	p = parent;	
		lineLayers = new ArrayList<Line[]>();
		pointLayers = new ArrayList<Point[]>();
	}
	
	public void addLineLayer(Line[] lineLayer){
		lineLayers.add(lineLayer);
	}
	
	public void addPointLayer(Point[] pointLayer){
		pointLayers.add(pointLayer);
	}
	
	public void draw(){
		
		//drawPoints();
		//p.pushMatrix();
		//p.scale(SCALE);
		drawLineLayers();
		drawPointLayers();
		//p.popMatrix();
		//drawMesh();
	}
	
	public void drawPoints(){
		
	}
	
	public void drawLineLayers(){
		if(!lineLayers.isEmpty()){
			for(int i=0;i<lineLayers.size();i++){
				drawLineLayer(lineLayers.get(i));
			}
		}
		
	}
	
	public void drawLineLayer(Line[] lines){
		for(int i=0;i<lines.length;i++){
			lines[i].draw(1);
		}
	}
	
	public void drawPointLayers(){
		if(!pointLayers.isEmpty()){
			for(int i=0;i<pointLayers.size();i++){
				drawPointLayer(pointLayers.get(i));
			}
		}
	}
	
	public void drawPointLayer(Point[] points){
		for(int i=0;i<points.length;i++){
			
			if(points[i] != null){
				points[i].drawSphere(p, (float)1, 1);
				//p.println(points[i].a.x);
			}
		//	points[i].draw(p, true,1);
		}
	}
	
	public void drawMesh(){
		
	}
	
public Point getPoint(float mX, float mY){
		
		float screenXpos;
		float screenYpos;
		
		float x;
		float y;
		float z;
		float greedy = 5;
		if(!pointLayers.isEmpty()){
		for(int i=0;i<pointLayers.size();i++){
				Point[] points = pointLayers.get(i);
				
				for(int j = 0;j<points.length;j++){
				
				if(points[j] != null){
				Point point = points[j];
				x = point.a.x;
				y = point.a.y;
				z = point.a.z;
				
				 screenXpos=p.screenX(x,y,z);
				 screenYpos=p.screenY(x,y,z);
				
				 if(mX > screenXpos-greedy && mX < screenXpos+greedy && mY > screenYpos-greedy && mY < screenYpos+greedy){
					p.pushMatrix();
					p.translate(point.a.x, point.a.y, point.a.z);
					
					p.pushStyle();
					p.fill(255,0,0,50);
					p.sphere(3);
					p.popStyle();
					p.popMatrix();
					
					return point;
					}
				}
				}
			}
		}
		return null;
	}
}
