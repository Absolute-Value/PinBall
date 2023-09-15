import java.awt.*;

abstract class Object {
  int x;
  int y;
  int w;
  int h;
  int hp;

  Object () {
    x = 500;
    y = 300;
    w = h = 15;
    hp = 1;
  }

  abstract void move(Graphics buf,int width, int height);

  abstract void revive();

  boolean collisionCheck(Object obj,int score){
    if (Math.pow(this.x-obj.x,2)+Math.pow(this.y-obj.y,2)<Math.pow(this.w+obj.w,2)) {
      obj.hp--;
      return true;
    }
    else return false;
  }
}

class Chip extends Object {
  int sx;
  int sy;
  Chip (int sx,int sy) {
    x = sx;
    y = sy;
    w = h = 9;
    hp = 0;
  }
  void move(Graphics buf, int apWidth,int apHeight) {
    if (hp>0) {
      buf.setColor(new Color(255,255,100));
      buf.fillOval(x,y,2*w,2*w);
      buf.setColor(Color.white);
      buf.drawOval(x,y,2*w,2*w);
    }
  }
  void revive() {
    x = sx;
    y = sy;
    hp = 1;
  }
}
