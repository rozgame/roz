import processing.core.*; 
import processing.xml.*; 

import ddf.minim.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class sketch_130404a extends PApplet {

//\u8a9e\u6cd5\u501f\u7528

Minim minim;
AudioPlayer songBGM;
AudioPlayer songSE;

//\u756b\u9762\u69cb\u6210
PImage imgCharaMain_L_r; //\u4e3b\u4eba\u516c (L\u6a21\u5f0f\u9762\u53f3)
PImage imgCharaMain_L_l; //\u4e3b\u4eba\u516c (L\u6a21\u5f0f\u9762\u5de6)
PImage imgCharaMain_T_r; //\u4e3b\u4eba\u516c (T\u6a21\u5f0f\u9762\u53f3)
PImage imgCharaMain_T_l; //\u4e3b\u4eba\u516c (T\u6a21\u5f0f\u9762\u5de6)
PImage imgBG; //\u80cc\u666f
PImage imgT; //\u6253\u64ca\u9ede
PImage imgTs; //\u6253\u64ca\u9ede small
float x = 0; //\u4e3b\u4eba\u516c\u7684\u4f4d\u7f6e x
float y = 400; //\u4e3b\u4eba\u516c\u7684\u4f4d\u7f6e y
int xT = 100;
int yT = 400;
float tspeed = 5.0f; //\u6253\u64ca\u9ede\u8dd1\u7684\u901f\u5ea6

//\u72c0\u614b\u8a18\u9304
char modeControlVar = 'L';  // L = Limbo, T = Taiko (\u7576\u524d\u6a21\u5f0f)
char charaControlVar = 'r'; // r = right, l = left  (\u4e3b\u4eba\u516c\u9762\u671d\u65b9\u5411)
char hitRank = 'N'; //\u6253\u64ca\u8a55\u7b49 A, B, C, N (None)

//\u9375\u76e4\u5224\u5b9a
boolean[] keys; //\u591a\u91cdkey\u6aa2\u67e5

//\u7269\u7406\u6a21\u64ec
float gravity = 0.35f; //\u91cd\u529b\u52a0\u901f\u5ea6
float jump = 7; //\u8df3\u7684\u52a0\u901f\u5ea6
float speed = 0; //\u901f\u5ea6 (\u65b9\u5411\uff1a\u6b63\u5f80\u4e0b\uff0c\u8ca0\u5f80\u4e0a)

//\u5b57\u9ad4
PFont myFont1; //\u5fae\u8edf\u6b63\u9ed1
PFont myFont2; //Calibri

public void setup() {
  size(800, 600); //\u8996\u7a97\u5927\u5c0f (800x600)
  // Make a new instance of a PImage by loading an image file
  imgCharaMain_L_r = loadImage("img/D_L_r_75x150.png");
  imgCharaMain_L_l = loadImage("img/D_L_l_75x150.png");
  imgCharaMain_T_r = loadImage("img/D_T_r_75x150.png");
  imgCharaMain_T_l = loadImage("img/D_T_l_75x150.png");
  imgBG = loadImage("img/BG.png");
  imgT = loadImage("img/T.png");
  imgTs = loadImage("img/T_45x45.png");

  keys = new boolean[7];
  keys[0] = false; //UP
  keys[1] = false; //LEFT
  keys[2] = false; //RIGHT
  keys[3] = false; //DOWN
  keys[4] = false; //SHIFT
  keys[5] = false; //P
  keys[6] = false; //T

  myFont1 = createFont("\u5fae\u8edf\u6b63\u9ed1\u9ad4", 12);
  myFont2 = createFont("Calibri", 12);
  
  //\u8a9e\u6cd5\u501f\u7528
  minim = new Minim(this);  
  songBGM = minim.loadFile("08 estha - Remember the Reminiscence.mp3",512);
  songSE = minim.loadFile("SNARE_IR1.aif",512);
  songBGM.play();
}

public void draw() {
  //background(225);
  image(imgBG, 0, 0);//\u756b\u80cc\u666f
  modeShift();       //\u6a21\u5f0f\u5224\u5b9a

  charaControl();    //\u6c7a\u5b9a\u4e3b\u4eba\u516c\u4f4d\u7f6e
  charaDraw();       //\u4e3b\u4eba\u516c\u63cf\u7e6a

  testSwitch();      //\u6e2c\u8a66\u6a21\u5f0f
  physicsSim();      //\u7269\u7406\u6a21\u64ec
}

//\u7269\u7406\u6a21\u64ec
public void physicsSim()
{
  // Add speed to location.
  y += speed;
  // Add gravity to speed.
  speed += gravity;
  // If square reaches the bottom
  // Reverse speed
  if (y > 400) { //\u649e\u5230\u5730\u9762 \u2192 \u5f48\u8df3
    // Multiplying by -0.95 instead of -1 slows the square down each time it bounces (by decreasing speed).  
    // This is known as a "dampening" effect and is a more realistic simulation of the real world (without it, a ball would bounce forever).
    speed = speed * -0.7f;
    if (speed > -0.5f) {
      y = 400;
      speed = 0;
    }
  }
}

//\u9375\u76e4\u5224\u5b9a
public void keyPressed()
{
  charaControl ();
  if (key == CODED) {
    if (keyCode == UP)
      keys [0] = true;
    if (keyCode == LEFT) {
      keys [1] = true;
      charaControlVar = 'l';
    }
    if (keyCode == RIGHT) {
      keys [2] = true;
      charaControlVar = 'r';
    }
    if (keyCode == DOWN) {
      keys [3] = true;
      modeT_Hit();
    }
    if (keyCode == SHIFT && !keys[5]) {
      keys [4] = (keys[4]) ? false : true;  //\u5728 keyPressed() \u5224\u5b9a\uff0c\u5728 draw() \u57f7\u884c
    }
  } 
  else if (key == 'p' || key == 'P') {
    keys [5] = (keys[5]) ? false : true;
    pauseControl();                         //\u5728 keyPressed() \u5224\u5b9a\uff0c\u5728 keyPressed() \u57f7\u884c
  } 
  else if (key == 't' || key == 'T') {
    keys [6] = (keys[6]) ? false : true;    //\u5728 keyPressed() \u5224\u5b9a\uff0c\u5728 draw() \u57f7\u884c
  }
  else if (keys[6] && (key == '=' || key == '+')) tspeed += 0.1f;
  else if (keys[6] && (key == '-' || key == '_')) tspeed -= 0.1f;

}
public void keyReleased()
{
  if (key == CODED) {
    if (keyCode == UP)
      keys [0] = false;
    if (keyCode == LEFT)
      keys [1] = false;
    if (keyCode == RIGHT)
      keys [2] = false;
    if (keyCode == DOWN)
      keys [3] = false;
  }
}

//\u89d2\u8272\u63a7\u5236 (\u66ab\u505c\u6642\u4e0d\u6703run\u5230)
public void charaControl () {
  if (keys[0] && y==400)
    speed -= jump; //\u7d66\u4ed6\u4e00\u500b\u52a0\u901f\u5ea6
  if (keys[1])
    x -= 1.5f;
  if (keys[2])
    x += 1.5f;
}
//\u89d2\u8272\u7e6a\u88fd
public void charaDraw () {
  if (modeControlVar=='L') {      // L Mode //
    if (charaControlVar == 'r') image(imgCharaMain_L_r, x, y);
    if (charaControlVar == 'l') image(imgCharaMain_L_l, x, y);
  }
  else if (modeControlVar=='T') { // T Mode //
    if (charaControlVar == 'r') {
      image(imgCharaMain_T_r, x, y); 
      image(imgTs, x+70, y);
    }
    if (charaControlVar == 'l') {
      image(imgCharaMain_T_l, x, y);
      image(imgTs, x-40, y);
    }
  }
}

//\u6a21\u5f0f\u5207\u63db\u5224\u5b9a
public void modeShift () {
  textFont(myFont2);
  fill(255, 255, 255);
  modeControlVar = keys[4] ? 'T' : 'L';
  text(keys[4] ? "T Mode" : "L Mode", 10, 580); //\u5370\u51fa\u6a21\u5f0f\u540d\u7a31
  if (keys[4]) modeT_Draw();
}
//\u6253\u64ca\u7e6a\u88fd
public void modeT_Draw () {
  //---\u4e0a\u534a\u90e8\uff1a\u7e6a\u88fd\u6301\u7e8c\u5f80\u4e0b\u8dd1\u7684\u6253\u64ca\u9ede
  image(imgT, charaControlVar=='r' ? x+60 : x-50, yT+=tspeed);
  if (yT>600) {
    yT=0; //\u756b\u9762\u908a\u7de3 loop
    hitRank = 'N'; //\u8a55\u7b49\u6b78 0
  }
  //---\u4e0b\u534a\u90e8\uff1a\u5370\u51fa\u8a55\u7b49\u6587\u5b57
  if (abs(y-yT)<100 && hitRank!='N') {
    textFont(myFont1);
    if (hitRank == 'A') {
      
      fill(255, 255, 0);
      text("GREAT", charaControlVar=='r' ? x+150 : x-100, y+15);
    }
    if (hitRank == 'B') {
      fill(255, 255, 255);
      text("GOOD", charaControlVar=='r' ? x+150 : x-100, y+15);
    }
    if (hitRank == 'C') {
      fill(0, 0, 255);
      text("BAD", charaControlVar=='r' ? x+150 : x-100, y+15);
    }
  }
}
//\u6253\u64ca\u5224\u5b9a (called by keyPressed)
public void modeT_Hit () {
    {
      songSE.rewind(); // set playback position to the beginning of your sound
      songSE.play(); //play intro wav
    }
  if (abs(y-yT)<10) {
    hitRank = 'A';
  }
  else if (abs(y-yT)<25) {
    hitRank = 'B';
  }
  else if (abs(y-yT)<100) {
    hitRank = 'C';
  }
}

//\u66ab\u505c\u63a7\u5236
public void pauseControl () {
  if (keys[5]) {
    fill(0, 150);
    rect(0, 0, 800, 600);
    fill(255, 255);
    text("Pause", 10, 20);
    noLoop();
    explain();
  } 
  else {
    loop();
  }
}

//\u6e2c\u8a66\u6a21\u5f0f
public void testSwitch() {
  if (keys[6]) {
    fill(0, 0, 0);
    textFont(myFont2);
    text("TEST", 14, 552);
    noFill();
    stroke(0, 0, 0);
    rect(10, 540, 30, 15); // [TEST] \u6846
    rect(x, y, 75, 150);
    rect(charaControlVar=='r' ? x+70 : x-40, y, 45, 45);
    rect(charaControlVar=='r' ? x+60 : x-50, yT, 65, 65);
    
    if(charaControlVar=='r') {
      stroke(0, 0, 255); //Blue
      line(x+75, y-100, x+105, y-100);
      line(x+75, y+100, x+105, y+100);
      stroke(255, 255, 255); //White
      line(x+80, y-25, x+100, y-25);
      line(x+80, y+25, x+100, y+25);
      stroke(255, 255, 0); //Yellow
      line(x+85, y-10, x+95, y-10);
      line(x+85, y+10, x+95, y+10);
    } else if(charaControlVar=='l') {
      stroke(0, 0, 255); //Blue
      line(x-5, y-100, x-35, y-100);
      line(x-5, y+100, x-35, y+100);
      stroke(255, 255, 255); //White
      line(x-10, y-25, x-30, y-25);
      line(x-10, y+25, x-30, y+25);
      stroke(255, 255, 0); //Yellow
      line(x-15, y-10, x-25, y-10);
      line(x-15, y+10, x-25, y+10);
    } //\u7169\u6b38
    
    //\u8abf\u6253\u64ca\u9ede\u79fb\u52d5\u901f\u5ea6\u901f\u5ea6
    if(modeControlVar=='T'){
      text("- ("+tspeed+") +",55,580);
    }
    stroke(255, 255, 255);
  }
}

//\u8aaa\u660e
public void explain() {
  noFill();
  stroke(255, 255, 255);
  rect(50, 50, 170, 170);
  fill(255, 255, 255);
  textFont(myFont1);
  text("\u2190: \u5de6", 70, 80);
  text("\u2192: \u53f3", 70, 100);
  text("\u2191: \u8df3", 70, 120);
  text("\u2193: \u6253\u7bc0\u594f", 70, 140);
  text("Shift: L/T \u6a21\u5f0f\u5207\u63db", 70, 160);
  text("P: \u66ab\u505c (pause)", 70, 180);
  text("T: \u6e2c\u8a66 (test)", 70, 200);
  text("+-: \u8abf\u901f\u5ea6", 130, 80);

  text("\u203b \u6ce8\u97f3\u6703\u5e72\u64fe\uff0c\u5efa\u8b70\u5207\u63db\u5230 Shift \u6c92\u4f5c\u7528\u7684\u8f38\u5165\u6cd5", 250, 80);
  text("\u203b \u6253\u64ca\u5224\u5b9a\u5206\u4e09\u7a2e Great, Good, Bad", 250, 100);
  text("\u203b \u6e2c\u8a66\u529f\u80fd\u53ef\u79c0\u51fa\u5716\u7247\u7684\u6846\uff0c\u53e6\u5916\u53ef\u8abf\u901f\u5ea6", 250, 120);
  text("\u203b ---\u76ee\u524d\u554f\u984c---", 250, 140);
  text("\u203b \u64ad\u97f3\u6a02\u6703\u9813", 250, 160);
  text("\u203b \u8df3\u8e8d\u66b4\u8d70\u3042\u308a (\u53ea\u662f\u597d\u73a9\uff0c\u53ef\u96a8\u6642\u62ff\u6389)", 250, 180);
  text("\u203b \u6253\u64ca\u5224\u5b9a\u9ede\u4e2d\u5fc3\u76ee\u524d\u662f\u6b6a\u7684", 250, 200);
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "sketch_130404a" });
  }
}
