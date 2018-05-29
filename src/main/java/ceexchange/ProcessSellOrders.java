/** 
+ * The ProcessSellOrders class implements a method that
* process the Buy orders against the available sell orders
* And return the status and details of the transaction.
*/


package ceexchange;

import java.util.*;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.jayway.jsonpath.JsonPath;

public class ProcessSellOrders extends AbstractMessageTransformer {
    final String CLASSNAME = "ProcessSellOrders";
                Logger logger = Logger.getLogger(ProcessSellOrders.class);
       //         logger.info("Entering : "+CLASSNAME);
                
                
                @SuppressWarnings("unchecked")
                @Override
                public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
                    final String METHODNAME = "transformMessage";

                                logger.info("Entering : "+CLASSNAME+"==== Method"+METHODNAME);

                                //Getting the sell orders details in the form of Jason String
                                String jsonStr = message.getInvocationProperty("SellCheckJson");

                                
                                //Jason String is converted into a sorted jason array
                                JSONArray jsonArr = new JSONArray(jsonStr);
                                
                                if(jsonArr.length()==0)
                                {
                                    message.setInvocationProperty("flagVariable", false);
                                    return message;


                                }
                                else {
                                JSONArray sortedJsonArray = new JSONArray();

                                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                                for (int i = 0; i < jsonArr.length(); i++) {
                                                jsonValues.add(jsonArr.getJSONObject(i));
                                }
                                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                                                // Jason array is sorted on the basis of Quoted price
                                                // lowest quoted price will be picked up first 
                                                private static final String KEY_NAME = "QUOTED_PRICE";

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
                                boolean orderSettled = false;
                                List<String> settleTransactionList = new LinkedList<String>();
                                Map partialTransactionMap = new HashMap();
                                Map completeTransactionMap = new HashMap();
                                
                                /***Getting the details of BUY Order****/
                                
                                Map<String, String> buyOrder = new HashMap<String, String>();

                                buyOrder = (Map) message.getPayload();

                                String quantityStr = (String) buyOrder.get("quantity");
                       //       String priceStr = (String) buyOrder.get("price"); 	Removed as price is not coming as original order request
                                String priceStr = message.getInvocationProperty("price");
                                double quantity = Double.valueOf(quantityStr);
                                double price = Double.valueOf(priceStr);
                                logger.info("OrderQuantity->" + quantity);
                                logger.info("Orderprice->" + price);

                                /*****************************************/
                                
                                double partialTransactionQunatity = 0;
                                String partialTransactionID = null;
                                double partialTransactionprice = 0;
                                String partialTransactionStatus = null;
                                String sellStatus=null;
                                double thridPartyQunatity=0;
                                Map<String,Integer> Netprofit = new HashMap<String, Integer>();
                               
                                
                                for (int i = 0; i < sortedJsonArray.length(); i++) {
                                                JSONObject jsonobject = sortedJsonArray.getJSONObject(i);
                                                double quotedprice = (jsonobject.getDouble("QUOTED_PRICE"));
                                                logger.info("quotedprice->" + i + "=====" + quotedprice);
                                                
                                                double sellQuantity = (jsonobject.getDouble("QUANTITY"));
                                                logger.info("sellQuantity->" + i + "=====" + sellQuantity);

                                                String transactionID = jsonobject.getString("SELLTRANSACTIONID");
                                                sellStatus = jsonobject.getString("SELLSTATUS");
                                                //logger.info("transactionID->" + i + "=====" + transactionID);
                                                

                                                
                                                if (quotedprice <= price) {
                                                                if (sellQuantity <= quantity) {
                                                                                sellStatus = "CLOSED";
                                                                                message.setInvocationProperty("sellStatus", sellStatus);
                                                                                settleTransactionList.add(transactionID);
                                                                                logger.info("Settled transactionID->" + i + "=====" + transactionID);
                                                                                
                                                                                double cal = price - quotedprice;
                                                                                double netpro = cal*sellQuantity;
                                                                                
                                                                                Netprofit.put(transactionID, (int) netpro);
                                                                                                                                                               
                                                                                message.setInvocationProperty("Netprofit", Netprofit);


                                                                } else {
                                                                             
                                                                                partialTransactionQunatity = sellQuantity - quantity;
                                                                                partialTransactionMap.put("partialTransactionQuantity", partialTransactionQunatity);
                                                                                System.out.println("Partial Transaction Qunatity---->"+partialTransactionQunatity);
                                                                                
                                                                                partialTransactionID = transactionID;
                                                                                partialTransactionMap.put("partialTransactionID", partialTransactionID);
                                                                                logger.info("Partially settled Transaction ID---->"+transactionID);
                                                                                
                                                                                partialTransactionprice = quotedprice;
                                                                                partialTransactionMap.put("partialTransactionprice", partialTransactionprice);
                                                                                logger.info("partialTransactionprice---->"+partialTransactionprice);
                                                                                
                                                                                partialTransactionStatus = "OPEN";
                                                                                partialTransactionMap.put("partialTransactionStatus", partialTransactionStatus);
                                                                                logger.info("partialTransactionStatus---->"+partialTransactionStatus);
                                                                                
                                                                                sellStatus = "Partial";
                                                                                
                                                                                double partialcal = price-partialTransactionprice;
                                                                                double partialNetPro = partialcal * quantity;
                                                                                
                                                                           
                                                                                                                                                               
                                                                                message.setInvocationProperty("PartialNetprofit", partialNetPro);
                                                                }
                                                                
                                                                quantity = quantity - sellQuantity;


                                                }
                                                if (quantity <= 0) {
                                                                orderSettled = true;
                                                                break;
                                                }
                                                else
                                                {
                                                                thridPartyQunatity=quantity;
                                                }

                                }
                                
                                

                                /*This flag will indicate that order is settled completely or not using SELL ORDERS*/
                                message.setInvocationProperty("flagVariable", orderSettled);

                                if(!orderSettled)
                                {
                                message.setInvocationProperty("thridPartyQunatity", thridPartyQunatity);
                                sellStatus="THIRDPARTY";
                                }
                                // MSG is a
                                                                                                                                                                                                                                                                                // returnValue
                 
                                /*Return the list of completely settled transaction*/
                                message.setInvocationProperty("OriginalMessage", settleTransactionList);
                                
                                
                                /*Return the details of partial transaction,if there is any**/
                                if(partialTransactionStatus!=null){
                                message.setInvocationProperty("PartialMap", partialTransactionMap);
                                }
                                
                                                
                                message.setInvocationProperty("sellStatus", sellStatus);

                                
                                return message;
                                }

                }

}