package cas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * MainApp — Clinic Appointment System (JavaFX)
 *
 * Layout:
 *   ┌──────────────────────────────────────────────────┐
 *   │  Header (title bar)                              │
 *   ├──────────┬───────────────────────────────────────┤
 *   │  Sidebar │  Content Panel (swappable)            │
 *   │  (nav)   │                                       │
 *   └──────────┴───────────────────────────────────────┘
 *
 * Screens: Dashboard · Add Patient · View Patients ·
 *          Add Doctor · View Doctors · Book Appointment · View Appointments
 */
public class MainApp extends Application {

    // ─────────────────────────────────────────────────────────────
    //  Colours & style constants (inline CSS — no external file)
    // ─────────────────────────────────────────────────────────────
    private static final String CLR_SIDEBAR_BG   = "#1e2a3a";
    private static final String CLR_SIDEBAR_SEL  = "#2e4a6e";
    private static final String CLR_HEADER_BG    = "#16202e";
    private static final String CLR_CONTENT_BG   = "#f0f4f8";
    private static final String CLR_CARD_BG      = "#ffffff";
    private static final String CLR_ACCENT_BLUE  = "#2563eb";
    private static final String CLR_ACCENT_GREEN = "#16a34a";
    private static final String CLR_ACCENT_AMBER = "#d97706";
    private static final String CLR_TEXT_DARK    = "#1e293b";
    private static final String CLR_TEXT_MUTED   = "#64748b";
    private static final String CLR_DIVIDER      = "#e2e8f0";

    // ─────────────────────────────────────────────────────────────
    //  State
    // ─────────────────────────────────────────────────────────────
    private final ClinicService service = new ClinicService();
    private Stage primaryStage;

    /** The swappable centre area of the BorderPane. */
    private StackPane contentArea;

    /** Currently highlighted sidebar button. */
    private Button activeNavBtn = null;

    // ─────────────────────────────────────────────────────────────
    //  Entry
    // ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Clinic Appointment System");
        stage.setMinWidth(900);
        stage.setMinHeight(580);

