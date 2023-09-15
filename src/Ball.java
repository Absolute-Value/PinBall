import java.awt.*;

class Ball extends Object{
  // フィールド変数
  double v,dx,dy;

  Ball (int width, int height) {
    x = (int)Math.random()*550+210;
    y = 72;
    dx = (int)Math.random()*6-3;
    dy = 0;
    w = 15;
    hp = 2;
  }

  void move(Graphics buf,int width, int height) {
    buf.setColor(new Color(200,200,200));
    buf.fillOval(x, y, 2*w, 2*w);
    dy+=0.1;
    x=x+(int)dx;
    y=y+(int)dy;
    if (x<210|760<x) dx = -1*dx; // 両端当たり判定

    /* フリッパー当たり判定 */
    if (Flipper.lFlp==2) {
      if(336<=x&&x<=431-30&&456-30<y&&y<474-30) {
        dx-=2;
        dy=-1*dy;
      }
    } else if (Flipper.lFlp==1) {
      if(336<x&&x<436-30&&474-30<=y&&y<528-30) dy-=3;
    } else {

    }
    if (Flipper.rFlp==2) {
      if(529<=x&&x<=624-30&&456-30<y&&y<474-30) {
        dx+=3;
        dy=-1*dy;
      }
    } else if (Flipper.rFlp==1) {
      if(524<x&&x<624-30&&474-30<=y&&y<528-30) dy-=3;
    } else {

    }
    if ((Math.pow(x+w-318-18,2)+Math.pow(y+w-474-18,2)<=Math.pow(w+18,2))|(Math.pow(x+w-606-18,2)+Math.pow(y+w-474-18,2)<=Math.pow(w+18,2))) {
      if ((319<=x&&x<=323)|(606<=x&&x<=612)) {
        dy=-0.9*dy;
        if (x<=321|(400<x&&x<=609)) dx-=0.1;
        if ((321<x&&x<400)|609<x) dx+=0.1;
      } else if (475<=y&&y<=479) {
        dx=-0.95*dx;
      } else {
        dx=-0.95*dx;  dy=-0.9*dy;
      }
    }
    v=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
    switch(GameMaster.mode){
    case 0: // 上ステージ
      if (y>height) {
        GameMaster.mode=1;
        y=y-height;
        break;
      }
      /* 右上曲線 */
      if (640<x&&y<222-15) {
        dx=-v*Math.sin(Math.atan2((222-y-15),(x+18-640)));
        dy=-v*Math.cos(Math.atan2((222-y-15),(x+18-640)));
      }
      /* フリッパー左 */
      LineColl(246,432,246,600); // 左
      Line45Coll(246,432,270,408); // 左上斜め45
      Line135Coll(270,408,318,456); // 右上斜め45
      LineColl(318,456,318,600); // 右中直線
      /* 外郭 */
      Line225Coll(210,408,246,372); // 左斜め45
      LineColl(246,302,246,372); // 左中直線
      LineColl(200,72,800,72); // 上限
      /* 左上の島 */
      LineColl(350,108,358,108); // 上直線
      LineColl(420,144,420,182); // 右直線
      Line225Coll(318,284,420,182); // 右下斜め45
      LineColl(282,284,318,284); // 下直線
      /* フリッパー右 */
      LineColl(642,456,642,600); // フリッパー右直線
      Line45Coll(642,456,690,408); // 右下斜め45左
      Line135Coll(690,408,714,432); // 右下斜め45右
      LineColl(714,384,738,408); // 右下斜め45右上
      LineColl(750,218,750,348); // 右中直線
      LineColl(672,158,690,158); LineColl(540,182,630,182);
      LineColl(540,144,540,182); LineColl(602,108,640,108);
      LineColl(754,222,754,600); // 右直線
      /* 右島 */
      Line315Coll(648,218,690,260); // 右島左上斜め45
      LineColl(690,260,690,336); // 右島左
      LineColl(714,218,714,336); // 右島右
      /* 上のちょんちょん */
      DiaColl(new int[] {501,498,498,501,504,504},new int[] {144,147,170,173,170,147});
      DiaColl(new int[] {459,456,456,459,462,462},new int[] {144,147,170,173,170,147});
      if (222<y&&750<x) { // 発射装置内
        dx = 0; x=757;
      }
      break;
    case 1: // 下ステージ
      if (y<-15) {
        GameMaster.mode=0;
        y=height+y;
        break;
      }
      /* 左端 */
      LineColl(246,-30,246,17); // 左上の島直線左
      LineColl(264,48,282,48); // 左上の島直線中
      Line225Coll(282,48,318,12); // 左上斜め45
      LineColl(318,-30,318,12); // 左上の島直線右
      LineColl(226,76,226,276); // 左中直線
      Line135Coll(226,276,250,300); // 左中斜め45
      Line135Coll(210,492,318,600); // 左下斜め45
      /* 右端 */
      LineColl(642,0,642,12); // 右上直線
      Line315Coll(642,12,678,48); // 右上斜め45
      LineColl(734,87,734,276); // 右中直線
      Line45Coll(710, 300, 734, 276); // 右中斜め135
      Line315Coll(710,300,750,340); // 右中斜め45
      LineColl(750,340,750,492); // 右下直線
      Line45Coll(642, 600, 750, 492); // 右下斜め45
      /* レーン */
      LineColl(246,379,246,432); // 左レーン左
      LineColl(246,379,250,379); // 左レーン上
      LineColl(250,379,250,429); // 左レーン右
      LineColl(286,379,286,416); // 左レーン三角左
      LineColl(674,379,674,416); // 右レーン三角右
      LineColl(710,379,710,429); // 右レーン左
      LineColl(710,379,714,379); // 右レーン上
      LineColl(714,379,714,432); // 右レーン右
      /* 上のちょんちょん */
      DiaColl(new int[] {585,582,582,585,588,588},new int[] {100,103,126,129,126,103});
      DiaColl(new int[] {543,540,540,543,546,546},new int[] {100,103,126,129,126,103});
      DiaColl(new int[] {501,498,498,501,504,504},new int[] {100,103,126,129,126,103});
      DiaColl(new int[] {459,456,456,459,462,462},new int[] {100,103,126,129,126,103});
      DiaColl(new int[] {417,414,414,417,420,420},new int[] {100,103,126,129,126,103});
      DiaColl(new int[] {375,372,372,375,378,378},new int[] {100,103,126,129,126,103});
      if (750<x) { // 発射装置内
        dx = 0; x=757;
        if(410-33<y) {
          dy = -0.5*dy;
          y = 410-33;
        }
      }
      if (y<0) {
        GameMaster.mode=0;
        y=height;
      }
    }
  }

