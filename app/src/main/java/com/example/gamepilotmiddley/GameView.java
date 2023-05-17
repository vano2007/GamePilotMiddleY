package com.example.gamepilotmiddley;

import android.content.Context;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    // поля
    private  Thread thread; // поле нового потока

    // конструктор на основе SurfaceView
    public GameView(Context context) {
        super(context);
    }

    // реализация метода run() дополнительного потока
    @Override
    public void run() {

    }

    // метод запуска потока
    public void resumeThread() {
        // установление флага запуска игры
        isPlaying = true;
        // создание объекта потока
        thread = new Thread(this);
        // запуск потока
        thread.start();
    }

    
}