        BorderPane root = new BorderPane();
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());

        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: " + CLR_CONTENT_BG + ";");
        root.setCenter(contentArea);

        // Start on dashboard
        showDashboard();

        Scene scene = new Scene(root, 1050, 660);
        stage.setScene(scene);
        stage.show();
    }

    // ═════════════════════════════════════════════════════════════
    //  CHROME — Header & Sidebar
    // ═════════════════════════════════════════════════════════════

    private Node buildHeader() {
        Label appName = new Label("🏥  Clinic Appointment System");
        appName.setStyle(
            "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;"
        );

        Label tagline = new Label("Desktop Management Tool");
        tagline.setStyle("-fx-font-size: 11px; -fx-text-fill: #94a3b8;");

        VBox titleBox = new VBox(2, appName, tagline);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(titleBox);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: " + CLR_HEADER_BG + ";");
        return header;
    }

    private Node buildSidebar() {
        Label section1 = sidebarSection("PATIENTS");
        Button btnDash      = navButton("📊  Dashboard",          this::showDashboard);
        Button btnAddPat    = navButton("➕  Add Patient",         this::showAddPatientScreen);
        Button btnViewPat   = navButton("👥  View Patients",       this::showViewPatientsScreen);

        Label section2 = sidebarSection("DOCTORS");
        Button btnAddDoc    = navButton("➕  Add Doctor",          this::showAddDoctorScreen);
        Button btnViewDoc   = navButton("🩺  View Doctors",        this::showViewDoctorsScreen);

        Label section3 = sidebarSection("APPOINTMENTS");
        Button btnBook      = navButton("📅  Book Appointment",    this::showBookAppointmentScreen);
        Button btnViewAppt  = navButton("📋  View Appointments",   this::showViewAppointmentsScreen);

        VBox sidebar = new VBox(4,
            section1, btnDash, btnAddPat, btnViewPat,
            new Region(),   // small gap
            section2, btnAddDoc, btnViewDoc,
            new Region(),
            section3, btnBook, btnViewAppt
        );
        sidebar.setPadding(new Insets(16, 0, 16, 0));
        sidebar.setPrefWidth(210);
        sidebar.setStyle("-fx-background-color: " + CLR_SIDEBAR_BG + ";");

        // Activate dashboard button by default
        activateNav(btnDash);

        // Store buttons so activateNav can highlight them
        btnDash   .setUserData("btnDash");
        btnAddPat .setUserData("btnAddPat");
        btnViewPat.setUserData("btnViewPat");
        btnAddDoc .setUserData("btnAddDoc");
        btnViewDoc.setUserData("btnViewDoc");
        btnBook   .setUserData("btnBook");
        btnViewAppt.setUserData("btnViewAppt");

        // Re-wire actions so they also update the highlight
        btnDash    .setOnAction(e -> { activateNav(btnDash);    showDashboard(); });
        btnAddPat  .setOnAction(e -> { activateNav(btnAddPat);  showAddPatientScreen(); });
        btnViewPat .setOnAction(e -> { activateNav(btnViewPat); showViewPatientsScreen(); });
        btnAddDoc  .setOnAction(e -> { activateNav(btnAddDoc);  showAddDoctorScreen(); });
        btnViewDoc .setOnAction(e -> { activateNav(btnViewDoc); showViewDoctorsScreen(); });
        btnBook    .setOnAction(e -> { activateNav(btnBook);    showBookAppointmentScreen(); });
        btnViewAppt.setOnAction(e -> { activateNav(btnViewAppt); showViewAppointmentsScreen(); });

        return sidebar;
    }

    private Label sidebarSection(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(
            "-fx-font-size: 10px; -fx-font-weight: bold;"
            + "-fx-text-fill: #64748b; -fx-padding: 14 16 4 16;"
        );
        return lbl;
    }

    private Button navButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(38);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 16, 0, 20));
        btn.setStyle(navStyle(false));
        btn.setOnAction(e -> action.run());
        btn.setOnMouseEntered(e -> {
            if (btn != activeNavBtn)
                btn.setStyle(navStyle(false) + "-fx-background-color: #253547;");
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeNavBtn)
                btn.setStyle(navStyle(false));
        });
        return btn;
    }

    private String navStyle(boolean active) {
        String bg = active ? CLR_SIDEBAR_SEL : "transparent";
        String fg = active ? "white" : "#cbd5e1";
        return "-fx-background-color: " + bg + "; -fx-text-fill: " + fg + ";"
             + "-fx-font-size: 13px; -fx-border-color: transparent; -fx-cursor: hand;";
    }

    private void activateNav(Button btn) {
        if (activeNavBtn != null)
            activeNavBtn.setStyle(navStyle(false));
        activeNavBtn = btn;
        btn.setStyle(navStyle(true));
    }

    // ─────────────────────────────────────────────────────────────
    //  Helper: set the content area
    // ─────────────────────────────────────────────────────────────
    private void setContent(Node node) {
        contentArea.getChildren().setAll(node);
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 1 — DASHBOARD
    // ═════════════════════════════════════════════════════════════

    private void showDashboard() {
        VBox page = contentPage("Dashboard");

        // ── Stat cards ──
        HBox cards = new HBox(16,
            statCard("Total Patients",    String.valueOf(service.getPatients().size()),
                     "Registered in system", CLR_ACCENT_BLUE),
            statCard("Total Doctors",     String.valueOf(service.getDoctors().size()),
                     "Available doctors",    CLR_ACCENT_GREEN),
            statCard("Appointments",      String.valueOf(service.getAppointments().size()),
                     "Booked so far",        CLR_ACCENT_AMBER)
        );
        cards.setPadding(new Insets(0, 0, 20, 0));

        // ── Recent appointments mini-table ──
        Label recentLbl = sectionLabel("Recent Appointments");

        TableView<Appointment> recentTable = appointmentTable();
        ObservableList<Appointment> all = FXCollections.observableArrayList(service.getAppointments());
        // Show last 8 max
        int from = Math.max(0, all.size() - 8);
        recentTable.setItems(FXCollections.observableArrayList(all.subList(from, all.size())));
        recentTable.setPrefHeight(240);
        recentTable.setPlaceholder(new Label("No appointments yet — go book one!"));

        // ── Quick-action row ──
        Label quickLbl = sectionLabel("Quick Actions");
        Button qAddPat = quickActionBtn("➕ Add Patient",      CLR_ACCENT_BLUE);
        Button qAddDoc = quickActionBtn("➕ Add Doctor",       CLR_ACCENT_GREEN);
        Button qBook   = quickActionBtn("📅 Book Appointment", CLR_ACCENT_AMBER);

        qAddPat.setOnAction(e -> showAddPatientScreen());
        qAddDoc.setOnAction(e -> showAddDoctorScreen());
        qBook  .setOnAction(e -> showBookAppointmentScreen());

        HBox quickRow = new HBox(12, qAddPat, qAddDoc, qBook);

        page.getChildren().addAll(cards, recentLbl, recentTable, quickLbl, quickRow);
        setContent(scrollWrap(page));
    }

    private VBox statCard(String title, String value, String sub, String accentColor) {
        Label valLbl = new Label(value);
        valLbl.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + CLR_TEXT_DARK + ";");

        Label subLbl = new Label(sub);
        subLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: " + CLR_TEXT_MUTED + ";");

        Region topBar = new Region();
        topBar.setPrefHeight(4);
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.setStyle("-fx-background-color: " + accentColor + "; -fx-background-radius: 4 4 0 0;");

        VBox card = new VBox(6, topBar, valLbl, titleLbl, subLbl);
        card.setPadding(new Insets(0, 20, 20, 20));
        card.setStyle(
            "-fx-background-color: " + CLR_CARD_BG + ";"
            + "-fx-background-radius: 6;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"
        );
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private Button quickActionBtn(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.setStyle(
            "-fx-background-color: " + color + "; -fx-text-fill: white;"
            + "-fx-font-size: 13px; -fx-background-radius: 5; -fx-cursor: hand;"
            + "-fx-padding: 6 18 6 18;"
        );
        return btn;
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 2 — ADD PATIENT
    // ═════════════════════════════════════════════════════════════

    private void showAddPatientScreen() {
        VBox page = contentPage("Add New Patient");

        TextField nameField = styledField("Full name");
        TextField ageField  = styledField("Age  (1 – 150)");

        Label status = statusLabel();

        Button saveBtn = primaryBtn("Save Patient", CLR_ACCENT_BLUE);
        saveBtn.setOnAction(e -> {
            String name   = nameField.getText().trim();
            String ageStr = ageField.getText().trim();

            if (name.isEmpty() || ageStr.isEmpty()) {
                setStatus(status, "⚠  Please fill in all fields.", false);
                return;
            }
            try {
                int age = Integer.parseInt(ageStr);
                if (age <= 0 || age > 150) throw new NumberFormatException();
                service.addPatient(name, age);
                setStatus(status, "✔  Patient \"" + name + "\" added successfully.", true);
                nameField.clear();
                ageField.clear();
            } catch (NumberFormatException ex) {
                setStatus(status, "⚠  Enter a valid age between 1 and 150.", false);
            }
        });

        VBox form = formCard(
            formRow("Full Name", nameField),
            formRow("Age",       ageField),
            saveBtn,
            status
        );

        page.getChildren().add(form);
        setContent(scrollWrap(page));
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 3 — VIEW PATIENTS
    // ═════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private void showViewPatientsScreen() {
        VBox page = contentPage("Registered Patients");

        TableView<Patient> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Patient, String> colId   = col("Patient ID",   "patientId", 100);
        TableColumn<Patient, String> colName = col("Name",         "name",      220);
        TableColumn<Patient, String> colAge  = col("Age",          "age",        80);

        table.getColumns().addAll(colId, colName, colAge);
        table.setItems(FXCollections.observableArrayList(service.getPatients()));
        table.setPlaceholder(new Label("No patients registered yet."));

        styleTable(table);

        Label countLbl = new Label("Total: " + service.getPatients().size() + " patient(s)");
        countLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + CLR_TEXT_MUTED + ";");

        VBox card = tableCard(table, countLbl);
        page.getChildren().add(card);
        setContent(scrollWrap(page));
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 4 — ADD DOCTOR
    // ═════════════════════════════════════════════════════════════

    private void showAddDoctorScreen() {
        VBox page = contentPage("Add New Doctor");

        TextField nameField = styledField("Full name");
        TextField specField = styledField("e.g. Cardiology, General Practice");

        Label status = statusLabel();

        Button saveBtn = primaryBtn("Save Doctor", CLR_ACCENT_GREEN);
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String spec = specField.getText().trim();

            if (name.isEmpty() || spec.isEmpty()) {
                setStatus(status, "⚠  Please fill in all fields.", false);
                return;
            }
            service.addDoctor(name, spec);
            setStatus(status, "✔  Doctor \"" + name + "\" added successfully.", true);
            nameField.clear();
            specField.clear();
        });

        VBox form = formCard(
            formRow("Full Name",       nameField),
            formRow("Specialization",  specField),
            saveBtn,
            status
        );

        page.getChildren().add(form);
        setContent(scrollWrap(page));
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 5 — VIEW DOCTORS
    // ═════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private void showViewDoctorsScreen() {
        VBox page = contentPage("Registered Doctors");

        TableView<Doctor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Doctor, String> colId   = col("Doctor ID",      "doctorId",       100);
        TableColumn<Doctor, String> colName = col("Name",           "name",           200);
        TableColumn<Doctor, String> colSpec = col("Specialization", "specialization", 220);

        table.getColumns().addAll(colId, colName, colSpec);
        table.setItems(FXCollections.observableArrayList(service.getDoctors()));
        table.setPlaceholder(new Label("No doctors registered yet."));

        styleTable(table);

        Label countLbl = new Label("Total: " + service.getDoctors().size() + " doctor(s)");
        countLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + CLR_TEXT_MUTED + ";");

        VBox card = tableCard(table, countLbl);
        page.getChildren().add(card);
        setContent(scrollWrap(page));
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 6 — BOOK APPOINTMENT
    // ═════════════════════════════════════════════════════════════

    private void showBookAppointmentScreen() {
        VBox page = contentPage("Book Appointment");

        ComboBox<Patient> patientBox = new ComboBox<>(
                FXCollections.observableArrayList(service.getPatients()));
        patientBox.setPromptText("— select patient —");
        styleCombo(patientBox);

        ComboBox<Doctor> doctorBox = new ComboBox<>(
                FXCollections.observableArrayList(service.getDoctors()));
        doctorBox.setPromptText("— select doctor —");
        styleCombo(doctorBox);

        TextField slotField = styledField("e.g.  10:00 AM  or  Slot-3");

        Label status = statusLabel();

        Button bookBtn = primaryBtn("📅  Confirm Booking", CLR_ACCENT_BLUE);
        bookBtn.setOnAction(e -> {
            Patient patient = patientBox.getValue();
            Doctor  doctor  = doctorBox.getValue();
            String  slot    = slotField.getText().trim();

            if (patient == null || doctor == null || slot.isEmpty()) {
                setStatus(status, "⚠  Please fill in all fields.", false);
                return;
            }
            boolean ok = service.bookAppointment(patient, doctor, slot);
            if (ok) {
                setStatus(status,
                    "✔  Booked: " + patient.getName() + " → Dr. " + doctor.getName()
                    + " at " + slot, true);
                slotField.clear();
                patientBox.setValue(null);
                doctorBox.setValue(null);
            } else {
                setStatus(status,
                    "✘  Slot \"" + slot + "\" is already taken for Dr. " + doctor.getName(), false);
            }
        });

        // Warn if lists are empty
        if (service.getPatients().isEmpty())
            setStatus(status, "⚠  No patients yet — add patients first.", false);
        else if (service.getDoctors().isEmpty())
            setStatus(status, "⚠  No doctors yet — add doctors first.", false);

        VBox form = formCard(
            formRow("Patient",  patientBox),
            formRow("Doctor",   doctorBox),
            formRow("Time Slot", slotField),
            bookBtn,
            status
        );

        page.getChildren().add(form);
        setContent(scrollWrap(page));
    }

    // ═════════════════════════════════════════════════════════════
    //  SCREEN 7 — VIEW APPOINTMENTS
    // ═════════════════════════════════════════════════════════════

    @SuppressWarnings("unchecked")
    private void showViewAppointmentsScreen() {
        VBox page = contentPage("All Appointments");

        TableView<Appointment> table = appointmentTable();
        table.setItems(FXCollections.observableArrayList(service.getAppointments()));
        table.setPlaceholder(new Label("No appointments booked yet."));
        styleTable(table);

        Label countLbl = new Label("Total: " + service.getAppointments().size() + " appointment(s)");
        countLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + CLR_TEXT_MUTED + ";");

        VBox card = tableCard(table, countLbl);
        page.getChildren().add(card);
        setContent(scrollWrap(page));
    }

    // ═════════════════════════════════════════════════════════════
    //  REUSABLE COMPONENT BUILDERS
    // ═════════════════════════════════════════════════════════════

    /** Page root: titled section with top padding. */
    private VBox contentPage(String title) {
        Label h = new Label(title);
        h.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + CLR_TEXT_DARK + ";"
        );

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + CLR_DIVIDER + ";");

        VBox page = new VBox(14, h, sep);
        page.setPadding(new Insets(28, 32, 28, 32));
        page.setMaxWidth(Double.MAX_VALUE);
        return page;
    }

    /** Wraps a VBox page inside a ScrollPane. */
    private ScrollPane scrollWrap(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: " + CLR_CONTENT_BG + "; -fx-background: " + CLR_CONTENT_BG + ";");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return sp;
    }

    /** Card with white background for forms. */
    private VBox formCard(Node... children) {
        VBox card = new VBox(14, children);
        card.setPadding(new Insets(24));
        card.setMaxWidth(540);
        card.setStyle(
            "-fx-background-color: " + CLR_CARD_BG + ";"
            + "-fx-background-radius: 8;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 2);"
        );
        return card;
    }

    /** Wraps a table in a card-style VBox. */
    private VBox tableCard(TableView<?> table, Label countLbl) {
        VBox card = new VBox(10, table, countLbl);
        card.setPadding(new Insets(4, 0, 4, 0));
        card.setStyle(
            "-fx-background-color: " + CLR_CARD_BG + ";"
            + "-fx-background-radius: 8;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 2);"
        );
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setPrefHeight(440);
        return card;
    }

    /** Labelled form row. */
    private VBox formRow(String label, Node field) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + CLR_TEXT_DARK + ";");
        VBox row = new VBox(5, lbl, field);
        return row;
    }

    /** Styled text field. */
    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setPrefHeight(34);
        tf.setStyle(
            "-fx-background-radius: 5; -fx-border-color: " + CLR_DIVIDER + ";"
            + "-fx-border-radius: 5; -fx-font-size: 13px;"
        );
        return tf;
    }

    /** Styled combo box. */
    private <T> void styleCombo(ComboBox<T> box) {
        box.setMaxWidth(Double.MAX_VALUE);
        box.setPrefHeight(34);
        box.setStyle(
            "-fx-background-radius: 5; -fx-border-color: " + CLR_DIVIDER + ";"
            + "-fx-border-radius: 5; -fx-font-size: 13px;"
        );
    }

    /** Primary button with custom colour. */
    private Button primaryBtn(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.setStyle(
            "-fx-background-color: " + color + "; -fx-text-fill: white;"
            + "-fx-font-size: 13px; -fx-background-radius: 5; -fx-cursor: hand;"
            + "-fx-padding: 6 22 6 22;"
        );
        return btn;
    }

    /** Status label (styled on use). */
    private Label statusLabel() {
        Label lbl = new Label();
        lbl.setWrapText(true);
        lbl.setStyle("-fx-font-size: 13px;");
        return lbl;
    }

    private void setStatus(Label lbl, String msg, boolean ok) {
        lbl.setText(msg);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: " + (ok ? CLR_ACCENT_GREEN : "#dc2626") + ";");
    }

    /** Section label inside dashboard. */
    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + CLR_TEXT_DARK + ";");
        return lbl;
    }

    /** Generic column builder. */
    private <T, V> TableColumn<T, V> col(String title, String prop, double width) {
        TableColumn<T, V> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(width);
        return c;
    }

    /** Shared 3-column appointment table (doctor, patient, slot). */
    @SuppressWarnings("unchecked")
    private TableView<Appointment> appointmentTable() {
        TableView<Appointment> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Appointment, String> colDoc  = col("Doctor ID",    "doctorId",    110);
        TableColumn<Appointment, String> colPat  = col("Patient Name", "patientName", 200);
        TableColumn<Appointment, String> colSlot = col("Time Slot",    "slot",        140);

        table.getColumns().addAll(colDoc, colPat, colSlot);
        return table;
    }

    /** Applies alternating row colours to a table. */
    private <T> void styleTable(TableView<T> table) {
        table.setStyle(
            "-fx-font-size: 13px;"
        );
    }
}
