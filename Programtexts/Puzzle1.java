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
public class Puzzle1 extends Applet
implements MouseListener, Runnable, MouseMotionListener// ActionListener

{int ij, nbrows, nbcolumns, piecewidth, pieceheight,distancex, distancey, totalnbpieces;
int correctionx, correctiony, screen, swidth, sheight, picturewidth, pictureheight, di;
int piecedragged=-1, groupdragged=-1, nbofgrps=0;
int piececenter[]; int PosX[]; int PosY[];
int pictpix[]; int mouldpix[]; int mcol[];
Image board, mypicture=null, mould=null;  Graphics bg;
Image piece[]; 
Group[] grp= new Group[80]; 
 Thread walk;                   //only for beginning
 boolean isbusy=false; boolean isloading=true; boolean dragpiece=false, draggroup=false; 
Font f=new Font("Dialog", Font.BOLD,18);
Color lightblue = new Color(128,255,255);

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
addMouseListener(this); addMouseMotionListener(this); 
picturewidth=nbcolumns*di; pictureheight=nbrows*di;
piece= new Image[totalnbpieces];
pictpix=new int[picturewidth*pictureheight];
mouldpix=new int[picturewidth*pictureheight];
mcol=new int[picturewidth*pictureheight];
MediaTracker tracker = new MediaTracker(this);
try{if(getParameter("mode").equals("zip")){mypicture=getImage(getClass().getResource(getParameter("img")));}
else {mypicture=getImage(getDocumentBase(), getParameter("img"));}
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
   try{walk.sleep(200);}catch(InterruptedException ie){showStatus("Interrupted");}
  }
for(int ii=0;ii<totalnbpieces; ii++)
  { piececenter[ii]=defineCenter(ii);
    piece[ii]=definePiece(piececenter[ii]);
    screen=3; ij=ii; repaint();
//try{ Thread.sleep(200);}catch(InterruptedException ie){showStatus("Interrupted");}
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
{isbusy=true;
int lowerlimitx=center%picturewidth-(piecewidth/2);
int higherlimitx=center%picturewidth+(piecewidth/2);
int lowerlimity=center/picturewidth-(piecewidth/2);
int higherlimity=center/picturewidth+(piecewidth/2);
int area=piecewidth*pieceheight;
int piecepix[] = new int [area];
Image img;
int nborig=1, oldnborig=0, nbexpanded=1;
int contorig[]=new int [area]; int contexp[]=new int[area];
for(int j=0; j<area; j++)
{contorig[j]=-1; contexp[j]=-1;}
contexp[0]=center; int cycle=0;
do{ for(int i=0; i<nbexpanded; i++) {contorig[i]=contexp[i];}
       nborig=nbexpanded; if(nborig>(int)(di*di*2.2)) break;
       for (int i=oldnborig; i<nborig; i++)
{   // expanding to west
if((contorig[i]%picturewidth!=0)&&(itcontains((contorig[i]-1), contexp,nbexpanded)==false)&&((contorig[i])%picturewidth>lowerlimitx))
	{if(mcol[contorig[i]-1]==1) 
	 {contexp[nbexpanded]=(contorig[i]-1); nbexpanded++;}
	}
//east
 if(((contorig[i]+1)%picturewidth!=0)&&(itcontains((contorig[i]+1), contexp,nbexpanded)==false)&&((contorig[i])%picturewidth<higherlimitx))
	{if(mcol[contorig[i]+1]==1) 
	 {contexp[nbexpanded]=(contorig[i]+1); nbexpanded++;}
	}
 //north
 if((contorig[i]>(picturewidth-1))&&(itcontains((contorig[i]-picturewidth), contexp,nbexpanded)==false)&&((contorig[i])/picturewidth>lowerlimity))
	{if(mcol[contorig[i]-picturewidth]==1)  
	 {contexp[nbexpanded]=(contorig[i]-picturewidth); nbexpanded++;}
	}
//south
 if((contorig[i]<(picturewidth*(pictureheight-1)))&&(itcontains((contorig[i]+picturewidth), contexp,nbexpanded)==false)&&((contorig[i])/picturewidth<higherlimity))
	{if(mcol[contorig[i]+picturewidth]==1)  
	 {contexp[nbexpanded]=(contorig[i]+picturewidth); nbexpanded++;}
	}
cycle++; oldnborig=nborig;
//showStatus("nb= "+nbexpanded+ "  for center at  "+center+  "  in  "+cycle+ "  cycles");
}
}while((nbexpanded<area)&&(nborig<nbexpanded)&&(cycle<(di*di*2)));
int kp=0;
for (int kk=center-picturewidth*(piecewidth/2) - (piecewidth/2); kk<center+picturewidth*(piecewidth/2-1)+(piecewidth/2-1);kk=kk+picturewidth)
  {for(int kl=kk; kl<kk+(piecewidth); kl++,kp++)
{if(itcontains(kl,contexp,nbexpanded)==true) {piecepix[kp]=pictpix[kl];}
 else {piecepix[kp]=0x00000000;}
}}
img=createImage(new MemoryImageSource(piecewidth, pieceheight,piecepix,0,piecewidth));
isbusy=false;
return img;
}
public boolean itcontains(int k, int[]contexp, int nbexpanded)
 { for (int ik=0; ik<nbexpanded; ik++)
    {if(k==contexp[ik]){return true;}
    }
return false;
}
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
     if((piecedragged>=nbcolumns)&&(Math.abs(PosY[piecedragged]-PosY[targetU]-di)<3)&&(Math.abs(PosX[piecedragged]-PosX[targetU])<3))
            {itsgrp=findGroup(targetU,piecedragged);   
   //          showStatus("groupU  "+targetU+"," +piecedragged);
            }
       //approach from the right     
    else  if((piecedragged%nbcolumns!=0)&&(Math.abs(PosX[piecedragged]-PosX[targetL]-di)<3)&&(Math.abs(PosY[piecedragged]-PosY[targetL])<3))
            {itsgrp= findGroup(targetL,piecedragged);   
    //       showStatus("groupL  "+targetL+"," +piecedragged);
            }
    else if(((piecedragged%nbcolumns)<(nbcolumns-1))&&(Math.abs(PosX[piecedragged]-PosX[targetR]+di)<3)&&(Math.abs(PosY[piecedragged]-PosY[targetR])<3))
            {itsgrp=findGroup(targetR,piecedragged);    
      //      showStatus("groupR  "+targetR+"," +piecedragged);
            }
    else if((piecedragged<(totalnbpieces- nbcolumns))&&(Math.abs(PosY[piecedragged]-PosY[targetD]+di)<3)&&(Math.abs(PosX[piecedragged]-PosX[targetD])<3))
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
        }
         dragpiece=false; piecedragged=-1;
    }

