package game.gui;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Optional;


import game.engine.Battle;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.PiercingCannon;
import game.engine.weapons.SniperCannon;
import game.engine.weapons.WallTrap;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class gameGui extends Application implements EventHandler<ActionEvent> {
	
	private Battle battle;
	private AnchorPane layout;

	private Scene startScene;
	private ComboBox<String> difficultyBox;
	private Button startButton;

	private Scene easyScene;
	private Scene hardScene;
	private Label Phase;
	private Label Score;
	private Label currentResources;
	private Label Turn;
	private VBox weaponBox;
	private Button[] weaponShop;
	private Button passTurn;
	private Pane[] titans;
	private Label[] lanes;
	private HBox[] weaponHBoxes;
	private Button insButton;
	
	private Scene s;
	private Stage st;
	

	private Scene gameOverScene;
	private javafx.stage.Stage primaryStage;

	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Attack of Titans");

		createStartLayout();

		primaryStage.show();
	}

	private void createStartLayout() {
		layout = new AnchorPane();
		startScene = new Scene(layout, 500, 500);
		primaryStage.setScene(startScene);

		Label welcomeLabel = new Label("Welcome to Attack of Titans!");
		welcomeLabel.setLayoutX(125);
		welcomeLabel.setLayoutY(25);
		welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		startButton = new Button("Start");
		startButton.setLayoutX(200);
		startButton.setLayoutY(350);
		startButton.setPrefSize(100, 30);
		startButton.setOnAction(this);

		insButton = new Button("Instructions");
		insButton.setPrefSize(100, 30);
		insButton.setLayoutX(400);
		insButton.setLayoutY(400);
		insButton.setOnAction(this);

		difficultyBox = new ComboBox<>();
		difficultyBox.getItems().addAll("Easy Mode", "Hard Mode");
		difficultyBox.setLayoutX(150);
		difficultyBox.setLayoutY(250);
		difficultyBox.setPrefSize(200, 30);
		difficultyBox.setValue("Select Difficulty");
		difficultyBox.setOnAction(this);

		layout.getChildren().addAll(welcomeLabel, startButton, difficultyBox, insButton);
	}

	private void insScene() {
	    st = new Stage();
	    VBox vbox = new VBox();

	    
	    TextArea t1 = new TextArea();
	    t1.appendText("Instructions for Easy mode:\n In Easy mode you have 3 lanes where titans spawn and initial resources per lane of 250 ");
	    t1.setEditable(false);
	    t1.setStyle("-fx-background-color: #e6ffe6; -fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 1px;"); 
	    

	    
	    TextArea t2 = new TextArea();
	    t2.appendText("Instructions for Hard mode \n In Hard mode you have 5 lanes where titans spawn and initial resources per lane of 125");
	    t2.setEditable(false);
	    t2.setStyle("-fx-background-color: #ffe6e6; -fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 1px;"); 
	   

	   
	    TextArea t3 = new TextArea();
	    t3.setEditable(false);
	    t3.setStyle("-fx-background-color: #ffffcc; -fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 1px;"); 
	    t3.appendText("Your Objective is to protect your base wall from the incoming titans buy building your defences and buying weapons.\nIn this game titans will spawn each and every time you press the pass turn button or choose a weapon to add to your lanes");
	    
	    vbox.getChildren().addAll(t3,t1,t2);
	    s = new Scene(vbox, 1000, 600);
	    st.setScene(s);
	    st.setTitle("Instructions");
	    st.show();
	}

	public void startEasyMode() {
		initializeBattle(1, 0, 300, 3, 250);

		layout = new AnchorPane();
		easyScene = new Scene(layout, 1050, 550);
		primaryStage.setScene(easyScene);

		setupLanes(3, 150, 300);
		setupWeapons(3, 150, 300);
		setupTitans(3, 300);

		battleInfo();
		updateData();
	}

	public void startHardMode() {
		initializeBattle(1, 0, 300, 5, 125);

		layout = new AnchorPane();
		hardScene = new Scene(layout, 1050, 575);
		primaryStage.setScene(hardScene);

		setupLanes(5, 150, 150);
		setupWeapons(5, 150, 150);
		setupTitans(5, 150);

		battleInfo();
		updateData();
	}

	private void initializeBattle(int param1, int param2, int param3, int param4, int param5) {
		try {
			this.battle = new Battle(param1, param2, param3, param4, param5);
		} catch (IOException e) {
			showError(e.getMessage());
		}
	}

	private void setupLanes(int count, double width, double spacing) {
		this.lanes = new Label[count];
		int posL = 20;
		for (int i = 0; i < count; i++) {
			lanes[i] = new Label();
			lanes[i].setLayoutX(posL);
			lanes[i].setLayoutY(0);
			lanes[i].setPrefSize(width, 50);
			lanes[i].setStyle("-fx-border-color: black; -fx-border-width: 2;");
			layout.getChildren().add(lanes[i]);

			Label wall = new Label("Wall");
			wall.setAlignment(Pos.CENTER);
			wall.setStyle(
					"-fx-text-fill: brown; -fx-font-weight: bold; -fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 2px;");
			wall.setPrefSize(width, 30);
			wall.setLayoutX(posL);
			wall.setLayoutY(125);
			layout.getChildren().add(wall);

			posL += spacing;
		}
	}

	private void setupWeapons(int count, double width, double spacing) {
		this.weaponHBoxes = new HBox[count];
		int posW = 20;
		int weaponBoxPos = 45;
		for (int i = 0; i < count; i++) {
			this.weaponHBoxes[i] = new HBox();
			HBox weapon = weaponHBoxes[i];
			weapon.setLayoutX(posW);
			weapon.setLayoutY(weaponBoxPos);
			weapon.setPrefSize(width, 150);
			weapon.setMaxWidth(250);

			Rectangle r = new Rectangle(posW, 150, width, 335);
			r.setStroke(Color.BLACK);
			r.setFill(Color.WHITE);
			r.setStrokeWidth(2);
			layout.getChildren().add(r);

			Label indicator = new Label("Titan Spawn Point");
			indicator.setLayoutX(posW);
			indicator.setLayoutY(490);
			indicator.setStyle(
					"-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 16px; -fx-border-color: red; -fx-border-width: 2px;");
			indicator.setPrefWidth(width);
			layout.getChildren().add(indicator);

			posW += spacing;
			weaponBoxPos += 15;
		}
		layout.getChildren().addAll(weaponHBoxes);
	}

	private void setupTitans(int count, double spacing) {
		this.titans = new Pane[count];
		int posT = 20;
		for (int i = 0; i < count; i++) {
			this.titans[i] = new Pane();
			Pane titan = titans[i];

			titan.setLayoutX(posT);
			titan.setLayoutY(150);
			titan.setPrefSize(350, 100);
			layout.getChildren().add(titan);
			posT += spacing;
		}
	}

	public void handle(ActionEvent a) {
		if (a.getSource() == insButton) {
			insScene();
		} else if (a.getSource() == startButton) {
			layout = new AnchorPane();
			String selectedDifficulty = difficultyBox.getValue();
			if ("Easy Mode".equals(selectedDifficulty)) {
				startEasyMode();
			} else if ("Hard Mode".equals(selectedDifficulty)) {
				startHardMode();
			}
		} else if (a.getSource() == passTurn) {
			try {
				battle.passTurn();
				updateData();
			} catch (Exception e) {
				e.printStackTrace();;
			}
		}
	}

	public void updateData() {
		Phase.setText("Phase: " + battle.getBattlePhase());
		currentResources.setText("Resources: " + battle.getResourcesGathered());
		Score.setText("Score: " + battle.getScore());
		Turn.setText("Turn: " + battle.getNumberOfTurns());
		for (Pane t : titans)
			t.getChildren().clear();
		for (HBox h : weaponHBoxes)
			h.getChildren().clear();

		if (battle.isGameOver()) {
			showGameOverScene();
		} else {
			ArrayList<Lane> lanesP = battle.getOriginalLanes();
			int i = 0;
			for (Lane current : lanesP) {
				if (!current.isLaneLost()) {
					lanes[i].setText("Danger Level: " + current.getDangerLevel() + "\nHealth: "
							+ current.getLaneWall().getCurrentHealth());
					for (int j = 0; j < current.getWeapons().size(); j++) {
						Weapon weapon = current.getWeapons().get(j);
						String wLabel = weaponImage(weapon);

						Image im = new Image(getClass().getResourceAsStream(wLabel));
						ImageView image = new ImageView(im);
						image.setFitWidth(30);
						image.setFitHeight(30);
						weaponHBoxes[i].getChildren().addAll(image);
					}
					for (Titan titan : current.getTitans()) {
						ImageView imageView = titanImageView(titan, 30, 30); // Adjust width and height as needed
						Text health = new Text("" + titan.getCurrentHealth());
						Font f = new Font(12);
						health.setFont(f);
						health.setStyle("-fx-fill:Blue;");

						BorderPane pane = new BorderPane();
						pane.setLayoutY(titan.getDistance());
						pane.setLayoutX(Math.random() * 120);
						pane.getChildren().addAll(imageView, health);

						titans[i].getChildren().add(pane);
					}
				} else {
					lanes[i].setText("Danger Level: 0\nHealth: 0");
					lanes[i].setStyle("-fx-border-color: red; -fx-border-width: 2px;");
					
					
					
				}
				i++;
			}
		}
	}

	public void battleInfo() {
		Phase = new Label();
		Phase.setLayoutX(800);
		Phase.setLayoutY(475);
		Phase.setPrefSize(150, 25);
		layout.getChildren().add(Phase);

		Score = new Label();
		Score.setLayoutX(800);
		Score.setLayoutY(450);
		Score.setPrefSize(100, 25);
		layout.getChildren().add(Score);

		currentResources = new Label();
		currentResources.setLayoutX(800);
		currentResources.setLayoutY(425);
		currentResources.setPrefSize(100, 25);
		layout.getChildren().add(currentResources);

		Turn = new Label();
		Turn.setLayoutX(800);
		Turn.setLayoutY(400);
		Turn.setPrefSize(100, 25);
		layout.getChildren().add(Turn);

		weaponBox = new VBox();
		weaponBox.setLayoutX(800);
		weaponBox.setLayoutY(75);
		weaponBox.setPrefSize(300, 300);
		layout.getChildren().add(weaponBox);

		Label wepShop = new Label("WeaponShop:");
		wepShop.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 15));
		wepShop.setStyle("-fx-text-fill: Green;");
		wepShop.setLayoutX(800);
		wepShop.setLayoutY(50);
		wepShop.setPrefSize(200, 25);
		layout.getChildren().add(wepShop);

		weaponShop = new Button[4];
		for (int i = 0; i < weaponShop.length; i++) {
			weaponShop[i] = new Button();
			int finalI = i;
			weaponShop[i].setOnAction(event -> {
				int laneNo = inputLaneNo();
				if (laneNo > 0 && laneNo <= battle.getOriginalLanes().size()) {
					Lane lane = battle.getOriginalLanes().get(laneNo - 1);
					try {
						battle.purchaseWeapon(finalI + 1, lane);
						updateData();
					} catch (Exception e) {
						showError(e.getMessage());
					}
				} else {
					showError("Invalid lane number!");
				}
			});
			weaponBox.getChildren().add(weaponShop[i]);
			WeaponRegistry wr = battle.getWeaponFactory().getWeaponShop().get(i + 1);
			Weapon ww = wr.buildWeapon();
			String data = "Name: " + wr.getName() +"\nType: "+weaponType(ww) +"\nPrice: " + wr.getPrice() + "\nDamage: " + wr.getDamage();
			weaponShop[i].setText(data);
		}

		passTurn = new Button("Pass Turn!");
		passTurn.setOnAction(this);
		passTurn.setLayoutX(850);
		passTurn.setLayoutY(400);
		passTurn.setPrefSize(100, 25);
		layout.getChildren().add(passTurn);

		updateData();
	}
	public String weaponType(Weapon w) {
		if (w instanceof PiercingCannon) {
			return "Piercing Cannon";
		} else if (w instanceof SniperCannon) {
			return "Sniper Cannon";
		} else if (w instanceof WallTrap) {
			return "Wall Trap";
		} 
		return "Volley Spread Cannon";
	}

	public void showError(String msg) {
		Alert error = new Alert(Alert.AlertType.ERROR);
		error.setTitle("Error");
		error.setHeaderText("Something went wrong!");
		error.setContentText(msg);
		error.showAndWait();
	}

	public int inputLaneNo() {
		TextInputDialog laneNo = new TextInputDialog();
		laneNo.setTitle("Lane");
		laneNo.setHeaderText("Choose the lane you want: ");
		laneNo.setContentText("Lane: ");
		Optional<String> laneString = laneNo.showAndWait();
		if (laneString.isPresent()) {
			try {
				return Integer.parseInt(laneString.get());
			} catch (NumberFormatException e) {
				System.err.println("Invalid input: Not a number");
			}
		}
		return -1;
	}

	public String weaponImage(Weapon w) {
		if (w instanceof PiercingCannon) {
			return "/Volley.png";
		} else if (w instanceof SniperCannon) {
			return "/Cannonn.jpg";
		} else if (w instanceof WallTrap) {
			return "/Cannonnn.jpg";
		} 
		return "/Cannonnnn.jpg";
	}

	public ImageView titanImageView(Titan t, double width, double height) {
		String imagePath;
		if (t instanceof ArmoredTitan) {
			imagePath = "/Armored titan.jpeg";
		} else if (t instanceof AbnormalTitan) {
			imagePath = "/Abnormal.png";
		} else if (t instanceof PureTitan) {
			imagePath = "/Pure.jpg";
		} else {
			imagePath = "/Colossal.png";
		}
		return createImageView(imagePath, width, height);
	}

	private ImageView createImageView(String imagePath, double width, double height) {
		Image image = new Image(getClass().getResourceAsStream(imagePath));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		return imageView;
	}

	public String titanImage(Titan t) {
		if (t instanceof ArmoredTitan) {
			return "Armored titan.jpeg";
		} else if (t instanceof AbnormalTitan) {
			return "Abnormal.png";
		} else if (t instanceof PureTitan) {
			return "Pure.jpg";
		} else {
			return "Colossal.png";
		}
	}

	public void showGameOverScene() {
	    StackPane gameOverLayout = new StackPane();
	    gameOverLayout.setStyle("-fx-background-color: #FFCCCC;");
	    
	    Label gameOverLabel = new Label("Game Over");
	    gameOverLabel.setFont(new Font("Arial", 36));
	    gameOverLabel.setStyle("-fx-text-fill: #FF0000;");

	    
	    Label finalScoreLabel = new Label("Final Score: " + battle.getScore());
	    finalScoreLabel.setFont(new Font("Arial", 20));
	    finalScoreLabel.setStyle("-fx-text-fill: #FFFFFF;");

	    Button restartButton = new Button("Restart Game");
	    restartButton.setFont(new Font("Arial", 20));
	    restartButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000;");
	    restartButton.setOnAction(e -> {
	        createStartLayout();
	    });

	    gameOverLayout.getChildren().addAll(gameOverLabel, finalScoreLabel, restartButton);
	    StackPane.setAlignment(gameOverLabel, Pos.TOP_CENTER);
	    StackPane.setAlignment(finalScoreLabel, Pos.CENTER);
	    StackPane.setAlignment(restartButton, Pos.BOTTOM_CENTER);
	    StackPane.setMargin(restartButton, new javafx.geometry.Insets(20));

	    gameOverScene = new Scene(gameOverLayout, 800, 600);
	    primaryStage.setScene(gameOverScene);
	    primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}


}
