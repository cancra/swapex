package ceexchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class MinMaxPrice extends AbstractMessageTransformer {
	JSONObject sortedarray = new JSONObject();
	String BuySell;
	String min = "min";
	String max = "max";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		try {
			String jsonStr = message.getPayloadAsString();
			BuySell = message.getInvocationProperty("BuySell");
			JSONArray jsonArr = new JSONArray(jsonStr);
			JSONArray sortedJsonArray = new JSONArray();
			List<JSONObject> jsonValues = new ArrayList<JSONObject>();
			for (int i = 0; i < jsonArr.length(); i++) {
				jsonValues.add(jsonArr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				// Jason array is sorted on the basis of Quoted price
				// lowest quoted price will be picked up first
				private static final String KEY_NAME = "Price";

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = new String();
					String valB = new String();

					try {
						valA = (String) a.get(KEY_NAME).toString();
						valB = (String) b.get(KEY_NAME).toString();

					} catch (JSONException e) {

					}

					return (valA.compareTo(valB));

				}
			});

			for (int i = 0; i < jsonArr.length(); i++) {
				sortedJsonArray.put(jsonValues.get(i));
			}

			if (BuySell.equals(min)) {

				sortedarray = (JSONObject) sortedJsonArray.get(0);

			} else if (BuySell.equals(max)) {
				sortedarray = (JSONObject) sortedJsonArray.get(sortedJsonArray.length() - 1);
			}

		} catch (Exception e1) {

			e1.printStackTrace();
		}

		return sortedarray;

	}

}