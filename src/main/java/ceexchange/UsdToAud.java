package ceexchange;

import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.jayway.jsonpath.JsonPath;

public class UsdToAud extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<String> conversion1 = new ArrayList<String>();
		List<String> conversion = new ArrayList<String>();

		try {
			conversion1 = JsonPath.read(message.getPayloadAsString(),
					"$[?(@.currency_code=~ /.*aud/i)]");
			
			conversion = JsonPath.read(conversion1,
							"$..rate");
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conversion;
	}

}
