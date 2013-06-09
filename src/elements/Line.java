package elements;

import processing.core.PApplet;

public class Line {

	PApplet p;
	public Point a;
	public Point b;
	public Point[] endPoints = new Point[2];
	public Point[] verts; //multiLine - at least 3
	//two point
	int LINE_COLOR = 0;
	int WEIGHT = 1;
	public Line(PApplet parent, Point _a, Point _b){
		p = parent;
		a = _a;
		b = _b;
		endPoints[0] = a;
		endPoints[1] = b;
		verts = endPoints;
	}
	
	//two point
	
	public Line(PApplet parent, float aX, float aY, float aZ, float bX, float bY, float bZ){
		p = parent;
		a = new Point(aX,aY,aZ);
		b = new Point(bX,bY,bZ);
		endPoints[0] = a;
		endPoints[1] = b;
		verts = endPoints;
	}
	
	//multi vert
	public Line(PApplet parent, Point[] v){
		p = parent;
		verts = v;
	}
	
	public void setAppearence(int stroke, int color){
		LINE_COLOR = color;
		WEIGHT = stroke;
	}
	
	//TODO constructor sdl
	public void draw(float scale){
		p.pushStyle();
		p.noFill();
		//p.stroke(LINE_COLOR);
		//p.stroke(200,50);
		p.stroke(200);
		p.strokeWeight(WEIGHT);
		p.beginShape(p.LINES);
			for(int i=0;i<verts.length;i++){
				p.vertex(verts[i].a.x*scale, verts[i].a.y*scale, verts[i].a.z*scale);
			}
		
		p.endShape();
		p.popStyle();
	}
	
}
