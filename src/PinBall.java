import java.awt.*;

public class PinBall extends Frame implements Runnable {
  // ■ フィールド変数
  Thread     th;  // Thread クラスのオブジェクトを宣言
  GameMaster gm;

  // ■ main メソッド（スタート地点）
  public static void main(String[] args){
    new PinBall();
  }

  // ■ コンストラクタ
  PinBall() {
    super("PinBall");
    int cW=800, cH=600;
    this.setSize(cW, cH);
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    gm = new GameMaster(cW,cH);
    this.add(gm);
    this.setVisible(true);

    th = new Thread(this);
    th.start();

    requestFocusInWindow();
  }

  // ■ メソッド (Runnable インターフェース用)
  public void run() {
    try {
      while (true) {
        Thread.sleep(20);
        gm.repaint();
      }
    }
    catch (Exception e) {System.out.println("Exception: " + e);}
  }
}
