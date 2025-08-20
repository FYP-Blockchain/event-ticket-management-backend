package com.ruhuna.event_ticket_management_system.utils;

import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class ConversionHelper {

    private static final Genson genson = new Genson();

    public static BigInteger stringUTC2Timestamp(String localDateTimeUTC) {
        BigInteger localDateTimestamp;
        try {
            Instant eventInstant = Instant.parse(localDateTimeUTC);
            long epochSecond = eventInstant.getEpochSecond();
            return BigInteger.valueOf(epochSecond);
        } catch (DateTimeParseException e) {
            log.error("Invalid UTC date format provided: {}", localDateTimeUTC);
            throw new IllegalArgumentException("Invalid eventDateUTC format. Please use ISO 8601 format, e.g., '2024-05-21T14:30:00Z'.");
        }
    }

    public static BigInteger convertEtherToWei(BigDecimal priceInEther) {
        return Convert.toWei(priceInEther, Convert.Unit.ETHER).toBigInteger();
    }

    public static LocalDateTime toLocalDateTime(BigInteger timestamp) {
        return Instant.ofEpochSecond(timestamp.longValue()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static  <T> T deserializeResponse(byte[] resultBytes, GenericType<T> type) {
        return genson.deserialize(new String(resultBytes, StandardCharsets.UTF_8), type);
    }
}
