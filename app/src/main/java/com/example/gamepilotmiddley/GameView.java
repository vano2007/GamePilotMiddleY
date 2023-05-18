package com.example.gamepilotmiddley;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    // поля
    private  Thread thread; // поле нового потока
    private boolean isPlaying; // поле запуска и приостановления игры
    private Background background1, background2; // поля работы с фоном (необходимо два, что было непрерывное движение фона)
    private int screenX, screenY; // поля размеров экрана по осям X и Y
    private Paint paint; // поле стилей рисования
    private float screenRatioX, screenRatioY; // поля размеров экрана для совместимости разных размеров экрана
    private Flight flight; // создание поля самолёта

    // конструктор на основе SurfaceView
    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 1920f / screenX; // калибровка совместимости оси X
        screenRatioY = 1080f / screenY; // калибровка совместимости оси Y

        // создание объектов фонов (размеры, ранее созданный ресурс)
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        // присваивание полю x класса Background переменной ширины screenX
        background2.setX(screenX); // второй фон мы сдвигаем по оси Х с нуля на размер ширины изображения

        paint = new Paint(); // создание объекта стиля рисования

        // создание объекта самолёта
        flight = new Flight(screenX, screenY, getResources());
    }

    // реализация метода run() дополнительного потока
    @Override
    public void run() {
        // операции в потока
        while (isPlaying) {
            // методы запускаемые в потоке
            update();
            draw();
            sleep();
        }
    }

    // метод обновления потока
    private void update() {
        // сдвиг фона по оси X на 10 пикселей и преобразование для совместимости разных экранов
        background1.setX(background1.getX() - (int)(10 * screenRatioX));
        background2.setX(background2.getX() - (int)(10 * screenRatioX));

        if ((background1.getX() + background1.getBackground().getWidth()) <= 0) { // если фон 1 полностью исчез с экрана
            background1.setX(screenX); // то обновление x до размера ширины фона
        }
        if ((background2.getX() + background2.getBackground().getWidth()) <= 0) { // если фон 2 полностью исчез с экрана
            background2.setX(screenX); // то обновление x до размера ширины фона
        }

        // задание скорости подъёма и снижения самолёта
        if (flight.isGoingUp()) { // условие подъёма
            flight.setY(flight.getY() - (int)(30 * screenRatioY));
        } else { // условие снижения
            flight.setY(flight.getY() + (int)(30 * screenRatioY));
        }
        // задание порога значений местоположения самолёта
        if (flight.getY() < 0) { // запрет на снижение меньше нуля
            flight.setY(0);
        } else if (flight.getY() >= screenY - flight.getHeight()) { // запрет на подъём выше экрана за минусом высоты самолёта
            flight.setY(screenY - flight.getHeight());
        }
    }
    // метод рисования в потоке
    private void draw() {

        if (getHolder().getSurface().isValid()) { // проверка валидности объекта surface

            Canvas canvas = getHolder().lockCanvas(); // метод lockCanvas() возвращает объект Canvas (холст для рисования)
            // метод drawBitmap() рисует растровое изображение фона на холсте (изображение, координаты X и Y, стиль для рисования)
            canvas.drawBitmap(background1.getBackground(), background1.getX(), background1.getY(), paint);
            canvas.drawBitmap(background2.getBackground(), background2.getX(), background2.getY(), paint);

            // отрисовка растрового изображения самолёта
            canvas.drawBitmap(flight.getFlight(), flight.getX(), flight.getY(), paint);

            // вывод нарисованных изображений на экран
            getHolder().unlockCanvasAndPost(canvas);
        }
    }
    // метод засыпания потока
    private void sleep() {
        try {
            // засыпание потока на 16 милисекунд
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    // метод паузы потока
    public void pauseThread() {
        try {
            // установление флага приостановления игры
            isPlaying = false;
            // приостановление потока
            thread.join();
            /**
             * метод join() — используется для того,
             * чтобы приостановить выполнение текущего потока до тех пор,
             * пока другой поток не закончит свое выполнение
             */
        } catch (InterruptedException e) { // исключение на случай зависания потока
            e.printStackTrace();
        }
    }
    // метод обработки касания экрана (для управления самолётом)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // обработка событий касания экрана
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                // если пользователь нажал на левую сторону экрана
                if (event.getX() < (screenX / 2)) {
                    // то движение самолёта вверх
                    flight.setGoingUp(true);
                    // если пользователь нажал на правую сторону экрана
                } else if (event.getX() >= (screenX / 2)){
                    // то самолет должен встать в начальную точку
                    flight.setY(screenY/2);
                    flight.setX(screenX/21);
                }
                break;
            case MotionEvent.ACTION_MOVE: // движение по экрану

                break;
            case MotionEvent.ACTION_UP: // отпускание
                // при отпускании экрана самолёт начнёт снижаться
                flight.setGoingUp(false);
                break;
        }

        return true; // активация обработки касания экрана
    }


}
