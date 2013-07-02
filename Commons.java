
/**
 * Interface to hold common constants.
 * 
 * @author Rodney Earl
 * @version 1.0
 */
public interface Commons {

	/**
	 * Width of the game frame.
	 */
	public static final int BOARD_WIDTH = 1000;
	/**
	 * Height of the game frame.
	 */
	public static final int BOARD_HEIGHT = 800;
	/**
	 * Width/height of a sprite.  All sprites are square.
	 */
	public static final int SPRITE_WIDTH = 35;

	public static final int OFFSET = 30;
	/**
	 * Number for top collisions.
	 */
	public static final int TOP_COLLISION = 1;
	/**
	 * Number for right collisions.
	 */
	public static final int RIGHT_COLLISION = 2;
	/**
	 * Number for bottom collisions.
	 */
	public static final int BOTTOM_COLLISION = 3;
	/**
	 * Number for left collisions.
	 */
	public static final int LEFT_COLLISION = 4;
	/**
	 * String containing a basic level layout.
	 */
	public static final String levelZero = 
            "    ######\n"
          + "    ##   #\n"
          + "    ##$  #\n"
          + "  ####  $##\n"
          + "  ##  $ $ #\n"
          + "#### # ## #   ######\n"
          + "##   # ## #####  ..#\n"
          + "## $  $          ..#\n"
          + "###### ### #@##  ..#\n"
          + "    ##     #########\n"
          + "    ########\n";
	/**
	 * String containing a more complex level.
	 */
	public static final String levelOne =
			 "     #####            \n"
			+"     #...#            \n"
			+"     #   #            \n"
			+"     #   #            \n"
			+"     ## ##            \n"
			+"      # #             \n"
			+"####### #####         \n"
			+"#   #       #         \n"
			+"#   #      $##        \n"
			+"# $   ###  $ #        \n"
			+"#     ### ## #   #####\n"
			+"#         ## #####  .#\n"
			+"#  ######$        @ .#\n"
			+"#    $    # ### ##  .#\n"
			+"#       ### ### ######\n"
			+"##      #   ### #     \n"
			+" #      #   ### #     \n"
			+" ###         $  #     \n"
			+"   #        #####     \n"
			+"   ##########         \n";
}
