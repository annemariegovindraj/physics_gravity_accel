 import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
/*<applet code="Puzzle.java" width=900 height=600>
<param name="img" value="Kangaroo.jpg">  
<param name="mould" value="mould8x5.jpg"> 
</applet>
*/
public class EasyPuzzle extends Applet
implements MouseListener, Runnable, MouseMotionListener// ActionListener

{//fast version using sort, merge , bisectsearch and dragging allowing loose fit =5 pix

int ij, nbrows, nbcolumns, piecewidth, pieceheight,distancex, distancey, totalnbpieces;
int correctionx, correctiony, screen, swidth, sheight, picturewidth, pictureheight, di;
int piecedragged=-1, groupdragged=-1, nbofgrps=0, center;
int piececenter[]; int PosX[]; int PosY[];
int pictpix[]; int mouldpix[]; int mcol[];
Image board, mypicture=null, mould=null,msgimg;  Graphics bg, msgg;
Image piece[]; 
Group[] grp= new Group[80]; 
 Thread walk;                   //only for beginning
boolean isloading=true; boolean dragpiece=false, draggroup=false; 
Font f=new Font("Dialog", Font.BOLD,18);
Color lightblue = new Color(128,255,255);
String msg1="",msg2="";

public void init()
{sheight=Integer.parseInt(getParameter("height"));
 swidth=Integer.parseInt(getParameter("width"));
nbcolumns=Integer.parseInt(getParameter("nbcolumns"));
nbrows=Integer.parseInt(getParameter("nbrows"));
di=Integer.parseInt(getParameter("piecedistance"));
 totalnbpieces=nbrows*nbcolumns;
piececenter=new int[120];  PosX=new int[120];  PosY=new int[120];
piecewidth=(int)(di*1.6); pieceheight=(int)(di*1.6);
board=createImage(swidth,sheight);  bg=board.getGraphics();
msgimg=createImage(200,40); msgg=msgimg.getGraphics();msgg.setColor(Color.black);
addMouseListener(this); addMouseMotionListener(this); 
picturewidth=nbcolumns*di; pictureheight=nbrows*di;
piece= new Image[totalnbpieces];
pictpix=new int[picturewidth*pictureheight];
mouldpix=new int[picturewidth*pictureheight];
mcol=new int[picturewidth*pictureheight];
MediaTracker tracker = new MediaTracker(this);
try{mypicture=getImage(getClass().getResource(getParameter("img")));
 tracker.addImage(mypicture, 0);
mould=getImage(getClass().getResource(getParameter("mould")));
 tracker.addImage(mould, 1);
 tracker.waitForAll();
    }catch(InterruptedException e){showStatus("No picture found");}
pictpix=getPixels(mypicture, picturewidth, pictureheight);
mouldpix=getPixels(mould, picturewidth, pictureheight);
for(int i=0; i<mouldpix.length; i++)
  {int p= mouldpix[i];
   int green=0xff&(p>>8);int blue=0xff&(p);
   if((green>220)&&(blue>220)){mcol[i]=1;} else {mcol[i]=0;}
  }
Random r= new Random();
for(int i=0; i<totalnbpieces; i++)
  {PosX[i]=r.nextInt()%(swidth-di); if(PosX[i]<0) {PosX[i]=-PosX[i];}
   PosY[i]=r.nextInt()%(sheight-di);  if(PosY[i]<0) {PosY[i]=-PosY[i];}
  }
}
public void start()
{if(walk==null)
  {walk=new Thread(this);}    isloading=true; 
  walk.start();
 }
public void run()
{while(isloading==true)
 { for(screen=1; screen<3;screen++)
  {repaint(); 
   try{walk.sleep(100);}catch(InterruptedException ie){showStatus("Interrupted");}
  }
for(int ii=0;ii<totalnbpieces; ii++)
  {  piececenter[ii]=defineCenter(ii);
     ij=ii;
	 piece[ii]=definePiece( piececenter[ii]);
     screen=3; repaint();
try{ Thread.sleep(20);}catch(InterruptedException ie){showStatus("Interrupted");}
  }
isloading=false;
repaint();
}}
public int[] getPixels(Image img, int width, int height)
{int pixels[]= new int[width*height];
PixelGrabber pg= new PixelGrabber(img,0,0, width, height, pixels, 0, width);
try{pg.grabPixels();} catch(InterruptedException iie){showStatus("Dont get Pixels");}
return pixels;
}
public int defineCenter (int k)
{int center =(((int )(k/nbcolumns))*picturewidth*di)+(picturewidth*di/2)+(((int)(k%nbcolumns))*di)+di/2;
return center;
}

public Image definePiece(int center)
{/*
*/
int lowerlimitx=center%picturewidth-(piecewidth/2);
int higherlimitx=center%picturewidth+(piecewidth/2);
int lowerlimity=center/picturewidth-(piecewidth/2);
int higherlimity=center/picturewidth+(piecewidth/2);
int area=piecewidth*pieceheight;
int piecepix[] = new int [area];
Image img;
 int contexp[]=new int[20*di];int finalcont[]=new int[area];

Arrays.fill(finalcont,-1); Arrays.fill(contexp,-1);
contexp[0]=center-picturewidth;contexp[1]=center-1;contexp[2]=center+1;contexp[3]=center+picturewidth;
int cycle=0; finalcont[0]=center;
int nborig=0, nbfinalcont=1, nbexpanded=4;
//msg1+="center "+center+","+finalcont[0];repaint();
do{ int[] contorig=new int[nbexpanded];
	System.arraycopy(contexp,0,contorig,0,nbexpanded);
	Arrays.fill(contexp,-1);
	nborig=nbexpanded; nbexpanded=0;

 for(int i=0; i<nborig; i++) 
   {      // expanding to west
if((contorig[i]%picturewidth!=0)&&((contorig[i]%picturewidth)>lowerlimitx)&&(itcontains((contorig[i]-1), contexp,nbexpanded)==false)&&(bisectSearch(finalcont, contorig[i]-1, nbfinalcont)==false))
	{if(mcol[contorig[i]-1]==1)  //{
	 {contexp[nbexpanded]=(contorig[i]-1); nbexpanded++;}
	
	}
//east
 if(((contorig[i]+1)%picturewidth!=0)&&(itcontains((contorig[i]+1), contexp,nbexpanded)==false)&&((contorig[i])%picturewidth<higherlimitx)&&(bisectSearch(finalcont, contorig[i]+1, nbfinalcont)==false))
	{if(mcol[contorig[i]+1]==1) 
	 {contexp[nbexpanded]=(contorig[i]+1); nbexpanded++;}
	}
 //north
 if((contorig[i]>(picturewidth-1))&&((contorig[i])/picturewidth>lowerlimity)&&(itcontains((contorig[i]-picturewidth), contexp,nbexpanded)==false)&&(bisectSearch(finalcont, contorig[i]-picturewidth,nbfinalcont)==false))
	{if(mcol[contorig[i]-picturewidth]==1)  
	 {contexp[nbexpanded]=(contorig[i]-picturewidth); nbexpanded++;}
	}
//south
 if((contorig[i]<(picturewidth*(pictureheight-1)))&&(itcontains((contorig[i]+picturewidth), contexp,nbexpanded)==false)&&((contorig[i])/picturewidth<higherlimity)&&(bisectSearch(finalcont, contorig[i]+picturewidth,nbfinalcont)==false))

	{if(mcol[contorig[i]+picturewidth]==1)  
	 {contexp[nbexpanded]=(contorig[i]+picturewidth); nbexpanded++;}
	}
}
cycle++;
if(cycle>1){Arrays.sort(contorig,0,nborig);
finalcont=merge(finalcont,nbfinalcont,contorig,nborig);
nbfinalcont= nbfinalcont+nborig;}
if(nbfinalcont>(int)(di*di*22)) break; 
}while((nborig>0)&&(cycle<di*4));//
  int ff=0;
Arrays.fill(piecepix,0X00000000);
//firstrow and colums of piece compared to picture
int extra= (piecewidth-di)/2;
int firstrow= ij/nbcolumns*di-extra;
 extra= (pieceheight-di)/2;
int firstcolumn= ij%nbcolumns*di-extra;
for ( ff=0;ff<nbfinalcont;ff++)
	{//msg2="placeff= "+finalcont[ff];repaint();
	int row =finalcont[ff]/picturewidth-firstrow;
	int column =finalcont[ff]%picturewidth-firstcolumn;
	piecepix[row*piecewidth+column]=pictpix[finalcont[ff]];}
img=createImage(new MemoryImageSource(piecewidth, pieceheight,piecepix,0,piecewidth));
return img;
}
public int[] merge(int[] finalcont,int nbfinalcont, int[] toAdd, int nborig)
  {	int ii=0,jj=0;
  int area=piecewidth*pieceheight;
	int[] mergedarray=new int[area];
	while ((ii<nbfinalcont)&&(jj<nborig))
		{if (finalcont[ii]<toAdd[jj]) {mergedarray[ii+jj]=finalcont[ii];ii++;}
		else {mergedarray[ii+jj]=toAdd[jj];jj++;}
		}
	while(jj<nborig){mergedarray[ii+jj]=toAdd[jj];jj++;}
	while(ii<nbfinalcont){mergedarray[ii+jj]=finalcont[ii];ii++;}
	//nbfinalcont=ii+jj;
    return mergedarray;
  }

public boolean itcontains(int k, int[]contexp, int nbexp)
 { for (int ik=nbexp-1; ik>=0; ik--)
    {if(k==contexp[ik]){return true;}
    }
return false;
}
public boolean bisectSearch(int[] myarray,int element,int nbelements)
{int lower =0; int high=nbelements;
  if(nbelements==0){ return false;}
  if ((element==myarray[0])||(element==myarray[high-1])) return true;
   if ((element<myarray[0])||(element>myarray[high-1])) return false;
   int cycle=0;
   while(((high-lower)>1)&&(cycle<50))
		{cycle++;
		int bisect=(lower+high)/2;
		if (myarray[bisect-1]==element) return true;
		else if  (myarray[bisect-1]<element) {lower=bisect;}
		else {high=bisect;}
		}
	return false;
	}
public boolean recursivebSearch(int[] myarray,int element,int lower,int high)
{	if((high-lower)<2) {return ((myarray[lower]==element)||( myarray[high-1]==element));}
	 if ((element<myarray[lower])||(element>myarray[high])) return false;
  	int bisect=(lower+high)/2;
	if (myarray[bisect]==element) return true;
	else if  (myarray[bisect]<element) {return  recursivebSearch(myarray, element,bisect,high);}
	else { recursivebSearch(myarray, element, lower, bisect);}
return false;}

public  void mousePressed(MouseEvent me)
{if(isloading==false) //&&(isreleased==false)) 
 { int px=me.getX();int  py=me.getY();
    for (int jj=0; jj<totalnbpieces; jj++)
   {if((px>PosX[jj])&&(px<(PosX[jj]+piecewidth))&&(py>PosY[jj])&&(py<(PosY[jj]+piecewidth)) )
     { int pixxy=py*swidth+px;
//      showStatus("got piece "+jj+", with "+PosY[jj]+" < "+py+"  < "+(PosY[jj]+piecewidth) );
      boolean opacity=checkForOpacity(jj, pixxy, piece[jj]);
      if (opacity==true)
       {    for(int g=0; g<=nbofgrps; g++)     
              {if(g==nbofgrps)
                        {dragpiece=true; piecedragged=jj;//groupdragged=-1; //jj is local variable
                          distancex=px-(PosX[jj]);distancey=py-(PosY[jj]);
                        }
              else if ((grp[g]).doescontain(jj)==true)
	  {groupdragged=g; draggroup=true;
        	  // int members[]=new int[grp[g].getsize()];
       	     int leader=(grp[g]).leaderr;
        	     distancex=px-(PosX[leader]); distancey=py-(PosY[leader]);
      //                 showStatus(" pressed  group  "+g+ "at distance"+distancex);
                       break;  }
             else{}; 
     }     
// try{ Thread.sleep(200);}catch(InterruptedException ie){showStatus("Interrupted");}
  }
else{};// dragpiece=false;groupdragged=-1;}  //opacity=false
}
else{};
}}
}

public void mouseDragged(MouseEvent me)
{int dX=me.getX();   int  dY=me.getY();
 if(dragpiece==true)
      {PosX[piecedragged]=dX -distancex;
       PosY[piecedragged]=dY -distancey;
     repaint();
      }
 else if (groupdragged>=0)
  {showStatus("in group dragging");
   int N=(grp[groupdragged]).sizeofgroup;
   int  Cont[]= new int[N]; int offX[]= new int[N]; int offY[]= new int[N]; 
   Cont=(grp[groupdragged]).getContent();
   int leader=  (grp[groupdragged]).leaderr;
    showStatus("groupdragged= " + groupdragged);              
     for(int pp=0; pp<N; pp++)
      {PosX[Cont[pp]]=dX -distancex+(((Cont[pp])%nbcolumns)-(leader%nbcolumns))*di;
       PosY[Cont[pp]]=dY -distancey +((Cont[pp])/nbcolumns-leader/nbcolumns)*di;
      }
   repaint();
  }
else{}
 }
public void mouseReleased(MouseEvent me)
{ int rX=me.getX();   int rY=me.getY();
  int itsgrp=-1;
if(dragpiece==true)
     { PosX[piecedragged]=rX -distancex;
       PosY[piecedragged]=rY -distancey;
     int targetL = piecedragged-1;  int targetU = piecedragged-nbcolumns;
     int targetR = piecedragged+1;  int targetD = piecedragged+nbcolumns;   
       //approach from below
     if((piecedragged>=nbcolumns)&&(Math.abs(PosY[piecedragged]-PosY[targetU]-di)<5)&&(Math.abs(PosX[piecedragged]-PosX[targetU])<5))
            {itsgrp=findGroup(targetU,piecedragged);   
   //          showStatus("groupU  "+targetU+"," +piecedragged);
            }
       //approach from the right     
    else  if((piecedragged%nbcolumns!=0)&&(Math.abs(PosX[piecedragged]-PosX[targetL]-di)<5)&&(Math.abs(PosY[piecedragged]-PosY[targetL])<5))
            {itsgrp= findGroup(targetL,piecedragged);   
    //       showStatus("groupL  "+targetL+"," +piecedragged);
            }
    else if(((piecedragged%nbcolumns)<(nbcolumns-1))&&(Math.abs(PosX[piecedragged]-PosX[targetR]+di)<5)&&(Math.abs(PosY[piecedragged]-PosY[targetR])<5))
            {itsgrp=findGroup(targetR,piecedragged);    
      //      showStatus("groupR  "+targetR+"," +piecedragged);
            }
    else if((piecedragged<(totalnbpieces- nbcolumns))&&(Math.abs(PosY[piecedragged]-PosY[targetD]+di)<5)&&(Math.abs(PosX[piecedragged]-PosX[targetD])<5))
            {itsgrp=findGroup(targetD,piecedragged);     
         //     showStatus("groupD  "+targetD+"," +piecedragged);
           }
        if(itsgrp>=0)           
         {int leader=(grp[itsgrp]).leaderr;
      // ask for repainting the whole new group
          int N=(grp[itsgrp]).sizeofgroup;
          int  Cont[]= new int[N]; 
          Cont=(grp[itsgrp]).getContent();
           for(int pp=0; pp<N; pp++)
                             {PosX[Cont[pp]]=rX -distancex+(((Cont[pp])%nbcolumns)-(leader%nbcolumns))*di;
                              PosY[Cont[pp]]=rY -distancey +((Cont[pp])/nbcolumns-leader/nbcolumns)*di;
                              }
          repaint();
        } dragpiece=false; piecedragged=-1;
    }
else if (groupdragged>=0)
{int N=(grp[groupdragged]).getsize();
 int Cont[]=new int[N];  int oldleaderr=grp[groupdragged].leaderr;
 Cont=(grp[groupdragged]).getContent();boolean foundGroup=false;
showStatus("Just released group   "+groupdragged);
 for(int i=0; i<N; i++)
   {int pp=Cont[i];  int targetU = pp-nbcolumns; int targetL = pp-1;
       int targetR = pp+1; int targetD = pp+nbcolumns;
     	
	   showStatus("distance R "+Math.abs(PosX[pp]-PosX[targetR]+di) );
	 if((pp>=nbcolumns)&&((grp[groupdragged]).doescontain(targetU)==false)&&(Math.abs(PosY[pp]-PosY[targetU]-di)<3)&&(Math.abs(PosX[pp]-PosX[targetU])<3))
                   {itsgrp=findGroup(targetU,pp); foundGroup=true; 
        showStatus("groupU  "+itsgrp +"containing"+targetU+"," +groupdragged);
  
 break;}
         //approach from the right    
       else  if((pp%nbcolumns!=0)&&((grp[groupdragged]).doescontain(targetL)==false)&&(Math.abs(PosX[pp]-PosX[targetL]-di)<3)&&(Math.abs(PosY[pp]-PosY[targetL])<3))
                   {itsgrp= findGroup(targetL,pp); foundGroup=true;
       showStatus("groupL  "+itsgrp +"containing"+targetL+"," +groupdragged);
 break;} 
      //approach from left
       else if(((pp%nbcolumns)<(nbcolumns-1))&&((grp[groupdragged]).doescontain(targetR)==false)&&(Math.abs(PosX[pp]-PosX[targetR]+di)<3)&&(Math.abs(PosY[pp]-PosY[targetR])<3))
                  {itsgrp=findGroup(targetR,pp); foundGroup=true;
				         showStatus("groupR  "+itsgrp +"containing"+targetR+"," +groupdragged);
break;} 
       else  if((pp<(totalnbpieces- nbcolumns))&&((grp[groupdragged]).doescontain(targetD)==false)&&(Math.abs(PosY[pp]-PosY[targetD]+di)<3)&&(Math.abs(PosX[pp]-PosX[targetD])<3))
                   {itsgrp=findGroup(targetD,pp); foundGroup=true;
      showStatus("groupD  "+itsgrp +"containing"+targetD+"," +groupdragged);

                     break;   }
    } // next pp
if(foundGroup==true){  // ask for repainting the whole new group
  int NN=(grp[itsgrp]).sizeofgroup;
   int  NCont[]= new int[NN]; 
    NCont=(grp[itsgrp]).getContent();
    int leader=(grp[itsgrp]).leaderr; distancex+=(oldleaderr%nbcolumns-leader%nbcolumns)*di;
	distancey+=(oldleaderr/nbcolumns-leader/nbcolumns)*di;
   for(int pp=0; pp<NN; pp++)
      {PosX[NCont[pp]]=rX -distancex+(((NCont[pp])%nbcolumns)-(leader%nbcolumns))*di;
        PosY[NCont[pp]]=rY -distancey +((NCont[pp])/nbcolumns-leader/nbcolumns)*di;
       }
 repaint();}else{};
draggroup=false; groupdragged=-1; 
 }  //end of draggroup=true 
draggroup=false; groupdragged=-1; 
}
public int findGroup (int target, int piecedragged)
{ int g=0;
 if(dragpiece==true)
   { int oldnbofgrps=nbofgrps;
       if(oldnbofgrps==0){grp[0]=new Group(target, piecedragged);
                                                distancex=correctionx+distancex; distancey=correctiony+distancey;
                                              nbofgrps++; return g;  }    //g=0;
      else          
      {for(g=0; g<oldnbofgrps; g++)
         { if((grp[g]).doescontain(target)==true) {(grp[g]).setin(piecedragged); 
           distancex=correctionx+distancex; distancey=correctiony+distancey;
           return g;}
         }
        g=oldnbofgrps;
        grp[oldnbofgrps]= new Group(target, piecedragged); 
         nbofgrps++;     distancex=correctionx+distancex; distancey=correctiony+distancey;
      
         showStatus("new groupnb "+g+"with"+target+ " , "+piecedragged);
    }  }
 else if(groupdragged>=0)     //put the existing groupmb in groupdragged
  {   for( g=0; g<nbofgrps; g++)
      {if((grp[g]).doescontain(target)==true)
           {int N=(grp[groupdragged]).getsize();
            int Cont[]=new int[N];
            Cont=(grp[groupdragged]).getContent();
            for(int dr=0; dr<N; dr++)
                {(grp[g]).setin(Cont[dr]);
                  }           
           (grp[groupdragged]).setToNull();
          return g;
          }
       else{};
     }  //end of  for
if (g==nbofgrps) (grp[groupdragged]).setin(target);
}
showStatus("found group = "+g); 
return g;}

public boolean checkForOpacity(int jj, int pixxy, Image p)
  {boolean opaque=true;
   int pxinpiece=pixxy%swidth-(PosX[jj]);        
   int pyinpiece=pixxy/swidth-(PosY[jj]);
     int pixels[]= new int[piecewidth*pieceheight];        
  PixelGrabber pg= new PixelGrabber(piece[jj],0,0, piecewidth, pieceheight, pixels, 0, piecewidth);
try{pg.grabPixels();} catch(InterruptedException iie){showStatus("Dont get Pixels");}
if( pixels[(pxinpiece+piecewidth*pyinpiece)]!=0x00000000)
 {opaque=true;}
else {opaque=false;}
return opaque;
}
public void mouseClicked(MouseEvent me){}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}
public void mouseMoved(MouseEvent me) {}

