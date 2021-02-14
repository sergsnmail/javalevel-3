package com.sergsnmail.lesson1.task3;

import java.util.ArrayList;
import java.util.List;

class Box<T extends Fruit> {

    private final List<T> content;

    public Box() {
        this.content = new ArrayList<>();
    }

    public float getWeight(){
        float result = 0.0F;
        for (T element :content) {
            result += element.getWeight();
        }
        return result;
    }

    public void add(T element){
        if (element == null){
            throw new NullPointerException();
        }
        content.add(element);
    }

    public boolean compareTo(Box<?> otherBox) {
        if (otherBox == null){
            throw new NullPointerException();
        }
        return getWeight() == otherBox.getWeight();
    }

    public void moveTo(Box<T> otherBox){
        if (otherBox == null){
            throw new NullPointerException();
        }
        otherBox.content.addAll(content);
        content.clear();
    }

    public int getQuantity(){
        return content.size();
    }

    public List<T> getContent() {
        return content;
    }

}
