import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Fences extends Applet
implements Runnable  , KeyListener , MouseListener 

{final int row =25; final int column=18;  int square=30;
 int level=0, biscuitnb=0 , totaltime=600, timeleft;
 AIdistance distAI;
 long counter=0;
 Color grass=grass =new Color(87,167,82); 
Color dark, brick, golden, beige;
 int snooze, monstersleep, girl_x, mx, dir, smx;
Image offScreenImage, girlimg, biscuitimg, fenceimg;
 Graphics offS, girl, biscuit, fence;
Thread  monsterThread;
 boolean gameover=false, intro=true, won=false, isrunning=false, smx_up=false,stupidmonster=false;
 int Grid[]=new int[row*column];
 int dist[]=new int[row*column];
Image MonsterRight, MonsterLeft, M, StupidMonsterRight, StupidMonsterLeft,SM;
Graphics MR, ML, MSL, MSR;
int mouthMRx[]={24,24,17}; int mouthMRz[]={13,19,15};int mouthMLx[]={1,1,8};
int topearRx[]={10,8,14};int topearRy[]={1,8,8};int topearLx[]={15,18,11};
int earRx[]={1,3,9}; int earRy[]={10,1,8}; int earLx[]={23,20,17};
Font f= new Font("Dialog", Font.BOLD, 18);
int F2[]={
 1,0,1,0,1,0,2,0,1,0,1,2,1,0,1,0,1,0,0,1,0,1,0,2,0,
 0,2,0,2,2,1,0,1,2,2,0,1,0,2,2,1,2,1,2,0,2,2,1,0,1,
 1,2,1,0,2,0,2,2,2,2,2,2,1,1,2,0,2,0,2,1,0,2,0,2,0,
 0,2,2,1,0,1,2,1,0,1,0,2,2,1,2,1,3,0,2,2,1,0,1,2,1,
 1,0,2,0,2,0,1,0,2,2,1,0,1,0,1,0,2,0,0,2,0,2,0,1,0,
 2,1,2,3,2,2,2,2,2,2,2,2,2,2,2,1,2,2,1,2,3,2,2,2,1,
 1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0,
 0,2,2,2,2,2,2,1,2,1,2,2,2,2,2,2,2,0,2,2,2,2,2,2,1,
 1,2,1,0,1,0,1,0,2,0,1,0,1,0,1,0,2,1,2,1,0,1,0,1,0,
 0,1,0,1,0,1,2,1,2,1,2,1,0,1,0,1,0,0,1,0,1,0,1,2,1,
 1,2,2,2,2,2,2,0,2,1,2,2,2,2,2,2,2,1,2,2,2,2,2,2,0,
 0,1,0,1,3,1,2,1,2,1,2,1,0,1,0,1,0,0,3,0,1,0,1,2,1,
 0,2,0,2,2,1,0,1,2,2,0,1,0,2,2,1,2,1,2,0,2,2,1,0,0,
 1,2,1,0,2,0,2,2,2,2,2,2,1,1,2,0,2,0,2,1,0,2,0,2,1,
 0,2,2,1,0,1,2,1,0,1,0,2,2,1,2,1,1,0,2,2,1,0,1,2,0,
 1,0,2,0,2,0,1,0,2,2,1,0,1,0,1,0,2,0,0,2,0,2,0,1,1,
 2,1,2,1,2,2,2,2,2,2,2,2,2,2,2,1,2,2,1,2,1,2,2,2,0,
 1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0};
 int F3[]={
 1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1,0,1,0,2,0,
 0,2,2,2,0,1,2,2,0,2,2,2,2,2,2,2,0,2,2,0,0,2,2,2,1,
 1,0,1,2,1,0,1,2,1,0,3,0,2,1,0,1,0,2,0,1,0,2,0,1,0,
 0,2,0,2,2,2,0,2,2,2,2,0,2,1,2,2,2,2,0,2,2,2,1,2,1,
 1,2,1,0,1,2,1,0,1,0,2,0,1,0,2,0,1,0,0,2,0,1,0,2,0,
 0,2,2,2,0,2,2,2,2,0,2,2,0,2,2,1,2,2,2,2,1,2,2,2,1,
 1,0,1,2,1,0,1,0,2,0,3,0,1,0,3,0,2,1,0,1,0,2,0,1,0,
 0,1,0,2,2,2,2,1,2,2,2,2,0,2,2,2,2,0,2,2,2,2,1,0,1,
 1,2,1,0,1,0,2,0,1,0,3,0,1,0,3,0,1,0,2,1,0,1,0,2,0,
 0,2,2,2,2,1,2,1,2,2,2,2,0,2,2,2,2,0,2,0,2,2,2,2,1,
 1,2,1,0,1,0,2,0,1,0,3,0,1,0,3,0,1,0,2,1,0,1,0,2,0,
 0,1,0,2,2,2,2,1,2,0,2,2,0,2,2,0,2,0,2,2,2,2,1,0,1,
 1,2,2,2,0,1,0,1,2,0,2,0,1,0,2,1,2,0,1,0,1,2,2,2,0,
 0,2,0,1,0,2,2,2,2,0,2,1,2,1,2,0,2,2,2,2,0,1,0,2,1,
 1,2,0,2,2,2,0,1,0,1,2,0,2,0,2,1,0,0,1,0,1,2,0,2,1,
 0,2,1,2,1,0,1,0,2,2,2,1,2,1,2,2,2,1,0,1,0,2,1,2,0,
 0,2,0,2,0,0,2,2,2,0,1,0,2,0,1,0,2,2,2,0,1,2,1,2,1,
 1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1,0};
int F1[]= {0,1,0,1,0,1,0,1,2,2,2,2,1,0,1,0,1,0,0,1,0,1,0,1,0,
           1,2,2,2,2,1,0,2,0,0,1,2,0,1,2,2,2,2,1,2,2,2,2,1,0,
           0,2,0,1,0,1,1,2,1,1,0,2,1,0,1,0,1,2,0,2,0,1,0,1,1,
           1,3,1,0,1,0,1,2,0,0,1,2,0,1,0,1,0,1,0,0,1,0,1,0,1,
           0,2,2,2,2,1,0,2,1,1,0,2,1,0,2,2,2,2,1,2,2,2,2,1,0,
           1,0,1,0,1,0,1,0,1,1,0,3,0,1,0,1,0,1,0,0,1,0,1,0,1,
           0,2,2,2,2,1,0,1,2,0,2,2,1,0,2,2,2,2,1,2,2,2,2,1,0,
           1,2,0,1,2,0,1,0,2,1,2,1,0,1,2,0,1,2,0,2,0,1,2,0,1,
           0,3,0,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,0,3,0,1,0,1,0,
           1,2,1,0,1,0,1,0,2,1,2,1,0,1,0,1,0,2,1,2,1,0,1,0,1,
           0,2,2,2,2,2,0,1,2,0,2,0,1,2,2,2,2,2,0,2,2,2,2,2,0,
           1,0,1,0,1,0,1,0,2,1,2,1,0,1,0,1,0,1,1,0,1,0,1,0,1,
           0,2,2,2,2,0,1,0,1,2,2,2,1,0,2,2,2,2,0,2,2,2,2,1,0,
           1,2,0,1,2,1,0,1,0,2,2,1,0,1,2,0,1,2,1,2,0,1,2,0,1,
           0,3,0,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,0,3,0,1,0,1,0,
		   1,2,1,0,1,1,0,1,2,2,2,1,0,1,0,1,0,2,1,2,1,0,1,0,1,           
		   0,2,2,2,2,2,1,0,2,1,2,0,1,2,2,2,2,2,0,2,2,2,2,2,0,
		   1,0,1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,1};

public void init()
{addKeyListener(this); requestFocus();
addMouseListener(this);
 //height=Integer.parseInt(getParameter("height"));
//width=Integer.parseInt(getParameter("width"));
level=Integer.parseInt(getParameter("level"));
snooze=Integer.parseInt(getParameter("pause"));
 offScreenImage=createImage(row*square, column*square);
 offS=offScreenImage.getGraphics();
 biscuitimg=createImage(square,square); biscuit=biscuitimg.getGraphics();
 girlimg=createImage(22,30); girl=girlimg.getGraphics();
 fenceimg=createImage(25,25); fence=fenceimg.getGraphics();
 MonsterRight=createImage(25,30); MonsterLeft=createImage(25,30);
 MR=MonsterRight.getGraphics(); ML=MonsterLeft.getGraphics();
 StupidMonsterRight=createImage(25,30); StupidMonsterLeft=createImage(25,30);
 MSR=StupidMonsterRight.getGraphics(); MSL=StupidMonsterLeft.getGraphics();
dark =new Color(105,75,47); brick= new Color(203, 96, 80); 
 golden= new Color(243, 187,7); beige =new Color(199,157,116); 
 drawGirl(); drawMonster(); drawBiscuit(); drawFence();drawStupidMonster();
 readyingForStart(level);
}
public int countBiscuitNumber()
{biscuitnb=0; for(int i=0;i<row*column;i++)
 {if (Grid[i]==1) biscuitnb++;}
 return biscuitnb;
}
public void start()
{if(monsterThread==null){monsterThread= new Thread(this);}
 readyingForStart(0); 
 monsterThread.start();
}
public void stop()
{if(monsterThread!=null)
  {monsterThread=null;}
  isrunning=false;
} 

public void readyingForStart(int level)
{isrunning=true;
switch(level)
 {case 0 : case 2: //many fences, 1AI hound
  System.arraycopy(F2,0,Grid,0,row*column); 
  Grid[366]=3; Grid[206]=3; Grid[359]=3; Grid[223]=3; Grid[84]=3; Grid[210]=3; smx=150;smx_up=false; break;
  case 1 : case 3:  //many fences, 1AI hound
  System.arraycopy(F3,0,Grid,0,row*column); Grid[230]=3; Grid[294]=3; Grid[73]=3; Grid[402]=3; Grid[338]=3; smx=112;smx_up=false; break;
   case 4:  case 6: //less fences , 1AI hound
  System.arraycopy(F2,0,Grid,0,row*column);  smx=150;smx_up=false; break;
   case 5: case 7: //less fences, slow 
  System.arraycopy(F3,0,Grid,0,row*column); smx=112;smx_up=false; break;
   case 8: case 9://more fences, 1 AI+1 stupid hound
  System.arraycopy(F1,0,Grid,0,row*column); Grid[94]=3; Grid[294]=3;smx=12;smx_up=false; break;
   case 10: case 11: //high speed
  System.arraycopy(F2,0,Grid,0,row*column); Grid[366]=3; Grid[206]=3;Grid[210]=3;Grid[84]=3;Grid[359]=3;Grid[223]=3;smx=150;smx_up=false; break;
  case 12 : case 13:  //high speed
  System.arraycopy(F3,0,Grid,0,row*column); Grid[230]=3; Grid[294]=3;smx=112;smx_up=false; break;
  
 default: break;}
 
 mx=2; girl_x=(row*column)-4;
 
switch(level)
 {case 0 : case 1: case 4 : case 5: case 8 : case 10: case 12 : stupidmonster=false; break;
  case 2: case 3 : case 6 : case 7: case 9 : case 11: case 13 : stupidmonster=true; break;
  default: break;}
 snooze= (level<10) ? 400 : 250; 
  
 biscuitnb=countBiscuitNumber();
timeleft=totaltime; 
 dir=0;  won=false; 
 repaint();
}

public void drawGirl()
{girl.setColor(grass); girl.fillRect(0,0,22,30);
 girl.setColor(Color.red); girl.fillRect(0,21,22,9); 
 girl.setColor(Color.yellow); girl.fillOval(1,8,20,13);
 girl.setColor(Color.black); girl.fillArc(0,0,22,17,200, -220);
 girl.fillOval(7,13,2,2); girl.fillOval(15,13,2,2);
 girl.setColor(Color.red); girl.drawArc(7,14,8,5,180,180);
repaint();
}
public void drawMonster()
{MR.setColor(grass); MR.fillRect(0,0,25,30);
 MR.setColor(dark);MR.fillRect(0,17,12,12);MR.fillOval(0,6,20,17); MR.fillOval(10,13,15,8);
 MR.fillPolygon(topearRx,topearRy,3); MR.fillPolygon(earRx,earRy,3);
 MR.setColor(Color.red); MR.fillOval(9,9,4,4);
 MR.fillPolygon(mouthMRx, mouthMRz,3);
 //MR.setColor(Color.black); MR.fillOval(11,11,2,2);
ML.setColor(grass); ML.fillRect(0,0,25,30);
 ML.setColor(dark);ML.fillRect(13,18,12,12);
 ML.fillOval(5,6,20,17); ML.fillOval(0,13,15,8);
 ML.fillPolygon(topearLx,topearRy,3); ML.fillPolygon(earLx,earRy,3);
 ML.setColor(Color.red); ML.fillOval(10,10,4,4); ML.fillPolygon(mouthMLx, mouthMRz,3);
 //ML.setColor(Color.black); ML.fillOval(12,12,2,2);
}
public void drawStupidMonster()
{MSR.setColor(grass); MSR.fillRect(0,0,25,30);
 MSR.setColor(beige);MSR.fillRect(0,17,12,12);MSR.fillOval(0,6,20,17); MSR.fillOval(10,13,15,8);
 MSR.fillPolygon(topearRx,topearRy,3); MSR.fillPolygon(earRx,earRy,3);
 MSR.setColor(Color.red); MSR.fillOval(9,9,4,4);
 MSR.fillPolygon(mouthMRx, mouthMRz,3);
MSL.setColor(grass); MSL.fillRect(0,0,25,30);
 MSL.setColor(beige);MSL.fillRect(13,18,12,12);
 MSL.fillOval(5,6,20,17); MSL.fillOval(0,13,15,8);
 MSL.fillPolygon(topearLx,topearRy,3); MSL.fillPolygon(earLx,earRy,3);
 MSL.setColor(Color.red); MSL.fillOval(10,10,4,4); MSL.fillPolygon(mouthMLx, mouthMRz,3);
}
public void drawBiscuit()
{biscuit.setColor(grass); biscuit.fillRect(0,0,square,square);
 int leftup= square/2 -12;
 biscuit.setColor(golden); biscuit.fillOval(leftup+2,leftup+2,18,18);
 biscuit.setColor(dark); biscuit.drawOval(leftup+1,leftup+1,18,18);
 biscuit.fillRect(leftup+10,leftup+3,2,2);  biscuit.fillRect(leftup+14,leftup+6,2,2); 
 biscuit.fillRect(leftup+6,leftup+8,2,2); biscuit.fillRect(leftup+2,leftup+14,2,2); 
}
public void drawFence()
{fence.setColor(grass);fence.fillRect(0,0,25,25);
 fence.setColor(Color.black); for(int i=5; i<25; i+=5)   {fence.drawLine(i,0,i,25);  fence.drawLine(0,i,25,i);}
}
public void run()
{ try {synchronized(this){while(intro==true) wait();}}catch(InterruptedException e){}; 
 	
 while (gameover==false)
  { timeleft--;
    if(won==true){isrunning=false; repaint();
				 try{ monsterThread.sleep(3000);} catch(InterruptedException ie){};
				 level++; readyingForStart(level);}
   else	if (timeleft==0)
       { isrunning=false; won=false; repaint();
         try{monsterThread.sleep(3000);}
             catch(InterruptedException ie){};
			 readyingForStart(level);
		}
 distAI= new AIdistance(mx, girl_x, Grid, row, column); 
 int move= distAI.giveBestMove();
 dir=distAI.givedir();
 if (dir>1){dir= (mx%row>girl_x%row)? 1: 0;}//vertival mvt of hound facing left or right
 mx=mx+move; repaint();
 try{monsterThread.sleep(snooze);} catch(InterruptedException ie){};

 if(mx==girl_x) {isrunning=false; repaint();
                 try{monsterThread.sleep(3000);}
                 catch(InterruptedException ie){};
		 readyingForStart(level);}
 if(stupidmonster==true){ moveStupidMonster();
			  if(smx==girl_x) 
				{isrunning=false; repaint();
                 try{monsterThread.sleep(3000);}
                 catch(InterruptedException ie){};
				 readyingForStart(level);}
			 }
 //dist= distAI.givedist();
repaint();
}}
public void moveStupidMonster()
{if((level==8)||(level==9))
	{if (smx_up==false){smx+=row; if(smx>row*column){smx_up=true;smx-=2*row;}}
	 else {smx-=row; if(smx<0){smx_up=false; smx+=2*row;}}
	}
 else if((level==2)||(level==6)||(level==11))
	{if (smx_up==false){smx++; if((smx+1)%row==0){smx_up=true;smx-=2;}}
	 else {smx--; if(smx%row==0){smx_up=false; smx+=2;}}
	}
 else if((level==3)||(level==7)||(level==13))
	{if (smx_up==false){smx+=row;; if(smx>325){smx_up=true;smx-=2*row;}}
	 else {smx-=row; if(smx<100){smx_up=false; smx+=2*row;}}
	}	
}

public void keyPressed(KeyEvent ke)
{int girl_nex=0;
 if(isrunning==true)
{ if(ke.getKeyCode()==KeyEvent.VK_LEFT) //left arrow
   {if((girl_x)%row==0){girl_nex=girl_x;}
    else {girl_nex=(girl_x)-1;}}
  else if(ke.getKeyCode()==KeyEvent.VK_RIGHT)
   { if((girl_x)%row==(row-1)){girl_nex=girl_x;}
     else {girl_nex=girl_x+1; }} 
  else if(ke.getKeyCode()==KeyEvent.VK_UP) //up arrow
   {if (girl_x<row){girl_nex=girl_x;}
    else {girl_nex=girl_x-row; }}
  else if(ke.getKeyCode()==KeyEvent.VK_DOWN) //down arrow
   {if (girl_x<(column-1)*row){girl_nex=girl_x+row;}
  else {girl_nex=girl_x; }}
 if (Grid[girl_nex]==0) { girl_x=girl_nex;}   
 else if (girl_x==mx){isrunning=false; }
  else if ((stupidmonster==true)&& (girl_x==smx)){isrunning=false; }
 else if (Grid[girl_nex]==3)
	{	girl_x=girl_nex+(girl_nex-girl_x);//jumps fence
		if (Grid[girl_x]==1) { Grid[girl_x]=0; biscuitnb--; }
	}
 else if (Grid[girl_nex]==1)
        { Grid[girl_nex]=0;girl_x=girl_nex;///girl ate the biscuit
           biscuitnb--; }
 else {}
 if(biscuitnb==0){won=true;  }
	   //try{ Thread.sleep(3000);} catch(InterruptedException ie){};
	   //level++; readyingForStart(level);}
                                      
repaint();
}}


public void keyReleased(KeyEvent ke){}
public void keyTyped(KeyEvent ke){}
public void mouseReleased(MouseEvent me){}
public void mousePressed(MouseEvent me){}

synchronized public void mouseClicked(MouseEvent me)
{if (intro==true){ intro=false;  notify();}}
public void mouseEntered(MouseEvent me){}
public void mouseExited(MouseEvent me){}


public void update (Graphics g)
{ offS.setColor(grass); offS.fillRect(0,0,row*square,column*square);
 if (intro==true){offS.setColor(golden); offS.fillRect(300,100,300,200);
  offS.setFont(f); offS.setColor(Color.black);offS.drawString("START", 380, 200);}
 else
 {for (int i=0; i<row*column; i++)
	{if (Grid[i]==2) { offS.setColor(brick); offS.fillRect((i%row)*square, ((int)(i/row))*square, square,square);}
	else if (Grid[i]==1){ offS.drawImage(biscuitimg, (i%row)*square, ((int)(i/row))*square, this);}
	else if (Grid[i]==3) { offS.drawImage(fenceimg, (i%row)*square, 	 ((int)(i/row))*square, this);}
	}
 offS.drawImage(girlimg, ((girl_x)%row)*square, ((int)(girl_x/row))*square, this);

if (stupidmonster==true){SM= (smx%row>girl_x%row)? StupidMonsterLeft: StupidMonsterRight;
			offS.drawImage(SM, (smx%row)*square, ((int)(smx/row))*square, this);}

if(dir==0){ M=MonsterRight;}
else if(dir==1) {   M=MonsterLeft;}
offS.drawImage(M, (mx%row)*square, ((int)(mx/row))*square, this);
}
paint(g);
}
public void paint(Graphics g)
{  g.setColor(grass); g.fillRect(0,0,200,30); g.fillRect(250,0,200,30);g.fillRect(row*square-200,0,200,30);
  g.setColor(Color.yellow);
  g.setFont(f); g.drawString("biscuits left = "+biscuitnb, 20, 20);
  g.drawString("level = "+level, 270, 20);
  g.drawString("time left = "+timeleft, row*square-180, 20);
 if (isrunning==true)
   {g.drawImage(offScreenImage, 0,50, this);}
 else
 { g.setColor(golden); g.fillRect(270,200,350,50);
    g.setColor(Color.black); g.setFont(f);
	 if(won==true){ g.drawString("You win,Congratulations", 280,220);
    g.drawString(" Level = "+level, 280, 240);}
  else 
   {g.drawString("You lost, the hound caught you", 280,220);
    g.drawString(" Level = "+level, 280,240);}
  } 
}
}
class AIdistance
{int distance=1; int dir; int coord[]; int oldcoord[];
 int AIdist[]; int bestmove, mx, girl_x, row, column;
 int Grid[]; int AIcounter=0;
 boolean goalreached =false;
 AIdistance(int mx, int girl_x, int[]Grid,  int row, int column)
{ this.row= row; this.column=column;//this.dir=dir;
 this.mx=mx; this.girl_x= girl_x; this.Grid=Grid;
 coord= new int[row*column]; oldcoord= new int[row*column];
 int lengthcoord=1;//needed because oldcoord and coord = immutable
 AIdist = new int[row*column];
 for (int i=0; i<row*column; i++)
  {if(Grid[i]<2) {AIdist[i]=0;}
   else{AIdist[i]=-Grid[i];}
   coord[i]=-1;}
 coord[0]=girl_x;
 AIdist[girl_x]=5; AIdist[mx]=6;
 do {AIcounter ++;
     for (int i=0; i<lengthcoord;  i++)
       {oldcoord[i]=coord[i]; coord[i]=0;}
     int lk=0; distance++;
     for (int k=0; k<lengthcoord; k++)
      {if((oldcoord[k])%row!=0)
          {///trying to reach M from right
		  if (((oldcoord[k])-1)==mx)
             {goalreached=true;
              bestmove=+1; dir=0;
			  break;}
           else if (AIdist[(oldcoord[k])-1]==0)
  	     {AIdist[((oldcoord[k])-1)]=distance;
     	      coord[lk]=(oldcoord[k])-1;
              lk++; }
          }
      if(((oldcoord[k])+1)%row!=0)
          {if (((oldcoord[k])+1)==mx)
             {goalreached=true;
              bestmove=-1; dir=1;
  		break;}
           else if (AIdist[(oldcoord[k])+1]==0)
  	     {AIdist[((oldcoord[k])+1)]=distance;
     	      coord[lk]=(oldcoord[k])+1;
              lk++; }
          }
      if((oldcoord[k])>=row)
          {if (((oldcoord[k])-row)==mx)
             {goalreached=true;
              bestmove=+row;dir=3;
  		break;}
           else if (AIdist[(oldcoord[k])-row]==0)
  	     {AIdist[((oldcoord[k])-row)]=distance;
     	      coord[lk]=(oldcoord[k])-row;
              lk++; }
          }
     if((oldcoord[k])<row*(column-1))
          {if (((oldcoord[k])+row)==mx)
             {goalreached=true;
              bestmove=-row; dir=2;	
		break;}
           else if (AIdist[(oldcoord[k])+row]==0)
  	     {AIdist[((oldcoord[k])+row)]=distance;
     	      coord[lk]=(oldcoord[k])+row;
              lk++; }
          }
  } //end of for
 lengthcoord=lk;
} while ((goalreached==false) && (AIcounter<250));
}  // end of constructor
public int giveBestMove() {return bestmove;}
public int givedir() {return dir;}
public int[] givedist() {return AIdist;}
}
