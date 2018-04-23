package client.model;

public class Status extends Data {
	public enum Actions{NA, SINGLE, CONNECT, HOST, START, JOIN, PRESS};
	public enum States {ATMENU, CONNECTING, ATLOBBY, ATSETTINGS, WAITING, PLAYING, GAMEOVER};
	public enum ControllerStates {NA, TOMENU, TOCLIENT, TOGAME};
	public enum Messages {NA, NOTCONNECTED, CONNECTING};
	
	public static Actions action = Actions.NA;
	public static States state = States.ATMENU;
	public static ControllerStates conrState = ControllerStates.TOMENU;
}
