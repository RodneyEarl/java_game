import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.Timer;

//import java.util.Timer;

/**
 * Class for the board of the game.
 * 
 * Adapted from zetcode.com
 * 
 * @author Rodney Earl
 * @version 1.0
 */
public class Board extends JPanel implements Commons {

	/**
	 * Player object.
	 */
	private Player player;
	/**
	 * Boolean to check of the game is finished or not.
	 */
	private boolean finished = false;
	/**
	 * Arraylist containing the trees of the game.
	 */
	private ArrayList<Tree> trees = new ArrayList<Tree>();
	/**
	 * Arraylist containing the ships of the game.
	 */
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	/**
	 * Arraylist containing the water blocks in the game.
	 */
	private ArrayList<Water> water = new ArrayList<Water>();
	/**
	 * Arraylist containing the chests of the game.
	 */
	private ArrayList<Treasure> chests = new ArrayList<Treasure>();
	/**
	 * Arraylist containing the goal zones of the game.
	 */
	private ArrayList<Goal> goals = new ArrayList<Goal>();

	private SimpleDateFormat sdf;

	private Timer timer;

	private long timeRemaining;

	private boolean outOfTime = false;

	private int currentScore;

	long start;

	long current;

	long end;

	long previous;

	/**
	 * Constructor for the board.
	 */
	public Board() {
		addKeyListener(new MyKeyAdapter());
		setFocusable(true);
		sdf = new SimpleDateFormat("mm : ss");
		initWorld(levelOne);
	}

	/**
	 * Initializer for a level.
	 * 
	 * @param level
	 *            String containing the level layout.
	 */
	public void initWorld(String level) {

		int x = OFFSET;
		int y = OFFSET;

		Tree tree;
		Ship ship;
		Water waterBlock;
		Treasure chest;
		Goal goal;

		for (int pos = 0; pos < level.length(); pos++) {

			char item = level.charAt(pos);

			if (item == '\n') {
				y += SPRITE_WIDTH;
				x = OFFSET;
			} else if (item == 'T') {
				tree = new Tree(x, y);
				trees.add(tree);
				x += SPRITE_WIDTH;
			} else if (item == 'W') {
				waterBlock = new Water(x, y);
				water.add(waterBlock);
				x += SPRITE_WIDTH;
			} else if (item == 'S') {
				ship = new Ship(x, y);
				ships.add(ship);
				x += SPRITE_WIDTH;
			} else if (item == '$') {
				chest = new Treasure(x, y);
				chests.add(chest);
				x += SPRITE_WIDTH;
			} else if (item == '.') {
				goal = new Goal(x, y);
				goals.add(goal);
				x += SPRITE_WIDTH;
			} else if (item == '@') {
				player = new Player(x, y);
				x += SPRITE_WIDTH;
			} else if (item == ' ') {
				x += SPRITE_WIDTH;
			}
		}

		currentScore = 0;
		timeRemaining = GAME_TIME;
		timer = new Timer(1000, new CDT());
		timer.start();
	}

