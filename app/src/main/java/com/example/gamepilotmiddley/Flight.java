package com.example.gamepilotmiddley;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Flight {
    // поля
    private int x = 0, y = 0; // смещение самолёта по направлениям осей координат
    private int width, height; // ширина и высота для растрового изображения
    private int wingCounter = 0; // поле счётчика крыла
    private Bitmap flight1, flight2; // поле растровых изображений

    private boolean isGoingUp = false; // направление движения самолёта (true - вверх, false - вниз)

    // конструктор (размеры по оси X и Y, ресурс)
    public Flight(int screenX, int screenY, Resources resources) {
        // считывание изображений летящих самолётов из ресурсов
        flight1 = BitmapFactory.decodeResource(resources, R.drawable.plane_fly1);
        flight2 = BitmapFactory.decodeResource(resources, R.drawable.plane_fly2);

        // инициализация размеров самолёта с масштабированием
        width = flight1.getWidth() / 3;
        height = flight1.getHeight() / 3;

        // приведение размера самолёта совместимым с другими экранами
        width = (int)(width * 1920f / screenX);
        height = (int)(height * 1080f / screenY);

        // изменение размера изображения самолёта, где width и height соответственно ширина и высота
        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        // начальное местоположение полёта
        y = screenY / 2; // посередине оси Y
        x = screenX / 21; // практически вначале оси X
    }

    // метод задания очерёдности переключения изображений полёта
    public Bitmap getFlight() {

        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        } else if(wingCounter > 0) {
            wingCounter--;
            return flight2;
        }

        return null;
    }

    // геттеры и сеттеры
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isGoingUp() {
        return isGoingUp;
    }

    public void setGoingUp(boolean goingUp) {
        isGoingUp = goingUp;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
