package lindar.fonix.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j @NoArgsConstructor
public class FonixDateParser {
    final DateTimeFormatter parseStatusTime = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public Date getParsedDate(String timeDescriptor, String dateTimeString, String gid) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, parseStatusTime);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            log.error("unable to parse {} from string {}, gid: {}", timeDescriptor, dateTimeString, gid, e);
            return null;
        }
    }
}