package ceexchange;

import java.util.List;
import java.util.ArrayList;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class CandleChartFilter extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<String> chart = new ArrayList<String>();

		try {

			chart.add(message.getPayloadAsString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chart;

	}

}
