package patientintake;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("dateTime") // I could scatter tags across multiple test classes & the test runner will only target those
// tests that match the tag expression
@DisplayName("DateTimeConverter should")
class DateTimeConverterShould {

   @Nested
   @DisplayName("convert string with 'today' keyword")
   class TodayTests {
      @Test
      @DisplayName("correctly")
      void convertTodayStringCorrectly() {
         LocalDate today = LocalDate.of(2018, 9, 1);
         LocalDateTime result = DateTimeConverter.convertStringToDateTime("today 1:00 pm",
            today);
         assertEquals(result, LocalDateTime.of(2018, 9, 1, 13, 0),
            // see what date was passed for the today parameter on failure
                 // useful for test failures that are difficult to understand
                 // if you have many such test methods calculating the failure message string is taking
                 // some time and slowing the test down- as it stands this string will be computed
                 // even if tests pass. We can use a slightly different syntax- using a lambda expression
                 // for the 3rd parameter (anonymous function basically) to provide the failure message
                 // instead of just a fixed string - same results on a failure but the lambda expression will
                 // only be evaluated if there IS a failure. Speed up test execution!

//            "Failed to convert 'today' string to expected date time, today passed was: " +today);
            () -> "Failed to convert 'today' string to expected date time, today passed was: " +today);
      }

      @Test
      @DisplayName("regardless of case")
      void convertTodayStringCorrectlyCaseInsensitive() {
         LocalDate today = LocalDate.of(2018, 9, 1);
         LocalDateTime result = DateTimeConverter.convertStringToDateTime("ToDay 1:00 pm",
            today);
         assertEquals(result, LocalDateTime.of(2018, 9, 1, 13, 0),
            () -> "Failed to convert 'today' string to expected date time, today passed was: " +today);
      }
   }

   @Test
   @DisplayName("convert expected date time pattern in string correctly")
   void convertCorrectPatternToDateTime() {
      LocalDateTime result = DateTimeConverter.convertStringToDateTime("9/2/2018 1:00 pm",
         LocalDate.of(2018, 9, 1));
      assertEquals(result, LocalDateTime.of(2018, 9, 2, 13, 0));
   }

   @Test
//   @Tag("dateTime")
   @DisplayName("throw exception if entered pattern of string incorrect")
   void throwExceptionIfIncorrectPatternProvided() {
      // Junit5 assertion that we can use to explicitly verify that we get an exception when certain code is called
      Throwable error = assertThrows(RuntimeException.class, () ->
         DateTimeConverter.convertStringToDateTime("9/2/2018 100 pm",
         LocalDate.of(2018, 9, 1)));
      // verify the text/message of the exception is what we expect it to be
      assertEquals("Unable to create date time from: [9/2/2018 100 pm], " +
         "please enter with format [M/d/yyyy h:mm a], Text '9/2/2018 100 PM' " +
         "could not be parsed at index 12", error.getMessage());
   }
}