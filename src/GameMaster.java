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

  Color bgCol1 = new Color(60,0,240); // 背景色1 (青色)
  Color bgCol2 = new Color(0,250,0);  // 背景色2 (緑色)
  int TopScore = 0;        // ベストスコアを保存しておく変数
  static int Score_1P = 0; // 現在のスコアを保存しておく変数
  private int i;      // for文などで利用する変数
  private int spring; // ばねのたわみ量を保存しておく変数

  Flipper ftr;  // フリッパー
  Ball ball;    // ボール
  private int chipNum  = 3;   // 丸いチップの数
  private int sChipNum  = 20; // 丸い小さいチップの数
  private int SBombNum = 6;   // 丸いオブジェクトの数
  private int diamNum = 11;   // 通過判定オブジェクトの数
  public static int mode = -1; // -1: タイトル画面, -2: ゲームオーバー, 0〜 ゲームステージ
  Chip  chip[] = new Chip[chipNum];           // 丸いチップの宣言
  Chip sChip[] = new Chip[sChipNum];          // 丸い小さいチップの宣言
  ScoreBomb SBomb[] = new ScoreBomb[SBombNum];// 丸いオブジェクトの宣言
  Diam  diam[] = new Diam[diamNum];           // 通過判定オブジェクトの宣言
  int chipPos[][] = {{219+4,231+4,243+4},{222+4,243+4,264+4}}; // chipの座標を保存しておく配列
  int delay = -1;

  Image imgTitle, imgGaOv, imgLet, imgSpr;
  AudioClip muTitle, bomb, get, fill;

  GameMaster(int imgW, int imgH) { // コンストラクタ（アプレット本体が引数。ゲームの初期化を行う）
    this.imgW = imgW; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    this.imgH = imgH; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    setSize(imgW, imgH); // 描画領域のサイズを設定

    addKeyListener(this); // キーリスナー登録

    ftr = new Flipper(); // フリッパーのオブジェクトの実体を作成
    ball = new Ball(imgW, imgH); // ボールのオブジェクトの実体を作成
    for (i=0;i<chipNum;i++) chip[i]  = new Chip(chipPos[0][i], chipPos[1][i], 7);       // 丸いチップの実体を作成
    for (i=0;i<sChipNum/2;i++) sChip[i]  = new Chip(226,110+15*i,4);                    // 丸い小さいチップの実体を作成
    for (i=sChipNum/2;i<sChipNum;i++) sChip[i]  = new Chip(726,120+15*(i-sChipNum/2),4);// 丸い小さいチップの実体を作成
    SBomb[0] = new ScoreBomb(455,284,new Color(255,40,120));     // 丸いオブジェクトの実体を指定した色・座標で作成
    SBomb[1] = new ScoreBomb(392,190,new Color(255,40,120));     // 丸いオブジェクトの実体を指定した色・座標で作成
    SBomb[2] = new ScoreBomb(518,190,new Color(255,40,120));     // 丸いオブジェクトの実体を指定した色・座標で作成
    SBomb[3] = new ScoreBomb(455,275,new Color(255,150,0));      // 丸いオブジェクトの実体を指定した色・座標で作成
    for (i=0;i<3;i++) diam[i]= new Diam(477+42*(i-1),154);       // 通過判定オブジェクトの実体を指定した座標で作成
    for (i=3;i<diamNum;i++) diam[i]= new Diam(477+42*(i-6),110); // 通過判定オブジェクトの実体を指定した座標で作成
    diam[10] = new Diam(717,260);                                // 通過判定オブジェクトの実体を指定した座標で作成

    imgTitle = getToolkit().getImage("../img/Title.jpg");    // タイトル画面の画像のインポート
    imgGaOv  = getToolkit().getImage("../img/GameOver.jpg"); // ゲームオーバー画面の画像のインポート
    imgLet   = getToolkit().getImage("../img/letter.png");   // 文字数字のインポート
    imgSpr   = getToolkit().getImage("../img/spring.jpg");   // ばね画像のインポート
    muTitle  = Applet.newAudioClip(getClass().getResource("SE/title.wav")); // ゲーム開始時の音源のインポート
    bomb     = Applet.newAudioClip(getClass().getResource("SE/bomb.wav")); // 音源bomb.wavをインポート
    get      = Applet.newAudioClip(getClass().getResource("SE/get.wav")); // 音源get.wavをインポート
    fill     = Applet.newAudioClip(getClass().getResource("SE/fill.wav")); // 音源fill.wavをインポート
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
      buf_gc.drawImage(imgGaOv,0,0,imgW,imgH,0,0,1280,960,this); // ゲームオーバー画面の画像を描画
      ball.hp=2; Score_1P=0; // ボールの数とスコアを初期化
      break;
    case -1: // タイトル画面（スペースキーを押されたらゲーム開始）
      buf_gc.drawImage(imgTitle,0,0,imgW,imgH,0,0,1280,960,this); // タイトル画面の画像を描画
      muTitle.play(); // ゲーム開始時の音源を再生
      ObjReSet(); // オブジェクトを再配置
      break;
    case 0: // 下の面
      if (Score_1P>=500000) bgCol1=new Color(255,255,0);
      map2(); // マップの描画
      Side(); // 得点などの描画
      for (i=3;i<diamNum-1;i++) if(Math.pow(ball.x+ball.w-diam[i].x-diam[i].w,2)+Math.pow(ball.y+ball.w-diam[i].y-diam[i].w,2)>Math.pow(ball.w+diam[i].w,2)) diam[i].revive();
      // もしボールが範囲外に出たら,通過判定オブジェクトを生き返らせる
      // ** オブジェクトの移動＆描画 **
      ftr.move(buf_gc,imgW, imgH); // フリッパーの描画
      for (i=0;i<sChipNum;i++) sChip[i].move(buf_gc,imgW, imgH); // 丸いチップの描画
      for (i=1;i<4;i++) SBomb[i].move(buf_gc,imgW, imgH);        // 丸い小さいチップの描画
      for (i=3;i<diamNum-1;i++) diam[i].move(buf_gc,i);          // 通過判定オブジェクトの描画
      ball.move(buf_gc,imgW,imgH,bomb); // ボールの描画
      // buf_gc.drawString(String.valueOf(spring),700,300); // ばねのたわみと発射速度
      buf_gc.drawImage(imgSpr,757,421+spring,787,511,0,0,33,79,this); // 発射装置のばね画像を貼る
      if (ball.y>imgH) { // ボールが画面下に落ちていったら
        ball.hp--; ball.revive(); // 残りボールを減らしてボールを発射装置に再配置
        if(ball.hp<0) mode = -2; // ボールの残機がなければゲーム終了
        ObjReSet(); // オブジェクトを再配置
      }
      // ** ボールとオブジェクトの衝突判定 **
      for (i=0;i<sChipNum;i++) if(sChip[i].hp>0) ball.collisionCheck(sChip[i],100,get); // 丸い小さいチップが残っていたら衝突判定
      for (i=1;i<4;i++) ball.collisionCheck(SBomb[i],bomb); // 丸いオブジェクトとの衝突判定
      for (i=3;i<diamNum-1;i++) if(ball.collisionCheck(diam[i],1000,get)==true) diam[i].card=true; // 通過の判定をし、通過しているならカードを表にする
      if (delay > 0) delay--; // // 待ち時間を１減らす
      else if (delay==0) { // 待ち時間が0になったら
        for (i=3;i<diamNum-1;i++) diam[i].card=false; // カードをすべて裏にする
        delay --; // 待ち時間を１減らす
      } else if (diam[3].card==true&&diam[4].card==true&&diam[5].card==true&&diam[6].card==true&&diam[7].card==true&&diam[8].card==true&&diam[9].card==true) {
        Score_1P += 10000; // スコアを1万点加算
        fill.play(); // 音源fill.wavを再生
        delay = 140; // 持ち時間を設定
      }
      break;
    case 1: // 上の面
      spring=0; // ばねのたわみを0にもどす
      if (Score_1P>=500000) bgCol1=new Color(255,255,0); // 50満点以上の時、背景色を黄色にする
      map1(); // マップの描画
      Side(); // 得点などの描画
      for (i=0;i<3;i++) if(Math.pow(ball.x+ball.w-diam[i].x-diam[i].w,2)+Math.pow(ball.y+ball.w-diam[i].y-diam[i].w,2)>Math.pow(ball.w+diam[i].w,2)) diam[i].revive();
      if(Math.pow(ball.x+ball.w-diam[10].x-diam[10].w,2)+Math.pow(ball.y+ball.w-diam[10].y-diam[10].w,2)>Math.pow(ball.w+diam[10].w,2)) diam[10].revive();
      // ** オブジェクトの移動＆描画 **
      ftr.move(buf_gc,imgW,imgH); // フリッパーの移動＆描画
      for (i=0;i<chipNum;i++) chip[i].move(buf_gc,imgW, imgH); // 丸いチップの描画
      SBomb[0].move(buf_gc,imgW, imgH); // 丸いオブジェクトの描画
      ball.move(buf_gc,imgW,imgH,bomb); // ボールの描画
      // ** ボールとオブジェクトの衝突判定 **
      for (i=0;i<chipNum;i++ ) if(chip[i].hp>0) ball.collisionCheck(chip[i],100,get); // 丸いチップが残っていたら衝突判定
      ball.collisionCheck(SBomb[0],bomb); // 丸いオブジェクトとの衝突判定
      for (i=0;i<2;i++) ball.collisionCheck(diam[i*2],500,get); // 上の500と書いてある通過点
      ball.collisionCheck(diam[1],1000,get); // 上の1000と書いてある通過点
      ball.collisionCheck(diam[10],500,get); // 右のレーンの通過判定
      break;
    }
    if (Score_1P>999999) Score_1P=999999; // スコアの上限(999999)を設定
    if (Score_1P>TopScore) TopScore = Score_1P; // トップスコアの更新
    /* 管理者用コマンド */
    if (ftr.f1flag==true) mode = 1;      // F1キーが押されたらモード1(上の面)に
    else if (ftr.f2flag==true) mode = 0; // F2キーが押されたらモード2(下の面)に
    else if (ftr.f3flag==true) {         // F3キーが押されたら(発表用コマンド)
      ball.x = 650;   ball.y = 330;      // ボールを指定した座標に移動
      ball.dx = 1.5;  ball.dy = 0.5;     // ボールに右下向きの力を与える
    } else if (ftr.f4flag==true) Score_1P+=10000;  // F4キーが押されたらスコアを1万加算(発表用コマンド)
    else if (ftr.f5flag==true) {                   // F5キーが押されたら
      ball.x=(int)(Math.random()*500)+210; ball.y=(int)(Math.random()*500); // x=210~709,y=0~499の範囲にランダムにボールを移動
      ball.dx=(int)(Math.random()*7)-3; ball.dy=0; // -3~3の速度をx方向に与え、y方向の速度を0に
    } else if (ftr.f6flag==true) {                 // F6キーが押されたら
      for (i=4;i<diamNum-1;i++) diam[i].card=true; // 1番左以外のカードを表にする
    } else if (ftr.f7flag==true) {                 // F7キーが押されたら
      ball.x=342;  ball.y=200; // ボールを指定した座標に移動
      ball.dx=0; ball.dy=-6;   // ボールに上向きの力を与える
    }

    g.drawImage(buf, 0 ,0 ,this); // 表の画用紙に裏の画用紙 (buffer) の内容を貼り付ける
  }

  public void ObjReSet() { // オブジェクトの再配置
    ball.revive(); // ボールを速度を0にして発射装置内に
    for (i=0;i<chipNum;i++) chip[i].revive();   // 丸いチップの再配置
    for (i=0;i<sChipNum;i++) sChip[i].revive(); // 丸い小さいチップの再配置
    for (i=0;i<4;i++) SBomb[i].revive();        // 丸いオブジェクトの再配置
    for (i=3;i<diamNum;i++) diam[i].card=false; // すべてのカードを裏にする
    delay = -1;
  }

  public void Side() { // 左側の得点などを表示するエリア
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.drawRect(57,140,100,25); buf_gc.drawRect(57,240,100,25); buf_gc.drawRect(125,450,32,25); // gcに□を描画(得点を囲む四角)
    buf_gc.setColor(new Color(100,200,250)); // gcの色を水色に設定
    buf_gc.fillPolygon(new int[] {82,70,82},new int[] {114,122,130},3); // TOPの左の三角
    buf_gc.drawImage(imgLet,84,110,100,134,168,0,178,13,this);  // T の画像を貼り付け
    buf_gc.drawImage(imgLet,100,110,116,134,124,0,132,13,this); // O の画像を貼り付け
    buf_gc.drawImage(imgLet,116,110,132,134,133,0,141,13,this); // P の画像を貼り付け
    buf_gc.fillPolygon(new int[] {134,146,134},new int[] {114,122,130},3); // TOPの右の三角
    DispNum(TopScore,80,140,6); // ベストスコアの数字を画像で貼り付け
    buf_gc.setColor(new Color(100,200,250));
    buf_gc.fillPolygon(new int[] {66,54,66},new int[] {214,222,230},3); // SCOREの左の三角
    buf_gc.drawImage(imgLet,68,210,84,234,159,0,167,13,this);   // S の画像を貼り付け
    buf_gc.drawImage(imgLet,84,210,100,234,17,0,25,13,this);    // C の画像を貼り付け
    buf_gc.drawImage(imgLet,100,210,116,234,124,0,132,13,this); // O の画像を貼り付け
    buf_gc.drawImage(imgLet,116,210,132,234,150,0,158,13,this); // R の画像を貼り付け
    buf_gc.drawImage(imgLet,132,210,148,234,35,0,43,13,this);   // E の画像を貼り付け
    buf_gc.fillPolygon(new int[] {150,162,150},new int[] {214,222,230},3); // SCOREの右の三角
    DispNum(Score_1P,80,240,6); // 現在スコアの数字を画像で貼り付け
    buf_gc.setColor(new Color(100,200,250)); // gcの色を水色に設定
    buf_gc.fillPolygon(new int[] {82,70,82},new int[] {424,432,440},3); // BALLの左の三角
    buf_gc.drawImage(imgLet,84,420,100,444,8,0,16,13,this); // B の画像を貼り付け
    buf_gc.drawImage(imgLet,100,420,116,444,0,0,8,13,this); // A の画像を貼り付け
    for (i=0;i<2;i++) buf_gc.drawImage(imgLet,116+i*16,420,132+i*16,444,97,0,105,13,this); // L の画像を貼り付け
    buf_gc.fillPolygon(new int[] {150,162,150},new int[] {424,432,440},3); // BALLの右の三角
    DispNum(ball.hp,80,450,2); // 残りのボールの数を画像で貼り付け
    buf_gc.setColor(Color.white); // gcの色を白色に設定
    buf_gc.fillRect(200,0,10,imgH); buf_gc.fillRect(790,0,10,imgH); // ゲーム境界線(外)の描画
    buf_gc.setColor(bgCol1); // gcの色を青色に設定
    buf_gc.fillRect(202,0,6,imgH); buf_gc.fillRect(792,0,6,imgH);   // ゲーム境界線(内)の描画
  }

  public void map1() { // 上面のマップ
    /* フリッパー左 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fillPolygon(new int[] {246,246,270,318,318}, new int[] {600,432,408,456,600},5); // フリッパー左側の内側を背景色1で埋める
    ball.LineColl(246,432,246,600,buf_gc); ball.Line45Coll(246,432,270,408,buf_gc); //左
    ball.Line135Coll(270,408,318,456,buf_gc); ball.LineColl(318,456,318,600,buf_gc); // 右中
    /* 外郭 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fillRect(210,0,580,72); // gcに■を描画
    buf_gc.fillPolygon(new int[] {210,210,350}, new int[] {222,72,72},3);
    buf_gc.fillPolygon(new int[] {640,790,790}, new int[] {72,72,222},3);
    buf_gc.fillPolygon(new int[] {210,246,246,210}, new int[] {408,372,302,240},4);
    buf_gc.setColor(Color.black); // gcの色を黒色に設定
    buf_gc.fill(new QuadCurve2D.Double(210,222,210,72,350,72)); // 左上外円
    buf_gc.fill(new QuadCurve2D.Double(640,72,790,72,790,222)); // 右上外円
    buf_gc.setColor(Color.white); // gcの色を白色に設定
    ball.Line225Coll(210,408,246,372,buf_gc); // 左斜め45下
    ball.LineColl(246,302,246,372,buf_gc); // 左中直線
    ball.Line135Coll(210,240,246,302,buf_gc); //左斜め45上
    buf_gc.draw(new QuadCurve2D.Double(210,222,210,72,350,72)); // 左上外円
    buf_gc.drawLine(350,72,640,72); // 上限の直線
    buf_gc.draw(new QuadCurve2D.Double(640,72,790,72,790,222)); // 右上外円
    /* 左上の島 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fill(new QuadCurve2D.Double(246,222,246,108,350,108)); // 左上内円
    buf_gc.fillPolygon(new int[] {350,358,420,420,318,282,246},new int[] {108,108,144,182,284,284,222},7); // 左上の島の内側を背景色1で埋める
    buf_gc.setColor(Color.white); // gcの色を白色に設定
    buf_gc.draw(new QuadCurve2D.Double(246,222,246,108,350,108)); // 左上内円
    ball.LineColl(350,108,358,108,buf_gc); ball.Line135Coll(358,108,420,144,buf_gc); // 上
    ball.LineColl(420,144,420,182,buf_gc); ball.Line225Coll(318,284,420,182,buf_gc); // 右
    ball.LineColl(282,284,318,284,buf_gc); ball.Line315Coll(246,222,282,284,buf_gc); // 下
    /* フリッパー右 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fillPolygon(new int[] {642,642,690,714,738,714,730,750,750,720,672,630,540,540,602,640,720+1,750+1,754,754},
                       new int[] {600,456,408,432,408,384,384,364,194,158,158,182,182,144,108,108,158-1,194-1,222,600},20); // 下の白線の内側を背景色1で埋める
    buf_gc.fill(new QuadCurve2D.Double(640,108,754,108,754,222)); // 右上外円
    buf_gc.setColor(Color.black); // gcの色を黒色に設定
    buf_gc.fillPolygon(new int[] {680,720,750,750},new int[] {158,158,194,230},4);
    ball.LineColl(642,456,642,600,buf_gc); // フリッパー右直線
    ball.Line45Coll(642,456,690,408,buf_gc); ball.Line135Coll(690,408,714,432,buf_gc); // 右下／＼
    ball.Line45Coll(714,432,738,408,buf_gc); ball.Line315Coll(714,384,738,408,buf_gc); // 右下>
    ball.LineColl(714,384,730,384,buf_gc); ball.Line45Coll(730,384,750,364,buf_gc);
    ball.LineColl(750,194,750,364,buf_gc); ball.Line315Coll(720,158,750,194,buf_gc); // 右上内円
    ball.LineColl(672,158,720,158,buf_gc); ball.Line225Coll(630,182,672,158,buf_gc);
    ball.LineColl(540,182,630,182,buf_gc); ball.LineColl(540,144,540,182,buf_gc);
    ball.Line45Coll(540,144,602,108,buf_gc); ball.LineColl(602,108,640,108,buf_gc);
    buf_gc.draw(new QuadCurve2D.Double(640,108,754,108,754,222)); // 右上外円
    ball.LineColl(754,222,754,600,buf_gc); // 右直線
    /* 右島 */
    ball.ballColl(690,324,12,buf_gc,bgCol2);// 右島下
    buf_gc.setColor(bgCol2);  // gcの色を背景色2に設定
    buf_gc.fill(new QuadCurve2D.Double(714,218,714,194,690,194)); //  // 右島右上曲線内側を背景色2で埋める
    buf_gc.fillPolygon(new int[] {690,648,690,690,702,714,714},new int[] {194,218,260,336,348,336,218},7); // 右島内側を背景色2で埋める
    buf_gc.setColor(Color.white); // gcの色を白色に設定
    ball.Line45Coll(648,218,690,194,buf_gc); ball.Line315Coll(648,218,690,260,buf_gc); // 右島／＼
    ball.LineColl(690,260,690,336,buf_gc); ball.LineColl(714,218,714,336,buf_gc); // 左右直線
    buf_gc.draw(new QuadCurve2D.Double(714,218,714,194,690,194)); // 右島右上曲線描画
    if(690-15<ball.x&&ball.y<218+15) ball.ballColl(666,194,24); // 右島右上当たり判定
    buf_gc.drawString("500",684,184); buf_gc.drawLine(732,260,732,280); // 島上オブジェ
    /* 上のちょんちょん */
    for (i=0;i<2;i++) dia(456+42*i,144); // 上の小さい水色のオブジェクト
    buf_gc.drawString("1000",480-13,108); // gcに"1000"を描画
    for (i=0;i<2;i++) buf_gc.drawString("500",428+84*i,128); // gcに"1000"を描画
    buf_gc.setColor(new Color(100,200,250)); // gcの色を水色に設定
    buf_gc.fillPolygon(new int[] {470,480,490},new int[] {112,122,112},3); // "1000"の下に▼を描画
    for (i=0;i<2;i++) buf_gc.fillPolygon(new int[] {428+84*i,438+84*i,448+84*i},new int[] {132,142,132},3); // "500"の下に▼を描画
    /* 上の逆流防止弁 */
    buf_gc.setColor(new Color(200,200,200));  // gcの色を灰色に設定
    buf_gc.fillRect(350-4,72,4,36); buf_gc.fillRect(640,72,4,36);  // gcに■を描画
  }

  public void map2(){ // 下面のマップ
    /* 左上島 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fillPolygon(new int[] {246,246,264,282,318,318},new int[] {-1,17,48,48,12,-1},6); // 六角形内側を背景色1で埋める
    ball.LineColl(246,-30,246,17,buf_gc); ball.Line315Coll(246,17,264,48,buf_gc); // 左+下
    ball.LineColl(264,48,282,48,buf_gc); // 中
    ball.Line225Coll(282,48,318,12,buf_gc); ball.LineColl(318,-30,318,12,buf_gc); // 右
    /* 左端 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fillPolygon(new int[] {210,210,318},new int[] {492,600,600},3); // 左下
    buf_gc.fillPolygon(new int[] {210,250,226,226,210},new int[] {340,300,276,76,48},5); // 五角形内側を背景色1で埋める
    ball.Line135Coll(210,48,226,76,buf_gc); ball.LineColl(226,76,226,276,buf_gc);
    ball.Line135Coll(226,276,250,300,buf_gc); ball.Line225Coll(210,340,250,300,buf_gc);
    ball.Line135Coll(210,492,318,600,buf_gc); // 左下斜め45
    /* 右端 */
    buf_gc.setColor(bgCol1); // gcの色を背景色1に設定
    buf_gc.fillPolygon(new int[] {750,750,642},new int[] {492,600,600},3); // 右下
    buf_gc.fillRect(750,410,40,190);
    buf_gc.fillPolygon(new int[] {750,750,710,734,734,642,642,754,754},
                       new int[] {410,340,300,276,104, 12,  0,  0,410},9); // 下の白線の内側を背景色1で埋める
    buf_gc.setColor(Color.white); // gcの色を白色に設定
    ball.LineColl(642,0,642,12,buf_gc); ball.Line315Coll(642,12,734,104,buf_gc);
    ball.LineColl(734,104,734,276,buf_gc); ball.Line45Coll(710,300,734,276,buf_gc);
    ball.Line315Coll(710,300,750,340,buf_gc); ball.LineColl(750,340,750,492,buf_gc);
    buf_gc.drawLine(754,410,754,0); ball.Line45Coll(642,600,750,492,buf_gc); // 右下斜め45
    buf_gc.setColor(Color.black); buf_gc.fillRect(754,416,36,100); // gcに黒色の■を描画
    /* レーン */
    buf_gc.setColor(new Color(255,40,120)); // gcの色を桃色に設定
    buf_gc.fillPolygon(new int[] {286,286,318+4},new int[] {379-8,416-8,434-8},3); // 左三角
    buf_gc.fillPolygon(new int[] {674,674,642-4},new int[] {379-8,416-8,434-8},3); // 右三角
    buf_gc.setColor(Color.white); // gcの色を白色に設定
    buf_gc.fillPolygon(new int[] {246,246,318+4,320+4,250,250},new int[] {379-8,432-3,474-3,470+1-3,428+1-3,379-8},6); // 左
    buf_gc.fillPolygon(new int[] {714,714,642-4,640-4,710,710},new int[] {379-8,432-3,474-3,470+1-3,428+1-3,379-8},6); // 右
    buf_gc.drawPolygon(new int[] {286,286,318+4},new int[] {379-8,416-8,434-8},3); // 左三角
    buf_gc.drawPolygon(new int[] {674,674,642-4},new int[] {379-8,416-8,434-8},3); // 右三角
    for (i=-1;i<7;i++) dia(372+42*i,100); // 上のちょんちょん
  }

  // ■ 上の小さい水色のオブジェクトを表示させるメソッド
  void dia(int x,int y) {
    ball.ballColl(x,y,3,buf_gc,new Color(100,200,250)); // 円の描画&当たり判定
    ball.ballColl(x,y+23,3,buf_gc,new Color(100,200,250)); // 円の描画&当たり判定
    buf_gc.setColor(new Color(100,200,250)); buf_gc.fillRect(x,y+3,6,23);  // gcの色を水色に設定し■を描画
    ball.LineColl(x,y+3,x,y+26,buf_gc); ball.LineColl(x+6,y+3,x+6,y+26,buf_gc); // 線の描画&当たり判定
  }

  // ■ 数字の画像を表示させるメソッド
  void DispNum(int score, int x, int y, int k) {
    for(i=0;i<k;i++) {
      int num = score % 10; // numにスコアを10で割った余りを代入
      buf_gc.drawImage(imgLet,-i*17+x+60,y,-i*17+x+18+60,y+26,238+num*9,0,238+(1+num)*9,13,this); // 画像から数字を切り取って貼る
      score /= 10; // scoreを10で割る。
    }
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
      ftr.rflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_SPACE: // スペースキーが押されたら
      ftr.sflag = true; // フラグを立てる
      if(mode==0&&spring<40&&750<ball.x&&370<ball.y)spring++; // 徐々にばねを沈ませる
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
    case KeyEvent.VK_F6: // [F6]キーが押されたら
      ftr.f6flag = true; // フラグを立てる
      break;
    case KeyEvent.VK_F7: // [F7]キーが押されたら
      ftr.f7flag = true; // フラグを立てる
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
      break;
    case KeyEvent.VK_SPACE: // スペースキーが離されたら
      ftr.sflag = false; // フラグを降ろす
      if(spring>0&&ball.y>=410-35) {
        ball.dy=-spring; // ばねの力をボールに与える
        bomb.play();
      }
      spring=0; // ばねの沈みを元に戻す
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
    case KeyEvent.VK_F6: // [F6]キーが離されたら
      ftr.f6flag = false; // フラグを立てる
      break;
    case KeyEvent.VK_F7: // [F7]キーが離されたら
      ftr.f7flag = false; // フラグを立てる
      break;
    }
  }
}
