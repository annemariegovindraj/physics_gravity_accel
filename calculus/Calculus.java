import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class Calculus extends Applet implements Runnable , MouseListener //, MouseMotionListener, KeyListener  
{ boolean isrunning=true, pause1=true , pause2=false;
int height, width, step= 1,   xo=450, yo=150, maxitems=0;

Thread roller; boolean exerciseover=false; 
String text[]=new String [25];
String[][] table; //=new String[5][5]; 
String tab1title="time$distance$\u0394s$average speed$";
String tab2title=" $ (s) $ $over last sec$";
/////////display//////
Image offScreenImage, msgimg1, msgimg2, tbimg, graphimg,equatimg;
//Image msgimg[]= new Image[5];
Color lightyellow= new Color(255,245,191); Color liteblue=new Color(47, 166, 187); //new Color(135,220,245);
Color bluegreen= new Color(47, 166, 187); Color lighterblue= new Color(55,181,200);
Color[] colorpalette={Color.red,   Color.blue, Color.magenta, Color.cyan};
Graphics offS; //Font thizfont;
FontMetrics fm; 
Font big, smallfont,textfont;

public void init()
{height=Integer.parseInt(getParameter("height"));
 width=Integer.parseInt(getParameter("width"));
 step=Integer.parseInt(getParameter("step"));
 setBackground(liteblue); 
 addMouseListener(this);
 offScreenImage=createImage(width, height); offS=offScreenImage.getGraphics();
 offS.setColor(liteblue);offS.fillRect(0,0,width,height);
 Font thizfont= offS.getFont();
  String fontname= thizfont.getName(); big= new Font(fontname, Font.PLAIN,22); smallfont = new Font(fontname, Font.PLAIN,12);
 textfont= new Font(fontname, Font.PLAIN,18);
 offS.setFont(textfont); fm=getFontMetrics(textfont);
 text[1]="In physics, we often work with rate of change : e.g. velocity is the rate at which distance covered in a unit time-interval, changes. Rate of change also applies to chemistry, biology, economics. . e.g. how does adding a little more of a reactant change the speed of the reaction?  . How does a slightly higher population change the proliferation rate?  .  How does the production-cost vary with the amount produced?";
 text[2]="AVERAGE SPEED VERSUS INSTANTANEOUS SPEED . average speed = $distance covered!/time elapsed!= $s!/t! . we know from Galileo's experiments that when a stone is dropped from a height, it will cover a distance s = $gt�!/2! in a time t. We  calculate the distance covered (s) for different times and deduce the distance covered in the last second.";
 text[3]="we want to calculate the speed at exactly 2 sec. ";
 text[4]="  We calculate the average speed for intervals of time of 1 sec, 0.1, and 0.01 sec, using the formula . $\u0394s!/\u0394t! = $5 (t+\u0394t)�-5t�!/\u0394t!. .  We see that at very small \u0394t, the average speed tends to a limit of 20 m/sec. At exactly 2 sec, $\u0394s!/\u0394t! cannot be calculated because \u0394t = 0.   . The Instantaneous Speed is the limit of the average speed for \u0394t tending to 0  "; 
 text[5]="  Taking the limit of the average speed, we get . v%instant!(t) = lim%\u0394t->0!( $5 (t\u2080+\u0394t)�-5t\u2080�!/\u0394t!) = lim%\u0394t->0! 5(2t\u2080+\u0394t)=10 t\u2080. . In calculus, we use \' h\' to denote a very small interval tending to zero. The derivative of a function f at x : . lim%h->0! $f(x+h)-f(x)!/h! ";
 text[6]="Let's represent it graphically for a function f(x)= 0.5t�. We start with t\u2080=1 and \u0394t =2. First we connect the points (t\u2080=1, 0.5t\u2080�=0.5) and (t\u2080+\u0394t=3, 0.5(t\u2080+\u0394t)�=4.5).  When we reduce \u0394t  to 1 then to 0.5 and 0.25, the line connecting the points (t, 0.5t�) and (t+\u0394t, 0.5(t+\u0394t)�)  rotates towards the tangent to the curve. The slope of this line is $\u0394s!/\u0394t!. . Taking the limit \u0394t tending to 0 , the connecting line is on the tangent to the curve, and the derivative . $ds!/dt! = lim%\u0394t->0!($0.5 (t+\u0394t)�-0.5t�!/\u0394t!) is the slope of this tangent, also the slope of the curve y=0.5x�. The slope gives the instantaneous rate of change ( here : slope = 1)";
 text[7]="Let's approach the tangent from the left side, starting with \u0394t =-1. We connect the point (1, 0.5) to (t+\u0394t=0, 0.5(t+\u0394t)�=0). We get $\u0394s!/ \u0394t ! = $-0.5!/-1!= + 0.5.  We reduce \u0394t  to -0.5 then to -0.25, and get the same tangent than before." ;
 text[8]= "For a function f(x), we can often find the function  $df(x)!/dx! ( called f'(x)) which at each point gives the derivative of f(x) at that point. Let's  try for simple polynomial functions. . . $d(x�)!/dx! = limit %h->0! ( $(x+h)� -x�!/ h!) =  2x. (Means the slope increases with increasing x) .";  
 text[9]= " $d(x�)!/ dx! =  . limit%h->0! ( $(x+h)� -x�!/ h!) .   =  $x�+3x�h+3xh  +h� -x�!/ h! . Taking the limit: . =  3x�";
 text[10]="$df(x)!/dx!=1 .  $df(constant)!/dx!=0. . The general formula is : $d!/dx!(x$n!) =nx$n-1!. It is also valid for negative exponents and for fractional exponents. . e.g. $d!/dx!( $1!/x�!) = -2 ($1!/x�!) .  $d!/dx!(\u221Ax)= $1!/ 2\u221Ax.!   ";
 text[11]="The derivative of the sum of 2 functions is the sum of the derivatives : . $d(f(x)+g(x))!/dx!=$d f(x)!/dx! + $d g(x)!/dx! . and  . $d(c f(x))!/dx!=c $d f(x)!/dx! . Watch out : derivative of a function multiplied by another function : .  $d(f(x).g(x))!/dx!=$d f(x)!/dx!g(x) + f(x)$d g(x)!/dx! . (not the product of the derivatives $d (x\u2074x�)!/dx! = $d(x\u2074)!/dx!x�+ x\u2074$d(x�!/dx!)";  
 text[12]=" There is one super-cool function (an infinite polynomial) whose derivative gives the function itself : $d!/dx!( 1 +x + $x�!/2!+$x�!/32!";//+$x\u2074!/234+  )";
 //text[24]="END OF THE DEMONSTRATION . Program written by annemarie.govindraj@gmail.com";
 }
 
 public void start()
{if(roller==null)   {roller=new Thread(this);}
  isrunning=true; roller.start(); pause1=true;
 } 
{if(roller!=null)  { isrunning=false; roller=null;} 
  } 
public void run()
{while (isrunning==true) {  
if (step==1)	{
 msgimg1=textDrawLines(550,300,text[1]);
pause2=true; repaint(); 
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
} 
if(step==2){msgimg1=textGetLines(text[2],550); msgimg2=textGetLines(text[3],550); 
 int items[][]=new int[4][4];
 for (int t=0; t<4;t++){items[t][0]=t; items[t][1]=5*t*t; }
 items[0][2]=0; items[0][3]=0;
 for (int t=1; t<4;t++){items[t][2]=items[t][1]-items[t-1][1]; items[t][3]=items[t][2]; }
   table=new String[4][4]; /////////////t =row  j=column
  for (int j=0;j<4;j++){ for (int t=0; t<4;t++){table[t][j]= ""+items[t][j] ;}}
  table[0][2]=table[0][3]=" ";                                     
int twidth=4*90+60; int theight= 5*50+20; 
tbimg=createImage(twidth, theight);
drawTable(tbimg, 4,4,90, table);
drawTitle(tbimg, 4,90, tab1title,true); 
drawTitle(tbimg, 4,90, tab2title, false); 
 pause2=true; repaint();
try{synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==3){ table=new String[5][5]; /////////////i =row  j=column
 for(int j=0;j<5;j++){ for (int i=0;i<5;i++) 
    {table[i][j]= "";
	 }}double ditems[][]=new double[3][5]; double t=1;
 for (int i=0 ; i<3;i++ ){ditems[i][1]=t; ditems[i][0]=ditems[i][1]+2; 
 ditems[i][2]=5*(ditems[i][0])*(ditems[i][0]); ditems[i][3]=(ditems[i][2])-20; 
 ditems[i][4]= (ditems[i][3])/ditems[i][1]; 
 t*=0.1;}
   /////////////i =row  j=column
 for(int j=0;j<5;j++){ for (int i=0;i<3;i++) 
    {int v=(int)(Math.round(ditems[i][j]*1000)); float vv=v;
	 table[i][j]= ""+vv/1000;//ditems[i][j];
	 }}
 table[3][0]=""+2; table[3][1]=""+0;  table[3][2]=""+20; table[3][4]=table[3][3]="n.d. ";                                     
int  twidth=5*90+60; int theight= 5*50+20; 
 tbimg=createImage(twidth, theight);
 drawTable(tbimg, 5, 4, 90, table);
tab1title="time$\u0394"+tab1title; 
drawTitle(tbimg, 5,90, tab1title,true); 
 msgimg1=textGetLines(text[4],420);
 msgimg2=textGetLines(text[5],320);
 repaint(); pause2=true; //repaint();
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
//try{roller.sleep(3000);} catch(InterruptedException ie){};
}
if(step==4){msgimg1=textGetLines(text[6],580); exerciseover= false ;
 int gwidth=360; int gheight=480;
 graphimg=createImage(gwidth,gheight); drawGraphpaper(graphimg,gwidth,gheight);
float scaley=1;
 drawAxes(graphimg,  gwidth,  gheight, 100,100, scaley);
 Graphics graphg= graphimg.getGraphics();  graphg.setColor(Color.black); graphg.setFont(new Font("Dialog", Font.PLAIN, 16));
  float previousx=0; float previousy=0; float y=0; float x=0; 
  for(x=0.01f;x<3.05;x+=0.01){y=(float)(0.5*x*x); graphg.drawLine(15+(int)(100*previousx),gheight-15-(int)(100*previousy),15+(int)(100*x),gheight-15-(int)(100*y)); previousx=x; previousy=y;}
 repaint(); 
 ///////approach tangent from right
 graphg.drawString("\u0394t", 150,gheight-50);graphg.drawString("\u0394s", 230, gheight-120);
 float deltax=2;	x=1;  float yo= 0.5f;
 for (int i=0;i<4;i++){
   graphg.setColor(colorpalette[i]); y=0.5f*(x+deltax)*(x+deltax); 
   graphg.drawLine(15+(int)(100*x),gheight-15-(int)(100*yo),15+(int)(100*(x+deltax)),gheight-15-(int)(100*y));
   graphg.drawLine(15+(int)(100*x),gheight-16-(int)(100*yo),15+(int)(100*(x+deltax)),gheight-16-(int)(100*yo));
    graphg.drawLine(14+(int)(100*(x+deltax)),gheight-15-(int)(100*yo),14+(int)(100*(x+deltax)),gheight-15-(int)(100*yo));
 graphg.drawLine(15+(int)(100*(x+deltax)),gheight-15-(int)(100*yo),15+(int)(100*(x+deltax)),gheight-15-(int)(100*y));
  graphg.drawString("\u0394s = "+deltax, 50,100+30*(i));
  repaint(); try{roller.sleep(1000);} catch(InterruptedException ie){}; deltax=deltax*0.5f;}
 ///////approach tangent from left
 msgimg2=textGetLines(text[7],580); exerciseover=true ;
 try{roller.sleep(2000);} catch(InterruptedException ie){}; 
 deltax=-1;	x=1; yo= 0.5f;
 for (int i=0;i<3;i++){
   graphg.setColor(colorpalette[i]); y=0.5f*(x+deltax)*(x+deltax); 
   graphg.drawLine(15+(int)(100*x),gheight-15-(int)(100*yo),15+(int)(100*(x+deltax)),gheight-15-(int)(100*y));
   graphg.drawLine(15+(int)(100*x),gheight-16-(int)(100*yo),15+(int)(100*(x+deltax)),gheight-16-(int)(100*yo));
    graphg.drawLine(14+(int)(100*(x+deltax)),gheight-15-(int)(100*yo),14+(int)(100*(x+deltax)),gheight-15-(int)(100*yo));
 graphg.drawLine(15+(int)(100*(x+deltax)),gheight-15-(int)(100*yo),15+(int)(100*(x+deltax)),gheight-15-(int)(100*y));
  graphg.drawString("\u0394s = "+deltax, 50,100+30*(i+4));
graphg.setColor(Color.black); 
graphg.drawString("\u0394t", 35,gheight-70);graphg.drawString("\u0394s", 20, gheight-40);
  repaint(); try{roller.sleep(1000);} catch(InterruptedException ie){}; deltax=deltax*0.5f;}
 
 pause2=true; repaint();
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
}
if(step==5){msgimg1=textGetLines(text[8],520); msgimg2=null;
 int gwidth=380; int gheight=520; int i=0;
 exerciseover=false;
 graphimg=createImage(gwidth,gheight);  Graphics graphg= graphimg.getGraphics(); 
 graphg.setFont(new Font("Dialog", Font.PLAIN, 16));
 float scaley=2;
  drawGraphpaper(graphimg,gwidth,gheight);
  drawAxes(graphimg,  gwidth,  gheight, 100,100, scaley);
  graphg.setColor(Color.black); graphg.setFont(big); graphg.drawString( "y = x�", 50,80);
  pause1=true; float previousx=0; float previousy=0; float y=0; float x=0; 
  for(x=0.01f;x<3.05;x+=0.01)
	{ y=(float)(x*x); 
	graphg.drawLine(15+(int)(100*previousx),gheight-15-(int)(100*previousy/scaley),15+(int)(100*x),gheight-15-(int)(100*y/scaley)); 
	previousx=x; previousy=y;
	}
	repaint(); ////for each curve , calculate different touchpoints for tangents
  graphg.setColor(colorpalette[0]);
  
 for (float xo=0.25f; xo<=2; xo+=0.25)
	 {float yo=0; float m=0; float dy =0;
	  m=xo; yo=0.5f*xo*xo;
		  if((yo-xo)<=0.12){previousx=(-0.12f-yo)/m +xo; previousx=(float)((int)(previousx*100))/100;}
		  else {previousx=0;}
		  previousy=m*(previousx-xo)+yo;
			 for(x=previousx+0.01f;x<3.05;x+=0.01)
				{ dy=m*(x-xo)+yo;
				graphg.drawLine(15+(int)(100*previousx),gheight-15-(int)(100*previousy),15+(int)(100*x),gheight-15-(int)(100*dy)); 
				previousx=x; previousy=dy;
				} repaint();
		 try{roller.sleep(1000);} catch(InterruptedException ie){}; 
  }
  exerciseover=true;  pause2=true; repaint();
try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
 }
 if(step==6){msgimg1=textGetLines(text[9],540);  //msgimg2=null;
 int gwidth=300; int gheight=580; 
 exerciseover=false;
 graphimg=createImage(gwidth,gheight);  Graphics graphg= graphimg.getGraphics(); 
 //graphg.setFont(new Font("Dialog", Font.PLAIN, 16));
 float scaley=2;
	drawGraphpaper(graphimg,gwidth,gheight);
	drawAxes(graphimg,  gwidth,  gheight, 100,100, scaley);
	graphg.setColor(Color.black); 
 	pause1=true; float previousx=0; float previousy=0; float y=0; float x=0; 
	for(x=0.01f;x<2.25;x+=0.01)
		{y=(float) (x*x*x);
		graphg.drawLine(15+(int)(100*previousx),gheight-15-(int)(100*previousy/scaley),15+(int)(100*x),gheight-15-(int)(100*y/scaley)); 
		previousx=x; previousy=y;
		}
		 graphg.setFont(big); graphg.setColor(Color.black); graphg.drawString( "y = x�", 50,80);//�

		repaint();
		////for each curve , calculate different touchpoints for tangents
 graphg.setColor(Color.red); 
 for (float xo=0.25f; xo<=2; xo+=0.25)
	 {float yo=0; float m=0; float dy =0;
		m= 3*xo*xo;  yo=(float) (xo*xo*xo); 
		if((yo-xo)<=0.15){previousx=(-0.12f-yo)/m +xo; previousx=(float)((int)(previousx*100))/100;}
		else {previousx=0;}
		previousy=m*(previousx-xo)+yo;
		 for(x=previousx+0.01f;x<2.25;x+=0.01)
				{ dy=m*(x-xo)+yo;
				graphg.drawLine(15+(int)(100*previousx),gheight-15-(int)(100*previousy/scaley),15+(int)(100*x),gheight-15-(int)(100*dy/scaley)); 
				previousx=x; previousy=dy;
				} repaint();
		 try{roller.sleep(1000);} catch(InterruptedException ie){}; 
	}
  exerciseover=true;  pause2=true;
 msgimg2=textGetLines(text[10],540); repaint();
 equatimg=createImage(200,60); Graphics mg= equatimg.getGraphics(); mg.setFont(textfont);
 mg.setColor(lightyellow); mg.fillRect(0,0,200,60);mg.setColor(Color.black);
 repaint();
 try{ synchronized(this){while(pause2==true) wait();}} catch(InterruptedException ie){};
 }
  if(step==8){//msgimg1=textGetLines(text[9],540); exerciseover=false; //msgimg2=null;
 int gwidth=540; int gheight=230; 
 exerciseover=false;
 graphimg=createImage(gwidth,gheight); 
 Graphics graphg= graphimg.getGraphics(); 
 graphg.setFont(big);
 drawGraphpaper(graphimg,gwidth,gheight);
 setAxis(graphimg);//drawAxes(graphimg,  gwidth,  gheight, 45, 20, scaley);
 graphg.setColor(Color.black); 
 graphg.drawString("y=cos(x)", 50,100);
 pause1=true; int previousx=0; float previousy=1; float y=0; float x=0; 
	int po=15; int qo=gheight-115;
	for(int i=1; i<520; i++)
		{ y=(float) Math.cos(i*Math.PI/180);
		graphg.drawLine(po+previousx,qo-(int)(100*previousy),po+i,qo-(int)(100*y));
		previousx=i; previousy=y;
		}
	repaint();
	 try{roller.sleep(500);} catch(InterruptedException ie){}; 
////for each curve , calculate different touchpoints for tangents
 graphg.setColor(Color.red); float xt=0;
 for (int i=15; i<=520; i+=45) 
	 { float xo=0, x1=0; float  yo=1, y1=0;  //xt,yt tangent touchpoint, xo,yo  x1,y1 first and last point within graph
		float yt=(float) Math.cos(xt);
		float m= -(float)Math.sin(xt);   
		if(m==0){xo=xt-(float)Math.PI/4; x1=xt+(float)Math.PI/4; yo=yt;y1=yt;}
		else if(m<0){xo=((1-yt)/m +xt);yo=1; y1=-1 ;x1=(-1-yt)/m+xt;}
		else if(m>0){xo=(-1-yt)/m +xt;yo=-1; y1=1 ;x1=(1-yt)/m+xt;}
		graphg.drawLine(po+(int)(xo*180/Math.PI),qo-(int)(yo*100),po+(int)(x1*180/Math.PI),qo-(int)(y1*100)); 
		 repaint();
		 try{roller.sleep(1000);} catch(InterruptedException ie){}; 
	 xt+= 0.25*Math.PI;}
}	
} }
		 
		 
public void drawTable(Image img, int nbcolumns, int nbrows,int widthcolumn, String[][] item)
{int dwidth=img.getWidth(null); int dheight= (1+ nbrows)*50+20;
 
 Graphics tg= img.getGraphics();   tg.setColor(lightyellow); tg.fillRect(0,0,dwidth,dheight);
	  tg.setColor(Color.black);tg.drawRect(2,2,dwidth-4, dheight-4);
	  tg.drawLine(2,70,dwidth-2,70);
	  for(int i=0; i<nbrows;i++){tg.drawLine(2,71+i*50,dwidth-2,71+i*50);}
	  for(int j=0; j<(nbcolumns);j++){tg.drawLine(j*widthcolumn,1,j*widthcolumn,dheight-1);}
	   maxitems=nbcolumns*nbrows; 
	  tg.setFont(textfont);
	// if(item.length==maxitems){
	for(int i=0; i<nbrows;i++){
	  for(int j=0; j<nbcolumns;j++)
	   {tg.drawString(item[i][j], 10+j*widthcolumn, i*50+100);}}}
  //}   
public void drawTitle(Image img, int nbcolumns, int widthcolumn, String titlestring, boolean ismain)
{Graphics tg= img.getGraphics(); 
 int ii=0; String [] ttitems= new String[nbcolumns];
 StringTokenizer mst= new StringTokenizer(titlestring,"$");
      while(mst.hasMoreTokens()&&ii<nbcolumns)      
	{ttitems[ii]=mst.nextToken();ii++;}
 tg.setColor(Color.black); 
 if(ismain==true){ii=0; tg.setFont(textfont);
  for(int j=0 ; j<nbcolumns;j++)
	   {tg.drawString(ttitems[ii], 10+j*widthcolumn, 30); ii++;}}
 else{ii=0; tg.setFont(new Font("Dialog", Font.PLAIN, 16)); for(int j=0 ; j<nbcolumns;j++)
	   {tg.drawString(ttitems[ii], 10+j*widthcolumn, 60); ii++;}}
}
 


public void drawGraphpaper(Image im, int dwidth, int dheight)
{   Graphics pg= im.getGraphics();   pg.setColor(Color.white); pg.fillRect(0,0,dwidth,dheight);
	  pg.setColor(Color.orange);
       for(int i=0; i<dwidth; i+=5)   {pg.drawLine(i,0,i,dheight-1);}
      for(int j=0; j<dheight; j+=5)   {pg.drawLine(0,j, dwidth-1,j);}
  }
public void drawAxes(Image im,  int gwidth, int gheight, int deltaX, int deltaY, float scaleY)
{ Graphics pg= im.getGraphics(); pg.setFont(new Font("Dialog", Font.PLAIN, 16)); 
 int po=15; int qo= gheight-15;
  pg.setColor(Color.black); pg.drawLine( po, qo, gwidth, qo); pg.drawLine(po,1, po, gheight);
      for(int p=po, i=0; p<gwidth; p+=deltaX, i++){pg.drawLine(p, qo,p, qo+4);
               pg.drawString(""+i, p-4,qo+14);}  
       for(int q=qo, i=0; q>0; q-=deltaY, i++){pg.drawLine(po, q,po-4, q);
               pg.drawString(""+(int)(i*scaleY), po-14,q+5);}  
     }
public void setAxis(Image im)
  {int dheight=im.getHeight(null); int dwidth=im.getWidth(null); 
   Graphics pg= im.getGraphics(); pg.setFont(big);  // x=tt
    int divx= 45; //int unitx=;
	int qo=dheight-115; int po=15; 
	     pg.setColor(Color.black); pg.drawLine( po, qo, dwidth-1, qo); pg.drawLine(po,2, po, dheight-2);
      for(int p=po, i=0; p<dwidth; p+=(divx), i++){pg.drawLine(p, qo,p, qo+4);}
             //  pg.drawString(""+i, p-4,qo+18);}  //1 sec = 10 [] =40 pix 
}
/*      pg.drawString("Time (sec)", dwidth[jj]-200 , qo-2); pg.drawString(""+variab[jj], 20, 16); 
      if(uny!=0){  for(int q=qo, i=0; q>0; q-=(float)(divy*uny*3),  i++)
       {pg.drawLine(po-3, (int)(qo-(float)(3*i*divy*uny)), po, (int)(qo-3*i*(divy*uny)));/// (int)(i*q));	   
	   if(i%2==0){pg.drawString(""+(i*divy), 1, (int)(qo-3*i*(divy*uny)));}}}
  }

public void drawVector(Image im, int Xo, int Yo,float bigangle , float dis)
{ float angle= bigangle%(2*(float)Math.PI);
 Graphics imgr= im.getGraphics(); imgr.setColor(Color.black);
 int xp=Xo +(int)(dis*Math.cos(angle)); int yp=Yo - (int)(dis*Math.sin(angle));
  imgr.drawLine(Xo,Yo, xp,yp); //int []triangleX= new int [3]; int []triangleY= new int [3];
   if((angle>=0.15*Math.PI)&&(angle<0.25*Math.PI))  //45*
    { int[] triangleX={xp-3, xp+3,xp+6}; int[] triangleY={yp-3,yp+3,yp-8};
      imgr.fillPolygon(triangleX,triangleY,3); } 
	 else if ((angle>=0.25*Math.PI)&&(angle<0.38*Math.PI))   //<90*
    { int[] triangleX={xp-4, xp+4,xp+4}; int[] triangleY={yp-3,yp+3,yp-6};
      imgr.fillPolygon(triangleX,triangleY,3); }  
     else if ((angle>=0.38*Math.PI)&&(angle<0.62*Math.PI))   //90*
    { int[] triangleX={xp-4, xp,xp+4}; int[] triangleY={yp,yp-8,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }  
     else if ((angle>=0.62*Math.PI)&&(angle<0.74*Math.PI))   //135*
    { int[] triangleX={xp-3, xp+3,xp-6}; int[] triangleY={yp+3,yp-3,yp-6};
      imgr.fillPolygon(triangleX,triangleY,3); }
	 else if ((angle>=0.74*Math.PI)&&(angle<0.88*Math.PI))   //>135*
    { int[] triangleX={xp-2, xp+2,xp-7}; int[] triangleY={yp+4,yp-4,yp-4};
    imgr.fillPolygon(triangleX,triangleY,3); }
	else if ((angle>=0.88*Math.PI)&&(angle<1.12*Math.PI))  //180*
    { int[] triangleX={xp, xp,xp-8}; int[] triangleY={yp+4,yp-4,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }
     else if ((angle>=1.12*Math.PI)&&(angle<1.26*Math.PI))   //>180 <225
    { int[] triangleX={xp+2, xp-2,xp-7}; int[] triangleY={yp+4,yp-4,yp+6};
      imgr.fillPolygon(triangleX,triangleY,3); }  
	else if ((angle>=1.26*Math.PI)&&(angle<1.38*Math.PI))   //225*
    { int[] triangleX={xp-3, xp+3,xp-6}; int[] triangleY={yp-3,yp+3,yp+6};
      imgr.fillPolygon(triangleX,triangleY,3); }
	else if ((angle>=1.38*Math.PI)&&(angle<1.62*Math.PI))   //270*
    { int[] triangleX={xp-4, xp,xp+4}; int[] triangleY={yp,yp+8,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }  
	else if((angle>=1.62*Math.PI)&&(angle<1.88*Math.PI))  //315*
    { int[] triangleX={xp-3, xp+3,xp+6}; int[] triangleY={yp+3,yp-3,yp+6};
      imgr.fillPolygon(triangleX,triangleY,3); } 
    else if ((angle>=1.88*Math.PI)||(angle<0.12*Math.PI))
    { int[] triangleX={xp, xp,xp+8}; int[] triangleY={yp-4,yp+4,yp};
      imgr.fillPolygon(triangleX,triangleY,3); }
	}
*/
public Image textDrawLines( int widthRect, int heightRect, String s)
{ Image im=createImage(widthRect, heightRect); Graphics msg=im.getGraphics();
   msg.setFont(textfont);  fm=msg.getFontMetrics();  
   msg.setColor(lightyellow); msg.fillRect(0,0,widthRect, heightRect);
   msg.setColor(Color.black);
   int wl=0;  wl=fm.stringWidth(s); 
        int linel=0; int lineheight=fm.getHeight();int y=lineheight+2;
       int spacelength = fm.stringWidth(" ");
         StringTokenizer tt=new StringTokenizer(s+" ."); //  fullstop to find end of string
             String word=" ", linee="";   					//contains the text
   while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("."))
	      {  msg.drawString (linee, 8,   y); linel=0; linee=""; y=y+lineheight+2;}   
	  else    {
	   wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>(widthRect-8))
	         { msg.drawString (linee, 8,  y);     
                         linel=wl; linee=word; y+=lineheight+2;  }
	    else {linee=linee+" "+word; }//showStatus("lines = "+linee); }
	     }
	} msg.drawString (linee, 8,  y);     //end of while	
 return im;}
 