else if (groupdragged>=0)
{int N=(grp[groupdragged]).getsize();
 int Cont[]=new int[N];
 Cont=(grp[groupdragged]).getContent();
showStatus("Just released group   "+groupdragged);
 for(int pp=0; pp<N; pp++)
   {  int targetU = pp-nbcolumns; int targetL = pp-1;
       int targetR = pp+1; int targetD = pp+nbcolumns;
      if((pp>=nbcolumns)&&((grp[groupdragged]).doescontain(targetU)==false)&&(Math.abs(PosY[pp]-PosY[targetU]-di)<3)&&(Math.abs(PosX[pp]-PosX[targetU])<3))
                   {itsgrp=findGroup(targetU,pp); break;}
         //approach from the right    
       else  if((pp%nbcolumns!=0)&&((grp[groupdragged]).doescontain(pp)==false)&&(Math.abs(PosX[pp]-PosX[targetL]-di)<3)&&(Math.abs(PosY[pp]-PosY[targetL])<3))
                   {itsgrp= findGroup(targetL,pp); break;} 
      //approach from left
       else if(((pp%nbcolumns)<(nbcolumns-1))&&((grp[groupdragged]).doescontain(pp)==false)&&(Math.abs(PosX[pp]-PosX[targetR]+di)<3)&&(Math.abs(PosY[pp]-PosY[targetR])<3))
                  {itsgrp=findGroup(targetR,pp);break;} 
       else  if((pp<(totalnbpieces- nbcolumns))&&((grp[groupdragged]).doescontain(pp)==false)&&(Math.abs(PosY[pp]-PosY[targetD]+di)<3)&&(Math.abs(PosX[pp]-PosX[targetD])<3))
                   {itsgrp=findGroup(targetD,pp);
                     break;   }
    } // next pp
  // ask for repainting the whole new group
  int NN=(grp[itsgrp]).sizeofgroup;
   int  NCont[]= new int[NN]; 
    NCont=(grp[itsgrp]).getContent();
    int leader=(grp[itsgrp]).leaderr;
   for(int pp=0; pp<NN; pp++)
      {PosX[NCont[pp]]=rX -distancex+(((NCont[pp])%nbcolumns)-(leader%nbcolumns))*di;
        PosY[NCont[pp]]=rY -distancey +(NCont[pp]/nbcolumns-leader/nbcolumns)*di;
       }
 repaint();
draggroup=false; groupdragged=-1; 
 }  //end of draggroup=true 
//draggroup=false; groupdragged=-1; }
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
           {int N=(grp[g]).getsize();
            int Cont[]=new int[N];
            Cont=(grp[g]).getContent();
            for(int dr=0; dr<N; dr++)
                {(grp[groupdragged]).setin(Cont[dr]);
                  }           
           (grp[g]).setToNull();
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
{g.drawImage(board,0,0,this);}
      
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