package ceexchange;

import java.util.ArrayList;

import java.util.List;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.jayway.jsonpath.JsonPath;

public class Symbol extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		// TODO Auto-generated method stub

		List<String> filtered = new ArrayList<String>();

		try {
			filtered = JsonPath.read(message.getPayloadAsString(),
					"$[?(@.symbol=~ /.*" + message.getInvocationProperty("sym") + "/i)]");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return filtered;

	}

}
