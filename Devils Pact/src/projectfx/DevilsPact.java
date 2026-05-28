/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectfx;

/**
 *
 * @author Andrii
 */

import javafx.application.Application; 
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Point2D;
import java.util.*;

public class DevilsPact extends Application {

    //  Configuration Constants 
    static int TILE = 100;                 // Pixel size of one grid tile
    static int MAP_W = 14;                 // Current map width in tiles
    static int MAP_H = 7;                  // Current map height in tiles
    static final int UI_HEIGHT = 50;       // Height reserved for the top UI bar
    static final int ROOM_MIN = 2, ROOM_MAX = 4; // Min/Max dimensions for procedurally generated rooms
    static final int MAX_MAP_W = 14;       // Maximum possible map width
    static final int MAX_MAP_H = 7;        // Maximum possible map height

    //  Asset Paths 
    // Backgrounds
    static final String START_BG_IMAGE = "/Assets/Backgrounds/Start_BG.png";
    static final String LOST_BG_IMAGE = "/Assets/Backgrounds/Lost_BG.png"; 
    static final String ENDING_BG_IMAGE = "/Assets/Backgrounds/Ending_BG.png";

    // Player Sprites
    static final String PLAYER_IMAGE_BASIC = "/Assets/Characters/MainHeroBasic.png";
    static final String PLAYER_IMAGE_LEFT = "/Assets/Characters/MainHeroLeft.png";
    static final String PLAYER_IMAGE_RIGHT = "/Assets/Characters/MainHeroRight.png";

    // Scaled Player Sprites (for combat)
    static final String PLAYER_IMAGE_BASIC_2X = "/Assets/Characters/MainHeroBasic_2x.png";
    static final String PLAYER_IMAGE_LEFT_2X = "/Assets/Characters/MainHeroLeft_2x.png";
    static final String PLAYER_IMAGE_RIGHT_2X = "/Assets/Characters/MainHeroRight_2x.png";

    // Player Attack Animations
    static final String HERO_ATTACK_1 = "/Assets/Characters/MainHeroAttack1.png";
    static final String HERO_ATTACK_2 = "/Assets/Characters/MainHeroAttack2.png";
    static final String HERO_ATTACK_3 = "/Assets/Characters/MainHeroAttack3.png";

    // Dice Assets
    static final String DICE_1 = "/Assets/Dice/Attack_dice_side1.png";
    static final String DICE_2 = "/Assets/Dice/Attack_dice_side2.png";
    static final String DICE_3 = "/Assets/Dice/Attack_dice_side3.png";
    static final String DICE_4 = "/Assets/Dice/Attack_dice_side4.png";
    static final String DICE_5 = "/Assets/Dice/Attack_dice_side5.png";
    static final String DICE_6 = "/Assets/Dice/Attack_dice_side6.png";

    // Enemy/Boss Animation Arrays
    static final Image[][] ENEMY_IDLE_IMGS = new Image[8][3];
    static final Image[][] ENEMY_ATTACK_IMGS = new Image[8][3];
    static final Image[] BOSS1_IDLE = new Image[3];
    static final Image[] BOSS1_ATTACK = new Image[3];
    static final Image[] BOSS2_IDLE = new Image[3];
    static final Image[] BOSS2_ATTACK = new Image[5];

    // Environment/Tile Icons
    static final String FLOOR_IMAGE = "/Assets/Icons/floor_icon.png";
    static final String STAIRS_IMAGE = "/Assets/Icons/stairs_icon.png";
    static final String CLOSED_STAIRS_IMAGE = "/Assets/Icons/closed_stairs_icon.png";
    static final String TOP_WALL_IMAGE = "/Assets/Icons/top_wall_icon.png";
    static final String BOTTOM_WALL_IMAGE = "/Assets/Icons/bottom_wall_icon.png";
    static final String LEFT_WALL_IMAGE = "/Assets/Icons/left_wall_icon.png";
    static final String RIGHT_WALL_IMAGE = "/Assets/Icons/right_wall_icon.png";
    static final String SIDE_LEFT_WALL_IMAGE = "/Assets/Icons/side_left_wall_icon.png";
    static final String SIDE_RIGHT_WALL_IMAGE = "/Assets/Icons/side_right_wall_icon.png";
    static final String CORNER_LEFT_WALL_IMAGE = "/Assets/Icons/corner_left_wall.png";
    static final String CORNER_RIGHT_WALL_IMAGE = "/Assets/Icons/corner_right_wall.png";

    // Room interactables
    static final String BASIC_FIGHT_IMAGE = "/Assets/Icons/basicfight_icon.png";
    static final String CHEST_IMAGE = "/Assets/Icons/chest_icon.png";
    static final String FIGHT_IMAGE = "/Assets/Icons/fight_icon.png";
    static final String SHOP_IMAGE = "/Assets/Icons/shop_icon.png";
    static final String COINS_IMAGE = "/Assets/Icons/coins_icon.png";

    // Room Backgrounds
    static final String FIGHT_BG = "/Assets/Backgrounds/Fight_BG.png";
    static final String CHEST_BG = "/Assets/Backgrounds/Chest_BG.png";
    static final String CHEST_BG_OPEN = "/Assets/Backgrounds/Chest_BG_open.png";
    static final String SHOP_BG = "/Assets/Backgrounds/Shop_BG.png";

    // NPC and UI Elements
    static final String MERCHANT_IMAGE = "/Assets/Characters/Merchant_icon_basic.png";
    static final String MERCHANT_IMAGE_MIDDLE = "/Assets/Characters/Merchant_icon_middle.png";
    static final String MERCHANT_IMAGE_LOW = "/Assets/Characters/Merchant_icon_low.png";
    static final String SHOP_TABLE_IMAGE = "/Assets/Backgrounds/Shop_Table.png";
    static final String START_BUTTON_IMAGE = "/Assets/Icons/Start_button.png";

    // --- Item Data Configuration ---
    static final String[] ITEM_IMAGES = {
        "/Assets/Items/item_1.png", "/Assets/Items/item_2.png", "/Assets/Items/item_3.png",
        "/Assets/Items/item_4.png", "/Assets/Items/item_5.png", "/Assets/Items/item_6.png",
        "/Assets/Items/item_7.png", "/Assets/Items/item_8.png", "/Assets/Items/item_9.png",
        "/Assets/Items/item_10.png"
    };

    static final String[] ITEM_NAMES = {
        "NONSTOP energy", "Missmaxprint", "Piggy bank", "Attack", "Heal",
        "Shield", "Shield belt", "Reece wheel", "Gashapon keychain", "Carleon belt"
    };

    static final String[] ITEM_DESCRIPTIONS = {
        "Instantly restores 25 HP.",
        "Instantly deals 15 damage\nto ALL enemies in the room.",
        "Permanently increases Max HP by +20\nand instantly heals you for 20.",
        "Permanently adds +2 to all Attack Rolls.",
        "Permanently adds +2 to all Heal Rolls.",
        "Permanently adds +2 to all Shield Rolls,\nincreases Shield cap from 15 to 25.",
        "Thorns: Attackers take 2 damage.",
        "Automatically rerolls any 1s\nyou get on your dice.",
        "Lifesteal: Attack die 5 or 6 heals 3 HP.",
        "If HP < 20, Attack damage is doubled."
    };
    
    static final int[] ITEM_PRICES = {
        20, 25, 60, 50, 40, 45, 55, 75, 65, 50
    };

    // --- Enemy Size & Layout Configurations ---
    static final double[] ENEMY_WIDTH =  {0, 200, 300, 200, 300, 300, 400, 300, 400, 500, 600};
    static final double[] ENEMY_HEIGHT = {0, 200, 300, 200, 300, 300, 600, 300, 600, 500, 600};
    static final double[] ENEMY_SOLO_X = {0, 950, 950, 950, 950, 950, 875, 950, 875, 825, 815};
    static final double[] ENEMY_DUO_X1 = {0, 800, 800, 800, 800, 800, 800, 800, 800, 800, 800};
    static final double[] ENEMY_DUO_X2 = {0, 1050, 1050, 1050, 1050, 1050, 1050, 1050, 1050, 1050, 1050};
    static final double[] ENEMY_Y =      {0, 550, 425, 550, 425, 425, 350, 425, 350, 225, 125};

    // --- Game State Variables ---
    TileType[][] map = new TileType[MAX_MAP_W][MAX_MAP_H];
    Random rng = new Random();
    Player player;
    Point2D stairsPos = null;   // Coordinates for the exit to the next floor
    int floorLevel = 1;         // Current dungeon floor
    
    int earlyDuoCount = 0;      // Tracks how many duo-enemy fights spawned early
    int lateDuoCount = 0;       // Tracks how many duo-enemy fights spawned late
    
    // --- Cached Images ---
    Image heroBasicImage = null, heroLeftImage = null, heroRightImage = null;
    Image heroBasicImage2x = null, heroLeftImage2x = null, heroRightImage2x = null;
    Image heroAttack1 = null, heroAttack2 = null, heroAttack3 = null;

    Image[] diceImages = new Image[6];
    Image[] healDiceImages = new Image[6];
    Image[] shieldDiceImages = new Image[6];
    Image[] rollAnimationImages = new Image[16];
    Image[] loadedItems = new Image[10];

    Image floorImage = null, stairsImage = null, closedStairsImage = null;
    Image topWallImage = null, bottomWallImage = null, leftWallImage = null, rightWallImage = null;
    Image sideLeftWallImage = null, sideRightWallImage = null;
    Image cornerLeftWallImage = null, cornerRightWallImage = null;
    Image basicFightImage = null, chestImage = null, fightImage = null, shopImage = null;
    Image fightBgImage = null, chestBgImage = null, chestBgOpenImage = null, shopBgImage = null;
    Image merchantImage = null, merchantImageMiddle = null, merchantImageLow = null, shopTableImage = null;
    Image startButtonImage = null;

    // --- Runtime Flow State ---
    boolean isGameStarted = false;
    boolean inRoom = false;
    boolean isChestOpened = false;
    boolean fightWon = false; 
    RoomType currentRoom = null;
    int currentFightEnemyId = -1;
    
    boolean isDuoFight = false;
    int currentEnemyHp = 0;
    int currentEnemy2Hp = 0;
    
    // UI nodes mapped to specific dynamic views
    ImageView roomBackground = null;
    ImageView heroInFight = null; 
    ImageView currentEnemyView = null;
    ImageView currentEnemy2View = null;
    ImageView attackButtonView = null;
    
    boolean isAnimating = false; // Blocks input while animations play

    // --- JavaFX Scene Graph Groups ---
    Group startScreenGroup = new Group();
    Group lostScreenGroup = new Group(); 
    Group winScreenGroup = new Group();
    Group roomGroup = new Group();
    Group dungeonGroup = new Group(); // Contains the grid and overworld entities

    Group root = new Group();
    Group mapGroup = new Group();
    Group entityGroup = new Group();
    Group uiGroup = new Group();
    Group uiInventory = new Group();

    // Shop State
    int[] currentShopItems = new int[4];
    ImageView[] shopItemViews = new ImageView[4];
    int selectedShopIndex = -1;
    Group shopDialogGroup = new Group();

    // HUD Text Elements
    Text uiHp;
    Text uiShield;
    ImageView uiCoinIcon;
    Text uiCoinsText;
    Text uiStatsRest;
    Text uiMessage;
    Text uiEnemyHp; 
    
    Group instructionGroup = new Group();
    Rectangle instructionBg;
    Text instructionText; 

    // Timelines for handling animations and scheduled tasks
    Timeline messageTimeline;
    Timeline merchantTimeline;
    Timeline heroFightTimeline; 
    Timeline enemy1IdleTimeline;
    Timeline enemy2IdleTimeline;
    String lastMessage = "";
    Stage stage;