	/**
	 * Paint method for java guis.
	 * 
	 * @param g
	 *            Graphic object being painted.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(250, 240, 170));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		ArrayList<Sprite> world = new ArrayList<Sprite>();
		world.addAll(trees);
		world.addAll(ships);
		world.addAll(water);
		world.addAll(goals);
		world.addAll(chests);
		world.add(player);

		for (int i = 0; i < world.size(); i++) {

			Sprite item = world.get(i);

			if ((item instanceof Player) || (item instanceof Treasure)) {
				g.drawImage(item.getImage(), item.getX() + 2, item.getY() + 2,
						this);
			} else {
				g.drawImage(item.getImage(), item.getX(), item.getY(), this);
			}

			if (outOfTime)
				finished = true;

			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Verdana", Font.BOLD, 24));
			g2d.drawString(sdf.format(new Date(timeRemaining)),
					BOARD_WIDTH - 150, OFFSET * 8);

			g2d.drawString(currentScore + "/" + goals.size(),
					BOARD_WIDTH / 2 - 25, OFFSET * 8);
			g2d.drawString("Press R to restart.", 700, BOARD_HEIGHT - 40);
			
			if (finished) {
				SoundEffect.SONG.stoploop();
				timer.stop();

				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("Verdana", Font.BOLD, 48));
				g2d.drawString("Game Over", 150,
						BOARD_HEIGHT / 2);
				if (outOfTime)
					g2d.drawString("Out of Time", 150,
							BOARD_HEIGHT / 2 + 50);

				g2d.setColor(Color.GRAY);
				g2d.setFont(new Font("Verdana", Font.BOLD, 24));
				g2d.drawString("Game made by Bowen Hui",
						OFFSET, BOARD_HEIGHT - 130);
				g2d.drawString("and Rodney Earl", OFFSET, BOARD_HEIGHT - 100);
				g2d.drawString("Music from Kevin MacLeod", OFFSET,
						BOARD_HEIGHT - 70);
				g2d.drawString("Sound effects from MediaCollege.com", OFFSET,
						BOARD_HEIGHT - 40);
			}

		}
	}

	/**
	 * Custom key adapter class to act upon certain key actions.
	 * 
	 * @author Rodney Earl
	 * @version 1.0
	 */
	private class MyKeyAdapter extends KeyAdapter {

		/**
		 * Method called whenever a key is pressed on the keyboard.
		 * 
		 * @param e
		 *            Key pressed.
		 */
		public void keyPressed(KeyEvent e) {

			if (finished) {
				return;
			}

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_UP) {

				if (checkTreeCollision(TOP_COLLISION, player)) {
					return;
				}
				if (checkWaterCollision(TOP_COLLISION, player)) {
					return;
				}
				if (checkShipCollision(TOP_COLLISION, player)) {
					return;
				}

				if (checkChestCollision(TOP_COLLISION)) {
					return;
				}

				player.move(0, -SPRITE_WIDTH);
			} else if (key == KeyEvent.VK_RIGHT) {
				if (checkTreeCollision(RIGHT_COLLISION, player)) {
					return;
				}
				if (checkWaterCollision(RIGHT_COLLISION, player)) {
					return;
				}
				if (checkShipCollision(RIGHT_COLLISION, player)) {
					return;
				}

				if (checkChestCollision(RIGHT_COLLISION)) {
					return;
				}

				player.move(SPRITE_WIDTH, 0);
			} else if (key == KeyEvent.VK_DOWN) {
				if (checkTreeCollision(BOTTOM_COLLISION, player)) {
					return;
				}
				if (checkWaterCollision(BOTTOM_COLLISION, player)) {
					return;
				}
				if (checkShipCollision(BOTTOM_COLLISION, player)) {
					return;
				}

				if (checkChestCollision(BOTTOM_COLLISION)) {
					return;
				}

				player.move(0, SPRITE_WIDTH);
			} else if (key == KeyEvent.VK_LEFT) {
				if (checkTreeCollision(LEFT_COLLISION, player)) {
					return;
				}
				if (checkWaterCollision(LEFT_COLLISION, player)) {
					return;
				}
				if (checkShipCollision(LEFT_COLLISION, player)) {
					return;
				}

				if (checkChestCollision(LEFT_COLLISION)) {
					return;
				}

				player.move(-SPRITE_WIDTH, 0);
			} else if (key == KeyEvent.VK_R) {
				restartLevel();
			}

			repaint();
		}

		/**
		 * Method to check if an object is going to collide with a tree.
		 * 
		 * @param type
		 *            What type of collision; top, right, bottom, or left.
		 * @param object
		 *            Object that is being checked if it will collide with a
		 *            tree.
		 * @return True if there is a collision, false otherwise.
		 */
		private boolean checkTreeCollision(int type, Sprite object) {

			if (type == TOP_COLLISION) {
				for (int index = 0; index < trees.size(); index++) {
					Tree tree = trees.get(index);
					if (object.isTopCollision(tree))
						return true;
				}
				return false;
			} else if (type == RIGHT_COLLISION) {
				for (int index = 0; index < trees.size(); index++) {
					Tree tree = trees.get(index);
					if (object.isRightCollision(tree))
						return true;
				}
				return false;
			} else if (type == BOTTOM_COLLISION) {
				for (int index = 0; index < trees.size(); index++) {
					Tree tree = trees.get(index);
					if (object.isBottomCollision(tree))
						return true;
				}
				return false;
			} else if (type == LEFT_COLLISION) {
				for (int index = 0; index < trees.size(); index++) {
					Tree tree = trees.get(index);
					if (object.isLeftCollision(tree))
						return true;
				}
				return false;
			}
			return false;
		}

