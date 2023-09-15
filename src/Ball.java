import java.awt.*;

class Ball extends Object{
  // フィールド変数
  int x;
  int y;
  int dx;
  double dy;
  int r;

  Ball (int width, int height) {
    x = (int)Math.random()*550+210;
    y = 72;
    dx = (int)Math.random()*6-3;
    dy = 0;
    r = 15;
  }

  void move(Graphics buf,int width, int height) {
    buf.setColor(new Color(200,200,200));
    buf.fillOval(x, y, 2*r, 2*r);
    dy+=0.1;
    x=x+dx;
    y=y+(int)dy;
    if (x<210|760<x) dx = -1*dx;

    /* フリッパー当たり判定 */
    if (GameMaster.lFlp==2) {

    } else if (GameMaster.lFlp==1) {

    } else {

    }
    if (GameMaster.rFlp==2) {

    } else if (GameMaster.rFlp==1) {

    } else {

    }
    /* ステージ上 */
    if (GameMaster.mode==0) {
      if (y<72) dy = -1*dy;
      if (y>height) {
        GameMaster.mode=1;
        y=0;
      }
      if (269<y&&y<357&&x<=246) dx=-1*dx;
      if (222<y&&750<x) { // 発射装置内
        dx = 0; x=757;
      }
      if (417<y&&y<474&&x<228) {
        dx = 0; x=213;
      }
    }
    if (GameMaster.mode==1) {
      if (x<226 && 76<y && y<276) { // 左上直線
        dx = -1*dx;
        x = 226;
      }
      if (678-30<x && x<678 && y<24) dx = -1*dx;// 右上直線
      if (87-15<y && y<276-15 && 734-30<x && x<750-30) dx = -1*dx; // 右中直線
      if (750-30 <x && x<750) dx = -1*dx; // 右下直線
      if (750<x) { // 発射装置内
        dx = 0; x=757;
        if(410-33<y) dy = -0.7*dy;
      }
      if (y>height) {
        y=-30;
        dy=0;
      }
    }
  }

  void revive() {
    x=(int)Math.random()*366+282;
    y=-30;
    dx = (int)Math.random()*6-3;
    dy=0;
  }
}
