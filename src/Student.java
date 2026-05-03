import javafx.beans.property.*;

public class Student {

    private StringProperty name;
    private IntegerProperty id;
    private DoubleProperty marks;

    public Student(String name, int id, double marks) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty(id);
        this.marks = new SimpleDoubleProperty(marks);
    }

    public String getName() { return name.get(); }
    public double getMarks() { return marks.get(); }

    public StringProperty nameProperty() { return name; }
    public IntegerProperty idProperty() { return id; }
    public DoubleProperty marksProperty() { return marks; }
}