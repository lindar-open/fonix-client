package lindar.fonix.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

public class FonixDateUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FonixDateUtil fonixDateUtil = new FonixDateUtil();

    @Test
    public void getParsedDate_validDate_statusTimeResponseCorrect() {
        // when
        Date response = fonixDateUtil.getParsedDate("20230306101252", "gid");

        // then
        assertThat(response, is(new Date(1678093972000L)));
    }

    @Test
    public void getParsedDate_validDateNearMidnight_statusTimeResponseCorrect() {
        // when
        Date response = fonixDateUtil.getParsedDate("20230306235959", "gid");

        // then
        assertThat(response, is(new Date(1678143599000L)));
    }

    @Test
    public void getParsedDate_validDateAtDaylightSavingsSummerTime_statusTimeResponseCorrect() {
        // when
        Date response = fonixDateUtil.getParsedDate("20230326020000", "gid");

        // then
        assertThat(response, is(new Date(1679792400000L)));
    }

    @Test
    public void getParsedDate_validDateAtDaylightSavingsWinterTime_statusTimeResponseCorrect() {
        // when
        Date response = fonixDateUtil.getParsedDate("20231029030000", "gid");

        // then
        assertThat(response, is(new Date(1698544800000L)));
    }

    @Test
    public void getParsedDate_invalidDate_statusTimeResponseNull() {
        // when
        Date response = fonixDateUtil.getParsedDate("2023032602", "gid");

        // then
        assertNull(response);
    }
}
