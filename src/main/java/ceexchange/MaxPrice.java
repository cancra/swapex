package ceexchange;

import java.util.*;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class MaxPrice extends AbstractMessageTransformer {

	List<Map<String, String>> ListofHash = new ArrayList<Map<String, String>>();
	Map<String, String> maxmap = new HashMap<String, String>();

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		// TODO Auto-generated method stub
		ListofHash = (List<Map<String, String>>) message.getPayload();
		Double max = 0D;
		for (int i = 0; i < ListofHash.size(); i++) {
			if (Double.parseDouble(ListofHash.get(i).get("Price")) < max) {
				max = -Double.parseDouble(ListofHash.get(i).get("Price"));
				maxmap = ListofHash.get(i);

			}

		}
		System.out.println(maxmap);
		return maxmap;

	}
}
