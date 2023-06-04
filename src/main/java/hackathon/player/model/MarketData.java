package hackathon.player.model;

import hackathon.player.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class MarketData {

    private final String symbol;

    private final long LTP;

    private final long LTQ;

    private final long totalTradedVolume;

    private final long bestBid;

    private final long bestAsk;

    private final long bestBidQty;

    private final long bestAskQty;

    private final long openInterest;

    private final Date timestamp;

    private static final AtomicLong _sequence = new AtomicLong(0);

    private final long sequence;
    private final long prevClosePrice;
    private final long prevOpenInterest;


    public MarketData(String symbol, long ltp, long ltq, long totalTradedVolume, long bestBid, long bestAsk, long bestBidQty, long bestAskQty, long openInterest, Date timestamp, long prevClosePrice, long prevOpenInterest) {
        this.symbol = symbol;
        this.LTP = ltp;
        this.LTQ = ltq;
        this.totalTradedVolume = totalTradedVolume;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.bestBidQty = bestBidQty;
        this.bestAskQty = bestAskQty;
        this.openInterest = openInterest;
        this.timestamp = timestamp;
        this.prevClosePrice = prevClosePrice;
        this.prevOpenInterest = prevOpenInterest;
        sequence = _sequence.incrementAndGet();
    }

    public MarketData(String symbol, long ltp, long ltq, long totalTradedVolume, long bestBid, long bestAsk, long bestBidQty, long bestAskQty, long openInterest, Date timestamp, long prevClosePrice, long prevOpenInterest, long sequence) {
        this.symbol = symbol;
        this.LTP = ltp;
        this.LTQ = ltq;
        this.totalTradedVolume = totalTradedVolume;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.bestBidQty = bestBidQty;
        this.bestAskQty = bestAskQty;
        this.openInterest = openInterest;
        this.timestamp = timestamp;
        this.prevClosePrice = prevClosePrice;
        this.prevOpenInterest = prevOpenInterest;
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "symbol='" + symbol + '\'' +
                ", LTP=" + LTP +
                ", LTQ=" + LTQ +
                ", totalTradedVolume=" + totalTradedVolume +
                ", bestBid=" + bestBid +
                ", bestAsk=" + bestAsk +
                ", bestBidQty=" + bestBidQty +
                ", bestAskQty=" + bestAskQty +
                ", openInterest=" + openInterest +
                ", timestamp=" + timestamp +
                ", sequence=" + sequence +
                ", prevClosePrice=" + prevClosePrice +
                ", prevOpenInterest=" + prevOpenInterest +
                '}';
    }

    /**
     * PacketLength - 4
     * TradingSymbol - 30
     * Sequence Number - 8
     * Timestamp - 8 Epoch
     * LTP = 8;
     * LTQ = 8
     * Volume = 8
     * BidPrice = 8
     * bidQty = 8;
     * AskPrice = 8
     * AskQty = 8;
     * OI = 8;
     * PrevClose 8;
     * Prev OI = Open 8;
     * @return the asked byte array
     */
    public byte[] toBytes() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(130).order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.putInt(124);//The remaining byte size
        byteBuffer.put(ByteUtils.charsToSinglePaddedByteArray(symbol.toCharArray(), 30));
        byteBuffer.putLong(sequence);
        byteBuffer.putLong(new Date().getTime());
        byteBuffer.putLong(LTP);
        byteBuffer.putLong(LTQ);
        byteBuffer.putLong(totalTradedVolume);
        byteBuffer.putLong(bestBid);
        byteBuffer.putLong(bestBidQty);
        byteBuffer.putLong(bestAsk);
        byteBuffer.putLong(bestAskQty);
        byteBuffer.putLong(openInterest);
        byteBuffer.putLong(prevClosePrice);
        byteBuffer.putLong(prevOpenInterest);
        return byteBuffer.array();
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
