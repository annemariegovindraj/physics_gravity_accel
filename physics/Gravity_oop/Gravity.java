import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
/*<applet code="Gravity.class" width=1150 height=650>
</applet>
  yo departure of ball in pixels from top of picture, d in m , 
 
  to write text: nl text uses function TextDrawLines (int widthRect, int heightRect, String text)
  If exponents or fractions use external class Mathstext : constr : Image (width, height will be adjusted anyway so pass max height possible), width, height (same as for pict))
  Use  mtext.setmyStyle (Color backgr, Color textcolour); 
  to set text : mtext.setmyText(String text); if superscript (  $superstring!) , subscript (%substring!) 
 and fractions (#numerator /denominator#). Superscripts inside the fractions allowed($ s!).  
  
*/
public class Gravity extends Applet implements MouseListener, Runnable
{	boolean isrunning=true, downwards=true, pause1=true , pause2=false ,croppingdone=false;  
	int height, width,   pauselength=30, step=1 ,lineheight=0,  A=150,B=80;
	int  depart=0, y, graphwidth, graphheight, graphheight2; float a=9.81f;//for step8
	float tt=0, lasttt=0, v=0, d=0, d1=0, d2=0; // for numeric display in step 10  
	Thread roller;
	String[] texts=new String[20];
	////external classses
	Mathstext[] mtext=new Mathstext[15];
	Graphpaper graph_v, graph_d, graph_d1, graph_d2,graph_d3,graph_v2,graph_v3;
	
