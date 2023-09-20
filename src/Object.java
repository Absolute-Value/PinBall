/**
 *  ゲームに登場するオブジェクトに共通する親クラス（抽象クラス）
 *   変数：　位置（x,y），速度（dx,dy），幅（w,h），Hit Point（hp）
 *   メソッド： move, revive, CcollisionCheck
 * @author Keisuke
 */

import java.awt.*;
import java.applet.*;

abstract class Object extends Canvas { // 抽象クラス
  int x, y;     // 左上座標
  double dx,dy; // 速度
  int w, h;     // 幅の半分
  int hp;       // ヒットポイント（ゼロ以下で死亡）
  Image cdBack = getToolkit().getImage("../img/card_back.png"); // カードの画像をインポート

  // コンストラクタ
  Object () {
    x = 500; y = 300;
    w = h = 15;
    hp = 1;
  }

  abstract void move(Graphics buf,int width, int height);
  // オブジェクトを動かし、位置を更新する抽象メソッド（Object を継承する全てのクラスに同名での実装を強制）

  abstract void revive();
  // オブジェクトを生き返らす抽象メソッド（Object を継承する全てのクラスに同名での実装を強制）

  boolean collisionCheck(Object obj,int score,AudioClip get){
    // 引数は相手のオブジェクトとスコア
    if (Math.pow(this.x+this.w-obj.x-obj.w,2)+Math.pow(this.y+this.w-obj.y-obj.w,2)<=Math.pow(this.w+obj.w,2)&&obj.hp>0) {
      // ボールとオブジェクトが衝突したら
      obj.hp--; // 相手のhpを減らす
      get.play(); // 音源get.wavを再生
      GameMaster.Score_1P+=score; // スコアをscore分加算
      return true; // true をお持ち帰り
    }
    else return false; // false をお持ち帰り
  }

  boolean collisionCheck(Object obj,AudioClip bomb){
    // 引数は相手のオブジェクト
    if (Math.pow(this.x+this.w-obj.x-obj.w,2)+Math.pow(this.y+this.w-obj.y-obj.w,2)<=Math.pow(this.w+obj.w,2)&&obj.hp>0) {
      // ボールとオブジェクトが衝突したら
      GameMaster.Score_1P+=100; // スコアを100加算
      bomb.play(); // 音源bomb.wavを再生
      double v=Math.sqrt(Math.pow(this.dx,2)+Math.pow(this.dy,2)); // 速度の大きさを計算
      double alph = Math.atan2(this.dx,this.dy);                   // xとyのなすﾀﾝｼﾞｪﾝﾄを計算
      double beta = Math.atan2(obj.x+obj.w-this.x-this.w,obj.y+obj.w-this.y-this.w); // ボールとオブジェクトのなすﾀﾝｼﾞｪﾝﾄを計算
      this.dx = -1.1*v*Math.sin(2*beta-alph); // 計算した速度を1.1倍した値をx方向に与える
      this.dy = -1.1*v*Math.cos(2*beta-alph); // 計算した速度を1.1倍した値をy方向に与える
      return true; // true をお持ち帰り
    }
    else return false; // false をお持ち帰り
  }
}

class ScoreBomb extends Object { // 丸いオブジェクト(当たると跳ね返って得点)
  Color col;
  ScoreBomb (int sx,int sy,Color cl) { // コンストラクタ(初期値設定)
    col= cl;         // 色を保存しておく
    x = sx; y = sy;  // 座標を指定したものに設定
    w = h = 25;      // 幅を指定
    hp = 1;
  }
  void move(Graphics buf, int apWidth,int apHeight) {
    if (hp>0) {
      buf.setColor(col);
      buf.fillOval(x,y,2*w,2*w); // gc を使って●を描く
      buf.setColor(Color.white); // gc の色を白色に
      buf.fillOval(x+6,y+6,2*(w-6),2*(w-6)); // gc を使って●を描く
      buf.setColor(Color.black); // gc の色を黒色に
      buf.drawString("100",x+w-10,y+w+5); // gcに100を描画
    }
    if (hp==0) {
      buf.setColor(col);
      buf.fillOval(x+3,y+3,2*(w-3),2*(w-3)); // gc を使って●を描く
      buf.setColor(Color.white); // gc の色を白色に
      buf.fillOval(x+6,y+6,2*(w-6),2*(w-6)); // gc を使って●を描く
      buf.setColor(Color.black); // gc の色を黒色に
      buf.drawString("100",x+w-10,y+w+5); // gcに100を描画
    }
  }
  void revive() {
    hp = 1;
  }
}

class Chip extends Object { // 丸いチップ(取ると得点)
  Chip (int sx,int sy,int w2) { // コンストラクタ(初期値設定)
    x = sx;  y = sy; // 座標を指定したものに設定
    w = h = w2; // 幅を指定
    hp = 0;
  }
  void move(Graphics buf, int apWidth,int apHeight) {
    if (hp>0) { // もし生きていれば（取っていなかったら）
      buf.setColor(new Color(255,255,100)); // gc の色を黄色に
      buf.fillOval(x,y,2*w,2*w); // gc を使って●を描く
      buf.setColor(Color.white); // gc の色を白色に
      buf.drawOval(x,y,2*w,2*w); // gc を使って○を描く(縁取り)
    }
  }
  void revive() {
    hp = 1;
  }
}

class Diam extends Object { // ボールの通過を判定するオブジェクト
  boolean card = false; // ボールの通過を保存しておく変数
  Diam (int sx,int sy) { // コンストラクタ(初期値設定)
    x = sx;  y = sy; // 座標を指定したものに設定
    w = h = 3;       // 幅を指定
    hp = 0;
  }
  void move(Graphics buf, int apWidth,int apHeight) {}
  void move(Graphics buf, int i) { // 判定の上にカードを描かせる
    if (card==true) { // もしボールが通過していれば
      buf.setColor(Color.white); buf.fillRect(x+w-17,y-63,34,50); // 白い四角を描画
      if(i<6) buf.setColor(Color.blue);         // PINの文字(iが3~5)のとき青色に文字色を青に
      else buf.setColor(new Color(255,40,120)); // BALLの文字(iが6~9)のとき青色に文字色をピンクに
      switch(i){
      case 3: // iが3のとき
        buf.drawString("P",x,y-33); // "P"を描画
        break;
      case 4: // iが4のとき
        buf.drawString("I",x,y-33); // "I"を描画
        break;
      case 5: // iが5のとき
        buf.drawString("N",x,y-33); // "N"を描画
        break;
      case 6: // iが6のとき
        buf.drawString("B",x,y-33); // "B"を描画
        break;
      case 7: // iが7のとき
        buf.drawString("A",x,y-33); // "A"を描画
        break;
      case 8: // iが8のとき
      case 9: // iが9のとき
        buf.drawString("L",x,y-33); // "L"を描画
        break;
      }
    }
    else { // もしボールが通過していなければ
      // 画像を読み込めなかったときのために■を描画しています
      buf.setColor(Color.green); buf.fillRect(x+w-17,y-63,34,50); // 緑の■を描画
      buf.setColor(Color.white); buf.drawRect(x+w-17,y-63,34,50); // 白の□を描画
      buf.drawImage(cdBack,x+w-17,y-63,this); // カードの画像を貼る
    }
  }
  void revive() {
    hp = 1;
  }
}
