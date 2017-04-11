//語法借用
import ddf.minim.*;
Minim minim;
AudioPlayer songBGM;
AudioPlayer songSE;

//畫面構成
PImage imgCharaMain_L_r; //主人公 (L模式面右)
PImage imgCharaMain_L_l; //主人公 (L模式面左)
PImage imgCharaMain_T_r; //主人公 (T模式面右)
PImage imgCharaMain_T_l; //主人公 (T模式面左)
PImage imgBG; //背景
PImage imgT; //打擊點
PImage imgTs; //打擊點 small
float x = 0; //主人公的位置 x
float y = 400; //主人公的位置 y
int xT = 100;
int yT = 400;
float tspeed = 5.0; //打擊點跑的速度

//狀態記錄
char modeControlVar = 'L';  // L = Limbo, T = Taiko (當前模式)
char charaControlVar = 'r'; // r = right, l = left  (主人公面朝方向)
char hitRank = 'N'; //打擊評等 A, B, C, N (None)

//鍵盤判定
boolean[] keys; //多重key檢查

//物理模擬
float gravity = 0.35; //重力加速度
float jump = 7; //跳的加速度
float speed = 0; //速度 (方向：正往下，負往上)

//字體
PFont myFont1; //微軟正黑
PFont myFont2; //Calibri

void setup() {
  size(800, 600); //視窗大小 (800x600)
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

  myFont1 = createFont("微軟正黑體", 12);
  myFont2 = createFont("Calibri", 12);
  
  //語法借用
  minim = new Minim(this);  
  songBGM = minim.loadFile("08 estha - Remember the Reminiscence.mp3",512);
  songSE = minim.loadFile("SNARE_IR1.aif",512);
  songBGM.play();
}

void draw() {
  //background(225);
  image(imgBG, 0, 0);//畫背景
  modeShift();       //模式判定

  charaControl();    //決定主人公位置
  charaDraw();       //主人公描繪

  testSwitch();      //測試模式
  physicsSim();      //物理模擬
}

//物理模擬
void physicsSim()
{
  // Add speed to location.
  y += speed;
  // Add gravity to speed.
  speed += gravity;
  // If square reaches the bottom
  // Reverse speed
  if (y > 400) { //撞到地面 → 彈跳
    // Multiplying by -0.95 instead of -1 slows the square down each time it bounces (by decreasing speed).  
    // This is known as a "dampening" effect and is a more realistic simulation of the real world (without it, a ball would bounce forever).
    speed = speed * -0.7;
    if (speed > -0.5) {
      y = 400;
      speed = 0;
    }
  }
}

//鍵盤判定
void keyPressed()
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
      keys [4] = (keys[4]) ? false : true;  //在 keyPressed() 判定，在 draw() 執行
    }
  } 
  else if (key == 'p' || key == 'P') {
    keys [5] = (keys[5]) ? false : true;
    pauseControl();                         //在 keyPressed() 判定，在 keyPressed() 執行
  } 
  else if (key == 't' || key == 'T') {
    keys [6] = (keys[6]) ? false : true;    //在 keyPressed() 判定，在 draw() 執行
  }
  else if (keys[6] && (key == '=' || key == '+')) tspeed += 0.1;
  else if (keys[6] && (key == '-' || key == '_')) tspeed -= 0.1;

}
void keyReleased()
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

//角色控制 (暫停時不會run到)
void charaControl () {
  if (keys[0] && y==400)
    speed -= jump; //給他一個加速度
  if (keys[1])
    x -= 1.5;
  if (keys[2])
    x += 1.5;
}
//角色繪製
void charaDraw () {
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

//模式切換判定
void modeShift () {
  textFont(myFont2);
  fill(255, 255, 255);
  modeControlVar = keys[4] ? 'T' : 'L';
  text(keys[4] ? "T Mode" : "L Mode", 10, 580); //印出模式名稱
  if (keys[4]) modeT_Draw();
}
//打擊繪製
void modeT_Draw () {
  //---上半部：繪製持續往下跑的打擊點
  image(imgT, charaControlVar=='r' ? x+60 : x-50, yT+=tspeed);
  if (yT>600) {
    yT=0; //畫面邊緣 loop
    hitRank = 'N'; //評等歸 0
  }
  //---下半部：印出評等文字
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
//打擊判定 (called by keyPressed)
void modeT_Hit () {
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

//暫停控制
void pauseControl () {
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

//測試模式
void testSwitch() {
  if (keys[6]) {
    fill(0, 0, 0);
    textFont(myFont2);
    text("TEST", 14, 552);
    noFill();
    stroke(0, 0, 0);
    rect(10, 540, 30, 15); // [TEST] 框
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
    } //煩欸
    
    //調打擊點移動速度速度
    if(modeControlVar=='T'){
      text("- ("+tspeed+") +",55,580);
    }
    stroke(255, 255, 255);
  }
}

//說明
void explain() {
  noFill();
  stroke(255, 255, 255);
  rect(50, 50, 170, 170);
  fill(255, 255, 255);
  textFont(myFont1);
  text("←: 左", 70, 80);
  text("→: 右", 70, 100);
  text("↑: 跳", 70, 120);
  text("↓: 打節奏", 70, 140);
  text("Shift: L/T 模式切換", 70, 160);
  text("P: 暫停 (pause)", 70, 180);
  text("T: 測試 (test)", 70, 200);
  text("+-: 調速度", 130, 80);

  text("※ 注音會干擾，建議切換到 Shift 沒作用的輸入法", 250, 80);
  text("※ 打擊判定分三種 Great, Good, Bad", 250, 100);
  text("※ 測試功能可秀出圖片的框，另外可調速度", 250, 120);
  text("※ ---目前問題---", 250, 140);
  text("※ 播音樂會頓", 250, 160);
  text("※ 跳躍暴走あり (只是好玩，可隨時拿掉)", 250, 180);
  text("※ 打擊判定點中心目前是歪的", 250, 200);
}
