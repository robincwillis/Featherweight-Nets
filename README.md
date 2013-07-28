```
	    _                          _   _                             
           | |  o                  o  | | | | o                   |      
 ,_    __  | |      _  _              | | | |     ,   __   __   __|   _  
/  |  /  \_|/ \_|  / |/ |  |  |  |_|  |/  |/  |  / \_/    /  \_/  |  |/  
   |_/\__/  \_/ |_/  |  |_/ \/ \/  |_/|__/|__/|_/ \/ \___/\__/ \_/|_/|__/
```

Title: fw_Nets

Author: Robin Willis

Modified: 3/18/11

Version: 1

[Homepage](http://code.robincwillis.com)
___
**License:** MIT
___
**Description:**

For decades designers have experimented with simulated structural behavior and its geometric implications through “Form Finding”. Techniques in form finding are best known through the hanging chain models of Antonio Gaudi as well as the physical models of Frei Otto who experimented with a number of physical modeling techniques to discover highly accurate structural forms. Recently, methods such as dynamic relaxation and force density have been employed for analyzing and optimizing structural forms. While these methods have led to a number of tools for refining structural forms, very few design tools exist for exploring and creating new structural forms. As a response to this Axel Killian and John Ochsenforf developed a three dimensional design and analysis tool to find funicular structural forms in real time through the use of particle-spring systems. This can be accomplished directly through computational methods providing powerful fast ways to generate and manipulate digital cable net models.
___
**Dependancies:**

- PEASYCAM
- TREAR PHYSICS
- CONTROLP5

___
**Notes:**

- This read me is incomplete.
- There are plenty of undocumented bugs

___
**PHYSICS:**
- NOT GOING TO GET INTO EXPLAINING THIS, THESE SLIDERS BASICLY CONTROL AVAILABLE SETTINGS FROM THE TREAT PHYSICS LIBRARY SO JUST SEE ITS DOCUMENTATION

**ELEMENTS:**
- ADDNET: ADDS A GEODESIC NET OBJECT (INSERTS AT COORD 0,0,0)
- SETXCOUNT:SETS THE NUMBER OF PARTICLES(U) FOR THE NEXT ADDED NET TO BE COMPOSED OF
- SETYCOUNT:SETS THE NUMBER OF PARTICLES(V) FOR THE NEXT ADDED NET TO BE COMPOSED OF
- ADDRADIALNET: ADDS A RADIAL NET OBJECT(CLICK POINT ON STAGE OR MODEL FOR INSERTION)
- SETRADIUS:SETS THE RADIUS(FOOT PRITN) FOR TH NEXT RADIAL NET
- SETRINGS: SETS THE NUMBER OF RINGS(V) FOR THE NEXT RADIAL NET TO BE COMPOSED OF
- SETRADIALS: SETS THE NUMBER OF RADIALS(U) FOR THE NEXT RADIAL NET TO BE COMPOSED OF
- SETSTEPSIZE: SETS THE SPACING BETWEEN PARTICLES FOR THE NEXT RADIAL TO BE ADDED
- SETHEIGHT: SETS THE HEIGHT OF THE NEXT RADIAL NET TO BE ADDED
- ADDMAST: ADDS A MAST ELEMENT (NOT FUNCTIONING)
- ADDCABLE: ADDS A CABLE ELEMENT (NOT FUNCTIONING)
- NEXTNET: FOCUS ON THE NEXT NET
- PREVNET: FOCUS ON THE PREVIOUS NET
- DELETENET: DELETE FOCUSED NET

**IMPORT/EXPORT:**
- IMPORTLINES: IMPORT POINTS FROM RHINO MODEL(LOOK INTO IO FUNCTIONALITY, THIS WORKS WITH SOME RHINOSCRIPTS I DEVELOPED)
- IMPORTPOINTS: IMPORT LINES FORM RHINO MODEL(LOOK INTO IO FUNCTIONALITY, THIS WORKS WITH SOME RHINOSCRIPTS I DEVELOPED)
- LOAD GEOMETRY: IMPORT RHINO MODEL (HMM)
- SAVE GEOMETRY: SAVE ALL NET GEOMETRY (HMM)
- SAVE STATE: SAVE PHYSICS AND NET STATE (NOT FUNCTIONING)
- DUMP: EXPORT EVERYTHING (NOT FUNCTIONING)

**KEYBOARD:**
- P: RESET
- W: CONSTRAIN DRAGGING VERTICAL
- S: CONSTRAIN DRAGGING HORIZONTAL
- Q: EXPORT .OBJ
- A: EXPORT .DXF
- 1: RESET CAMERA
- 2: ROTATE CAMERA X 90 DEGREES
- 3: ROTATE CAMERA Z 90 DEGREES
- 4: ROTATE CAMERA Y 90 DEGREES
- N: ADD NET (DOES THIS STILL WORK?)

**MOUSE:**
- LEFT CLICK: ROTATE
- RIGHT CLICK: ZOOM
- OPT + RIGHT CLICK: PAN

**PARTICLES:**
THERE ARE BASICLY TWO KINDS OF PARTICLES (FIXED AND FREE) CLICK AND DRAG A FREE PARTICLE TO FIX IT, CLICK A FIXED PARTICLE TO FREE IT. YOU CAN SNAP PARTICLES TO IMPORTED GEOMETRY OR POINTS ON THE STAGE GRID. YOU CAN CREATE MORE COMPLEX GEOMETRY BY WEILDING THE PARTICLES OF NETS TOGETHER. TO DO SO DRAG ONE PARTICLE ON TOP OF ANOTHER AND REALEASE. THESE TWO PARTICLES WILL BECOME ONE AND THE FORCES OF THE NET WILL ACT THROUGH THEM (AS OF RIGHT NOW THERE IS NO WAY TO UNWEILD PARTICLES)
RADIAL NETS HAVE A SPECIAL KIND OF PARTICLE AT THE CENTROID OF THERE UPPER RING, THIS BASICLY DIRECTS THE STEM AND NET GEOMETRY, THIS PARTICLE IS ALWAYS FIXED BUT CAN BE DRAGGED ANYWHERE TO DIRECT YOUR NET GEOMETRY
