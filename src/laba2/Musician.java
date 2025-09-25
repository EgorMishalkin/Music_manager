package laba2;

public class Musician {
    private String name;
    private String instrument;
    private int age;

    // Конструктор с параметрами
    public Musician(String name, String instrument, int age) {
        this.name = name;
        this.instrument = instrument;
        this.age = age;
    }

    // Пустой конструктор (если нужен)
    public Musician() { }

    // Геттеры
    public String getName() { return name; }
    public String getInstrument() { return instrument; }
    public int getAge() { return age; }

    // Сеттеры
    public void setName(String name) { this.name = name; }
    public void setInstrument(String instrument) { this.instrument = instrument; }
    public void setAge(int age) { this.age = age; }
}
