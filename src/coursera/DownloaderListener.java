package coursera;

import java.net.URL;
import java.nio.file.Path;

public interface DownloaderListener {
	public void beforeDownloading();
	public void downloading(int sizeDownloaded);
	public void afterDownloading();//will not call afterDownloading if stoppedDownloading is called
	public void stoppedDownloading();
}
