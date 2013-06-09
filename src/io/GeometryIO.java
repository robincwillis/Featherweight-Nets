package io;


import processing.core.PApplet;
//import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import saito.objloader.*;
import elements.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.SwingUtilities;


public class GeometryIO {

	PApplet p;
	
	public float SCALE;
	
	public GeometryIO(PApplet parent){
		p = parent;
		SCALE = 12;
	}
	
	
	protected File selectFile(final String prompt)
	{

		  Frame parent = new Frame();
		  FileDialog fileDialog = new FileDialog(parent, prompt, FileDialog.LOAD);
		 // fileDialog.setFilenameFilter(new FilterImages());
		  fileDialog.setVisible(true);
		  String directory = fileDialog.getDirectory();
		  String filename = fileDialog.getFile();
		  return (filename == null) ? null : new File(directory, filename);

	}

	
	public String getFile(String prompt){
	p.println("aaaaa");
	String loadPath = p.selectInput(prompt);
	//prompt the user for a file path
	
	if (loadPath == null) {
		  // If a file was not selected
		  p.println("No file was selected...");
		  return null;
		} else {
		  // If a file was selected, print path to file
		  p.println(loadPath);
		}
	return loadPath;
	}
	
	public String timeStamp() {
		int s = PApplet.second(); // Values from 0 - 59
		int m = PApplet.minute(); // Values from 0 - 59
		int h = PApplet.hour(); // Values from 0 - 23
		int d = PApplet.day();

		String now = "_" + d + h + m + s;
		return now;
	}
	
	
	//OBJ LOADER
	
	public OBJModel readObj(String s){
		
		
		OBJModel model = new OBJModel(p, s, OBJModel.ABSOLUTE, p.POLYGON);
		// this is done becaue the model was made in meters 1 = 1m
		// where processing is 1 = 1pixel
		  model.disableMaterial();
		  model.disableTexture();
		  model.scale(SCALE);
		  model.translateToCenter();
		return model;
	}
	
	public OBJModel readObj(){
		
		File objFile   = selectFile("Select OBJ File");
		
		String path = objFile.getAbsolutePath();
		
		OBJModel model = new OBJModel(p, path, OBJModel.ABSOLUTE, p.POLYGON);
		// this is done becaue the model was made in meters 1 = 1m
		// where processing is 1 = 1pixel
		  model.disableMaterial();
		  model.disableTexture();
		  model.scale(6);
		  model.translateToCenter();
		return model; 	
	}
	
	//READING TEXT FILES
	
	//BROKE
	public void readMesh(String[] v, String[] n, Point[] point, Surface[] face){
		  //-----CREATE POINTS-----
		  //       EACH LINE OF v SPECIFIES COORDINATE VALUES FOR A VERTEX
		  for(int i=0; i<v.length; i++){
		     //    GET POINT COORDINATES OF INDEXED VERTEX
		     String[] vVals = p.splitTokens(v[i], ", ");
		     //    ADD VERTEX TO PT[]
		     point[i]= new Point( Float.valueOf(vVals[0]), Float.valueOf(vVals[1]), Float.valueOf(vVals[2]) );
		  }
		  
		  //-----CREATE FACES FROM POINTS-----
		  //       EACH LINE OF n SPECIFIES INDEX VALUES FOR A FACE
		  for(int i=0; i<n.length; i++){
		    //     GET INDIVIDUAL INDEX VALUES
		    String[] nVals = p.splitTokens(n[i], ", ");
		   
		   //      CREATE f[i] AS NEW FACE
		    //********* NEED FIX
		  // face[i]=new Surface();
		   
		   for(int j=0; j<nVals.length; j++){
		  //   int ndx = (int)(nVals[j]);
		       
		     // ADD POINT TO FACE
			   //********* NEED FIX
		   //  face[i]. =  new Surface(p[ndx]);
		   }
		  }
	//return array of surface objects
	}
	
	//TODO
	
	public String[] readFile(File file){
		
		String[] fileStrings = new String[0];
		p.println("START READ");
		try{
			 String stringRead = null;
			FileReader fro = new FileReader(file);
	       BufferedReader bro = new BufferedReader( fro );
	 
	       // declare String variable and prime the read
	      
	      // p.println("stringRead");
	       while( (stringRead = bro.readLine()) != null ) // end of the file
	       {
	          System.out.println(stringRead);
	       //  stringRead = bro.readLine( );
	         fileStrings = p.append(fileStrings, stringRead);// read next line
	       }
	 
	       bro.close();
	       fro.close();
	    }
	 
	    catch( FileNotFoundException filenotfoundexxption )
	    {
	      System.out.println( file.getName() +", does not exist" );
	    }
	 
	    catch( IOException ioexception )
	    {
	      ioexception.printStackTrace( );
	    }
	    
	    return fileStrings;
	  }
	
