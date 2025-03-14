package me.coley.recaf.ui.dnd;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import me.coley.recaf.util.logging.Logging;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Drag and drop utilities.
 *
 * @author Matt Coley
 */
public class DragAndDrop {
	private static final Logger logger = Logging.get(DragAndDrop.class);

	/**
	 * Install drag-drop support on the given region.
	 *
	 * @param region
	 * 		Control to support.
	 * @param listener
	 * 		Behavior when file content is dropped into the region.
	 */
	public static void installFileSupport(Region region, FileDropListener listener) {
		region.setOnDragOver(e -> onDragOver(region, e));
		region.setOnDragDropped(e -> onDragDropped(region, e, listener));
		region.setOnDragEntered(e -> onDragEntered(region, e, listener));
		region.setOnDragExited(e -> onDragExited(region, e, listener));
	}

	/**
	 * Handle drag-over.
	 *
	 * @param region
	 * 		Region dragged over.
	 * @param event
	 * 		Drag event.
	 */
	private static void onDragOver(Region region, DragEvent event) {
		if (event.getGestureSource() != region && event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			event.consume();
		}
	}

	/**
	 * Pass drag-drop to listener.
	 *
	 * @param region
	 * 		Region dropped completed on.
	 * @param event
	 * 		Drag event.
	 * @param listener
	 * 		Listener to call when drop completed.
	 */
	private static void onDragDropped(Region region, DragEvent event, FileDropListener listener) {
		Dragboard db = event.getDragboard();
		boolean success = true;
		if (db.hasFiles()) {
			try {
				List<Path> paths = db.getFiles().stream()
						.map(File::toPath)
						.collect(Collectors.toList());
				listener.onDragDrop(region, event, paths);
			} catch (IOException ex) {
				logger.error("Failed drag-and-drop due to IO", ex);
				success = false;
			} catch (Throwable ex) {
				logger.error("Failed drag-and-drop due to unhanded error", ex);
				success = false;
			}
			event.consume();
		}
		event.setDropCompleted(success);
	}

	/**
	 * Pass drag-entering to listener.
	 *
	 * @param region
	 * 		Region dragged over.
	 * @param event
	 * 		Drag event.
	 * @param listener
	 * 		Listener to call when drag enters.
	 */
	private static void onDragEntered(Region region, DragEvent event, FileDropListener listener) {
		listener.onDragEnter(region, event);
		event.consume();
	}

	/**
	 * Pass drag-leaving to listener.
	 *
	 * @param region
	 * 		Region dragged over.
	 * @param event
	 * 		Drag event.
	 * @param listener
	 * 		Listener to call when drag leaves.
	 */
	private static void onDragExited(Region region, DragEvent event, FileDropListener listener) {
		listener.onDragExit(region, event);
		event.consume();
	}
}
