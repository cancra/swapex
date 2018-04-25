package ceexchange;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.jayway.jsonpath.JsonPath;

public class ChartFilter extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<String> open = new ArrayList<String>();
		List<String> close = new ArrayList<String>();
		List<String> chart = new ArrayList<String>();
		List<Long> timestamp = new ArrayList<Long>();
		int length;

		try {
			length = JsonPath.read(message.getPayloadAsString(), "$.length()");

			open = JsonPath.read(message.getPayloadAsString(), "$..open");
			close = JsonPath.read(message.getPayloadAsString(), "$..close");
			timestamp = JsonPath.read(message.getPayloadAsString(), "$..timestamp");

			for (int i = 0; i < length; i++) {

				List<String> temp = new ArrayList<String>();

				final long unixTime = timestamp.get(i);

				final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				final String formatted = Instant.ofEpochSecond(unixTime / 1000).atZone(ZoneId.of("GMT+10"))
						.format(formatter);
				temp.add(formatted);
				temp.add(open.get(i));
				temp.add(open.get(i));
				temp.add(close.get(i));
				temp.add(close.get(i));

				chart.add(temp.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return chart;

	}

}
