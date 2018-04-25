package ceexchange;

import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.module.http.internal.ParameterMap;
import org.mule.transformer.AbstractMessageTransformer;

public class ThirdPartyExchangeURIParam extends AbstractMessageTransformer{

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		
		 
		 Map<String, String> queryParams = message.getInboundProperty("http.query.params");
		 String id=queryParams.get("id");
		 String coin;
		 if (id.toString()== "ethereum")
		 {
			// ParameterMap map = message.getInboundProperty("http.query.params");
			 ParameterMap map = new ParameterMap();
			 map.put("email", "rohit061989@gmail.com");
			 message.setProperty("http.query.params", map,  PropertyScope.INBOUND);
		/*	 ParameterMap map = new ParameterMap();
			 map.put("email", "rohit061989@gmail.com");

			 message.setProperty("http.uri.params", map, PropertyScope.INBOUND);
			*/ 
	
		 }
		
		return message;
	}

}
