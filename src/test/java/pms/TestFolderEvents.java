package pms;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class TestFolderEvents {

	public static void main(String[] args) throws IOException, InterruptedException {

		Path faxFolder = FileSystems.getDefault().getPath("Path to root folder to monitor");
		WatchService watchService = faxFolder.getFileSystem().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				 if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
	                    System.out.println("Created: " + event.context().toString());
	                }
	                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
	                    System.out.println("Deleted: " + event.context().toString());
	                }
	                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
	                    System.out.println("Modified: " + event.context().toString());
	                }
			}
			valid = watchKey.reset();
		} while (valid);
	}
}
