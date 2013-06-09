package elements;

import processing.core.PApplet;
import processing.core.PConstants;

public class Point
{
  public Vector3F a;
 
  public Point(){
	  this.a = new Vector3F();
  }
  
  public Point(float vx, float vy, float vz)
  {
    this.a = new Vector3F(vx, vy, vz);
  }
  
  public Point(Vector3F pos)
  {
    this.a = pos;
  }

  public void draw(PApplet p,boolean lineOn,float scale)
  {
        
    p.pushStyle();
    p.fill(0,0,0);
    p.stroke(0);
    p.strokeWeight(0);
    
    if (lineOn){
      p.beginShape(PConstants.LINES);
      p.vertex(a.x*scale-10, a.y*scale, a.z*scale);
      p.vertex(a.x*scale+10, a.y*scale, a.z*scale);
      p.vertex(a.x*scale, a.y*scale-10, a.z*scale);
      p.vertex(a.x*scale, a.y*scale+10, a.z*scale);
      //		p.vertex(a.x, a.y, a.z-1);
      //		p.vertex(a.x, a.y, a.z+1);
      p.endShape();
    } 
    else if (!lineOn){	
      p.point(a.x*scale, a.y*scale, a.z*scale);
    }
    p.popStyle();
  }
  
  public void drawSphere(PApplet p, float size, float scale){
	  p.pushStyle();
	  p.noStroke();
	  p.fill(100,100);
	  p.pushMatrix();
	  p.translate(this.a.x*scale,this.a.y*scale,this.a.z*scale);
	  p.sphere(size);
	  p.popMatrix();
	  p.popStyle();
  }
  
  public void draw2d(PApplet p,boolean lineOn,float scale)
  {
        
    p.pushStyle();
    p.fill(150,150,150,80);

    if (lineOn){

      p.beginShape(PConstants.LINES);
      p.vertex(a.x*scale-1/10, a.y*scale);
      p.vertex(a.x*scale+1/10, a.y*scale);
      p.vertex(a.x*scale, a.y*scale-1/10);
      p.vertex(a.x*scale, a.y*scale+1/10);
      //		p.vertex(a.x, a.y, a.z-1);
      //		p.vertex(a.x, a.y, a.z+1);
      p.endShape();
    } 
    else if (!lineOn){	
      p.point(a.x*scale, a.y*scale);
    }
    p.popStyle();
  }
  
  public void scale(float scale){
	  this.a.x = this.a.x * scale;
	  this.a.y = this.a.y * scale;
	  this.a.z = this.a.z * scale;
  }
  
	public float distanceTo(Point dPoint){
		Vector3F v = new Vector3F(dPoint.a.x,dPoint.a.y,dPoint.a.z);
		return (float)Math.sqrt((a.x-v.x)*(a.x-v.x) + (a.y-v.y)*(a.y-v.y) + (a.z-v.z)*(a.z-v.z));
	}
  
  
  public String ToString()
  {
    return "Pt:" + a.x + "," + a.y + "," + a.z;
  }


}

