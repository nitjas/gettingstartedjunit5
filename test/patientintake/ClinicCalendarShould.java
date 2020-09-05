package patientintake;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ClinicCalendarShould {

   private ClinicCalendar calendar;

   @BeforeAll
   static void intiAll() {
      System.out.println("Before all..");
   }

   // Usually you just need only this
   @BeforeEach
   void init() {
      System.out.println("Before each..");
      calendar = new ClinicCalendar(LocalDate.of(2018, 8, 26));
//      calendar = new ClinicCalendar(LocalDate.now()); // will fail, so above is better
   }

   @Test
   void allowEntryOfAnAppointment() {
      calendar.addAppointment("Jim", "Weaver", "avery",
         "09/01/2018 2:00 pm");
      List<PatientAppointment> appointments = calendar.getAppointments();
      assertNotNull(appointments);
      assertEquals(1, appointments.size());
      PatientAppointment enteredAppt = appointments.get(0);

      // Assertions short circuit- so you won't see the second failure along with the first. Fix first then run into
      // next. Fix that and so on.
      // It would have saved us some time if we saw all the mismatches on the appointment data at one go
      // Useful if several assertions are really checking just the same thing
      // 4 assertions but really just 1 assumption. Created appointment matches one entered by user
      // A series of assertions in one test method use assertAll- prevents short circuiting of any assertions it wraps
      // each of the parameters needs to be a lambda expression

      /*
      assertEquals("Jim", enteredAppt.getPatientFirstName());
      assertEquals("Weaver", enteredAppt.getPatientLastName());
      assertEquals(Doctor.avery, enteredAppt.getDoctor());
      assertEquals("9/1/2018 02:00 PM",
              enteredAppt.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy hh:mm a", Locale.US)));
      */

      assertAll(
         () -> assertEquals("Jim", enteredAppt.getPatientFirstName()),
         () -> assertEquals("Weaver", enteredAppt.getPatientLastName()),
         () -> assertSame(Doctor.avery, enteredAppt.getDoctor()), // will verify that the 2 objects being compared
              // point to literally the same object in memory- enum instances are unique so these two shouldn't
              // be just equivalent they should be EXACTLY the same object in memory
              // use assertNotSame to verify that an object that's supposed to have been copied to another object
              // isn't the same instance as the original it was copied from
         () -> assertEquals("9/1/2018 02:00 PM",
            enteredAppt.getAppointmentDateTime().format(DateTimeFormatter.ofPattern("M/d/yyyy hh:mm a", Locale.US)))
      );
   }

   @Test
   void returnTrueForHasAppointmentsIfThereAreAppointments() {
      calendar.addAppointment("Jim", "Weaver", "avery",
         "09/01/2018 2:00 pm");
      assertTrue(calendar.hasAppointment(LocalDate.of(2018, 9, 1)));
   }

   @Test
   void returnFalseForHasAppointmentsIfThereAreNoAppointments() {
      assertFalse(calendar.hasAppointment(LocalDate.of(2018, 9, 1)));
   }

   @Test
//   @Disabled // lets you skip these tests, but still keep their presence visible in the report
   void returnCurrentDaysAppointments() {
      calendar.addAppointment("Jim", "Weaver", "avery",
         "08/26/2018 2:00 pm");
      calendar.addAppointment("Jim", "Weaver", "avery",
         "08/26/2018 3:00 pm");
      calendar.addAppointment("Jim", "Weaver", "avery",
         "09/01/2018 2:00 pm");
      assertEquals(2, calendar.getTodayAppointments().size());
//      assertEquals(calendar.getTodayAppointments(), calendar.getAppointments()); // compares object instance references
      // for collections jUnit will check the two collections contain equivalent objects using equals() and that they are
      // in the same order

      // Collection specific assertion
//      assertIterableEquals(calendar.getTodayAppointments(), calendar.getAppointments());
      // An iterable in java is something that returns a series of objects for use in a for loop
      // If you are trying to compare contents of different collection types, if you can get an iterable from both of
      // them, you can use Junit5s assertIterable to compare them
   }

   @Test
   void testStringSplitLimit() {
      String[] arr = {"b", "", ":and:f", "", "" };
      assertIterableEquals(Arrays.asList(arr), Arrays.asList("boo:and:foo".split("o", 5)));
   }

   // to clean up test data, even if an unexpected exception occurs during a test
   @AfterEach
   void tearDown() {
      System.out.println("After each..");
   }

   @AfterAll
   static void tearDownAll() {
      System.out.println("After all..");
   }
}