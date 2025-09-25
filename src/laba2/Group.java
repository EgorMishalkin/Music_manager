package laba2;

import java.util.List;
import java.util.ArrayList;

public class Group {
    // Переменные
    private String name;
    private List<Musician> members; // список музыкантов
    private int foundedYear;
    private int chartPosition;
    // (альбомы и туры пока опустим для простоты)

    // Конструктор
    public Group(String name, int foundedYear) {
        this.name = name;
        this.foundedYear = foundedYear;
        this.members = new ArrayList<>();
    }

    // Методы
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void addMember(Musician musician) { members.add(musician); }
    public void removeMember(Musician musician) { members.remove(musician); }
    public List<Musician> getMembers() { return members; }

    public void changeChartPosition(int pos) { this.chartPosition = pos; }
    public int getChartPosition() { return chartPosition; }
}
