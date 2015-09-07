import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Student {
  private int id;
  private String description;
  private String enrolldate;


  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getEnrollDate() {
    return enrolldate;
  }

  public Student(String description, String enrolldate) {
    this.description = description;
    this.enrolldate = enrolldate;
  }

  @Override
  public boolean equals(Object otherStudent) {
    if(!(otherStudent instanceof Student)) {
      return false;
    } else {
      Student newStudent = (Student) otherStudent;
      return this.getDescription().equals(newStudent.getDescription()) &&
             this.getId() == newStudent.getId();
    }
  }

  public static List<Student> all() {
    String sql = "SELECT id, description, enrolldate FROM students";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Student.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO students (description, enrolldate) VALUES (:description, :enrolldate)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .addParameter("enrolldate", enrolldate)
        .executeUpdate()
        .getKey();
    }
  }

  public static Student find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM students where id=:id";
      Student student = con.createQuery(sql)
          .addParameter("id", id)
          .executeAndFetchFirst(Student.class);
          return student;
    }
  }

  public void addCourse(Course course) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO courses_students (course_id, student_id) VALUES (:course_id, :student_id)";
      con.createQuery(sql)
        .addParameter("course_id", course.getId())
        .addParameter("student_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Course> getCourses() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT course_id FROM courses_students WHERE student_id = :student_id";
      List<Integer> courseIds = con.createQuery(sql)
        .addParameter("student_id", this.getId())
        .executeAndFetch(Integer.class);

        ArrayList<Course> courses = new ArrayList<Course>();

        for(Integer courseId : courseIds) {
          String studentQuery = "SELECT * FROM courses WHERE id = :courseId";
          Course course = con.createQuery(studentQuery)
            .addParameter("courseId", courseId)
            .executeAndFetchFirst(Course.class);
          courses.add(course);
        }
      return courses;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM students WHERE id = :id";
        con.createQuery(deleteQuery)
        .addParameter("id", id)
        .executeUpdate();

      String joinDeleteQuery = "DELETE FROM courses_students WHERE student_id = :studentId";
        con.createQuery(joinDeleteQuery)
          .addParameter("studentId", this.getId())
          .executeUpdate();
    }
  }

  public void markCompleted() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET completed = true WHERE id = :studentId";
        con.createQuery(sql)
        .addParameter("studentId", this.getId())
        .executeUpdate();
    }
  }




  public void update(String description, String enrolldate) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET description = :description, enrolldate = :enrolldate WHERE id = :id";
      con.createQuery(sql)
        .addParameter("description", description)
        .addParameter("enrolldate", enrolldate)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

}
