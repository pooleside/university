import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;



public class App {
  public static void main(String[] args) {
   staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
     HashMap<String, Object> model = new HashMap<String, Object>();
     model.put("template", "templates/index.vtl");
     return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());

     get("/students", (request, response) -> {
       HashMap<String, Object> model = new HashMap<String, Object>();
       List<Student> students = Student.all();
       model.put("students", students);
       model.put("template", "templates/students.vtl");
       return new ModelAndView(model, layout);
     }, new VelocityTemplateEngine());

     get("/courses", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      List<Course> courses = Course.all();
      model.put("courses", courses);
      model.put("template", "templates/courses.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

      get("/students/:id", (request,response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        int id = Integer.parseInt(request.params("id"));
        Student student = Student.find(id);
        model.put("student", student);
        model.put("allCourses", Course.all());
        model.put("template", "templates/student.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      get("/courses/:id", (request,response) ->{
        HashMap<String, Object> model = new HashMap<String, Object>();
        int id = Integer.parseInt(request.params("id"));
        Course course = Course.find(id);
        model.put("course", course);
        model.put("allStudents", Student.all());
        model.put("template", "templates/course.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/students", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        String description = request.queryParams("description");
        String enrolldate = request.queryParams("enrolldate");
        Student newStudent = new Student(description, enrolldate);
        newStudent.save();
        response.redirect("/students");
        return null;
      });

      post("/courses", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        String name = request.queryParams("name");
        String coursenum = request.queryParams("coursenum");
        Course newCourse = new Course(name, coursenum);
        newCourse.save();
        response.redirect("/courses");
        return null;
      });

      post("/add_students", (request, response) -> {
        int studentId = Integer.parseInt(request.queryParams("student_id"));
        int courseId = Integer.parseInt(request.queryParams("course_id"));
        Course course = Course.find(courseId);
        Student student = Student.find(studentId);
        course.addStudent(student);
        response.redirect("/courses/" + courseId);
        return null;
      });

      post("/add_courses", (request, response) -> {
        int studentId = Integer.parseInt(request.queryParams("student_id"));
        int courseId = Integer.parseInt(request.queryParams("course_id"));
        Course course = Course.find(courseId);
        Student student = Student.find(studentId);
        student.addCourse(course);
        response.redirect("/students/" + studentId);
        return null;
      });

      post("/students/:studentId/complete", (request, response) -> {
        int studentId = Integer.parseInt(request.queryParams("student_id"));
        int courseId = Integer.parseInt(request.queryParams("course_id"));
        Course course = Course.find(courseId);
        Student student = Student.find(studentId);
        student.markCompleted();
        response.redirect("/courses/" + courseId);
        return null;
      });

     get("/courses/:id/update", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Course course = Course.find(Integer.parseInt(request.params(":id")));
        model.put("course", course);
        model.put("template", "templates/edit-course.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());


      post("/courses/:id/update", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Course course = Course.find(Integer.parseInt(request.params(":id")));
        String name = request.queryParams("name");
        String coursenum = request.queryParams("coursenum");
        course.update(name, coursenum);
        response.redirect("/courses/" + course.getId());
        return null;
      });

       post("/courses/:id/delete", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Course course = Course.find(Integer.parseInt(request.params(":id")));
        course.delete();
        response.redirect("/");
        return null;
         });


      get("/courses/:course_id/students/:id/update", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Course course = Course.find(Integer.parseInt(request.params(":course_id")));
        Student student = Student.find(Integer.parseInt(request.params(":id")));
        model.put("course", course);
        model.put("student", student);
        model.put("template", "templates/edit-student.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/students/:id/update", (request, response) -> {
        int studentId = Integer.parseInt(request.params(":id"));
        Student student = Student.find(studentId);
        String description = request.queryParams("description");
        String enrolldate = request.queryParams("enrolldate");
        student.update(description, enrolldate);
        response.redirect("/students/" + studentId);
        return null;
      });

       get("/students/:id/update", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Student student = Student.find(Integer.parseInt(request.params(":id")));
        model.put("student", student);
        model.put("template", "templates/edit-student.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

       post("/students/:id/delete", (request, response) -> {
        int studentId = Integer.parseInt(request.params(":id"));
        Student student = Student.find(studentId);
        student.delete();
        response.redirect("/students");
        return null;
      });


 }//end of main
}//end app class
