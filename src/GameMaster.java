/**
 * ゲームの進行自体を取り仕切るクラス
 * ・キーボード入力
 * ・ゲーム内の各オブジェクトの管理
 * ・ゲーム画面の描画
 *
 * @author Keisuke
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.applet.*;

public class GameMaster extends Canvas implements KeyListener {
  // ■ フィールド変数
  Image        buf;   // 仮の画面としての buffer に使うオブジェクト(Image クラス)
  Graphics2D   buf_gc;// buffer のグラフィックスコンテキスト (gc) 用オブジェクト
  Dimension    d;     // ウィンドウの大きさを管理するオブジェクト
  private int  imgW, imgH; // キャンバスの大きさ

  Color bgCol1 = new Color(60,0,240);
  Color bgCol2 = new Color(0,250,0);
  int TopScore = 0;
  static int Score_1P = 0;
  static int Score_2P = 0;
  private int i;
  private int spring;

  Flipper ftr;  // フリッパー
  Ball ball;
  private int chipNum  = 3; // 丸いチップの数
  private int sChipNum  = 20; // 丸い小さいチップの数
  private int SBombNum = 6; // 丸いオブジェクトの数
  private int diamNum = 8;
  public static int mode = -1; // -1: タイトル画面, -2: ゲームオーバー, 0〜 ゲームステージ
  Chip  chip[] = new Chip[chipNum];
  Chip sChip[] = new Chip[sChipNum];
  ScoreBomb SBomb[] = new ScoreBomb[SBombNum];
  Diam  diam[] = new Diam[diamNum];
  int chipPos[][] = {{219+4,231+4,243+4},{222+4,243+4,264+4}};

  Image imgTitle, imgGaOv;
  AudioClip muTitle;

  GameMaster(int imgW, int imgH) { // コンストラクタ（アプレット本体が引数。ゲームの初期化を行う）
    this.imgW = imgW; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    this.imgH = imgH; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    setSize(imgW, imgH); // 描画領域のサイズを設定

    addKeyListener(this); // キーリスナー登録

    ftr = new Flipper(); // フリッパーのオブジェクトの実体を作成
    ball = new Ball(imgW, imgH); // ボールのオブジェクトの実体を作成
    for (i=0;i<chipNum;i++) chip[i]  = new Chip(chipPos[0][i], chipPos[1][i], 7);
    for (i=0;i<sChipNum/2;i++) sChip[i]  = new Chip(226,110+15*i,4);
    for (i=sChipNum/2;i<sChipNum;i++) sChip[i]  = new Chip(726,120+15*(i-sChipNum/2),4);
    SBomb[0] = new ScoreBomb(455,284,new Color(255,200,200));
    SBomb[1] = new ScoreBomb(392,190,new Color(255,200,200));
    SBomb[2] = new ScoreBomb(518,190,new Color(255,200,200));
    SBomb[3] = new ScoreBomb(455,275,new Color(255,150,0));
    for (i=0;i<3;i++) diam[i]= new Diam(477+42*(i-1),154);
    for (i=3;i<diamNum;i++) diam[i]= new Diam(477+42*(i-5),110);

    imgTitle = getToolkit().getImage("img/Title.jpg");    // タイトル画面の画像のインポート
    imgGaOv  = getToolkit().getImage("img/GameOver.jpg"); // ゲームオーバー画面の画像のインポート
    muTitle  = Applet.newAudioClip(getClass().getResource("SE/title.wav")); // ゲーム開始時の音源のインポート
  }

  // ■ メソッド
  // コンストラクタ内で createImage を行うと peer の関連で
  // nullpointer exception が返ってくる問題を回避するために必要
  public void addNotify(){
    super.addNotify();
    buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
    buf_gc = (Graphics2D)buf.getGraphics();
  }

  // ■ メソッド (Canvas)
  public void paint(Graphics g) {
    buf_gc.setColor(Color.black);       // gc の色を黒に
    buf_gc.fillRect( 0, 0, imgW, imgH); // gc を使って白の四角を描く（背景の初期化）
    switch (mode) {
    case -2: // ゲームオーバー画面（スペースキーを押されたらタイトル画面）
      buf_gc.setColor(Color.white); // ゲームオーバー画面を描く
      buf_gc.drawString("PLAYER 1", imgW/2-20, imgH/2-10);
      buf_gc.drawString("GAME  OVER", imgW/2-20, imgH/2+10);
      buf_gc.drawImage(imgGaOv,0,0,imgW,imgH,0,0,1280,960,this); // ゲームオーバー画面の画像を描画
      ball.hp=2; Score_1P=0; Score_2P=0; // ボールの数とスコアを初期化
      break;
    case -1: // タイトル画面（スペースキーを押されたらゲーム開始）
      buf_gc.setColor(Color.pink); // タイトル画面を描く
      buf_gc.drawString("PIN", imgW/2-40, imgH/2-20);
      buf_gc.drawString("BALL", imgW/2-20, imgH/2);
      buf_gc.setColor(Color.cyan);
      buf_gc.drawString("Press SPACE bar", imgW/2-20, imgH/2+40);
      buf_gc.drawImage(imgTitle,0,0,imgW,imgH,0,0,1280,960,this); // タイトル画面の画像を描画
      muTitle.play(); // ゲーム開始時の音源を再生
      ObjReSet();
      break;
    case 0: // 下の面
      map2();
      Side();
      for (i=3;i<diamNum;i++) if(Math.pow(ball.x+ball.w-diam[i].x-diam[i].w,2)+Math.pow(ball.y+ball.w-diam[i].y-diam[i].w,2)>Math.pow(ball.w+diam[i].w,2)) diam[i].revive();
      ftr.move(buf_gc,imgW, imgH);
      for (i=0;i<sChipNum;i++) sChip[i].move(buf_gc,imgW, imgH);
      for (i=1;i<4;i++) SBomb[i].move(buf_gc,imgW, imgH);
      ball.move(buf_gc,imgW, imgH);
      // buf_gc.drawString(String.valueOf(spring),700,300); // ばねのたわみと発射速度
      buf_gc.fillRect(757,421+spring,30,90-spring);
      if (ball.y>imgH) {
        ball.hp--; ball.revive();
        if(ball.hp<0) mode = -2; // ゲーム終了
        ObjReSet();
      }
      for (i=0;i<sChipNum;i++) if(sChip[i].hp>0) ball.collisionCheck(sChip[i],100);
      for (i=1;i<4;i++) ball.collisionCheck(SBomb[i]);
      for (i=3;i<diamNum;i++) ball.collisionCheck(diam[i],1000);
      ChangeMode();
      break;
    case 1: // 上の面
      spring=0;
      map1();
      Side();
      for (i=0;i<3;i++) if(Math.pow(ball.x+ball.w-diam[i].x-diam[i].w,2)+Math.pow(ball.y+ball.w-diam[i].y-diam[i].w,2)>Math.pow(ball.w+diam[i].w,2)) diam[i].revive();
      ftr.move(buf_gc,imgW, imgH);
      for (i=0;i<chipNum;i++) chip[i].move(buf_gc,imgW, imgH);
      SBomb[0].move(buf_gc,imgW, imgH);
      ball.move(buf_gc,imgW, imgH);
      for (i=0;i<chipNum;i++ ) if(chip[i].hp>0) ball.collisionCheck(chip[i],100);
      ball.collisionCheck(SBomb[0]);
      for (i=0;i<2;i++) ball.collisionCheck(diam[i*2],500);
      ball.collisionCheck(diam[1],1000);
      ChangeMode();
      break;
    case 2: // 第3面(今回実装なし)
      map3();
      Side();
      ball.move(buf_gc,imgW, imgH);
      ChangeMode();
    }
    if (ftr.f5flag==true) { // ボールをランダムに動かす管理者用コマンド
      ball.revive();
      ball.x=(int)(Math.random()*500)+200;
      ball.y=(int)(Math.random()*500);
      ball.dx=(int)(Math.random()*6)-3;
    }
    if (Score_1P>TopScore) TopScore = Score_1P; // トップスコアの更新
    g.drawImage(buf, 0 ,0 ,this); // 表の画用紙に裏の画用紙 (buffer) の内容を貼り付ける
  }
  public void ObjReSet() { // オブジェクトの再配置
    ball.revive();
    for (i=0;i<chipNum;i++) chip[i].revive();
    for (i=0;i<sChipNum;i++) sChip[i].revive();
    for (i=0;i<4;i++) SBomb[i].revive();
  }
  public void ChangeMode() { // 面を強制的に変える管理者用コマンド
    if (ftr.f1flag==true) {
      mode = 0;
    } else if (ftr.f2flag==true) {
      mode = 1;
    } else if (ftr.f3flag==true) {
      mode = 2;
    }
  }
  public void Side() { // 左側の得点などを表示するエリア
    buf_gc.setColor(Color.white);
    buf_gc.drawString("<TOP>", 80, 100);
    buf_gc.drawString(String.valueOf(TopScore), 80, 110);
    buf_gc.drawString("<Score>", 80, 200);
    buf_gc.drawString(String.valueOf(Score_1P), 80, 210);
    //buf_gc.drawString("<Player 2>", 80, 280);
    //buf_gc.drawString(String.valueOf(Score_2P), 80, 290);
    buf_gc.drawString("<BALL>", 80, 440);
    buf_gc.drawString(String.valueOf(ball.hp), 100, 450);
    buf_gc.fillRect(200,0,10,imgH);
    buf_gc.fillRect(790,0,10,imgH);
    if (mode == 2) buf_gc.setColor(bgCol2);
    else buf_gc.setColor(bgCol1);
    buf_gc.fillRect(202,0,6,imgH);
    buf_gc.fillRect(792,0,6,imgH);
  }
  public void map1() { // 上面のマップ
    /* フリッパー左 */
    buf_gc.setColor(bgCol1);
    buf_gc.fillPolygon(new int[] {246,246,270,318,318}, new int[] {600,432,408,456,600},5);
    buf_gc.setColor(Color.white);
    buf_gc.drawPolygon(new int[] {246,246,270,318,318}, new int[] {600,432,408,456,600},5);
    /* 外郭 */
    buf_gc.setColor(bgCol1);
    buf_gc.fillRect(210,0,580,72);
    buf_gc.fillPolygon(new int[] {210,210,350}, new int[] {222,72,72},3);
    buf_gc.fillPolygon(new int[] {640,790,790}, new int[] {72,72,222},3);
    buf_gc.fillPolygon(new int[] {210,246,246,210}, new int[] {408,372,302,240},4);
    buf_gc.setColor(Color.black);
    buf_gc.fill(new QuadCurve2D.Double(210,222,210,72,350,72)); // 左上外円
    buf_gc.fill(new QuadCurve2D.Double(640,72,790,72,790,222)); // 右上外円
    buf_gc.setColor(Color.white);
    buf_gc.drawPolygon(new int[] {210,246,246,210}, new int[] {408,372,302,240},4);
    buf_gc.draw(new QuadCurve2D.Double(210,222,210,72,350,72)); // 左上外円
    buf_gc.drawLine(350,72,640,72);
    buf_gc.draw(new QuadCurve2D.Double(640,72,790,72,790,222)); // 右上外円
    /* 左上の島 */
    buf_gc.setColor(bgCol1);
    buf_gc.fill(new QuadCurve2D.Double(246,222,246,108,350,108)); // 左上内円
    buf_gc.fillPolygon(new int[] {350,358,420,420,318,282,246},new int[] {108,108,144,182,284,284,222},7);
    buf_gc.setColor(Color.white);
    buf_gc.draw(new QuadCurve2D.Double(246,222,246,108,350,108)); // 左上内円
    buf_gc.drawLine(350,108,358,108); buf_gc.drawLine(358,108,420,144); buf_gc.drawLine(420,144,420,182);
    buf_gc.drawLine(420,182,318,284); buf_gc.drawLine(318,284,282,284); buf_gc.drawLine(282,284,246,222);
    /* フリッパー右 */
    buf_gc.setColor(bgCol1);
    buf_gc.fillPolygon(new int[] {642,642,690,714,738,714,730,750,750,720,672,630,540,540,602,640,720+1,750+1,754,754},
                       new int[] {600,456,408,432,408,384,384,364,194,158,158,182,182,144,108,108,158-1,194-1,222,600},20);
    buf_gc.fill(new QuadCurve2D.Double(640,108,754,108,754,222)); // 右上外円
    buf_gc.setColor(Color.black);
    buf_gc.fill(new QuadCurve2D.Double(714,432,738,432,738,408)); // 再発射
    buf_gc.fillPolygon(new int[] {680,720,750,750},new int[] {158,158,194,230},4);
    buf_gc.setColor(Color.white);
    buf_gc.drawLine(642,600,642,456); buf_gc.drawLine(642,456,690,408); buf_gc.drawLine(690,408,714,432);
    buf_gc.draw(new QuadCurve2D.Double(714,432,738,432,738,408)); // 再発射
    buf_gc.drawLine(738,408,714,384); buf_gc.drawLine(714,384,730,384); buf_gc.drawLine(730,384,750,364);
    buf_gc.drawLine(750,364,750,194); buf_gc.drawLine(750,194,720,158); // 右上内円
    buf_gc.drawLine(720,158,672,158); buf_gc.drawLine(672,158,630,182); buf_gc.drawLine(630,182,540,182);
    buf_gc.drawLine(540,182,540,144); buf_gc.drawLine(540,144,602,108); buf_gc.drawLine(602,108,640,108);
    buf_gc.draw(new QuadCurve2D.Double(640,108,754,108,754,222)); // 右上外円
    buf_gc.drawLine(754,222,754,600);
    /* 右島 */
    buf_gc.setColor(bgCol2); buf_gc.fillOval(690,324,24,24);
    buf_gc.setColor(Color.white); buf_gc.drawOval(690,324,24,24);
    buf_gc.setColor(bgCol2); buf_gc.fill(new QuadCurve2D.Double(714,218,714,194,690,194));
    buf_gc.fillPolygon(new int[] {690,648,690,690,702,714,714},new int[] {194,218,260,336,348,336,218},7);
    buf_gc.setColor(Color.white);
    buf_gc.drawLine(690,194,648,218); buf_gc.drawLine(648,218,690,260); buf_gc.drawLine(690,260,690,336);
    buf_gc.drawLine(714,336,714,218); buf_gc.draw(new QuadCurve2D.Double(714,218,714,194,690,194));
    /* 上のちょんちょん */
    for (i=0;i<2;i++) dia(456+42*i,144);
    buf_gc.drawString("1000",480-13,108);
    for (i=0;i<2;i++) buf_gc.drawString("500",428+84*i,128);
    /* 上の逆流防止弁 */
    buf_gc.setColor(new Color(200,200,200));
    buf_gc.fillRect(350-4,72,4,36);
    buf_gc.fillRect(640,72,4,36);
  }

  public void map2(){ // 下面のマップ
    /* 左上島 */
    buf_gc.setColor(bgCol1);
    buf_gc.fillPolygon(new int[] {246,246,264,282,318,318},new int[] {-1,17,48,48,12,-1},6);
    buf_gc.setColor(Color.white);
    buf_gc.drawPolygon(new int[] {246,246,264,282,318,318},new int[] {-1,17,48,48,12,-1},6);
    /* 左端 */
    buf_gc.setColor(bgCol1);
    buf_gc.fillPolygon(new int[] {210,210,318},new int[] {492,600,600},3); // 左下
    buf_gc.fillPolygon(new int[] {210,250,226,226,210},new int[] {340,300,276,76,48},5);
    buf_gc.setColor(Color.white);
    buf_gc.drawLine(210,492,318,600); // 左下
    buf_gc.drawLine(210,340,250,300); buf_gc.drawLine(250,300,226,276);
    buf_gc.drawLine(226,276,226,76); buf_gc.drawLine(226,76,210,48);
    /* 右端 */
    buf_gc.setColor(bgCol1);
    buf_gc.fillPolygon(new int[] {750,750,642},new int[] {492,600,600},3); // 右下
    buf_gc.fillRect(750,410,40,190);
    buf_gc.fillPolygon(new int[] {750,750,710,734,734,642,642,754,754},
                       new int[] {410,340,300,276,104, 12,  0,  0,410},9);
    buf_gc.setColor(Color.white);
    buf_gc.drawLine(750,492,642,600); // 右下
    buf_gc.drawLine(750,600,750,340); buf_gc.drawLine(750,340,710,300); buf_gc.drawLine(710,300,734,276);
    buf_gc.drawLine(734,276,734,104); buf_gc.drawLine(734,104,642,12); buf_gc.drawLine(642,12,642,0);
    buf_gc.drawLine(754,410,754,0);
    buf_gc.setColor(Color.black); buf_gc.fillRect(754,416,36,100);
    /* レーン */
    buf_gc.setColor(Color.pink);
    buf_gc.fillPolygon(new int[] {286,286,318+4},new int[] {379-3,416-3,434-3},3); // 左三角
    buf_gc.fillPolygon(new int[] {674,674,642-4},new int[] {379-3,416-3,434-3},3); // 右三角
    buf_gc.setColor(Color.white);
    buf_gc.fillPolygon(new int[] {246,246,318+4,320+4,250,250},new int[] {379-3,432-3,474-3,470+1-3,428+1-3,379-3},6); // 左
    buf_gc.fillPolygon(new int[] {714,714,642-4,640-4,710,710},new int[] {379-3,432-3,474-3,470+1-3,428+1-3,379-3},6); // 右
    buf_gc.drawPolygon(new int[] {286,286,318+4},new int[] {379-3,416-3,434-3},3); // 左三角
    buf_gc.drawPolygon(new int[] {674,674,642-4},new int[] {379-3,416-3,434-3},3); // 右三角
    /* 上のちょんちょん */
    for (i=0;i<6;i++) dia(372+42*i,100);
  }

  public void map3() { // 第3面のマップ(今回実装なし)
    buf_gc.setColor(bgCol2);
    buf_gc.fillRect(210,0,580,150);
    buf_gc.setColor(new Color(255,150,0));
    buf_gc.fillRect(240,0,520,150);
  }

  void dia(int x,int y) {
    buf_gc.setColor(new Color(100,200,250)); buf_gc.fillOval(x,y,6,6); buf_gc.fillOval(x,y+23,6,6);
    buf_gc.setColor(Color.white); buf_gc.drawOval(x,y,6,6); buf_gc.drawOval(x,y+23,6,6);
    buf_gc.setColor(new Color(100,200,250)); buf_gc.fillRect(x,y+3,6,23);
    buf_gc.setColor(Color.white); buf_gc.drawLine(x,y+3,x,y+26); buf_gc.drawLine(x+6,y+3,x+6,y+26);
  }

  // ■ メソッド (Canvas)
  public void update(Graphics gc) { // repaint() に呼ばれる
    paint(gc);
  }

  // ■ メソッド (KeyListener)
  public void keyTyped(KeyEvent ke) {} // 今回は使わないが実装は必要

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_F: // [F]キーが押されたら
      ftr.lflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_J: // [J]キーが押されたら
      if(mode==0&&spring<40&&750<ball.x&&370<ball.y)spring++; // 徐々にばねを沈ませる
      ftr.rflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_SPACE: // スペースキーが押されたら
      ftr.sflag = true; // フラグを立てる
      if (mode<0) mode++;
      break;
    case KeyEvent.VK_F1: // [F1]キーが押されたら
      ftr.f1flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F2: // [F2]キーが押されたら
      ftr.f2flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F3: // [F3]キーが押されたら
      ftr.f3flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F4: // [F4]キーが押されたら
      ftr.f4flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F5: // [F5]キーが押されたら
      ftr.f5flag = true; // フラグを立てる
      break;
    }
  }

  // ■ メソッド (KeyListener)
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_F: // [F]キーが離されたら
      ftr.lflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_J: // [J]キーが離されたら
      ftr.rflag = false; // フラグを降ろす
      if(spring>0&&ball.y>=410-35) ball.dy=-spring; // ばねの力をボールに与える
      spring=0; // ばねの沈みを元に戻す
      break;
    case KeyEvent.VK_SPACE: // スペースキーが離されたら
      ftr.sflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_F1: // [F1]キーが離されたら
      ftr.f1flag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_F2: // [F2]キーが離されたら
      ftr.f2flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F3: // [F3]キーが離されたら
      ftr.f3flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F4: // [F4]キーが離されたら
      ftr.f4flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F5: // [F5]キーが離されたら
      ftr.f5flag = false; // フラグを立てる
      break;
    }
  }
}
