import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private XYChart.Series<String, Number> series = new XYChart.Series<>();

    @Override
    public void start(Stage stage) {

        // TITLE
        Label title = new Label("Student Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

        // TABLE
        TableView<Student> table = new TableView<>();

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Student, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<Student, Number> marksCol = new TableColumn<>("Marks");
        marksCol.setCellValueFactory(data -> data.getValue().marksProperty());

        // SAFE ADD (no varargs warning)
        table.getColumns().clear();
        table.getColumns().add(nameCol);
        table.getColumns().add(idCol);
        table.getColumns().add(marksCol);

        table.setItems(studentList);

        // CHART
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Student Performance");
        chart.getData().add(series);
        chart.setAnimated(true);

        // INPUTS
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField marksField = new TextField();
        marksField.setPromptText("Marks");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by Name");

        // BUTTONS
        Button addBtn = new Button("Add");
        Button deleteBtn = new Button("Delete");
        Button statsBtn = new Button("Stats");

        Label result = new Label();

        // ADD
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int id = Integer.parseInt(idField.getText());
                double marks = Double.parseDouble(marksField.getText());

                Student s = new Student(name, id, marks);
                studentList.add(s);

                series.getData().add(new XYChart.Data<>(name, marks));

                animateNode(chart);

                nameField.clear();
                idField.clear();
                marksField.clear();

            } catch (Exception ex) {
                result.setText("Invalid Input!");
            }
        });

        // DELETE
        deleteBtn.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                studentList.remove(selected);
                refreshChart();
            }
        });

        // STATS
        statsBtn.setOnAction(e -> {
            if (studentList.isEmpty()) return;

            double total = 0, max = Double.MIN_VALUE, min = Double.MAX_VALUE;

            for (Student s : studentList) {
                double m = s.getMarks();
                total += m;
                if (m > max) max = m;
                if (m < min) min = m;
            }

            double avg = total / studentList.size();
            result.setText("Avg: " + avg + " | High: " + max + " | Low: " + min);
        });

        // SEARCH
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            table.setItems(studentList.filtered(s ->
                    s.getName().toLowerCase().contains(newVal.toLowerCase())
            ));
        });

        // LAYOUT
        HBox inputs = new HBox(10);
        inputs.getChildren().addAll(nameField, idField, marksField, addBtn);

        HBox actions = new HBox(10);
        actions.getChildren().addAll(deleteBtn, statsBtn);

        VBox left = new VBox(15);
        left.getChildren().addAll(title, searchField, table, inputs, actions, result);
        left.setPadding(new Insets(20));
        left.setPrefWidth(450);

        chart.setPrefWidth(500);

        HBox root = new HBox(20);
        root.getChildren().addAll(left, chart);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add("style.css");

        // ANIMATION
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), root);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), root);
        slide.setFromX(-200);
        slide.setToX(0);

        new ParallelTransition(fade, slide).play();

        stage.setTitle("Advanced Student Manager");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshChart() {
        series.getData().clear();
        for (Student s : studentList) {
            series.getData().add(new XYChart.Data<>(s.getName(), s.getMarks()));
        }
    }

    private void animateNode(javafx.scene.Node node) {
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.3), node);
        scale.setFromX(0.9);
        scale.setToX(1);
        scale.setFromY(0.9);
        scale.setToY(1);
        scale.play();
    }

    public static void main(String[] args) {
        launch();
    }
}