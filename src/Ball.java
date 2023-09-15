/**
 * ボールのクラス
 *
 * @author Keisuke
 */

import java.awt.*;

class Ball extends Object{
  // フィールド変数
  double v,alph,beta;
  Image ImBal = getToolkit().getImage("img/ball.png"); // ボールの画像をインポート

  Ball (int width, int height) {
    x=757; y=350;
    dx=0; dy=0;
    w = 15;
    hp = 2;
  }

  void move(Graphics buf,int width, int height) {
    buf.setColor(new Color(200,200,200)); // ボールの色を設定
    buf.fillOval(x, y, 2*w, 2*w); // ボールを描画
    buf.drawImage(ImBal,x,y,null); // ボールの画像を貼り付ける
    dy+=0.1; // 重力
    x=x+(int)dx; // ボールをx方向に動かす
    y=y+(int)dy; // ボールをy方向に動かす
    LineColl(210,0,210,600); // 左端当たり判定
    LineColl(790,0,790,600); // 右端当たり判定

    v=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2)); // xとyの合成ベクトルの大きさを計算
    alph = Math.atan2(dx,dy); // xとyのなすﾀﾝｼﾞｪﾝﾄを計算
    /* 左フリッパー当たり判定 */
    switch(Flipper.lFlp){
    case 2: // 上がりきっている時
      if(y+w*(1+Math.cos(beta))-456-9<(474+18-456-9)*(x+w*(1-Math.sin(beta))-421-9)/(318+18-421-9)) Line45Coll(336-3,474,431,456);
      ballColl(431-1-9,456,9); // 左フリッパー右端の丸
      if(y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-421-9)/(318+18-421-9)) Line225Coll(336+9,510-2,431,456+18);
      break;
    case 1: // 上がっている途中
      if(318+18<x&&x<436&&y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-421-9)/(318+18-421-9)&&y+w*(1+Math.cos(beta))-476<(528-476)*(x+w*(1-Math.sin(beta))-345)/(431-345)) {
        if(y+2*w<434) {
          dx=1; dy=-5;
        } else if(y+2*w<=438) {
          dy=-8-v;
        } else if(y+2*w<=453) {
          dx=-1; dy=-5;
        } else {
          dx=-1; dy=-10;
        }
        break;
      }
    case 0: // 上がってない時
      if(y+w*(1+Math.cos(beta))-474-18<(528+9-474-18)*(x+w*(1-Math.sin(beta))-318-18)/(419+9-318-18)) Line135Coll(345,476,431,528);
      ballColl(431-3-9,528,9); // 左フリッパー右端の丸
      if(y+w*(1-Math.cos(beta))-474-18>(528+9-474-18)*(x+w*(1+Math.sin(beta))-318-18)/(419+9-318-18)) Line315Coll(336-3,510,431-4,528+18);
    }
    ballColl(318,474,18); // 左フリッパー左側の丸
    /* 右フリッパー当たり判定 */
    switch(Flipper.rFlp){
    case 2: // 上がりきっている時
      if(y+w*(1+Math.cos(beta))-456-9<(474+18-456-9)*(x+w*(1-Math.sin(beta))-521-9)/(606+18-521-9)) Line135Coll(529,456,624+3,474);
      ballColl(529+1-9,456,9); // 右フリッパー左端の丸
      if(y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-521-9)/(606+18-521-9)) Line315Coll(529,456+18,624-9,510-2);
      break;
    case 1: // 上がっている途中
      if(524-2*w<x&&x<642-18&&y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-521-9)/(606+18-521-9)&&y+w*(1+Math.cos(beta))-476<(528-476)*(x+w*(1-Math.sin(beta))-345)/(431-345)) {
        if(y+2*w<434) {
          dx=1-v*0.27; dy=-5-0.75*v;
        } else if(y+2*w<=438) {
          dy=-8-v;
        } else if(y+2*w<=453) {
          dx=-1-v*0.25; dy=-5-0.75*v;
        } else {
          dx=-1-v*0.1; dy=-10-0.9*v;
        }
      }
      break;
    case 0: // 上がってない時
      if(y+w*(1+Math.cos(beta))-474-18<(528+9-474-18)*(x+w*(1-Math.sin(beta))-606-18)/(523+9-606-18)) Line45Coll(529,528,615,476);
      ballColl(529+3-9,528,9); // 右フリッパー左端の丸
      if(y+w*(1-Math.cos(beta))-474-18>(528+9-474-18)*(x+w*(1+Math.sin(beta))-606-18)/(523+9-606-18)) Line225Coll(529+4,528+18,624+3,510);
    }
    ballColl(606,474,18); // 右フリッパー右端の丸
    switch(GameMaster.mode){ // modeごとに分岐
    case 0: // 下ステージ当たり判定
      if (y<-15) {
        GameMaster.mode=1;
        y=height+y;
        break;
      }
      /* 左端 */
      LineColl(246,-30,246,17); // 左上の島直線左
      Line315Coll(246,17,264,48); // 左上の島左下斜め45
      LineColl(264,48,282,48); // 左上の島直線中
      Line225Coll(282,48,318,12); // 左上の島右下斜め45
      LineColl(318,-30,318,12); // 左上の島直線右
      Line135Coll(210,48,226,76); // 左上斜め30
      LineColl(226,76,226,276); // 左中直線
      Line135Coll(226,276,250,300); // 左中斜め45
      Line135Coll(210,492,318,600); // 左下斜め45
      /* 右端 */
      LineColl(642,0,642,12); // 右上直線
      Line315Coll(642,12,734,104); // 右上斜め
      LineColl(734,104,734,276); // 右中直線
      Line45Coll(710, 300, 734, 276); // 右中斜め135
      Line315Coll(710,300,750,340); // 右中斜め45
      LineColl(750,340,750,492); // 右下直線
      Line45Coll(642, 600, 750, 492); // 右下斜め45
      /* レーン */
      LineColl(246,379-3,246,432-3); // 左レーン左
      LineColl(246,379-3,250,379-3); // 左レーン上
      LineColl(250,379-3,250,429-3); // 左レーン右
      Line135Coll(250,428+1-3,320+4,470+1-3); // 左レーン斜め右上
      if (y+w*(1+Math.cos(beta))-397.5-3>(434-397.5)*(x+w*(1-Math.sin(beta))-286)/(318+4-286)) Line315Coll(286,416-3,318+4,434-3); // 左レーン三角左下
      LineColl(286,379-3,286,416-3); // 左レーン三角左
      if (y+w*(1-Math.cos(beta))-397.5-3<(434-397.5)*(x+w*(1+Math.sin(beta))-286)/(318+4-286)) Line135Coll(286,379-3,318+4,434-3); // 左レーン三角右上
      if (y+w*(1-Math.cos(beta))-397.5-3<(434-397.5)*(x+w*(1+Math.sin(beta))-674)/(642-4-674)) Line45Coll(642-4,434-3,674,379-3); // 右レーン三角左上
      LineColl(674,379-3,674,416-3); // 右レーン三角右
      if (y+w*(1+Math.cos(beta))-397.5-3>(434-397.5)*(x+w*(1-Math.sin(beta))-674)/(642-4-674)) Line225Coll(642-4,434-3,674,416-3); // 右レーン三角右下
      Line45Coll(642-4,470+1-3,710,428+1-3);// 右レーン斜め左上
      LineColl(710,379-3,710,429-3); // 右レーン左
      LineColl(710,379-3,714,379-3); // 右レーン上
      LineColl(714,379-3,714,432-3); // 右レーン右
      /* 上のちょんちょん */
      for (int i=0;i<6;i++) DiaColl(372+42*i,100);
      if (750<x) { // 発射装置内
        dx = 0; x=757;
        if(410-33<y) {
          dy = -0.5*dy;
          y = 410-33;
        }
      }
      if (y<0) {
        GameMaster.mode=1;
        y=height;
      }
      break;
    case 1: // 上ステージ当たり判定
      if (y>height) { // 下端
        GameMaster.mode=0; // 下ステージへ切り替え
        y=y-height;        // 下ステージ上へ
        break;
      }
      /* 左上曲線 */
      if (x<350&&y<222-15) {
        dx=v*Math.sin(Math.atan2((222-y-15),(x+18-350)));
        dy=-v*Math.cos(Math.atan2((222-y-15),(x+18-350)));
      }
      /* 右上曲線 */
      if (640<x&&y<222-15&&Math.pow(x+w-640,2)+Math.pow(y+w-222,2)>Math.pow(114+w,2)) {
        dx=-v*Math.sin(Math.atan2((222-y-15),(x+18-640)));
        dy=-v*Math.cos(Math.atan2((222-y-15),(x+18-640)));
      }
      /* フリッパー左 */
      LineColl(246,432,246,600); // 左
      Line45Coll(246,432,270,408); // 左上斜め45
      Line135Coll(270,408,318,456); // 右上斜め45
      LineColl(318,456,318,600); // 右中直線
      /* 外郭 */
      Line225Coll(210,408,246,372); // 左斜め45下
      LineColl(246,302,246,372); // 左中直線
      Line135Coll(210,240,246,302); //左斜め45上
      LineColl(200,72,800,72); // 上限
      /* 左上の島 */
      LineColl(350,108,358,108); // 上直線
      Line135Coll(358,108,420,144); //右上斜め
      LineColl(420,144,420,182); // 右直線
      Line225Coll(318,284,420,182); // 右下斜め45
      LineColl(282,284,318,284); // 下直線
      Line315Coll(246,222,282,284); // 左下斜め
      /* フリッパー右 */
      LineColl(642,456,642,600); // フリッパー右直線
      Line45Coll(642,456,690,408); // 右下斜め45左
      Line135Coll(690,408,714,432); // 右下斜め45右
      Line45Coll(714,432,738,408);
      Line315Coll(714,384,738,408); // 右下斜め45右上
      LineColl(714,384,730,384); Line45Coll(730,384,750,364); LineColl(750,194,750,364);
      Line315Coll(720,158,750,194); LineColl(672,158,720,158);
      LineColl(672,158,690,158);  Line225Coll(630,182,672,158); LineColl(540,182,630,182);
      LineColl(540,144,540,182); Line45Coll(540,144,602,108);
      LineColl(602,108,640,108); LineColl(754,222,754,600); // 右直線
      /* 右島 */
      Line45Coll(648,218,690,194); // 右島左上斜め45
      Line315Coll(648,218,690,260); // 右島左上斜め45
      LineColl(690,260,690,336); // 右島左
      ballColl(690,324,12);// 右島下
      LineColl(714,218,714,336); // 右島右
      if(690-15<x&&y<218+15) ballColl(666,194,24); // 右島右上
      /* 上のちょんちょん */
      for (int i=0;i<2;i++) DiaColl(456+42*i,144);
      if (222<y&&750<x) { // 発射装置内
        dx = 0; x=757;
      }
      if (y<72) y=72; // 上端
    }
  }

  void revive() {
    x=757; y=350;
    dx=0; dy=0;
  }

  void LineColl (int x1,int y1,int x2,int y2) { // 斜めでない直線の当たり判定
    if(x1==x2 && x1-2*w<x && x<x1 && y1-w<=y && y<=y2-w) {
      dx *= -0.9;
      if (x<=x1-w) x = x1-2*w;  else x = x1;
    } if(y1==y2 && x1-w<=x && x<=x2-w && y1-2*w<y && y<y1) {
      dy *= -0.8;
      if (y<=y1-w) y = y1-2*w;  else y = y1;
    }
  }

  void Line45Coll (int x1, int y1, int x2, int y2) { // ／の左上の当たり判定
    beta=Math.atan2(x1-x2,y1-y2);
    if(x1-15<=x&&x<=x2-15&&y2-30<=y&&y<y1&&y+w*(1+Math.cos(beta))-y2>=(y1-y2)*(x+w*(1-Math.sin(beta))-x2)/(x1-x2)) {
      Vect();
    }
  }

  void Line135Coll (int x1, int y1, int x2, int y2) { // ＼の右上の当たり判定
    beta=Math.atan2(x2-x1,y2-y1);
    if(x1-15<=x&&x<=x2-15&&y1-30<=y&&y<y2&&y+w*(1+Math.cos(beta))-y1>=(y2-y1)*(x+w*(1-Math.sin(beta))-x1)/(x2-x1)) {
      Vect();
    }
  }

  void Line225Coll (int x1, int y1, int x2, int y2) { // ／の右下の当たり判定
    beta=Math.atan2(x1-x2,y1-y2);
    if(x1-15<=x&&x<=x2-15&&y2-30<=y&&y<y1&&y+w*(1-Math.cos(beta))-y2<=(y1-y2)*(x+w*(1+Math.sin(beta))-x2)/(x1-x2)) {
      Vect();
    }
  }

  void Line315Coll (int x1, int y1, int x2, int y2) { //  ＼の左下の当たり判定
      beta=Math.atan2(x2-x1,y2-y1);
      if(x1-15<=x&&x<=x2-15&&y1-30<=y&&y<y2&&y+w*(1-Math.cos(beta))-y1<=(y2-y1)*(x+w*(1+Math.sin(beta))-x1)/(x2-x1)) {
        Vect();
      }
    }

  void Vect() { // 斜めの直線衝突時に計算して速度を与える
    if (v<1.5) {
      dx=v*Math.sin(2*beta-alph);
      dy=v*Math.cos(2*beta-alph);
    }
    else {
      dx=0.9*v*Math.sin(2*beta-alph);
      dy=0.9*v*Math.cos(2*beta-alph);
    }
  }

  void DiaColl (int a,int b) { // 上のちょんちょん当たり判定
    LineColl(a,b+3,a,b+23);  LineColl(a+6,b+3,a+6,b+23);
    ballColl(a, b, 3);  ballColl(a,b+20,3);
  }

  void ballColl (int x1,int y1,int r) {
    if (Math.pow(x1+r-x-w,2)+Math.pow(y1+r-y-w,2)<=Math.pow(r+w,2)) {
      beta=Math.atan2(x1+r-x-w,y1+r-y-w);
      dx=-v*Math.sin(2*beta-alph);
      dy=-v*Math.cos(2*beta-alph);
    }
  }
}
