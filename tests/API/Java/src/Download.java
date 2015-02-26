import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface Download {

	public abstract HttpResponse executeRequest()
			throws ClientProtocolException, IOException;
	/**
	 * Saves the file to the local file system
	 * @param response the response from {@link #executeRequest()}
	 * @param saveLocation the absolute path to the directory in which to to save the file
	 * @return the absolute filename of the saved file
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public abstract String saveFile(HttpResponse response, String saveLocation)
			throws IllegalStateException, IOException;

}