public Image textGetLines( String s, int widthRect) ///text with sub or superscripts 
{  int wl=0; /* Font f=offS.getFont(); String fontname=f.getName();
 Font mathtext= new Font(fontname, Font.PLAIN,18);
 FontMetrics fm=getFontMetrics(mathtext);*/  
 int nblines=0; String[] mylines=new String[30];
        int linel=0;  int lineheight=fm.getHeight();
       int spacelength = fm.stringWidth(" ");
         StringTokenizer tt=new StringTokenizer(s+" ."); //  fullstop to find end of string
             String word=" ", linee="";   					//contains the text
   while(tt.hasMoreTokens())
	{ word= tt.nextToken();
	   if (word.equals("."))
	      {  mylines[nblines]= linee; linel=0; linee=""; nblines++;}   
	   else  {int oldlinel=linel;
	   wl=fm.stringWidth(word);
	  linel=linel+spacelength+wl;
	   if (linel>(widthRect-8))
	         { mylines[nblines]=linee;     
                         linel=wl; linee=word; nblines++;  }
	    else {linee=linee+" "+word; }
	     }
	}     //end of while	
 lineheight=30;  
 Image im=createImage(widthRect, (5+nblines)*lineheight); Graphics msg=im.getGraphics();
 msg.setFont(textfont); // fm=msg.getFontMetrics();  
 msg.setColor(lightyellow);msg.fillRect(0,0,widthRect,lineheight*(5+nblines));
 msg.setColor(Color.black);  int jx=2;
 for (int j=0; j<nblines; j++)
  {   jx+=lineheight;///among the [] of lines ,look for $ or % ,super or sub script. Then note string upto ! into sublines[]
       int wline=(mylines[j]).length();   int index=0, startindex=0; int Xstart=0;
         int nbsub=0, nbsup=0;  int Xsuperscript[]= new int[10];String strsuper[]=new String[10];  ///place and comtent of superscripts
		 int Xsubscript[]= new int[10];String strsub[]=new String[10];  ///place and content of subscript
		 int Xsublines[]= new int [20];String[] sublines=new String[20]; ///place and content of non sub or super-script
               int Xfrac[]= new int[10]; String strfrac[]=new String[10]; int nbfrac=0;int [] strfraclength=new int[5];
	 Xsublines[0]=8;    
  while(index<wline)                                     //check for $ for superscript, % =subscript
{  if( mylines[j].charAt(index)=='$')  
   { sublines[nbsup+nbsub]=mylines[j].substring(startindex, index);
     Xsuperscript[nbsup]=Xsublines[nbsup+nbsub]+spacelength+fm.stringWidth(sublines[nbsup+nbsub]);
     String leftstring=mylines[j].substring(index+1, wline);
     strsuper[nbsup]= leftstring.substring(0,leftstring.indexOf('!'));
	int strsuperlength= fm.stringWidth(strsuper[nbsup]);
	Xsublines[nbsup+nbsub+1]=Xsuperscript[nbsup]+fm.stringWidth(strsuper[nbsup])+spacelength;
	index=index+1+leftstring.indexOf('!'); 
		 
     if((mylines[j].charAt(index+1)=='/')||(mylines[j].charAt(index+2)=='/'))
       	{  Xsuperscript[nbsup]+=spacelength;	 Xfrac[nbfrac]=Xsuperscript[nbsup];
		   leftstring=mylines[j].substring(index+1, wline);
            strfrac[nbfrac]= leftstring.substring(1,leftstring.indexOf('!')); 
			strfraclength[nbfrac]= fm.stringWidth(strfrac[nbfrac]);
			index=index+1+leftstring.indexOf('!'); 
			if(strfraclength[nbfrac]>strsuperlength) {Xsublines[nbsup+nbsub+1]+=(strfraclength[nbfrac]-strsuperlength);}
			 strfraclength[nbfrac]=Math.max(strfraclength[nbfrac], strsuperlength);  nbfrac++;
		}			 
	 startindex=index+1; nbsup++;
     sublines[nbsup+nbsub]=mylines[j].substring(startindex,wline);
    }            

  else if(mylines[j].charAt(index)=='%')
          { sublines[nbsup+nbsub]=mylines[j].substring(startindex, index);
            Xsubscript[nbsub]=Xsublines[nbsup+nbsub]+fm.stringWidth(sublines[nbsup+nbsub]);
			String leftstring=mylines[j].substring(index+1, wline);
            strsub[nbsub]= leftstring.substring(0,leftstring.indexOf('!'));
			Xsublines[nbsup+nbsub+1]=Xsubscript[nbsub]+fm.stringWidth(strsub[nbsub])+spacelength;
	        index=index+1+leftstring.indexOf('!');
			startindex=index+1; nbsub++;
            sublines[nbsup+nbsub]=mylines[j].substring(startindex,wline);
		}
  else{index++;} }//end of while
  
 if((nbsup>0)||(nbsub>0))
  { for( int i=0; i<=(nbsub+nbsup);i++)
 {char[] temp= sublines[i].toCharArray();  msg.setFont(textfont); 
  msg.drawChars(temp,0,temp.length, Xsublines[i],jx) ;}
	if(nbsub>0){msg.setFont(smallfont); 
	for( int i=0; i<(nbsub);i++){
	char[] temp= strsub[i].toCharArray();
	msg.drawChars(temp,0,temp.length,Xsubscript[i], jx+1);}
    msg.setFont(textfont);
 }  
	if(nbsup>0){ for( int i=0; i<(nbsup);i++)
	{char[] temp= strsuper[i].toCharArray();
	 msg.drawChars(temp,0,temp.length,Xsuperscript[i], jx-6);}}
	if(nbfrac>0){for( int i=0; i< nbfrac;i++)
	 {char[] temp= strfrac[i].toCharArray();
	 msg.drawChars(temp,0,temp.length, Xfrac[i], jx+fm.getHeight()-3);
	 msg.drawLine(Xfrac[i],jx-3,Xfrac[i]+strfraclength[i],jx-3);}
	 jx=jx+fm.getHeight()-5;}}
 else {	 //jx=jx+fm.getHeight(); 
 char[] temp= mylines[j].toCharArray();
  msg.drawChars(temp,0,temp.length, 4,jx) ;
	} 
 } //next line
CropImageFilter cf=new CropImageFilter(0, 0,widthRect, jx+5);
 FilteredImageSource fis =new FilteredImageSource(im.getSource(), cf);
 Image imm =createImage(fis);
  return imm; } 
  