		/**
		 * Method to check if an object is going to collide with a ship.
		 * 
		 * @param type
		 *            What type of collision; top, right, bottom, or left.
		 * @param object
		 *            Object that is being checked if it will collide with a
		 *            ship.
		 * @return True if there is a collision, false otherwise.
		 */
		private boolean checkShipCollision(int type, Sprite object) {

			if (type == TOP_COLLISION) {
				for (int index = 0; index < ships.size(); index++) {
					Ship ship = ships.get(index);
					if (object.isTopCollision(ship))
						return true;
				}
				return false;
			} else if (type == RIGHT_COLLISION) {
				for (int index = 0; index < ships.size(); index++) {
					Ship ship = ships.get(index);
					if (object.isRightCollision(ship))
						return true;
				}
				return false;
			} else if (type == BOTTOM_COLLISION) {
				for (int index = 0; index < ships.size(); index++) {
					Ship ship = ships.get(index);
					if (object.isBottomCollision(ship))
						return true;
				}
				return false;
			} else if (type == LEFT_COLLISION) {
				for (int index = 0; index < ships.size(); index++) {
					Ship ship = ships.get(index);
					if (object.isLeftCollision(ship))
						return true;
				}
				return false;
			}
			return false;
		}

		/**
		 * Method to check if an object is going to collide with water.
		 * 
		 * @param type
		 *            What type of collision; top, right, bottom, or left.
		 * @param object
		 *            Object that is being checked if it will collide with
		 *            water.
		 * @return True if there is a collision, false otherwise.
		 */
		private boolean checkWaterCollision(int type, Sprite object) {

			if (type == TOP_COLLISION) {
				for (int index = 0; index < water.size(); index++) {
					Water waterBlock = water.get(index);
					if (object.isTopCollision(waterBlock))
						return true;
				}
				return false;
			} else if (type == RIGHT_COLLISION) {
				for (int index = 0; index < water.size(); index++) {
					Water waterBlock = water.get(index);
					if (object.isRightCollision(waterBlock))
						return true;
				}
				return false;
			} else if (type == BOTTOM_COLLISION) {
				for (int index = 0; index < water.size(); index++) {
					Water waterBlock = water.get(index);
					if (object.isBottomCollision(waterBlock))
						return true;
				}
				return false;
			} else if (type == LEFT_COLLISION) {
				for (int index = 0; index < water.size(); index++) {
					Water waterBlock = water.get(index);
					if (object.isLeftCollision(waterBlock))
						return true;
				}
				return false;
			}
			return false;
		}

