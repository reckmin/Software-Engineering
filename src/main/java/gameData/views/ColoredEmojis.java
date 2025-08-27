package gameData.views;

public class ColoredEmojis {
	
    private final static String RESET = "\u001B[0m";
    private final static String GREEN = "\u001B[32m";
    private final static String BRIGHT_GREEN = "\u001B[32;1m";
    private final static String BLUE = "\u001B[34m";
    private final static String GREY = "\u001B[90m";
    private final static String RED = "\u001B[31;1m";
    private final static String YELLOW = "\u001B[33;1m";
    private final static String PURPLE = "\u001B[35;1m";
    private final static String CYAN = "\u001B[36;1m";
	
    public static final String GRASS =  GREEN + "🟩" + RESET;
    public static final String MOUNTAIN = GREY + "⛰️" + RESET; 
    public static final String WATER = BLUE + "🌊" + RESET;
    
    public static final String MY_PLAYER_POSITION = PURPLE + "🧙‍" + RESET;
    public static final String ENEMY_PLAYER_POSITION = CYAN + "🥷" + RESET;
    public static final String BOTH_PLAYER_POSITION = RED + "⚔️" + RESET;
    
    public static final String MY_CASTLE = YELLOW + "🏰" + RESET;
    public static final String ENEMY_CASTLE = RED + "🏭" + RESET;
    
    public static final String TREASURE_NOT_FOUND = RED + "🎁" + RESET;
    public static final String TREASURE_FOUND = YELLOW + "🎁" + RESET;
    
    
    public static final String UNKNOWN = RED + "❓" + RESET;
    
    public static final String WON = BRIGHT_GREEN + "Congratulations! You won! :) " + RESET;
    public static final String LOST = RED + "Oops, You lost :(" + RESET;
}
