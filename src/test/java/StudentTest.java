import org.junit.*;
import static org.junit.Assert.*;
import org.junit.Rule;
import java.util.List;
import java.util.ArrayList;

public class StudentTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Student.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Student firstStudent = new Student("Sue Hill", "020215");
    Student secondStudent = new Student("Sue Hill", "020215");
    assertTrue(firstStudent.equals(secondStudent));
  }

  @Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    Student myStudent = new Student("Sue Hill", "020215");
    myStudent.save();
    assertTrue(Student.all().get(0).equals(myStudent));
  }

  @Test
  public void save_assignsIdToObject() {
    Student myStudent = new Student("Sue Hill", "020215");
    myStudent.save();
    Student savedStudent = Student.all().get(0);
    assertEquals(myStudent.getId(), savedStudent.getId());
  }

  @Test
  public void find_findsStudentInDatabase_true() {
    Student myStudent = new Student("Sue Hill", "020215");
    myStudent.save();
    Student savedStudent = Student.find(myStudent.getId());
    assertTrue(myStudent.equals(savedStudent));
  }

  @Test
  public void addCourse_addsCourseToStudent() {
    Course myCourse = new Course("biology");
    myCourse.save();
    Student myStudent = new Student("Sue Hill", "020215");
    myStudent.save();
    myStudent.addCourse(myCourse);
    Course savedCourse = myStudent.getCourses().get(0);
    assertTrue(myCourse.equals(savedCourse));
  }

  @Test
  public void getCourses_returnsAllCourses_ArrayList() {
    Course myCourse = new Course("biology");
    myCourse.save();
    Student myStudent = new Student("Sue Hill", "020215");
    myStudent.save();
    myStudent.addCourse(myCourse);
    List savedCourses = myStudent.getCourses();
    assertEquals(savedCourses.size(), 1);
  }

  @Test
  public void delete_deletesALlStudentAndListsAssociations() {
    Course myCourse = new Course("biology");
    myCourse.save();

    Student myStudent = new Student("Sue Hill", "020215");
    myStudent.save();

    myStudent.addCourse(myCourse);
    myStudent.delete();
    assertEquals(myCourse.getStudents().size(), 0);
    }
}
