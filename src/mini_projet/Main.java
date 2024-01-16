package mini_projet;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.Optional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    Stage primaryStage;
    private TableView<Student> tableView;
    private ObservableList<Student> studentList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Student Registration");

        // table student
        TableColumn<Student, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));

        TableColumn<Student, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));

        TableColumn<Student, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));

        TableColumn<Student, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));

        TableColumn<Student, String> cneColumn = new TableColumn<>("CNE");
        cneColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("cne"));

        TableColumn<Student, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("age"));

        TableColumn<Student, String> filiereColumn = new TableColumn<>("Major");
        filiereColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("filiere"));

        TableColumn<Student, String> anneeColumn = new TableColumn<>("Year");
        anneeColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("annee"));

        // table view
        tableView = new TableView<>();
        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, cneColumn, ageColumn,
                filiereColumn, anneeColumn);

        // buttons
        Button addButton = new Button("Add Student");
        Button updateButton = new Button("Update Student");
        Button deleteButton = new Button("Delete Student");
        Button viewButton = new Button("View Student Profile");
        Button printButton = new Button("Print Summary Sheet");

        // events
        addButton.setOnAction(e -> openAddStudentLayout());
        updateButton.setOnAction(e -> openUpdateStudentLayout());
        deleteButton.setOnAction(e -> openDeleteStudentLayout());
        viewButton.setOnAction(e -> openViewStudentLayout());
        printButton.setOnAction(e -> printSummarySheet());

        // HBox for buttons
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, viewButton, printButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        // VBox for the table + buttons
        VBox vbox = new VBox(tableView, buttonBox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox);

        // CSS
        String cssFile = getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(cssFile);

        primaryStage.setScene(scene);
        primaryStage.show();

        // set table view
        studentList = FXCollections.observableArrayList();
        tableView.setItems(studentList);

        // Load students
        loadStudents();
    }

    // load students
    private void loadStudents() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String cne = resultSet.getString("cne");
                int age = resultSet.getInt("age");
                String filiere = resultSet.getString("filiere");
                String annee = resultSet.getString("annee");

                Student student = new Student(id, firstName, lastName, email, cne, age, filiere, annee);
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add Student layout
    private void openAddStudentLayout() {
        Stage addStudentStage = new Stage();
        addStudentStage.setTitle("Add Student");

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameTextField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameTextField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailTextField = new TextField();

        Label cneLabel = new Label("CNE:");
        TextField cneTextField = new TextField();

        Label ageLabel = new Label("Age:");
        TextField ageTextField = new TextField();

        Label filiereLabel = new Label("Major:");
        TextField filiereTextField = new TextField();

        Label anneeLabel = new Label("Annee:");
        TextField anneeTextField = new TextField();

        Button addButton = new Button("Add Student");

        // btn event
        addButton.setOnAction(e -> {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String email = emailTextField.getText();
            String cne = cneTextField.getText();
            int age = Integer.parseInt(ageTextField.getText());
            String filiere = filiereTextField.getText();
            String annee = anneeTextField.getText();

            // Insert
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO students (firstName, lastName, email, cne, age, filiere,annee) VALUES (?, ?, ?, ?, ?, ?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query,
                        PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, cne);
                preparedStatement.setInt(5, age);
                preparedStatement.setString(6, filiere);
                preparedStatement.setString(7, annee);
                preparedStatement.executeUpdate();

                // Get ID
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);

                    // Create a Student
                    Student student = new Student(id, firstName, lastName, email, cne, age, filiere, annee);
                    studentList.add(student);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            // Clear the input
            firstNameTextField.clear();
            lastNameTextField.clear();
            emailTextField.clear();
            cneTextField.clear();
            ageTextField.clear();
            filiereTextField.clear();
            anneeTextField.clear();

            // Close Student layout
            addStudentStage.close();
        });

        // Create a grid pane layout for the add student form
        GridPane addGridPane = new GridPane();
        addGridPane.setPadding(new Insets(10));
        addGridPane.setVgap(10);
        addGridPane.setHgap(10);
        addGridPane.add(firstNameLabel, 0, 0);
        addGridPane.add(firstNameTextField, 1, 0);
        addGridPane.add(lastNameLabel, 0, 1);
        addGridPane.add(lastNameTextField, 1, 1);
        addGridPane.add(emailLabel, 0, 2);
        addGridPane.add(emailTextField, 1, 2);
        addGridPane.add(cneLabel, 0, 3);
        addGridPane.add(cneTextField, 1, 3);
        addGridPane.add(ageLabel, 0, 4);
        addGridPane.add(ageTextField, 1, 4);
        addGridPane.add(filiereLabel, 0, 5);
        addGridPane.add(filiereTextField, 1, 5);
        addGridPane.add(anneeLabel, 0, 6);
        addGridPane.add(anneeTextField, 1, 6);
        addGridPane.add(addButton, 0, 7);

        // Set the scene with the add student layout
        Scene scene = new Scene(addGridPane);
        addStudentStage.setScene(scene);
        addStudentStage.show();
    }

    // Update Student layout
    private void openUpdateStudentLayout() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            Dialog<Student> dialog = new Dialog<>();
            dialog.setTitle("Update Student");
            dialog.setHeaderText("Update Student Information");

            TextField firstNameField = new TextField(selectedStudent.getFirstName());
            TextField lastNameField = new TextField(selectedStudent.getLastName());
            TextField emailField = new TextField(selectedStudent.getEmail());
            TextField cneField = new TextField(selectedStudent.getCne());
            TextField ageField = new TextField(String.valueOf(selectedStudent.getAge()));
            TextField filiereField = new TextField(selectedStudent.getFiliere());
            TextField anneeField = new TextField(selectedStudent.getAnnee());

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20));

            gridPane.add(new Label("First Name:"), 0, 0);
            gridPane.add(firstNameField, 1, 0);
            gridPane.add(new Label("Last Name:"), 0, 1);
            gridPane.add(lastNameField, 1, 1);
            gridPane.add(new Label("Email:"), 0, 2);
            gridPane.add(emailField, 1, 2);
            gridPane.add(new Label("CNE:"), 0, 3);
            gridPane.add(cneField, 1, 3);
            gridPane.add(new Label("Age:"), 0, 4);
            gridPane.add(ageField, 1, 4);
            gridPane.add(new Label("Major:"), 0, 5);
            gridPane.add(filiereField, 1, 5);
            gridPane.add(new Label("Year:"), 0, 6);
            gridPane.add(anneeField, 1, 6);

            dialog.getDialogPane().setContent(gridPane);

            // Add update and cancel buttons
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            // Convert the result to a student object when update button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType) {
                    selectedStudent.setFirstName(firstNameField.getText());
                    selectedStudent.setLastName(lastNameField.getText());
                    selectedStudent.setEmail(emailField.getText());
                    selectedStudent.setCne(cneField.getText());
                    selectedStudent.setAge(Integer.parseInt(ageField.getText()));
                    selectedStudent.setFiliere(filiereField.getText());
                    selectedStudent.setAnnee(anneeField.getText());
                    return selectedStudent;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(updatedStudent -> {
                // Update student
                updateStudent(updatedStudent);
                // Refresh table
                tableView.refresh();
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student to update.");

            // Get the dialog pane
            DialogPane dialogPane = alert.getDialogPane();

            // Apply custom CSS to the dialog pane
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // Set the class for the button
            ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
            buttonBar.getStyleClass().add("my-button-bar");

            // Show the alert and wait for user response
            alert.showAndWait();

        }
    }

    // update the student
    private void updateStudent(Student student) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE students SET firstName = ?, lastName = ?, email = ?, cne = ?, age = ?, filiere = ?, annee = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getCne());
            preparedStatement.setInt(5, student.getAge());
            preparedStatement.setString(6, student.getFiliere());
            preparedStatement.setString(7, student.getAnnee());
            preparedStatement.setInt(8, student.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Student
    private void openDeleteStudentLayout() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();

        if (selectedStudent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Student");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Are you sure you want to delete this student?");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            dialogPane.getStyleClass().add("alert_confirmation");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the student from the database
                deleteStudent(selectedStudent);
                // Remove the student from the table view
                tableView.getItems().remove(selectedStudent);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student to delete.");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
            buttonBar.getStyleClass().add("my-button-bar");

            alert.showAndWait();

        }
    }

    // delete selected student
    private void deleteStudent(Student student) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM students WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, student.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View Student Profile layout
    private void openViewStudentLayout() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            Stage viewStudentProfileStage = new Stage();
            viewStudentProfileStage.setTitle("View Student Profile");

            Label idLabel = new Label("ID: ");
            Label firstNameLabel = new Label("First Name: ");
            Label lastNameLabel = new Label("Last Name: ");
            Label emailLabel = new Label("Email: ");
            Label cneLabel = new Label("CNE: ");
            Label ageLabel = new Label("Age: ");
            Label fillierLabel = new Label("Major: ");
            Label annLabel = new Label("Year : ");

            Text idData = new Text("" + selectedStudent.getId());
            Text firstNameData = new Text("" + selectedStudent.getFirstName());
            Text lastNameData = new Text("" + selectedStudent.getLastName());
            Text emailData = new Text("" + selectedStudent.getEmail());
            Text cneData = new Text("" + selectedStudent.getCne());
            Text ageData = new Text("" + selectedStudent.getAge());
            Text fillierData = new Text("" + selectedStudent.getFiliere());
            Text annData = new Text("" + selectedStudent.getAnnee());

            Button backButton = new Button("Back");
            backButton.setOnAction(e -> {
                viewStudentProfileStage.close();
                primaryStage.show();
            });

            GridPane viewGridPane = new GridPane();
            viewGridPane.getStyleClass().add("grid_view");
            viewGridPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            viewGridPane.setPadding(new Insets(10));
            viewGridPane.setVgap(20);
            viewGridPane.setHgap(40);
            viewGridPane.setAlignment(Pos.CENTER);

            viewGridPane.add(idLabel, 0, 0);
            viewGridPane.add(firstNameLabel, 0, 1);
            viewGridPane.add(lastNameLabel, 0, 2);
            viewGridPane.add(emailLabel, 0, 3);
            viewGridPane.add(cneLabel, 0, 4);
            viewGridPane.add(ageLabel, 0, 5);
            viewGridPane.add(fillierLabel, 0, 6);
            viewGridPane.add(annLabel, 0, 7);

            viewGridPane.add(idData, 1, 0);
            viewGridPane.add(firstNameData, 1, 1);
            viewGridPane.add(lastNameData, 1, 2);
            viewGridPane.add(emailData, 1, 3);
            viewGridPane.add(cneData, 1, 4);
            viewGridPane.add(ageData, 1, 5);
            viewGridPane.add(fillierData, 1, 6);
            viewGridPane.add(annData, 1, 7);
            viewGridPane.add(backButton, 0, 8);

            Scene viewStudentProfileScene = new Scene(viewGridPane, 700, 600);

            viewStudentProfileStage.setScene(viewStudentProfileScene);
            viewStudentProfileStage.isFocused();

            viewStudentProfileStage.show();
        } else {
            // No student selected, display a warning message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Student Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to view their profile.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
            buttonBar.getStyleClass().add("my-button-bar");

            alert.showAndWait();
        }
    }

    // print the summary sheet in PDF
    private int i = 0;
    private void printSummarySheet() {
        Student selectedStudent = tableView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            Document document = new Document();
            try {
                i++;
                PdfWriter.getInstance(document, new FileOutputStream("summary_sheet"+i+".pdf"));
                document.open();

                Font titleFont = FontFactory.getFont("Helvetica-Bold", 20, Font.BOLD, BaseColor.GRAY);
                Font infoFont = FontFactory.getFont("Arial", 12, Font.NORMAL, BaseColor.BLACK);
                Font label = FontFactory.getFont("Helvetica-Bold", 12, Font.BOLD, BaseColor.DARK_GRAY);
                Paragraph title = new Paragraph("Summary Sheet", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                Paragraph studentInfo = new Paragraph();
                studentInfo.setFont(infoFont);

                studentInfo.add(new Chunk("ID: \t",  label).setTextRise(2f));
                studentInfo.add(new Chunk("" + selectedStudent.getId()));
                studentInfo.add(Chunk.NEWLINE);
                studentInfo.add(new Chunk("First Name: ", label).setTextRise(2f));
                studentInfo.add(new Chunk(selectedStudent.getFirstName()));
                studentInfo.add(Chunk.NEWLINE);
                studentInfo.add(new Chunk("Last Name: ", label).setTextRise(2f));
                studentInfo.add(new Chunk(selectedStudent.getLastName()));
                studentInfo.add(Chunk.NEWLINE);
                studentInfo.add(new Chunk("Email: ", label).setTextRise(2f));

                studentInfo.add(new Chunk(selectedStudent.getEmail()));
                studentInfo.add(Chunk.NEWLINE);
                studentInfo.add(new Chunk("CNE: ", label).setTextRise(2f));

                studentInfo.add(new Chunk(selectedStudent.getCne()));
                studentInfo.add(Chunk.NEWLINE);

                studentInfo.add(new Chunk("Age: ", label).setTextRise(2f));

                studentInfo.add(new Chunk(String.valueOf(selectedStudent.getAge())));
                studentInfo.add(Chunk.NEWLINE);
                studentInfo.add(new Chunk("Major: ", label).setTextRise(2f));

                studentInfo.add(new Chunk(selectedStudent.getFiliere()));
                studentInfo.add(Chunk.NEWLINE);
                studentInfo.add(new Chunk("Year: ", label).setTextRise(2f));

                studentInfo.add(new Chunk(selectedStudent.getAnnee()));

                document.add(studentInfo);

                document.close();
                System.out.println("Summary sheet printed successfully.");

            } catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            // No student selected, display a warning message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Student Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student to print the summary sheet.");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
            buttonBar.getStyleClass().add("my-button-bar");
            alert.showAndWait();
        }
    }
}