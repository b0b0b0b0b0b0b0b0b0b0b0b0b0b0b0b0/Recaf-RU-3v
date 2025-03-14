package me.coley.recaf.ui.dnd;

import javafx.scene.input.DragEvent;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Simple file drop listener.
 *
 * @author Matt Coley
 */
public interface FileDropListener {
	/**
	 * Called when the drop is complete.
	 *
	 * @param region
	 * 		Region dropped on.
	 * @param event
	 * 		Drop event.
	 * @param files
	 * 		Files dropped.
	 *
	 * @throws IOException
	 * 		When handling of the files fails.
	 */
	void onDragDrop(Region region, DragEvent event, List<Path> files) throws IOException;

	/**
	 * Called when drag moves over the control.
	 *
	 * @param region
	 * 		Region moused over.
	 * @param event
	 * 		Drag event.
	 */
	default void onDragEnter(Region region, DragEvent event) {
		// no-op
	}

	/**
	 * Called when drag moves outside the control.
	 *
	 * @param region
	 * 		Region left.
	 * @param event
	 * 		Drag event.
	 */
	default void onDragExit(Region region, DragEvent event) {
		// no-op
	}
}
