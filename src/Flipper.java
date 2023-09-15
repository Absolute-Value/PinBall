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
  Color FlpCol = new Color(100,200,250);
  public static int lFlp, rFlp;

  // コンストラクタ
  Flipper () {
    lflag = false;
    rflag = false;
    sflag = false;
    f1flag= false;
    f2flag= false;
    f3flag= false;
    f4flag= false;
    f5flag= false;
    hp=1;
  }
  void move(Graphics buf,int width, int height) {
    if(lflag == true){if(lFlp < 2) lFlp++;}
    else{if(0<lFlp) lFlp--;}
    if(rflag == true){if(rFlp < 2) rFlp++;}
    else{if(0<rFlp) rFlp--;}
    /* 左フリッパー */
    buf.setColor(FlpCol);
    if (lFlp == 2) { // 上がりきっている時
      buf.fillOval(431-1-9,456,18,18); buf.setColor(Color.white);
      buf.drawOval(431-1-9,456,18,18); buf.setColor(FlpCol);
      buf.fillPolygon(new int[] {336-3,431,431,336+9}, new int[] {474,456,456+18,510-2},4);
      buf.setColor(Color.white); buf.drawLine(336-3,474,431,456); buf.drawLine(336+9,510-2,431,456+18);
    } else if(lFlp == 1) { // 上がっている途中
      buf.fillOval(436-9,474,18,18); buf.setColor(Color.white);
      buf.drawOval(436-9,474,18,18); buf.setColor(FlpCol);
      buf.fillPolygon(new int[] {336,436,436,336+6}, new int[] {474,474,474+18,510-1},4);
      buf.setColor(Color.white); buf.drawLine(336,474,436,474); buf.drawLine(336+6,510-1,436,474+18);
    } else { // 上がってない時
      buf.fillOval(431-3-9,528,18,18); buf.setColor(Color.white);
      buf.drawOval(431-3-9,528,18,18); buf.setColor(FlpCol);
      buf.fillPolygon(new int[] {336+9,431,431-4,336-3}, new int[] {474+2,528,528+18,510},4);
      buf.setColor(Color.white); buf.drawLine(336+9,474+2,431,528); buf.drawLine(336-3,510,431-4,528+18);
    }
    buf.setColor(FlpCol); buf.fillOval(318,474,36,36);
    buf.setColor(Color.white); buf.drawOval(318,474,36,36);
    /* 右フリッパー */
    buf.setColor(FlpCol);
    if (rFlp == 2){ // 上がりきっている時
      buf.fillOval(529+1-9,456,18,18); buf.setColor(Color.white);
      buf.drawOval(529+1-9,456,18,18); buf.setColor(FlpCol);
      buf.fillPolygon(new int[] {624+3,529,529,624-9}, new int[] {474,456,456+18,510-2},4);
      buf.setColor(Color.white); buf.drawLine(529,456,624+3,474); buf.drawLine(529,456+18,624-9,510-2);
    } else if(rFlp == 1) { // 上がっている途中
      buf.fillOval(524-9,474,18,18); buf.setColor(Color.white);
      buf.drawOval(524-9,474,18,18); buf.setColor(FlpCol);
      buf.fillPolygon(new int[] {624,524,524,624-6}, new int[] {474,474,474+18,510-1},4);
      buf.setColor(Color.white); buf.drawLine(524,474,624,474); buf.drawLine(524,474+18,624-6,510-1);
    } else { // 上がってない時
      buf.fillOval(529+3-9,528,18,18); buf.setColor(Color.white);
      buf.drawOval(529+3-9,528,18,18); buf.setColor(FlpCol);
      buf.fillPolygon(new int[] {624-9,529,529+4,624+3}, new int[] {474+2,528,528+18,510},4);
      buf.setColor(Color.white); buf.drawLine(529,528,624-9,474+2); buf.drawLine(529+4,528+18,624+3,510);
    }
    buf.setColor(FlpCol); buf.fillOval(606,474,36,36);
    buf.setColor(Color.white); buf.drawOval(606,474,36,36);
  }
    
  void revive() {}
}