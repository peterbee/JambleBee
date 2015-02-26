import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
/**
 * Upload interface is used to upload a file to the server
 * @author guy
 *
 */
public interface Upload {
	/**
	 * Executes the request on the server
	 * @return the server response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public abstract HttpResponse executeRequest()
			throws ClientProtocolException, IOException;

}