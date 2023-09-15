/**
 * ピンボールのプログラム
 * このクラスでは、
 * 1. フレームの作成
 * 2. スレッドの逐次進行
 * のみをを行う。ゲームの内容の更新等は GameMaster クラスで行う
 *
 * @author Keisuke
 */

import java.awt.*;

public class PinBall extends Frame implements Runnable {
  // ■ フィールド変数
  Thread     th;  // Thread クラスのオブジェクトを宣言
  GameMaster gm;  // ゲームの進行を担当するクラス

  // ■ main メソッド（スタート地点）
  public static void main(String[] args){
    new PinBall(); // 自分自身のオブジェクトを作成
  }

  // ■ コンストラクタ
  PinBall() {
    super("PinBall"); // 親クラスのコンストラクタを呼び出す
    int cW=800, cH=600; // キャンバスのサイズ
    this.setSize(cW+10, cH+40); // フレームのサイズを指定
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // キャンバスをフレームに配置

    gm = new GameMaster(cW,cH);// GameMaster クラスのオブジェクトを作成
    this.add(gm);              // キャンバスにフレーム配置
    this.setVisible(true);     // 可視化

    th = new Thread(this);     // Thread クラスのオブジェクトの作成
    th.start();                // 最後にスレッドを start メソッドで開始

    requestFocusInWindow();    // フォーカスを得る
  }

  // ■ メソッド (Runnable インターフェース用)
  public void run() {
    try {
      while (true) { // 無限ループ
        Thread.sleep(20); // ウィンドウを更新する前に指定時間だけ休止
        gm.repaint();     // 再描画を要求する． repaint() は update() を呼び出す
      }
    }
    catch (Exception e) {System.out.println("Exception: " + e);}
  }
}
