package me.coley.recaf.ui.control.menu;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;
import me.coley.recaf.ui.control.BoundLabel;

/**
 * Menu with an on-click runnable action.
 *
 * @author Matt Coley
 */
public class ActionMenu extends Menu {
	/**
	 * @param text
	 * 		Menu display text translation key.
	 * @param graphic
	 * 		Menu graphic.
	 * @param action
	 * 		Action to run on-click.
	 */
	public ActionMenu(ObservableValue<String> text, Node graphic, Runnable action) {
		super();
		// This is a hack: https://stackoverflow.com/a/10317260
		// Works well enough without having to screw with CSS.
		HBox pane = new HBox();
		pane.setAlignment(Pos.CENTER);
		Label label = new BoundLabel(text);
		pane.setStyle("-fx-background-insets: -8 -21 -8 -21;" +
				"-fx-background-color: rgba(0, 0, 0, 0.001);");

		pane.getChildren().add(graphic);
		pane.getChildren().add(label);

		setGraphic(pane);
		pane.setOnMousePressed(e -> action.run());
	}
}
