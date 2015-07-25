import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/* sh gives the position, im[sh gives the nb of the image 0 to 4 or highlighted 5-9
occ occupied by fixed shape, no memory for moving shape
*/

public class Rowof3 extends Applet implements KeyListener, Runnable  , MouseListener
{ int score=0, level=1,  pauselength=350, A=16, B=20, rectwidth=25;//A for row, B column
 int sh[]=new int[3];                                           ////position of the shape
 int [] im=new int[A*(B+1)];  boolean [] occ=new boolean[A*(B+1)]; ///img and occupancy of fixed shapes
 int [] refimgsh= new int[3]; ///refnb of img of falling shapes
   int allincrements[]={1,A+1,1-A, A};         /////possible positions for rows, diagonals, columns
int [] fullrow; ////int select[]; int [] selectleft;

 Image offScreenImage; Graphics offS; 
 Image img[]=new Image[10];
 Thread game; 
 boolean gameover=true, intro=true, highlighting=false, pause=false, playing=true;
 Font fat = new Font ("Dialog", Font.PLAIN,24); 
 Random rm=new Random();
 Color liteblue=new Color(151,203,255);
String msg;

public void init()
{offScreenImage=createImage(A*rectwidth, B*rectwidth+60);
 offS=offScreenImage.getGraphics(); offS.setFont(fat);
 addMouseListener(this);
 addKeyListener(this);  requestFocus();
 if(level<3) pauselength=500; else {pauselength=350;}
  repaint();}

public void start()
{if(game==null)  {game=new Thread(this);}
 game.start();
}
public void readyingForStart()
{for (int i=0; i<B*A;i++){im[i]=-1; occ[i]=false;}
 for (int i=A*B; i<(B+1)*A;i++){im[i]=-1; occ[i]=true;}
 createNewShape(); gameover=false;
 score=0; repaint();}
 
public void stop()
{if(game!=null)  {playing=false; game=null;} } 

public void run()
{if(intro==true){ repaint();
  int tracked=0; String name[]= new String[10]; 
  try{MediaTracker tracker = new MediaTracker(this);
      StringTokenizer mst= new StringTokenizer(getParameter("img"),"$");
      while(mst.hasMoreTokens()&&tracked<10)      
	{name[tracked]=mst.nextToken();
	img[tracked]=getImage(getClass().getResource(name[tracked]+".GIF"));
	tracker.addImage(img[tracked], tracked);  tracked++;}
      tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
 try {  synchronized(this){while(intro==true) wait();}  }catch(InterruptedException e){}; 
 intro=false; readyingForStart(); }
 int max=(B-1)*A;
 while(playing==true)
 {while (gameover==false)
  {	//if((sh[0]>=max)||(sh[1]>=max)||(sh[2]>=max)){fixshape();  }else
	 if((occ[sh[0]+A]==false)&&(occ[sh[1]+A]==false)&&(occ[sh[2]+A]==false))
		{ sh[0]=sh[0]+A;sh[1]=sh[1]+A;sh[2]=sh[2]+A; repaint();} ///free fall
	else {fixshape();} //gameover=true;} //
	repaint(); 
	try { game.sleep(pauselength);  }catch(InterruptedException e){}; 
  }
 //repaint(); 
 //try { game.sleep(3000);  }catch(InterruptedException e){}; 
 //readyingForStart();
}	}
  
public void createNewShape()
{int counter=0;
 if(level==1){counter=rm.nextInt()%6;} 
 else {counter=rm.nextInt()%4;}
 if(counter<0) counter=-counter; 
  switch(counter)
  {case 0 :	case 4: sh[0]=5; sh[1]=6; sh[2]=7; break;
   case 1 : 	 sh[0]=5+A; sh[1]=6; sh[2]=6+A; break;
   case 2 :	case 5 : sh[0]=6; sh[1]=6+A; sh[2]=6+2*A; break;
   case 3 : 	 sh[0]=7+A; sh[1]=6; sh[2]=6+A; break;
   default : 	 sh[0]=7; sh[1]=6; sh[2]=8; break;
  }
 int rsh=rm.nextInt()%5;if(rsh<0) rsh=-rsh; 
  switch(rsh)
 {case 0 :	 refimgsh[0]=rsh; refimgsh[1]=rsh; refimgsh[2]=(rsh+1)%5; break;
 case 1 :  	 refimgsh[0]=rsh; refimgsh[1]=(rsh+1)%5; refimgsh[2]=(rsh+2)%5; break;
 case 2 :	  refimgsh[0]=rsh; refimgsh[1]=(rsh+3)%5; refimgsh[2]=(rsh+1)%5; break;
 case 3:      refimgsh[0]=rsh; refimgsh[1]=(rsh+2)%5; refimgsh[2]=rsh; break;
 case 4 :     refimgsh[0]=rsh; refimgsh[1]=(rsh+3)%5; refimgsh[2]=(rsh+2)%5; break;
 default :    refimgsh[0]=rsh; refimgsh[1]=rsh; refimgsh[2]=rsh; break;
 	}
 }  
public void fixshape()
{int [] base = new int[3]; 
 for(int s=0;s<3;s++)
	{base[s]=sh[s]; occ[sh[s]]=true; im[sh[s]]=refimgsh[s];
	 sh[s]=-1;}
if ((base[1]==6)||(base[0]==6)||(base[2]==6))
   { gameover=true; repaint(); try { game.sleep(3000);  }catch(InterruptedException e){}; 
	   readyingForStart();}           
else {  int[] fulllrow; int maxnbsq=0;
	do	{maxnbsq=0;
		int[][] rows= new int[3][16];
	   for (int s=0;s<3;s++)
			{if(occ[base[s]]==true) 
				{rows[s]=checkForRow(base[s]);}
			else {rows[s][0]=0;}}
		maxnbsq=findMax(rows[0][0],rows[1][0],rows[2][0]);
		if(maxnbsq<3) break;
		for(int s=0;s<3;s++)
			{if(rows[s][0]==maxnbsq){fullrow=rows[s]; break;}}
 	  if(maxnbsq>2)
		{if (maxnbsq>=5){score+=100;}
		else if(maxnbsq>=4) {score+=50;}
		else {score+=10;} 
		highlighting=true; repaint();
		try { game.sleep(600);  }catch(InterruptedException e){}; 
		highlighting=false; 
		//remove the row and let squares fall
		for(int w=0; w<fullrow[0]; w++)
				{int j=fullrow[w+1]; tumble(j);}
		//for(int i=0; i<A;i++) {occ[i]=-1;}
		repaint(); 
		try { game.sleep(600);  }catch(InterruptedException e){msg="interrupted";} 
      	}
	} while(maxnbsq>2);
createNewShape();}
}

public int findMax(int x,int y, int z)
{int max=z;
 if (y>z) {if (x>y){ max=x;}  else {max=y;}}
 else if(z<x) {max=x;}
 return max;}
			
public int[] checkForRow(int shi) 
{	 int [] maxrow= new int [16]; maxrow[0]=1;
	for(int incrnb=0; incrnb<allincrements.length; incrnb++)
		{int incr=allincrements[incrnb];
		int [] select=new int[8];int [] selectleft=new int[8];
		Arrays.fill(select,-1); Arrays.fill(selectleft,-1);  
		int h=0;  select[h]=shi; 
		while((Math.abs((select[h]%A)-(select[h]+incr)%A)<2)&&(im[select[h]]==im[select[h]+incr]))
			{h++; select[h]=select[h-1]+incr;}
		int hh=0; selectleft[hh]=shi; 
		while((im[selectleft[hh]]==im[selectleft[hh]-incr]))//(Math.abs(((select[h])%A)-(select[h]-incr)%A)<2)&&
			{hh=hh+1; selectleft[hh]=selectleft[hh-1]-incr;}
		int maxh=h+hh+1; //msg=""+maxh+",h"+h+",hh"+hh; 
		if(maxrow[0]<maxh)
			{maxrow[0]=maxh; 
			for(int jj=0 ; jj<=h;jj++){maxrow[jj+1]=select[jj];}
			for(int jj=1 ; jj<=hh;jj++){maxrow[h+jj+1]=selectleft[jj];}}
		//	if(incrnb==3) {verticalrow=true;} else {verticalrow=false;}
		}//next incr     
	return maxrow;}
 
public void tumble(int j)
	{while(j>=A){occ[j]=occ[j-A];im[j]=im[j-A]; j=j-A;}
	}
public void keyPressed(KeyEvent ke)
{ if(gameover ==false)
  {if(ke.getKeyCode()==KeyEvent.VK_LEFT) //left arrow
     {if((sh[0]%A==0)||(sh[1]%A==0)||(sh[2]%A==0)){}
	  else if((occ[sh[0]-1]==false)&&(occ[sh[1]-1]==false)&&(occ[sh[2]-1]==false))
		{sh[0]=sh[0]-1; sh[1]=sh[1]-1; sh[2]=sh[2]-1;}
	 }
   else if(ke.getKeyCode()==KeyEvent.VK_RIGHT)
     {if((sh[0]%A==A-1)||(sh[1]%A==A-1)||(sh[2]%A==A-1)){}
       else if((occ[sh[0]+1]==false)&&(occ[sh[1]+1]==false)&&(occ[sh[2]+1]==false))
             { sh[0]=sh[0]+1; sh[1]=sh[1]+1; sh[2]=sh[2]+1;}
	 }
   else if(ke.getKeyCode()==KeyEvent.VK_UP) //up arrow /////turning counterclockwise
    {int nx=newpos(sh[0],sh[1]);int nz=newpos(sh[2],sh[1]); ///sh[1]= rotation axis
	  if(((sh[0]%A==A-1)&&(nx%A==0))||((sh[2]%A==A-1)&&(nz==0)))
	   {}  ///exclude left border
	   else if(((sh[0]%A==0)&&(nx%A==A-1))||((sh[2]%A==0)&&(nz==A-1)))
	   {}  ///exclude right border
	 else if((occ[nx]==false)&&(occ[nz]==false))
   {   sh[0]=nx;sh[2]=nz;}  /////OK rotate
   }
   else if(ke.getKeyCode()==KeyEvent.VK_DOWN) //down arrow
    {}
} }
public int newpos(int shi, int shy)
{/////turning counterclockwise
 int ni= shy-(shy/A)+(shi/A)-A*(shi%A-shy%A); 
 return ni;}

public void keyReleased(KeyEvent ke){};
public void keyTyped(KeyEvent ke){};

synchronized public void mouseClicked(MouseEvent me)
  {if(intro==true){ intro=false; notify(); }
  else if (pause==true){pause=false;notify();}               ///debugging
  else if (pause==false){pause=true;notify();}
   }
public void mousePressed(MouseEvent me) {}
public void mouseReleased(MouseEvent me) {}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}

public void update(Graphics g)
{if (gameover==false)
  {offS.setColor(liteblue); offS.fillRect(0,0,A*rectwidth,B*rectwidth);
 ///paint occupied spaces
 for(int i=0; i<A*B;i++)
 {if(occ[i]==true)
  {int m=i/A;
   offS.drawImage(img[im[i]],(i%A)*rectwidth,m*rectwidth,this);}}
 //paint moving shape
  for(int s=0; s<3;s++)
{ int m=sh[s]/A; 
 if(sh[s]!=0){offS.drawImage(img[refimgsh[s]],(sh[s]%A)*rectwidth,m*rectwidth,this);}}
if(highlighting==true) {for(int i=0; i<fullrow[0]; i++)
 {int m=fullrow[i+1]/A; offS.drawImage(img[im[fullrow[i+1]]+5],((fullrow[i+1])%A)*rectwidth,m*rectwidth,this);}}
 
 offS.setColor(Color.black);
    for(int i=0;i<=A;i++) offS.drawLine(i*rectwidth,0,i*rectwidth,B*rectwidth); //verticals
    for(int i=0;i<=B;i++) offS.drawLine(0, i*rectwidth,A*rectwidth,i*rectwidth); //horizontals
 }
  offS.setColor(new Color(150,193,195));  offS.fillRect(0,B*rectwidth, A*rectwidth,60);
 offS.setColor(Color.black);  offS.drawString("Score : "+score, 20, B*rectwidth+26);
 paint(g);}

public void paint(Graphics g)
{	 if (gameover==false)
		{g.drawImage(offScreenImage, 0,0, this); 
		}
	else if(intro==true){g.setColor(Color.blue);  g.fillRect(0,0,A*rectwidth, B*rectwidth+60);
		g.setColor(Color.yellow); g.setFont(fat); g.drawString("START",rectwidth*(A/3),200);}
	else if (occ[6]==true)
		{g.setColor(liteblue);   g.fillRect(0,0,A*rectwidth, B*rectwidth+60);
		g.setColor(Color.black); g.setFont(fat);
		if (score>500){g.drawString("Good score, Congratulations", 2,150);
						level++; if(level<4) {g.drawString("Try level"+level, 2,250);
												if(level==3) pauselength=350;}
								  else g.drawString("that was the highest level", 2,250);}
		else {g.drawString("Game over"	,2,150);
		g.drawString("Try doing better next time"	,2,250);}			
		}
}}
/* String str1="nb"+maxnbsq; for (int jj=0 ; jj<=fullrow[0];jj++){str1+= ","+fullrow[jj];} 
   String str2=""; for (int jj=1 ; jj<=fullrow[0];jj++){str2+= ","+occ[fullrow[jj]];} 
  //g.setColor(Color.black); g.drawString(msg, 10,rectwidth*(B+1)-15); 
 */