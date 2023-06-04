package hackathon.player.client;

import hackathon.player.ByteUtils;
import hackathon.player.model.MarketData;

import java.nio.ByteBuffer;
import java.util.Date;

public class MarketDataParser {

    public MarketData parse(ByteBuffer byteBuffer) {

        int packetSize = byteBuffer.getInt(Constants.PACKET_SIZE);
        byte[] symbol = new byte[30];
        byteBuffer.get(Constants.SYMBOL, symbol, 0, 30);
        String symbolStr = ByteUtils.byteArrayToString(symbol);
        long sequence = byteBuffer.getLong(Constants.SEQUENCE);
        Date timestamp = new Date(byteBuffer.getLong(Constants.TIMESTAMP));
        long LTP = byteBuffer.getLong(Constants.LTP);
        long LTQ = byteBuffer.getLong(Constants.LTQ);
        long volume = byteBuffer.getLong(Constants.VOLUME);
        long bestBid = byteBuffer.getLong(Constants.BEST_BID);
        long bestBidQty = byteBuffer.getLong(Constants.BEST_BID_QTY);
        long bestAsk = byteBuffer.getLong(Constants.BEST_ASK);
        long bestAskQty = byteBuffer.getLong(Constants.BEST_ASK_QTY);
        long openInterest = byteBuffer.getLong(Constants.OPEN_INTEREST);
        long prevClosePrice = byteBuffer.getLong(Constants.PREV_CLOSE_PRICE);
        long prevOpenInterest = byteBuffer.getLong(Constants.PREV_OPEN_INTEREST);
        return new MarketData(symbolStr, LTP, LTQ, volume, bestBid, bestAsk, bestBidQty, bestAskQty, openInterest, timestamp, prevClosePrice, prevOpenInterest, sequence);
    }

    private interface Constants {
        int PACKET_SIZE = 0;
        int SYMBOL = PACKET_SIZE + 4;
        int SEQUENCE = SYMBOL + 30;
        int TIMESTAMP = SEQUENCE + 8;
        int LTP = TIMESTAMP + 8;
        int LTQ = LTP + 8;
        int VOLUME = LTQ + 8;
        int BEST_BID = VOLUME + 8;
        int BEST_ASK = BEST_BID + 8;
        int BEST_BID_QTY = BEST_ASK + 8;
        int BEST_ASK_QTY = BEST_BID_QTY + 8;
        int OPEN_INTEREST = BEST_ASK_QTY + 8;
        int PREV_CLOSE_PRICE = OPEN_INTEREST + 8;
        int PREV_OPEN_INTEREST = PREV_CLOSE_PRICE + 8;
    }
}