    // List tracking all interactive nodes on the current floor
    List<ShapeObject> shapeObjects = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Load all assets silently (catching exceptions if missing to prevent crashes)
        // Hero base
        try { heroBasicImage = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_BASIC)); } catch (Exception e) {}
        try { heroLeftImage = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_LEFT)); } catch (Exception e) {}
        try { heroRightImage = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_RIGHT)); } catch (Exception e) {}
        // Hero 2x scaled (Combat)
        try { heroBasicImage2x = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_BASIC_2X)); } catch (Exception e) {}
        try { heroLeftImage2x = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_LEFT_2X)); } catch (Exception e) {}
        try { heroRightImage2x = new Image(getClass().getResourceAsStream(PLAYER_IMAGE_RIGHT_2X)); } catch (Exception e) {}
        // Hero Attack
        try { heroAttack1 = new Image(getClass().getResourceAsStream(HERO_ATTACK_1)); } catch (Exception e) {}
        try { heroAttack2 = new Image(getClass().getResourceAsStream(HERO_ATTACK_2)); } catch (Exception e) {}
        try { heroAttack3 = new Image(getClass().getResourceAsStream(HERO_ATTACK_3)); } catch (Exception e) {}
        
        // Attack Dice
        try { diceImages[0] = new Image(getClass().getResourceAsStream(DICE_1)); } catch (Exception e) {}
        try { diceImages[1] = new Image(getClass().getResourceAsStream(DICE_2)); } catch (Exception e) {}
        try { diceImages[2] = new Image(getClass().getResourceAsStream(DICE_3)); } catch (Exception e) {}
        try { diceImages[3] = new Image(getClass().getResourceAsStream(DICE_4)); } catch (Exception e) {}
        try { diceImages[4] = new Image(getClass().getResourceAsStream(DICE_5)); } catch (Exception e) {}
        try { diceImages[5] = new Image(getClass().getResourceAsStream(DICE_6)); } catch (Exception e) {}

        // Heal & Shield Dice / Dice roll animation frames
        for (int i = 1; i <= 6; i++) {
            try { healDiceImages[i-1] = new Image(getClass().getResourceAsStream("/Assets/Dice/Heal_dice_side" + i + ".png")); } catch (Exception e) {}
            try { shieldDiceImages[i-1] = new Image(getClass().getResourceAsStream("/Assets/Dice/Shield_dice_side" + i + ".png")); } catch (Exception e) {}
        }
        for (int i = 1; i <= 16; i++) {
            try { rollAnimationImages[i-1] = new Image(getClass().getResourceAsStream("/Assets/Dice/Roll_animation" + i + ".png")); } catch (Exception e) {}
        }

        // Enemy images
        for (int i = 0; i < 8; i++) {
            try { ENEMY_IDLE_IMGS[i][0] = new Image(getClass().getResourceAsStream("/Assets/Characters/enemy_" + (i+1) + ".png")); } catch (Exception e) {}
            try { ENEMY_IDLE_IMGS[i][1] = new Image(getClass().getResourceAsStream("/Assets/Characters/enemy_" + (i+1) + "_left.png")); } catch (Exception e) {}
            try { ENEMY_IDLE_IMGS[i][2] = new Image(getClass().getResourceAsStream("/Assets/Characters/enemy_" + (i+1) + "_right.png")); } catch (Exception e) {}

            try { ENEMY_ATTACK_IMGS[i][0] = ENEMY_IDLE_IMGS[i][0]; } catch (Exception e) {}
            try { ENEMY_ATTACK_IMGS[i][1] = new Image(getClass().getResourceAsStream("/Assets/Characters/enemy_" + (i+1) + "_attack1.png")); } catch (Exception e) {}
            try { ENEMY_ATTACK_IMGS[i][2] = new Image(getClass().getResourceAsStream("/Assets/Characters/enemy_" + (i+1) + "_attack2.png")); } catch (Exception e) {}
        }

        // Boss images
        try { BOSS1_IDLE[0] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_1.png")); } catch (Exception e) {}
        try { BOSS1_IDLE[1] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_1_move1.png")); } catch (Exception e) {}
        try { BOSS1_IDLE[2] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_1_move2.png")); } catch (Exception e) {}
        try { BOSS1_ATTACK[0] = BOSS1_IDLE[0]; } catch (Exception e) {}
        try { BOSS1_ATTACK[1] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_1_attack1.png")); } catch (Exception e) {}
        try { BOSS1_ATTACK[2] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_1_attack2.png")); } catch (Exception e) {}

        try { BOSS2_IDLE[0] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2.png")); } catch (Exception e) {}
        try { BOSS2_IDLE[1] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2_move1.png")); } catch (Exception e) {}
        try { BOSS2_IDLE[2] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2_move2.png")); } catch (Exception e) {}
        try { BOSS2_ATTACK[0] = BOSS2_IDLE[0]; } catch (Exception e) {}
        try { BOSS2_ATTACK[1] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2_attack1.png")); } catch (Exception e) {}
        try { BOSS2_ATTACK[2] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2_attack2.png")); } catch (Exception e) {}
        try { BOSS2_ATTACK[3] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2_attack3.png")); } catch (Exception e) {}
        try { BOSS2_ATTACK[4] = new Image(getClass().getResourceAsStream("/Assets/Characters/boss_2_attack4.png")); } catch (Exception e) {}

        // Items and environment tiles
        for (int i = 0; i < 10; i++) {
            try { loadedItems[i] = new Image(getClass().getResourceAsStream(ITEM_IMAGES[i])); } catch (Exception e) {}
        }
        try { floorImage = new Image(getClass().getResourceAsStream(FLOOR_IMAGE)); } catch (Exception e) {}
        try { stairsImage = new Image(getClass().getResourceAsStream(STAIRS_IMAGE)); } catch (Exception e) {}
        try { closedStairsImage = new Image(getClass().getResourceAsStream(CLOSED_STAIRS_IMAGE)); } catch (Exception e) {}
        
        try { topWallImage = new Image(getClass().getResourceAsStream(TOP_WALL_IMAGE)); } catch (Exception e) {}
        try { bottomWallImage = new Image(getClass().getResourceAsStream(BOTTOM_WALL_IMAGE)); } catch (Exception e) {}
        try { leftWallImage = new Image(getClass().getResourceAsStream(LEFT_WALL_IMAGE)); } catch (Exception e) {}
        try { rightWallImage = new Image(getClass().getResourceAsStream(RIGHT_WALL_IMAGE)); } catch (Exception e) {}
        try { sideLeftWallImage = new Image(getClass().getResourceAsStream(SIDE_LEFT_WALL_IMAGE)); } catch (Exception e) {}
        try { sideRightWallImage = new Image(getClass().getResourceAsStream(SIDE_RIGHT_WALL_IMAGE)); } catch (Exception e) {}
        try { cornerLeftWallImage = new Image(getClass().getResourceAsStream(CORNER_LEFT_WALL_IMAGE)); } catch (Exception e) {}
        try { cornerRightWallImage = new Image(getClass().getResourceAsStream(CORNER_RIGHT_WALL_IMAGE)); } catch (Exception e) {}

        try { basicFightImage = new Image(getClass().getResourceAsStream(BASIC_FIGHT_IMAGE)); } catch (Exception e) {}
        try { chestImage = new Image(getClass().getResourceAsStream(CHEST_IMAGE)); } catch (Exception e) {}
        try { fightImage = new Image(getClass().getResourceAsStream(FIGHT_IMAGE)); } catch (Exception e) {}
        try { shopImage = new Image(getClass().getResourceAsStream(SHOP_IMAGE)); } catch (Exception e) {}

        try { fightBgImage = new Image(getClass().getResourceAsStream(FIGHT_BG)); } catch (Exception e) {}
        try { chestBgImage = new Image(getClass().getResourceAsStream(CHEST_BG)); } catch (Exception e) {}
        try { chestBgOpenImage = new Image(getClass().getResourceAsStream(CHEST_BG_OPEN)); } catch (Exception e) {}
        try { shopBgImage = new Image(getClass().getResourceAsStream(SHOP_BG)); } catch (Exception e) {}

        try { merchantImage = new Image(getClass().getResourceAsStream(MERCHANT_IMAGE)); } catch (Exception e) {}
        try { merchantImageMiddle = new Image(getClass().getResourceAsStream(MERCHANT_IMAGE_MIDDLE)); } catch (Exception e) {}
        try { merchantImageLow = new Image(getClass().getResourceAsStream(MERCHANT_IMAGE_LOW)); } catch (Exception e) {}
        try { shopTableImage = new Image(getClass().getResourceAsStream(SHOP_TABLE_IMAGE)); } catch (Exception e) {}
        try { startButtonImage = new Image(getClass().getResourceAsStream(START_BUTTON_IMAGE)); } catch (Exception e) {}

        // Initialize logical map structure
        for (int x = 0; x < MAX_MAP_W; x++) {
            for (int y = 0; y < MAX_MAP_H; y++) {
                map[x][y] = TileType.WALL;
            }
        }

        // Set visual layer order (Lower numbers render on top)
        uiGroup.setViewOrder(-1);
        startScreenGroup.setViewOrder(-2);
        lostScreenGroup.setViewOrder(-3); 
        winScreenGroup.setViewOrder(-3);

        // Setup Screen Overlays (Start/Win/Loss)
        try {
            Image startBg = new Image(getClass().getResourceAsStream(START_BG_IMAGE));
            ImageView startBgView = new ImageView(startBg);
            startBgView.setFitWidth(MAX_MAP_W * TILE);
            startBgView.setFitHeight(MAX_MAP_H * TILE + UI_HEIGHT + 2);
            startScreenGroup.getChildren().add(startBgView);
        } catch (Exception e) {
            // Fallback if image fails to load
            Rectangle startFallback = new Rectangle(MAX_MAP_W * TILE, MAX_MAP_H * TILE + UI_HEIGHT + 2);
            startFallback.setFill(Color.BLACK);
            startScreenGroup.getChildren().add(startFallback);
        }

        try {
            Image lostBg = new Image(getClass().getResourceAsStream(LOST_BG_IMAGE));
            ImageView lostBgView = new ImageView(lostBg);
            lostBgView.setFitWidth(MAX_MAP_W * TILE);
            lostBgView.setFitHeight(MAX_MAP_H * TILE + UI_HEIGHT + 2);
            lostScreenGroup.getChildren().add(lostBgView);
        } catch (Exception e) {
            Rectangle lostFallback = new Rectangle(MAX_MAP_W * TILE, MAX_MAP_H * TILE + UI_HEIGHT + 2);
            lostFallback.setFill(Color.DARKRED);
            lostScreenGroup.getChildren().add(lostFallback);
        }
        lostScreenGroup.setVisible(false);
        
        try {
            Image winBg = new Image(getClass().getResourceAsStream(ENDING_BG_IMAGE));
            ImageView winBgView = new ImageView(winBg);
            winBgView.setFitWidth(MAX_MAP_W * TILE);
            winBgView.setFitHeight(MAX_MAP_H * TILE + UI_HEIGHT + 2);
            winScreenGroup.getChildren().add(winBgView);
        } catch (Exception e) {
            Rectangle winFallback = new Rectangle(MAX_MAP_W * TILE, MAX_MAP_H * TILE + UI_HEIGHT + 2);
            winFallback.setFill(Color.GOLD);
            winScreenGroup.getChildren().add(winFallback);
        }
        winScreenGroup.setVisible(false);

        // Combine groups into the root
        dungeonGroup.getChildren().addAll(mapGroup, entityGroup);
        root.getChildren().addAll(dungeonGroup, roomGroup, uiGroup, startScreenGroup, lostScreenGroup, winScreenGroup);
        roomGroup.setVisible(false);

        // Initial Game State setup
        setMapSizeForLevel();
        generateDungeon();
        drawMap();

        int[] playerPos = findFreePosition();
        player = new Player(playerPos[0], playerPos[1]);
        entityGroup.getChildren().add(player.sprite);

        placeShapes(player.x, player.y);
        drawMap();
        player.sprite.toFront();

        // Setup top UI overlay bar
        Rectangle uiBackground = new Rectangle(0, 0, MAX_MAP_W * TILE, UI_HEIGHT);
        uiBackground.setFill(Color.rgb(20, 20, 30, 0.95));
        Rectangle uiSeparator = new Rectangle(0, UI_HEIGHT, MAX_MAP_W * TILE, 2);
        uiSeparator.setFill(Color.DARKGRAY);
        
        uiHp = new Text(15, 22, "");
        uiHp.setFill(Color.LIME);
        uiHp.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));

        uiShield = new Text(15, 22, "");
        uiShield.setFill(Color.DEEPSKYBLUE);
        uiShield.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        
        uiCoinIcon = new ImageView();
        try {
            Image coinImg = new Image(getClass().getResourceAsStream(COINS_IMAGE));
            uiCoinIcon.setImage(coinImg);
        } catch (Exception e) {}
        uiCoinIcon.setFitWidth(25);
        uiCoinIcon.setFitHeight(25);
        uiCoinIcon.setTranslateY(4);
        
        uiCoinsText = new Text();
        uiCoinsText.setFill(Color.GOLD);
        uiCoinsText.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        uiCoinsText.setY(22);

        uiStatsRest = new Text(15, 22, "");
        uiStatsRest.setFill(Color.LIME);
        uiStatsRest.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        
        uiMessage = new Text(15, 42, "");
        uiMessage.setFill(Color.YELLOW);
        uiMessage.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        uiEnemyHp = new Text(MAX_MAP_W * TILE - 330, 22, "");
        uiEnemyHp.setFill(Color.RED);
        uiEnemyHp.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        uiEnemyHp.setVisible(false);

        uiGroup.getChildren().addAll(uiBackground, uiSeparator, uiHp, uiShield, uiCoinIcon, uiCoinsText, uiStatsRest, uiMessage, uiEnemyHp, uiInventory);

        // Initialize Scene and bind Input
        Scene scene = new Scene(root, MAX_MAP_W * TILE, MAX_MAP_H * TILE + UI_HEIGHT + 2, Color.BLACK);
        scene.setOnKeyPressed(e -> input(e.getCode()));

        stage.setTitle("Dungeon Crawler - Floor " + floorLevel);
        stage.setScene(scene);
        stage.show();

        // Standard Game loop mapping updates to frame render
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isGameStarted) {
                    player.update();
                    updateUI();
                }
            }
        }.start();
    }

    /**
     * Resets the map dimensions for the level. Currently fixed size, but allows for expansion.
     */
    void setMapSizeForLevel() {
        MAP_W = MAX_MAP_W;
        MAP_H = MAX_MAP_H;
    }

    /**
     * Duo fights (IDs 11 and 12) represent a pair of actual enemy designs.
     * This method resolves the internal pseudo-ID into the real enemy sprite ID.
     */
    int getRealEnemyId(int id, int index) {
        if (id == 11) {
            return 2; // Two of enemy 2
        }
        if (id == 12) {
            if (index == 1) {
                return 5; // Left is enemy 5
            } else {
                return 6; // Right is enemy 6
            }
        }
        return id;
    }

    /**
     * Creates an idle bouncing animation timeline for an enemy sprite.
     */
    Timeline createEnemyIdleTimeline(ImageView view, int eId) {
        Image[] frames;
        double frameDuration;

        // Populate frames arrays based on enemy type
        if (eId >= 1 && eId <= 8) {
            Image b = ENEMY_IDLE_IMGS[eId - 1][0];
            Image l = ENEMY_IDLE_IMGS[eId - 1][1];
            Image r = ENEMY_IDLE_IMGS[eId - 1][2];
            frames = new Image[]{b, l, b, r};
            frameDuration = 0.3;
        } else if (eId == 9) {
            frames = new Image[]{BOSS1_IDLE[0], BOSS1_IDLE[1], BOSS1_IDLE[2]};
            frameDuration = 0.2;
        } else if (eId == 10) {
            frames = new Image[]{BOSS2_IDLE[0], BOSS2_IDLE[1], BOSS2_IDLE[2], BOSS2_IDLE[1]};
            frameDuration = 0.15;
        } else {
            return null;
        }

        final int[] frameIndex = {0};
        Timeline t = new Timeline(
            new KeyFrame(Duration.seconds(frameDuration), e -> {
                frameIndex[0] = (frameIndex[0] + 1) % frames.length;
                if (frames[frameIndex[0]] != null) {
                    view.setImage(frames[frameIndex[0]]);
                }
            })
        );
        t.setCycleCount(Timeline.INDEFINITE);
        return t;
    }

    /**
     * Builds and returns a complex Timeline mapping the physical movement and sprite
     * swapping that represents an enemy attack animation.
     */
    Timeline getAttackAnim(ImageView view, int eId) {
        double startX = view.getTranslateX();
        Timeline anim = new Timeline();
        Image base, a1, a2, a3, a4;
        
        if (eId >= 1 && eId <= 8) {
            base = ENEMY_ATTACK_IMGS[eId - 1][0];
            a1 = ENEMY_ATTACK_IMGS[eId - 1][1];
            a2 = ENEMY_ATTACK_IMGS[eId - 1][2];

            // Specific enemies get a simpler two-step jab
            if (eId == 1 || eId == 3 || eId == 6 || eId == 8) {
                anim.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, e -> { if (a1 != null) view.setImage(a1); }),
                    new KeyFrame(Duration.seconds(0.1), e -> { view.setTranslateX(startX - 15); }),
                    new KeyFrame(Duration.seconds(0.2), e -> { if (a2 != null) view.setImage(a2); }),
                    new KeyFrame(Duration.seconds(0.3), e -> { view.setTranslateX(startX); }),
                    new KeyFrame(Duration.seconds(0.4), e -> { if (a1 != null) view.setImage(a1); }),
                    new KeyFrame(Duration.seconds(0.5), e -> { if (base != null) view.setImage(base); })
                );
            } else {
                // Other enemies get a slightly more frantic 3-step lunge
                anim.getKeyFrames().addAll(
                    new KeyFrame(Duration.ZERO, e -> { if (a1 != null) view.setImage(a1); }),
                    new KeyFrame(Duration.seconds(0.075), e -> { view.setTranslateX(startX - 10); }),
                    new KeyFrame(Duration.seconds(0.15), e -> { if (a2 != null) view.setImage(a2); }),
                    new KeyFrame(Duration.seconds(0.225), e -> { view.setTranslateX(startX - 25); }),
                    new KeyFrame(Duration.seconds(0.25), e -> { if (base != null) view.setImage(base); }),
                    new KeyFrame(Duration.seconds(0.3), e -> { view.setTranslateX(startX - 10); }),
                    new KeyFrame(Duration.seconds(0.375), e -> { if (a2 != null) view.setImage(a2); }),
                    new KeyFrame(Duration.seconds(0.45), e -> { view.setTranslateX(startX); }),
                    new KeyFrame(Duration.seconds(0.525), e -> { if (a1 != null) view.setImage(a1); }),
                    new KeyFrame(Duration.seconds(0.6), e -> { if (base != null) view.setImage(base); })
                );
            }
        } else if (eId == 9) { // Boss 1 animation
            base = BOSS1_ATTACK[0]; a1 = BOSS1_ATTACK[1]; a2 = BOSS1_ATTACK[2];
            anim.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> { if (base != null) view.setImage(base); }),
                new KeyFrame(Duration.seconds(0.01), e -> { view.setTranslateX(startX - 10); }),
                new KeyFrame(Duration.seconds(0.15), e -> { if (a1 != null) view.setImage(a1); }),
                new KeyFrame(Duration.seconds(0.2), e -> { view.setTranslateX(startX - 30); }),
                new KeyFrame(Duration.seconds(0.25), e -> { if (a2 != null) view.setImage(a2); }),
                new KeyFrame(Duration.seconds(0.3), e -> { view.setTranslateX(startX - 10); }),
                new KeyFrame(Duration.seconds(0.35), e -> { if (base != null) view.setImage(base); }),
                new KeyFrame(Duration.seconds(0.45), e -> { view.setTranslateX(startX); })
            );
        } else if (eId == 10) { // Boss 2 animation
            base = BOSS2_ATTACK[0]; a1 = BOSS2_ATTACK[1]; a2 = BOSS2_ATTACK[2]; a3 = BOSS2_ATTACK[3]; a4 = BOSS2_ATTACK[4];
            anim.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, e -> { if (base != null) view.setImage(base); }),
                new KeyFrame(Duration.seconds(0.125), e -> { if (a1 != null) view.setImage(a1); }),
                new KeyFrame(Duration.seconds(0.25), e -> { if (a2 != null) view.setImage(a2); }),
                new KeyFrame(Duration.seconds(0.375), e -> { if (a3 != null) view.setImage(a3); }),
                new KeyFrame(Duration.seconds(0.5), e -> { if (a3 != null) view.setImage(a4); }),
                new KeyFrame(Duration.seconds(0.625), e -> { if (base != null) view.setImage(base); })
            );
        }
        return anim;
    }

    /**
     * Primary Input Handling. Routes keyboard keys depending on the current game state
     * (e.g. title screen, exploration, combat menus).
     */
    void input(KeyCode k) {
        // Start Screen intercept
        if (!isGameStarted) {
            if (k == KeyCode.SPACE) {
                isGameStarted = true;
                startScreenGroup.setVisible(false);
            }
            return;
        }

        // End states intercept
        if (lostScreenGroup.isVisible() || winScreenGroup.isVisible()) {
            stage.close();
            return;
        }

        // Dead player can't act
        if (player.hp <= 0) {
            return; 
        }

        // Hotkey: Use Energy Potion (Item 1) from anywhere
        if (k == KeyCode.DIGIT1 && player.hasItem[1] && !isAnimating) {
            player.hp += 25;
            if (player.hp > player.maxHp) {
                player.hp = player.maxHp;
            }
            player.hasItem[1] = false;
            updateUI();
            showMessage("Used Item 1: Restored 25 HP!", Color.LIME);
            return;
        }

        //  Interaction Inside a Room (Combat/Shop/Chest) 
        if (inRoom) {
            // SHOP LOGIC
            if (currentRoom == RoomType.SHOP) {
                if (k == KeyCode.DIGIT1) selectShopItem(0);
                else if (k == KeyCode.DIGIT2) selectShopItem(1);
                else if (k == KeyCode.DIGIT3) selectShopItem(2);
                else if (k == KeyCode.DIGIT4) selectShopItem(3);
                else if (k == KeyCode.ENTER) buyShopItem();
                else if (k == KeyCode.SPACE || k == KeyCode.ESCAPE) exitRoom();
                return;
            }

            // CHEST LOGIC
            if (currentRoom == RoomType.CHEST) {
                if (!isChestOpened && k == KeyCode.SPACE) {
                    isChestOpened = true;
                    if (roomBackground != null && chestBgOpenImage != null) {
                        roomBackground.setImage(chestBgOpenImage);
                    }
                    
                    List<Integer> available = getAvailableItems();
                    if (available.isEmpty()) {
                        setInstruction("Chest is empty! (Press SPACE to return)", Color.WHITE);
                    } else {
                        // Pick random item and award it to the player
                        int picked = available.get(rng.nextInt(available.size()));
                        giveItem(picked);

                        ImageView iv = new ImageView(loadedItems[picked - 1]);
                        iv.setFitWidth(150); 
                        iv.setFitHeight(150);
                        iv.setTranslateX((MAX_MAP_W * TILE) / 2.0 - 115);
                        iv.setTranslateY((MAX_MAP_H * TILE) / 2.0 - 50 + UI_HEIGHT);
                        roomGroup.getChildren().add(iv);

                        Group box = createDialogBox(ITEM_NAMES[picked - 1], ITEM_DESCRIPTIONS[picked - 1]);
                        roomGroup.getChildren().add(box);

                        setInstruction("Press SPACE to return", Color.WHITE);
                    }
                } else if ((isChestOpened && k == KeyCode.SPACE) || k == KeyCode.ESCAPE) {
                    exitRoom();
                }
                return;
            }

            // COMBAT LOGIC
            if (currentRoom == RoomType.BASIC_FIGHT || currentRoom == RoomType.BOSS_FIGHT) {
                boolean isBattleActive = false;
                if (isDuoFight) {
                    if (currentEnemyHp > 0 || currentEnemy2Hp > 0) isBattleActive = true;
                } else {
                    if (currentEnemyHp > 0) isBattleActive = true;
                }
                
                if (isBattleActive && !isAnimating) {
                    // Hotkey: Use AoE Attack item (Item 2)
                    if (k == KeyCode.DIGIT2 && player.hasItem[2]) {
                        player.hasItem[2] = false;
                        if (currentEnemyHp > 0) currentEnemyHp -= 15;
                        if (currentEnemy2Hp > 0) currentEnemy2Hp -= 15;
                        
                        updateUI();
                        showMessage("Used Item 2: Dealt 15 Damage to All!", Color.ORANGE);
                        checkFightWinCondition();
                        return;
                    }
                    // Roll Dice
                    if (k == KeyCode.SPACE) {
                        performPlayerAttack();
                    }
                } else if (!isBattleActive && !isAnimating) {
                    // Battle over, leave room
                    if (k == KeyCode.SPACE || k == KeyCode.ESCAPE) {
                        exitRoom();
                    }
                }
            }
            return;
        }

        // --- Overworld Grid Movement ---
        int oldX = player.x;
        int oldY = player.y;

        if (k == KeyCode.W || k == KeyCode.UP) movePlayer(0, -1);
        if (k == KeyCode.S || k == KeyCode.DOWN) movePlayer(0, 1);
        if (k == KeyCode.A || k == KeyCode.LEFT) movePlayer(-1, 0);
        if (k == KeyCode.D || k == KeyCode.RIGHT) movePlayer(1, 0);

        if (player.x != oldX || player.y != oldY) {
            drawMap();             // Redraw immediately for corner rendering
            checkShapeCollision(); // See if we stepped on a node (fight/shop/chest)
            
            // Check if standing on stairs
            if (stairsPos != null && player.x == stairsPos.getX() && player.y == stairsPos.getY()) {
                if (hasRemainingFightEnemies()) {
                    showMessage("Defeat all enemies before descending!", Color.RED);
                } else {
                    showMessage("Press SPACE to descend stairs", Color.WHITE);
                }
            }
        }

        // Attempting to use the stairs
        if (k == KeyCode.SPACE) {
            if (stairsPos != null && player.x == stairsPos.getX() && player.y == stairsPos.getY()) {
                if (hasRemainingFightEnemies()) {
                    showMessage("Defeat all enemies before descending!", Color.RED);
                } else {
                    if (floorLevel == 10) {
                        setInstruction("", Color.WHITE); 
                        winScreenGroup.setVisible(true); // Won the game!
                    } else {
                        nextFloor(); // Proceed to next level
                    }
                }
            }
        }
    }
    
    /** Base coin rewards given upon defeating an enemy. */
    int getCoinReward(int enemyId) {
        switch(enemyId) {
            case 1: return 10;
            case 2: return 15;
            case 3: return 15;
            case 4: return 20;
            case 5: return 20;
            case 6: return 25;
            case 7: return 30;
            case 8: return 35;
            case 9: return 50;
            case 10: return 100;
            case 11: return 25;
            case 12: return 45;
            default: return 10;
        }
    }

    /** Helper to show a pop-up text instruction at the bottom of the screen. */
    void setInstruction(String text, Color color) {
        if (text == null || text.isEmpty()) {
            instructionGroup.setVisible(false);
        } else {
            instructionText.setText("* " + text);
            instructionText.setFill(color);
            instructionGroup.setVisible(true);
        }
    }

    /** 
     * Verifies if combat has concluded by checking HP totals.
     * Cleans up dead sprites and triggers victory state if true.
     */
    boolean checkFightWinCondition() {
        // Clean dead enemies
        if (currentEnemyHp <= 0) {
            currentEnemyHp = 0;
            if (currentEnemyView != null) currentEnemyView.setVisible(false);
        }
        if (currentEnemy2Hp <= 0) {
            currentEnemy2Hp = 0;
            if (currentEnemy2View != null) currentEnemy2View.setVisible(false);
        }
        
        // Update HP UI display
        if (isDuoFight) {
            uiEnemyHp.setText(String.format("Enemy 1:%-3d | Enemy 2:%-3d", currentEnemyHp, currentEnemy2Hp));
        } else {
            uiEnemyHp.setText(String.format("Enemy HP:%-3d", currentEnemyHp));
        }
        
        // Evaluate victory
        boolean won = false;
        if (isDuoFight) {
            if (currentEnemyHp <= 0 && currentEnemy2Hp <= 0) won = true;
        } else {
            if (currentEnemyHp <= 0) won = true;
        }

        if (won) {
            if (!fightWon) {
                fightWon = true;
                int reward = getCoinReward(currentFightEnemyId);
                player.coins += reward;
                updateUI();
                
                if (currentFightEnemyId == 10) {
                    setInstruction("Boss Defeated! Press SPACE to return", Color.GOLD);
                    showMessage("YOU DEFEATED THE BOSS! +" + reward + " Coins", Color.GOLD);
                } else {
                    setInstruction("Press SPACE to return", Color.LIME);
                    showMessage("Enemy Defeated! +" + reward + " Coins", Color.LIME);
                }
            }
            isAnimating = false;
            if (attackButtonView != null) {
                attackButtonView.setVisible(false);
            }
            return true;
        }
        return false;
    }

    /**
     * Commences the player's turn in combat. Generates pseudo-random dice rolls,
     * factors in inventory item rerolls, and plays the dropping dice animation.
     */
    void performPlayerAttack() {
        isAnimating = true;

        if (attackButtonView != null) {
            attackButtonView.setVisible(false);
        }

        int attackRoll;
        int healRoll;
        int shieldRoll;
        
        // Item 8 (Reece wheel): Rerolls 1s
        do { attackRoll = rng.nextInt(6) + 1; } while(player.hasItem[8] && attackRoll == 1);
        do { healRoll = rng.nextInt(6) + 1; } while(player.hasItem[8] && healRoll == 1);
        do { shieldRoll = rng.nextInt(6) + 1; } while(player.hasItem[8] && shieldRoll == 1);

        final int finalAttackRoll = attackRoll;
        final int finalHealRoll = healRoll;
        final int finalShieldRoll = shieldRoll;

        // Position nodes for dice display
        double targetX = (MAX_MAP_W * TILE) / 2.0 - 75; 
        double targetY = (MAX_MAP_H * TILE) / 2.0 + UI_HEIGHT - 100;

        ImageView attackDiceView = new ImageView(diceImages[finalAttackRoll - 1]);
        attackDiceView.setFitWidth(150); attackDiceView.setFitHeight(150);
        attackDiceView.setTranslateX(targetX); attackDiceView.setTranslateY(targetY);

        ImageView healDiceView = new ImageView(healDiceImages[finalHealRoll - 1]);
        healDiceView.setFitWidth(150); healDiceView.setFitHeight(150);
        healDiceView.setTranslateX(targetX - 170); healDiceView.setTranslateY(targetY);

        ImageView shieldDiceView = new ImageView(shieldDiceImages[finalShieldRoll - 1]);
        shieldDiceView.setFitWidth(150); shieldDiceView.setFitHeight(150);
        shieldDiceView.setTranslateX(targetX + 170); shieldDiceView.setTranslateY(targetY);

        // Falling dice animation block
        ImageView animView = new ImageView(rollAnimationImages[0]);
        animView.setFitWidth(700); animView.setFitHeight(700);
        animView.setTranslateX(targetX-250); animView.setTranslateY(targetY - 700);
        roomGroup.getChildren().add(animView);
        
        setInstruction("", Color.WHITE); 

        Timeline dropTimeline = new Timeline();
        dropTimeline.setCycleCount(50);
        dropTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(12), e -> {
            animView.setTranslateY(animView.getTranslateY() + 10);
        }));

        // Finish falling -> play tumble frame sequence
        dropTimeline.setOnFinished(e -> {
            Timeline frameTimeline = new Timeline();
            for (int i = 1; i < 16; i++) {
                final int frameIdx = i;
                frameTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(60 * i), ev -> {
                    if (rollAnimationImages[frameIdx] != null) {
                        animView.setImage(rollAnimationImages[frameIdx]);
                    }
                }));
            }
            
            // Wait brief moment viewing results -> Resolve logic
            frameTimeline.setOnFinished(ev2 -> {
                roomGroup.getChildren().remove(animView);
                roomGroup.getChildren().addAll(attackDiceView, healDiceView, shieldDiceView);
                
                Timeline finishSequence = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), ev3 -> {
                        roomGroup.getChildren().removeAll(attackDiceView, healDiceView, shieldDiceView); 
                    }),
                    new KeyFrame(Duration.seconds(2.0), ev3 -> {
                        playAttackAnimation(finalAttackRoll, finalHealRoll, finalShieldRoll); 
                    })
                );
                finishSequence.play();
            });
            frameTimeline.play();
        });
        
        dropTimeline.play();
    }

    /**
     * Resolves mathematical outcome of the dice (including passive items)
     * and visualizes the player character slashing across screen.
     */
    void playAttackAnimation(int damage, int heal, int shieldVal) {
        if (heroFightTimeline != null) heroFightTimeline.pause(); 
        
        double startX = heroInFight.getTranslateX();

        Timeline anim = new Timeline(
            new KeyFrame(Duration.ZERO, e -> { 
                if (heroAttack1 != null) heroInFight.setImage(heroAttack1); 
            }),
            new KeyFrame(Duration.seconds(0.05), e -> { 
                if (heroAttack2 != null) heroInFight.setImage(heroAttack2); 
                heroInFight.setTranslateX(startX + 2); 
            }),
            new KeyFrame(Duration.seconds(0.1), e -> {
                if (heroAttack3 != null) heroInFight.setImage(heroAttack3);
                heroInFight.setTranslateX(startX + 5);
                
                // Stat modification based on inventory
                int actualDamage = damage;
                int actualHeal = heal;
                int actualShield = shieldVal;
                
                if (player.hasItem[4]) actualDamage += 2; // Attack +2
                if (player.hasItem[5]) actualHeal += 2;   // Heal +2
                if (player.hasItem[6]) actualShield += 2; // Shield +2
                
                // Item 9: Lifesteal
                if (player.hasItem[9] && damage >= 5) actualHeal += 3; 
                
                // Item 10: Berzerker rage
                if (player.hasItem[10] && player.hp < 20) actualDamage *= 2;

                // Apply heal
                player.hp += actualHeal;
                if (player.hp > player.maxHp) player.hp = player.maxHp;
                
                // Apply shield
                player.shield += actualShield;
                int maxShield = 15;
                if (player.hasItem[6]) maxShield = 25; // Increase shield cap
                if (player.shield > maxShield) player.shield = maxShield;
                
                // Apply damage to enemy
                if (isDuoFight) {
                    if (currentEnemyHp > 0) currentEnemyHp -= actualDamage;
                    else if (currentEnemy2Hp > 0) currentEnemy2Hp -= actualDamage;
                } else {
                    currentEnemyHp -= actualDamage;
                }
                
                updateUI();
                checkFightWinCondition();
            }),
            new KeyFrame(Duration.seconds(0.2), e -> { 
                if (heroAttack2 != null) heroInFight.setImage(heroAttack2); 
                heroInFight.setTranslateX(startX + 2); 
            }),
            new KeyFrame(Duration.seconds(0.25), e -> { 
                if (heroAttack1 != null) heroInFight.setImage(heroAttack1); 
                heroInFight.setTranslateX(startX); 
            }),
            new KeyFrame(Duration.seconds(0.3), e -> {
                if (heroFightTimeline != null) heroFightTimeline.play(); 
                // If combat isn't won, Enemy turn executes
                if (!checkFightWinCondition()) {
                    performEnemyAttack();
                }
            })
        );
        anim.play();
    }

    /**
     * Calculates enemy hit damage, triggering its attack animation and subtracting player HP/shield.
     */
    void performEnemyAttack() {
        int fixedDamage = 0;
        
        // Define base damage values per enemy class
        switch (currentFightEnemyId) {
            case 1: fixedDamage = 8; break; 
            case 2: fixedDamage = 10; break; 
            case 3: fixedDamage = 12; break; 
            case 4: fixedDamage = 14; break; 
            case 5: fixedDamage = 16; break; 
            case 6: fixedDamage = 18; break; 
            case 7: fixedDamage = 20; break; 
            case 8: fixedDamage = 22; break; 
            case 9: fixedDamage = 18; break; 
            case 10: fixedDamage = 28; break; 
            case 11: 
                if (currentEnemyHp > 0) fixedDamage += 7;
                if (currentEnemy2Hp > 0) fixedDamage += 7;
                break;
            case 12: 
                if (currentEnemyHp > 0) fixedDamage += 12;
                if (currentEnemy2Hp > 0) fixedDamage += 12;
                break;
        }

        final int initialDamage = fixedDamage;
        if (isDuoFight) {
            if (currentEnemyHp > 0 && currentEnemy2Hp > 0) {
                setInstruction("Both enemies attack for " + initialDamage + " damage total!", Color.RED);
            } else if (currentEnemyHp > 0) {
                setInstruction("Enemy 1 attacks for " + initialDamage + " damage!", Color.RED);
            } else {
                setInstruction("Enemy 2 attacks for " + initialDamage + " damage!", Color.RED);
            }
        } else {
            setInstruction("Enemy attacks for " + initialDamage + " damage!", Color.RED);
        }
        
        // Pause idle animations to play attacks
        if (enemy1IdleTimeline != null) enemy1IdleTimeline.pause();
        if (enemy2IdleTimeline != null) enemy2IdleTimeline.pause();

        int eId1 = getRealEnemyId(currentFightEnemyId, 1);
        int eId2 = getRealEnemyId(currentFightEnemyId, 2);

        // Execute visual attacks
        if (currentEnemyHp > 0 && currentEnemyView != null) {
            Timeline anim1 = getAttackAnim(currentEnemyView, eId1);
            anim1.setOnFinished(ev -> { 
                if (enemy1IdleTimeline != null) enemy1IdleTimeline.play(); 
            });
            anim1.play();
        }
        if (isDuoFight && currentEnemy2Hp > 0 && currentEnemy2View != null) {
            Timeline anim2 = getAttackAnim(currentEnemy2View, eId2);
            anim2.setOnFinished(ev -> { 
                if (enemy2IdleTimeline != null) enemy2IdleTimeline.play(); 
            });
            anim2.play();
        }

        // Apply damage values after animation ends
        Timeline seq = new Timeline(
            new KeyFrame(Duration.seconds(1.0), e -> {
                int damageToApply = initialDamage;

                // Ablative Shielding blocks before hitting HP
                if (player.shield > 0) {
                    if (player.shield >= damageToApply) { 
                        player.shield -= damageToApply; 
                        damageToApply = 0; 
                    } else { 
                        damageToApply -= player.shield; 
                        player.shield = 0; 
                    }
                }

                player.hp -= damageToApply;
                if (player.hp < 0) player.hp = 0;
                updateUI();
                
                // Death condition check
                if (player.hp <= 0) {
                    setInstruction("", Color.WHITE); 
                    showMessage("YOU DIED!", Color.RED);
                    isAnimating = false;
                    lostScreenGroup.setVisible(true); // Game Over Menu
                } else {
                    // Item 7: Thorns effect - deal damage back
                    if (player.hasItem[7]) {
                        showMessage("Thorns! Enemy takes 2 damage.", Color.ORANGE);
                        if (isDuoFight) {
                            if (currentEnemyHp > 0) currentEnemyHp -= 2;
                            if (currentEnemy2Hp > 0) currentEnemy2Hp -= 2;
                        } else if (currentEnemyHp > 0) {
                            currentEnemyHp -= 2;
                        }
                        if (checkFightWinCondition()) {
                            return; 
                        }
                    }

                    // Turn goes back to player
                    if (attackButtonView != null) {
                        attackButtonView.setVisible(true);
                        setInstruction("", Color.WHITE); 
                    } else {
                        setInstruction("Press SPACE to attack", Color.WHITE);
                    }
                    isAnimating = false;
                }
            })
        );
        seq.play();
    }

    /** Move character logic (bounds verification) */
    void movePlayer(int dx, int dy) {
        int nx = player.x + dx;
        int ny = player.y + dy;
        if (isWalkable(nx, ny)) {
            player.move(dx, dy);
        }
    }

    /** Helper checking if any combat nodes are left on the overworld floor */
    boolean hasRemainingFightEnemies() {
        for (ShapeObject shape : shapeObjects) {
            if (shape.type == ShapeType.BASIC_FIGHT || shape.type == ShapeType.FIGHT) {
                return true;
            }
        }
        return false;
    }

    /** Defines if exit is locked/unlocked */
    boolean areStairsOpen() {
        return !hasRemainingFightEnemies();
    }

    /** Returns all un-owned item IDs (1-10) for Chests and Shops. */
    List<Integer> getAvailableItems() {
        List<Integer> available = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (!player.hasItem[i]) {
                available.add(i);
            }
        }
        return available;
    }

    /** Registers a specific item into the player's boolean inventory array. */
    void giveItem(int itemId) {
        player.hasItem[itemId] = true;
        // Piggy bank effect (instantly boosts stats on pickup)
        if (itemId == 3) { 
            player.maxHp += 20; 
            player.hp += 20; 
        }
        updateUI();
    }

    /** Helper for UI: builds the black box containing item text details. */
    Group createDialogBox(String name, String desc) {
        Group g = new Group();
        double boxW = 800;
        double boxH = 160;
        double boxX = (MAX_MAP_W * TILE) / 2.0 - boxW / 2.0;
        double boxY = MAX_MAP_H * TILE + UI_HEIGHT - boxH - 20; 

        Rectangle bg = new Rectangle(boxX, boxY, boxW, boxH);
        bg.setFill(Color.BLACK); 
        bg.setStroke(Color.WHITE); 
        bg.setStrokeWidth(5);

        Text nameText = new Text(boxX + 30, boxY + 45, name);
        nameText.setFill(Color.GOLD); 
        nameText.setFont(Font.font("Monospaced", FontWeight.BOLD, 26));

        Text descText = new Text(boxX + 30, boxY + 85, "* " + desc);
        descText.setFill(Color.WHITE); 
        descText.setFont(Font.font("Monospaced", FontWeight.BOLD, 22));
        
        g.getChildren().addAll(bg, nameText, descText);
        return g;
    }

    /** Updates the selected choice in the merchant UI */
    void selectShopItem(int index) {
        if (index >= 0 && index < 4 && currentShopItems[index] != 0) {
            selectedShopIndex = index;
            shopDialogGroup.getChildren().clear();
            int itemId = currentShopItems[index];
            String name = ITEM_NAMES[itemId - 1];
            String desc = ITEM_DESCRIPTIONS[itemId - 1] + "\nPrice: " + ITEM_PRICES[itemId - 1] + " Coins";
            Group box = createDialogBox(name, desc);
            shopDialogGroup.getChildren().add(box);
        }
    }

    /** Subtracts currency and awards item if valid during shop selection */
    void buyShopItem() {
        if (selectedShopIndex != -1 && currentShopItems[selectedShopIndex] != 0) {
            int itemId = currentShopItems[selectedShopIndex];
            int price = ITEM_PRICES[itemId - 1];
            if (player.coins >= price) {
                player.coins -= price;
                giveItem(itemId);
                currentShopItems[selectedShopIndex] = 0; 
                shopItemViews[selectedShopIndex].setVisible(false); 
                shopDialogGroup.getChildren().clear(); 
                selectedShopIndex = -1;
                showMessage("Item acquired!", Color.GOLD);
            } else {
                showMessage("Not enough Coins!", Color.RED);
            }
        }
    }

    /**
     * Swaps the game state from 'dungeon crawling' to 'inside an interactable room'.
     * Sets up backgrounds, UI overlays, timelines, HP configs depending on what
     * node (Shop, Boss, Chest, Basic Fight) was entered.
     */
    void enterRoom(RoomType roomType, int enemyId) {
        inRoom = true; 
        fightWon = false; 
        currentRoom = roomType; 
        currentFightEnemyId = enemyId;
        isDuoFight = (enemyId == 11 || enemyId == 12);

        // Hide dungeon and reveal room group
        dungeonGroup.setVisible(false); 
        roomGroup.setVisible(true); 
        roomGroup.getChildren().clear();

        Image bgImage = null;
        switch (roomType) {
            case BASIC_FIGHT: bgImage = fightBgImage; showMessage("Entered Fight Room!", Color.RED); break;
            case BOSS_FIGHT:  bgImage = fightBgImage; showMessage("Entered Boss Room!", Color.RED); break;
            case CHEST:       bgImage = chestBgImage; isChestOpened = false; showMessage("Entered Chest Room!", Color.GOLD); break;
            case SHOP:        bgImage = shopBgImage; showMessage("Entered Shop!", Color.CYAN); break;
        }

        // Initialize HP thresholds
        switch(enemyId) {
            case 1: currentEnemyHp = 15; break; 
            case 2: currentEnemyHp = 20; break; 
            case 3: currentEnemyHp = 28; break;
            case 4: currentEnemyHp = 40; break; 
            case 5: currentEnemyHp = 45; break; 
            case 6: currentEnemyHp = 55; break;
            case 7: currentEnemyHp = 65; break; 
            case 8: currentEnemyHp = 75; break; 
            case 9: currentEnemyHp = 70; break;
            case 10: currentEnemyHp = 150; break; 
            case 11: currentEnemyHp = 15; currentEnemy2Hp = 15; break;
            case 12: currentEnemyHp = 40; currentEnemy2Hp = 40; break;
        }

        if (bgImage != null) {
            roomBackground = new ImageView(bgImage);
            roomBackground.setFitWidth(MAX_MAP_W * TILE); 
            roomBackground.setFitHeight(MAX_MAP_H * TILE + UI_HEIGHT);
            roomBackground.setTranslateY(UI_HEIGHT);
            roomGroup.getChildren().add(roomBackground);
        }

        // Setup shared instruction text bar
        instructionGroup = new Group();
        double bW = 800, bH = 80;
        double bX = (MAX_MAP_W * TILE) / 2.0 - bW / 2.0;
        double bY = UI_HEIGHT + 20; 

        instructionBg = new Rectangle(bX, bY, bW, bH);
        instructionBg.setFill(Color.BLACK); 
        instructionBg.setStroke(Color.WHITE); instructionBg.setStrokeWidth(5);

        instructionText = new Text(bX + 30, bY + 48, "");
        instructionText.setFill(Color.WHITE); 
        instructionText.setFont(Font.font("Monospaced", FontWeight.BOLD, 24));

        instructionGroup.getChildren().addAll(instructionBg, instructionText);
        roomGroup.getChildren().add(instructionGroup);
        instructionGroup.setVisible(false);

        // Build specific room structures
        if (roomType == RoomType.CHEST) {
            setInstruction("Press SPACE to open chest", Color.WHITE);
        } else if (roomType == RoomType.SHOP) {
            // Setup Merchant sprites and logic
            if (merchantImage != null) {
                ImageView merchant = new ImageView(merchantImage);
                double mWidth = TILE * 3, mHeight = TILE * 3;
                merchant.setFitWidth(mWidth); merchant.setFitHeight(mHeight); 
                merchant.setPreserveRatio(true);
                merchant.setTranslateX((MAX_MAP_W * TILE) / 2.0 - (mWidth / 2.0));
                merchant.setTranslateY((MAX_MAP_H * TILE) / 2.0 + UI_HEIGHT - (mHeight / 2.0) - 80);
                roomGroup.getChildren().add(merchant);

                if (merchantImageMiddle != null && merchantImageLow != null) {
                    Image[] mFrames = {merchantImage, merchantImageMiddle, merchantImageLow, merchantImageMiddle};
                    final int[] mF = {0};
                    merchantTimeline = new Timeline(
                        new KeyFrame(Duration.seconds(0.3), e -> {
                            mF[0] = (mF[0] + 1) % 4; 
                            merchant.setImage(mFrames[mF[0]]);
                        })
                    );
                    merchantTimeline.setCycleCount(Timeline.INDEFINITE); 
                    merchantTimeline.play();
                }
            }
            if (shopTableImage != null) {
                ImageView table = new ImageView(shopTableImage);
                table.setFitWidth(MAX_MAP_W * TILE); table.setFitHeight(MAX_MAP_H * TILE);
                table.setTranslateY(UI_HEIGHT); 
                roomGroup.getChildren().add(table);
            }

            // Distribute randomized shop inventory array
            List<Integer> available = getAvailableItems();
            Collections.shuffle(available);
            Arrays.fill(currentShopItems, 0);
            int shopItemCount = Math.min(4, available.size());
            
            double[] xs = {465, 590, 720, 845};
            double[] txs = {500, 635, 765, 890};

            for(int i = 0; i < shopItemCount; i++) {
                int itemId = available.get(i);
                currentShopItems[i] = itemId;
                
                ImageView iv = new ImageView(loadedItems[itemId - 1]);
                iv.setFitWidth(100); iv.setFitHeight(100);
                iv.setTranslateX(xs[i]); iv.setTranslateY(350);
                roomGroup.getChildren().add(iv);
                shopItemViews[i] = iv;

                Text t = new Text(txs[i], 473, String.valueOf(i+1));
                t.setFill(Color.WHITE); t.setFont(Font.font("Arial", FontWeight.BOLD, 20));
                roomGroup.getChildren().add(t);
            }
            
            shopDialogGroup = new Group();
            roomGroup.getChildren().add(shopDialogGroup);
            setInstruction("Press 1-4 to view, ENTER to buy, SPACE to leave", Color.WHITE);
        } else if (roomType == RoomType.BASIC_FIGHT || roomType == RoomType.BOSS_FIGHT) {
            
            // Build Combat view layout (Player on left, Enemy/Enemies on right)
            if (isDuoFight) {
                uiEnemyHp.setText(String.format("Enemy 1:%-3d | Enemy 2:%-3d", currentEnemyHp, currentEnemy2Hp));
            } else {
                uiEnemyHp.setText(String.format("Enemy HP:%-3d", currentEnemyHp));
            }
            uiEnemyHp.setVisible(true);

            // Left side Player Sprite
            if (heroBasicImage2x != null && heroLeftImage2x != null && heroRightImage2x != null) {
                heroInFight = new ImageView(heroBasicImage2x);
                heroInFight.setFitWidth(TILE * 3); heroInFight.setFitHeight(TILE * 3); 
                heroInFight.setPreserveRatio(true);
                heroInFight.setTranslateX(150); heroInFight.setTranslateY(425);
                roomGroup.getChildren().add(heroInFight);

                Image[] hFrames = {heroBasicImage2x, heroLeftImage2x, heroBasicImage2x, heroRightImage2x};
                final int[] hF = {0};
                heroFightTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.3), e -> {
                        hF[0] = (hF[0] + 1) % 4; 
                        heroInFight.setImage(hFrames[hF[0]]);
                    })
                );
                heroFightTimeline.setCycleCount(Timeline.INDEFINITE); 
                heroFightTimeline.play();
            }

            // Right side Enemy Sprite resolution
            Image img1 = null;
            Image img2 = null;
            
            if (enemyId >= 1 && enemyId <= 8) {
                img1 = ENEMY_IDLE_IMGS[enemyId - 1][0];
            } else if (enemyId == 9) {
                img1 = BOSS1_IDLE[0];
            } else if (enemyId == 10) {
                img1 = BOSS2_IDLE[0];
            } else if (enemyId == 11) { 
                img1 = ENEMY_IDLE_IMGS[1][0]; img2 = ENEMY_IDLE_IMGS[1][0]; 
            } else if (enemyId == 12) { 
                img1 = ENEMY_IDLE_IMGS[4][0]; img2 = ENEMY_IDLE_IMGS[5][0]; 
            } 
            
            int eId1 = getRealEnemyId(enemyId, 1);
            int eId2 = getRealEnemyId(enemyId, 2);

            // Add Enemy 1
            if (img1 != null) {
                currentEnemyView = new ImageView(img1);
                currentEnemyView.setFitWidth(ENEMY_WIDTH[eId1]); currentEnemyView.setFitHeight(ENEMY_HEIGHT[eId1]); 
                currentEnemyView.setPreserveRatio(true);
                
                if (isDuoFight) currentEnemyView.setTranslateX(ENEMY_DUO_X1[eId1]);
                else currentEnemyView.setTranslateX(ENEMY_SOLO_X[eId1]);
                
                currentEnemyView.setTranslateY(ENEMY_Y[eId1]);
                roomGroup.getChildren().add(currentEnemyView);
            }
            // Add Enemy 2 if Duo
            if (isDuoFight && img2 != null) {
                currentEnemy2View = new ImageView(img2);
                currentEnemy2View.setFitWidth(ENEMY_WIDTH[eId2]); currentEnemy2View.setFitHeight(ENEMY_HEIGHT[eId2]); 
                currentEnemy2View.setPreserveRatio(true);
                currentEnemy2View.setTranslateX(ENEMY_DUO_X2[eId2]); currentEnemy2View.setTranslateY(ENEMY_Y[eId2]);
                roomGroup.getChildren().add(currentEnemy2View);
            }

            // Spin up bouncing animation timelines for enemies
            if (currentEnemyView != null) {
                enemy1IdleTimeline = createEnemyIdleTimeline(currentEnemyView, eId1);
                if (enemy1IdleTimeline != null) enemy1IdleTimeline.play();
            }
            if (currentEnemy2View != null) {
                enemy2IdleTimeline = createEnemyIdleTimeline(currentEnemy2View, eId2);
                if (enemy2IdleTimeline != null) enemy2IdleTimeline.play();
            }

            // Central Attack Start Button element
            if (startButtonImage != null) {
                attackButtonView = new ImageView(startButtonImage);
                attackButtonView.setFitWidth(280); attackButtonView.setPreserveRatio(true);
                attackButtonView.setTranslateX((MAX_MAP_W * TILE) / 2.0 - 140);
                attackButtonView.setTranslateY((MAX_MAP_H * TILE) / 2.0 + UI_HEIGHT - 30);
                roomGroup.getChildren().add(attackButtonView);
                setInstruction("", Color.WHITE); 
            } else {
                setInstruction("Press SPACE to attack", Color.WHITE);
            }
        }
        
        instructionGroup.toFront();
    }

    /** 
     * Performs clean-up after leaving a sub-room, pausing animation loops, 
     * nulling variables, and revealing the dungeon map once again.
     */
    void exitRoom() {
        inRoom = false; currentRoom = null; heroInFight = null; 
        isChestOpened = false; fightWon = false;
        
        if (currentEnemyView != null) { currentEnemyView.setVisible(false); currentEnemyView = null; }
        if (currentEnemy2View != null) { currentEnemy2View.setVisible(false); currentEnemy2View = null; }
        
        isDuoFight = false; currentEnemy2Hp = 0; attackButtonView = null; 
        isAnimating = false; uiEnemyHp.setVisible(false);
        
        // Reset volatile buff variables like Shield that don't persist outside fights
        player.shield = 0; 
        updateUI();

        // Kill garbage timelines
        if (merchantTimeline != null) { merchantTimeline.stop(); merchantTimeline = null; }
        if (heroFightTimeline != null) { heroFightTimeline.stop(); heroFightTimeline = null; }
        if (enemy1IdleTimeline != null) { enemy1IdleTimeline.stop(); enemy1IdleTimeline = null; }
        if (enemy2IdleTimeline != null) { enemy2IdleTimeline.stop(); enemy2IdleTimeline = null; }

        Arrays.fill(currentShopItems, 0); 
        Arrays.fill(shopItemViews, null); 
        selectedShopIndex = -1;
        
        // Return view state
        dungeonGroup.setVisible(true); 
        roomGroup.setVisible(false);

        // Delete the interactive event object from the map where the player just stood
        shapeObjects.removeIf(shape -> shape.x == player.x && shape.y == player.y);
        drawMap(); 
        showMessage("Returned to dungeon", Color.LIME);
    }

    /** Prepares the game for the subsequent level depth. */
    void nextFloor() {
        floorLevel++; 
        setMapSizeForLevel();
        
        showMessage("DESCENDING TO FLOOR " + floorLevel + " (" + MAP_W + "x" + MAP_H + ")", Color.PURPLE);
        
        // Reset logic map grid
        for (int x = 0; x < MAX_MAP_W; x++) {
            for (int y = 0; y < MAX_MAP_H; y++) {
                map[x][y] = TileType.WALL;
            }
        }
        
        // Clear all visuals
        mapGroup.getChildren().clear(); 
        entityGroup.getChildren().clear(); 
        shapeObjects.clear();
        
        // Build new floor
        generateDungeon(); 
        drawMap();

        // Relocate player on valid tile
        int[] playerPos = findFreePosition();
        player.x = playerPos[0]; player.y = playerPos[1]; 
        player.update();
        entityGroup.getChildren().add(player.sprite);
        
        placeShapes(player.x, player.y); 
        drawMap(); 
        player.sprite.toFront();

        stage.setTitle("Dungeon Crawler - Floor " + floorLevel); 
        updateUI();
    }

    /**
     * Core Algorithm for Procedural Map Generation.
     * Places a random number of random-sized rectangles (rooms) on the grid
     * making sure they don't overlap, then carves L-shaped hallways joining their centers.
     */
    void generateDungeon() {
        List<Room> rooms = new ArrayList<>();
        int roomCount = rng.nextInt(3) + 3; // 3 to 5 rooms

        for (int i = 0; i < roomCount; i++) {
            int w = rng.nextInt(ROOM_MAX - ROOM_MIN) + ROOM_MIN;
            int h = rng.nextInt(ROOM_MAX - ROOM_MIN) + ROOM_MIN;
            
            int xRange = MAP_W - w - 1;
            int yRange = MAP_H - h - 1;
            
            if (xRange <= 0 || yRange <= 0) continue;

            int x = rng.nextInt(xRange) + 1;
            int y = rng.nextInt(yRange) + 1;
            
            Room room = new Room(x, y, w, h);
            
            // Collision validation against existing rooms
            boolean overlaps = false;
            for (Room r : rooms) {
                if (r.overlaps(room)) { overlaps = true; break; }
            }
            if (!overlaps) { 
                rooms.add(room); 
                carveRoom(room); 
            }
        }

        // Bridge all generated rooms consecutively
        for (int i = 1; i < rooms.size(); i++) {
            Room prev = rooms.get(i - 1);
            Room curr = rooms.get(i);
            carveCorridor(prev.centerX, prev.centerY, curr.centerX, curr.centerY);
        }
        
        // The endpoint is placed exactly in the center of the final generated room
        if (!rooms.isEmpty()) {
            Room lastRoom = rooms.get(rooms.size() - 1);
            stairsPos = new Point2D(lastRoom.centerX, lastRoom.centerY);
            map[lastRoom.centerX][lastRoom.centerY] = TileType.STAIRS;
        }
    }

    /** Writes floor enum to grid array for room footprint */
    void carveRoom(Room room) {
        for (int x = room.x; x < room.x + room.width && x < MAX_MAP_W; x++) {
            for (int y = room.y; y < room.y + room.height && y < MAX_MAP_H; y++) {
                map[x][y] = TileType.FLOOR;
            }
        }
    }

    /** Writes floor enum connecting points using a simple manhattan bend path */
    void carveCorridor(int x1, int y1, int x2, int y2) {
        int x = x1;
        int y = y1;
        while (x != x2) { 
            if (x >= 0 && x < MAX_MAP_W && y >= 0 && y < MAX_MAP_H) map[x][y] = TileType.FLOOR; 
            x += (x < x2) ? 1 : -1; 
        }
        while (y != y2) { 
            if (x >= 0 && x < MAX_MAP_W && y >= 0 && y < MAX_MAP_H) map[x][y] = TileType.FLOOR; 
            y += (y < y2) ? 1 : -1; 
        }
    }

    /** Safety bounds checker for if a specific X,Y is valid ground */
    boolean isFloorOrStairs(int x, int y) {
        if (x < 0 || y < 0 || x >= MAX_MAP_W || y >= MAX_MAP_H) return false;
        return map[x][y] == TileType.FLOOR || map[x][y] == TileType.STAIRS;
    }

    /**
     * Translates logical 2D enum grid into a collection of visual JavaFX Nodes.
     * Includes logic for auto-tiling proper border wall images.
     */
    void drawMap() {
        mapGroup.getChildren().clear();
        boolean stairsOpen = areStairsOpen();

        for (int x = 0; x < MAX_MAP_W; x++) {
            for (int y = 0; y < MAX_MAP_H; y++) {
                double posX = x * TILE;
                double posY = y * TILE + UI_HEIGHT;
                
                // Keep areas outside max dimensions solid black
                if (x >= MAP_W || y >= MAP_H) {
                    Rectangle bgRect = new Rectangle(posX, posY, TILE, TILE);
                    bgRect.setFill(Color.BLACK);
                    mapGroup.getChildren().add(bgRect);
                    continue;
                }

                if (map[x][y] == TileType.WALL) {
                    // Decide if this wall acts as a border visually (touches floor)
                    boolean bordersTop = isFloorOrStairs(x, y - 1);
                    boolean bordersBottom = isFloorOrStairs(x, y + 1);
                    boolean bordersLeft = isFloorOrStairs(x - 1, y);
                    boolean bordersRight = isFloorOrStairs(x + 1, y);

                    if (bordersTop || bordersBottom || bordersLeft || bordersRight) {
                        if (bordersBottom && topWallImage != null) {
                            ImageView wallTile = new ImageView(topWallImage);
                            wallTile.setFitWidth(TILE); wallTile.setFitHeight(TILE);
                            wallTile.setTranslateX(posX); wallTile.setTranslateY(posY);
                            mapGroup.getChildren().add(wallTile);
                        }
                        if (bordersTop && bottomWallImage != null) {
                            ImageView wallTile = new ImageView(bottomWallImage);
                            wallTile.setFitWidth(TILE); wallTile.setFitHeight(TILE);
                            wallTile.setTranslateX(posX); wallTile.setTranslateY(posY);
                            mapGroup.getChildren().add(wallTile);
                        }
                        if (bordersRight && leftWallImage != null) {
                            ImageView wallTile = new ImageView(leftWallImage);
                            wallTile.setFitWidth(TILE); wallTile.setFitHeight(TILE);
                            wallTile.setTranslateX(posX); wallTile.setTranslateY(posY);
                            mapGroup.getChildren().add(wallTile);
                        }
                        if (bordersLeft && rightWallImage != null) {
                            ImageView wallTile = new ImageView(rightWallImage);
                            wallTile.setFitWidth(TILE); wallTile.setFitHeight(TILE);
                            wallTile.setTranslateX(posX); wallTile.setTranslateY(posY);
                            mapGroup.getChildren().add(wallTile);
                        }
                    } else {
                        Rectangle r = new Rectangle(posX, posY, TILE, TILE);
                        r.setFill(Color.BLACK); // Deep non-border voids
                        mapGroup.getChildren().add(r);
                    }

                } else if (map[x][y] == TileType.STAIRS) {
                    if (stairsOpen) {
                        if (stairsImage != null) {
                            ImageView stairsTile = new ImageView(stairsImage);
                            stairsTile.setFitWidth(TILE); stairsTile.setFitHeight(TILE);
                            stairsTile.setTranslateX(posX); stairsTile.setTranslateY(posY);
                            mapGroup.getChildren().add(stairsTile);
                        } else {
                            Rectangle r = new Rectangle(posX, posY, TILE, TILE);
                            r.setFill(Color.PURPLE); r.setStroke(Color.BLACK);
                            mapGroup.getChildren().add(r);
                        }
                        Text stairText = new Text(posX + TILE / 3, posY + TILE / 2 + 5, "⬇");
                        stairText.setFont(Font.font(24)); stairText.setFill(Color.WHITE);
                        mapGroup.getChildren().add(stairText);
                    } else {
                        // Display locked state graphic
                        if (closedStairsImage != null) {
                            ImageView stairsTile = new ImageView(closedStairsImage);
                            stairsTile.setFitWidth(TILE); stairsTile.setFitHeight(TILE);
                            stairsTile.setTranslateX(posX); stairsTile.setTranslateY(posY);
                            mapGroup.getChildren().add(stairsTile);
                        } else {
                            Rectangle r = new Rectangle(posX, posY, TILE, TILE);
                            r.setFill(Color.DARKSLATEGRAY); r.setStroke(Color.BLACK);
                            mapGroup.getChildren().add(r);
                        }
                    }
                } else {
                    // Standard Floor Rendering
                    if (floorImage != null) {
                        ImageView floorTile = new ImageView(floorImage);
                        floorTile.setFitWidth(TILE); floorTile.setFitHeight(TILE);
                        floorTile.setTranslateX(posX); floorTile.setTranslateY(posY);
                        mapGroup.getChildren().add(floorTile);
                    } else {
                        Rectangle r = new Rectangle(posX, posY, TILE, TILE);
                        r.setFill(Color.DIMGRAY); r.setStroke(Color.BLACK);
                        mapGroup.getChildren().add(r);
                    }
                }
            }
        }

        // Run secondary loop to patch corner wall visual overlap glitches (top corners)
        for (int x = 0; x < MAX_MAP_W; x++) {
            for (int y = 0; y < MAX_MAP_H; y++) {
                if (x >= MAP_W || y >= MAP_H) continue;
                if (map[x][y] != TileType.WALL) continue;
                if (!isFloorOrStairs(x, y + 1)) continue;
                
                double posY = y * TILE + UI_HEIGHT;
                int lx = x - 1;
                
                if (lx >= 0 && lx < MAP_W && map[lx][y] == TileType.WALL && !isFloorOrStairs(lx, y+1)) {
                    if (sideRightWallImage != null) {
                        ImageView sv = new ImageView(sideRightWallImage);
                        sv.setFitWidth(TILE); sv.setFitHeight(TILE);
                        sv.setTranslateX(lx * TILE); sv.setTranslateY(posY);
                        mapGroup.getChildren().add(sv);
                    }
                }
                
                int rx = x + 1;
                if (rx < MAP_W && map[rx][y] == TileType.WALL && !isFloorOrStairs(rx, y+1)) {
                    if (sideLeftWallImage != null) {
                        ImageView sv = new ImageView(sideLeftWallImage);
                        sv.setFitWidth(TILE); sv.setFitHeight(TILE);
                        sv.setTranslateX(rx * TILE); sv.setTranslateY(posY);
                        mapGroup.getChildren().add(sv);
                    }
                }
            }
        }
        applyBottomWallCorners();
    }

    /** Secondary patching loop for visual wall tile corners (bottom corners). */
    void applyBottomWallCorners() {
        for (int x = 0; x < MAX_MAP_W; x++) {
            for (int y = 0; y < MAX_MAP_H; y++) {
                if (x >= MAP_W || y >= MAP_H) continue;
                if (map[x][y] != TileType.WALL) continue;
                if (!isFloorOrStairs(x, y - 1)) continue;
                
                double posY = y * TILE + UI_HEIGHT;
                int lx = x - 1;
                
                if (lx >= 0 && lx < MAP_W && map[lx][y] == TileType.WALL && !isFloorOrStairs(lx, y-1)) {
                    if (cornerLeftWallImage != null) {
                        ImageView cv = new ImageView(cornerLeftWallImage);
                        cv.setFitWidth(TILE); cv.setFitHeight(TILE);
                        cv.setTranslateX(lx * TILE); cv.setTranslateY(posY);
                        mapGroup.getChildren().add(cv);
                    }
                }
                
                int rx = x + 1;
                if (rx < MAP_W && map[rx][y] == TileType.WALL && !isFloorOrStairs(rx, y-1)) {
                    if (cornerRightWallImage != null) {
                        ImageView cv = new ImageView(cornerRightWallImage);
                        cv.setFitWidth(TILE); cv.setFitHeight(TILE);
                        cv.setTranslateX(rx * TILE); cv.setTranslateY(posY);
                        mapGroup.getChildren().add(cv);
                    }
                }
            }
        }
    }

    /** 
     * Distributes intractable Nodes based on the strict progression defined by floorLevel.
     */
    void placeShapes(int excludeX, int excludeY) {
        shapeObjects.clear();
        
        // Gather all safe floor tiles to sprinkle events on
        List<int[]> floorPositions = new ArrayList<>();
        for (int x = 0; x < MAP_W; x++) {
            for (int y = 0; y < MAP_H; y++) {
                if (map[x][y] == TileType.FLOOR) {
                    // Never spawn on stairs or directly under the player
                    if (stairsPos != null && x == stairsPos.getX() && y == stairsPos.getY()) continue;
                    if (x == excludeX && y == excludeY) continue;
                    floorPositions.add(new int[]{x, y});
                }
            }
        }

        Collections.shuffle(floorPositions); 
        int posIndex = 0;

        // Structured level design (hardcoded floor pacing)
        switch(floorLevel) {
            case 1: 
                addShape(ShapeType.BASIC_FIGHT, 1, floorPositions.get(posIndex++)); 
                addShape(ShapeType.CHEST, -1, floorPositions.get(posIndex++)); 
                break;
            case 2: 
                addShape(ShapeType.BASIC_FIGHT, 2, floorPositions.get(posIndex++));
                addShape(ShapeType.SHOP, -1, floorPositions.get(posIndex++)); 
                break;
            case 3: 
                addShape(ShapeType.CHEST, -1, floorPositions.get(posIndex++));
                addShape(ShapeType.SHOP, -1, floorPositions.get(posIndex++)); 
                break;
            case 4: 
                addShape(ShapeType.BASIC_FIGHT, 3, floorPositions.get(posIndex++)); 
                addShape(ShapeType.BASIC_FIGHT, 4, floorPositions.get(posIndex++)); 
                addShape(ShapeType.CHEST, -1, floorPositions.get(posIndex++)); 
                break;
            case 5: 
                addShape(ShapeType.FIGHT, 9, floorPositions.get(posIndex++)); // Boss 1
                addShape(ShapeType.CHEST, -1, floorPositions.get(posIndex++)); 
                break;
            case 6: 
                addShape(ShapeType.SHOP, -1, floorPositions.get(posIndex++)); 
                break;
            case 7: 
                addShape(ShapeType.BASIC_FIGHT, 5, floorPositions.get(posIndex++)); 
                addShape(ShapeType.BASIC_FIGHT, 6, floorPositions.get(posIndex++)); 
                addShape(ShapeType.CHEST, -1, floorPositions.get(posIndex++)); 
                break;
            case 8: 
                addShape(ShapeType.BASIC_FIGHT, 7, floorPositions.get(posIndex++));
                break;
            case 9: 
                addShape(ShapeType.BASIC_FIGHT, 8, floorPositions.get(posIndex++)); 
                addShape(ShapeType.CHEST, -1, floorPositions.get(posIndex++)); 
                addShape(ShapeType.SHOP, -1, floorPositions.get(posIndex++)); 
                break;
            case 10: 
            default: 
                addShape(ShapeType.FIGHT, 10, floorPositions.get(posIndex++)); // Final Boss
                break;
        }
    }

    /** Wraps positional config with duo-RNG modifications to populate the scene. */
    void addShape(ShapeType type, int enemyId, int[] pos) {
        if (type == ShapeType.BASIC_FIGHT) {
            boolean allowDuo = true;
            // Prevent duo fights on certain milestone floors
            if (floorLevel == 1 || floorLevel == 4 || floorLevel == 7) {
                allowDuo = false;
            }
            if (allowDuo) {
                // ~40% chance to upgrade an encounter to a duo (early floors)
                if (floorLevel >= 1 && floorLevel <= 4 && earlyDuoCount < 2 && rng.nextDouble() < 0.4) {
                    enemyId = 11; 
                    earlyDuoCount++;
                } 
                // ~40% chance to upgrade an encounter to a duo (late floors)
                else if (floorLevel >= 6 && floorLevel <= 9 && lateDuoCount < 2 && rng.nextDouble() < 0.4) {
                    enemyId = 12; 
                    lateDuoCount++;
                }
            }
        }
        
        ShapeObject shape = new ShapeObject(pos[0], pos[1], type); 
        shape.enemyId = enemyId;
        shapeObjects.add(shape); 
        entityGroup.getChildren().add(shape.sprite);
    }

    /** 
     * Iterates all interactables and verifies if the player is currently standing on them.
     * Initiates the room swap immediately upon stepping onto an icon.
     */
    void checkShapeCollision() {
        for (ShapeObject shape : shapeObjects) {
            if (player.x == shape.x && player.y == shape.y) {
                RoomType roomType;
                switch (shape.type) {
                    case BASIC_FIGHT: roomType = RoomType.BASIC_FIGHT; break;
                    case FIGHT:       roomType = RoomType.BOSS_FIGHT;  break;
                    case CHEST:       roomType = RoomType.CHEST;       break;
                    case SHOP:        roomType = RoomType.SHOP;        break;
                    default:          roomType = RoomType.BASIC_FIGHT;
                }
                
                // Erase from map memory and launch the transition
                entityGroup.getChildren().remove(shape.sprite); 
                enterRoom(roomType, shape.enemyId); 
                return;
            }
        }
    }

    /** 
     * Algorithm locating the furthest valid spot away from the stairs to drop the player,
     * ensuring maximum traversal is usually required.
     */
    int[] findFreePosition() {
        if (stairsPos == null) {
            int x;
            int y; 
            do { 
                x = rng.nextInt(MAP_W); 
                y = rng.nextInt(MAP_H); 
            } while (map[x][y] != TileType.FLOOR);
            return new int[]{x, y};
        }
        
        List<int[]> furthestTiles = new ArrayList<>(); 
        int maxDistance = -1;
        int stairsX = (int) stairsPos.getX();
        int stairsY = (int) stairsPos.getY();
        
        for (int x = 0; x < MAP_W; x++) {
            for (int y = 0; y < MAP_H; y++) {
                if (map[x][y] == TileType.FLOOR && !(x == stairsX && y == stairsY)) {
                    int distance = Math.abs(x - stairsX) + Math.abs(y - stairsY);
                    if (distance > maxDistance) { 
                        maxDistance = distance; 
                        furthestTiles.clear(); 
                        furthestTiles.add(new int[]{x, y}); 
                    } else if (distance == maxDistance) {
                        furthestTiles.add(new int[]{x, y});
                    }
                }
            }
        }
        if (!furthestTiles.isEmpty()) {
            return furthestTiles.get(rng.nextInt(furthestTiles.size()));
        }
        return new int[]{0, 0};
    }

    /** Checks boundaries before character steps. */
    boolean isWalkable(int x, int y) {
        if (x < 0 || y < 0 || x >= MAX_MAP_W || y >= MAX_MAP_H) return false;
        if (x >= MAP_W || y >= MAP_H) return false;
        return map[x][y] == TileType.FLOOR || map[x][y] == TileType.STAIRS;
    }

    /**
     * Pushes a text message into the upper HUD.
     * Starts a 2-second timeout before scrubbing it to empty.
     */
    void showMessage(String text, Color color) {
        if (messageTimeline != null) messageTimeline.stop();
        
        uiMessage.setText(text); 
        uiMessage.setFill(color); 
        lastMessage = text;
        
        messageTimeline = new Timeline(
            new KeyFrame(Duration.seconds(2), e -> { 
                if (uiMessage.getText().equals(lastMessage)) {
                    uiMessage.setText(""); 
                }
            })
        );
        messageTimeline.play();
    }

    /**
     * Pushes all logic-derived stats onto the visible JavaFX HUD text nodes.
     * Manages dynamically placing layout items in the inventory icon row.
     */
    void updateUI() {
        int remainingEnemies = 0;
        for (ShapeObject shape : shapeObjects) {
            if (shape.type == ShapeType.BASIC_FIGHT || shape.type == ShapeType.FIGHT) {
                remainingEnemies++;
            }
        }

        uiHp.setText(String.format("HP:%-3d", player.hp));
        double hpWidth = uiHp.getLayoutBounds().getWidth();

        double shieldWidth = 0;
        if (player.shield > 0) { 
            uiShield.setText("+" + player.shield); 
            uiShield.setX(15 + hpWidth); 
            shieldWidth = uiShield.getLayoutBounds().getWidth(); 
        } else {
            uiShield.setText("");
        }

        uiCoinIcon.setTranslateX(15 + hpWidth + shieldWidth + 10);
        uiCoinsText.setText(String.valueOf(player.coins));
        uiCoinsText.setX(15 + hpWidth + shieldWidth + 10 + 20 + 5);

        String restString;
        if (inRoom && (currentRoom == RoomType.BASIC_FIGHT || currentRoom == RoomType.BOSS_FIGHT)) {
            int roomEnemies = 0;
            if (currentEnemyHp > 0) roomEnemies++;
            if (isDuoFight && currentEnemy2Hp > 0) roomEnemies++;
            
            restString = String.format(" | FLOOR:%d | Enemies in room:%d", floorLevel, roomEnemies);
        } else {
            if (remainingEnemies > 0) {
                restString = String.format(" | FLOOR:%d | Enemies:%d | Stairs:Locked", floorLevel, remainingEnemies);
            } else {
                restString = String.format(" | FLOOR:%d | All Clear! | Stairs:OPEN", floorLevel);
            }
        }
        
        uiStatsRest.setText(restString); 
        uiStatsRest.setX(uiCoinsText.getX() + uiCoinsText.getLayoutBounds().getWidth());

        // Refresh inventory icons loop
        uiInventory.getChildren().clear();
        int itemCount = 0;
        for (int i = 1; i <= 10; i++) {
            if (player.hasItem[i]) itemCount++;
        }
        
        double invStartX = (MAX_MAP_W * TILE) / 2.0 - (itemCount * 45) / 2.0;
        int index = 0;
        
        for (int i = 1; i <= 10; i++) {
            if (player.hasItem[i]) {
                if (loadedItems[i - 1] != null) {
                    ImageView iv = new ImageView(loadedItems[i - 1]);
                    iv.setFitWidth(40); iv.setFitHeight(40); 
                    iv.setTranslateX(invStartX + index * 45); iv.setTranslateY(5);
                    uiInventory.getChildren().add(iv);
                }
                // Add keybind hints over consumables
                if (i == 1 || i == 2) {
                    Text t = new Text(invStartX + index * 45 + 3, 18, String.valueOf(i));
                    t.setFill(Color.WHITE); t.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                    t.setStroke(Color.BLACK); t.setStrokeWidth(1);
                    uiInventory.getChildren().add(t);
                }
                index++;
            }
        }
    }

    // --- Inner Enum Definitions ---
    enum TileType { 
        FLOOR, WALL, STAIRS 
    }
    
    enum ShapeType { 
        BASIC_FIGHT("Basic Fight", Color.ORANGE), 
        CHEST("Chest", Color.GOLD), 
        SHOP("Shop", Color.CYAN), 
        FIGHT("Fight", Color.RED); 
        
        final String name; 
        final Color color; 
        
        ShapeType(String name, Color color) { 
            this.name = name; 
            this.color = color; 
        } 
    }
    
    enum RoomType { 
        BASIC_FIGHT, BOSS_FIGHT, CHEST, SHOP 
    }

    // --- Inner Class Definitions ---

    /** Abstract definition structure for building logical rectangular rooms on the map */
    class Room {
        int x, y, width, height, centerX, centerY;
        
        Room(int x, int y, int w, int h) { 
            this.x = x; this.y = y; this.width = w; this.height = h; 
            this.centerX = x + w / 2; this.centerY = y + h / 2; 
        }
        
        boolean overlaps(Room other) { 
            return x < other.x + other.width && x + width > other.x && y < other.y + other.height && y + height > other.y; 
        }
    }

    /** Base class for movable characters possessing coordinates and graphical presence. */
    class Entity {
        int x, y, hp; 
        Group sprite;
        
        Entity(int x, int y, int hp, Color c) { 
            this.x = x; this.y = y; this.hp = hp; 
            sprite = new Group(); 
        }
        
        void move(int dx, int dy) { 
            x += dx; y += dy; 
        }
        
        void update() { 
            sprite.setTranslateX(x * TILE); 
            sprite.setTranslateY(y * TILE + UI_HEIGHT); 
        }
    }

    /** Physical objects on the ground representing points of interest (Fights, shops, chests). */
    class ShapeObject {
        int x, y;
        int enemyId = -1; 
        ShapeType type; 
        javafx.scene.Node sprite;
        
        ShapeObject(int x, int y, ShapeType type) {
            this.x = x; this.y = y; this.type = type;
            
            Image shapeIcon = null;
            switch (type) {
                case BASIC_FIGHT: shapeIcon = basicFightImage; break;
                case CHEST:       shapeIcon = chestImage; break;
                case SHOP:        shapeIcon = shopImage; break;
                case FIGHT:       shapeIcon = fightImage; break;
            }
            
            if (shapeIcon != null) { 
                ImageView iv = new ImageView(shapeIcon); 
                iv.setFitWidth(TILE * 0.7); iv.setFitHeight(TILE * 0.7); 
                iv.setPreserveRatio(true); 
                sprite = iv; 
            } else { 
                Rectangle rect = new Rectangle(TILE * 0.7, TILE * 0.7);
                rect.setFill(type.color); rect.setStroke(Color.WHITE); rect.setStrokeWidth(2);
                sprite = rect; 
            }
            
            double offset = (TILE - TILE * 0.7) / 2;
            sprite.setTranslateX(x * TILE + offset); 
            sprite.setTranslateY(y * TILE + UI_HEIGHT + offset);
        }
    }

    /** Singleton managing all specific stats for the user controlling the game. */
    class Player extends Entity {
        ImageView imageView; 
        private int currentFrame = 0; 
        private Timeline animationTimeline;
        
        int shield = 0;
        int maxHp = 50;
        int coins = 0; 
        boolean[] hasItem = new boolean[11]; // Index mapping to Item IDs

        Player(int x, int y) {
            super(x, y, 50, Color.LIME);
            
            if (heroBasicImage != null && heroLeftImage != null && heroRightImage != null) {
                Image[] animationFrames = {heroBasicImage, heroLeftImage, heroBasicImage, heroRightImage};
                imageView = new ImageView(heroBasicImage); 
                imageView.setFitWidth(TILE); imageView.setFitHeight(TILE); 
                imageView.setPreserveRatio(true);
                sprite.getChildren().add(imageView);
                
                // Continuous idle animation loop while exploring
                animationTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.3), e -> { 
                        currentFrame = (currentFrame + 1) % 4; 
                        imageView.setImage(animationFrames[currentFrame]); 
                    })
                );
                animationTimeline.setCycleCount(Timeline.INDEFINITE); 
                animationTimeline.play();
            } else {
                Rectangle r = new Rectangle(0, 0, TILE, TILE);
                r.setFill(Color.LIME);
                sprite.getChildren().add(r);
            }
        }
    }

    public static void main(String[] args) { 
        launch(args); 
    }
}