synchronized public void mouseClicked(MouseEvent me)
 { int coX=me.getX(); int coY=me.getY(); 
  switch(step) 
   {case 1:  case 2 : case 3:  
          if((coY>height-100)&&(coX>390)) //&&(coX<510))
                    { step++; pause2=false;notify();} 
					break;
	 case 4 : case 5 : case 6 :
    if((coY>height-100)&&(coX>390))
  	  {if(exerciseover==true){if (coX<510) { step++;  pause2=false;notify();} 
	                          else if (coX<690) { pause2=false;notify();} } ////SAME ANIME
	   else if(exerciseover==false) { pause1=false;notify();} 
	  }
	 break;
	 default: 
 step++; pause2=false;notify();}
  }
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}
public void mousePressed(MouseEvent me)
 {int coX=me.getX(); int coY=me.getY(); }
 public void mouseReleased(MouseEvent me){}
 
 public void update(Graphics g)////////////////////////////////////////////////
{offS.setColor(liteblue); offS.fillRect(0,0,width,height); 
offS.setColor(Color.black); offS.drawString("step = "+step, 20,height-5); //,+" t =  "+tt
 
  switch (step)
   {case 1 : case 2 : case 3 : case 10:
    offS.setColor(lighterblue);offS.fillRect(390, height-50, 120 ,40);     //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);
	break;
	case 4 :  case 5 : case 6 : case 8:
    if(exerciseover==true){offS.setColor(lighterblue);offS.fillRect(390, height-50, 120 ,40);offS.fillRect(520, height-50, 180 ,40);     //button next
    offS.setColor(Color.yellow); offS.drawString("NEXT",400,height-20);offS.drawString("SAME ANIMATION",530,height-20);
	}break;
	 case 15 : 
    offS.setColor(lighterblue);offS.fillRect(390, height-50, 120 ,40);offS.fillRect(520, height-50, 180 ,40);     //button next
    offS.setColor(Color.yellow); 
	 if(exerciseover==true){offS.drawString("NEXT",400,height-20);offS.drawString("SAME ANIMATION",530,height-20);}
	 else if(exerciseover==false) offS.drawString("CONTINUE",400,height-20); break;
	default : break;}
 switch (step)
   {case 1 :  offS.drawImage(msgimg1, 150, 100,this);
    break;
	case 2 : offS.drawImage(msgimg1, 430, 20,this);
			 offS.drawImage(msgimg2, 430, 300,this); 
				offS.drawImage(tbimg,2,100, this);
	break;
	case 3 : offS.drawImage(msgimg1, 2, 20,this);
	offS.drawImage(tbimg,450,100, this);
	offS.drawImage(msgimg2,510,300, this);
	break;
	case 4 :  offS.drawImage(msgimg1, 380, 20,this);
	offS.drawImage(graphimg,10,100,this);
	 if (exerciseover==true) {offS.drawImage(msgimg2, 380, 420,this);}
	break;
	case 5: case 8: // offS.drawImage(msgimg1, 440, 20,this);
	offS.drawImage(graphimg,10,100,this);
    break;
	case 6 : offS.drawImage(msgimg1, 400, 20,this);
	offS.drawImage(graphimg,10,10,this);
	// int hh=	msgimg2.getHeight(null);
	 //if (exerciseover==true) {
	 offS.drawImage(msgimg2, 400,330, this); //offS.drawImage(equatimg, 410,450, this);
	 //}
	 break;
	default : break;}

paint(g);
}
public void paint(Graphics g)
 {if(isrunning==true) 
  { g.drawImage(offScreenImage,0,0,this);
    }
//	g.setColor(Color.black);
  // for(int ii=0;ii<4;ii++){g.drawString(ttitems[ii], 50+150*ii, height-100);}
	   
  }   
 }
