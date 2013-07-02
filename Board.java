

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

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
	 * Arraylist containing the chests of the game.
	 */
	private ArrayList<Treasure> chests = new ArrayList<Treasure>();
	/**
	 * Arraylist containing the goal zones of the game.
	 */
	private ArrayList<Goal> goals = new ArrayList<Goal>();

	/**
	 * Constructor for the board.
	 */
	public Board() {
		addKeyListener(new MyKeyAdapter());
		setFocusable(true);
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
		Treasure chest;
		Goal goal;

		for (int pos = 0; pos < level.length(); pos++) {

			char item = level.charAt(pos);

			if (item == '\n') {
				y += SPRITE_WIDTH;
				x = OFFSET;
			} else if (item == '#') {
				tree = new Tree(x, y);
				trees.add(tree);
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

			if (finished) {
				SoundEffect.SONG.stoploop();
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
				g2d.drawString("Game Over", OFFSET*3, BOARD_HEIGHT - 100);

				g2d.setColor(Color.GRAY);
				g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
				g2d.drawString("Game made by Bowen Hui and Rodney Earl", OFFSET,
						BOARD_HEIGHT - 70);
				g2d.drawString("Music from Kevin MacLeod", OFFSET, BOARD_HEIGHT - 40);
				g2d.drawString("Sound effects from MediaCollege.com", OFFSET, BOARD_HEIGHT - 10);
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

				if (checkChestCollision(TOP_COLLISION)) {
					return;
				}

				player.move(0, -SPRITE_WIDTH);
			} else if (key == KeyEvent.VK_RIGHT) {
				if (checkTreeCollision(RIGHT_COLLISION, player)) {
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

				if (checkChestCollision(BOTTOM_COLLISION)) {
					return;
				}

				player.move(0, SPRITE_WIDTH);
			} else if (key == KeyEvent.VK_LEFT) {
				if (checkTreeCollision(LEFT_COLLISION, player)) {
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
		 *            Object that is being checked if it will collide witha
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
}
