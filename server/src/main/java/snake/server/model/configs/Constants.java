package snake.server.model.configs;

import java.util.Random;

public class Constants {
	public static int sizeN = 20, sizeM = 20;
	public static boolean noBorder = true;
	public static int turnTimeMS = 1000;
	public static int decreaseTimeMS = 2;
	
	public static long hostCheckInterval = 2000;
	
	public static String[] funnyNames = new String[]{ 
		"Alf",
		"Jim Bob",
		"Anheuser",
		"Jolly Roger",
		"Belch",
		"Leonidas",
		"Big Mac",
		"ManBearPig",
		"Bob Zombie",
		"Master Chief",
		"Boomhauer",
		"Mr. Clean",
		"Braveheart",
		"Mustache",
		"Brundon",
		"O'Doyle",
		"Captain Crunch",
		"Pablo",
		"Chewbacca",
		"Popeye",
		"Chubs",
		"Pork Chop",
		"Chum",
		"Rufio",
		"Derp",
		"Rumplestiltskin",
		"Django",
		"Snoopy",
		"Fight Club",
		"Spiderpig",
		"Flanders",
		"Spongebob",
		"Focker",
		"Spud",
		"Frodo",
		"Taco",
		"Frogger",
		"Turd Ferguson",
		"Gooch",
		"Uh-Huh",
		"Goonie",
		"Vader",
		"Gump",
		"Weiner",
		"Homer",
		"Wizzer",
		"Huggies",
		"Wonka",
		"The Hulk",
		"Wreck-it Ralph",
		"Jedi" };
	
	public static String getRandomName(){
		Random rand = new Random();
		return funnyNames[rand.nextInt(funnyNames.length)];
	}

}