	public Line[] readLines(){
		
		File indexFile   = selectFile("Select Index File");
		File pointFile = selectFile("Select Vertex File");
		//p.println(indexFile.getName()+" "+ indexFile.getPath());
		String indexLine[] = readFile(indexFile);
		String pointLine[] = readFile(pointFile);
		
		Point[] points = new Point[pointLine.length];
		Line[] lines = new Line[indexLine.length];

		for(int i=0; i<pointLine.length; i++){
			p.println(pointLine[i]);
			String[] pos = p.splitTokens(pointLine[i], ", ");
			float x = Float.valueOf(pos[0]);
			float y = -1 * Float.valueOf(pos[1]);
			float z = Float.valueOf(pos[2]);
			x = round(x,2);
			y = round(y,2);
			z = round(z,2);
			
			points[i] = new Point(x,y,z);
			points[i].scale(SCALE);
		}
		p.println(points.length);
		for(int i=0; i<indexLine.length; i++){
			String[] index = p.splitTokens(indexLine[i], ", ");
			Point[] linePoints = new Point[index.length];
			for(int j=0;j<index.length;j++){
				linePoints[j] = points[Integer.valueOf(index[j])];
			}
			lines[i] = new Line(p,linePoints);
			
		}
		
		return lines;	
		
	}
	
	//DONE
	
	public Point[] readPoints(){
		
		File pointFile = selectFile("Select Point File");
		
		String fileLines[] = readFile(pointFile);
		Point[] points = new Point[fileLines.length];
		
		for(int i=0; i<fileLines.length; i++){
			 
			String[] pointVals = p.splitTokens(fileLines[i], ", ");
			 
			   float x = Float.valueOf(pointVals[0]).floatValue();
			   float y = -1* Float.valueOf(pointVals[1]).floatValue();
			   float z = Float.valueOf(pointVals[2]).floatValue();
			   x = round(x,2);
			   y = round(y,2);
			   z = round(z,2);
			   points[i] = new Point(x,y,z);
			   points[i].scale(SCALE);
			
		}
		p.println(Arrays.toString(points));
		p.println("Returned "+ points.length +" Points");
		return points;
	}
	
	float round(float number, float decimal) {
		  return (float)(p.round((number*p.pow(10, decimal))))/p.pow(10, decimal);
		} 

		
	//WRITING TEXT FILES - DONE!
	
	public void dump(){
		
	}
	
	public void writeMesh(ArrayList<Surface> faces, int vertCount, String name){
		//takes array of surface objects


		
		//is it quad mesh? or triangle mesh?
		int[][] indexes = new int[faces.size()][vertCount];
		
		ArrayList<Point> points = new ArrayList();
		
		for(int i=0;i<faces.size();i++){

			for(int j=0;j<faces.get(i).verts.length;j++){
				float x = faces.get(i).verts[j].x;
				float y = faces.get(i).verts[j].y;
				float z = faces.get(i).verts[j].z;
				//this is not going to work since I am making unique points here
				//maybe make this arraylist as something to check duplicates against??
				//or just remove duplicates i dunno
				Point point = new Point(x,y,z);
				if(!points.contains(point)){
					points.add(point);
					indexes[i][j] = points.indexOf(point);
				}else{
					indexes[i][j] = points.indexOf(point);
				}
			}
		}
		
		writePoints(points, name+"-mesh-");
		writeIndex(indexes, name+"-mesh-");

		
	}
	
	public void writeIndex(int[][] indexes, String name){
		
		PrintWriter output;
		output = p.createWriter(name+"-indx-"+timeStamp()+".txt");
		
		for(int i=0;i<indexes.length;i++){
			String line = "";
			for(int j=0;j<indexes[i].length;j++){
				int index = indexes[i][j];
				
				String sIndex = Integer.toString(index);
				if(i<indexes.length-1){
				line.concat(sIndex+",");
				}else{
					line.concat(sIndex);
				}
			}
			output.println(line);
		}
		
		output.flush();
		output.close();
	}
	
	public void writePoints(ArrayList<Point> points, String name){
		//takes array of point objects
		PrintWriter output;
		output = p.createWriter(name+"-pts-"+timeStamp()+".txt");
		for(int i=0;i<points.size();i++){
			float x = points.get(i).a.x;
			float y = points.get(i).a.y;
			float z = points.get(i).a.z;
			String sX = Float.toString(x);
			String sY = Float.toString(y);
			String sZ = Float.toString(z);
			output.println(sX +","+sY+","+"sZ");
		}
		
		output.flush();
		output.close();
		
	}
	
	public void writeLines(ArrayList<Line> lines,String name){
		//takes array of line objects
		int[][] indexes = new int [lines.size()][2];
		ArrayList<Point> points = new ArrayList();
		
		for (int i=0; i<lines.size();i++){
			for(int j=0; j< lines.get(i).endPoints.length;j++){
				Point point =lines.get(i).endPoints[j];
				if(!points.contains(point)){
					points.add(point);
					indexes[i][j] = points.indexOf(point);
				}else{
					indexes[i][j] = points.indexOf(point);
				}
			}
		}
		
		writeIndex(indexes,name+"-lines-");
		writePoints(points,name+"-lines-");
		
	}
	
}
