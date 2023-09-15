import java.awt.*;

class Flipper extends Object {
  boolean lflag;
  boolean rflag;
  boolean sflag;
  boolean f1flag;
  boolean f2flag;
  boolean f3flag;
  boolean f4flag;
  boolean f5flag;
  Color FlpCol = new Color(100,200,250);
  public static int lFlp, rFlp;

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
    buf.setColor(FlpCol);
    if (lFlp == 2) {
      buf.fillPolygon(new int[] {336-3,431,336+9}, new int[] {474,456,510-2},3);
      buf.setColor(Color.white);
      buf.drawLine(336-3,474,431,456);
      buf.drawLine(336+9,510-2,431,456);
    } else if(lFlp == 1) {
      buf.fillPolygon(new int[] {336,436,336+6}, new int[] {474,474,510-1},3);
      buf.setColor(Color.white);
      buf.drawLine(336,474,436,474);
      buf.drawLine(336+6,510-1,436,474);
    } else {
      buf.fillPolygon(new int[] {336+9,431,336-3}, new int[] {474+2,528,510},3);
      buf.setColor(Color.white);
      buf.drawLine(336+9,474+2,431,528);
      buf.drawLine(336-3,510,431,528);
    }
    buf.setColor(FlpCol);
    if (rFlp == 2){
      buf.fillPolygon(new int[] {624+3,529,624-9}, new int[] {474,456,510-2},3);
      buf.setColor(Color.white);
      buf.drawLine(529,456,624+3,474);
      buf.drawLine(529,456,624-9,510-2);
    } else if(rFlp == 1) {
      buf.fillPolygon(new int[] {624,524,624-6}, new int[] {474,474,510-1},3);
      buf.setColor(Color.white);
      buf.drawLine(524,474,624,474);
      buf.drawLine(524,474,624-6,510-1);
    } else {
      buf.fillPolygon(new int[] {624-9,529,624+3}, new int[] {474+2,528,510},3);
      buf.setColor(Color.white);
      buf.drawLine(529,528,624-9,474+2);
      buf.drawLine(529,528,624+3,510);
    }
    buf.setColor(FlpCol); buf.fillOval(318,474,36,36); buf.fillOval(606,474,36,36);
    buf.setColor(Color.white); buf.drawOval(318,474,36,36); buf.drawOval(606,474,36,36);
  }
    
  void revive() {}
}