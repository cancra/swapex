package ceexchange;

import java.util.ArrayList;

import java.util.List;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.jayway.jsonpath.JsonPath;

public class ListofCoins extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
List<String> listofcoins = new ArrayList<String>();
		
		
		try {			
			listofcoins =	JsonPath.read(message.getPayloadAsString(), "$..name");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			return listofcoins ;

	}
	
}