public void update(Graphics g)
{if(isloading==true)
   { if(screen==1)
          {if(mypicture==null){bg.drawString("Sorry, no picture to cut", 10, 50);}
           else{bg.drawImage(mypicture,(swidth-picturewidth)/2, (sheight-pictureheight)/2, this);}
          }
    else if (screen==2)
          {if(mould==null){bg.drawString("Sorry, no mould", 10, 100);}
           else{bg.drawImage(mould,(swidth-picturewidth)/2, (sheight-pictureheight)/2, this);}
      try{Thread.sleep(30);}catch(InterruptedException ie){};
   }
   else if(screen==3)
          {bg.drawImage(piece[ij],piececenter[ij]%picturewidth, (piececenter[ij]/picturewidth), this);
           bg.setColor(Color.black);bg.setFont(f);
           bg.drawString("cutting the puzzle, please wait",  50,250); 
          bg.drawString(msg1, 50,50); bg.drawString(msg2, 50,150);
	showStatus("Program written by annemarie.govindraj@gmail.com");
  }        }
 else if (isloading==false)
 {bg.setColor(lightblue); bg.fillRect(0,0,swidth,sheight);
  for(int i=0; i<totalnbpieces; i++)
 {bg.drawImage(piece[i],PosX[i], PosY[i],this);}
}
paint(g);
}
public void paint (Graphics g)
{g.drawImage(board,0,0,this);  
		}
      
class Group
{int content[]= new int[totalnbpieces];
int leaderr;                             //  leftmost piece because %
 int sizeofgroup;
Group (int target, int piecedragged)
 {sizeofgroup=1; content[0]=target;
  leaderr=target;
  setin(piecedragged);
 }
public void setin (int p)
  {content[sizeofgroup]=p; int oldleaderr=leaderr; correctionx=0;correctiony=0;
   if((p%nbcolumns)<(leaderr%nbcolumns)) {leaderr=p;}
   else{ correctionx=((p%nbcolumns)-(leaderr%nbcolumns))*di;correctiony=(p/nbcolumns-leaderr/nbcolumns)*di;} 
 sizeofgroup++;
  }

public int getsize(){return sizeofgroup;}
public int[] getContent(){return content;}
public void setToNull()
{for (int ik=0;ik<sizeofgroup;ik++) {content[ik]=-1;}
 sizeofgroup=0;
}
public boolean doescontain (int k)
 {for (int ik=0;ik<sizeofgroup;ik++) 
    {
       if(k==content[ik])  {return true;}
    }
  return false;
}}}