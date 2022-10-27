// --== CS400 File Header Information ==--
// Name: Evan Burnside
// Email: ejburnside@wisc.edu
// Team: BS
// TA: Mohit
// Lecturer: Gary Dahl
// Notes to Grader: Utilized information from 
// https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm about ComboBoxes, as well as 4 lines
// from https://stackoverflow.com/questions/26343495/indexoutofboundsexception-while-updating-a-list
// view-in-javafx in order to solve an error that can happen when updating enabled ComboBoxes

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Frontend extends Application {

	// Instantiate the loader and backend.
	RoadTripLoader loader = new RoadTripLoader();
	RoadTripBackend backend = new RoadTripBackend();

	// Instantiate lists.
	private List<IGraphNode> citiesList = new ArrayList<>();
	private List<IGraphNode> chosenCities = new ArrayList<IGraphNode>();
	private List<IGraphNode> reorderedCities = new ArrayList<IGraphNode>();

	// Instantiate states.
	private boolean startCityAdded = false;
	private boolean destCityAdded = false;

	// Build the GUI.
	Group root = new Group();
	Scene scene = null;

	public void startSelections(Stage stage) throws Exception {
		loader = new RoadTripLoader();
		backend = new RoadTripBackend();
		citiesList = loader.loadCities("cities.json");
		chosenCities = new ArrayList<IGraphNode>();
		reorderedCities = new ArrayList<IGraphNode>();
		startCityAdded = false;
		destCityAdded = false;
		IGraphNode[] startEndArray = new IGraphNode[2];

		double madisonLong = -89.4012302;
		double madisonLat = 43.0730517;
		double centerLong = -80.1918;
		double centerLat = 25.7617;
		// Size of the map
		int width = 800;
		int height = 353;
		// Map X and Y boundaries
		double westLong = -124.733056;
		double eastLong = -66.949722;
		double northLat = 49.384444;
		double southLat = 24.520833;
		double madx = width * ((westLong - madisonLong) / (westLong - eastLong)) - 5;
		double mady = (height * ((northLat - madisonLat) / (northLat - southLat)) + 5);
		double centerx = width * ((westLong - centerLong) / (westLong - eastLong)) - 5;
		double centery = (height * ((northLat - centerLat) / (northLat - southLat)) + 5);

		GridPane gridPane = new GridPane();
		gridPane.setVgap(100);
		gridPane.setHgap(5);
		root = new Group(gridPane);
		scene = new Scene(root, 800, 515);

		stage.setTitle("Road Trip Calculator");

		Label label1 = new Label("Starting state & city");
		Label label2 = new Label("Additional state/cities");
		Label label3 = new Label("Destination state & city");
		ObservableList<String> stateOptions = FXCollections.observableArrayList("Alabama", "Arizona", "Arkansas",
				"California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia",
				"Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine",
				"Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska",
				"Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota",
				"Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
				"Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin",
				"Wyoming");

		// Initialize all ComboBoxes
		final ComboBox<String> comboBox = new ComboBox(stateOptions);
		final ComboBox<String> comboBox2 = new ComboBox();
		final ComboBox<String> comboBox3 = new ComboBox(stateOptions);
		final ComboBox<String> comboBox4 = new ComboBox();
		final ComboBox<String> comboBox5 = new ComboBox(stateOptions);
		final ComboBox<String> comboBox6 = new ComboBox();

		Label message = new Label("Choose the cities you would want to visit on your roadtrip\n"
				+ "and see the shortest path between them!");
		message.setId("Message");
		gridPane.add(message, 3, 0);
		
		// Set up top row of ComboBoxes
		comboBox.setPromptText("State");
		comboBox.setId("comboBox");
		comboBox2.setPromptText("City");
		comboBox2.setId("comboBox");
		comboBox.setOnAction(e -> {
			if (gridPane.getChildren().contains(comboBox2)) {
				gridPane.getChildren().remove(comboBox2);
			}
			gridPane.getChildren().remove(message);
			String state = comboBox.getValue();
			List<String> startingCityOptions = new ArrayList<String>();
			for (int i = 0; i < citiesList.size(); i++) {
				if (citiesList.get(i).getStateName().equals(state)) {
					startingCityOptions.add(citiesList.get(i).getCity());
				}
			}
			ObservableList<String> cities = FXCollections.observableArrayList(startingCityOptions);
			comboBox2.setItems(cities);
			gridPane.add(comboBox2, 3, 0);
			comboBox2.setOnAction(f -> {
				comboBox.setDisable(true);
				comboBox2.setDisable(true);
				String citySelected = comboBox2.getValue();
				for (int i = 0; i < citiesList.size(); i++) {
					if (citiesList.get(i).getStateName().equals(state) &&
							citiesList.get(i).getCity().equals(citySelected)) {
						//chosenCities.add(citiesList.get(i));
						startEndArray[0] = citiesList.get(i);
						Label displayStart = new Label(citiesList.get(i).getCity());
						citiesList.remove(i);
						gridPane.add(displayStart, 4, 0);
						toggleStartBoolean();
						break;
					}
				}
			});
		});

		// set up middle row of ComboBoxes
		comboBox3.setPromptText("State");
		comboBox4.setPromptText("City");
		int[] numACities = new int[] { 0 };
		comboBox3.setOnAction(e -> {
			if (gridPane.getChildren().contains(comboBox4)) {
				gridPane.getChildren().remove(comboBox4);
			}
			String state = comboBox3.getValue();
			List<String> startingCityOptions = new ArrayList<String>();
			for (int i = 0; i < citiesList.size(); i++) {
				if (citiesList.get(i).getStateName().equals(state)) {
					startingCityOptions.add(citiesList.get(i).getCity());
				}
			}
			ObservableList<String> cities = FXCollections.observableArrayList(startingCityOptions);
			comboBox4.setItems(cities);
			gridPane.add(comboBox4, 3, 1);
			comboBox4.setOnAction(f -> {
				String citySelected = comboBox4.getValue();
				for (int i = 0; i < citiesList.size(); i++) {

					if (citiesList.get(i).getStateName().equals(state) &&
							citiesList.get(i).getCity().equals(citySelected)) {
						chosenCities.add(citiesList.get(i));
						Label displayStart = new Label(citiesList.get(i).getCity());
						switch (numACities[0]) {
							case 0:
								gridPane.add(displayStart, 4, 1);
								numACities[0]++;
								break;
							case 1:
								gridPane.add(displayStart, 5, 1);
								numACities[0]++;
								break;
							case 2:
								gridPane.add(displayStart, 6, 1);
								numACities[0]++;
								break;
							case 3:
								gridPane.add(displayStart, 7, 1);
								numACities[0]++;
								break;
							case 4:
								Label ellipse = new Label("...");
								gridPane.add(ellipse, 8, 1);
								numACities[0]++;
								break;
							default:
								break;
						}
						citiesList.remove(i);

						// this is just some code that relieves an unneccesary IndexOutOfBounds
						// error that is associated with updating ComboBox and also updates the
						// cities list within this middle row of ComboBoxes
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								startingCityOptions.clear();
								for (int j = 0; j < citiesList.size(); j++) {
									if (citiesList.get(j).getStateName().equals(state)) {
										startingCityOptions.add(citiesList.get(j).getCity());
									}
								}
								ObservableList<String> cities2 = FXCollections.observableArrayList(startingCityOptions);
								comboBox4.setItems(cities2);
							}
						});
						break;
					}
				}
			});
		});

		// set up bottom row of ComboBoxes
		comboBox5.setPromptText("State");
		comboBox6.setPromptText("City");
		comboBox5.setOnAction(e -> {
			if (gridPane.getChildren().contains(comboBox5)) {
				gridPane.getChildren().remove(comboBox6);
			}
			String state = comboBox5.getValue();
			List<String> startingCityOptions = new ArrayList<String>();
			for (int i = 0; i < citiesList.size(); i++) {
				if (citiesList.get(i).getStateName().equals(state)) {
					startingCityOptions.add(citiesList.get(i).getCity());
				}
			}
			ObservableList<String> cities = FXCollections.observableArrayList(startingCityOptions);
			comboBox6.setItems(cities);
			gridPane.add(comboBox6, 3, 2);
			comboBox6.setOnAction(f -> {
				comboBox5.setDisable(true);
				comboBox6.setDisable(true);
				String citySelected = comboBox6.getValue();
				for (int i = 0; i < citiesList.size(); i++) {
					if (citiesList.get(i).getStateName().equals(state) &&
							citiesList.get(i).getCity().equals(citySelected)) {
						//chosenCities.add(citiesList.get(i));
						startEndArray[1] = citiesList.get(i);
						Label displayDestination = new Label(citiesList.get(i).getCity());
						citiesList.remove(i);
						gridPane.add(displayDestination, 4, 2);
						toggleDestBoolean();
						break;
					}
				}
			});
		});

		// Reset Button
		Button resetButton = new Button("Reset");
		resetButton.setId("resetButton");
		resetButton.setOnAction(e -> {
			try {
				restart(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		// Calculate Button
		Button calculateButton = new Button("Calculate shortest route");
		calculateButton.setId("calcButton");
		calculateButton.setOnAction(e -> {
			if (startCityAdded && destCityAdded) {

				// retrieve and set up USA image
				InputStream stream = null;
				try {
					stream = new FileInputStream("EquidistantUSA.png");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				Image image = new Image(stream);
				ImageView imageView = new ImageView();
				imageView.setX(0);
				imageView.setY(0);
				imageView.setFitWidth(800);
				imageView.setPreserveRatio(true);
				imageView.setImage(image);
				Pane pane = new Pane();

				// Plot points and lines
				
                                Collections.reverse(chosenCities);

			        chosenCities.add(startEndArray[0]);

			        Collections.reverse(chosenCities);

			        chosenCities.add(startEndArray[1]);
				reorderedCities = backend.cityList(chosenCities); // get reordered list of cities
				int nodeCount = 1;
				double[] lineCoords = new double[4];
				for (int i = 0; i < reorderedCities.size(); i++) {
					double x = reorderedCities.get(i).getX();
					double y = reorderedCities.get(i).getY();
					double[] xyCoords = calculateXYCoords(x, y);
					if (i == 0) {
						Circle dot = new Circle(xyCoords[0], xyCoords[1], 5, Color.GREEN);
						pane.getChildren().add(dot);
					} else if (i == reorderedCities.size() - 1) {
						Circle dot = new Circle(xyCoords[0], xyCoords[1], 5, Color.RED);
						pane.getChildren().add(dot);
					} else {
						Circle dot = new Circle(xyCoords[0], xyCoords[1], 5, Color.BLACK);
						pane.getChildren().add(dot);
					}
					if (nodeCount == 1) {
						lineCoords[0] = xyCoords[0];
						lineCoords[1] = xyCoords[1];
						nodeCount++;
					} else {
						lineCoords[2] = xyCoords[0];
						lineCoords[3] = xyCoords[1];
						Line line = new Line(lineCoords[0], lineCoords[1], lineCoords[2], lineCoords[3]);
						pane.getChildren().add(line);
						lineCoords[0] = xyCoords[0];
						lineCoords[1] = xyCoords[1];
					}
				}

				Button restartButton = new Button("restart");
				restartButton.setOnAction(d -> {
					try {
						restart(stage);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
				gridPane.getChildren().clear();
				gridPane.setVgap(50);
				gridPane.add(restartButton, 0, 9);
				root = new Group(imageView, pane, gridPane);
				scene = new Scene(root, 800, 500);
				stage.setScene(scene);
			} else {
				Label errorMessage = new Label("Be sure to choose a starting and ending city!");
				errorMessage.setId("errorMessage");
				gridPane.add(errorMessage, 3, 4);
			}
		});

		gridPane.add(label1, 1, 0);
		gridPane.add(label2, 1, 1);
		gridPane.add(label3, 1, 2);
		gridPane.add(comboBox, 2, 0);
		gridPane.add(comboBox3, 2, 1);
		gridPane.add(comboBox5, 2, 2);
		gridPane.add(resetButton, 1, 4);
		gridPane.add(calculateButton, 2, 4);

		stage.setScene(scene);
		stage.show();

	}

	private double[] calculateXYCoords(double x, double y) {
		// Size of the map in pixels
		int width = 800;
		int height = 353;
		// Longitude and Latitude values of the Maps boundaries
		double westLong = -124.933056; //-124.733056
		double eastLong = -66.249722; //-66.949722
		double northLat = 49.9; //49.384444
		double southLat = 24.0; //24.520833
		double newX = width * ((westLong - x) / (westLong - eastLong)) ; //-5
		double newY = (height * ((northLat - y) / (northLat - southLat)) ); //+5)
		double[] XY = { newX, newY };
		return XY;
	}

	private void toggleStartBoolean() {
		this.startCityAdded = true;
	}

	private void toggleDestBoolean() {
		this.destCityAdded = true;
	}

	private void restart(Stage stage) throws Exception {
		startSelections(stage);// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Application.launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		startSelections(primaryStage);

	}

}
