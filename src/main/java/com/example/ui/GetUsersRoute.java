package com.example.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.appl.GameCenter;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * The {@code GET /} route handler; aka the Home page. This is the page where
 * the user starts (no Game yet) but is also the landing page after a game ends.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetUsersRoute implements TemplateViewRoute {

	////
	//// Put all strings into constants so that you don't duplicate
	//// these strings in your unit tests.
	////

	static final String TITLE_ATTR = "title";
	static final String NEW_SESSION_ATTR = "newSession";
	static final String TITLE = "Global Wins Average for All Users";
	static final String VIEW_NAME = "users.ftl";
	static final String GLOBAL_AVERAGE_OF_WINS_ATTR = "globalWinAverage";

	//
	// Attributes
	//

	private final GameCenter gameCenter;

	//
	// Constructor
	//

	/**
	 * The constructor for the {@code POST /guess} route handler.
	 *
	 * @param gameCenter
	 *            The {@link GameCenter} for the application.
	 */
	GetUsersRoute(final GameCenter gameCenter) {
		// validation
		Objects.requireNonNull(gameCenter, "gameCenter must not be null");
		//
		this.gameCenter = gameCenter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModelAndView handle(Request request, Response response) {

		// start building the View-Model
		final Map<String, Object> vm = new HashMap<>();
		vm.put(TITLE_ATTR, TITLE);
		vm.put(GLOBAL_AVERAGE_OF_WINS_ATTR, gameCenter.getGlobalAverageGamesWon());
		// render the Game Form view
		vm.put(NEW_SESSION_ATTR, true);
		return new ModelAndView(vm, VIEW_NAME);
	}

}
