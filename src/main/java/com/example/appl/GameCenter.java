package com.example.appl;

import java.util.Objects;
import java.util.logging.Logger;

import spark.Session;

import com.example.model.GuessGame;

/**
 * The object to coordinate the state of the Web Application.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GameCenter {
	private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

	//
	// Constants
	//

	final static String NO_GAMES_MESSAGE = "No game stats yet.";
	final static String NO_GAMES_WON_MESSAGE = "You have not won a game, yet. But I *feel* your luck changing";
	final static String GLOBAL_AVERAGE_NUMBER_OF_GAMES_WON_FORMAT = "You have won an average of %.1f of this session's %d games";
	final static String GLOBAL_AVERAGE_NUMBER_OF_GAMES_WON_FORMAT_USERS = "%.1f";

	/**
	 * The user session attribute name that points to a game object.
	 */
	public final static String GAME_ID = "game";

	//
	// Attributes
	//

	private int totalGames = 0;
	private int numberOfWins;
	private double globalAverageOfGamesWon;


	//
	// Public methods
	//

	/**
	 * Get the {@linkplain GuessGame game} for the current user (identified by a
	 * browser session).
	 *
	 * @param session
	 *            The HTTP session
	 *
	 * @return A existing or new {@link GuessGame}
	 */
	public GuessGame get(final Session session) {
		// validation
		Objects.requireNonNull(session, "session must not be null");
		//
		GuessGame game = session.attribute(GAME_ID);
		if (game == null) {
			// create new game
			game = new GuessGame();
			session.attribute(GAME_ID, game);
			LOG.fine("New game created: " + game);
		}
		return game;
	}

	/**
	 * End the user's current {@linkplain GuessGame game} and remove it from the
	 * session.
	 *
	 * @param session
	 *            The HTTP session
	 */
	public void end(Session session) {
		// validation
		Objects.requireNonNull(session, "session must not be null");
		// remove the game from the user's session
		session.removeAttribute(GAME_ID);
		// do some application-wide book-keeping
		synchronized (this) { // protect the critical code
			totalGames++;
			updateGlobalWinAverage();
		}
	}

	/**
	 * Get a user message about the statistics for the whole site.
	 *
	 * @return The message to the user about global game statistics.
	 */
	public synchronized String getGameStatsMessage() {
		if (totalGames == 0) {
			return NO_GAMES_MESSAGE;
		}
		else if (getGlobalAverageGamesWon() > 0) {
			return String.format(GLOBAL_AVERAGE_NUMBER_OF_GAMES_WON_FORMAT, getGlobalAverageGamesWon(), totalGames);
		}
		else {
			return NO_GAMES_WON_MESSAGE;
		}
	}

	public String getAverageGamesWonMessage() {
		return String.format(GLOBAL_AVERAGE_NUMBER_OF_GAMES_WON_FORMAT_USERS, getGlobalAverageGamesWon());
	}

	public int getTotalGamesCount() {
		return totalGames;
	}

	public void incrementNumberOfWins() {
		numberOfWins++;
	}
	public int getNumberOfWins() {
		return numberOfWins;
	}

	public void updateGlobalWinAverage() {
		if (numberOfWins > 0) {
			globalAverageOfGamesWon = (double) numberOfWins / (double) totalGames;
		}
	}
	public double getGlobalAverageGamesWon() {
		return globalAverageOfGamesWon;
	}
}