  void revive() {
    x=757; y=350;
    dx=0; dy=0;
  }

  void LineColl (int x1,int y1,int x2,int y2) {
    if(x1==x2 && x1-2*w<=x && x<=x1 && y1-w<=y && y<=y2-w) dx = -dx;
    if(y1==y2 && x1-w<=x && x<=x2-w && y1-2*w<=y && y<=y1) dy = -dy;
  }

  void Line45Coll (int x1, int y1, int x2, int y2) {
    if (x1-15<=x && x<=x2-15 && y2-15<=y && y<=y1-15 && y>=-x+(x2+y2-42)) {
      dx=-0.9*v*Math.cos(Math.atan(dx/dy));
      dy=0.9*v*Math.sin(Math.atan(dx/dy));
      System.out.println("当たった");
    }
  }

  void Line135Coll (int x1, int y1, int x2, int y2) {
    if (x1-15<=x && x<=x2-15 && y1-15<=y && y<=y2-15 && y>=x+(y2-x2-21)) {
      dx=0.9*v*Math.cos(Math.atan(dx/dy));
      dy=-0.9*v*Math.sin(Math.atan(dx/dy));
      System.out.println("当たった");
    }
  }

  void Line225Coll (int x1, int y1, int x2, int y2) {
    if (x1-15<=x && x<=x2-15 && y2-15<=y && y<=y1-15 && y<=-x+y2+x2) {
      dx=-0.9*v*Math.cos(Math.atan(dx/dy));
      dy=0.9*v*Math.sin(Math.atan(dx/dy));
      System.out.println("当たった");
    }
  }

  void Line315Coll (int x1, int y1, int x2, int y2) {
    if (x1-15<=x && x<=x2-15 && y1-15<=y && y<=y2-15 && y<=x+y2-x2) {
      dx=-0.9*v*Math.cos(Math.atan(dx/dy));
      dy=0.9*v*Math.sin(Math.atan(dx/dy));
      System.out.println("当たった");
    }
  }

  void DiaColl (int[] a,int[] b) {
    Line45Coll(a[1],b[1],a[0],b[0]);
    LineColl(a[1],b[1],a[2],b[2]);
    Line315Coll(a[2],b[2],a[3],b[3]);
    Line225Coll(a[3],b[3],a[4],b[4]);
    LineColl(a[4],b[4],a[5],b[5]);
    Line135Coll(a[0],b[0],a[5],b[5]);
  }
}
