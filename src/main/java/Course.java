import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Course {
  private int id;
  private String name;
  private String coursenum;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCourseNum(){

  }

  public Course (String name, String coursenum) {
    this.name = name;
    this.coursenum = coursenum;
  }

  public static List<Course> all() {
    String sql = "SELECT id, name FROM courses";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Course.class);
    }
  }

  @Override
  public boolean equals(Object otherCourse) {
    if (!(otherCourse instanceof Course)) {
      return false;
    } else {
      Course newCourse = (Course) otherCourse;
      return this.getName().equals(newCourse.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO courses (name, coursenum) VALUES (:name, :coursenum)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("coursenum", this.coursenum)
        .executeUpdate()
        .getKey();
    }
  }

  public static Course find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM courses WHERE id=:id";
      Course course = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Course.class);
        return course;
    }
  }

  public void addStudent(Student student){
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO courses_students (course_id, student_id) VALUES (:course_id, :student_id)";
      con.createQuery(sql)
        .addParameter("course_id", this.getId())
        .addParameter("student_id", student.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Student> getStudents() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT student_id FROM courses_students WHERE course_id = :course_id";
      List<Integer> studentIds = con.createQuery(sql)
        .addParameter("course_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Student> students = new ArrayList<Student>();

      for (Integer studentId : studentIds) {
        String studentQuery = "SELECT * FROM students WHERE id = :studentId";
        Student student = con.createQuery(studentQuery)
          .addParameter("studentId", studentId)
          .executeAndFetchFirst(Student.class);
        students.add(student);
      }
      return students;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM courses WHERE id = :id";
        con.createQuery(deleteQuery)
        .addParameter("id", id)
        .executeUpdate();

      String joinDeleteQuery = "DELETE FROM courses_students WHERE course_id =:courseId";
      con.createQuery(joinDeleteQuery)
      .addParameter("courseId", this.getId())
      .executeUpdate();
    }
  }

    public void update(String name) {
      try(Connection con = DB.sql2o.open()) {
        String sql = "UPDATE courses SET (name, coursenum) = :name, :coursenum WHERE id = :id";
        con.createQuery(sql)
          .addParameter("name", name)
          .addParameter("coursenum", coursenum)
          .addParameter("id", id)
          .executeUpdate();
      }
    }


}