	KButton nextbutton, samebutton, previousbutton, startbutton; //ask_v_button,ask_d_button;
/////////////////display///////////////
	Color lightblue= new Color(0,255,255); Color grass=new Color(0,136,40);Color green= new Color(78,214,173);
	Color bluegreen= new Color(47, 166, 187); Color lightyellow= new Color(255,245,191);Color darkblue=new Color(0,75,151);
	Image offScreenImage, msgimg, msgimg2, bigScene, scene_now, ballthrow; //ballthrow, 5 img for anime
	Image earthimg, moonimg, appleimg, newtonimg, branchimg, solarsystemimg, galaxiesimg, boyimg, ballimg;
	Image [] bgitems=new Image[20]; Image ballthrowing[]= new Image[5];
	Graphics offS, bigg; 
	Font textfont= new Font("Verdana", Font.PLAIN, 18); 
	FontMetrics fm; 
	int  xo=50, yo=50, x=xo,  x1=50,x2=200,  y1=50, y2=300; //step 12  
	int Xapple[]={120,232, 280};int Yapple[]={115, 50,80};int Xmoon=920, Ymoon=300;
	
 	
	
public void init()
{	height=Integer.parseInt(getParameter("height"));
	width=Integer.parseInt(getParameter("width"));
	pauselength=40;
	addMouseListener(this); setBackground(green);
	offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
	offS.setFont(textfont); offS.setColor(green);offS.fillRect(0,0,width,height);
	bigScene=createImage( 200,1300); bigg=bigScene.getGraphics();
	bigg.setColor(lightblue); bigg.fillRect(0,0,200,1300);bigg.setColor(grass); bigg.fillRect(0,1200,200,100);
////////downloading texts///////
int	totalitems=0; 
String inLine=null; InputStreamReader instream =null;
 try{ 
 instream= new InputStreamReader(getClass().getResourceAsStream("Gravity_Texts.txt"));
 BufferedReader dataStream = new BufferedReader(instream);
  while ((inLine=dataStream.readLine())!=null)
       {texts[totalitems]=inLine;totalitems++;}
     }catch(IOException e){showStatus ("error in data reading");}
		/////////downloading the images
  int tracked=0; String imgname[]= new String[20]; 
  try {MediaTracker tracker = new MediaTracker(this);
       StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
       while(mst.hasMoreTokens()&&tracked<20)      
		{imgname[tracked]=mst.nextToken();
		 bgitems[tracked]=getImage(getClass().getResource(imgname[tracked]+".gif"));
		 tracker.addImage(bgitems[tracked], tracked);
		 tracked++;}
		tracker.waitForID(0);
		boyimg=getAlpha(bgitems[0], 107,210); 
tracker.waitForAll();
 }catch(InterruptedException e){showStatus("No picture found");}
int[] coord1={290,height-50, 120,40};
    if(nextbutton==null){nextbutton=new KButton("NEXT", coord1);}
    int[] coord2={420,height-50, 200,40};
    if(samebutton==null){samebutton=new KButton("SAME ANIMATION", coord2);}
  	
}

public Image getAlpha(Image img, int width, int height)
{Image a_img;/// to make background transparent, all whites turn transparent
 int[] pixels= new int[width*height];
 PixelGrabber pg= new PixelGrabber(img,0,0,width,height,pixels,0,width);
 try{pg.grabPixels();}catch(InterruptedException ie){};
 for(int i=0; i<pixels.length; i++)
 {int p = pixels[i];  int red = 0xff&(p>>16);
  int green = 0xff&(p>>8); int blue = 0xff&(p);
  if ((red>245)&&(green>245)&&(blue>245)){pixels[i]=(0x00000000);}
 }
 a_img=createImage(new MemoryImageSource(width,height,pixels,0,width));
 return a_img;
}
public void start()
{if(roller==null)   {roller=new Thread(this);}
  isrunning=true; roller.start(); pause1=true;
 }

public void stop()
{if(roller!=null)  { isrunning=false; roller=null;} 
  } 
public void run()
{ while (isrunning==true)
 {if (step==1)
	{msgimg=textDrawLines(250,40," CENTRIPETAL FORCE "); 
	if(mtext[0]==null) {mtext[0]=new Mathstext(createImage((int)(width*0.45), height/2),(int) (width*0.45),(int) height/2,30 );
	mtext[0].setmyText(texts[0]);
	}
	nextbutton.setVisibility(false);samebutton.setVisibility(false);

	 repaint();

 ////////////////////////////anime of boy turning sling///////////////// 
	float th=0;A=150; B=80;
	for (tt =0; tt<153; tt++) 
		{th=(float)(tt*Math.PI/20); x=(int)(A*Math.cos(th)); y=(int)(-B*Math.sin(th)); 
		repaint();try{roller.sleep(20);   } catch(InterruptedException ie){};}
	int Yesc=y; int Xesc=x; x2=200;  //ang speed =w=589 pix/sec, th=3*PI/2+PI/6
	for (tt =154; tt<205; tt++)
		{x=Xesc+(int) (589*0.86*(tt-154)/40); y=Yesc-(int)(589*(tt-154)/80);if (x>width-x2) break;
		 repaint();try{roller.sleep(20);   } catch(InterruptedException ie){};}
	nextbutton.setVisibility(true);samebutton.setVisibility(true);

	 pause2=true;   repaint(); 
/////end of downloading the images	
	if (croppingdone==false)
	{earthimg=getAlpha(bgitems[3], 133,132);moonimg=getAlpha(bgitems[4], 165,163);
	appleimg=getAlpha(bgitems[5], 42,47); newtonimg=bgitems[6]; 
	branchimg=getAlpha(bgitems[7],311,150);solarsystemimg=bgitems[8]; galaxiesimg=bgitems[9];
	ballthrow=bgitems[10]; ballimg=getAlpha(bgitems[11],39,39);
	croppingdone=true;}
  try{  synchronized(this){while(pause2==true) wait();} }  catch(InterruptedException e){};  
 nextbutton.setVisibility(false);samebutton.setVisibility(false);
}
  
if(step==2){//////////anime earth and moon
  mtext[0]=null; croppingdone=false;
   msgimg=textDrawLines(250,40," GRAVITATIONAL FORCE "); 
  if(mtext[1]==null){mtext[1]=new Mathstext(createImage((int)(width*0.35), height/2),(int) (width*0.35),(int) height/2,30 );
  mtext[1].setmyStyle (darkblue,Color.yellow); 
  mtext[1].setmyText(texts[1]);}
  float th=0;A=200; B=140;
  for (tt =0; tt<200; tt++)
	{th=(float)(tt*Math.PI/100); x=(int)(A*Math.cos(th)); y=(int)(-B*Math.sin(th)); 
	 repaint(); try{roller.sleep(80);   } catch(InterruptedException ie){};}
  nextbutton.setVisibility(true);samebutton.setVisibility(true);
 pause2=true;   repaint(); 
try{  synchronized(this){while(pause2==true) wait();} }  catch(InterruptedException e){};  
 nextbutton.setVisibility(false);samebutton.setVisibility(false);
}

 if(step==3){///////////////////newton + apple+ moon
  mtext[1]=null;//we increased interline space of text
  msgimg=textDrawLines(250,50," UNIVERSAL LAW OF GRAVITATION "); 
  //////////preparing next text
  if(mtext[2]==null){mtext[2]=new Mathstext(createImage((int)(width*0.64), height), (int) (width*0.64), height,35 );
  //mtext[2].setmyStyle (lightyellow, Color.black); 
 mtext[2].setmyText(texts[2]);
 }
   Xapple[0]=120; Xapple[2]= 280; Yapple[0]=120; Yapple[2]=80; 
 // repaint(); try{roller.sleep(160);   } catch(InterruptedException ie){};
  for (tt =0; tt<150; tt++)
	{Yapple[1]=(int)(50+(float) 10*(tt)*(tt)/8); if (Yapple[1]>(height-120)) break;
	repaint();try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
  float th=(float)(Math.PI/6); A=1040; B=600;
  for (tt =0; tt<400; tt++)
	{ th=(float)(Math.PI/6+tt*Math.PI/1200); Xmoon=(int)(A*Math.cos(th)); Ymoon=(int)(-B*Math.sin(th)); //if(Xmoon<0) Xmoon=0;
	if(tt>110){Yapple[2]=(int)(80+(float) 10*(tt-110)*(tt-110)/8); if (Yapple[2]>(height-80)) Yapple[2]=height-80;}
	repaint();try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
  for (tt =0; tt<150; tt++)
	{Yapple[0]=(int)(120+(float) 10*(tt)*(tt)/8); if (Yapple[0]>(height-270)) break;
	repaint();try{roller.sleep(pauselength);   } catch(InterruptedException ie){};}
  nextbutton.setVisibility(true);samebutton.setVisibility(true);
 pause2=true;   repaint();  
  try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
nextbutton.setVisibility(false);samebutton.setVisibility(false);
}
if(step==4){///////////////////solar system ////////galaxies
	mtext[2]=null;
	 if(mtext[3]==null){mtext[3]=new Mathstext(createImage(250, 400),250,400,30 );
  mtext[3].setmyStyle (darkblue,Color.yellow); 
  mtext[3].setmyText(texts[3]);}
 if(mtext[4]==null){mtext[4]=new Mathstext(createImage(250, 300),250,300,30 );
  mtext[4].setmyStyle (darkblue,Color.yellow); 
  mtext[4].setmyText(texts[4]);}
	samebutton.setVisibility (false); nextbutton.setVisibility(true);
	pause2=true;   repaint();  
	try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
	}
if((step==5)||(step==6)||(step==7)){///////////calculations
  int nbtext=0;if (step==5){nbtext=5;msgimg=textDrawLines(350,40,"Attraction Force : Earth - Moon "); }/// force Earth - Sun
  else if (step==6) {nbtext=6;msgimg=textDrawLines(250,40," Gravity on Earth "); }  ////calculating  g on earth
  else if (step==7){nbtext=7;msgimg=textDrawLines(250,40," Mass and Weight "); } /////calculating  g on moon
  mtext[nbtext]=new Mathstext(createImage((int)(width*0.8), height), (int) (width*0.8), height,38 );
  mtext[nbtext].setmyStyle (lightyellow, Color.black); 
  mtext[nbtext].setmyText(texts[nbtext]);
  nextbutton.setVisibility(true); pause2=true;   repaint();  
 try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
}


 if(step==8){
  mtext[5]=null;mtext[6]=null;mtext[7]=null;
  if(mtext[8]==null){mtext[8]=new Mathstext(createImage((int)(width*0.65), height), (int) (width*0.65), height,30 );
	mtext[8].setmyStyle (lightyellow, Color.black); 
  mtext[8].setmyText(texts[8]);}
  pause2=true; repaint();     
  try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  

 }

  if(step==9){/////falling ball animation + numerical display
	mtext[8]=null;
	msgimg=textDrawLines(200,40, "FREE FALL");
	graphwidth=190; graphheight=400;
	a=9.81f;
	Image graph_d_img= createImage(graphwidth,graphheight); 
	if(graph_d==null){graph_d= new Graphpaper(graph_d_img, graphwidth, graphheight,3);}
	else {graph_d.drawbg();}
	graph_d.setAxis(10,1,"time(sec)",1, 10, "distance(m)"); //  1 m =1[] =3 pix//1 sec = 10 [] =30 pix 
	Image graph_v_img= createImage(graphwidth,graphheight); 
	if(graph_v==null){graph_v= new Graphpaper(graph_v_img, graphwidth, graphheight,3);}
	else {graph_v.drawbg();} 
	graph_v.setAxis(10,1,"time(sec)",2,5,"speed(m/sec)");  
	nextbutton.setVisibility (false); samebutton.setVisibility (false);
	if(startbutton==null){int[] coord1={40,height-50, 120,40}; startbutton=new KButton("START", coord1);}
    	else {startbutton.setVisibility (true); startbutton.changeLabel("START");}
//if(ask_d_button==null) {int[] coord2={width-300, 20, 80 , 40} ; ask_v_button=new KButton("V/T", coord2);
		//coord2[1]=90; ask_d_button=new KButton("S/T", coord2);}
    //graphasked='d'; 
	//ask_v_button.setVisibility(true); ask_d_button.setVisibility(true);
///animation 
for (int i=0;i<37; i++) {bigg.drawImage(bgitems[1],0,(i+2)*30,this);} bigg.drawImage(bgitems[2], 0, 30*39,this);
final int ho=115, yo=48; y=yo;///maximum height 1150,but 38*30 =1140 so yo=48, instead of 60;
float Tup=0, Tdn=0,  dmax=0, Vdn=0; v=0; d=0; // float last_v=0, last_d=0; downwards=true; 
 tt=0; repaint(); 
 pause1=true; try{  synchronized(this){while(pause1==true) wait();} }  catch(InterruptedException e){};
 startbutton.changeLabel("STOP"); 
  while ( tt<4.90)
  {v=(float) a*(tt-Tup); d=dmax+(float) a*(tt-Tup)*(tt-Tup)/2;
                        y= (int)(yo +d*10);
                       if(y>=1190) {Tdn=tt; Vdn=v; downwards=false;}
 depart=y-300; if(y<300) {depart=0;} else if (y> (1600-height)) {depart=1300-height;}//for cropping
   graph_d.addPoint(tt, d); graph_v.addPoint(tt,v);   
   repaint();  
try{synchronized(this){while(pause1==true) wait();} lasttt=tt; tt+=(float)0.05;roller.sleep(pauselength);   } catch(InterruptedException ie){};
 }
 pause2=true;  nextbutton.setVisibility (true); samebutton.setVisibility (true); repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 //ask_d_button.setVisibility(false);
 }

 if (step==10){ //////////////////choice of origin/////////////////////////////
	if(mtext[10]==null){mtext[10]=new Mathstext(createImage((int)(width*0.4), height), (int) (width*0.4), height,30 );
	mtext[10].setmyStyle (lightyellow, Color.black); 
	mtext[10].setmyText(texts[9]);}
    msgimg=textDrawLines(200,40," CHOICE OF ORIGIN "); 
	if(startbutton==null){int[] coord1={40,height-50, 120,40}; startbutton=new KButton("START", coord1);}
	else {startbutton.setVisibility (true); startbutton.changeLabel("START");}
	int[] coord3={500,height-50, 200,40};
  
	if(previousbutton==null){ previousbutton=new KButton("PREVIOUS PAGE", coord3);}
	previousbutton.setVisibility (false);
	nextbutton.setVisibility(false);samebutton.setVisibility(false);
	graphwidth=210; graphheight=400;
	a=9.81f;
	if (graph_d1==null){Image graph_d1_img= createImage(graphwidth,graphheight); 
	graph_d1= new Graphpaper(graph_d1_img, graphwidth, graphheight,3);}
	else {graph_d1.drawbg();}
	graph_d1.setAxis(10,1,"time(sec)",1, 10, "displacement(m)"); 
	
	if (graph_d2==null){Image graph_d2_img= createImage(graphwidth,graphheight); 
	graph_d2= new Graphpaper(graph_d2_img, graphwidth, graphheight,3);}
	else {graph_d2.drawbg();}
	graph_d2.setAxis(10,1,"time(sec)",1, 10, "distance(m)"); 
final int ho=115;///maximum height 115 m origin is at d =0;
float Tup=0, Tdn=0,  dmax=0, Vdn=0;  float last_v=0, last_d=0; v=0; d1=115; d2=0; downwards=true; 
 tt=0; repaint(); 
 pause1=true;
 try{  synchronized(this){while(pause1==true) wait();} }  catch(InterruptedException e){};
 startbutton.changeLabel("STOP"); 
  while ( tt<4.9)
  {v=(float) a*(tt-Tup);
			d2=dmax+(float) a*(tt-Tup)*(tt-Tup)/2;
			d1=ho-(float) a*(tt-Tup)*(tt-Tup)/2;
           // if(d2>=ho) {Tdn=tt; Vdn=v;downwards=false;}
					 //  }
   //else {v= Vdn-(float)a*(tt-Tdn);
	//	 d2 = ho- Vdn*(tt-Tdn)+(float) a*(tt-Tdn)*(tt-Tdn)/2; 
		// d1= Vdn*(tt-Tdn)-(float) a*(tt-Tdn)*(tt-Tdn)/2;
   // y=yo+(int)(d*10); if (v<=0) {downwards=true; Tup=tt; dmax=d;}
		//}
    graph_d1.addPoint(tt, d1); graph_d2.addPoint(tt,d2);   
   repaint();  
try{synchronized(this){while(pause1==true) wait();} lasttt=tt; tt+=(float)0.05;roller.sleep(pauselength);   } catch(InterruptedException ie){};
 }
 pause2=true; 
 nextbutton.setVisibility (true);  previousbutton.setVisibility (true);repaint(); 
 try{synchronized(this) {while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
startbutton.setVisibility (false);samebutton.setVisibility(false);previousbutton.setVisibility(false);
}


 if(step==11){  ////////////calculate time of impact///////////////////
	if(mtext[11]==null){mtext[11]=new Mathstext(createImage((int)(width*0.75), height), (int) (width*0.75), height,30 );
  //mtext[2].setmyStyle (lightyellow, Color.black); 
	mtext[11].setmyText(texts[10]);}
	msgimg=textDrawLines( (int) (width*0.75),35, "TIME OF IMPACT");
	msgimg2=textDrawLines( (int) (width*0.75),65,texts[11]); 
	int wl1= fm.stringWidth("we get t = \\/ "); int wl2=fm.stringWidth("2 h/g ");
	int wl3=fm.stringWidth("we get t = \\/ 2 h /g = \\/ ");int wl4= fm.stringWidth("23.45");
	Graphics msg=msgimg2.getGraphics(); msg.setColor(Color.black); 
	msg.drawLine(8+wl1,8,8 + wl1+wl2,8);msg.drawLine(8+wl1,9,8+ wl1+wl2,9);
	msg.drawLine(8+wl3,8,8 + wl3+wl4,8);msg.drawLine(8+wl3,9,8+ wl3+wl4,9);
 	previousbutton.setVisibility(true);
	repaint();  pause2=true; 	
		try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
   previousbutton.setVisibility(false);
   }
  
  if(step==12)
  {/////////////throwing ball up //////////////////
   if (croppingdone==false){
   try{	int tw= 470, th=  193; //(ballthrow.getWidth(null)); //(ballthrow.getHeight(null));
		CropImageFilter ff;
		FilteredImageSource fis;
		MediaTracker t=new MediaTracker(this);
		for (int i=0;i<5; i++)
			{ff=new CropImageFilter((int)(tw*i/5), 0, (int)(tw/5), 193);
			fis=new FilteredImageSource(ballthrow.getSource(), ff);
			Image imgg=createImage(fis);
			t.addImage(ballthrowing[i],i);
			t.waitForID(i);
			ballthrowing[i]=getAlpha(imgg, tw/5, 193);}
		} catch(InterruptedException ie){}; croppingdone=true;}
//////////throwing ball up  animation///// 
    mtext[9]=null;mtext[10]=null;mtext[11]=null;
	if(mtext[12]==null){mtext[12]=new Mathstext(createImage((int)(width*0.3), height), (int) (width*0.3), height,30 );
	//mtext[2].setmyStyle (lightyellow, Color.black); 
	mtext[12].setmyText(texts[12]);}
	graphwidth=210; graphheight=420;
	if (graph_d3==null){  
	graph_d3= new Graphpaper(createImage(graphwidth,graphheight), graphwidth, graphheight,3);}
	else {graph_d3.drawbg();}
	graph_d3.setAxis(30, 0.5f,"time(sec)", 40, 0.5f, "displacement(m)"); 
	
	if (graph_v3==null){ 
	graph_v3= new Graphpaper(createImage(graphwidth,graphheight), graphwidth, graphheight,3);}
	else {graph_v3.drawbg();}
	graph_v3.setAxis(30, 0.5f,"time(sec)",8, 1, "velocity(m/sec)",18, 180);
	
	 pauselength=50; 
 int Yball[]={106,91,70,42,4};// handpositions
 scene_now = ballthrowing[0]; y=Yball[0]-39; ;//start position of anime, 39 is size of ball
 float vo=5.6f, so=1.5f;
 v=vo; d=0; tt=0;  //
 if(startbutton==null){int[] coord1={40,height-50, 120,40}; startbutton=new KButton("START", coord1);}
	else {startbutton.setVisibility (true);startbutton.changeLabel("START");}
nextbutton.setVisibility (false); samebutton.setVisibility (false);	
 repaint();  pause1=true;
 try{synchronized(this){while(pause1==true) wait();} } catch(InterruptedException ie){};
//lasttt=0; float lasty= new float [5];
 tt=-0.25f;
 for(int tu=0;tu<4;tu++)
	{scene_now = ballthrowing[tu]; y=Yball[tu]-39;   repaint();  
	try{ roller.sleep(pauselength);   } catch(InterruptedException ie){}; 
	}
   tt=0;
  while( tt< 1.35f){          //// d in m, y in pixels  1m=150 pix
   scene_now=ballthrowing[4];v=vo-a*(float)(tt); d=vo*tt-(float)a*tt*tt/2; 
   y= -10-(int)(120*d);// if(y>180) break;
    graph_d3.addPoint(tt, d+so);  graph_v3.addPoint(tt, v); 
   repaint(); // if(d<(-1.1))break;
try{synchronized(this){while(pause1==true) wait();}lasttt=tt; tt+=0.05f; roller.sleep(pauselength);   } catch(InterruptedException ie){};
 } pause2=true; nextbutton.setVisibility (true); samebutton.setVisibility (true); repaint(); 
 try{synchronized(this){while(pause2==true) wait();  }} catch(InterruptedException ie){}; 
 startbutton.setVisibility (false);samebutton.setVisibility(false);
 } 

if(step==13)
  {/////////////////////////calculating height reached etc //////////////
  msgimg=textDrawLines(200,30,"HIGHEST POINT REACHED"); 
   if(mtext[13]==null){mtext[13]=new Mathstext(createImage((int)(width*0.7), height), (int) (width*0.7), height,30 );
	mtext[13].setmyText(texts[13]);}
	previousbutton.setVisibility (true);
	 pause2=true;   repaint();  
 try{  synchronized(this){while(pause2==true) wait();}  } catch(InterruptedException e){};  
previousbutton.setVisibility (false);
}
 
if(step==14){ msgimg=textDrawLines(300, 60, "END OF THE PROGRAM  " );
  repaint();isrunning=false; roller=null; }
}}



public void mouseReleased(MouseEvent me){}
public void mousePressed(MouseEvent me){}

synchronized public void mouseClicked(MouseEvent me)
 { int cX=me.getX(); int cY=me.getY(); 
    if(step==9){cX-=200; }
	KButton target=nextbutton.resolveButton(cX, cY);
	if (target==null) showStatus("error in button");
	else if (target==nextbutton)
		{step++; pause2=false; notify();} ///next panel
	else if (target==samebutton)
		{pause2=false; notify();} 
	else if (target==previousbutton)
		{step--; pause2=false; notify();} ///previous panel
	else if (target==startbutton)
		{ if (pause1==false)     { pause1=true; startbutton.changeLabel("RESUME");notify();}////pressed STOP
          else if (pause1==true) { pause1=false; startbutton.changeLabel("STOP"); notify(); }////pressed RESUME
		} 
 }/*else if (target==ask_v_button)
		{graphasked='v'; repaint();} // label=="V/T") 
	else if (target.label=="S/T")
		{graphasked='d'; repaint();} 
	else if (target==showanswerbutton)
		{tbl.showNextColumnItem(data); 
			pause1=false; notify(); } 
	*/


public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}

public Image textDrawLines( int widthRect, int heightRect, String s)
{  Image im=createImage(widthRect, heightRect); Graphics msg=im.getGraphics();
   msg.setFont(textfont);  fm=msg.getFontMetrics();  
   msg.setColor(lightyellow); msg.fillRect(0,0,widthRect, heightRect);
   msg.setColor(Color.black);
   int wl=0;  wl=fm.stringWidth(s); 
   int linel=0;   lineheight=fm.getHeight();int y=lineheight+2;
	int spacelength = fm.stringWidth(" ");
	StringTokenizer tt=new StringTokenizer(s+" ."); //  fullstop to find end of string
	String word=" ", linee="";   					//contains the text
   while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("."))
	      { msg.drawString (linee, 8,   y); 
		  linel=0; linee=""; y=y+lineheight+2;}   
	  else    {
	   wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>(widthRect-12))
	         { msg.drawString (linee, 8,  y);     
                         linel=wl; linee=word; y+=lineheight+2;  }
	    else {linee=linee+" "+word; }
	     }
	} msg.drawString (linee, 8,  y);     //end of while	
 return im;}
 

public void update(Graphics g)////////////////////////////////////////////////
{ offS.setColor(green); offS.fillRect(0,0,width,height);
 switch (step)
{
case 1 : ///////boy with sling
	offS.drawImage(msgimg, 200, 10,this); //offS.drawImage(msgimg2, 450, 160,this);
	mtext[0].drawTextbox(this, offS, (int)width/2, 50,this); 
	int handx=88, handy=17; x2=200; y2=300;offS.drawImage(boyimg, x2, y2,this); offS.setColor(Color.red); 
	
	   offS.fillOval(x2+handx-6+(int)x, y2+handy-6+(int)y, 12,12); offS.setColor(Color.black); 
	   if(tt<154) {offS.drawLine(x2+handx, y2+handy, x2+handx+(int)x, y2+handy+(int)y);}
	   else if (tt<161){offS.drawArc(x2+handx-40, y2+handy, 80, 147,0,90);}  //-40 because pix for full circle =80
	   else {offS.drawArc(x2+handx-20, y2+handy, 40, 175,0,90);}
	break;
case 2: ////////////earth + moon
	 offS.setColor(darkblue); offS.fillRect(0,0,width,height);          
	 offS.drawImage(msgimg, 200, 10,this); 
	 mtext[1].drawTextbox(this, offS, 500, 100,this); 
	 x2=250; y2=250;offS.drawImage(earthimg, x2-50,y2-50,100,100,this); offS.drawImage(moonimg, (x2+x-25), (y2+y-25), 50,50, this);
	break;
case 3:  /////////Newton, apple,moon
	 offS.setColor(darkblue); offS.fillRect(0,0,width,height);        
	 offS.drawImage(moonimg,Xmoon-110, 600+Ymoon,82,81, this);  
	 offS.drawImage(branchimg,0,0,this);
	 offS.drawImage(newtonimg, 0, height-228, this); 
	 for (int i=0; i<3;i++)
		{ offS.drawImage(appleimg,Xapple[i], Yapple[i], this); }
	 mtext[2].drawTextbox(this, offS, (int)(width*0.35), 270 ,this); 
	break;	
case 4: offS.setColor(darkblue); offS.fillRect(0,0,width,height);
 // offS.drawImage(msgimg, 560, 10,this); offS.drawImage(msgimg2, 290,360, this);
  offS.drawImage(solarsystemimg, 100, 10,this); 
  offS.drawImage(galaxiesimg, 550, 350,this); 
   mtext[3].drawTextbox(this, offS, 560, 10,this); 
	 mtext[4].drawTextbox(this, offS,290,360,this); 
 break;
case 5 :
	offS.drawImage(msgimg, 200, 2,this);
	mtext[5].drawTextbox(this, offS,  100, 50,this);
 break;    
case 6:
	offS.drawImage(msgimg, 200, 2,this);
	mtext[6].drawTextbox(this, offS,  100, 50,this);
 break;
case 7 :
	offS.drawImage(msgimg, 200, 10,this);
	mtext[7].drawTextbox(this, offS,  100, 70,this);
 break;
case 8 : 
	mtext[8].drawTextbox(this, offS,  100, 100,this);
 break;
case 9: /////////////free falling ball animation
	///picture demo, 
	bigg.setColor(lightblue); bigg.fillRect(150,0,20,1200); 
	bigg.setColor(grass); bigg.fillRect(150,1200, 20,100);
	bigg.setColor(Color.red); bigg.fillOval(152,y,8,8); 
	CropImageFilter f;
	FilteredImageSource fis;
	f=new CropImageFilter(0,depart, 200, height);
	fis=new FilteredImageSource(bigScene.getSource(), f);
	scene_now=createImage(fis);
 
	offS.drawImage(msgimg,50,2,this);
	//// numerical values
	offS.setColor(Color.black); 
	offS.drawString("Time (sec) :", 50,120); offS.drawString(""+(float)(Math.round(tt*100))/100, 300,120);
	offS.drawString("Acceleration (m/sec²) : ", 50,160); offS.drawString(""+a, 300,160);
	offS.drawString("Speed (m/sec) : ", 50,200); offS.drawString(""+(float)(Math.round(v*100))/100, 300,200);   //to avoid too many decimals
	offS.drawString("Distance travelled(m): ", 50,240); offS.drawString(""+(float)(Math.round(d*100))/100, 300,240); 
 ///graphs
 	graph_v.drawGraph(this, offS, width-2*graphwidth-220, height-graphheight-80, this);
 	graph_d.drawGraph(this, offS, width-graphwidth-210, height-graphheight-80, this); 
break;

case 10 : ////choice of origin
	mtext[10].drawTextbox(this, offS, 10, 62, this); offS.drawImage(msgimg, 10, 2,this);
	offS.setColor(Color.black);   //// numerical values
	offS.drawString("Time  (sec) :  "+(float)(Math.round(tt*100))/100, (int)(width*0.5)-20, 90); 
	offS.drawString("Distance from top of tower: ", (int)(width*0.5)-20,115); //offS.drawString(""+(float)(Math.round(d2*100))/100, 500,240); 
	offS.drawString("Displacement (from groundlevel): ", (int)(width*0.5)+graphwidth+10,115); 
		offS.drawString(" "+(float)(Math.round(d2*100))/100+" m ", (int)(width*0.5)-20,145); //offS.drawString(""+(float)(Math.round(d2*100))/100, 500,240); 
	offS.drawString(" "+(float)(Math.round(d1*100))/100+" m ", (int)(width*0.5)+graphwidth+10,145);// offS.drawString(""+(float)(Math.round(d1*100))/100, 800,240); 
	graph_d1.drawGraph(this, offS, (int)(width*0.5)+graphwidth+10, 170, this); 
	graph_d2.drawGraph(this, offS, (int)(width*0.5)-20, 170, this); 
break;
 
case 11:
	offS.drawImage(msgimg, 20, 5,this);
	mtext[11].drawTextbox(this, offS, 20, 50, this);  
	offS.drawImage(msgimg2, 20, 250,this);

 break;    

case 12: mtext[12].drawTextbox(this, offS, (int)(width*0.68), 10, this); //offS.drawImage(msgimg, 300, 10,this); 
	y2=height-350; x2=80; offS.drawImage(scene_now, x2, y2,this); 
	offS.drawImage(ballimg, x2+26, y2+y,this); 
	offS.setColor(Color.black);  
	offS.drawString("Velocity : "+(float)(Math.round(v*100))/100 +" m/sec ", 180,20);   //to avoid too many decimals
	float dis=(float)(Math.round(d*100))/100;
	offS.drawString("Displacement: 1.5m+ "+dis+" m = "+(float)(Math.round(150+dis*100))/100+" m", 180,40); 
	graph_d3.drawGraph(this, offS, 180, y2+193-graphheight ,  this); 
	graph_v3.drawGraph(this, offS, 400, y2+193-graphheight ,  this); 
break;

case 13 : //// calculate highest point
	mtext[13].drawTextbox(this, offS, 10, 100, this); offS.drawImage(msgimg, 10, 20,this);
break;
case 14:  offS.drawImage(msgimg, 100, 200,this);
break;
default : break;
 }
nextbutton.drawAllButtons(offS, this);  
paint(g);
}
public void paint(Graphics g)
 { if(isrunning==true) {if(step==9) { g.drawImage(offScreenImage,200,0,this); 
            g.drawImage(scene_now,0,0,this);}
    else{ g.drawImage(offScreenImage,0,0,this); 
//g.setColor(Color.black);g.drawString("step :" +step, 50,height-20);
 }
  }
else { g.setColor(green); g.fillRect(0,0,width,height);
g.setColor(Color.black); g.setFont(textfont);
g.drawString("END OF THE PROGRAM", 200,300); 
g.drawString("Program written by annemarie.govindraj@gmail.com", 200,330);}
}
}