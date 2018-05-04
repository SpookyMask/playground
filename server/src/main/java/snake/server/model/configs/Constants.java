package snake.server.model.configs;

import java.util.Random;

public class Constants {
	public static int sizeN = 20, sizeM = 20;
	public static boolean noBorder = true;
	public static int turnTimeMS = 1000;
	public static int decreaseTimeMS = 2;

	public static long hostCheckInterval = 2000;
	public static long gameStartDelay = 3000;
	
	public static String[] funnyNames = new String[]{ 
		"Alf",
		"JimBob",
		"Anheuser",
		"JollyRoger",
		"Belch",
		"Leonidas",
		"BigMac",
		"ManBearPig",
		"BobZombie",
		"MasterChief",
		"Boomhauer",
		"MrClean",
		"Braveheart",
		"Mustache",
		"Brundon",
		"ODoyle",
		"CaptainCrunch",
		"Pablo",
		"Chewbacca",
		"Popeye",
		"Chubs",
		"PorkChop",
		"Chum",
		"Rufio",
		"Derp",
		"Rumplestiltskin",
		"Django",
		"Snoopy",
		"FightClub",
		"Spiderpig",
		"Flanders",
		"Spongebob",
		"Focker",
		"Spud",
		"Frodo",
		"Taco",
		"Frogger",
		"TurdFerguson",
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
		"TheHulk",
		"WreckitRalph",
		"Jedi" };
	
	public static String getRandomName(){
		Random rand = new Random();
		return funnyNames[rand.nextInt(funnyNames.length)];
	}

}
