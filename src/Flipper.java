/**
 * フリッパー(ボールを打つやつ)のクラス
 *
 * @author Keisuke
 */

import java.awt.*;

class Flipper extends Object {
  boolean lflag;  // Fが押されているか否か
  boolean rflag;  // Jが押されているか否か
  boolean sflag;  // スペースキーが押されているか否か
  boolean f1flag; // f1が押されているか否か
  boolean f2flag; // f2が押されているか否か
  boolean f3flag; // f3が押されているか否か
  boolean f4flag; // f4が押されているか否か
  boolean f5flag; // f5が押されているか否か
  boolean f6flag; // f6が押されているか否か
  boolean f7flag; // f7が押されているか否か
  Color FlpCol = new Color(100,200,250); // フリッパー専用の色を設定
  public static int lFlp, rFlp; // 上がっている状態を保存する変数

  // コンストラクタ
  Flipper () {
    lflag = rflag = sflag = false;
    f1flag= f2flag= f3flag= f4flag= f5flag= f6flag= f7flag= false;
    hp=1;
  }
  void move(Graphics buf,int width, int height) {
    if (lflag == true) { // Fキーが押されている時
      if (lFlp < 2) lFlp++; // 2未満なら1加える
    } else { // Jキーが押されていない時
      if(0<lFlp) lFlp--; // 0より大きいなら1減らす
    }
    if (rflag == true) { // Jキーが押されている時
      if (rFlp < 2) rFlp++; // 2未満なら1加える
    } else { // Jキーが押されていない時
      if(0<rFlp) rFlp--; // 0より大きいなら1減らす
    }
    if (GameMaster.Score_1P<100000) { // スコアが10万点以下の時は描画
    /* 左フリッパー */
    buf.setColor(FlpCol); // 色をフリッパー専用色に設定
    if (lFlp == 2) { // 上がりきっている時
      buf.fillOval(431-1-9,456,18,18); // 先端の●を描画
      buf.setColor(Color.white); buf.drawOval(431-1-9,456,18,18); // 先端の○を描画
      buf.setColor(FlpCol); // 色をフリッパー専用色に設定
      buf.fillPolygon(new int[] {336-3,431,431,336+9}, new int[] {474,456,456+18,510-2},4); // フリッパー内側を専用色で埋める
      buf.setColor(Color.white); buf.drawLine(336-3,474,431,456); buf.drawLine(336+9,510-2,431,456+18); // 上下の斜線を描画
    } else if(lFlp == 1) { // 上がっている途中
      buf.fillOval(436-9,474,18,18); // 先端の●を描画
      buf.setColor(Color.white); buf.drawOval(436-9,474,18,18); // 先端の○を描画
      buf.setColor(FlpCol); // 色をフリッパー専用色に設定
      buf.fillPolygon(new int[] {336,436,436,336+6}, new int[] {474,474,474+18,510-1},4); // フリッパー内側を専用色で埋める
      buf.setColor(Color.white); buf.drawLine(336,474,436,474); buf.drawLine(336+6,510-1,436,474+18); // 上下の斜線を描画
    } else { // 上がってない時
      buf.fillOval(431-3-9,528,18,18); // 先端の●を描画
      buf.setColor(Color.white); buf.drawOval(431-3-9,528,18,18); // 先端の○を描画
      buf.setColor(FlpCol); // 色をフリッパー専用色に設定
      buf.fillPolygon(new int[] {336+9,431,431-4,336-3}, new int[] {474+2,528,528+18,510},4); // フリッパー内側を専用色で埋める
      buf.setColor(Color.white); buf.drawLine(336+9,474+2,431,528); buf.drawLine(336-3,510,431-4,528+18); // 上下の斜線を描画
    }
    buf.setColor(FlpCol); buf.fillOval(318,474,36,36);      // 左フリッパー左の●を描画
    buf.setColor(Color.white); buf.drawOval(318,474,36,36); // 左フリッパー左の○を描画
    /* 右フリッパー */
    buf.setColor(FlpCol); // 色をフリッパー専用色に設定
    if (rFlp == 2){ // 上がりきっている時
      buf.fillOval(529+1-9,456,18,18); // 先端の●を描画
      buf.setColor(Color.white); buf.drawOval(529+1-9,456,18,18); // 先端の○を描画
      buf.setColor(FlpCol); // 色をフリッパー専用色に設定
      buf.fillPolygon(new int[] {624+3,529,529,624-9}, new int[] {474,456,456+18,510-2},4); // フリッパー内側を専用色で埋める
      buf.setColor(Color.white); buf.drawLine(529,456,624+3,474); buf.drawLine(529,456+18,624-9,510-2); // 上下の斜線を描画
    } else if(rFlp == 1) { // 上がっている途中
      buf.fillOval(524-9,474,18,18); // 先端の●を描画
      buf.setColor(Color.white); buf.drawOval(524-9,474,18,18);  // 先端の○を描画
      buf.setColor(FlpCol); // 色をフリッパー専用色に設定
      buf.fillPolygon(new int[] {624,524,524,624-6}, new int[] {474,474,474+18,510-1},4); // フリッパー内側を専用色で埋める
      buf.setColor(Color.white); buf.drawLine(524,474,624,474); buf.drawLine(524,474+18,624-6,510-1); // 上下の斜線を描画
    } else { // 上がってない時
      buf.fillOval(529+3-9,528,18,18); // 先端の●を描画
      buf.setColor(Color.white); buf.drawOval(529+3-9,528,18,18); // 先端の○を描画
      buf.setColor(FlpCol); // 色をフリッパー専用色に設定
      buf.fillPolygon(new int[] {624-9,529,529+4,624+3}, new int[] {474+2,528,528+18,510},4); // フリッパー内側を専用色で埋める
      buf.setColor(Color.white); buf.drawLine(529,528,624-9,474+2); buf.drawLine(529+4,528+18,624+3,510); // 上下の斜線を描画
    }
    buf.setColor(FlpCol); buf.fillOval(606,474,36,36);      // 右フリッパー右の●を描画
    buf.setColor(Color.white); buf.drawOval(606,474,36,36); // 右フリッパー右の○を描画
    }
  }

  void revive() {}
}