		/**
		 * Method to check if a chest will collide with an object.
		 * 
		 * @param type
		 *            What type of collision; top, right, bottom, or left.
		 * @return True if there is a collision, false otherwise.
		 */
		private boolean checkChestCollision(int type) {

			if (type == TOP_COLLISION) {
				for (int index = 0; index < chests.size(); index++) {
					Treasure chest = chests.get(index);
					if (player.isTopCollision(chest)) {

						if (checkTreeCollision(TOP_COLLISION, chest))
							return true;
						if (checkShipCollision(TOP_COLLISION, chest))
							return true;
						if (checkWaterCollision(TOP_COLLISION, chest))
							return true;

						for (int chestIndex = 0; chestIndex < chests.size(); chestIndex++) {
							Treasure otherChest = chests.get(chestIndex);
							if (!chest.equals(otherChest)) {
								if (chest.isTopCollision(otherChest))
									return true;
							}
						}
						SoundEffect.PUSH.play();
						chest.move(0, -SPRITE_WIDTH);
						checkEndState();
					}
				}

				return false;
			} else if (type == RIGHT_COLLISION) {
				for (int index = 0; index < chests.size(); index++) {
					Treasure chest = chests.get(index);
					if (player.isRightCollision(chest)) {

						if (checkTreeCollision(RIGHT_COLLISION, chest))
							return true;
						if (checkShipCollision(RIGHT_COLLISION, chest))
							return true;
						if (checkWaterCollision(RIGHT_COLLISION, chest))
							return true;

						for (int chestIndex = 0; chestIndex < chests.size(); chestIndex++) {
							Treasure otherChest = chests.get(chestIndex);
							if (!chest.equals(otherChest)) {
								if (chest.isRightCollision(otherChest))
									return true;
							}
						}
						SoundEffect.PUSH.play();
						chest.move(SPRITE_WIDTH, 0);
						checkEndState();
					}
				}
				return false;
			} else if (type == BOTTOM_COLLISION) {
				for (int index = 0; index < chests.size(); index++) {
					Treasure chest = chests.get(index);
					if (player.isBottomCollision(chest)) {

						if (checkTreeCollision(BOTTOM_COLLISION, chest))
							return true;
						if (checkShipCollision(BOTTOM_COLLISION, chest))
							return true;
						if (checkWaterCollision(BOTTOM_COLLISION, chest))
							return true;

						for (int chestIndex = 0; chestIndex < chests.size(); chestIndex++) {
							Treasure otherChest = chests.get(chestIndex);
							if (!chest.equals(otherChest)) {
								if (chest.isBottomCollision(otherChest))
									return true;
							}
						}
						SoundEffect.PUSH.play();
						chest.move(0, SPRITE_WIDTH);
						checkEndState();
					}
				}

				return false;
			} else if (type == LEFT_COLLISION) {
				for (int index = 0; index < chests.size(); index++) {
					Treasure chest = chests.get(index);
					if (player.isLeftCollision(chest)) {

						if (checkTreeCollision(LEFT_COLLISION, chest))
							return true;
						if (checkShipCollision(LEFT_COLLISION, chest))
							return true;
						if (checkWaterCollision(LEFT_COLLISION, chest))
							return true;

						for (int chestIndex = 0; chestIndex < chests.size(); chestIndex++) {
							Treasure otherChest = chests.get(chestIndex);
							if (!chest.equals(otherChest)) {
								if (chest.isLeftCollision(otherChest))
									return true;
							}
						}
						SoundEffect.PUSH.play();
						chest.move(-SPRITE_WIDTH, 0);
						checkEndState();
					}
				}
				return false;
			}
			return false;
		}

		/**
		 * Check if the level is completed. Called once a chest is moved, as a
		 * level will be completed after a chest has moved into the goal.
		 */
		public void checkEndState() {

			int completed = 0;

			for (int chestIndex = 0; chestIndex < chests.size(); chestIndex++) {
				Treasure chest = (Treasure) chests.get(chestIndex);
				for (int goalIndex = 0; goalIndex < goals.size(); goalIndex++) {
					Goal goal = goals.get(goalIndex);
					if (chest.getX() == goal.getX()
							&& chest.getY() == goal.getY())
						completed++;
				}
			}

			currentScore = completed;

			if (completed == goals.size()) {
				finished = true;
				repaint();
			}
		}

		/**
		 * Method to restart the level.
		 */
		public void restartLevel() {

			goals.clear();
			chests.clear();
			trees.clear();
			initWorld(levelOne);
			if (finished) {
				finished = false;
			}
		}
	}

	/*
	 * private class CountDownTimer implements ActionListener{
	 * 
	 * public void actionPerformed(ActionEvent ae){ timeRemaining -= 1000;
	 * 
	 * if(timeRemaining == 0){ outOfTime = true; } repaint(); } }
	 */
	/*
	 * private class CDT extends TimerTask{
	 * 
	 * public void run(){ timeRemaining -= 1000;
	 * 
	 * if(timeRemaining == 0){ outOfTime = true; } repaint(); } }
	 */
	private class CDT implements ActionListener {

		public void actionPerformed(ActionEvent ae) {

			timeRemaining -= 1000;
			if(timeRemaining == 0)
				outOfTime = true;
			repaint();
		}
	}
}
