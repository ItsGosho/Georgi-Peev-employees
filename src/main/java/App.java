import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.EmployeeCommonWorkPair;
import models.EmployeeWorkLongestPair;
import models.WorkAssignment;
import services.EmployeeProjectOverlapService;
import services.LongestWorkPairService;
import services.WorkAssignmentCSVReader;

import java.io.File;
import java.util.List;
import java.util.Set;

public class App extends Application {

    private TableView<EmployeeCommonWorkPair> table;
    private Label bestPairLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Overlap Viewer");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Button loadButton = new Button("Load CSV");
        loadButton.setOnAction(e -> this.onLoadButtonClicked(stage));

        this.bestPairLabel = new Label("Best Pair: Not loaded yet");

        VBox topContainer = new VBox(10);
        topContainer.getChildren().addAll(loadButton, this.bestPairLabel);
        root.setTop(topContainer);

        this.table = new TableView<>();
        this.setupTableColumns();
        root.setCenter(this.table);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void setupTableColumns() {
        TableColumn<EmployeeCommonWorkPair, Integer> colEmp1 = new TableColumn<>("Employee #1");
        colEmp1.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getEmployeeId1()));

        TableColumn<EmployeeCommonWorkPair, Integer> colEmp2 = new TableColumn<>("Employee #2");
        colEmp2.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getEmployeeId2()));

        TableColumn<EmployeeCommonWorkPair, Integer> colProject = new TableColumn<>("Project ID");
        colProject.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getProjectId()));

        TableColumn<EmployeeCommonWorkPair, Integer> colDays = new TableColumn<>("Days Worked");
        colDays.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDaysWorked()));

        this.table.getColumns().addAll(colEmp1, colEmp2, colProject, colDays);
    }

    private void onLoadButtonClicked(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(stage);

        if (file == null)
            return;

        WorkAssignmentCSVReader reader = new WorkAssignmentCSVReader();
        List<WorkAssignment> workAssignments = reader.read(file);

        LongestWorkPairService service = new LongestWorkPairService();
        EmployeeWorkLongestPair bestPair = service.findPairWithLongestOverlap(workAssignments);

        if (bestPair == null) {
            showAlert("No Overlaps", "No employees overlap");
            return;
        }

        this.bestPairLabel.setText("Best Pair: Employee 1: " + bestPair.employeeId1() +
                ", Employee 2: " + bestPair.employeeId2() +
                ", Days Worked: " + bestPair.daysWorked());

        EmployeeProjectOverlapService employeeProjectOverlapService = new EmployeeProjectOverlapService();
        Set<EmployeeCommonWorkPair> overlapsForWinningPair =
                employeeProjectOverlapService.findAllProjectOverlapsForPair(workAssignments, bestPair);

        this.table.getItems().setAll(overlapsForWinningPair);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
