import java.awt.*;

abstract class Object extends Canvas {
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
    if (Math.pow(this.x+this.w-obj.x-obj.w,2)+Math.pow(this.y+this.w-obj.y-obj.w,2)<=Math.pow(this.w+obj.w,2)&&obj.hp>0) {
      obj.hp--;
      GameMaster.Score_1P+=score;
      return true;
    }
    else return false;
  }
}

class ScoreBomb extends Object {
  int sx;  int sy;
  Color col;
  ScoreBomb (int s2x,int s2y,Color c2l) {
    col= c2l;
    sx = s2x;
    sy = s2y;
    x = sx;
    y = sy;
    w = h = 25;
    hp = 0;
  }
  void move(Graphics buf, int apWidth,int apHeight) {
    if (hp>0) {
      buf.setColor(col);
      buf.fillOval(x,y,2*w,2*w);
      buf.setColor(Color.white);
      buf.fillOval(x+6,y+6,2*(w-6),2*(w-6));
      buf.setColor(Color.black);
      buf.drawString("100",x+w-10,y+w+5);
    }
    if (hp==0) {
      buf.setColor(col);
      buf.fillOval(x+3,y+3,2*(w-3),2*(w-3));
      buf.setColor(Color.white);
      buf.fillOval(x+6,y+6,2*(w-6),2*(w-6));
      buf.setColor(Color.black);
      buf.drawString("100",x+w-10,y+w+5);
    }
  }
  void revive() {
    x = sx;
    y = sy;
    hp = 1;
  }
}

class Chip extends Object {
  int sx;
  int sy;
  Chip (int s2x,int s2y) {
    sx = s2x;
    sy = s2y;
    x = sx;
    y = sy;
    w = h = 7;
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
