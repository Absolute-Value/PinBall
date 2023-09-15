/**
 * ボールのクラス
 *
 * @author Keisuke
 */

import java.awt.*;
import java.applet.*;

class Ball extends Object{
  // フィールド変数
  double v,alph,beta;
  Image ImBal = getToolkit().getImage("img/ball.png"); // ボールの画像をインポート
  int bar, delaytime, delTime;
  boolean ba=false; // 上ステージの左右に動くオブジェクトもどちらの向きに動かすか決めるboolean

  Ball (int width, int height) {
    x=757; y=350; // 発射装置内を初期位置に
    dx=0; dy=0;   // 速度は0に
    w = 15;       // 半径を15
    hp = 2;       // 残りのボール数を2にする
    bar = delaytime = 0;
    delTime = 90; // 初期待ち時間設定(ボールの描画に関わる)
  }

  void move (Graphics buf,int width, int height) {} // 音声をインポートしたいので今回はしようしない

  void move (Graphics buf,int width, int height, AudioClip bomb) { // 音声のインポート機能をつけたmove
    if(delTime>=80) buf.drawImage(ImBal,x,y,null); // ボールの画像を貼り付ける
    dy+=0.1; // 重力
    x=x+(int)dx; // ボールをx方向に動かす
    y=y+(int)dy; // ボールをy方向に動かす
    LineColl(210,0,210,600); // 左端当たり判定
    LineColl(790,0,790,600); // 右端当たり判定

    v=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2)); // 速度の大きさを計算
    alph = Math.atan2(dx,dy);                   // xとyのなすﾀﾝｼﾞｪﾝﾄを計算
    /* 左フリッパー当たり判定 */
    switch(Flipper.lFlp){
    case 2: // 上がりきっている時
      if(delaytime == 0) { // 待ち時間が0になったら
        if(y+w*(1+Math.cos(beta))-456-9<(474+18-456-9)*(x+w*(1-Math.sin(beta))-421-9)/(318+18-421-9)) Line45Coll(336-3,474,431,456); // 左フリッパー／左上当たり判定
        ballColl(431-1-9,456,9); // 左フリッパー右端の丸
        if(y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-421-9)/(318+18-421-9)) Line225Coll(336+9,510-2,431,456+18); // 左フリッパー／右下当たり判定
      } else delaytime--; // 待ち時間を1減らす
      break;
    case 1: // 上がっている途中
      if(318+18<x&&x<436&&y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-421-9)/(318+18-421-9)&&y+w*(1+Math.cos(beta))-474-18<(528+9-474-18)*(x+w*(1-Math.sin(beta))-318-18)/(419+9-318-18)) {
        if(y+2*w<434) { // yが434より小さい時
          dx=1; dy=-5;
        } else if(y+2*w<=438) { // yが434以上438以下の時
          dy=-7-v; // 速さに7を加算した大きさの速度を上向きに与える
        } else if(y+2*w<=453) { // yが438より大きく453以下の時
          dx=-1; dy=-5;
        } else { // yが453より大きい時
          dx=-1; dy=-10;
        }
        delaytime = 5; // 待ち時間case1ではね返った後にcase0でもう一度跳ね返るのを防ぐ待ち時間
      }
      break;
    case 0: // 上がってない時
      if(y+w*(1+Math.cos(beta))-474-18<(528+9-474-18)*(x+w*(1-Math.sin(beta))-318-18)/(419+9-318-18)) Line135Coll(345,476,431,528); // 左フリッパー＼右上当たり判定
      ballColl(431-3-9,528,9); // 左フリッパー右端の丸
      if(y+w*(1-Math.cos(beta))-474-18>(528+9-474-18)*(x+w*(1+Math.sin(beta))-318-18)/(419+9-318-18)) Line315Coll(336-3,510,431-4,528+18); // 左フリッパー＼左下当たり判定
    }
    ballColl(318,474,18); // 左フリッパー左側の丸
    /* 右フリッパー当たり判定 */
    switch(Flipper.rFlp){
    case 2: // 上がりきっている時
      if(delaytime == 0) { // 待ち時間が0になったら
        if(y+w*(1+Math.cos(beta))-456-9<(474+18-456-9)*(x+w*(1-Math.sin(beta))-521-9)/(606+18-521-9)) Line135Coll(529,456,624+3,474); // 右フリッパー＼右上当たり判定
        ballColl(529+1-9,456,9); // 右フリッパー左端の丸
        if(y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-521-9)/(606+18-521-9)) Line315Coll(529,456+18,624-9,510-2); // 右フリッパー＼左下当たり判定
      } else delaytime--; // 待ち時間を1減らす
      break;
    case 1: // 上がっている途中
      if(524-2*w<x&&x<642-18&&y+w*(1-Math.cos(beta))-456-9>(474+18-456-9)*(x+w*(1+Math.sin(beta))-521-9)/(606+18-521-9)
         &&y+w*(1+Math.cos(beta))-474-18<(528+9-474-18)*(x+w*(1-Math.sin(beta))-606-18)/(523+9-606-18)) {
        if(y+2*w<434) { // yが434より小さい時
          dx=1-v*0.27; dy=-5-0.75*v;
        } else if(y+2*w<=438) { // yが434以上438以下の時
          dy=-7-v; // 速さに7を加算した大きさの速度を上向きに与える
        } else if(y+2*w<=453) { // yが438より大きく453以下の時
          dx=-1; dy=-5;
        } else { // yが453より大きい時
          dx=-1; dy=-10;
        }
        delaytime = 5; // 待ち時間case1ではね返った後にcase0でもう一度跳ね返るのを防ぐ待ち時間
      }
      break;
    case 0: // 上がってない時
      if(y+w*(1+Math.cos(beta))-474-18<(528+9-474-18)*(x+w*(1-Math.sin(beta))-606-18)/(523+9-606-18)) Line45Coll(529,528,615,476); // 右フリッパー／左上当たり判定
      ballColl(529+3-9,528,9); // 右フリッパー左端の丸
      if(y+w*(1-Math.cos(beta))-474-18>(528+9-474-18)*(x+w*(1+Math.sin(beta))-606-18)/(523+9-606-18)) Line225Coll(529+4,528+18,624+3,510); // 右フリッパー／右下当たり判定
    }
    ballColl(606,474,18); // 右フリッパー右端の丸
    switch(GameMaster.mode){ // modeごとに分岐
    case 0: // 下ステージ当たり判定
      if (y<-15) { // 上に行ったら
        GameMaster.mode=1; // 上ステージに移動
        y=height+y; // 座標は下に
        break;
      }
      /* レーン */
      LineColl(246,379-3,246,432-3); // 左レーン左
      LineColl(246,379-3,250,379-3); // 左レーン上
      LineColl(250,379-3,250,429-3); // 左レーン右
      Line135Coll(250,428+1-3,320+4,470+1-3); // 左レーン斜め右上
      if (y+w*(1+Math.cos(beta))-397.5-8>(434-397.5)*(x+w*(1-Math.sin(beta))-286)/(318+4-286)) Line315Coll(286,416-8,318+4,434-8); // 左レーン三角左下
      LineColl(286,379-8,286,416-8); // 左レーン三角左
      if (y+w*(1-Math.cos(beta))-397.5-8<(434-397.5)*(x+w*(1+Math.sin(beta))-286)/(318+4-286)) Line135Coll(286,379-8,318+4,434-8); // 左レーン三角右上
      if (y+w*(1-Math.cos(beta))-397.5-8<(434-397.5)*(x+w*(1+Math.sin(beta))-674)/(642-4-674)) Line45Coll(642-4,434-8,674,379-8); // 右レーン三角左上
      LineColl(674,379-8,674,416-8); // 右レーン三角右
      if (y+w*(1+Math.cos(beta))-397.5-8>(434-397.5)*(x+w*(1-Math.sin(beta))-674)/(642-4-674)) Line225Coll(642-4,434-8,674,416-8); // 右レーン三角右下
      Line45Coll(642-4,470+1-3,710,428+1-3);// 右レーン斜め左上
      LineColl(710,379-3,710,429-3); // 右レーン左
      LineColl(710,379-3,714,379-3); // 右レーン上
      LineColl(714,379-3,714,432-3); // 右レーン右
      if (750<x) { // 発射装置内
        dx = 0; x=757; // x方向の速度と位置は固定
        if(410-33<y) { // 377より下にいったら
          dy *= -0.5; // y座標の向きを変えて半分に減衰
          y = 410-33; // 下のめり込み防止
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
      /* 動くバー */
      if(bar<80&&ba==false) bar++;       //baがfalseかつbarが80未満の時、barを1増やす
      else if(-80<bar&&ba==true) bar --; //baがtrueかつbarが80より大きい時、barを1減らす
      if(bar==80) ba = true;   // barが80に達したらbaをtrueにして動くバーの向きを反対に
      if(bar==-80) ba = false; // barが-80に達したらbaをfalseにして動くバーの向きを反対に
      ballColl(452+bar,372,8,buf,new Color(255,40,120)); // 動くバー左の○を描画する
      ballColl(492+bar,372,8,buf,new Color(255,40,120)); // 動くバー右の○を描画する
      buf.setColor(new Color(255,40,120)); // 色を桃色に設定
      buf.fillRect(460+bar,372,40,16);     // ■を描画する
      LineColl(460+bar,372,500+bar,372,buf); // 動くバー上の直線を描画する
      LineColl(460+bar,388,500+bar,388,buf); // 動くバー下の直線を描画する
      /* 左上曲線 */
      if (x<350&&y<222-15) { // 左上のカーブに入ったら
        dx=v*Math.sin(Math.atan2((222-y-15),(x+18-350)));  // x方向に計算した速度を与える
        dy=-v*Math.cos(Math.atan2((222-y-15),(x+18-350))); // y方向に計算した速度を与える
      }
      /* 右上曲線 */
      if (640<x&&y<222-15&&Math.pow(x+w-640,2)+Math.pow(y+w-222,2)>Math.pow(114+w,2)) { // 右上のカーブに入ったら
        dx=-v*Math.sin(Math.atan2((222-y-15),(x+18-640))); // x方向に計算した速度を与える
        dy=-v*Math.cos(Math.atan2((222-y-15),(x+18-640))); // y方向に計算した速度を与える
      }
      if (690<x&&x<720&&384<y&&y+w*(1-Math.cos(Math.atan2(690-714,408-384)))-384>=(408-384)*(x+w*(1+Math.sin(Math.atan2(690-714,408-384)))-714)/(690-714)) { // 再発射装置内
        x = 700; y = 394; // ボールを座標に固定
        dx = 0;  dy = 0;  // 速度成分を0に
        delTime--; // 待ち時間を１減らす
        if (delTime<80) { // 待ち時間によってボールの大きさを変更(徐々に小さくなるように見せている)
          if (delTime<20) buf.drawImage(ImBal,712,406,718,411,0,0,30,30,null);     //  1~19のとき
          else if (delTime<40) buf.drawImage(ImBal,709,403,723,414,0,0,30,30,null);// 20~39のとき
          else if (delTime<60) buf.drawImage(ImBal,706,400,724,417,0,0,30,30,null);// 40~59のとき
          else buf.drawImage(ImBal,703,397,727,423,0,0,30,30,null);                // 60~79のとき
        } if (delTime==0) { // 待ち時間が0になったら
          dx = dy = -11; // 左上向きの速度を与える
          bomb.play(); // 音源bombを再生
        }
      }
      else delTime = 90; // 待ち時間を90に設定
      if (222<y&&750<x) { // 発射装置内
        dx = 0; x=757; // xの速度を0にしてx座標を757で固定
      }
      if (y<72) { // 上限より上に行ったら
        y=72; // 上端
        dy*=-0.85; // yy方向の速度の向きを変えて減衰
      }
    }
  }

  void revive() {
    x=757; y=350; // 右下の発射装置内にballを移動
    dx=0; dy=0;   // 速度成分を0に
    delaytime = 0; // 待ち時間を0に
  }

  void LineColl (int x1,int y1,int x2,int y2,Graphics buf) { // 斜めでない直線の当たり判定(線も描いてくれる)
    buf.setColor(Color.white);  buf.drawLine(x1,y1,x2,y2); LineColl(x1,y1,x2,y2); // 白線を描画して当たり判定を呼び出す
  }
  void LineColl (int x1,int y1,int x2,int y2) { // 斜めでない直線の当たり判定
    if(x1==x2 && x1-2*w<x && x<x1 && y1-w<=y && y<=y2-w) { // 縦向きの直線だったら
      dx *= -0.9; // x方向の速度の向きを変えて少し減衰
      if (x<=x1-w) x = x1-2*w;  else x = x1; // めり込みを防ぐ調整
    } if(y1==y2 && x1-w<=x && x<=x2-w && y1-2*w<y && y<y1) { // 横向きの直線だったら
      dy *= -0.8; // x方向の速度の向きを変えて少し減衰
      if (y<=y1-w) y = y1-2*w;  else y = y1; // めり込みを防ぐ調整
    }
  }

  void Line45Coll (int x1, int y1, int x2, int y2,Graphics buf) { // ／の左上の当たり判定(線も描いてくれる)
    buf.setColor(Color.white); buf.drawLine(x1,y1,x2,y2); Line45Coll(x1,y1,x2,y2); // 白線を描画して当たり判定を呼び出す
  }
  void Line45Coll (int x1, int y1, int x2, int y2) { // ／の左上の当たり判定
    beta=Math.atan2(x1-x2,y1-y2); // 直線と水平線のなす角を計算
    if(x1-22<x&&x<x2-8&&y2-30<=y&&y<y1&&y+w*(1+Math.cos(beta))-y2>=(y1-y2)*(x+w*(1-Math.sin(beta))-x2)/(x1-x2)) {
      Vect();
    }
  }

  void Line135Coll (int x1, int y1, int x2, int y2,Graphics buf) { // ＼の右上の当たり判定(線も描いてくれる)
    buf.setColor(Color.white); buf.drawLine(x1,y1,x2,y2); Line135Coll(x1,y1,x2,y2); // 白線を描画して当たり判定を呼び出す
  }
  void Line135Coll (int x1, int y1, int x2, int y2) { // ＼の右上の当たり判定
    beta=Math.atan2(x2-x1,y2-y1); // 直線と水平線のなす角を計算
    if(x1-22<x&&x<x2-8&&y1-30<=y&&y<y2&&y+w*(1+Math.cos(beta))-y1>=(y2-y1)*(x+w*(1-Math.sin(beta))-x1)/(x2-x1)) {
      Vect();
    }
  }

  void Line225Coll (int x1, int y1, int x2, int y2,Graphics buf) { // ／の右下の当たり判定(線も描いてくれる)
    buf.setColor(Color.white); buf.drawLine(x1,y1,x2,y2); Line225Coll(x1,y1,x2,y2); // 白線を描画して当たり判定を呼び出す
  }
  void Line225Coll (int x1, int y1, int x2, int y2) { // ／の右下の当たり判定
    beta=Math.atan2(x1-x2,y1-y2); // 直線と水平線のなす角を計算
    if(x1-22<x&&x<x2-8&&y2-30<=y&&y<y1&&y+w*(1-Math.cos(beta))-y2<=(y1-y2)*(x+w*(1+Math.sin(beta))-x2)/(x1-x2)) {
      Vect();
    }
  }

  void Line315Coll (int x1, int y1, int x2, int y2,Graphics buf) { //  ＼の左下の当たり判定(線も描いてくれる)
    buf.setColor(Color.white); buf.drawLine(x1,y1,x2,y2); Line315Coll(x1,y1,x2,y2); // 白線を描画して当たり判定を呼び出す
  }
  void Line315Coll (int x1, int y1, int x2, int y2) { //  ＼の左下の当たり判定
    beta=Math.atan2(x2-x1,y2-y1);
    if(x1-22<x&&x<x2-8&&y1-30<=y&&y<y2&&y+w*(1-Math.cos(beta))-y1<=(y2-y1)*(x+w*(1+Math.sin(beta))-x1)/(x2-x1)) {
      Vect();
    }
  }

  void Vect() { // 斜めの直線衝突時に計算して速度を与える
    if (v<1.5) { // 速度の大きさが1.5より小さいなら
      dx=v*Math.sin(2*beta-alph); // x方向に計算した速度を与える
      dy=v*Math.cos(2*beta-alph); // y方向に計算した速度を与える
    }
    else {
      dx=0.9*v*Math.sin(2*beta-alph); // x方向に計算した値に反発係数0.9をかけた速度を与える
      dy=0.9*v*Math.cos(2*beta-alph); // y方向に計算した値に反発係数0.9をかけた速度を与える
    }
  }

  void ballColl (int x1,int y1,int r,Graphics buf,Color cl) { // 丸い物体の当たり判定(円も描く)
    buf.setColor(cl); buf.fillOval(x1,y1,r*2,r*2); // 色を設定して●を描く
    buf.setColor(Color.white); buf.drawOval(x1,y1,r*2,r*2); // 色を設定して○を描く
    ballColl(x1,y1,r); // ○の衝突判定
  }

  void ballColl (int x1,int y1,int r) { // 丸いオブジェクトの当たり判定
    if (Math.pow(x1+r-x-w,2)+Math.pow(y1+r-y-w,2)<=Math.pow(r+w,2)) { // ボールとオブジェクトが衝突したら
      beta=Math.atan2(x1+r-x-w,y1+r-y-w); // ボールと丸いオブジェクトのなすﾀﾝｼﾞｪﾝﾄを計算
      dx=-v*Math.sin(2*beta-alph); // x方向に計算した速度を与える
      dy=-v*Math.cos(2*beta-alph); // y方向に計算した速度を与える
    }
  }
}
