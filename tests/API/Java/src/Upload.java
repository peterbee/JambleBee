import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface Upload {

	public abstract HttpResponse executeRequest()
			throws ClientProtocolException, IOException;

}